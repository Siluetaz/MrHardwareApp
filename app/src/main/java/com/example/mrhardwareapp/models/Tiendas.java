package com.example.mrhardwareapp.models;

import java.io.Serializable;

public class Tiendas implements Serializable {

    private int idTiendas;
    private String tienda;

    public Tiendas(int idTiendas, String tienda) {
        this.idTiendas = idTiendas;
        this.tienda = tienda;
    }

    public int getIdTiendas() {
        return idTiendas;
    }

    public void setIdTiendas(int idTiendas) {
        this.idTiendas = idTiendas;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }
}
