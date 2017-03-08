package com.titosalinasm.org.serenasgoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class registrarUsuario extends AppCompatActivity implements View.OnClickListener {
    ImageView ivatraz;
    Button bregistrarme;

    EditText etnombres;
    EditText etapellidos;
    RadioButton rhombre;
    RadioButton rmujer;
    RadioButton rotro;
    EditText ettelefono;
    EditText etdireccion;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        ivatraz = (ImageView) findViewById(R.id.ivatraz);
        bregistrarme = (Button) findViewById(R.id.bregistrarme);

        etnombres = (EditText) findViewById(R.id.etnombres);
        etapellidos = (EditText) findViewById(R.id.etapellidos);
        rhombre = (RadioButton) findViewById(R.id.rhombre);
        rmujer = (RadioButton) findViewById(R.id.rmujer);
        rotro = (RadioButton) findViewById(R.id.rotro);
        ettelefono = (EditText) findViewById(R.id.ettelefono);
        etdireccion = (EditText) findViewById(R.id.etdireccion);


        bregistrarme.setOnClickListener(this);
        ivatraz.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bregistrarme) {
            Bundle bundle = getIntent().getExtras();

            //datos del usuario
            String correo = bundle.getString("correo");
            String clave = bundle.getString("clave");

            //datos de persona
            String nombres = etnombres.getText().toString();
            String apellidos = etapellidos.getText().toString();
            String genero = "";

            if (rhombre.isChecked()) {
                genero = "H";
            }

            if (rmujer.isChecked()) {
                genero = "M";
            }

            if (rotro.isChecked()) {
                genero = "O";
            }

            String telefono = ettelefono.getText().toString();
            String direccion = etdireccion.getText().toString();

            //datos del telefono
            String fabricante = infoMovil.getFabricante();
            String version_so = infoMovil.getVersionSo();
            String modelo = infoMovil.getModelo();
            String marca = infoMovil.getMarca();
            String imei = infoMovil.getImei(getApplicationContext());
            String codigo_confirmacion = infoMovil.codigoConfirmacion(10);

            //enviando datos al servidor
            if(nombres.trim().length()>2) {
                if(apellidos.trim().length()>2) {
                    if(genero.trim().length()>0) {
                        if(telefono.trim().length()>8 && telefono.trim().length()<13) {
                            sesionMovilUser smu = new sesionMovilUser();
                            requestQueue = Volley.newRequestQueue(this);
                            final ProgressDialog loading = ProgressDialog.show(registrarUsuario.this, "Registrando...", "Espere por favor...", false, false);
                            // final ProgressDialog loading = ProgressDialog.show(registrarUsuario.this,"Uploading... ","Please wait...",false,false);
                            smu.crear_nuevo_usuario_correo(requestQueue, correo, clave, nombres, apellidos, genero,
                                    direccion, telefono, fabricante, version_so,
                                    modelo, marca, imei, codigo_confirmacion, "correo", "null", registrarUsuario.this, loading);
                        }else{
                            Toast.makeText(this, "Número de celular no válido.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "Seleccione un genero.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "El apellido ingresado no es correcto.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "El nombre ingresado no es correcto.", Toast.LENGTH_SHORT).show();
            }



        }

        if (v == ivatraz) {
            finish();
        }
    }
}
