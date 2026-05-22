import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { Factura } from '../modelo/Factura_modelo';
import { environment } from '../../environments/environment';

export interface RespuestaFacturaDto {
  factura: Factura | null;
  warning: string | null;
  requiereAutorizacion: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class FacturaService {
  //private urlBase = "http://localhost:8080/facturas"
   private apiUrl = `${environment.apiUrl}/api/facturas`;


  constructor (private clienteHttp: HttpClient){}

    obtenerFacturaLista(): Observable<Factura[]>{
      //return this.clienteHttp.get<Factura[]>(this.urlBase);
      return this.clienteHttp.get<Factura[]>(this.apiUrl);
    }

    // ✅ Ahora retorna RespuestaFacturaDto en lugar de Factura
  agregarFactura(factura: Factura, autorizado: boolean = false): Observable<RespuestaFacturaDto> {
    const params = new HttpParams().set('autorizado', autorizado.toString());
    //return this.clienteHttp.post<RespuestaFacturaDto>(this.urlBase, factura, { params });
    return this.clienteHttp.post<RespuestaFacturaDto>(this.apiUrl, factura, { params });
  }

    actualizarFactura(factura: Factura) {
    //return this.clienteHttp.put(`${this.urlBase}/${factura.id_factura}`, factura);
    return this.clienteHttp.put(`${this.apiUrl}/${factura.id_factura}`, factura);
    }

  eliminarFactura(id: number): Observable<any> {
    //return this.clienteHttp.delete(`${this.urlBase}/${id}`);
    return this.clienteHttp.delete(`${this.apiUrl}/${id}`);
  }

 anularFactura(id: number): Observable<any> {
  //return this.clienteHttp.patch(`${this.urlBase}/${id}/anular`, {});
  return this.clienteHttp.patch(`${this.apiUrl}/${id}/anular`, {});
}
  obtenerDashboard(): Observable<{dia: number, mes: number, anio: number}> {
  //return this.clienteHttp.get<{dia: number, mes: number, anio: number}>(`${this.urlBase}/dashboard`);
  return this.clienteHttp.get<{dia: number, mes: number, anio: number}>(`${this.apiUrl}/dashboard`);
}
  
}
