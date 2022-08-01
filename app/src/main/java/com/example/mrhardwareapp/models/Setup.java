package com.example.mrhardwareapp.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Setup implements Serializable {

    private int idSetup;
    private int Usuario_idUsuario;
    private String nombre;
    private int copias;
    private List<Producto> producto_setup;

    public Setup() {
    }

    public Setup(int idSetup, int usuario_idUsuario, String nombre, int copias, List<Producto> producto_setup) {
        this.idSetup = idSetup;
        Usuario_idUsuario = usuario_idUsuario;
        this.nombre = nombre;
        this.copias = copias;
        this.producto_setup = producto_setup;
    }

    public int getIdSetup() {
        return idSetup;
    }

    public void setIdSetup(int idSetup) {
        this.idSetup = idSetup;
    }

    public int getUsuario_idUsuario() {
        return Usuario_idUsuario;
    }

    public void setUsuario_idUsuario(int usuario_idUsuario) {
        Usuario_idUsuario = usuario_idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCopias() {
        return copias;
    }

    public void setCopias(int copias) {
        this.copias = copias;
    }

    public List<Producto> getProducto_setup() {
        return producto_setup;
    }

    public void setProducto_setup(List<Producto> producto_setup) {
        this.producto_setup = producto_setup;
    }
    public static String vacios(String text){
        for (String t : Collections.singletonList("\\s")){
            text = text.replaceAll(t, "");
        }
        return text;
    }
}
