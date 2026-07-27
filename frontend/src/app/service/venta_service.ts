import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Venta } from '../modelo/Venta_modelo';
import { Factura } from '../modelo/Factura_modelo';
import { environment } from '../../../src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VentaService {
  private urlBase = `${environment.apiUrl}/api/ventas`;

  constructor(private clienteHttp: HttpClient) {}

  obtenerVentaLista(): Observable<Venta[]> {
    return this.clienteHttp.get<Venta[]>(this.urlBase);
  }

  buscarFacturaPorNumero(numero: string): Observable<Factura> {
    return this.clienteHttp.get<Factura>(`${this.urlBase}/buscar-factura/${numero}`);
  }

  agregarVenta(
    numero_factura: string,
    peso: number,
    con_caja: boolean,
    observaciones: string,
    valor_avaluo: number | null
  ): Observable<Venta> {
    return this.clienteHttp.post<Venta>(this.urlBase, {
      numero_factura,
      peso,
      con_caja,
      observaciones,
      valor_avaluo,
    });
  }

  eliminarVenta(id: number): Observable<any> {
    return this.clienteHttp.delete(`${this.urlBase}/${id}`);
  }

  anularVenta(id: number): Observable<any> {
    return this.clienteHttp.patch(`${this.urlBase}/${id}/anular`, {});
  }
}
