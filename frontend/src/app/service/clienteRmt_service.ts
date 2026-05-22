import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClienteRtm } from '../modelo/ClienteRmt_modelo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class clienteRmt_Service {
  //private urlBase = "http://localhost:8080/clientes"
private apiUrl = `${environment.apiUrl}/api/clientes`;

  constructor (private clienteHttp: HttpClient){}

    obtenerCliente_rmtLista(): Observable<ClienteRtm[]>{
      //return this.clienteHttp.get<ClienteRtm[]>(this.urlBase);
      return this.clienteHttp.get<ClienteRtm[]>(this.apiUrl);
    }

    agregarCliente_rmt(cliente: ClienteRtm): Observable<Object>{
      //return this.clienteHttp.post(this.urlBase, cliente)
      return this.clienteHttp.post(this.apiUrl, cliente);
    }

    actualizarCliente_rmt(cliente: ClienteRtm) {
    //return this.clienteHttp.put(`${this.urlBase}/${cliente.idClienteRmt}`, cliente);
     return this.clienteHttp.put(`${this.apiUrl}/${cliente.idClienteRmt}`, cliente);
    }

  eliminarCliente_rmt(id: number): Observable<any> {
    //return this.clienteHttp.delete(`${this.urlBase}/${id}`);
    return this.clienteHttp.delete(`${this.apiUrl}/${id}`);
  }

  buscarClienteRmt(texto: string): Observable<ClienteRtm>{
  //return this.clienteHttp.get<ClienteRtm>(`${this.urlBase}/buscar?texto=${texto}`);
  return this.clienteHttp.get<ClienteRtm>(`${this.apiUrl}/buscar?texto=${texto}`);
}
  
}
