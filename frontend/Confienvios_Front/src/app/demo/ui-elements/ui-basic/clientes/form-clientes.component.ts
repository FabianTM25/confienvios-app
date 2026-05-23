// angular import
//import { Component } from '@angular/core';

// project import
import { NgbDropdownModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { ChangeDetectorRef, Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Field, form, minLength, required } from '@angular/forms/signals';

// Models & Services
import { ClienteRtm } from 'src/app/modelo/ClienteRmt_modelo';
import { clienteRmt_Service } from 'src/app/service/clienteRmt_service';


@Component({
  selector: 'app-form-clientes',
  imports: [CommonModule, SharedModule, NgbDropdownModule, Field],
  templateUrl: './form-clientes.component.html',
  styleUrls: ['./form-clientes.component.scss']
})

export class BasicClientesComponent implements OnInit {
  // Inyecciones
  private cd = inject(ChangeDetectorRef);
  private ClienteRmt_Service = inject(clienteRmt_Service);
  private modalService = inject(NgbModal);


    //----------------------------Formulario modal-----------------------------------

   // Referencias y Estados
  private modalRef!: NgbModalRef;
  clienteRmt: ClienteRtm[] = [];
  clienteRmtOriginal: ClienteRtm[] = [];
  clienteRmtPaginado: ClienteRtm[] = [];
  
  //busqueda 
  textoBusqueda: string = '';
  submitted = signal(false);

  modalTitle = signal('Nuevo Usuario');

  //paginador
  paginaActual: number = 1;
  registrosPorPagina: number = 8;
  totalPaginas: number = 0;

  mensajeExito: string = '';
  mostrarToast: boolean = false;
  mensajeToast: string = '';

  
  // Modelo inicializado como Signal
  registerModel = signal<ClienteRtm>({
    idClienteRmt:null,
    nombreClienteRmt: '',
    documentoClienteRmt: '',
    direccionClienteRmt: '',
    telefonoClienteRmt: '',
    estado: 1 // 🔥 activo por defecto

  });
  
  

  registerForm = form(this.registerModel, (path) => {

    required(path.nombreClienteRmt, {
      message: 'El nombre es requerido'
    });

    required(path.documentoClienteRmt, {
      message: 'El documento es requerido'
    });

    required(path.direccionClienteRmt, {
      message: 'La dirección es requerida'
    });

    required(path.telefonoClienteRmt, {
      message: 'El teléfono es requerido'
    });

  });
  
   
  ngOnInit(): void {
    this.obtenerClienteRmt();
  }
  
 /**
   * Abre el modal para crear un nuevo registro
   */

 openNuevoClienteRmt(content: any) {
  this.modalTitle.set('Nuevo Cliente Rmt');
  this.submitted.set(false);

  // Resetear modelo
  this.registerModel.set({
    idClienteRmt:null,
    nombreClienteRmt: '',
    tipoDocumentoRmt:'',
    documentoClienteRmt: '',
    direccionClienteRmt: '',
    telefonoClienteRmt: '',
    estado: 1 // 🔥 activo por defecto
  });

  this.openModal(content);
}

  /**-------------------------------------------------------------- editar usuario-----------------------------------------
     * Abre el modal cargando los datos de un usuario existente
     */
    editarClienteRmt(clienteRmt: ClienteRtm, content: any) {
      this.modalTitle.set('Editar ClienteRmt');
      this.submitted.set(false);
      
      // Clonamos el objeto para evitar modificar la fila de la tabla directamente
      this.registerModel.set({ ...clienteRmt });
      
      this.openModal(content);
    }
  
  /**
   * Función genérica para abrir el modal
   */
  private openModal(content: any) {
    this.modalRef = this.modalService.open(content, {
      backdrop: 'static',
      keyboard: false,
      centered: true,
      size: 'xl'
    });
  }

/**-------------------------------------------------------------------guardar usuario-----------------------------------------------
   * Lógica de guardado (Crear o Actualizar)
   */
  onSubmit() {
  this.submitted.set(true);

  const datosUsuario = this.registerForm().value();
  const esEdicion = !!datosUsuario.idClienteRmt;

  const documentoExiste = this.clienteRmtOriginal.find(c =>
    c.documentoClienteRmt === datosUsuario.documentoClienteRmt &&
    c.idClienteRmt !== datosUsuario.idClienteRmt
  );

  if (documentoExiste) {
    alert("El documento ya está registrado");
    return;
  }

  console.log("Datos formulario:", datosUsuario);

  if (esEdicion) {
    this.ClienteRmt_Service.actualizarCliente_rmt(datosUsuario).subscribe({
      next: () => this.finalizarGuardado("Cliente actualizado correctamente"),
      error: (err) => console.error("Error al actualizar:", err)
    });
  } else {
    this.ClienteRmt_Service.agregarCliente_rmt(datosUsuario).subscribe({
      next: () => this.finalizarGuardado("Cliente creado correctamente"),
      error: (err) => console.error("Error al agregar:", err)
    });
  }
}
 /**
   * Limpia el estado tras una operación exitosa
   */
  private finalizarGuardado(mensaje: string) {
    console.log("Operación exitosa");
    this.modalRef.close();

    this.mensajeToast = mensaje;
    this.mostrarToast = true;

    this.obtenerClienteRmt(); // Refrescar tabla
   
    // Resetear modelo
    this.registerModel.set({
      idClienteRmt:null,
      nombreClienteRmt: '',
      tipoDocumentoRmt:'',
      documentoClienteRmt: '',
      direccionClienteRmt: '',
      telefonoClienteRmt: '',
      estado: 1 
    });
    this.submitted.set(false);

     // 🔥 cerrar con cualquier click
  const cerrar = () => {
    this.mostrarToast = false;
    document.removeEventListener('click', cerrar);
  };

  setTimeout(() => {
    document.addEventListener('click', cerrar);
  }, 100); // pequeño delay para evitar que el mismo click lo cierre

 }
 /**---------------------------------------------------------------------eliminar usuario------------------------------------------
   Lógica para eliminar*/
   
  eliminarClienteRmt(cliente: ClienteRtm) {
    if (cliente.idClienteRmt && confirm('¿Está seguro de ELIMINAR este cliente?')) {
  
      const clienteActualizado = {
        ...cliente,
        estado: 2
      };
  
      this.ClienteRmt_Service.actualizarCliente_rmt(clienteActualizado).subscribe({
        next: () => {
  
          // 🔥 ELIMINAR LOCALMENTE SIN ESPERAR BACKEND
          this.clienteRmtOriginal = this.clienteRmtOriginal.filter(c => c.idClienteRmt !== cliente.idClienteRmt);
          this.clienteRmt = this.clienteRmt.filter(c => c.idClienteRmt !== cliente.idClienteRmt);
  
          this.actualizarPaginacion();
  
          this.mensajeToast = "Cliente Eliminado correctamente";
          this.mostrarToast = true;
  
          this.cd.detectChanges();
        },
        error: (err) => console.error("Error al desactivar:", err)
      });
    }
  }

  ///funcion de paginacion
  actualizarPaginacion() {

  this.totalPaginas = Math.ceil(this.clienteRmt.length / this.registrosPorPagina);

  const inicio = (this.paginaActual - 1) * this.registrosPorPagina;
  const fin = inicio + this.registrosPorPagina;

  this.clienteRmtPaginado = this.clienteRmt.slice(inicio, fin);
}
//cambiar la pagina
cambiarPagina(pagina: number) {

  if (pagina < 1 || pagina > this.totalPaginas) return;

  this.paginaActual = pagina;
  this.actualizarPaginacion();
}


 //---------------------------- * Obtiene la lista de clientes desde el servicio-------------------------------
//obtener cliente
private obtenerClienteRmt(): void {
  this.ClienteRmt_Service.obtenerCliente_rmtLista().subscribe({
    next: (datos) => {

      console.log("Clientes RMT recibidos:", datos);

      this.clienteRmtOriginal = datos.sort(
  (a, b) => (b.idClienteRmt ?? 0) - (a.idClienteRmt ?? 0)
);

      this.clienteRmt = [...this.clienteRmtOriginal];

      this.paginaActual = 1;
      this.actualizarPaginacion();

      this.cd.detectChanges();
    },
    error: (error) => console.error("Error al obtener los usuarios:", error)
  });
}
//buscador entre paginas
buscarCliente(event: any) {

  const texto = event.target.value.toLowerCase();

  this.clienteRmt = this.clienteRmtOriginal.filter(cliente =>
    cliente.nombreClienteRmt?.toLowerCase().includes(texto) ||
    cliente.documentoClienteRmt?.toString().includes(texto)
  );

  this.paginaActual = 1;
  this.actualizarPaginacion();
}
obtenerInicio(): number {
  return (this.paginaActual - 1) * this.registrosPorPagina + 1;
}

obtenerFin(): number {
  const fin = this.paginaActual * this.registrosPorPagina;
  return fin > this.clienteRmt.length ? this.clienteRmt.length : fin;
}
}
