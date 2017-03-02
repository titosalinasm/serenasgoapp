package com.titosalinasm.org.serenasgoapp;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor) Executors
            .newScheduledThreadPool(1);

    private ScheduledFuture<?> periodicFuture;

    /*volley variable*/
    RequestQueue requestQueue;
    StringRequest stringRequest;
    /*fin volley variable*/

    /*Loading*/
   ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //fin pantalla completa
        setContentView(R.layout.activity_preload);

        //firebase messaje event


        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        requestQueue = Volley.newRequestQueue(this);
        sesionMovilUser sesionum= new sesionMovilUser();
        sesionum.estado_sesion_movil(MainActivity.this, requestQueue, infoMovil.getImei(getApplicationContext()));
        printKeyHash();
        /*
        if (sesionum.estado_sesion_movil(MainActivity.this, requestQueue, infoMovil.getImei(getApplicationContext()))) {
            Toast.makeText(this, "Abre activity principal", Toast.LENGTH_LONG).show();
        }{
            new Handler().postDelayed(new Runnable(){
                public void run(){
                    Intent intent = new Intent(MainActivity.this, iniciarSesion.class);
                    startActivity(intent);
                    finish();
                };
            }, 3000);
        }

       */
    }

    private void printKeyHash() {
        // Obtener el KEY hash para facebook
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.salinas.loginprobandofacebook02", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Clave tito hash key:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }

}
