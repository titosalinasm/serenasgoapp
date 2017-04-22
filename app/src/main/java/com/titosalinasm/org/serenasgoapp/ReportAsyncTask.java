package com.titosalinasm.org.serenasgoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ReportAsyncTask extends AsyncTask<String,Void,String> {

    private ProgressDialog progressDialog;
    private Context context;

    /**Constructor de clase */
    public ReportAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

            ReportRestFulGP reportRestFulGP = new ReportRestFulGP();
        try {
            if (params[0].equals("POST")) {
                return reportRestFulGP.addEventPost(params[1],params[2],params[3],params[4],params[5],params[6],params[7]);
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

    /**
     * Antes de comenzar la tarea muestra el progressDialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(
                context, "Por favor espere", "Procesando...");

    }

    /**
     * Cuando se termina de ejecutar, cierra el progressDialog y retorna el resultado a la interfaz
     * **/
    @Override
    protected void onPostExecute(String resul) {
        Log.i("RESULTADOS JSON", resul);
        try {
            JSONObject respuestaJSON = new JSONObject(resul.toString());
            sesionMovilUser sMu = new sesionMovilUser();
            sMu.add_reporte_firebase(respuestaJSON.getJSONObject("ultimo_reporte").getInt("idevent_report"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getInt("idcategoria_reporte"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("lat"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("long"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("fecha_hora_send"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("fecha_hora_event"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("url_img_event_report"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("descripcion"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("idusuario_send_event_report"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("nombres")+
                            " "+respuestaJSON.getJSONObject("ultimo_reporte").getString("apellidos"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("avatar"),
                    respuestaJSON.getJSONObject("ultimo_reporte").getString("nombre"));
        }catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        Toast.makeText(context, "Su reporte fue enciado con exito! :)", Toast.LENGTH_SHORT).show();
    }
}