import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Configuracion } from '../modelo/Configuracion_modelo';
import { RangoPeso } from '../modelo/RangoPeso_modelo';
import { environment } from '../../../src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ConfiguracionService {
  private urlBase = `${environment.apiUrl}/api/configuracion`;

  constructor(private clienteHttp: HttpClient) {}

  obtenerConfiguracion(): Observable<Configuracion> {
    return this.clienteHttp.get<Configuracion>(this.urlBase);
  }

  actualizarConfiguracion(configuracion: Configuracion): Observable<Configuracion> {
    return this.clienteHttp.put<Configuracion>(this.urlBase, configuracion);
  }

  listarRangos(): Observable<RangoPeso[]> {
    return this.clienteHttp.get<RangoPeso[]>(`${this.urlBase}/rangos`);
  }

  crearRango(rango: RangoPeso): Observable<RangoPeso> {
    return this.clienteHttp.post<RangoPeso>(`${this.urlBase}/rangos`, rango);
  }

  actualizarRango(rango: RangoPeso): Observable<RangoPeso> {
    return this.clienteHttp.put<RangoPeso>(`${this.urlBase}/rangos/${rango.id_rango}`, rango);
  }

  eliminarRango(id: number): Observable<any> {
    return this.clienteHttp.delete(`${this.urlBase}/rangos/${id}`);
  }
}
