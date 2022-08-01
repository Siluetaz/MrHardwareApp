package com.example.mrhardwareapp.models;

import java.io.Serializable;

public class Pais implements Serializable {

    private String idPais;
    private String nombre;

    public Pais(String idPais, String nombre) {
        this.idPais = idPais;
        this.nombre = nombre;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
