package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class notificacion_app extends AppCompatActivity {
    ImageView iv_cerrar, iv_img_url;
    TextView tv_titulo, tv_descripcion;
    Button b_enlace;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_app);

        iv_cerrar=(ImageView)findViewById(R.id.iv_cerrar);
        iv_img_url=(ImageView)findViewById(R.id.iv_img_url);
        tv_titulo=(TextView)findViewById(R.id.tv_titulo);
        tv_descripcion=(TextView)findViewById(R.id.tv_descripcion);
        b_enlace=(Button)findViewById(R.id.b_enlace);

        Bundle bundle = getIntent().getExtras();
        String titulo = bundle.getString("titulo");
        String descripcion = bundle.getString("descripcion");
        String img_url = bundle.getString("img");
        final String link = bundle.getString("link");
        final String idnotificacion=bundle.getString("idnotificacion_app");

        tv_titulo.setText(titulo);
        tv_descripcion.setText(descripcion);
        Glide.with(this).load(img_url).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_img_url);

        b_enlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                requestQueue = Volley.newRequestQueue(notificacion_app.this);
                abrir_enlace(requestQueue, notificacion_app.this, idnotificacion);
            }
        });
        iv_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void abrir_enlace(final RequestQueue req, final Context context, final String idnotificacion_app){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"in_vista_notificacion.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            if (respuestaJSON.getString("estado").equals("1")){
                                Log.i("Mensaje: ", "Se guardo como visto");
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error en volley:", error+"");
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idnotificacion_app", idnotificacion_app);
                params.put("idusuario", variablesGlobales.idusuario_movil);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }
}
