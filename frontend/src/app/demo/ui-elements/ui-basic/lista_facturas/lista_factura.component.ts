import { ChangeDetectorRef, Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbDropdownModule, NgbModal,NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { Field, form, minLength, required } from '@angular/forms/signals';

// Models & Services
import { Factura } from 'src/app/modelo/Factura_modelo';
import { FacturaService } from 'src/app/service/factura_service';
import { ReporteService } from 'src/app/service/reporteFactura_service';

@Component({
  selector: 'app-factura',
  standalone: true, // Importante si usas la propiedad 'imports' dentro del componente
  imports: [CommonModule, FormsModule, SharedModule, NgbDropdownModule, RouterModule],
  templateUrl: './lista_factura.component.html',
  styleUrl: './lista_factura.component.scss'
})
export class BasicClientesComponent implements OnInit{
 
  private cd = inject(ChangeDetectorRef);
  private Factura_Service = inject(FacturaService);
  private modalService = inject(NgbModal);
  //imprimir
  private reporteService = inject(ReporteService);


     //----------------------------Formulario modal-----------------------------------

   // Referencias y Estados
  private modalRef!: NgbModalRef;
  factura: Factura[] = [];
  facturaOriginal: Factura[] = [];
  facturaPaginado: Factura[] = [];
  
  //busqueda
  textoBusqueda: string = '';
  fechaDesde: string = '';
  fechaHasta: string = '';
  submitted = signal(false);

  //paginador
  paginaActual: number = 1;
  registrosPorPagina: number = 8;
  totalPaginas: number = 0;

  idRotulo!: number;
  idFactura!: number;
    
  // Modelo inicializado como Signal
  ngOnInit(): void {
    this.obtenerFactura();
  }

 // registerForm

  // Método para abrir el modal
  openModal(content: any): void {
    this.modalService.open(content, {
      backdrop: 'static',
      keyboard: false,
      centered: true,
      size: 'm'
    });

  }
  abrirModalRotulo(content: any, id: number) {
  this.idRotulo = id;
  this.modalService.open(content);
}
abrirModalFactura(content: any, id: number) {
  this.idFactura = id;
  this.modalService.open(content, {
    backdrop: 'static',
    keyboard: false,
    centered: true,
    size: 'm'
  });
}

  // Si necesitas el método open que mencionaste arriba
  open(content: any) {
    this.openModal(content);
  }
  /**
   * Limpia el estado tras una operación exitosa
   */
  private finalizarGuardado() {
    console.log("Operación exitosa");
    this.modalRef.close();
    this.obtenerFactura(); // Refrescar tabla
  }
    
  ///funcion de paginacion
  actualizarPaginacion() {

  this.totalPaginas = Math.ceil(this.factura.length / this.registrosPorPagina);

  const inicio = (this.paginaActual - 1) * this.registrosPorPagina;
  const fin = inicio + this.registrosPorPagina;

  this.facturaPaginado = this.factura.slice(inicio, fin);
}
//cambiar la pagina
cambiarPagina(pagina: number) {

  if (pagina < 1 || pagina > this.totalPaginas) return;

  this.paginaActual = pagina;
  this.actualizarPaginacion();
}

//paginas a mostrar en el paginador (con "..." para rangos largos)
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

 //---------------------------- * Obtiene la lista de clientes desde el servicio-------------------------------
//obtener cliente
private obtenerFactura(): void {
  this.Factura_Service.obtenerFacturaLista().subscribe({
    next: (datos) => {

      this.facturaOriginal = datos.sort(
        (a, b) => (b.id_factura ?? 0) - (a.id_factura ?? 0)
      );

      this.factura = [...this.facturaOriginal];

      this.paginaActual = 1;
      this.actualizarPaginacion();

      this.cd.detectChanges();
    },
    error: (error) => console.error("Error al obtener los usuarios:", error)
  });
}
//buscador entre paginas
buscarFactura(event: any) {
  this.textoBusqueda = event.target.value.toLowerCase();
  this.aplicarFiltros();
}

//filtro por rango de fechas (usa fecha_creacion, formato ISO yyyy-MM-dd...)
filtrarPorFecha() {
  this.aplicarFiltros();
}

limpiarFiltroFecha() {
  this.fechaDesde = '';
  this.fechaHasta = '';
  this.aplicarFiltros();
}

//aplica busqueda por texto y rango de fechas en conjunto
aplicarFiltros() {
  const texto = this.textoBusqueda;

  this.factura = this.facturaOriginal.filter(f => {
    const coincideTexto =
      !texto ||
      f.numero_factura?.toString().toLowerCase().includes(texto) ||
      f.documento_cliente_dto?.toLowerCase().includes(texto) ||
      f.documento_cliente_rmt?.toLowerCase().includes(texto) ||
      f.nombre_cliente_rmt?.toLowerCase().includes(texto) ||
      f.nombre_cliente_dto?.toLowerCase().includes(texto);

    const fechaFactura = f.fecha_creacion ? f.fecha_creacion.substring(0, 10) : '';
    const cumpleDesde = !this.fechaDesde || fechaFactura >= this.fechaDesde;
    const cumpleHasta = !this.fechaHasta || fechaFactura <= this.fechaHasta;

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
  return fin > this.factura.length ? this.factura.length : fin;
}

imprimirRotulo(id: number) {
  this.reporteService.imprimirRotulo(id)
    .subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        // ✅ Abre en nueva pestaña
        window.open(url, '_blank');

        // ✅ Descarga
        const link = document.createElement('a');
        link.href = url;
        link.download = `Rotulo_${id}.pdf`;
        link.click();

        window.URL.revokeObjectURL(url);
        this.modalService.dismissAll();
      },
      error: (err) => console.error('Error al imprimir rótulo:', err)
    });
}

imprimirCorrespondencia(id: number) {
  this.reporteService.imprimirCorrespondencia(id)
    .subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        window.open(url, '_blank');

        const link = document.createElement('a');
        link.href = url;
        link.download = `Correspondencia_${id}.pdf`;
        link.click();

        window.URL.revokeObjectURL(url);
        this.modalService.dismissAll();
      },
      error: (err) => {
        console.error('Error al imprimir correspondencia:', err);
        this.mensajeError = 'No se pudo generar la correspondencia';
      }
    });
}

