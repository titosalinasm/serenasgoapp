package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by Salinas on 30/01/2017.
 */
public class GridAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<TGridView> arrayList;

        public GridAdapter(ArrayList<TGridView> arrayList, Context c){
            this.arrayList = arrayList;
            mContext=c;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.categoriasdato, parent, false);
            }

            ImageView img_grid=(ImageView)convertView.findViewById(R.id.img_grid);
            TextView tv_grid=(TextView)convertView.findViewById(R.id.tv_grid);
            LinearLayout datos_categoria_rep=(LinearLayout)convertView.findViewById(R.id.ly_datos_categoria_rep);

            datos_categoria_rep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(mContext, send_report.class);
                    i.putExtra("idcategoria_rep", arrayList.get(position).getCodigo());
                    i.putExtra("url_img_report", arrayList.get(position).getImgCategoria());
                    i.putExtra("nombre_categoria", arrayList.get(position).getNombreCategoria());
                    mContext.startActivity(i);
                }
            });
            tv_grid.setText(arrayList.get(position).getNombreCategoria());
            Glide.with(mContext).load(arrayList.get(position).getImgCategoria()).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_grid);

            return convertView;
        }

}
