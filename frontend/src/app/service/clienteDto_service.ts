import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClienteDto } from '../modelo/ClienteDto_modelo';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class clienteDto_Service {
  //private urlBase = "http://localhost:8080/clienteDto"
  private apiUrl = `${environment.apiUrl}/api/clienteDto`;


  constructor (private clienteHttp: HttpClient){}

    obtenerCliente_dtoLista(): Observable<ClienteDto[]>{
      //return this.clienteHttp.get<ClienteDto[]>(this.urlBase);
      return this.clienteHttp.get<ClienteDto[]>(this.apiUrl);
    }

    agregarCliente_dto(cliente: ClienteDto): Observable<Object>{
      //return this.clienteHttp.post(this.urlBase, cliente)
       return this.clienteHttp.post(this.apiUrl, cliente);
    }

    actualizarCliente_dto(cliente: ClienteDto) {
    //return this.clienteHttp.put(`${this.urlBase}/${cliente.idClienteDto}`, cliente);
     return this.clienteHttp.put(`${this.apiUrl}/${cliente.idClienteDto}`, cliente);
    }

  eliminarCliente_dto(id: number): Observable<any> {
   // return this.clienteHttp.delete(`${this.urlBase}/${id}`);
   return this.clienteHttp.delete(`${this.apiUrl}/${id}`);
  }
    buscarClienteDto(texto: string): Observable<ClienteDto>{
    //return this.clienteHttp.get<ClienteDto>(`${this.urlBase}/buscar?texto=${texto}`);
    return this.clienteHttp.get<ClienteDto>( `${this.apiUrl}/buscar?texto=${texto}`  );
  }
}
