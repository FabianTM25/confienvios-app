import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbDropdownModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';

// Models & Services
import { Venta } from 'src/app/modelo/Venta_modelo';
import { Factura } from 'src/app/modelo/Factura_modelo';
import { RangoPeso } from 'src/app/modelo/RangoPeso_modelo';
import { Configuracion } from 'src/app/modelo/Configuracion_modelo';
import { VentaService } from 'src/app/service/venta_service';
import { ConfiguracionService } from 'src/app/service/configuracion_service';
import { ReporteVentaService } from 'src/app/service/reporteVenta_service';

@Component({
  selector: 'app-venta',
  standalone: true,
  imports: [CommonModule, SharedModule, NgbDropdownModule, FormsModule],
  templateUrl: './venta.component.html',
  styleUrls: ['./venta.component.scss']
})
export class VentaComponent implements OnInit {
  private cd = inject(ChangeDetectorRef);
  private ventaService = inject(VentaService);
  private configuracionService = inject(ConfiguracionService);
  private reporteVentaService = inject(ReporteVentaService);
  private modalService = inject(NgbModal);

  private modalRef!: NgbModalRef;

  // Busqueda de factura
  numeroFactura: string = '';
  facturaEncontrada: Factura | null = null;
  errorBusqueda: string = '';

  // Datos de la venta
  peso: number | null = null;
  conCaja: boolean = false;
  observaciones: string = '';
  valorAvaluo: number | null = null;
  formaPago: string = 'Efectivo';

  // Configuracion (cargada para el preview y el panel de configuracion)
  rangos: RangoPeso[] = [];
  configuracion: Configuracion = { id_configuracion: null, precio_caja: 0 };

  // Preview (solo UX, el valor real siempre lo calcula el servidor al guardar)
  previewValorPeso: number | null = null;
  previewValorEnvio: number | null = null;
  previewError: string = '';

  // Resultado tras guardar
  ventaGuardada: Venta | null = null;

  // Panel de configuracion
  nuevoRango: RangoPeso = { id_rango: null, peso_desde: 0, peso_hasta: 0, valor: 0 };

  ngOnInit(): void {
    this.cargarConfiguracion();
    this.cargarRangos();
  }

  private cargarConfiguracion(): void {
    this.configuracionService.obtenerConfiguracion().subscribe({
      next: (data) => {
        this.configuracion = data;
        this.cd.detectChanges();
      },
      error: (error) => console.error('Error al obtener la configuración:', error)
    });
  }

  private cargarRangos(): void {
    this.configuracionService.listarRangos().subscribe({
      next: (data) => {
        this.rangos = data;
        this.cd.detectChanges();
      },
      error: (error) => console.error('Error al obtener los rangos de peso:', error)
    });
  }

  buscarFactura(): void {
    this.errorBusqueda = '';
    this.facturaEncontrada = null;

    if (!this.numeroFactura) {
      alert('Ingrese el número de factura');
      return;
    }

    this.ventaService.buscarFacturaPorNumero(this.numeroFactura).subscribe({
      next: (data: Factura) => {
        this.facturaEncontrada = data;
        this.cd.detectChanges();
      },
      error: (error) => {
        console.error(error);
        this.errorBusqueda = 'No se encontró una factura con ese número';
        this.cd.detectChanges();
      }
    });
  }

  // Recalcula el preview en vivo cada vez que cambia el peso o la casilla "Con caja"
  calcularPreview(): void {
    this.previewError = '';
    this.previewValorPeso = null;
    this.previewValorEnvio = null;

    if (this.peso === null || this.peso <= 0) {
      return;
    }

    const rango = this.rangos.find((r) => this.peso! >= r.peso_desde && this.peso! <= r.peso_hasta);

    if (!rango) {
      this.previewError = 'No hay un rango configurado para ese peso';
      return;
    }

    this.previewValorPeso = rango.valor;
    this.previewValorEnvio = rango.valor + (this.conCaja ? this.configuracion.precio_caja : 0);
  }

  guardar(content: any): void {
    if (!this.facturaEncontrada?.id_factura) {
      alert('Debe buscar una factura válida antes de guardar');
      return;
    }

    if (this.peso === null || this.peso <= 0) {
      alert('Ingrese el peso');
      return;
    }

    this.ventaService
      .agregarVenta(this.numeroFactura, this.peso, this.conCaja, this.observaciones, this.valorAvaluo, this.formaPago)
      .subscribe({
      next: (data: Venta) => {
        this.ventaGuardada = data;
        this.modalService.open(content, {
          backdrop: 'static',
          keyboard: false,
          centered: true,
          size: 'm'
        });
      },
      error: (error) => {
        console.error(error);
        const mensaje = error.error?.error || 'Error al guardar la venta';
        alert(mensaje);
      }
    });
  }

  imprimirTicket(id: number): void {
    this.reporteVentaService.imprimirTicketVenta(id).subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);
        window.open(url, '_blank');
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al imprimir ticket:', err);
        alert('No se pudo generar el ticket (verifique que el reporte VentaTicket.jasper ya esté cargado)');
      }
    });
  }

  limpiarFormulario(): void {
    this.numeroFactura = '';
    this.facturaEncontrada = null;
    this.errorBusqueda = '';
    this.peso = null;
    this.conCaja = false;
    this.observaciones = '';
    this.valorAvaluo = null;
    this.formaPago = 'Efectivo';
    this.previewValorPeso = null;
    this.previewValorEnvio = null;
    this.previewError = '';
    this.ventaGuardada = null;
  }

  // ---------------- Panel de configuracion ----------------

  abrirConfiguracion(content: any): void {
    this.modalService.open(content, { centered: true, size: 'lg' });
  }

  guardarConfiguracionGeneral(): void {
    this.configuracionService.actualizarConfiguracion(this.configuracion).subscribe({
      next: (data) => {
        this.configuracion = data;
        this.calcularPreview();
        this.cd.detectChanges();
      },
      error: (error) => console.error('Error al actualizar la configuración:', error)
    });
  }

  guardarRango(rango: RangoPeso): void {
    const peticion = rango.id_rango
      ? this.configuracionService.actualizarRango(rango)
      : this.configuracionService.crearRango(rango);

    peticion.subscribe({
      next: () => {
        this.cargarRangos();
        this.calcularPreview();
      },
      error: (error) => console.error('Error al guardar el rango:', error)
    });
  }

  agregarRango(): void {
    if (this.nuevoRango.peso_hasta < this.nuevoRango.peso_desde) {
      alert('El peso "hasta" debe ser mayor o igual al peso "desde"');
      return;
    }

    this.configuracionService.crearRango(this.nuevoRango).subscribe({
      next: () => {
        this.nuevoRango = { id_rango: null, peso_desde: 0, peso_hasta: 0, valor: 0 };
        this.cargarRangos();
      },
      error: (error) => console.error('Error al crear el rango:', error)
    });
  }

  eliminarRango(id: number): void {
    if (!confirm('¿Eliminar este rango de peso?')) return;

    this.configuracionService.eliminarRango(id).subscribe({
      next: () => {
        this.cargarRangos();
        this.calcularPreview();
      },
      error: (error) => console.error('Error al eliminar el rango:', error)
    });
  }
}
