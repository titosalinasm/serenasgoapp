package com.titosalinasm.org.serenasgoapp;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by Salinas on 12/08/2016.
 */
public class infoMovil {
    public static String getImei(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    public static String getMarca() {
        return Build.BRAND;
    }
    public static String getFabricante() {
        return Build.MANUFACTURER;
    }
    public static String getModelo() {
        return Build.MODEL;
    }
    public static String getVersionSo() {
        return Build.VERSION.RELEASE;
    }
    public static String getHardware() {
        return Build.HARDWARE;
    }

    public static String codigoConfirmacion(int lenght){

        String numero="";

        for (int i=0; i<lenght; i++ ){
            int numero_creado = (int)(Math.random() *9)+1;
            numero+=""+numero_creado;
        }
        return numero;
    }

}
