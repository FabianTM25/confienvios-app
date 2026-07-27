package com.YuderTM.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Usuario {

    //atrubutos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_usuario;
    String usuario;
    String user_name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    // 1 = activo, 2 = eliminado (soft-delete, igual que Cliente_dto/Cliente_rmt)
    Integer estado = 1;

    //metodos

    //constructor
    public Usuario() {
    }
//constructor para eliminar
    public Usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    //constructor para adicionar
    public Usuario(String usuario, String user_name, String password) {
        this.usuario = usuario;
        this.user_name = user_name;
        this.password = password;
    }

    //constructor para actualizar
    public Usuario(Integer id_usuario, String usuario, String user_name, String password) {
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.user_name = user_name;
        this.password = password;
    }

    //getter y setter
    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario= id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return "Usuario{" +
                "id_usuario=" + id_usuario +
                ", usuario='" + usuario + '\'' +
                ", user_name='" + user_name + '\'' +
                ", estado=" + estado +
                '}';
    }
}
