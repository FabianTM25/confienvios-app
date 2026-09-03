import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbDropdownModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { forkJoin, Observable } from 'rxjs';

// Models & Services
import { DocumentoService } from 'src/app/service/documento_service';
import { ReporteDocumentoService } from 'src/app/service/reporteDocumento_service';
import { MaterialService } from 'src/app/service/material_service';
import { ReporteMaterialService } from 'src/app/service/reporteMaterial_service';

@Component({
  selector: 'app-lista-documento',
  standalone: true,
  imports: [CommonModule, FormsModule, SharedModule, NgbDropdownModule, RouterModule],
  templateUrl: './lista_documento.component.html',
  styleUrl: './lista_documento.component.scss'
})
export class ListaDocumentoComponent implements OnInit {
  private cd = inject(ChangeDetectorRef);
  private documentoService = inject(DocumentoService);
  private reporteDocumentoService = inject(ReporteDocumentoService);
  private materialService = inject(MaterialService);
  private reporteMaterialService = inject(ReporteMaterialService);
  private modalService = inject(NgbModal);

  // Lista combinada de documentos (correspondencia, tabla "documento") y materiales
  // (tabla "material", numeracion propia). Cada fila lleva _origen/_id para saber
  // a que servicio/tabla enrutar las acciones de editar e imprimir.
  documento: any[] = [];
  documentoOriginal: any[] = [];
  documentoPaginado: any[] = [];

  // edicion de estado/fechas (modal)
  documentoEnEdicion: any = {};

  // busqueda
  textoBusqueda: string = '';
  fechaDesde: string = '';
  fechaHasta: string = '';

  // paginador
  paginaActual: number = 1;
  registrosPorPagina: number = 8;
  totalPaginas: number = 0;

  ngOnInit(): void {
    this.obtenerDocumentos();
  }

  private obtenerDocumentos(): void {
    forkJoin({
      documentos: this.documentoService.obtenerDocumentoLista(),
      materiales: this.materialService.obtenerMaterialLista()
    }).subscribe({
      next: ({ documentos, materiales }) => {
        const docs = documentos.map((d: any) => ({ ...d, _origen: 'documento', _id: d.id_documento }));
        const mats = materiales.map((m: any) => ({ ...m, _origen: 'material', _id: m.id_material }));

        this.documentoOriginal = [...docs, ...mats].sort((a, b) =>
          (b.fecha_creacion ?? '').localeCompare(a.fecha_creacion ?? '')
        );
        this.documento = [...this.documentoOriginal];

        this.paginaActual = 1;
        this.actualizarPaginacion();

        this.cd.detectChanges();
      },
      error: (error) => console.error('Error al obtener los registros:', error)
    });
  }

  actualizarPaginacion() {
    this.totalPaginas = Math.ceil(this.documento.length / this.registrosPorPagina);

    const inicio = (this.paginaActual - 1) * this.registrosPorPagina;
    const fin = inicio + this.registrosPorPagina;

    this.documentoPaginado = this.documento.slice(inicio, fin);
  }

  cambiarPagina(pagina: number) {
    if (pagina < 1 || pagina > this.totalPaginas) return;

    this.paginaActual = pagina;
    this.actualizarPaginacion();
  }

  paginasVisibles(): number[] {
    const total = this.totalPaginas;
    const actual = this.paginaActual;
    const rango = 2;
    const paginas: number[] = [];

    for (let i = 1; i <= total; i++) {
      if (i === 1 || i === total || (i >= actual - rango && i <= actual + rango)) {
        paginas.push(i);
      } else if (paginas[paginas.length - 1] !== -1) {
        paginas.push(-1);
      }
    }

    return paginas;
  }

  buscarDocumento(event: any) {
    this.textoBusqueda = event.target.value.toLowerCase();
    this.aplicarFiltros();
  }

  filtrarPorFecha() {
    this.aplicarFiltros();
  }

  limpiarFiltroFecha() {
    this.fechaDesde = '';
    this.fechaHasta = '';
    this.aplicarFiltros();
  }

