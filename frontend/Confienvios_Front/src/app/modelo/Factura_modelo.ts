export interface Factura {
     id_factura: number| null;
    numero_factura: string;
    id_cliente_rmt: string;
    nombre_cliente_rmt: string;
    tipo_documento_rmt?: string;
    documento_cliente_rmt: string;
    direccion_cliente_rmt: string;
    telefono_cliente_rmt: string;
    id_cliente_dto: string;
    nombre_cliente_dto: string;
    tipo_documento_dto?: string;
    documento_cliente_dto: string;
    td: string;
    niu: string;
    pabellon: string;
    estructura: string;
    estado: string;   

}