import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbDropdownModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';

// Models & Services
import { ClienteRtm } from 'src/app/modelo/ClienteRmt_modelo';
import { clienteRmt_Service } from 'src/app/service/clienteRmt_service';
import { ClienteDto } from 'src/app/modelo/ClienteDto_modelo';
import { clienteDto_Service } from 'src/app/service/clienteDto_service';
import { Documento } from 'src/app/modelo/Documento_modelo';
import { DocumentoService } from 'src/app/service/documento_service';
import { ReporteDocumentoService } from 'src/app/service/reporteDocumento_service';
import { Material } from 'src/app/modelo/Material_modelo';
import { MaterialService } from 'src/app/service/material_service';
import { ReporteMaterialService } from 'src/app/service/reporteMaterial_service';

@Component({
  selector: 'app-documento',
  standalone: true,
  imports: [CommonModule, SharedModule, NgbDropdownModule, FormsModule],
  templateUrl: './documento.component.html',
  styleUrls: ['./documento.component.scss']
})
export class DocumentoComponent {
  private cd = inject(ChangeDetectorRef);
  private clienteRmtService = inject(clienteRmt_Service);
  private clienteDtoService = inject(clienteDto_Service);
  private documentoService = inject(DocumentoService);
  private reporteDocumentoService = inject(ReporteDocumentoService);
  private materialService = inject(MaterialService);
  private reporteMaterialService = inject(ReporteMaterialService);
  private modalService = inject(NgbModal);

  // Busqueda del cliente rmt
  textoBusqueda: string = '';
  clienteRmtSeleccionado: ClienteRtm = {} as ClienteRtm;

  // Busqueda del cliente dto
  textoBusquedaDto: string = '';
  clienteDtoSeleccionado: ClienteDto = {} as ClienteDto;

  // Datos propios del documento
  tipoDocumento: string = '';
  estadoEnvio: string = 'PENDIENTE';
  observacion: string = '';
  esMaterial: boolean = false;

  seleccionarTipoRegistro(esMaterial: boolean): void {
    this.esMaterial = esMaterial;
  }

  // Resultado tras guardar (puede ser un Documento o un Material, segun tipoGuardado)
  documentoGuardado: any = {};
  tipoGuardado: 'CORRESPONDENCIA' | 'MATERIALES' = 'CORRESPONDENCIA';

  // Edicion del cliente (modal)
  clienteEnEdicion: ClienteRtm = {} as ClienteRtm;
  clienteDtoEnEdicion: ClienteDto = {} as ClienteDto;

  buscarCliente(): void {
    if (!this.textoBusqueda) {
      alert('Ingrese el documento del cliente');
      return;
    }

    this.clienteRmtService.buscarClienteRmt(this.textoBusqueda).subscribe({
      next: (data: ClienteRtm) => {
        this.clienteRmtSeleccionado = data;
        this.cd.detectChanges();
      },
      error: (error) => {
        console.error(error);
        this.clienteRmtSeleccionado = {} as ClienteRtm;
        alert('Cliente no encontrado');
      }
    });
  }

  buscarClienteDto(): void {
    if (!this.textoBusquedaDto) {
      alert('Ingrese el documento del cliente');
      return;
    }

    this.clienteDtoService.buscarClienteDto(this.textoBusquedaDto).subscribe({
      next: (data: ClienteDto) => {
        this.clienteDtoSeleccionado = data;
        this.cd.detectChanges();
      },
      error: (error) => {
        console.error(error);
        this.clienteDtoSeleccionado = {} as ClienteDto;
        alert('Cliente no encontrado');
      }
    });
  }

  editarCliente(content: any): void {
    if (!this.clienteRmtSeleccionado?.idClienteRmt) {
      alert('Debe buscar un cliente antes de editarlo');
      return;
    }

    this.clienteEnEdicion = { ...this.clienteRmtSeleccionado };

    this.modalService.open(content, {
      centered: true,
      size: 'lg'
    });
  }

  guardarClienteEditado(modal: any): void {
    this.clienteRmtService.actualizarCliente_rmt(this.clienteEnEdicion).subscribe({
      next: (data: any) => {
        this.clienteRmtSeleccionado = data;
        this.cd.detectChanges();
        modal.close();
      },
      error: (error) => {
        console.error(error);
        alert('Error al actualizar el cliente');
      }
    });
  }

