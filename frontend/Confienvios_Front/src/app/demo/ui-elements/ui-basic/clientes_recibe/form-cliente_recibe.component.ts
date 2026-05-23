// angular import
//import { Component } from '@angular/core';

// project import
import { NgbDropdownModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { ChangeDetectorRef, Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Field, form, minLength, required } from '@angular/forms/signals';


// Models & Services
import { ClienteDto } from 'src/app/modelo/ClienteDto_modelo';
import { clienteDto_Service } from 'src/app/service/clienteDto_service';

@Component({
  selector: 'app-form-clienteDto',
  imports: [CommonModule, SharedModule, NgbDropdownModule,Field],
  templateUrl: './form-cliente_recibe.component.html',
  styleUrl: './form-cliente_recibe.component.scss'
})

export class BasicClientesComponent implements OnInit {
 // Inyecciones
  private cd = inject(ChangeDetectorRef);
  private ClienteDto_Service = inject(clienteDto_Service);
  private modalService = inject(NgbModal);

     //----------------------------Formulario modal-----------------------------------

   // Referencias y Estados
  private modalRef!: NgbModalRef;
  clienteDto: ClienteDto[] = [];
  clienteDtoOriginal: ClienteDto[] = [];
  clienteDtoPaginado: ClienteDto[] = [];
  
  //busqueda 
  textoBusqueda: string = '';
  submitted = signal(false);

  //paginador
  paginaActual: number = 1;
  registrosPorPagina: number = 8;
  totalPaginas: number = 0;

  mensajeExito: string = '';
  mostrarToast: boolean = false;
  mensajeToast: string = '';

   
  // Modelo inicializado como Signal
  registerModel = signal<ClienteDto>({
    idClienteDto: null,
    nombreClienteDto: '',
    documentoClienteDto: '',
    td: '',
    niu: '',
    pabellon: '',  
    estructura: '',
    estado: 1 // 🔥 activo por defecto
  }); 

    registerForm = form(this.registerModel, (path) => {

    required(path.nombreClienteDto, {
      message: 'El nombre es requerido'
    });

    required(path.documentoClienteDto, {
      message: 'El documento es requerido'
    });

    required(path.td, {
      message: 'El TD es requerido'
    });
  });
  
  ngOnInit(): void {
    this.obtenerClienteDto();
  }
  
 /**
   * Abre el modal para crear un nuevo registro
   */

 openNuevoClienteDto(content: any) {
  this.submitted.set(false);

  // Resetear modelo
  this.registerModel.set({
    idClienteDto: null,
    nombreClienteDto: '',
    documentoClienteDto: '',
    td: '',
    niu: '',
    pabellon: '',  
    estructura: '',
    estado: 1 // 🔥 activo por defecto
  });

  this.openModal(content);
}

/**-------------------------------------------------------------- editar usuario-----------------------------------------
     * Abre el modal cargando los datos de un usuario existente
     */
    editarClienteDto(clienteDto: ClienteDto, content: any) {
      this.submitted.set(false);
      
      // Clonamos el objeto para evitar modificar la fila de la tabla directamente
      this.registerModel.set({ ...clienteDto });
      
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
  const esEdicion = !!datosUsuario.idClienteDto;

  // validaciones
  const documentoExiste = this.clienteDtoOriginal.find(c =>
    c.documentoClienteDto === datosUsuario.documentoClienteDto &&
    c.idClienteDto !== datosUsuario.idClienteDto
  );

  if (documentoExiste) {
    alert("El documento ya está registrado");
    return;
  }

  const tdExiste = this.clienteDtoOriginal.find(c =>
    c.td === datosUsuario.td &&
    c.idClienteDto !== datosUsuario.idClienteDto
  );

  if (tdExiste) {
    alert("El TD ya está registrado");
    return;
  }

  if (esEdicion) {

    this.ClienteDto_Service.actualizarCliente_dto(datosUsuario).subscribe({
      next: () => this.finalizarGuardado("Cliente actualizado correctamente"),
      error: (err) => console.error("Error al actualizar:", err)
    });

  } else {

    this.ClienteDto_Service.agregarCliente_dto(datosUsuario).subscribe({
      next: () => this.finalizarGuardado("Cliente creado correctamente"),
      error: (err) => console.error("Error al agregar:", err)
    });

  }
}
 /**
   * Limpia el estado tras una operación exitosa
   */
  private finalizarGuardado(mensaje: string) {
  this.modalRef.close();
this.cd.detectChanges();

  this.mensajeToast = mensaje;
  this.mostrarToast = true;

  this.obtenerClienteDto();

  // 🔥 FORZAR REFRESCO
  setTimeout(() => {
    this.cd.detectChanges();
  });

  this.registerModel.set({
    idClienteDto: null,
    nombreClienteDto: '',
    documentoClienteDto: '',
    td: '',
    niu: '',
    pabellon: '',
    estructura: ''
  });

  this.registerForm().reset();
  this.submitted.set(false);

  const cerrar = () => {
    this.mostrarToast = false;
    document.removeEventListener('click', cerrar);
  };

  setTimeout(() => {
    document.addEventListener('click', cerrar);
  }, 100);
}
 /**---------------------------------------------------------------------eliminar usuario------------------------------------------
   Lógica para eliminar*/
   
 eliminarClienteDto(cliente: ClienteDto) {
  if (cliente.idClienteDto && confirm('¿Está seguro de desactivar este cliente?')) {

    const clienteActualizado = {
      ...cliente,
      estado: 2
    };

    this.ClienteDto_Service.actualizarCliente_dto(clienteActualizado).subscribe({
      next: () => {

        // 🔥 ELIMINAR LOCALMENTE SIN ESPERAR BACKEND
        this.clienteDtoOriginal = this.clienteDtoOriginal.filter(c => c.idClienteDto !== cliente.idClienteDto);
        this.clienteDto = this.clienteDto.filter(c => c.idClienteDto !== cliente.idClienteDto);

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

  this.totalPaginas = Math.ceil(this.clienteDto.length / this.registrosPorPagina);

  const inicio = (this.paginaActual - 1) * this.registrosPorPagina;
  const fin = inicio + this.registrosPorPagina;

  this.clienteDtoPaginado = this.clienteDto.slice(inicio, fin);
}
//cambiar la pagina
cambiarPagina(pagina: number) {

  if (pagina < 1 || pagina > this.totalPaginas) return;

  this.paginaActual = pagina;
  this.actualizarPaginacion();
}


 //---------------------------- * Obtiene la lista de clientes desde el servicio-------------------------------
//obtener cliente
private obtenerClienteDto(): void {
  this.ClienteDto_Service.obtenerCliente_dtoLista().subscribe({
    next: (datos) => {
      console.log("Clientes RMT recibidos:", datos);

      this.clienteDtoOriginal = datos
  .filter(c => c.estado === 1) // 🔥 solo activos
  .sort((a, b) => (b.idClienteDto ?? 0) - (a.idClienteDto ?? 0));

      this.clienteDto = [...this.clienteDtoOriginal];

      this.paginaActual = 1;
      this.actualizarPaginacion();

      this.cd.detectChanges();
    },
    error: (error) => console.error("Error al obtener los usuarios:", error)
  });
}
//buscador entre paginas
buscarClienteDto(event: any) {

  const texto = event.target.value.toLowerCase();

  this.clienteDto = this.clienteDtoOriginal.filter(cliente =>
    cliente.nombreClienteDto?.toLowerCase().includes(texto) ||
    cliente.documentoClienteDto?.toString().includes(texto)
  );

  this.paginaActual = 1;
  this.actualizarPaginacion();
}
obtenerInicio(): number {
  return (this.paginaActual - 1) * this.registrosPorPagina + 1;
}

obtenerFin(): number {
  const fin = this.paginaActual * this.registrosPorPagina;
  return fin > this.clienteDto.length ? this.clienteDto.length : fin;
}
mensajeError: string = '';
}



