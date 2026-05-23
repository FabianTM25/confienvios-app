import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClienteDto } from '../modelo/ClienteDto_modelo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class clienteDto_Service {
  //private urlBase = "http://localhost:8080/clienteDto"


private urlBase = `${environment.apiUrl}/clienteDto`;


  constructor (private clienteHttp: HttpClient){}

    obtenerCliente_dtoLista(): Observable<ClienteDto[]>{
      return this.clienteHttp.get<ClienteDto[]>(this.urlBase);
    }

    agregarCliente_dto(cliente: ClienteDto): Observable<Object>{
      return this.clienteHttp.post(this.urlBase, cliente)
    }

    actualizarCliente_dto(cliente: ClienteDto) {
    return this.clienteHttp.put(`${this.urlBase}/${cliente.idClienteDto}`, cliente);
    }

  eliminarCliente_dto(id: number): Observable<any> {
    return this.clienteHttp.delete(`${this.urlBase}/${id}`);
  }
    buscarClienteDto(texto: string): Observable<ClienteDto>{
    return this.clienteHttp.get<ClienteDto>(`${this.urlBase}/buscar?texto=${texto}`);
  }
}
