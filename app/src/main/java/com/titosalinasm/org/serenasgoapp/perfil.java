package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        iv_foto_avatar=(ImageView)findViewById(R.id.iv_foto_avatar);
        tv_nombre_perfil=(TextView)findViewById(R.id.tv_nombre_perfil);

        et_nombres=(EditText)findViewById(R.id.et_nombres);
        et_apellidos=(EditText)findViewById(R.id.et_apellidos);
        et_direccion=(EditText)findViewById(R.id.et_direccion);
        et_telefono=(EditText)findViewById(R.id.et_telefono);

        Bundle bundle = getIntent().getExtras();
        String idusuario_x=bundle.getString("idusuario");
        String avatar_x=bundle.getString("avatar");
        String nombres_x=bundle.getString("nombres");

        Glide.with(this).load(avatar_x).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_foto_avatar);
        tv_nombre_perfil.setText(nombres_x);
        requestQueue = Volley.newRequestQueue(this);
        recupera_perfil(requestQueue, this,idusuario_x);
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
}
