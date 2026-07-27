import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from 'src/app/demo/pages/authentication/security/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.obtenerToken();

  const reqAEnviar = token
    ? req.clone({ headers: req.headers.set('Authorization', `Bearer ${token}`) })
    : req;

  return next(reqAEnviar).pipe(
    catchError((error: HttpErrorResponse) => {
      // Un 401/403 en el propio login es "credenciales incorrectas", no una sesión vencida.
      const esLogin = req.url.includes('/api/auth/login');

      if (!esLogin && (error.status === 401 || error.status === 403)) {
        authService.cerrarSesion();
      }

      return throwError(() => error);
    })
  );
};