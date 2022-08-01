package com.example.mrhardwareapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Producto implements Serializable {

    private int idProducto;
    private String nombre;
    private int precio;
    private int Detalle_idDetalle;
    private int id_TipoProducto;
    private String link;
    private Tiendas Tiendas_idTiendas;
    private int vistas;
    private String linkTienda;

    public Producto(int idProducto, String nombre, int precio, int detalle_idDetalle, int id_TipoProducto, String link, Tiendas tiendas_idTiendas, int vistas, String linkTienda) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        Detalle_idDetalle = detalle_idDetalle;
        this.id_TipoProducto = id_TipoProducto;
        this.link = link;
        Tiendas_idTiendas = tiendas_idTiendas;
        this.vistas = vistas;
        this.linkTienda = linkTienda;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
            return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getDetalle_idDetalle() {
        return Detalle_idDetalle;
    }

    public void setDetalle_idDetalle(int detalle_idDetalle) {
        Detalle_idDetalle = detalle_idDetalle;
    }

    public int getId_TipoProducto() {
        return id_TipoProducto;
    }

    public void setId_TipoProducto(int id_TipoProducto) {
        this.id_TipoProducto = id_TipoProducto;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Tiendas getTiendas_idTiendas() {
        return Tiendas_idTiendas;
    }

    public void setTiendas_idTiendas(Tiendas tiendas_idTiendas) {
        Tiendas_idTiendas = tiendas_idTiendas;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }

    public String getLinkTienda() {
        return linkTienda;
    }

    public void setLinkTienda(String linkTienda) {
        this.linkTienda = linkTienda;
    }

    public String getPrecioConSignos(){
        String precioS = String.valueOf(precio);
        int pos = 4;
        StringBuilder stringBuilder= new StringBuilder(precioS);
        ArrayList<String> array = new ArrayList<>();
        for (int i = precioS.length(); i > 0;i--){
            array.add(precioS.substring(i));
            if (array.size() == pos){
                stringBuilder.insert(i,".");
                pos = pos+3;
            }
        }
        return "$"+stringBuilder;
    }
}
