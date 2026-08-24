export interface Venta {
    id_venta: number | null;
    numero_venta?: string;
    id_factura: number | null;
    numero_factura: string;
    nombre_cliente_dto?: string;
    documento_cliente_dto?: string;
    nombre_cliente_rmt?: string;
    tipo_documento_rmt?: string;
    documento_cliente_rmt?: string;
    telefono_cliente_rmt?: string;
    peso: number;
    valor_peso?: number;
    con_caja: boolean;
    precio_caja_aplicado?: number;
    valor_envio?: number;
    observaciones?: string;
    valor_avaluo?: number;
    vendedor?: string;
    forma_pago?: string;
    estado?: string;
    fecha_creacion?: string;
}