  editarClienteDto(content: any): void {
    if (!this.clienteDtoSeleccionado?.idClienteDto) {
      alert('Debe buscar un cliente antes de editarlo');
      return;
    }

    this.clienteDtoEnEdicion = { ...this.clienteDtoSeleccionado };

    this.modalService.open(content, {
      centered: true,
      size: 'lg'
    });
  }

  guardarClienteDtoEditado(modal: any): void {
    this.clienteDtoService.actualizarCliente_dto(this.clienteDtoEnEdicion).subscribe({
      next: (data: any) => {
        this.clienteDtoSeleccionado = data;
        this.cd.detectChanges();
        modal.close();
      },
      error: (error) => {
        console.error(error);
        alert('Error al actualizar el cliente');
      }
    });
  }

  imprimirGuardado(): void {
    const esMaterial = this.tipoGuardado === 'MATERIALES';
    const id = esMaterial ? this.documentoGuardado.id_material : this.documentoGuardado.id_documento;

    const peticion = esMaterial
      ? this.reporteMaterialService.imprimirMaterial(id)
      : this.reporteDocumentoService.imprimirDocumento(id);

    peticion.subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        window.open(url, '_blank');

        const link = document.createElement('a');
        link.href = url;
        link.download = `${esMaterial ? 'Material' : 'Documento'}_${id}.pdf`;
        link.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al generar el PDF:', err);
        alert('No se pudo generar el PDF');
      }
    });
  }

  guardar(content: any): void {
    if (!this.clienteRmtSeleccionado?.idClienteRmt) {
      alert('Debe buscar un cliente REMITE válido antes de guardar');
      return;
    }

    if (!this.clienteDtoSeleccionado?.idClienteDto) {
      alert('Debe buscar un cliente DESTINO válido antes de guardar');
      return;
    }

    if (!this.tipoDocumento) {
      alert('Ingrese el tipo de documento');
      return;
    }

    const datosComunes = {
      nombre_cliente_rmt: this.clienteRmtSeleccionado.nombreClienteRmt,
      tipo_documento_rmt: this.clienteRmtSeleccionado.tipoDocumentoRmt,
      documento_cliente_rmt: this.clienteRmtSeleccionado.documentoClienteRmt,
      direccion_cliente_rmt: this.clienteRmtSeleccionado.direccionClienteRmt,
      telefono_cliente_rmt: this.clienteRmtSeleccionado.telefonoClienteRmt,
      estado_cliente_rmt: this.clienteRmtSeleccionado.estado,
      nombre_cliente_dto: this.clienteDtoSeleccionado.nombreClienteDto,
      tipo_documento_dto: this.clienteDtoSeleccionado.tipoDocumentoDto,
      documento_cliente_dto: this.clienteDtoSeleccionado.documentoClienteDto,
      td: this.clienteDtoSeleccionado.td,
      niu: this.clienteDtoSeleccionado.niu,
      pabellon: this.clienteDtoSeleccionado.pabellon,
      estructura: this.clienteDtoSeleccionado.estructura,
      estado_cliente_dto: this.clienteDtoSeleccionado.estado,
      tipo_documento: this.tipoDocumento,
      estado_envio: this.estadoEnvio,
      observacion: this.observacion
    };

    const alGuardar = (data: Documento | Material) => {
      this.documentoGuardado = data;
      this.modalService.open(content, {
        backdrop: 'static',
        keyboard: false,
        centered: true,
        size: 'm'
      });
    };

    const alFallar = (error: any) => {
      console.error(error);
      const mensaje = error.error?.error || 'Error al guardar';
      alert(mensaje);
    };

    if (this.esMaterial) {
      this.tipoGuardado = 'MATERIALES';
      const material: Material = { id_material: null, ...datosComunes };
      this.materialService.agregarMaterial(material).subscribe({ next: alGuardar, error: alFallar });
    } else {
      this.tipoGuardado = 'CORRESPONDENCIA';
      const documento: Documento = { id_documento: null, ...datosComunes };
      this.documentoService.agregarDocumento(documento).subscribe({ next: alGuardar, error: alFallar });
    }
  }

  limpiarFormulario(): void {
    this.textoBusqueda = '';
    this.clienteRmtSeleccionado = {} as ClienteRtm;
    this.textoBusquedaDto = '';
    this.clienteDtoSeleccionado = {} as ClienteDto;
    this.tipoDocumento = '';
    this.estadoEnvio = 'PENDIENTE';
    this.observacion = '';
    this.esMaterial = false;
    this.documentoGuardado = {};
    this.tipoGuardado = 'CORRESPONDENCIA';
  }
}
