package com.YuderTM.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_venta;

    String numero_venta;

    Integer id_factura;
    String numero_factura;

    String nombre_cliente_dto;
    String documento_cliente_dto;

    String nombre_cliente_rmt;
    String tipo_documento_rmt;
    String documento_cliente_rmt;
    String telefono_cliente_rmt;

    BigDecimal peso;
    BigDecimal valor_peso;

    Boolean con_caja;
    BigDecimal precio_caja_aplicado;

    BigDecimal valor_envio;

    String observaciones;
    BigDecimal valor_avaluo;

    String vendedor;

    String forma_pago;

    String estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @PrePersist
    public void prePersist() {
        this.fecha_creacion = LocalDateTime.now(ZoneId.of("America/Bogota"));
        if (this.estado == null) {
            this.estado = "1";
        }
    }

    public Venta() {
    }

    public Integer getId_venta() {
        return id_venta;
    }

    public void setId_venta(Integer id_venta) {
        this.id_venta = id_venta;
    }

    public String getNumero_venta() {
        return numero_venta;
    }

    public void setNumero_venta(String numero_venta) {
        this.numero_venta = numero_venta;
    }

    public Integer getId_factura() {
        return id_factura;
    }

    public void setId_factura(Integer id_factura) {
        this.id_factura = id_factura;
    }

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

    public String getNombre_cliente_dto() {
        return nombre_cliente_dto;
    }

    public void setNombre_cliente_dto(String nombre_cliente_dto) {
        this.nombre_cliente_dto = nombre_cliente_dto;
    }

    public String getDocumento_cliente_dto() {
        return documento_cliente_dto;
    }

    public void setDocumento_cliente_dto(String documento_cliente_dto) {
        this.documento_cliente_dto = documento_cliente_dto;
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

    public String getTelefono_cliente_rmt() {
        return telefono_cliente_rmt;
    }

    public void setTelefono_cliente_rmt(String telefono_cliente_rmt) {
        this.telefono_cliente_rmt = telefono_cliente_rmt;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getValor_peso() {
        return valor_peso;
    }

    public void setValor_peso(BigDecimal valor_peso) {
        this.valor_peso = valor_peso;
    }

    public Boolean getCon_caja() {
        return con_caja;
    }

    public void setCon_caja(Boolean con_caja) {
        this.con_caja = con_caja;
    }

    public BigDecimal getPrecio_caja_aplicado() {
        return precio_caja_aplicado;
    }

    public void setPrecio_caja_aplicado(BigDecimal precio_caja_aplicado) {
        this.precio_caja_aplicado = precio_caja_aplicado;
    }

    public BigDecimal getValor_envio() {
        return valor_envio;
    }

    public void setValor_envio(BigDecimal valor_envio) {
        this.valor_envio = valor_envio;
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

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id_venta=" + id_venta +
                ", numero_venta='" + numero_venta + '\'' +
                ", id_factura=" + id_factura +
                ", numero_factura='" + numero_factura + '\'' +
                ", nombre_cliente_dto='" + nombre_cliente_dto + '\'' +
                ", documento_cliente_dto='" + documento_cliente_dto + '\'' +
                ", nombre_cliente_rmt='" + nombre_cliente_rmt + '\'' +
                ", tipo_documento_rmt='" + tipo_documento_rmt + '\'' +
                ", documento_cliente_rmt='" + documento_cliente_rmt + '\'' +
                ", telefono_cliente_rmt='" + telefono_cliente_rmt + '\'' +
                ", peso=" + peso +
                ", valor_peso=" + valor_peso +
                ", con_caja=" + con_caja +
                ", precio_caja_aplicado=" + precio_caja_aplicado +
                ", valor_envio=" + valor_envio +
                ", observaciones='" + observaciones + '\'' +
                ", valor_avaluo=" + valor_avaluo +
                ", vendedor='" + vendedor + '\'' +
                ", forma_pago='" + forma_pago + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
