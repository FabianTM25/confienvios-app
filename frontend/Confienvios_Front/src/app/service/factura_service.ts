import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { Factura } from '../modelo/Factura_modelo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FacturaService {
  //private urlBase = "http://localhost:8080/facturas"

  private urlBase = `${environment.apiUrl}/facturas`;



  constructor (private clienteHttp: HttpClient){}

    obtenerFacturaLista(): Observable<Factura[]>{
      return this.clienteHttp.get<Factura[]>(this.urlBase);
    }

   agregarFactura(factura: Factura): Observable<Factura> {
    return this.clienteHttp.post<Factura>(this.urlBase, factura);
    }

    actualizarFactura(factura: Factura) {
    return this.clienteHttp.put(`${this.urlBase}/${factura.id_factura}`, factura);
    }

  eliminarFactura(id: number): Observable<any> {
    return this.clienteHttp.delete(`${this.urlBase}/${id}`);
  }

 anularFactura(id: number): Observable<any> {
  return this.clienteHttp.patch(`${this.urlBase}/${id}/anular`, {});
}
  obtenerDashboard(): Observable<{dia: number, mes: number, anio: number}> {
  return this.clienteHttp.get<{dia: number, mes: number, anio: number}>(`${this.urlBase}/dashboard`);
}
  
}
