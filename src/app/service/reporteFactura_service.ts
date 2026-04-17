import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {

  private url = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

// ✅ Con responseType: 'blob'
imprimirFactura(id: number): Observable<Blob> {
  return this.http.get(`http://localhost:8080/api/report/${id}`, {
    responseType: 'blob'
  });
}

imprimirRotulo(id: number) {
  return this.http.get(`http://localhost:8080/api/rotulo/${id}`, {
    responseType: 'blob'
  });
}


}