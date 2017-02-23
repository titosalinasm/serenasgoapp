package com.titosalinasm.org.serenasgoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class plus_noticia extends AppCompatActivity {

    ImageView iv_plus_image;
    TextView tv_plus_titulo;
    TextView tv_plus_descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_noticia);
        iv_plus_image=(ImageView)findViewById(R.id.iv_plus_image);
        tv_plus_titulo=(TextView) findViewById(R.id.tv_plus_titulo);
        tv_plus_descripcion=(TextView) findViewById(R.id.tv_plus_descripcion);

        Bundle bundle = getIntent().getExtras();
        String titulo=bundle.getString("titulo");
        String descripcion=bundle.getString("descripcion");;
        String foto=bundle.getString("img");;


        Glide.with(this).load(foto).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_plus_image);
        tv_plus_titulo.setText(titulo);
        tv_plus_descripcion.setText(descripcion);
    }
}
