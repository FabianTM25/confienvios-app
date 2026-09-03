import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  //private url = 'http://localhost:8080/auth';

private url = environment.apiUrl + '/api/auth';

  private http = inject(HttpClient);
  private router = inject(Router);

  login(usuario: string, password: string): Observable<any> {
    return this.http.post(`${this.url}/login`, { usuario, password });
  }

  guardarToken(token: string): void {
    localStorage.setItem('token', token);
  }

  guardarSesion(token: string, rol: string): void {
    localStorage.setItem('token', token);
    localStorage.setItem('rol', rol);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  obtenerRol(): string | null {
    return localStorage.getItem('rol');
  }

  esAdmin(): boolean {
    return this.obtenerRol() === 'ADMIN';
  }

  estaAutenticado(): boolean {
    return !!this.obtenerToken();
  }

  cerrarSesion(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('rol');
    this.router.navigate(['/login']);
  }
}