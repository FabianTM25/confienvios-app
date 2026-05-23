// angular import
import { ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
//import { RouterModule } from '@angular/router';
import { email, Field, form, minLength, required } from '@angular/forms/signals';
import { Router, RouterModule } from '@angular/router';


// project import
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { AuthService } from '../security/auth.service';

const TEST_USER = 'Yuder';
const TEST_PASSWORD = '12345678';


@Component({
  selector: 'app-auth-signin',
  imports: [CommonModule, RouterModule, SharedModule, Field],
  templateUrl: './auth-signin.component.html',
  styleUrls: ['./auth-signin.component.scss']
})
export class AuthSigninComponent {
  private cd = inject(ChangeDetectorRef);

  submitted = signal(false);
  error = signal('');
  showPassword = signal(false);

  loginModal = signal<{ user: string; password: string }>({
    user: '',
    password: ''
  });

  loginForm = form(this.loginModal, (schemaPath) => {
    required(schemaPath.user, { message: 'Usuario es Requerido' });
    required(schemaPath.password, { message: 'Contraseña Requerida' });
    minLength(schemaPath.password, 8, { message: 'Ingrese una Contraseña valida' });
  });

 /* onSubmit(event: Event) {
    this.submitted.set(true);
    this.error.set('');
    event.preventDefault();
    const credentials = this.loginModal();
    console.log('login user logged in with:', credentials);
    this.cd.detectChanges();
  }*/

    private router = inject(Router);
    private authService = inject(AuthService);

onSubmit(event: Event) {
  event.preventDefault();
  this.submitted.set(true);
  this.error.set('');

  if (this.loginForm.user().invalid() || this.loginForm.password().invalid()) {
    return;
  }

  const { user, password } = this.loginModal();

  this.authService.login(user, password).subscribe({
    next: (resp) => {
      this.authService.guardarToken(resp.token);
      this.router.navigate(['/dashboard']);
    },
    error: () => {
      this.error.set('Usuario o contraseña incorrectos');
    }
  });
}



  togglePasswordVisibility() {
    this.showPassword.set(!this.showPassword());
  }
}
