package com.example.mrhardwareapp.models;

import java.io.Serializable;
import java.util.Date;

public class Comentario implements Serializable {

    private int idComentario;
    private Date fecha;
    private String comentario;
    private int votoPositivo;
    private int id_Producto;
    private Usuario id_Usuario;
    private int id_Comentario_Respuesta;
    private int cantidad_Reportes;
    private int estado_Comentario;
    private int idComentario_Principal;

    public Comentario(int idComentario, Date fecha, String comentario, int votoPositivo, int id_Producto, Usuario id_Usuario, int id_Comentario_Respuesta, int cantidad_Reportes, int estado_Comentario, int idComentario_Principal) {
        this.idComentario = idComentario;
        this.fecha = fecha;
        this.comentario = comentario;
        this.votoPositivo = votoPositivo;
        this.id_Producto = id_Producto;
        this.id_Usuario = id_Usuario;
        this.id_Comentario_Respuesta = id_Comentario_Respuesta;
        this.cantidad_Reportes = cantidad_Reportes;
        this.estado_Comentario = estado_Comentario;
        this.idComentario_Principal = idComentario_Principal;
    }

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getVotoPositivo() {
        return votoPositivo;
    }

    public void setVotoPositivo(int votoPositivo) {
        this.votoPositivo = votoPositivo;
    }

    public int getId_Producto() {
        return id_Producto;
    }

    public void setId_Producto(int id_Producto) {
        this.id_Producto = id_Producto;
    }

    public Usuario getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(Usuario id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public int getId_Comentario_Respuesta() {
        return id_Comentario_Respuesta;
    }

    public void setId_Comentario_Respuesta(int id_Comentario_Respuesta) {
        this.id_Comentario_Respuesta = id_Comentario_Respuesta;
    }

    public int getCantidad_Reportes() {
        return cantidad_Reportes;
    }

    public void setCantidad_Reportes(int cantidad_Reportes) {
        this.cantidad_Reportes = cantidad_Reportes;
    }

    public int getEstado_Comentario() {
        return estado_Comentario;
    }

    public void setEstado_Comentario(int estado_Comentario) {
        this.estado_Comentario = estado_Comentario;
    }    public int getIdComentario_Principal() {
        return idComentario_Principal;
    }

    public void setIdComentario_Principal(int idComentario_Principal) {
        this.idComentario_Principal = idComentario_Principal;
    }
}
