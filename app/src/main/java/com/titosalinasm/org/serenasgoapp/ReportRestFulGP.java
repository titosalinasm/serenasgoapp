package com.titosalinasm.org.serenasgoapp;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

public class ReportRestFulGP {
    private final String HTTP_EVENT="http://serapphuaraz.com/android/"+"reporte.php";
    private HttpClient httpclient;

    /**
     * Envia los datos por POST
     * @throws IOException
     * @throws ClientProtocolException
     * @throws JSONException
     * */
    public String addEventPost(String idusuario,String idcategoria_reporte, String nombre_img,
    String fecha_hora_event, String descripcion, String latitud, String longitud) throws ClientProtocolException, IOException, JSONException
    {
        httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(HTTP_EVENT);
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("idusuario", idusuario));
        nameValuePairs.add(new BasicNameValuePair("idcategoria_reporte", idcategoria_reporte));
        nameValuePairs.add(new BasicNameValuePair("nombre_img", nombre_img));
        nameValuePairs.add(new BasicNameValuePair("fecha_hora_event", fecha_hora_event));
        nameValuePairs.add(new BasicNameValuePair("descripcion", descripcion));
        nameValuePairs.add(new BasicNameValuePair("latitud", latitud));
        nameValuePairs.add(new BasicNameValuePair("longitud", longitud));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        //ejecuta
        HttpResponse response = httpclient.execute(httppost);
        //obtiene la respuesta y transorma a objeto JSON
        String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
        /*
        JSONObject object = new JSONObject(jsonResult);
        Log.i("jsonResult",object.getString("dato"));
        if( object.getString("Result").equals("200"))
        {
            return "Petición POST: Exito";
        }
        */
        return jsonResult;
    }

    /**
     * Transforma el InputStream en un String
     * @return StringBuilder
     * */
    private StringBuilder inputStreamToString(InputStream is)
    {
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader rd = new BufferedReader( new InputStreamReader(is) );
        try
        {
            while( (line = rd.readLine()) != null )
            {
                stringBuilder.append(line);
            }
        }
        catch( IOException e)
        {
            e.printStackTrace();
        }

        return stringBuilder;
    }
}