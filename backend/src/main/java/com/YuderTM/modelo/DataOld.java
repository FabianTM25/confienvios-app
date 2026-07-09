package com.YuderTM.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "data_old")
public class DataOld {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcliente")
    Integer idcliente;

    @Column(name = "cedula_rmt")
    String cedulaRmt;

    @Column(name = "nombre_rmt")
    String nombreRmt;

    @Column(name = "direccion_rmt")
    String direccionRmt;

    @Column(name = "nombre_dto")
    String nombreDto;

    @Column(name = "documento_dto")
    String documentoDto;

    @Column(name = "td")
    String td;

    @Column(name = "niu")
    String niu;

    @Column(name = "bloque")
    String bloque;

    @Column(name = "pabellon")
    String pabellon;

    @Column(name = "seccion")
    String seccion;

    public DataOld() {
    }

    public Integer getIdcliente() { return idcliente; }
    public void setIdcliente(Integer idcliente) { this.idcliente = idcliente; }

    public String getCedulaRmt() { return cedulaRmt; }
    public void setCedulaRmt(String cedulaRmt) { this.cedulaRmt = cedulaRmt; }

    public String getNombreRmt() { return nombreRmt; }
    public void setNombreRmt(String nombreRmt) { this.nombreRmt = nombreRmt; }

    public String getDireccionRmt() { return direccionRmt; }
    public void setDireccionRmt(String direccionRmt) { this.direccionRmt = direccionRmt; }

    public String getNombreDto() { return nombreDto; }
    public void setNombreDto(String nombreDto) { this.nombreDto = nombreDto; }

    public String getDocumentoDto() { return documentoDto; }
    public void setDocumentoDto(String documentoDto) { this.documentoDto = documentoDto; }

    public String getTd() { return td; }
    public void setTd(String td) { this.td = td; }

    public String getNiu() { return niu; }
    public void setNiu(String niu) { this.niu = niu; }

    public String getBloque() { return bloque; }
    public void setBloque(String bloque) { this.bloque = bloque; }

    public String getPabellon() { return pabellon; }
    public void setPabellon(String pabellon) { this.pabellon = pabellon; }

    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }
}
