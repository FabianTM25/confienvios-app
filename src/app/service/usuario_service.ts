import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Usuario } from '../modelo/Usuario_modelo';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private urlBase = "http://localhost:8080/usuarios"


  constructor (private clienteHttp: HttpClient){}

    obtenerUsuarioLista(): Observable<Usuario[]>{
      return this.clienteHttp.get<Usuario[]>(this.urlBase);
    }

    agregarUsuario(usuario: Usuario): Observable<Object>{
      return this.clienteHttp.post(this.urlBase, usuario)
    }

    actualizarUsuario(usuario: Usuario) {
    return this.clienteHttp.put(`${this.urlBase}/${usuario.id_usuario}`, usuario);
    }

  eliminarUsuario(id: number): Observable<any> {
    return this.clienteHttp.delete(`${this.urlBase}/${id}`);
  }
  
}
