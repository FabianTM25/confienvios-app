import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Material } from '../modelo/Material_modelo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MaterialService {
  private urlBase = `${environment.apiUrl}/api/materiales`;

  constructor(private materialHttp: HttpClient) {}

  obtenerMaterialLista(): Observable<Material[]> {
    return this.materialHttp.get<Material[]>(this.urlBase);
  }

  agregarMaterial(material: Material): Observable<Material> {
    return this.materialHttp.post<Material>(this.urlBase, material);
  }

  actualizarMaterial(material: Material): Observable<Material> {
    return this.materialHttp.put<Material>(`${this.urlBase}/${material.id_material}`, material);
  }

  eliminarMaterial(id: number): Observable<any> {
    return this.materialHttp.delete(`${this.urlBase}/${id}`);
  }
}
