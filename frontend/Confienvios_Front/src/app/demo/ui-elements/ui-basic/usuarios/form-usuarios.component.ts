
import { ChangeDetectorRef, Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

// Project imports
import { NgbDropdownModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { Field, form, minLength, required } from '@angular/forms/signals';

// Models & Services
import { Usuario } from 'src/app/modelo/Usuario_modelo';
import { UsuarioService } from 'src/app/service/usuario_service';

@Component({
  selector: 'app-form-usuarios',
  standalone: true,
  imports: [
    CommonModule,
    SharedModule,
    NgbDropdownModule,
    FormsModule,
    Field
  ],
  templateUrl: './form-usuarios.component.html',
  styleUrl: './form-usuarios.component.scss'
})
export class BasicUsuariosComponent implements OnInit {
  // Inyecciones
  private cd = inject(ChangeDetectorRef);
  private usuarioService = inject(UsuarioService);
  private modalService = inject(NgbModal);
  
  //----------------------------Formulario modal-----------------------------------
  // Referencias y Estados
  private modalRef!: NgbModalRef;
  usuarios: Usuario[] = [];
  submitted = signal(false);
  modalTitle = signal('Nuevo Usuario');

  // Modelo inicializado como Signal
  registerModel = signal<Usuario>({
    id_usuario: null,
    usuario: '',
    user_name: '',
    password: ''
  });

  // Configuración del Formulario con Signals
  registerForm = form(this.registerModel, (schemaPath) => {
    required(schemaPath.usuario, {
      message: 'El Usuario es requerido'
    });

    required(schemaPath.user_name, {
      message: 'El Nombre es requerido'
    });

    required(schemaPath.password, {
      message: 'Contraseña es requerida'
    });

    minLength(schemaPath.password, 8, {
      message: 'La contraseña debe tener mínimo 8 caracteres'
    });
  });

  ngOnInit(): void {
    this.obtenerUsuario();
  }

  /**
  ---------------------------- * Obtiene la lista de usuarios desde el servicio-------------------------------
   */
  private obtenerUsuario(): void {
    this.usuarioService.obtenerUsuarioLista().subscribe({
      next: (datos) => {
        this.usuarios = datos.sort((a, b) => (a.id_usuario ?? 0) - (b.id_usuario ?? 0));
      this.cd.detectChanges();
      },
      error: (error) => console.error("Error al obtener los usuarios:", error)
    });
  }

  /**
   * Abre el modal para crear un nuevo registro
   */
  openNuevoUsuario(content: any) {
    this.modalTitle.set('Nuevo Usuario');
    this.submitted.set(false);
    
    // Resetear el modelo a valores vacíos
    this.registerModel.set({
      id_usuario: null,
      usuario: '',
      user_name: '',
      password: ''
    });

    this.openModal(content);
  }

  /**-------------------------------------------------------------- editar usuario-----------------------------------------
   * Abre el modal cargando los datos de un usuario existente
   */
  editarUsuario(usuario: Usuario, content: any) {
    this.modalTitle.set('Editar Usuario');
    this.submitted.set(false);
    
    // Clonamos el objeto para evitar modificar la fila de la tabla directamente
    this.registerModel.set({ ...usuario });
    
    this.openModal(content);
  }

  /**
   * Función genérica para abrir el modal
   */
  private openModal(content: any) {
    this.modalRef = this.modalService.open(content, {
      backdrop: 'static',
      keyboard: false,
      centered: true
    });
  }

  /**-------------------------------------------------------------------guardar usuario-----------------------------------------------
   * Lógica de guardado (Crear o Actualizar)
   */
  onSubmit() {
    this.submitted.set(true);
    const formState = this.registerForm();

    if (!formState.valid) {
      console.log("Formulario inválido");
      return;
    }

    const datosUsuario = this.registerModel();

    // Decidir si es UPDATE o CREATE basado en el id_usuario
    if (datosUsuario.id_usuario) {
      // Caso: Editar
      this.usuarioService.actualizarUsuario(datosUsuario).subscribe({
        next: () => this.finalizarGuardado(),
        error: (err) => console.error("Error al actualizar:", err)
      });
    } else {
      // Caso: Nuevo
      this.usuarioService.agregarUsuario(datosUsuario).subscribe({
        next: () => this.finalizarGuardado(),
        error: (err) => console.error("Error al agregar:", err)
      });
    }
  }

  /**
   * Limpia el estado tras una operación exitosa
   */
  private finalizarGuardado() {
    console.log("Operación exitosa");
    this.modalRef.close();
    this.obtenerUsuario(); // Refrescar tabla
    
    // Resetear modelo
    this.registerModel.set({
      id_usuario: null,
      usuario: '',
      user_name: '',
      password: ''
    });
    this.submitted.set(false);
  }

  /**---------------------------------------------------------------------eliminar usuario------------------------------------------
   Lógica para eliminar*/
   
  eliminarUsuario(id: number | null) {
    if (id && confirm('¿Está seguro de eliminar este usuario?')) {
      this.usuarioService.eliminarUsuario(id).subscribe({
        next: () => this.obtenerUsuario(),
        error: (err) => console.error("Error al eliminar:", err)
      });
    }
  }
}

