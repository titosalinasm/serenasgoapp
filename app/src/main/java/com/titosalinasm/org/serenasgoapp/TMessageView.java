package com.titosalinasm.org.serenasgoapp;

/**
 * Created by Salinas on 11/02/2017.
 */

public class TMessageView {
    private String imgAvatar;
    private String nombreRemitente;
    private String mensajeRemitente;
    private String fechHora;
    private String idusuariosuario;

    public String getIdusuariosuario() {
        return idusuariosuario;
    }

    public void setIdusuariosuario(String idusuariosuario) {
        this.idusuariosuario = idusuariosuario;
    }

    public String getFechHora() {
        return fechHora;
    }

    public String getImgAvatar() {
        return imgAvatar;
    }

    public String getMensajeRemitente() {
        return mensajeRemitente;
    }

    public String getNombreRemitente() {
        return nombreRemitente;
    }

    public void setFechHora(String fechHora) {
        this.fechHora = fechHora;
    }

    public void setImgAvatar(String imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public void setNombreRemitente(String nombreRemitente) {
        this.nombreRemitente = nombreRemitente;
    }

    public void setMensajeRemitente(String mensajeRemitente) {
        this.mensajeRemitente = mensajeRemitente;
    }
}
