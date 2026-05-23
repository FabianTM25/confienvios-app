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
  private urlBase = `${environment.apiUrl}/clientes`;


  constructor (private clienteHttp: HttpClient){}

    obtenerCliente_rmtLista(): Observable<ClienteRtm[]>{
      return this.clienteHttp.get<ClienteRtm[]>(this.urlBase);
    }

    agregarCliente_rmt(cliente: ClienteRtm): Observable<Object>{
      return this.clienteHttp.post(this.urlBase, cliente)
    }

    actualizarCliente_rmt(cliente: ClienteRtm) {
    return this.clienteHttp.put(`${this.urlBase}/${cliente.idClienteRmt}`, cliente);
    }

  eliminarCliente_rmt(id: number): Observable<any> {
    return this.clienteHttp.delete(`${this.urlBase}/${id}`);
  }

  buscarClienteRmt(texto: string): Observable<ClienteRtm>{
  return this.clienteHttp.get<ClienteRtm>(`${this.urlBase}/buscar?texto=${texto}`);
}
  
}
