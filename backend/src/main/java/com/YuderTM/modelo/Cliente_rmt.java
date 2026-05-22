package com.YuderTM.modelo;

import jakarta.persistence.*;

@Entity
public class Cliente_rmt {

    //atrubutos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "idClienteRmt")
    Integer idClienteRmt;
    
    @Column(name="nombreClienteRmt")
    String nombreClienteRmt;

    @Column(name = "tipoDocumentoRmt")
    String tipoDocumentoRmt;

    @Column(name="documentoClienteRmt")
    String documentoClienteRmt;

    @Column(name="direccionClienteRmt")
    String direccionClienteRmt;

    @Column(name="telefonoClienteRmt")
    String telefonoClienteRmt;

    @Column(name ="estado")
    Integer estado;

    //constructor
    public Cliente_rmt(){
    }

    //constructor para eliminar
    public Cliente_rmt(Integer idClienteRmt) {
        this.idClienteRmt = idClienteRmt;
    }
    //constructor para añadir
    public Cliente_rmt(String nombreClienteRmt, String documentoClienteRmt, String direccionClienteRmt, String telefonoClienteRmt) {
        this.nombreClienteRmt = nombreClienteRmt;
        this.documentoClienteRmt = documentoClienteRmt;
        this.direccionClienteRmt = direccionClienteRmt;
        this.telefonoClienteRmt = telefonoClienteRmt;
        this.estado = 1;
    }
    //constructor para actualizar
    public Cliente_rmt(Integer idClienteRmt, String nombreClienteRmt, String documentoClienteRmt, String direccionClienteRmt, String telefonoClienteRmt,Integer estado) {
        this.idClienteRmt = idClienteRmt;
        this.nombreClienteRmt = nombreClienteRmt;
        this.documentoClienteRmt = documentoClienteRmt;
        this.direccionClienteRmt = direccionClienteRmt;
        this.telefonoClienteRmt = telefonoClienteRmt;
        this.estado = estado;
    }
//getter and setter
    public Integer getIdClienteRmt() {return idClienteRmt; }
    public void setIdClienteRmt(Integer idClienteRmt) {
        this.idClienteRmt = idClienteRmt;
    }

    public String getNombreClienteRmt() {return nombreClienteRmt;}
    public void setNombreClienteRmt(String nombreClienteRmt) {
        this.nombreClienteRmt = nombreClienteRmt;
    }

    public String getTipoDocumentoRmt() { return tipoDocumentoRmt; }
    public void setTipoDocumentoRmt(String tipoDocumentoRmt)
    { this.tipoDocumentoRmt = tipoDocumentoRmt; }

    public String getDocumentoClienteRmt() {return documentoClienteRmt;}
    public void setDocumentoClienteRmt(String documentoClienteRmt) {
        this.documentoClienteRmt = documentoClienteRmt;
    }
    public String getDireccionClienteRmt() {return direccionClienteRmt;}
    public void setDireccionClienteRmt(String direccionClienteRmt) {
        this.direccionClienteRmt = direccionClienteRmt;
    }
    public String getTelefonoClienteRmt() {return telefonoClienteRmt;}
    public void setTelefonoClienteRmt(String telefonoClienteRmt) {
        this.telefonoClienteRmt = telefonoClienteRmt;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    //toString

    @Override
    public String toString() {
        return  "Cliente_rmt{" +
                "idClienteRmt=" + idClienteRmt +
                ", nombreClienteRmt='" + nombreClienteRmt + '\'' +
                ", documentoClienteRmt='" + documentoClienteRmt + '\'' +
                ", direccionClienteRmt='" + direccionClienteRmt + '\'' +
                ", telefonoClienteRmt='" + telefonoClienteRmt + '\'' +
                '}';
    }
}
