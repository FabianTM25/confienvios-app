import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from 'src/app/demo/pages/authentication/security/auth.guard';


  const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'usuarios',
        canActivate: [authGuard],
        loadComponent: () => import('./usuarios/form-usuarios.component').then((c) => c.BasicUsuariosComponent)
      },
      {
        path: 'clientes',
        canActivate: [authGuard],
        loadComponent: () => import('./clientes/form-clientes.component').then((c) => c.BasicClientesComponent)
      },
      {
        path: 'clientes_recibe',
        canActivate: [authGuard],
        loadComponent: () => import('./clientes_recibe/form-cliente_recibe.component').then((c) => c.BasicClientesComponent)
      },
      {
        path: 'facturacion',
        canActivate: [authGuard],
        loadComponent: () => import('./facturacion/factura.component').then((c) => c.FacturaComponent)
      },
      {
        path: 'lista_facturas',
        canActivate: [authGuard],
        loadComponent: () => import('./lista_facturas/lista_factura.component').then((c) => c.BasicClientesComponent)
      },
      {
        path: 'dashboard',
        canActivate: [authGuard],
        loadComponent: () => import('src/app/demo/dashboard/dashboard.component').then((c) => c.DashboardComponent)
      }
    ]
  }


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UiBasicRoutingModule {} 
