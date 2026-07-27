import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReporteVentaService {

  private url = environment.apiUrl;

  constructor(private http: HttpClient) {}

  imprimirTicketVenta(id: number): Observable<Blob> {
    return this.http.get(`${this.url}/api/report-venta/${id}`, {
      responseType: 'blob'
    });
  }
}
