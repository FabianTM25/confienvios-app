// ANGULAR
import { NgbDropdownModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { ChangeDetectorRef, Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Field, form, minLength, required } from '@angular/forms/signals';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule  } from '@angular/forms';

// Models & Services
import { ClienteRtm } from 'src/app/modelo/ClienteRmt_modelo';
import { clienteRmt_Service } from 'src/app/service/clienteRmt_service';
import { Factura } from 'src/app/modelo/Factura_modelo';
import { FacturaService } from 'src/app/service/factura_service';
import { ClienteDto } from 'src/app/modelo/ClienteDto_modelo';
import { clienteDto_Service } from 'src/app/service/clienteDto_service';
import { ReporteService } from 'src/app/service/reporteFactura_service';

@Component({
  selector: 'app-factura',
  standalone: true,
  imports: [CommonModule, SharedModule, NgbDropdownModule, FormsModule ],
  templateUrl: './factura.component.html',
  styleUrls: ['./factura.component.scss']
})
export class FacturaComponent implements OnInit {

  // Inyecciones
  private cd = inject(ChangeDetectorRef);
  private Factura_Service = inject(FacturaService);
  private modalService = inject(NgbModal);
  private ClienteRmt_Service = inject(clienteRmt_Service);
  private ClienteDto_Service = inject(clienteDto_Service);
  private reporteService = inject(ReporteService);


     // Referencias y Estados
  private modalRef!: NgbModalRef;
  clienteRmt: ClienteRtm[] = [];
  clienteRmtSeleccionado: ClienteRtm = {} as ClienteRtm;
  clienteDtoSeleccionado: ClienteDto = {} as ClienteDto;
  clienteRmtOriginal: ClienteRtm[] = [];
  clienteDto: ClienteDto[] = [];
  clienteDtoOriginal: ClienteDto[] = [];
  facturaGuardada: Factura = {} as Factura;

  //busqueda 
  textoBusqueda: string = '';
  textoBusquedaDto: string = '';
  submitted = signal(false);



   ngOnInit(): void {
    this.obtenerClienteRmt();
    this.obtenerClienteDto();
  }


  //obtener cliente dto
private obtenerClienteDto(): void {
  this.ClienteDto_Service.obtenerCliente_dtoLista().subscribe({
    next: (datos) => {

      this.clienteDtoOriginal = datos.sort(
        (a, b) => (a.idClienteDto ?? 0) - (b.idClienteDto ?? 0)
      );

      this.clienteDto = [...this.clienteDtoOriginal];

      this.cd.detectChanges();
    },
    error: (error) => console.error("Error al obtener los usuarios:", error)
  });
}
//obtener cliente rmt
private obtenerClienteRmt(): void {
  this.ClienteRmt_Service.obtenerCliente_rmtLista().subscribe({
    next: (datos) => {

      this.clienteRmtOriginal = datos.sort(
        (a, b) => (a.idClienteRmt ?? 0) - (b.idClienteRmt ?? 0)
      );

      this.clienteRmt = [...this.clienteRmtOriginal];

      this.cd.detectChanges();
    },
    error: (error) => console.error("Error al obtener los usuarios:", error)
  });
}


buscarCliente(){

  if(!this.textoBusqueda){
    alert("Ingrese documento");
    return;
  }

  this.ClienteRmt_Service.buscarClienteRmt(this.textoBusqueda).subscribe({
    next: (data: ClienteRtm) => {
  console.log("Cliente encontrado:", data);

  this.clienteRmtSeleccionado = data;
  this.cd.detectChanges(); // fuerza refresco del formulario
},
    error: (error) => {
      
      console.error(error);
      alert("Cliente no encontrado");
    }
  });

}

 buscarClienteDto() {
  
  if(!this.textoBusquedaDto){
 alert("Ingrese documento");
    return;
  }

  this.ClienteDto_Service.buscarClienteDto(this.textoBusquedaDto).subscribe({
    next: (data: ClienteDto) => {
  console.log("Cliente encontrado:", data);

  this.clienteDtoSeleccionado = data;
  this.cd.detectChanges(); // fuerza refresco del formulario
},
    error: (error) => {
      console.error(error);
      alert("Cliente no encontrado");
    }
  });
  }



  openModal(content: any): void {

    this.modalService.open(content, {
      backdrop: 'static',
      keyboard: false,
      centered: true,
      size: 'xl'
    });

  }


  guardar(content: any): void {
      // VALIDAR CLIENTE RMT
   if (!this.clienteRmtSeleccionado?.idClienteRmt) {
    alert("Debe buscar un cliente que REMITE");
    return;
  }

  if (!this.clienteDtoSeleccionado?.idClienteDto) {
    alert("Debe buscar un cliente DESTINO");
    return;
  }

  const factura: Factura = {

    id_factura: null,
    numero_factura: '',

    id_cliente_rmt: this.clienteRmtSeleccionado.idClienteRmt?.toString(),
    nombre_cliente_rmt: this.clienteRmtSeleccionado.nombreClienteRmt,
    tipo_documento_rmt: this.clienteRmtSeleccionado.tipoDocumentoRmt,
    documento_cliente_rmt: this.clienteRmtSeleccionado.documentoClienteRmt,
    direccion_cliente_rmt: this.clienteRmtSeleccionado.direccionClienteRmt,
    telefono_cliente_rmt: this.clienteRmtSeleccionado.telefonoClienteRmt,

    id_cliente_dto: this.clienteDtoSeleccionado.idClienteDto?.toString(),
    nombre_cliente_dto: this.clienteDtoSeleccionado.nombreClienteDto,
    tipo_documento_dto: this.clienteDtoSeleccionado.tipoDocumentoDto,
    documento_cliente_dto: this.clienteDtoSeleccionado.documentoClienteDto,

    td: this.clienteDtoSeleccionado.td,
    niu: this.clienteDtoSeleccionado.niu,
    pabellon: this.clienteDtoSeleccionado.pabellon,
    estructura: this.clienteDtoSeleccionado.estructura,

    estado: "1"
  };

  this.Factura_Service.agregarFactura(factura).subscribe({

    next: (data: Factura) => {

      console.log("Factura guardada:", data);

      this.facturaGuardada = data;

      this.openModal(content);

    },

    error: (error) => {
      console.error(error);
      alert("Error al guardar la factura");
    }

  });

}

limpiarFormulario(): void {

  // limpiar clientes
  this.clienteRmtSeleccionado = {} as ClienteRtm;
  this.clienteDtoSeleccionado = {} as ClienteDto;

  // limpiar factura
  this.facturaGuardada = {} as Factura;

  // limpiar buscadores
  this.textoBusqueda = '';
  this.textoBusquedaDto = '';

}

imprimirFacturaYRotulo(id: number): void {
  // Imprimir Factura
  this.reporteService.imprimirFactura(id).subscribe({
    next: (data: Blob) => {
      const file = new Blob([data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(file);
      window.open(url, '_blank');
    },
    error: (err) => console.error('Error al imprimir factura:', err)
  });

  // Imprimir Rótulo
  this.reporteService.imprimirRotulo(id).subscribe({
    next: (data: Blob) => {
      const file = new Blob([data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(file);
      window.open(url, '_blank');
    },
    error: (err) => console.error('Error al imprimir rótulo:', err)
  });
}


}
