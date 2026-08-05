import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbDropdownModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';

// Models & Services
import { ClienteRtm } from 'src/app/modelo/ClienteRmt_modelo';
import { clienteRmt_Service } from 'src/app/service/clienteRmt_service';
import { Documento } from 'src/app/modelo/Documento_modelo';
import { DocumentoService } from 'src/app/service/documento_service';
import { ReporteDocumentoService } from 'src/app/service/reporteDocumento_service';

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
  private documentoService = inject(DocumentoService);
  private reporteDocumentoService = inject(ReporteDocumentoService);
  private modalService = inject(NgbModal);

  // Busqueda del cliente rmt
  textoBusqueda: string = '';
  clienteRmtSeleccionado: ClienteRtm = {} as ClienteRtm;

  // Datos propios del documento
  tipoDocumento: string = '';
  estadoEnvio: string = 'PENDIENTE';
  fechaEntrega: string | null = null;
  observacion: string = '';

  documentoGuardado: Documento = {} as Documento;

  // Edicion del cliente (modal)
  clienteEnEdicion: ClienteRtm = {} as ClienteRtm;

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

  imprimirDocumento(id: number): void {
    this.reporteDocumentoService.imprimirDocumento(id).subscribe({
      next: (data: Blob) => {
        const file = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(file);

        window.open(url, '_blank');

        const link = document.createElement('a');
        link.href = url;
        link.download = `Documento_${id}.pdf`;
        link.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error al generar el documento:', err);
        alert('No se pudo generar el PDF del documento');
      }
    });
  }

  guardar(content: any): void {
    if (!this.clienteRmtSeleccionado?.idClienteRmt) {
      alert('Debe buscar un cliente válido antes de guardar');
      return;
    }

    if (!this.tipoDocumento) {
      alert('Ingrese el tipo de documento');
      return;
    }

    const documento: Documento = {
      id_documento: null,
      nombre_cliente_rmt: this.clienteRmtSeleccionado.nombreClienteRmt,
      tipo_documento_rmt: this.clienteRmtSeleccionado.tipoDocumentoRmt,
      documento_cliente_rmt: this.clienteRmtSeleccionado.documentoClienteRmt,
      direccion_cliente_rmt: this.clienteRmtSeleccionado.direccionClienteRmt,
      telefono_cliente_rmt: this.clienteRmtSeleccionado.telefonoClienteRmt,
      estado_cliente_rmt: this.clienteRmtSeleccionado.estado,
      tipo_documento: this.tipoDocumento,
      estado_envio: this.estadoEnvio,
      fecha_entrega: this.fechaEntrega,
      observacion: this.observacion
    };

    this.documentoService.agregarDocumento(documento).subscribe({
      next: (data: Documento) => {
        this.documentoGuardado = data;
        this.modalService.open(content, {
          backdrop: 'static',
          keyboard: false,
          centered: true,
          size: 'm'
        });
      },
      error: (error) => {
        console.error(error);
        const mensaje = error.error?.error || 'Error al guardar el documento';
        alert(mensaje);
      }
    });
  }

  limpiarFormulario(): void {
    this.textoBusqueda = '';
    this.clienteRmtSeleccionado = {} as ClienteRtm;
    this.tipoDocumento = '';
    this.estadoEnvio = 'PENDIENTE';
    this.fechaEntrega = null;
    this.observacion = '';
    this.documentoGuardado = {} as Documento;
  }
}
