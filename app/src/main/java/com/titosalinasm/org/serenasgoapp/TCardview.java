package com.titosalinasm.org.serenasgoapp;

import java.security.PrivateKey;

/**
 * Created by Salinas on 04/01/2017.
 */
public class TCardview {
    private String imagen;
    private String nombre;
    private String descripcion;
    private int codigo;

    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

}
