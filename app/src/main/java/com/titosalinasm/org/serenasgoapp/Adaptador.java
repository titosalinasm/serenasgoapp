package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        TextView hora_fecha=(TextView)convertView.findViewById(R.id.hora_fecha);
        TextView nombre=(TextView)convertView.findViewById(R.id.i_tv_nombre);
        TextView descripcion=(TextView)convertView.findViewById(R.id.i_tv_descripcion);


        final String des=arrayList.get(position).getDescripcion();
        LinearLayout liner_layaut=(LinearLayout)convertView.findViewById(R.id.liner_layaut);
        //asignando valores dinamicos
        liner_layaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, des, Toast.LENGTH_LONG).show();
            }
        });

        Glide.with(context).load(arrayList.get(position).getFotoperfil()).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar_logo);
        nombre_entidad.setText(arrayList.get(position).getNombre_entidad());
        hora_fecha.setText(arrayList.get(position).getHora_fech());

        nombre.setText(arrayList.get(position).getNombre());
        descripcion.setText(arrayList.get(position).getDescripcion());
        Glide.with(context).load(arrayList.get(position).getImagen()).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_imagen);

        return convertView;
    }
}
