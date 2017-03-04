package com.titosalinasm.org.serenasgoapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by Salinas on 06/01/2017.
 */
public class Adaptador extends BaseAdapter{
    private Context context;
    private ArrayList<TCardview> arrayList;

    public Adaptador(ArrayList<TCardview> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.card_view, parent, false);
        }

        //inicializando las variables dinamicas

        ImageView iv_imagen=(ImageView)convertView.findViewById(R.id.iv_imagen);
        ImageView avatar_logo=(ImageView)convertView.findViewById(R.id.avatar_logo);
        TextView nombre_entidad=(TextView)convertView.findViewById(R.id.nombre_entidad);
        final TextView hora_fecha=(TextView)convertView.findViewById(R.id.hora_fecha);
        TextView nombre=(TextView)convertView.findViewById(R.id.i_tv_nombre);
        TextView descripcion=(TextView)convertView.findViewById(R.id.i_tv_descripcion);

        final String entit=arrayList.get(position).getNombre_entidad();
        final String av=arrayList.get(position).getFotoperfil();
        final String fh=arrayList.get(position).getHora_fech();

        final String des=arrayList.get(position).getDescripcion();
        final String tit=arrayList.get(position).getNombre();
        final String fot=arrayList.get(position).getImagen();

        Button iv_plus_not=(Button) convertView.findViewById(R.id.iv_plus_not);

        //asignando valores dinamicos
        iv_plus_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, plus_noticia.class);

                intent.putExtra("avatar", av);
                intent.putExtra("entidad", entit);
                intent.putExtra("fecha_h", fh);

                intent.putExtra("titulo", tit);
                intent.putExtra("descripcion", des);
                intent.putExtra("img", fot);

                context.startActivity(intent);
            }
        });

        Glide.with(context).load(arrayList.get(position).getFotoperfil()).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar_logo);
        nombre_entidad.setText(arrayList.get(position).getNombre_entidad());
        hora_fecha.setText(arrayList.get(position).getHora_fech());

        if(arrayList.get(position).getNombre().length()>60) {
            nombre.setText(arrayList.get(position).getNombre().substring(0, 60)+"...");
        }else{
            descripcion.setText(arrayList.get(position).getNombre());
        }
        nombre.setText(arrayList.get(position).getNombre());

        if(arrayList.get(position).getDescripcion().length()>200) {
            descripcion.setText(arrayList.get(position).getDescripcion().substring(0, 200)+"...");
        }else{
            descripcion.setText(arrayList.get(position).getDescripcion());
        }


        Glide.with(context).load(arrayList.get(position).getImagen()).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_imagen);

        return convertView;
    }
}
