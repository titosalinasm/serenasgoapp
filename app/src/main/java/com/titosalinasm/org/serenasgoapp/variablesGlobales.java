package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Salinas on 12/08/2016.
 */
public class variablesGlobales {
    public static String paginaweb="http://serapphuaraz.com/android/";
    public static int cantidadpublicaciones=0;
    public static int limite_inferior_publicacion=2;
    public static int codigo_layout=1;
    public static String url_img_pub="http://serapphuaraz.com/administrador/imgpublicaciones/";
    public static String url_img_categorias="http://serapphuaraz.com/administrador/imgcategoria/";
    public static int sucesofirebase=1;
    public static String idusuario;
    //Longitud y latitud
    public static String latitud;
    public static String longitud;
    //.Longitud y latitud

    public static String idusuario_movil="";
    public static String avatar_movil="";
    public static String nombre_movil="";

    public static boolean estado_movil=false;
    public static boolean existencia_usuario=false;
    public static boolean ccrear_persona=false;
    public static String mensajes="";
    public static String tutorial_player;

    public static String contacto1;
    public static String contacto2;
    public static String contacto3;


}
