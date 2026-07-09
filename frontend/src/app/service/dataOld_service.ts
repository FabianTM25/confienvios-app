import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DataOld } from '../modelo/DataOld_modelo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class dataOld_Service {
  private urlBase = `${environment.apiUrl}/api/data-old`;

  constructor(private dataOldHttp: HttpClient) {}

  buscarDataOld(texto: string): Observable<DataOld[]> {
    return this.dataOldHttp.get<DataOld[]>(`${this.urlBase}/buscar?texto=${texto}`);
  }
}
