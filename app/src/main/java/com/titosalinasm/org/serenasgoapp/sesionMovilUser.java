package com.titosalinasm.org.serenasgoapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Salinas on 12/08/2016.
 */

public class sesionMovilUser {
    private static final String TAG = sesionMovilUser.class.getSimpleName();
    private ListView lista;
    private Adaptador adapter;
    public void estado_sesion_movil(final Context context, final RequestQueue req, final String ime){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"estado_sesion_movil.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el movil esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                               // Log.d("nombretito",respuestaJSON.getJSONObject("usuariocompleto").getString("nombres") );

                                actualizar_estado_sesion(req,"1",respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario"));

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
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Lo sentimos, algo salió mal: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("imei", ime);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
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
    public void validar_usuario(final Context context, final RequestQueue req, final String usuario, final String clave){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"compruebausuario.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el movil esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                Toast.makeText(context, respuestaJSON.getString("mensaje"), Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(context, respuestaJSON.getString("mensaje"), Toast.LENGTH_LONG).show();

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

                                actualizar_estado_sesion(req,"1",respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario"));

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

                                actualizar_estado_sesion(req,"1",respuestaJSON.getJSONObject("usuariocompleto").getString("idusuario"));

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

    public void actualizar_estado_sesion(final RequestQueue req, final String valor, final String idusuario){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"update_estado_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                Log.d("Acceso", "Se actualizo estado");
                            }else{
                                Log.d("Acceso", "Algo salio mal al actualizar");
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                         }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("valor", valor);
                params.put("idusuario", idusuario);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static boolean validateEmail(String email) {
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
