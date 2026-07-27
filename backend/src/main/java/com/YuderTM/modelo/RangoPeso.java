package com.YuderTM.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class RangoPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_rango;

    BigDecimal peso_desde;
    BigDecimal peso_hasta;
    BigDecimal valor;

    public RangoPeso() {
    }

    public Integer getId_rango() {
        return id_rango;
    }

    public void setId_rango(Integer id_rango) {
        this.id_rango = id_rango;
    }

    public BigDecimal getPeso_desde() {
        return peso_desde;
    }

    public void setPeso_desde(BigDecimal peso_desde) {
        this.peso_desde = peso_desde;
    }

    public BigDecimal getPeso_hasta() {
        return peso_hasta;
    }

    public void setPeso_hasta(BigDecimal peso_hasta) {
        this.peso_hasta = peso_hasta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "RangoPeso{" +
                "id_rango=" + id_rango +
                ", peso_desde=" + peso_desde +
                ", peso_hasta=" + peso_hasta +
                ", valor=" + valor +
                '}';
    }
}
