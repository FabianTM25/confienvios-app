package com.YuderTM.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_factura;
    String numero_factura;
    Integer id_cliente_rmt;
    String nombre_cliente_rmt;
    String tipo_documento_rmt;
    String documento_cliente_rmt;
    String direccion_cliente_rmt;
    String telefono_cliente_rmt;
    Integer id_cliente_dto;
    String nombre_cliente_dto;
    String tipo_documento_dto;
    String documento_cliente_dto;
    String td;
    String niu;
    String pabellon;
    String estructura;
    String estado;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fecha_creacion;

    //constructor
    public Factura() {
    }
    //constructor para añadir
    public Factura(Integer id_cliente_rmt, String nombre_cliente_rmt, String tipo_documento_rmt, String documento_cliente_rmt, String direccion_cliente_rmt, String telefono_cliente_rmt, Integer id_cliente_dto, String nombre_cliente_dto, String tipo_documento_dto, String documento_cliente_dto, String td, String niu, String pabellon, String estructura) {
        this.id_cliente_rmt = id_cliente_rmt;
        this.nombre_cliente_rmt = nombre_cliente_rmt;
        this.tipo_documento_rmt = tipo_documento_rmt;
        this.documento_cliente_rmt = documento_cliente_rmt;
        this.direccion_cliente_rmt = direccion_cliente_rmt;
        this.telefono_cliente_rmt = telefono_cliente_rmt;
        this.id_cliente_dto = id_cliente_dto;
        this.nombre_cliente_dto = nombre_cliente_dto;
        this.tipo_documento_dto = tipo_documento_dto;
        this.documento_cliente_dto = documento_cliente_dto;
        this.td = td;
        this.niu = niu;
        this.pabellon = pabellon;
        this.estructura = estructura;
    }


    //getter y setter

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public Integer getId_cliente_rmt() {
        return id_cliente_rmt;
    }

    public void setId_cliente_rmt(Integer id_cliente_rmt) {
        this.id_cliente_rmt = id_cliente_rmt;
    }

    public Integer getId_factura() {
        return id_factura;
    }

    public void setId_factura(Integer id_factura) {
        this.id_factura = id_factura;
    }

    public String getNombre_cliente_rmt() {
        return nombre_cliente_rmt;
    }

    public void setNombre_cliente_rmt(String nombre_cliente_rmt) {
        this.nombre_cliente_rmt = nombre_cliente_rmt;
    }

    public String getTipo_documento_rmt() {
        return tipo_documento_rmt;
    }

    public void setTipo_documento_rmt(String tipo_documento_rmt) {
        this.tipo_documento_rmt = tipo_documento_rmt;
    }


    public String getDocumento_cliente_rmt() {
        return documento_cliente_rmt;
    }

    public void setDocumento_cliente_rmt(String documento_cliente_rmt) {
        this.documento_cliente_rmt = documento_cliente_rmt;
    }

    public String getDireccion_cliente_rmt() {
        return direccion_cliente_rmt;
    }

    public void setDireccion_cliente_rmt(String direccion_cliente_rmt) {
        this.direccion_cliente_rmt = direccion_cliente_rmt;
    }

    public String getTelefono_cliente_rmt() {
        return telefono_cliente_rmt;
    }

    public void setTelefono_cliente_rmt(String telefono_cliente_rmt) {
        this.telefono_cliente_rmt = telefono_cliente_rmt;
    }

    public Integer getId_cliente_dto() {
        return id_cliente_dto;
    }

    public void setId_cliente_dto(Integer id_cliente_dto) {
        this.id_cliente_dto = id_cliente_dto;
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
    public LocalDateTime getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(LocalDateTime fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    //toString

    @Override
    public String toString() {
        return "Factura{" +
                "id_factura=" + id_factura +
                ", numero_factura=" + numero_factura +
                ", id_cliente_rmt=" + id_cliente_rmt +
                ", nombre_cliente_rmt='" + nombre_cliente_rmt + '\'' +
                ", tipo_documento_rmt='" + tipo_documento_rmt + '\'' +
                ", documento_cliente_rmt='" + documento_cliente_rmt + '\'' +
                ", direccion_cliente_rmt='" + direccion_cliente_rmt + '\'' +
                ", telefono_cliente_rmt='" + telefono_cliente_rmt + '\'' +
                ", id_cliente_dto=" + id_cliente_dto +
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
