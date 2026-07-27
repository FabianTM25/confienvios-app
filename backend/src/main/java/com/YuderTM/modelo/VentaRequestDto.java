package com.YuderTM.modelo;

import java.math.BigDecimal;

// Cuerpo de entrada para crear una Venta: solo lo que el cajero captura.
// El resto (datos del cliente, valores calculados) los resuelve el servidor.
public class VentaRequestDto {

    private String numero_factura;
    private BigDecimal peso;
    private boolean con_caja;
    private String observaciones;
    private BigDecimal valor_avaluo;

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public boolean isCon_caja() {
        return con_caja;
    }

    public void setCon_caja(boolean con_caja) {
        this.con_caja = con_caja;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getValor_avaluo() {
        return valor_avaluo;
    }

    public void setValor_avaluo(BigDecimal valor_avaluo) {
        this.valor_avaluo = valor_avaluo;
    }
}
