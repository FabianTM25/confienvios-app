// angular import
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// project import
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { FacturaService } from 'src/app/service/factura_service';
import { VentaService } from 'src/app/service/venta_service';
import { DocumentoService } from 'src/app/service/documento_service';
import { MaterialService } from 'src/app/service/material_service';
import { Venta } from 'src/app/modelo/Venta_modelo';
import { Documento } from 'src/app/modelo/Documento_modelo';
import { Material } from 'src/app/modelo/Material_modelo';

declare const AmCharts: any;

import '../../../assets/charts/amchart/amcharts.js';
import '../../../assets/charts/amchart/gauge.js';
import '../../../assets/charts/amchart/serial.js';
import '../../../assets/charts/amchart/light.js';
import '../../../assets/charts/amchart/pie.min.js';
import '../../../assets/charts/amchart/ammap.min.js';
import '../../../assets/charts/amchart/usaLow.js';
import '../../../assets/charts/amchart/radar.js';
import '../../../assets/charts/amchart/worldLow.js';
// @ts-ignore
import dataJson from 'src/fake-data/map_data';
// @ts-ignore
import mapColor from 'src/fake-data/map-color-data.json';




@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, SharedModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

   private facturaService = inject(FacturaService);
   private ventaService = inject(VentaService);
   private documentoService = inject(DocumentoService);
   private materialService = inject(MaterialService);

   // Datos del dashboard
  facturasDia: number = 0;
  facturasMes: number = 0;
  facturasAnio: number = 0;

  // Filtro de fechas compartido por Ventas, Correspondencia y Materiales
  fechaDesde: string = '';
  fechaHasta: string = '';

  // Listas completas sin filtrar (se guardan para poder recalcular al cambiar el filtro)
  private ventasOriginal: Venta[] = [];
  private documentosOriginal: Documento[] = [];
  private materialesOriginal: Material[] = [];

  // Resumen de ventas (excluye anuladas)
  ventasResumen: { cantidad: number; total: number; porFormaPago: { forma: string; cantidad: number }[] } = {
    cantidad: 0,
    total: 0,
    porFormaPago: []
  };

  // Resumen de correspondencia y materiales, por estado de envío
  correspondenciaResumen: { total: number; porEstado: Record<string, number> } = {
    total: 0,
    porEstado: { PENDIENTE: 0, ENVIADO: 0, ENTREGADO: 0, CANCELADO: 0 }
  };

  materialesResumen: { total: number; porEstado: Record<string, number> } = {
    total: 0,
    porEstado: { PENDIENTE: 0, ENVIADO: 0, ENTREGADO: 0, CANCELADO: 0 }
  };

  sales = [
    {
      title: 'Diario',
      icon: 'icon-arrow-up text-c-green',
      amount: '0',         // se actualiza dinámicamente
      percentage: 'Hoy',
      progress: 0,
      design: 'col-md-6',
      progress_bg: 'progress-c-theme'
    },
    {
      title: 'Mes',
      icon: 'icon-arrow-up text-c-green',
      amount: '0',
      percentage: 'Este mes',
      progress: 0,
      design: 'col-md-6',
      progress_bg: 'progress-c-theme2'
    },
    {
      title: 'Año',
      icon: 'icon-arrow-up text-c-green',
      amount: '0',
      percentage: 'Este año',
      progress: 0,
      design: 'col-md-12',
      progress_bg: 'progress-c-theme'
    }
  ];




  // life cycle event
  ngOnInit() {
    this.cargarDashboard();
    this.cargarVentas();
    this.cargarCorrespondenciaMateriales();

    setTimeout(() => {
      const latlong = dataJson;
      const mapData = mapColor;
      const minBulletSize = 3;
      const maxBulletSize = 70;
      let min = Infinity;
      let max = -Infinity;
      let i;
      let value;

      for (i = 0; i < mapData.length; i++) {
        value = mapData[i].value;
        if (value < min) {
          min = value;
        }
        if (value > max) {
          max = value;
        }
      }

      const maxSquare = maxBulletSize * maxBulletSize * 2 * Math.PI;
      const minSquare = minBulletSize * minBulletSize * 2 * Math.PI;
      const images = [];

     for (i = 0; i < mapData.length; i++) {
        const dataItem = mapData[i];
        value = dataItem.value;
        let square = ((value - min) / (max - min)) * (maxSquare - minSquare) + minSquare;
        if (square < minSquare) square = minSquare;
        const size = Math.sqrt(square / (Math.PI * 8));
        const id = dataItem.code;
        images.push({
          type: 'circle',
          theme: 'light',
          width: size,
          height: size,
          color: dataItem.color,
          longitude: latlong[id].longitude,
          latitude: latlong[id].latitude,
          title: dataItem.name + '</br> [ ' + value + ' ]',
          value: value
        });
      }

      // world-low chart
      AmCharts.makeChart('world-low', {
        type: 'map',
        projection: 'eckert6',
        dataProvider: { map: 'worldLow', images: images },
        export: { enabled: true }
      });

      const chartDatac = [
        { day: 'Mon', value: 60 },
        { day: 'Tue', value: 45 },
        { day: 'Wed', value: 70 },
        { day: 'Thu', value: 55 },
        { day: 'Fri', value: 70 },
        { day: 'Sat', value: 55 },
        { day: 'Sun', value: 70 }
      ];

      setTimeout(() => {
        AmCharts.makeChart('widget-line-chart', {
          type: 'serial',
          // ... tu config existente del chart
          dataProvider: chartDatac,
        });
      }, 500);

    }, 500);
  }

  private cd = inject(ChangeDetectorRef);

