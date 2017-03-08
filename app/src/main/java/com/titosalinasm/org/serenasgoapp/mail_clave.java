package com.titosalinasm.org.serenasgoapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class mail_clave extends AppCompatActivity implements View.OnClickListener {
    ImageView ivatraz;
    EditText etcorreo, pwclave, pwrclave;
    Button bsiguiente;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_clave);

        /*Localizando variables*/
        ivatraz=(ImageView)findViewById(R.id.ivatraz);
        etcorreo=(EditText) findViewById(R.id.etcorreo);
        pwclave=(EditText) findViewById(R.id.pwclave);
        pwrclave=(EditText) findViewById(R.id.pwrclave);
        bsiguiente=(Button) findViewById(R.id.bsiguiente);


        bsiguiente.setOnClickListener(this);
        ivatraz.setOnClickListener(this);
        /*fin de Localizacion de variables*/
    }

    @Override
    public void onClick(View v) {
        if (bsiguiente==v){
        sesionMovilUser csesionusuario=new sesionMovilUser();

            requestQueue = Volley.newRequestQueue(this);

            if (csesionusuario.validateEmail(etcorreo.getText().toString())) {
                if (true) {
                if (pwclave.getText().length()>5 && pwrclave.getText().length()>5) {
                    if (pwclave.getText().toString().equals(pwrclave.getText().toString())) {
                        final ProgressDialog loading = ProgressDialog.show(mail_clave.this,"Verificando...","Espere por favor...",false,false);
                        csesionusuario.existencia_usuario(mail_clave.this, requestQueue, loading,  etcorreo.getText().toString(), pwclave.getText().toString());

                    } else {
                        Toast.makeText(this, "Las contraseñas ingresadas no coinciden.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "La clave debe contener al menos 6 digitos.", Toast.LENGTH_SHORT).show();
                }
                //
                } else {
                    Toast.makeText(this, "El usuario que ingresaste ya existe", Toast.LENGTH_SHORT).show();
                }
                           }else{
                Toast.makeText(this, "El correo ingresado no es válido", Toast.LENGTH_SHORT).show();
            }
        }

        if (ivatraz==v){
            finish();
        }
    }
}
