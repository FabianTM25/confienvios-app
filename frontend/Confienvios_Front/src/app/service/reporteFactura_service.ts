import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {


 //private url = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

/* // ✅ Con responseType: 'blob'
imprimirFactura(id: number): Observable<Blob> {
=======
  //private url = 'http://localhost:8080/api';
  private url = environment.apiUrl;


  constructor(private http: HttpClient) {}

// ✅ Con responseType: 'blob' conexion local
/*imprimirFactura(id: number): Observable<Blob> {

  return this.http.get(`http://localhost:8080/api/report/${id}`, {
    responseType: 'blob'
  });
}

imprimirRotulo(id: number) {
  return this.http.get(`http://localhost:8080/api/rotulo/${id}`, {
    responseType: 'blob'
  });
}*/



private url = environment.apiUrl;


//conexion rendel

imprimirFactura(id: number): Observable<Blob> {
  return this.http.get(`${this.url}/report/${id}`, {
    responseType: 'blob'
  });

}



imprimirRotulo(id: number) {
  return this.http.get(`${this.url}/rotulo/${id}`, {
    responseType: 'blob'
  });
}
}
