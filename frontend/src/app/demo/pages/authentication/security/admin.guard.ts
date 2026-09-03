import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from 'src/app/demo/pages/authentication/security/auth.service';

// Igual que authGuard, pero ademas exige rol ADMIN. La verificacion real
// (que no se puede eludir cambiando el frontend) esta en el backend:
// /api/usuarios/** exige ROLE_ADMIN en SecurityConfig. Este guard solo evita
// que un usuario sin permisos llegue a ver la pantalla.
export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.estaAutenticado() && authService.esAdmin()) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};
