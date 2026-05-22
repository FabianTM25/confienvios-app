import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

//local
/*@Injectable({
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


}*/


//web

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {

  private url = environment.apiUrl;

  constructor(private http: HttpClient) { }

  imprimirFactura(id: number): Observable<Blob> {
    return this.http.get(`${this.url}/api/report/${id}`, {
      responseType: 'blob'
    });
  }

  imprimirRotulo(id: number): Observable<Blob> {
    return this.http.get(`${this.url}/api/rotulo/${id}`, {
      responseType: 'blob'
    });
  }
}
