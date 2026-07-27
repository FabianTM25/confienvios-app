import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbDropdownModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';

// Models & Services
import { Venta } from 'src/app/modelo/Venta_modelo';
import { VentaService } from 'src/app/service/venta_service';
import { ReporteVentaService } from 'src/app/service/reporteVenta_service';

@Component({
  selector: 'app-lista-venta',
  standalone: true,
  imports: [CommonModule, FormsModule, SharedModule, NgbDropdownModule, RouterModule],
  templateUrl: './lista_venta.component.html',
  styleUrl: './lista_venta.component.scss'
})
export class ListaVentaComponent implements OnInit {
  private cd = inject(ChangeDetectorRef);
  private ventaService = inject(VentaService);
  private modalService = inject(NgbModal);
  private reporteVentaService = inject(ReporteVentaService);

  private modalRef!: NgbModalRef;

  venta: Venta[] = [];
  ventaOriginal: Venta[] = [];
  ventaPaginado: Venta[] = [];

  // busqueda
  textoBusqueda: string = '';
  fechaDesde: string = '';
  fechaHasta: string = '';

  // paginador
  paginaActual: number = 1;
  registrosPorPagina: number = 8;
  totalPaginas: number = 0;

  ventaSeleccionada: Venta = {} as Venta;

  ngOnInit(): void {
    this.obtenerVenta();
  }

  private obtenerVenta(): void {
    this.ventaService.obtenerVentaLista().subscribe({
      next: (datos) => {
        this.ventaOriginal = datos.sort((a, b) => (b.id_venta ?? 0) - (a.id_venta ?? 0));
        this.venta = [...this.ventaOriginal];

        this.paginaActual = 1;
        this.actualizarPaginacion();

        this.cd.detectChanges();
      },
      error: (error) => console.error('Error al obtener las ventas:', error)
    });
  }

  actualizarPaginacion() {
    this.totalPaginas = Math.ceil(this.venta.length / this.registrosPorPagina);

    const inicio = (this.paginaActual - 1) * this.registrosPorPagina;
    const fin = inicio + this.registrosPorPagina;

    this.ventaPaginado = this.venta.slice(inicio, fin);
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

  buscarVenta(event: any) {
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

    this.venta = this.ventaOriginal.filter((v) => {
      const coincideTexto =
        !texto ||
        v.numero_venta?.toString().toLowerCase().includes(texto) ||
        v.numero_factura?.toString().toLowerCase().includes(texto) ||
        v.documento_cliente_rmt?.toLowerCase().includes(texto) ||
        v.nombre_cliente_rmt?.toLowerCase().includes(texto);

      const fechaVenta = v.fecha_creacion ? v.fecha_creacion.substring(0, 10) : '';
      const cumpleDesde = !this.fechaDesde || fechaVenta >= this.fechaDesde;
      const cumpleHasta = !this.fechaHasta || fechaVenta <= this.fechaHasta;

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
    return fin > this.venta.length ? this.venta.length : fin;
  }

  verTicket(id: number) {
    this.reporteVentaService.imprimirTicketVenta(id).subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        window.open(url, '_blank');

        window.URL.revokeObjectURL(url);
      },
      error: (err) => console.error('Error al visualizar el ticket:', err)
    });
  }

  imprimirTicket(id: number) {
    this.reporteVentaService.imprimirTicketVenta(id).subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        window.open(url, '_blank');

        const link = document.createElement('a');
        link.href = url;
        link.download = `Venta_${id}.pdf`;
        link.click();

        window.URL.revokeObjectURL(url);
        this.modalService.dismissAll();
      },
      error: (err) => console.error('Error al imprimir ticket:', err)
    });
  }

  anularVenta(venta: Venta) {
    if (confirm(`¿Está seguro de ANULAR la venta ${venta.numero_venta}?`)) {
      if (!venta.id_venta) return;

      this.ventaService.anularVenta(venta.id_venta).subscribe({
        next: () => {
          const index = this.ventaOriginal.findIndex((v) => v.id_venta === venta.id_venta);
          if (index !== -1) this.ventaOriginal[index].estado = '2';

          this.venta = [...this.ventaOriginal];
          this.actualizarPaginacion();
          this.cd.detectChanges();
        },
        error: (err) => console.error('Error al anular:', err)
      });
    }
  }

  abrirModalImprimir(content: any, venta: Venta) {
    this.ventaSeleccionada = venta;
    this.modalService.open(content, {
      backdrop: 'static',
      keyboard: false,
      centered: true,
      size: 'm'
    });
  }

  abrirModalAnular(content: any, venta: Venta) {
    this.ventaSeleccionada = venta;
    this.modalService.open(content, { centered: true });
  }
}
