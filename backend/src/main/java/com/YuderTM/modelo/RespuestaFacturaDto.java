package com.YuderTM.modelo;

import com.YuderTM.modelo.Factura;

public class RespuestaFacturaDto {

    private Factura factura;
    private String warning;
    private boolean requiereAutorizacion;

    // Constructor vacío
    public RespuestaFacturaDto() {}

    // Getters y Setters
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public String getWarning() { return warning; }
    public void setWarning(String warning) { this.warning = warning; }

    public boolean isRequiereAutorizacion() { return requiereAutorizacion; }
    public void setRequiereAutorizacion(boolean requiereAutorizacion) {
        this.requiereAutorizacion = requiereAutorizacion;
    }
}