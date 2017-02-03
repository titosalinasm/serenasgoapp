package com.titosalinasm.org.serenasgoapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Salinas on 28/01/2017.
 */
public class categorias extends DialogFragment {
    public categorias() {
    }

    GridView gv_categoria;
    GridAdapter gridAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearCategorias();
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog crearCategorias() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.categorias, null);

        gv_categoria=(GridView) v.findViewById(R.id.gv_categoria);
        ArrayList<TGridView> modelcategoria=new ArrayList<>();
        TGridView m=new TGridView();

        m.setImgCategoria("http://d3j5y3w2fmfk8c.cloudfront.net/wp-content/uploads/drugs_law_in_prague.jpg?x31740");
        m.setNombreCategoria("Mariguana");
        m.setCodigo(1);
        modelcategoria.add(m);

        m=new TGridView();
        m.setImgCategoria("https://thumbs.dreamstime.com/z/jeringuilla-de-la-droga-y-herona-cocinada-38227473.jpg");
        m.setNombreCategoria("Tito hola");
        m.setCodigo(2);
        modelcategoria.add(m);

        m=new TGridView();
        m.setImgCategoria("https://thumbs.dreamstime.com/z/jeringuilla-de-la-droga-y-herona-cocinada-38227473.jpg");
        m.setNombreCategoria("Tito hola");
        m.setCodigo(3);
        modelcategoria.add(m);

        m=new TGridView();
        m.setImgCategoria("https://thumbs.dreamstime.com/z/jeringuilla-de-la-droga-y-herona-cocinada-38227473.jpg");
        m.setNombreCategoria("Tito hola");
        m.setCodigo(4);
        modelcategoria.add(m);

        m=new TGridView();
        m.setImgCategoria("https://thumbs.dreamstime.com/z/jeringuilla-de-la-droga-y-herona-cocinada-38227473.jpg");
        m.setNombreCategoria("Tito hola");
        m.setCodigo(5);
        modelcategoria.add(m);

        gridAdapter=new GridAdapter(modelcategoria, getActivity());
        gv_categoria.setAdapter(gridAdapter);

       // gv_categoria.setAdapter();
        gv_categoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(v);

        //tv_segundo = (TextView) v.findViewById(R.id.tv_segundo);

        return builder.create();
    }



}
