package com.YuderTM.modelo;


import jakarta.persistence.*;

@Entity
@Table(name = "rotulo")
public class Rotulo {

        @Id
        private Integer id_rotulo;
        private String n_factura;
        private String nombre_cliente_dto;
        private String tipo_documento_dto;
        private String documento_cliente_dto;
        private String td;
        private String niu;
        private String pabellon;
        private String estructura;
        private String estado;


    // getters y setters
    public Integer getId_rotulo() {
        return id_rotulo;
    }

    public void setId_rotulo(Integer id_rotulo) {
        this.id_rotulo = id_rotulo;
    }

    public String getN_factura() {
        return n_factura;
    }

    public void setN_factura(String n_factura) {
        this.n_factura = n_factura;
    }

    public String getNombre_cliente_dto() {
        return nombre_cliente_dto;
    }

    public void setNombre_cliente_dto(String nombre_cliente_dto) {
        this.nombre_cliente_dto = nombre_cliente_dto;
    }

    public String getTipo_documento_dto() {
        return tipo_documento_dto;
    }

    public void setTipo_documento_dto(String tipo_documento_dto) {
        this.tipo_documento_dto = tipo_documento_dto;
    }

    public String getDocumento_cliente_dto() {
        return documento_cliente_dto;
    }

    public void setDocumento_cliente_dto(String documento_cliente_dto) {
        this.documento_cliente_dto = documento_cliente_dto;
    }

    public String getTd() {
        return td;
    }

    public void setTd(String td) {
        this.td = td;
    }

    public String getNiu() {
        return niu;
    }

    public void setNiu(String niu) {
        this.niu = niu;
    }

    public String getPabellon() {
        return pabellon;
    }

    public void setPabellon(String pabellon) {
        this.pabellon = pabellon;
    }

    public String getEstructura() {
        return estructura;
    }

    public void setEstructura(String estructura) {
        this.estructura = estructura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    //ToString

    @Override
    public String toString() {
        return "Rotulo{" +
                "id_rotulo=" + id_rotulo +
                ", n_factura='" + n_factura + '\'' +
                ", nombre_cliente_dto='" + nombre_cliente_dto + '\'' +
                ", tipo_documento_dto='" + tipo_documento_dto + '\'' +
                ", documento_cliente_dto='" + documento_cliente_dto + '\'' +
                ", td='" + td + '\'' +
                ", niu='" + niu + '\'' +
                ", pabellon='" + pabellon + '\'' +
                ", estructura='" + estructura + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}

