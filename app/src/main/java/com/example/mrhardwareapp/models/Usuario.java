package com.example.mrhardwareapp.models;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int idUsuario;
    private String email;
    private String username;
    private String password;
    private String avatar;
    private String descripcion;
    private int id_TipoUsuario;
    private String instagram;
    private String nombre;
    private int Pais_idPais;
    private int estado;
    private int codigo;
    private int confirmacion;
    private int meGusta;

    public Usuario(int idUsuario, String email, String username, String password, String avatar, String descripcion, int id_TipoUsuario, String instagram, String nombre, int pais_idPais, int estado, int codigo, int confirmacion, int meGusta) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.descripcion = descripcion;
        this.id_TipoUsuario = id_TipoUsuario;
        this.instagram = instagram;
        this.nombre = nombre;
        Pais_idPais = pais_idPais;
        this.estado = estado;
        this.codigo = codigo;
        this.confirmacion = confirmacion;
        this.meGusta = meGusta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_TipoUsuario() {
        return id_TipoUsuario;
    }

    public void setId_TipoUsuario(int id_TipoUsuario) {
        this.id_TipoUsuario = id_TipoUsuario;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPais_idPais() {
        return Pais_idPais;
    }

    public void setPais_idPais(int pais_idPais) {
        Pais_idPais = pais_idPais;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(int confirmacion) {
        this.confirmacion = confirmacion;
    }

    public int getMeGusta() {
        return meGusta;
    }

    public void setMeGusta(int meGusta) {
        this.meGusta = meGusta;
    }
}
