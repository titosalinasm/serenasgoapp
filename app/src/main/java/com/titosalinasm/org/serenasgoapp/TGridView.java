package com.titosalinasm.org.serenasgoapp;

/**
 * Created by Salinas on 30/01/2017.
 */
public class TGridView {
    private String imgCategoria;
    private String nombreCategoria;
    private int codigo;
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getImgCategoria() {
        return imgCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setImgCategoria(String imgCategoria) {
        this.imgCategoria = imgCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
