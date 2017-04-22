package com.titosalinasm.org.serenasgoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class plus_noticia extends AppCompatActivity {
    ImageView ivatraz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_noticia);

        ivatraz=(ImageView)findViewById(R.id.ivatraz);
        ivatraz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView avatar_logo=(ImageView)findViewById(R.id.avatar_logo);
        TextView nombre_entidad=(TextView)findViewById(R.id.nombre_entidad);
        TextView hora_fecha=(TextView)findViewById(R.id.hora_fecha);

        TextView i_tv_nombre=(TextView)findViewById(R.id.i_tv_nombre);
        TextView i_tv_descripcion=(TextView)findViewById(R.id.i_tv_descripcion);
        ImageView iv_imagen=(ImageView)findViewById(R.id.iv_imagen);
        Bundle bundle = getIntent().getExtras();
        String avata=bundle.getString("avatar");
        String nombr_ent=bundle.getString("entidad");
        String fech=bundle.getString("fecha_h");

        String titulo=bundle.getString("titulo");
        String descripcion=bundle.getString("descripcion");
        String foto=bundle.getString("img");

        Glide.with(this).load(avata).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar_logo);
        nombre_entidad.setText(nombr_ent);
        hora_fecha.setText(fech);
        i_tv_nombre.setText(titulo);
        i_tv_descripcion.setText(descripcion);
        Glide.with(this).load(foto).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_imagen);
    }
}
