package com.titosalinasm.org.serenasgoapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Salinas on 12/08/2016.
 */

public class sesionMovilUser {
    private static final String TAG = sesionMovilUser.class.getSimpleName();
    private ListView lista;
    private Adaptador adapter;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refReportado = ref.child("reportado");

    public void estado_sesion_movil(final Context context, final RequestQueue req, final String ime, final ProgressBar progressBar, final Button button){

        if(getusuario(context).toString().length()>0){
            //Toast.makeText(context, getusuario(context).toString(), Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"estado_sesion_movil.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject respuestaJSON = new JSONObject(response.toString());
                                //verifica si el movil esta registrado
                                if (respuestaJSON.getString("estadox").equals("1")){
                                    // Log.d("nombretito",respuestaJSON.getJSONObject("usuariocompleto").getString("nombres") );
                                    Intent intent = new Intent(context, home.class);
                                    intent.putExtra("usuario",respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                    intent.putExtra("idusuario", respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") );
                                    intent.putExtra("foto", respuestaJSON.getJSONObject("usuariocompleto").getString("avatar"));
                                    intent.putExtra("nombres", respuestaJSON.getJSONObject("usuariocompleto").getString("nombres"));
                                    intent.putExtra("apellidos", respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos"));

                                    variablesGlobales.idusuario_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") ;
                                    variablesGlobales.avatar_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("avatar");
                                    variablesGlobales.nombre_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("nombres")+" "+
                                            respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos");
                                    context.startActivity(intent);
                                    ((Activity)(context)).finish();
                                }else{
                                    // Toast.makeText(context, "Abrir activity iniciar sesion", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, iniciarSesion.class);
                                    context.startActivity(intent);
                                    ((Activity)(context)).finish();
                                }
                            } catch (JSONException e) {
                                button.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    button.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(context, "Lo sentimos, algo salió mal: "+error, Toast.LENGTH_LONG).show();
                }
            }){
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("usuario", getusuario(context).toString());
                    params.put("clave", getclave(context).toString());
                    return params;
                }
            };
// Add the request to the RequestQueue.
            req.add(stringRequest);
        }else{
            Intent intent = new Intent(context, iniciarSesion.class);
            context.startActivity(intent);
            ((Activity)(context)).finish();
        }

    }
    public void existencia_usuario(final Context context, final RequestQueue req, final ProgressDialog progressDialog, final String usu, final String clave){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"verificaUsuario.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el movil esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                progressDialog.dismiss();
                                Toast.makeText(context, "Este correo ya esta registrado.", Toast.LENGTH_LONG).show();

                            }else{
                                progressDialog.dismiss();
                                Intent i = new Intent(context, registrarUsuario.class);
                                i.putExtra("correo", usu);
                                i.putExtra("clave", clave);
                                context.startActivity(i);
                            }

                            Log.d(TAG, respuestaJSON.getString("mensaje") );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(sesionMovilUser.this, "Error: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usuario", usu);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }

    public void crear_nuevo_usuario_correo(RequestQueue req, final String usu, String clave, String nombres,
                                           final String apellidos, String genero,
                                           String direccion, String telefono, String fabricante, String version_so,
                                           String modelo, String marca, final String imei, String codigo_confirmacion,
                                           final String social, final String avatar, final Context context,
                                           final ProgressDialog loading){

        //aquí todos
        final RequestQueue req1=req;
        //datos de persona
        final String fnombres=nombres;
        final String fapellidos=apellidos;
        final String fgenero=genero;
        final String fdireccion=direccion;
        final String ftelefono=telefono;

        //datos de usuario
        final String fusuario=usu;
        final String fclave=clave;

        //datos del telefono
        final String ffabricante=fabricante;
        final String fversion_so=version_so;
        final String fmodelo=modelo;
        final String fmarca=marca;
        final String fimei=imei;
        final String fcodigo_confirmacion=codigo_confirmacion;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"crear_nuevo_usuario_correo.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el movil esta registrado
                                if (respuestaJSON.getString("estado").equals("1")){
                                    saveusuario(context, respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                    saveclave(context, respuestaJSON.getJSONObject("usuariocompleto").getString("clave_usuario"));
                                    Intent intent = new Intent(context, home.class);
                                    intent.putExtra("usuario",respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                    intent.putExtra("idusuario", respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") );
                                    intent.putExtra("foto", respuestaJSON.getJSONObject("usuariocompleto").getString("avatar"));
                                    intent.putExtra("nombres", respuestaJSON.getJSONObject("usuariocompleto").getString("nombres"));
                                    intent.putExtra("apellidos", respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos"));

                                    variablesGlobales.idusuario_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") ;
                                    variablesGlobales.avatar_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("avatar");
                                    variablesGlobales.nombre_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("nombres")+" "+
                                            respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos");

                                    context.startActivity(intent);
                                    ((Activity)(context)).finish();
                                    loading.dismiss();
                                }else{

                                    Toast.makeText(context,"No se pudo registrar el usuario. Intentelo otra vez." ,Toast.LENGTH_LONG).show();
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Algo salío mal. Por favor revisa tu conexion a internet." ,Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //datos de personas
                params.put("nombresx", fnombres);
                params.put("apellidosx", fapellidos);
                params.put("generox", fgenero);
                params.put("direccionx", fdireccion);
                params.put("telefonox", ftelefono);

                //datos de usuario
                params.put("social", social);
                params.put("usuariox", fusuario);
                params.put("clavex", fclave);
                params.put("avatar", avatar);
                params.put("codigo_confirmacionx", fcodigo_confirmacion);

                //datos de telefono
                params.put("fabricantex", ffabricante);
                params.put("version_sox", fversion_so);
                params.put("modelox", fmodelo);
                params.put("marcax", fmarca);
                params.put("imeix", fimei);


                return params;
            }
        };
// Add the request to the RequestQueue.
        req1.add(stringRequest);

    }
    public void validar_usuario(final Context context, final RequestQueue req, final String usuario, final String clave, final ProgressDialog loading){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"compruebausuario.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el movil esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                saveusuario(context, respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                saveclave(context, respuestaJSON.getJSONObject("usuariocompleto").getString("clave_usuario"));

                                Intent intent = new Intent(context, home.class);
                                intent.putExtra("usuario",respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                intent.putExtra("idusuario", respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") );
                                intent.putExtra("foto", respuestaJSON.getJSONObject("usuariocompleto").getString("avatar"));
                                intent.putExtra("nombres", respuestaJSON.getJSONObject("usuariocompleto").getString("nombres"));
                                intent.putExtra("apellidos", respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos"));
                                variablesGlobales.idusuario_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") ;
                                variablesGlobales.avatar_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("avatar");
                                variablesGlobales.nombre_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("nombres")+" "+
                                        respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos");
                                context.startActivity(intent);
                                ((Activity)(context)).finish();
                                loading.dismiss();

                                //Toast.makeText(context, respuestaJSON.getString("mensaje"), Toast.LENGTH_LONG).show();

                            }else{
                                if(respuestaJSON.getString("estado").equals("2")){
                                    loading.dismiss();
                                    Toast.makeText(context, respuestaJSON.getString("mensaje"), Toast.LENGTH_LONG).show();
                                }else{
                                    if(respuestaJSON.getString("estado").equals("3")){
                                        loading.dismiss();
                                        Toast.makeText(context, respuestaJSON.getString("mensaje"), Toast.LENGTH_LONG).show();
                                    }
                                }


                                //Intent intent = new Intent(context, iniciarSesion.class);
                                //context.startActivity(intent);
                                //((Activity)(context)).finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(context, "No se pudo establecer conexión con el servidor. Revisa tu conexión a internet e intenta conectarte: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usuario", usuario);
                params.put("clave", clave);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }
    public void google_id_tjsegurity( final RequestQueue req, final String idgoogle,
                                      final String clave,final String nombres, final String apellidos,final String genero,
                                      final String direccion,final String telefono,final String fabricante,
                                      final String version_so,
                                      final String modelo, final String marca, final String imei,
                                      final String codigo_confirmacion,final String social,
                                      final ProgressDialog loading, final String avatar, final Context context){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"id_google.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                //envia al activity principal


                                saveusuario(context, respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                saveclave(context, respuestaJSON.getJSONObject("usuariocompleto").getString("clave_usuario"));
                                Intent intent = new Intent(context, home.class);
                                intent.putExtra("usuario",respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                intent.putExtra("idusuario", respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") );
                                intent.putExtra("foto", respuestaJSON.getJSONObject("usuariocompleto").getString("avatar"));
                                intent.putExtra("nombres", respuestaJSON.getJSONObject("usuariocompleto").getString("nombres"));
                                intent.putExtra("apellidos", respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos"));
                                variablesGlobales.idusuario_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") ;
                                variablesGlobales.avatar_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("avatar");
                                variablesGlobales.nombre_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("nombres")+" "+
                                        respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos");
                                context.startActivity(intent);
                                ((Activity)(context)).finish();
                                loading.dismiss();
                                //Toast.makeText(context, respuestaJSON.getString("mensaje"), Toast.LENGTH_LONG).show();

                            }else{
                                sesionMovilUser smu=new sesionMovilUser();
                                smu.crear_nuevo_usuario_correo(req, idgoogle, "", nombres, apellidos, "O","", "", fabricante, version_so,  modelo, marca, imei, codigo_confirmacion, "google", avatar, context, loading);
                                loading.dismiss();
                               // Toast.makeText(context, respuestaJSON.getString("mensaje"), Toast.LENGTH_LONG).show();
                                //Intent intent = new Intent(context, iniciarSesion.class);
                                //context.startActivity(intent);
                                //((Activity)(context)).finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "No se pudo establecer conexión con el servidor. Revisa tu conexión a internet e intenta conectarte: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idgoogle", idgoogle);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }

    public void facebook_id_tjsegurity( final RequestQueue req, final String idfacebook,final String clave,final String nombres, final String apellidos,final String genero,
                                      final String direccion,final String telefono,final String fabricante, final String version_so,
                                      final String modelo, final String marca, final String imei, final String codigo_confirmacion,final String social, final ProgressDialog loading, final String avatar, final Context context){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"id_facebook.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){


                                saveusuario(context, respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                saveclave(context, respuestaJSON.getJSONObject("usuariocompleto").getString("clave_usuario"));
                                Intent intent = new Intent(context, home.class);
                                intent.putExtra("usuario",respuestaJSON.getJSONObject("usuariocompleto").getString("nombre_usuario"));
                                intent.putExtra("idusuario", respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") );
                                intent.putExtra("foto", respuestaJSON.getJSONObject("usuariocompleto").getString("avatar"));
                                intent.putExtra("nombres", respuestaJSON.getJSONObject("usuariocompleto").getString("nombres"));
                                intent.putExtra("apellidos", respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos"));

                                variablesGlobales.idusuario_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario") ;
                                variablesGlobales.avatar_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("avatar");
                                variablesGlobales.nombre_movil=respuestaJSON.getJSONObject("usuariocompleto").getString("nombres")+" "+
                                respuestaJSON.getJSONObject("usuariocompleto").getString("apellidos");

                                context.startActivity(intent);
                                ((Activity)(context)).finish();
                                loading.dismiss();
                                //Toast.makeText(context, respuestaJSON.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }else{
                                sesionMovilUser smu=new sesionMovilUser();

                                smu.crear_nuevo_usuario_correo(req, idfacebook, "", nombres, apellidos, genero,"", "", fabricante, version_so,  modelo, marca, imei, codigo_confirmacion, social, avatar, context, loading);
                                loading.dismiss();
                                                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading.dismiss();
                Toast.makeText(context, "No se pudo establecer conexión con el servidor. Revisa tu conexión a internet e intenta conectarte: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idfacebook", idfacebook);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }

    public void cerrar_sesion(Context context){
        saveusuario(context, "");
       saveclave(context, "");
        Intent intent=new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity)(context)).finish();
    }
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static boolean validateEmail(String email) {
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void add_reporte_firebase(int idreporte , int idtiporeporte,String latitud, String longitud, String fecha_hora_send,
                                     String fecha_hora_event, String urlimg,
                                 String descripcion, String idusuario_send_event_report, String nombres_apellidos, String avatar, String nombre_categoria){
        Map<String,Object> map_key = new HashMap<String, Object>();
        String temp_key = refReportado.push().getKey();
        refReportado.updateChildren(map_key);
        DatabaseReference datos_in_key_sos = refReportado.child(temp_key);
        Map<String,Object> map_datos = new HashMap<String, Object>();
        map_datos.put("idevent_report", idreporte);
        map_datos.put("idcategoria_reporte", idtiporeporte);
        map_datos.put("nombre_categoria", nombre_categoria);
        map_datos.put("lat",latitud);
        map_datos.put("long",longitud);
        map_datos.put("fecha_hora_send",fecha_hora_send);
        map_datos.put("fecha_hora_event",fecha_hora_event);
        map_datos.put("urlimg",urlimg);
        map_datos.put("descripcion",descripcion);
        map_datos.put("idusuario_send_event_report",idusuario_send_event_report);
        map_datos.put("nombres_apellidos",nombres_apellidos);
        map_datos.put("avatar",avatar);
        map_datos.put("estado","0");

        datos_in_key_sos.updateChildren(map_datos);
    }

    private String PREFS_KEY_USUARIO= "mis_pref_usuario";
    private String PREFS_KEY_CLAVE= "mis_pref_clave";

    public void saveusuario(Context context, String dt) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY_USUARIO, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString("pref_usuario", dt);
        editor.commit();
    }

    public String getusuario(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY_USUARIO, MODE_PRIVATE);
        return  preferences.getString("pref_usuario", "");
    }

    public void saveclave(Context context, String dt) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY_CLAVE, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString("pref_clave", dt);
        editor.commit();
    }

    public String getclave(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY_CLAVE, MODE_PRIVATE);
        return  preferences.getString("pref_clave", "");
    }
}
