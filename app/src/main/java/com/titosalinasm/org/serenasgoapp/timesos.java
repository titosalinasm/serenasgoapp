package com.titosalinasm.org.serenasgoapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
                //envia_SOS(requestQueue);
                //Log.d("error jessit", variablesGlobales.idusuario);
                if(variablesGlobales.latitud!=null && variablesGlobales.longitud!=null) {
                    tv_segundo.setTextSize(14);
                    tv_segundo.setText("ENVIANDO SOS...");
                    new SosAsyncTask(getContext(), alertsos, getActivity()).execute("POST", "" + variablesGlobales.idusuario,
                            "" + variablesGlobales.latitud, "" + variablesGlobales.longitud);
                }else{
                    alertsos.dismiss();
                    Toast.makeText(getContext(), "Lo sentimos no capturamos tu ubicación, intentalo otra vez", Toast.LENGTH_SHORT).show();
                }
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
        map_datos.put("estado","0");
        map_datos.put("condicion",condicion);
        map_datos.put("detalle_sos",detalle_sos);
        map_datos.put("nombres_apellidos",nombre_apellidos);
        map_datos.put("avatar",avatar_user);
        datos_in_key_sos.updateChildren(map_datos);
        send_sos_contacto(latitud, longitud);
    }

    public void send_sos_contacto(String la, String lo){

        String phone1 = variablesGlobales.contacto1;
        String phone2 = variablesGlobales.contacto2;
        String phone3 = variablesGlobales.contacto3;
        String text = "Emergencia: http://maps.google.com/?q=loc:"+la.substring(0, 8)+","+lo.substring(0,8)+"";
        SmsManager sms = SmsManager.getDefault();
        if(phone1.length()>9) {
            sms.sendTextMessage(phone1, null, text, null, null);
        }
        if(phone1.length()>9) {
            sms.sendTextMessage(phone2, null, text, null, null);
        }
        if(phone1.length()>9) {
            sms.sendTextMessage(phone3, null, text, null, null);
        }

    }

}