imprimirFactura(id: number) {
  this.reporteService.imprimirFactura(id)
    .subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        // ✅ Abre en nueva pestaña
        window.open(url, '_blank');

        // ✅ Descarga
        const link = document.createElement('a');
        link.href = url;
        link.download = `Factura_${id}.pdf`;
        link.click();

        window.URL.revokeObjectURL(url);
        this.modalService.dismissAll();
      },
      error: (err) => {
        console.error('Error al imprimir factura:', err);
        this.mensajeError = 'No se pudo generar la factura';
      }
    });
}
anularFactura(factura: Factura) {
  if (confirm(`¿Está seguro de ANULAR la factura ${factura.numero_factura}?`)) {
    
    // ✅ Validar que el id no sea null antes de continuar
    if (!factura.id_factura) return;

    this.Factura_Service.anularFactura(factura.id_factura).subscribe({
      next: () => {
        const index = this.facturaOriginal.findIndex(f => f.id_factura === factura.id_factura);
        if (index !== -1) this.facturaOriginal[index].estado = '2';

        this.factura = [...this.facturaOriginal];
        this.actualizarPaginacion();
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error al anular:', err)
    });
  }
}

facturaSeleccionada: Factura = {} as Factura;

abrirModalAnular(content: any, factura: Factura) {
  this.facturaSeleccionada = factura;
  this.modalService.open(content, { centered: true });
}

// Edicion de datos secundarios (contacto/ruta). El nombre y documento de los
// clientes se corrigen desde el modulo Clientes, no aqui, para no desincronizar
// esos datos maestros.
facturaEnEdicion: Factura = {} as Factura;

abrirModalEditar(content: any, factura: Factura) {
  this.facturaEnEdicion = { ...factura };
  this.modalService.open(content, { backdrop: 'static', keyboard: false, centered: true });
}

guardarEdicion(modal: any) {
  if (!this.facturaEnEdicion.id_factura) return;

  this.Factura_Service.actualizarFactura(this.facturaEnEdicion).subscribe({
    next: (data: any) => {
      const index = this.facturaOriginal.findIndex((f) => f.id_factura === data.id_factura);
      if (index !== -1) this.facturaOriginal[index] = data;

      this.factura = [...this.facturaOriginal];
      this.actualizarPaginacion();
      this.cd.detectChanges();
      modal.close();
    },
    error: (err) => {
      console.error('Error al actualizar la factura:', err);
      alert('No se pudo actualizar la factura');
    }
  });
}

mensajeError: string = '';

} // <--- Aquí termina la clase del componente