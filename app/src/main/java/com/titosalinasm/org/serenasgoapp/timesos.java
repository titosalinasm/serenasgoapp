package com.titosalinasm.org.serenasgoapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Salinas on 28/01/2017.
 */
public class timesos extends DialogFragment {
   static CountDownTimer countDownTimer;
    public static int tiempo_temp=4;
    TextView tv_segundo;
    ProgressBar progressBar;
    RequestQueue requestQueue;
    public static AlertDialog alertsos=null;

    public timesos() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        comenzar();
        requestQueue = Volley.newRequestQueue(getContext());
        return creaTimesos();
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog creaTimesos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.timesos, null);
        builder.setView(v);
        progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        tv_segundo = (TextView) v.findViewById(R.id.tv_segundo);
        alertsos=builder.create();

        return alertsos;
    }

    public void comenzar(){
        countDownTimer= new CountDownTimer(4*1000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {

                tiempo_temp=(Integer.parseInt(millisUntilFinished+"")/1000)-1;
                tv_segundo.setText(""+(tiempo_temp+1));
                Log.d("titove", ""+(tiempo_temp+1));
            }
            @Override
            public void onFinish() {
                progressBar.setVisibility(View.INVISIBLE);
                tv_segundo.setTextSize(14);
                tv_segundo.setText("Enviando SOS...");
                envia_SOS(requestQueue);
                //Log.d("Falta data", "Finalizo los 5 segundos");
            }
        };
        countDownTimer.start();
    }

    public void cancelar(){
        if(countDownTimer!=null){
            if (alertsos!=null) {
                alertsos.dismiss();

            }
            countDownTimer.cancel();
            countDownTimer=null;
        }
        tiempo_temp=4;
    }

    public void envia_SOS(final RequestQueue req){
        // Toast.makeText(this, "probando llegando", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"in_sos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                            tv_segundo.setText("Sos enviado");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo establecer conexión con el servidor. Revisa tu conexión a internet e intenta conectarte: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idusuariox", variablesGlobales.idusuario);
                params.put("latitudx", variablesGlobales.latitud);
                params.put("longitudx", variablesGlobales.longitud);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }
}
