import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Documento } from '../modelo/Documento_modelo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DocumentoService {
  private urlBase = `${environment.apiUrl}/api/documentos`;

  constructor(private documentoHttp: HttpClient) {}

  obtenerDocumentoLista(): Observable<Documento[]> {
    return this.documentoHttp.get<Documento[]>(this.urlBase);
  }

  agregarDocumento(documento: Documento): Observable<Documento> {
    return this.documentoHttp.post<Documento>(this.urlBase, documento);
  }

  actualizarDocumento(documento: Documento): Observable<Documento> {
    return this.documentoHttp.put<Documento>(`${this.urlBase}/${documento.id_documento}`, documento);
  }

  eliminarDocumento(id: number): Observable<any> {
    return this.documentoHttp.delete(`${this.urlBase}/${id}`);
  }
}
