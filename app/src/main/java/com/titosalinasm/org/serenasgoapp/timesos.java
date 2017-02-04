package com.titosalinasm.org.serenasgoapp;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Salinas on 28/01/2017.
 */
public class timesos extends DialogFragment {
   static CountDownTimer countDownTimer;
    TextView tv_segundo;
    public static AlertDialog alertsos=null;
    public timesos() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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

        tv_segundo = (TextView) v.findViewById(R.id.tv_segundo);
        alertsos=builder.create();

        return alertsos;

    }

    public void comenzar(){
        countDownTimer= new CountDownTimer(4*1000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                // tv_segundo.setText(""+(millisUntilFinished/1000));
                tv_segundo.setText(""+(millisUntilFinished/1000));
                Log.d("Falta", ""+(millisUntilFinished/1000) );
            }
            @Override
            public void onFinish() {
                tv_segundo.setText("Finalizo");
                Log.d("Falta data", "Finalizo los 5 segundos");
            }
        };
        countDownTimer.start();
    }
    public void cancelar(){
        if(countDownTimer!=null){
            alertsos.dismiss();
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }



}