cargarDashboard(): void {
  this.facturaService.obtenerDashboard().subscribe({
    next: (datos) => {
      this.sales[0].amount = datos.dia + ' facturas';
      this.sales[0].progress = Math.min(datos.dia * 10, 100);
      this.sales[1].amount = datos.mes + ' facturas';
      this.sales[1].progress = Math.min(datos.mes * 3, 100);
      this.sales[2].amount = datos.anio + ' facturas';
      this.sales[2].progress = Math.min(datos.anio, 100);

      this.cd.detectChanges(); // ✅ fuerza actualización
    },
    error: (err) => console.error('Error cargando dashboard:', err)
  });
}

  cargarVentas(): void {
    this.ventaService.obtenerVentaLista().subscribe({
      next: (ventas) => {
        this.ventasOriginal = ventas;
        this.actualizarResumenVentas();
      },
      error: (err) => console.error('Error cargando resumen de ventas:', err)
    });
  }

  cargarCorrespondenciaMateriales(): void {
    this.documentoService.obtenerDocumentoLista().subscribe({
      next: (documentos) => {
        this.documentosOriginal = documentos;
        this.actualizarResumenCorrespondencia();
      },
      error: (err) => console.error('Error cargando resumen de correspondencia:', err)
    });

    this.materialService.obtenerMaterialLista().subscribe({
      next: (materiales) => {
        this.materialesOriginal = materiales;
        this.actualizarResumenMateriales();
      },
      error: (err) => console.error('Error cargando resumen de materiales:', err)
    });
  }

  // El filtro compara solo la parte de fecha (yyyy-MM-dd) de fecha_creacion contra Desde/Hasta
  private dentroDelRango(fecha?: string): boolean {
    if (!fecha) return !this.fechaDesde && !this.fechaHasta;

    const soloFecha = fecha.substring(0, 10);
    const cumpleDesde = !this.fechaDesde || soloFecha >= this.fechaDesde;
    const cumpleHasta = !this.fechaHasta || soloFecha <= this.fechaHasta;

    return cumpleDesde && cumpleHasta;
  }

  aplicarFiltroFechas(): void {
    this.actualizarResumenVentas();
    this.actualizarResumenCorrespondencia();
    this.actualizarResumenMateriales();
  }

  limpiarFiltroFechas(): void {
    this.fechaDesde = '';
    this.fechaHasta = '';
    this.aplicarFiltroFechas();
  }

  private actualizarResumenVentas(): void {
    const activas = this.ventasOriginal.filter((v) => v.estado !== '2' && this.dentroDelRango(v.fecha_creacion));

    const conteoPorForma = new Map<string, number>();
    for (const v of activas) {
      const forma = v.forma_pago && v.forma_pago.trim() ? v.forma_pago : 'Sin especificar';
      conteoPorForma.set(forma, (conteoPorForma.get(forma) ?? 0) + 1);
    }

    this.ventasResumen = {
      cantidad: activas.length,
      total: activas.reduce((suma, v) => suma + (v.valor_envio ?? 0), 0),
      porFormaPago: Array.from(conteoPorForma, ([forma, cantidad]) => ({ forma, cantidad }))
    };

    this.cd.detectChanges();
  }

  private contarPorEstado(registros: { estado_envio?: string }[]): Record<string, number> {
    const porEstado: Record<string, number> = { PENDIENTE: 0, ENVIADO: 0, ENTREGADO: 0, CANCELADO: 0 };

    for (const r of registros) {
      const estado = r.estado_envio ?? 'PENDIENTE';
      porEstado[estado] = (porEstado[estado] ?? 0) + 1;
    }

    return porEstado;
  }

  private actualizarResumenCorrespondencia(): void {
    const filtrados = this.documentosOriginal.filter((d) => this.dentroDelRango(d.fecha_creacion));

    this.correspondenciaResumen = {
      total: filtrados.length,
      porEstado: this.contarPorEstado(filtrados)
    };

    this.cd.detectChanges();
  }

  private actualizarResumenMateriales(): void {
    const filtrados = this.materialesOriginal.filter((m) => this.dentroDelRango(m.fecha_creacion));

    this.materialesResumen = {
      total: filtrados.length,
      porEstado: this.contarPorEstado(filtrados)
    };

    this.cd.detectChanges();
  }
}

  