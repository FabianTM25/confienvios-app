// angular import
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

// project import
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { FacturaService } from 'src/app/service/factura_service';

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
  imports: [CommonModule, SharedModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

   private facturaService = inject(FacturaService);

   // Datos del dashboard
  facturasDia: number = 0;
  facturasMes: number = 0;
  facturasAnio: number = 0;

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
}

  