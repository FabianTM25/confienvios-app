package com.YuderTM.modelo;

import jakarta.persistence.*;

@Entity
public class Cliente_dto {

    //atrubutos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="idClienteDto")            
    Integer idClienteDto;
    
    @Column(name ="nombreClienteDto")
    String nombreClienteDto;

    @Column(name = "tipoDocumentoDto")
    String tipoDocumentoDto;
    
    @Column(name ="documentoClienteDto")            
    String documentoClienteDto;
    
    @Column(name ="td")            
    String td;
    
    @Column(name ="niu")            
    String niu;
    
    @Column(name ="pabellon")            
    String pabellon;
    
    @Column(name ="estructura")
    String estructura;

    @Column(name ="estado")
    Integer estado;

    //constructor
    public Cliente_dto(){
    }

    //constructor para eliminar
    public Cliente_dto(Integer idClienteDto) {
        this.idClienteDto = idClienteDto;
    }

    //constructor para añadir
    public Cliente_dto(String nombreClienteDto, String documentoClienteDto, String td, String niu, String pabellon, String estructura) {
        this.nombreClienteDto = nombreClienteDto;
        this.documentoClienteDto = documentoClienteDto;
        this.td = td;
        this.niu = niu;
        this.pabellon = pabellon;
        this.estructura = estructura;
        this.estado = 1;
    }

    //constructor para actualizar

    public Cliente_dto(Integer idClienteDto, String nombreClienteDto, String tipoDocumentoDto, String documentoClienteDto, String td, String niu, String pabellon, String estructura,Integer estado) {
        this.idClienteDto = idClienteDto;
        this.nombreClienteDto = nombreClienteDto;
        this.tipoDocumentoDto = tipoDocumentoDto;
        this.documentoClienteDto = documentoClienteDto;
        this.td = td;
        this.niu = niu;
        this.pabellon = pabellon;
        this.estructura = estructura;
        this.estado = estado;
    }

    //getter and setter

    public Integer getIdClienteDto() {
        return idClienteDto;
    }
    public void setIdClienteDto(Integer idClientedto) {
        this.idClienteDto = idClientedto;
    }

    public String getNombreClienteDto() {
        return nombreClienteDto;
    }
    public void setNombreClienteDto(String nombreClienteDto) {
        this.nombreClienteDto = nombreClienteDto;
    }

    public String getTipoDocumentoDto()
    { return tipoDocumentoDto; }
    public void setTipoDocumentoDto(String tipoDocumentoDto)
    { this.tipoDocumentoDto = tipoDocumentoDto; }

    public String getDocumentoClienteDto() {
        return documentoClienteDto;
    }
    public void setDocumentoClienteDto(String documentoClienteDto) {
        this.documentoClienteDto = documentoClienteDto;
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

    public Integer getEstado() {
        return estado;
    }
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    //toString
    @Override
    public String toString() {
        return "Cliente_dto{" +
                "idClientedto=" + idClienteDto +
                ", nombreClienteDto='" + nombreClienteDto + '\'' +
                ", tipoDocumentoDto='" + tipoDocumentoDto + '\'' +
                ", documentoClienteDto='" + documentoClienteDto + '\'' +
                ", td='" + td + '\'' +
                ", niu='" + niu + '\'' +
                ", pabellon='" + pabellon + '\'' +
                ", estructura='" + estructura + '\'' +
                '}';
    }
}
