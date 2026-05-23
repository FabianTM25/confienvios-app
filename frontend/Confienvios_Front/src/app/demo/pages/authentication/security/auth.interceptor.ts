import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from 'src/app/demo/pages/authentication/security/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.obtenerToken();

  if (token) {
    const reqClone = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(reqClone);
  }

  return next(req);
};