export interface Documento {
  id_documento: number | null;
  numero_radicado?: string;

  // Snapshot del cliente rmt (se copian al guardar, no se referencian)
  nombre_cliente_rmt: string;
  tipo_documento_rmt?: string;
  documento_cliente_rmt: string;
  direccion_cliente_rmt?: string;
  telefono_cliente_rmt?: string;
  estado_cliente_rmt?: number;

  // Datos propios del documento
  tipo_documento: string;
  estado_envio?: string;
  fecha_recibido?: string | null;
  fecha_entrega?: string | null;
  observacion?: string;

  fecha_creacion?: string;
}
