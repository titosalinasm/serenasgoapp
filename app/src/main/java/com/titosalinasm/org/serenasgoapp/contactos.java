package com.titosalinasm.org.serenasgoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class contactos extends AppCompatActivity {
    ImageView iv_foto_avatar;
    TextView tv_nombre_perfil;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        iv_foto_avatar=(ImageView)findViewById(R.id.iv_foto_avatar);
        tv_nombre_perfil=(TextView)findViewById(R.id.tv_nombre_perfil);

        Bundle bundle = getIntent().getExtras();
        String idusuario_x=bundle.getString("idusuario");
        String avatar_x=bundle.getString("avatar");
        String nombres_x=bundle.getString("nombres");

        Glide.with(this).load(avatar_x).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_foto_avatar);
        tv_nombre_perfil.setText(nombres_x);
        //requestQueue = Volley.newRequestQueue(this);
    }
}
