import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {


  private url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  imprimirFactura(id: number): Observable<Blob> {
    return this.http.get(`${this.url}/api/reportes/report/${id}`, {
      responseType: 'blob'
    });
  }

  imprimirRotulo(id: number) {
    return this.http.get(`${this.url}/api/reportes/rotulo/${id}`, {
      responseType: 'blob'
    });
  }
}