  aplicarFiltros() {
    const texto = this.textoBusqueda;

    this.documento = this.documentoOriginal.filter((d) => {
      const coincideTexto =
        !texto ||
        d.numero_radicado?.toLowerCase().includes(texto) ||
        d.nombre_cliente_rmt?.toLowerCase().includes(texto) ||
        d.documento_cliente_rmt?.toLowerCase().includes(texto) ||
        d.tipo_documento?.toLowerCase().includes(texto);

      const fechaDocumento = d.fecha_creacion ? d.fecha_creacion.substring(0, 10) : '';
      const cumpleDesde = !this.fechaDesde || fechaDocumento >= this.fechaDesde;
      const cumpleHasta = !this.fechaHasta || fechaDocumento <= this.fechaHasta;

      return coincideTexto && cumpleDesde && cumpleHasta;
    });

    this.paginaActual = 1;
    this.actualizarPaginacion();
  }

  obtenerInicio(): number {
    return (this.paginaActual - 1) * this.registrosPorPagina + 1;
  }

  obtenerFin(): number {
    const fin = this.paginaActual * this.registrosPorPagina;
    return fin > this.documento.length ? this.documento.length : fin;
  }

  abrirModalEstado(content: any, registro: any): void {
    this.documentoEnEdicion = { ...registro };
    this.modalService.open(content, { centered: true });
  }

  guardarEstado(modal: any): void {
    this.guardarRegistro(this.documentoEnEdicion, {
      onExito: () => modal.close(),
      onError: () => alert('No se pudo actualizar el registro')
    });
  }

  cancelarRegistro(registro: any): void {
    if (!confirm(`¿Está seguro de CANCELAR el radicado ${registro.numero_radicado}?`)) return;

    this.guardarRegistro(
      { ...registro, estado_envio: 'CANCELADO' },
      { onError: () => alert('No se pudo cancelar el registro') }
    );
  }

  // Actualiza un Documento o Material (segun _origen) y refresca la fila en la lista local.
  private guardarRegistro(registro: any, callbacks: { onExito?: () => void; onError?: () => void }): void {
    const esMaterial = registro._origen === 'material';

    // El backend no conoce _origen/_id (son solo para enrutar en el frontend)
    const { _origen, _id, ...payload } = registro;

    const peticion: Observable<any> = esMaterial
      ? this.materialService.actualizarMaterial(payload)
      : this.documentoService.actualizarDocumento(payload);

    peticion.subscribe({
      next: (data: any) => {
        const actualizado = {
          ...data,
          _origen: esMaterial ? 'material' : 'documento',
          _id: esMaterial ? data.id_material : data.id_documento
        };

        const index = this.documentoOriginal.findIndex(
          (d) => d._origen === actualizado._origen && d._id === actualizado._id
        );
        if (index !== -1) this.documentoOriginal[index] = actualizado;

        this.documento = [...this.documentoOriginal];
        this.actualizarPaginacion();
        this.cd.detectChanges();
        callbacks.onExito?.();
      },
      error: (err) => {
        console.error('Error al actualizar el registro:', err);
        callbacks.onError?.();
      }
    });
  }

  verVistaPrevia(registro: any): void {
    const esMaterial = registro._origen === 'material';
    const peticion = esMaterial
      ? this.reporteMaterialService.imprimirMaterial(registro._id)
      : this.reporteDocumentoService.imprimirDocumento(registro._id);

    peticion.subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        window.open(url, '_blank');

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al generar la vista previa:', err);
        alert('No se pudo generar la vista previa');
      }
    });
  }

  imprimirDocumento(registro: any): void {
    const esMaterial = registro._origen === 'material';
    const peticion = esMaterial
      ? this.reporteMaterialService.imprimirMaterial(registro._id)
      : this.reporteDocumentoService.imprimirDocumento(registro._id);

    peticion.subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        window.open(url, '_blank');

        const link = document.createElement('a');
        link.href = url;
        link.download = `${esMaterial ? 'Material' : 'Documento'}_${registro._id}.pdf`;
        link.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al generar el PDF:', err);
        alert('No se pudo generar el PDF');
      }
    });
  }
}
