package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class perfil extends AppCompatActivity {
    ImageView iv_foto_avatar;
    TextView tv_nombre_perfil;
    RequestQueue requestQueue;

    EditText et_nombres;
    EditText et_apellidos;
    EditText et_direccion;
    EditText et_telefono;

    EditText et_contacto_1;
    EditText et_contacto_2;
    EditText et_contacto_3;

    Button b_actualizar_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        iv_foto_avatar=(ImageView)findViewById(R.id.iv_foto_avatar);
        tv_nombre_perfil=(TextView)findViewById(R.id.tv_nombre_perfil);

        b_actualizar_perfil=(Button)findViewById(R.id.b_actualizar_perfil);

        et_nombres=(EditText)findViewById(R.id.et_nombres);
        et_apellidos=(EditText)findViewById(R.id.et_apellidos);
        et_direccion=(EditText)findViewById(R.id.et_direccion);
        et_telefono=(EditText)findViewById(R.id.et_telefono);

        et_contacto_1=(EditText)findViewById(R.id.et_contacto_1);
        et_contacto_2=(EditText)findViewById(R.id.et_contacto_2);
        et_contacto_3=(EditText)findViewById(R.id.et_contacto_3);

        Bundle bundle = getIntent().getExtras();
        final String idusuario_x=bundle.getString("idusuario");
        String avatar_x=bundle.getString("avatar");
        String nombres_x=bundle.getString("nombres");

        b_actualizar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_nombres.getText().toString().trim().length()>2) {
                    if (et_apellidos.getText().toString().trim().length()>2) {
                            actualizar_perfil(requestQueue, getApplicationContext(), idusuario_x,
                                    et_nombres.getText().toString(), et_apellidos.getText().toString(), et_direccion.getText().toString(),
                                    et_telefono.getText().toString(),
                                    et_contacto_1.getText().toString(), et_contacto_2.getText().toString(), et_contacto_3.getText().toString());

                    }else{
                        Toast.makeText(perfil.this, "Tu apellido debe contener mas de 2 letras.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(perfil.this, "Tu apellido debe contener mas de 2 letras.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Glide.with(this).load(avatar_x).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_foto_avatar);
        tv_nombre_perfil.setText(nombres_x);
        requestQueue = Volley.newRequestQueue(this);
        recupera_perfil(requestQueue, this,idusuario_x);
        recupera_contactos(requestQueue, this,idusuario_x);
    }

    public void recupera_perfil(final RequestQueue req, final Context context,final String idusuario){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"recupera_perfil.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                JSONObject resultadoperfil = respuestaJSON.getJSONObject("perfil");

                                et_nombres.setText(resultadoperfil.getString("nombres"));
                                et_apellidos.setText(resultadoperfil.getString("apellidos"));
                                et_direccion.setText(resultadoperfil.getString("direccion"));
                                et_telefono.setText(resultadoperfil.getString("telefono"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error:", error+"");
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idusuario", idusuario);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }

    public void recupera_contactos(final RequestQueue req, final Context context,final String idusuario){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"recupera_contactos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado

                            if (respuestaJSON.getString("estado").equals("1")){

                                JSONArray resultadocontactos = respuestaJSON.getJSONArray("contactos");
                                for(int i=0; i<resultadocontactos.length(); i++){
                                    JSONObject resultadoItem = (JSONObject)resultadocontactos.get(i);
                                    if(i==0){
                                        et_contacto_1.setText(resultadoItem.getString("numero"));
                                    }
                                    if(i==1){
                                        et_contacto_2.setText(resultadoItem.getString("numero"));
                                    }
                                    if(i==2){
                                        et_contacto_3.setText(resultadoItem.getString("numero"));
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error:", error+"");
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idusuario", idusuario);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }
    public void actualizar_perfil(final RequestQueue req, final Context context,final String idusuario,
                                  final String nombres, final String apellidos, final String direccion, final String telefono,
                                  final String contacto_1, final String contacto_2, final String contacto_3){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"actualiza_perfil.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado

                            if (respuestaJSON.getString("estado").equals("1")){
                                Toast.makeText(perfil.this, "¡Se actualizo la informacion de forma correcta!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(perfil.this, MainActivity.class);
                                startActivity(intent);
                               finishAffinity();
                            }else{
                                Toast.makeText(perfil.this, "Problema en la actualizacion dentro del servidor", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(perfil.this, "Algo salio mal. Por favor intentelo otra vez.", Toast.LENGTH_SHORT).show();
                Log.i("error:", error+"");
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idusuario", idusuario);
                params.put("nombres", nombres);
                params.put("apellidos", apellidos);
                params.put("direccion", direccion);
                params.put("telefono", telefono);
                params.put("contacto_1", contacto_1);
                params.put("contacto_2", contacto_2);
                params.put("contacto_3", contacto_3);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }
}
