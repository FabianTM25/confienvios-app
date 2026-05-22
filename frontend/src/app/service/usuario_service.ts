import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Usuario } from '../modelo/Usuario_modelo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {

  //private urlBase = `${environment.apiUrl}/usuarios`; 
    private apiUrl = `${environment.apiUrl}/api/usuarios`;

  constructor(private clienteHttp: HttpClient) {}

  obtenerUsuarioLista(): Observable<Usuario[]> {
    //return this.clienteHttp.get<Usuario[]>(this.urlBase);
    return this.clienteHttp.get<Usuario[]>(this.apiUrl);
  }

  agregarUsuario(usuario: Usuario): Observable<Object> {
    //return this.clienteHttp.post(this.urlBase, usuario);
    return this.clienteHttp.post(this.apiUrl, usuario);
  }

  actualizarUsuario(usuario: Usuario) {
    //return this.clienteHttp.put(`${this.urlBase}/${usuario.id_usuario}`, usuario);
    return this.clienteHttp.put(`${this.apiUrl}/${usuario.id_usuario}`, usuario);
  }

  eliminarUsuario(id: number): Observable<any> {
    //return this.clienteHttp.delete(`${this.urlBase}/${id}`);
    return this.clienteHttp.delete(`${this.apiUrl}/${id}`);
  }
};