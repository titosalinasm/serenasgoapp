package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Salinas on 23/02/2017.
 */

public class adaptadorEmergencias extends BaseAdapter {
    private Context context;
    private ArrayList<TEmergenciaView> arrayList;
    public adaptadorEmergencias(ArrayList<TEmergenciaView> arrayList, Context context){
        this.arrayList=arrayList;
        this.context=context;
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
            convertView = layoutInflater.inflate(R.layout.list_numeros_emerencia, parent, false);
        }

        TextView tv_list_nombre_entidad=(TextView)convertView.findViewById(R.id.tv_list_nombre_entidad);
        TextView tv_list_numero_entidad=(TextView)convertView.findViewById(R.id.tv_list_numero_entidad);
        Button ib_list_call=(Button) convertView.findViewById(R.id.ib_list_call);
        TextView tv_list_direccion=(TextView)convertView.findViewById(R.id.tv_list_direccion);

        tv_list_nombre_entidad.setText(arrayList.get(position).getNombre_entidad());
        tv_list_numero_entidad.setText(arrayList.get(position).getNumero());
        final String numeros=arrayList.get(position).getNumero();
        ib_list_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: "+numeros));
                context.startActivity(intent);
            }
        });
        if (arrayList.get(position).getDireccion().trim().length()>0){
            tv_list_direccion.setText(arrayList.get(position).getDireccion());
        }else{
            tv_list_direccion.setVisibility(View.GONE);
        }


        return convertView;
    }
}
