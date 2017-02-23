package com.titosalinasm.org.serenasgoapp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Salinas on 22/02/2017.
 */

public class SosRestFulGP {
    private final String HTTP_EVENT="http://serapphuaraz.com/android/"+"in_sos.php";
    private HttpClient httpclient;

    /**
     * Envia los datos por POST
     * @throws IOException
     * @throws ClientProtocolException
     * @throws JSONException
     * */

    public String addEventPost(String idusuario, String latitud, String longitud) throws
            ClientProtocolException, IOException, JSONException
    {
        httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(HTTP_EVENT);
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("idusuariox", idusuario));
        nameValuePairs.add(new BasicNameValuePair("latitudx", latitud));
        nameValuePairs.add(new BasicNameValuePair("longitudx", longitud));

        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        //ejecuta
        HttpResponse response = httpclient.execute(httppost);
        //obtiene la respuesta y transorma a objeto JSON
        String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

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
