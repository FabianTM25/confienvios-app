package com.YuderTM.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Configuracion {

    @Id
    Integer id_configuracion;

    BigDecimal precio_caja;

    public Configuracion() {
    }

    public Integer getId_configuracion() {
        return id_configuracion;
    }

    public void setId_configuracion(Integer id_configuracion) {
        this.id_configuracion = id_configuracion;
    }

    public BigDecimal getPrecio_caja() {
        return precio_caja;
    }

    public void setPrecio_caja(BigDecimal precio_caja) {
        this.precio_caja = precio_caja;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
                "id_configuracion=" + id_configuracion +
                ", precio_caja=" + precio_caja +
                '}';
    }
}
