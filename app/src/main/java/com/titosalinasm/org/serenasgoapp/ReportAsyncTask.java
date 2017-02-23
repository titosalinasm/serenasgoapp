package com.titosalinasm.org.serenasgoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

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
        progressDialog.dismiss();
        Toast.makeText(context, "Su reporte fue enciado con exito! :)", Toast.LENGTH_SHORT).show();
    }
}