package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by Salinas on 11/02/2017.
 */

public class adaptadorMensaje extends BaseAdapter {
    private Context context;
    private ArrayList<TMessageView> arrayList;
    public adaptadorMensaje(ArrayList<TMessageView> arrayList, Context context) {
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
            convertView = layoutInflater.inflate(R.layout.mensajes, parent, false);
        }

        ImageView iv_left=(ImageView)convertView.findViewById(R.id.iv_left);
        TextView tv_nombre_left=(TextView)convertView.findViewById(R.id.tv_nombre_left);
        TextView tv_mensaje_left=(TextView)convertView.findViewById(R.id.tv_mensaje_left);
        LinearLayout ly_left=(LinearLayout) convertView.findViewById(R.id.ly_left);
        RelativeLayout rly_contenedor=(RelativeLayout)convertView.findViewById(R.id.rly_contenedor);
        String xidusuario=arrayList.get(position).getIdusuariosuario();
        String nombre_u=arrayList.get(position).getNombreRemitente();
        String foto_u=arrayList.get(position).getImgAvatar();
        String mensaje_u=arrayList.get(position).getMensajeRemitente();
        String fecha_hora=arrayList.get(position).getFechHora();

        if(variablesGlobales.idusuario_movil.equals(xidusuario)){
            Log.d("lado", "right");
            Log.d("usuario movil", variablesGlobales.idusuario_movil);
            Log.d("servidor", xidusuario);

            ly_left.setBackgroundResource(R.mipmap.mensaje_right1);
        }
        else
        {
            Log.d("lado", "left");

            Log.d("usuario movil", variablesGlobales.idusuario_movil);
            Log.d("servidor", xidusuario);

            ly_left.setBackgroundResource(R.mipmap.mensaje_left1);
        }

        Glide.with(context).load(foto_u).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_left);
        tv_mensaje_left.setText(mensaje_u);
        tv_nombre_left.setText(nombre_u);

        return convertView;
    }
}
