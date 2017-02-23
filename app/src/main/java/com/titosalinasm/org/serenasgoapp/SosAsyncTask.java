package com.titosalinasm.org.serenasgoapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Salinas on 22/02/2017.
 */

public class SosAsyncTask  extends AsyncTask<String, Void, String>{
    private Context context;
    AlertDialog alertDialog;
    Activity activity;

    public SosAsyncTask(Context context, final AlertDialog alertDialog, Activity activity) {

        this.context = context;
        this.alertDialog=alertDialog;
        this.activity=activity;
    }
    @Override
    protected String doInBackground(String... params) {

        SosRestFulGP sosRestFulGP = new SosRestFulGP();
        try {
            if (params[0].equals("POST")) {
                return sosRestFulGP.addEventPost(params[1],params[2],params[3]);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        alertDialog.dismiss();
        crea_mensaje_sos().show();
        try {
            JSONObject respuestaJSON = new JSONObject(s.toString());
            timesos tsos = new timesos();
            tsos.add_sos_firebase(respuestaJSON.getJSONObject("misos").getInt("idsos"),
                    respuestaJSON.getJSONObject("misos").getString("lat"),
                    respuestaJSON.getJSONObject("misos").getString("long"),
                    respuestaJSON.getJSONObject("misos").getString("fecha_hora_sos"),
                    respuestaJSON.getJSONObject("misos").getInt("idusuario_send_sos"),
                    respuestaJSON.getJSONObject("misos").getInt("idusuario_attend_sos"),
                    respuestaJSON.getJSONObject("misos").getString("estado"),
                    respuestaJSON.getJSONObject("misos").getString("condicion"),
                    respuestaJSON.getJSONObject("misos").getString("detalle_sos"),
                    respuestaJSON.getJSONObject("misos").getString("nombres") + " " +
                            respuestaJSON.getJSONObject("misos").getString("apellidos"),
                    respuestaJSON.getJSONObject("misos").getString("avatar")
            );
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AlertDialog crea_mensaje_sos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.mensaje_sos, null);
        builder.setView(v);
        return builder.create();
    }

}