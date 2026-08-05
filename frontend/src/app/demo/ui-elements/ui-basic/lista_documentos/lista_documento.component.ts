import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbDropdownModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';

// Models & Services
import { Documento } from 'src/app/modelo/Documento_modelo';
import { DocumentoService } from 'src/app/service/documento_service';
import { ReporteDocumentoService } from 'src/app/service/reporteDocumento_service';

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
  private modalService = inject(NgbModal);

  documento: Documento[] = [];
  documentoOriginal: Documento[] = [];
  documentoPaginado: Documento[] = [];

  // edicion de estado/fechas (modal)
  documentoEnEdicion: Documento = {} as Documento;

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
    this.documentoService.obtenerDocumentoLista().subscribe({
      next: (datos) => {
        this.documentoOriginal = datos.sort((a, b) => (b.id_documento ?? 0) - (a.id_documento ?? 0));
        this.documento = [...this.documentoOriginal];

        this.paginaActual = 1;
        this.actualizarPaginacion();

        this.cd.detectChanges();
      },
      error: (error) => console.error('Error al obtener los documentos:', error)
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

  abrirModalEstado(content: any, documento: Documento): void {
    this.documentoEnEdicion = { ...documento };
    this.modalService.open(content, { centered: true });
  }

  guardarEstado(modal: any): void {
    this.documentoService.actualizarDocumento(this.documentoEnEdicion).subscribe({
      next: (data: Documento) => {
        const index = this.documentoOriginal.findIndex((d) => d.id_documento === data.id_documento);
        if (index !== -1) this.documentoOriginal[index] = data;

        this.documento = [...this.documentoOriginal];
        this.actualizarPaginacion();
        this.cd.detectChanges();
        modal.close();
      },
      error: (err) => {
        console.error('Error al actualizar el documento:', err);
        alert('No se pudo actualizar el documento');
      }
    });
  }

  imprimirDocumento(id: number): void {
    this.reporteDocumentoService.imprimirDocumento(id).subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        window.open(url, '_blank');

        const link = document.createElement('a');
        link.href = url;
        link.download = `Documento_${id}.pdf`;
        link.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al generar el documento:', err);
        alert('No se pudo generar el PDF del documento');
      }
    });
  }
}
