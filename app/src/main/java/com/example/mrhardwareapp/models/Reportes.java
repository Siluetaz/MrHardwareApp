package com.example.mrhardwareapp.models;

import java.util.ArrayList;
import java.util.List;

public class Reportes {
    private int idReporte;
    private Usuario id_Usuario;
    private int id_TipoReporte;
    private Comentario id_Comentario;

    public Reportes(int idReporte, Usuario id_Usuario, int id_TipoReporte, Comentario id_Comentario) {
        this.idReporte = idReporte;
        this.id_Usuario = id_Usuario;
        this.id_TipoReporte = id_TipoReporte;
        this.id_Comentario = id_Comentario;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public Usuario getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(Usuario id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public int getId_TipoReporte() {
        return id_TipoReporte;
    }

    public void setId_TipoReporte(int id_TipoReporte) {
        this.id_TipoReporte = id_TipoReporte;
    }

    public Comentario getId_Comentario() {
        return id_Comentario;
    }

    public void setId_Comentario(Comentario id_Comentario) {
        this.id_Comentario = id_Comentario;
    }
}
