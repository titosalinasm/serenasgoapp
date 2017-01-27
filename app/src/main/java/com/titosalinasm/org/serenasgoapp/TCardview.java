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
    private String fotoperfil;
    private String nombre_entidad;
    private String hora_fech;

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

    public String getFotoperfil() {
        return fotoperfil;
    }

    public void setFotoperfil(String fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public String getNombre_entidad() {
        return nombre_entidad;
    }

    public void setHora_fech(String hora_fech) {
        this.hora_fech = hora_fech;
    }

    public String getHora_fech() {
        return hora_fech;
    }

    public void setNombre_entidad(String nombre_entidad) {
        this.nombre_entidad = nombre_entidad;
    }
}
