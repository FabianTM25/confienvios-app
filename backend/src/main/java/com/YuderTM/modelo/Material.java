package com.YuderTM.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_material;

    // Numero unico de radicado (se asigna al crear, con base en el id: MAT-0001)
    @Column(unique = true)
    String numero_radicado;

    // Datos del cliente rmt: se copian tal cual al guardar (sin relacion a la tabla original)
    String nombre_cliente_rmt;
    String tipo_documento_rmt;
    String documento_cliente_rmt;
    String direccion_cliente_rmt;
    String telefono_cliente_rmt;
    Integer estado_cliente_rmt;

    // Datos del cliente dto (destinatario): igual, se copian tal cual al guardar
    String nombre_cliente_dto;
    String tipo_documento_dto;
    String documento_cliente_dto;
    String td;
    String niu;
    String pabellon;
    String estructura;
    Integer estado_cliente_dto;

    // Datos propios del material
    String tipo_documento;
    String estado_envio;
    LocalDateTime fecha_recibido;
    LocalDateTime fecha_entrega;
    String observacion;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @PrePersist
    public void prePersist() {
        this.fecha_creacion = LocalDateTime.now(ZoneId.of("America/Bogota"));
        // La fecha de recibido es siempre la del primer guardado, no la edita el usuario
        this.fecha_recibido = this.fecha_creacion;
        if (this.estado_envio == null || this.estado_envio.isBlank()) {
            this.estado_envio = "PENDIENTE";
        }
    }

    public Material() {
    }

    public Integer getId_material() {
        return id_material;
    }

    public void setId_material(Integer id_material) {
        this.id_material = id_material;
    }

    public String getNumero_radicado() {
        return numero_radicado;
    }

    public void setNumero_radicado(String numero_radicado) {
        this.numero_radicado = numero_radicado;
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

    public Integer getEstado_cliente_rmt() {
        return estado_cliente_rmt;
    }

    public void setEstado_cliente_rmt(Integer estado_cliente_rmt) {
        this.estado_cliente_rmt = estado_cliente_rmt;
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

    public Integer getEstado_cliente_dto() {
        return estado_cliente_dto;
    }

    public void setEstado_cliente_dto(Integer estado_cliente_dto) {
        this.estado_cliente_dto = estado_cliente_dto;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getEstado_envio() {
        return estado_envio;
    }

    public void setEstado_envio(String estado_envio) {
        this.estado_envio = estado_envio;
    }

    public LocalDateTime getFecha_recibido() {
        return fecha_recibido;
    }

    public void setFecha_recibido(LocalDateTime fecha_recibido) {
        this.fecha_recibido = fecha_recibido;
    }

    public LocalDateTime getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(LocalDateTime fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id_material=" + id_material +
                ", numero_radicado='" + numero_radicado + '\'' +
                ", nombre_cliente_rmt='" + nombre_cliente_rmt + '\'' +
                ", tipo_documento_rmt='" + tipo_documento_rmt + '\'' +
                ", documento_cliente_rmt='" + documento_cliente_rmt + '\'' +
                ", direccion_cliente_rmt='" + direccion_cliente_rmt + '\'' +
                ", telefono_cliente_rmt='" + telefono_cliente_rmt + '\'' +
                ", nombre_cliente_dto='" + nombre_cliente_dto + '\'' +
                ", documento_cliente_dto='" + documento_cliente_dto + '\'' +
                ", tipo_documento='" + tipo_documento + '\'' +
                ", estado_envio='" + estado_envio + '\'' +
                ", fecha_recibido=" + fecha_recibido +
                ", fecha_entrega=" + fecha_entrega +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}
