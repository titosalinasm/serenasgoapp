package com.titosalinasm.org.serenasgoapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Salinas on 28/01/2017.
 */
public class categorias extends DialogFragment {
    public categorias() {
    }
    AlertDialog alertcategoria=null;
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

        gv_categoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        builder.setView(v);

        //tv_segundo = (TextView) v.findViewById(R.id.tv_segundo);
        alertcategoria=builder.create();
        return alertcategoria;
    }
  public void recupera_categoria(final RequestQueue req, final Context context,final ProgressDialog progressDialog){
        // Toast.makeText(this, "probando llegando", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"recupera_categoria.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                ArrayList<TGridView> modelcategoria=new ArrayList<>();

                                JSONArray resultadopublicaciones = respuestaJSON.getJSONArray("categorias");
                                for(int i=0; i<resultadopublicaciones.length(); i++){
                                    JSONObject resultadoItem = (JSONObject)resultadopublicaciones.get(i);
                                    TGridView m=new TGridView();
                                    Log.d("urlimgtito",variablesGlobales.url_img_categorias+resultadoItem.getString("img") );
                                    m.setImgCategoria(variablesGlobales.url_img_categorias+resultadoItem.getString("img"));
                                    m.setNombreCategoria(resultadoItem.getString("nombre"));
                                    m.setCodigo(resultadoItem.getInt("idcategoria_reporte"));
                                    modelcategoria.add(m);
                                }

                                gridAdapter=new GridAdapter(modelcategoria, getActivity());
                                gv_categoria.setAdapter(gridAdapter);
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // loading.dismiss();
                Toast.makeText(context, "No se pudo establecer conexión con el servidor. Revisa tu conexión a internet e intenta conectarte: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("limite", variablesGlobales.limite_inferior_publicacion+"");
                Log.d("titove", variablesGlobales.limite_inferior_publicacion+"");
                variablesGlobales.limite_inferior_publicacion++;
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }


}
