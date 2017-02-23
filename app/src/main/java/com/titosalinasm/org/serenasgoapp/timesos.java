package com.titosalinasm.org.serenasgoapp;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference sosRef = ref.child("sos");

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
                //envia_SOS(requestQueue);
                Log.d("error jessit", variablesGlobales.idusuario);
                new SosAsyncTask(getContext()).execute("POST",""+variablesGlobales.idusuario,
                        ""+variablesGlobales.latitud, ""+variablesGlobales.longitud);
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
                            if (respuestaJSON.getString("estado").equals("1")){
                                add_sos_firebase(respuestaJSON.getJSONObject("misos").getInt("idsos"),
                                        respuestaJSON.getJSONObject("misos").getString("lat"),
                                        respuestaJSON.getJSONObject("misos").getString("long"),
                                        respuestaJSON.getJSONObject("misos").getString("fecha_hora_sos"),
                                        respuestaJSON.getJSONObject("misos").getInt("idusuario_send_sos"),
                                        respuestaJSON.getJSONObject("misos").getInt("idusuario_attend_sos"),
                                        respuestaJSON.getJSONObject("misos").getString("estado") ,
                                        respuestaJSON.getJSONObject("misos").getString("condicion"),
                                        respuestaJSON.getJSONObject("misos").getString("detalle_sos"),
                                        respuestaJSON.getJSONObject("misos").getString("nombres")+" "+
                                        respuestaJSON.getJSONObject("misos").getString("apellidos"),
                                        respuestaJSON.getJSONObject("misos").getString("avatar")
                                        );

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

    public void add_sos_firebase(int idsos, String latitud, String longitud, String fecha_hora_sos, int idusuario_send_sos, int idusuario_attend_sos,
                                 String estado, String condicion, String detalle_sos, String nombre_apellidos, String avatar_user){
        Map<String,Object> map_key = new HashMap<String, Object>();
       String temp_key = sosRef.push().getKey();
        sosRef.updateChildren(map_key);

        DatabaseReference datos_in_key_sos = sosRef.child(temp_key);
        Map<String,Object> map_datos = new HashMap<String, Object>();
        map_datos.put("idsos", idsos);
        map_datos.put("lat",latitud);
        map_datos.put("long",longitud);
        map_datos.put("fecha_hora_sos",fecha_hora_sos);
        map_datos.put("idusuario_send_sos",idusuario_send_sos);
        map_datos.put("idusuario_attend_sos",idusuario_attend_sos);
        map_datos.put("estado",estado);
        map_datos.put("condicion",condicion);
        map_datos.put("detalle_sos",detalle_sos);
        map_datos.put("nombres_apellidos",nombre_apellidos);
        map_datos.put("avatar",avatar_user);
        datos_in_key_sos.updateChildren(map_datos);
    }

}
