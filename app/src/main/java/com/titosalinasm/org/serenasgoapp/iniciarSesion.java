package com.titosalinasm.org.serenasgoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class iniciarSesion extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    //Signin button
    private SignInButton signInButton;
    //Signing Options
    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;

    /*volley variable*/
    RequestQueue requestQueue;
    /*fin volley variable*/

    /*variables de inicio de sesion xml*/
    EditText etusuario, etclave;
    Button bingresar;
    TextView tvregistrate;

    //VARIABLES FACEBOOK
    private Button bfacebook;
    private CallbackManager callbackManager;
    private ProfilePictureView profilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FACEBOOK
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                          /*informacion de usuario de facebook*/

                        Profile profile = Profile.getCurrentProfile();
                        sesionMovilUser smu=new sesionMovilUser();

                        String  nombre, apellido;
                        nombre =profile.getFirstName();
                        apellido = profile.getLastName();
                        String foto="https://graph.facebook.com/"+profile.getId()+"/picture?type=large";

                        requestQueue = Volley.newRequestQueue(iniciarSesion.this);

                        // Toast.makeText(this, "Nombre: "+acct.getDisplayName(), Toast.LENGTH_LONG).show();
                        //datos del telefono
                        String fabricante=infoMovil.getFabricante();
                        String version_so=infoMovil.getVersionSo();
                        String modelo=infoMovil.getModelo();
                        String marca=infoMovil.getMarca();
                        String imei=infoMovil.getImei(getApplicationContext());
                        String codigo_confirmacion=infoMovil.codigoConfirmacion(10);

                        final ProgressDialog loading = ProgressDialog.show(iniciarSesion.this,"Verificando...","Espere por favor...",false,false);
                        Log.d("ti ero", nombre+"|"+apellido);
                        smu.facebook_id_tjsegurity(requestQueue, profile.getId(), "", nombre, apellido, "O","", "", fabricante, version_so,  modelo, marca, imei, codigo_confirmacion, "facebook", loading, foto, iniciarSesion.this);
                       // Glide.with(iniciarSesion.this).load("https://graph.facebook.com/" +profile.getId()+ "/picture?type=small").diskCacheStrategy(DiskCacheStrategy.ALL).into(ivperfilfacebook);

                        // info.setText(profile.getFirstName());
                        // profilePicture = (ProfilePictureView) findViewById(R.id.profilePicture);
                        // profilePicture.setProfileId(profile.getId());
                 /*fin informacion de usuario de facebook*/

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(iniciarSesion.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("Success", "Login");
                        Toast.makeText(iniciarSesion.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        //FIN FACEBOOK

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

           /*inicializa variables de xml*/
        etusuario=(EditText)findViewById(R.id.etusuario);
        etclave=(EditText)findViewById(R.id.etclave);
        bingresar=(Button) findViewById(R.id.bingresar);
        tvregistrate=(TextView)findViewById(R.id.tvregistrate);
        bingresar.setOnClickListener(this);
        tvregistrate.setOnClickListener(this);
        /*fin inicializa variables de xml*/

        etusuario.setText("titosalinasm@hotmail.com");
        etclave.setText("123456");

        /*Login Google plus*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        //Inicializacion de botones google
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setColorScheme(signInButton.COLOR_DARK);

        // Construye el API cliente con acceso a google login
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        //laza las acciones cuando se presiona onclick signButton
        signInButton.setOnClickListener(this);
        /*FIN Login Google plus*/


        //LOGIN FACEBOOK
        bfacebook=(Button)findViewById(R.id.bfacebook);
        bfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(iniciarSesion.this, Arrays.asList("public_profile", "user_friends"));
            }
        });

        //FIN LOGIN FACEBOOK

    }

    //login facebook

    //login google
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(GoogleSignInResult result){
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Uri personPhoto = acct.getPhotoUrl();
            //textView.setText(acct.getDisplayName());
            //textViewEmail.setText(acct.getEmail());
            //Picasso.with(this).load(personPhoto+"").into(imageView);

            String nombreCompleto, nombre, apellido, espacio;
            nombreCompleto = acct.getDisplayName();
            espacio = " ";
            nombre = nombreCompleto.substring(0,nombreCompleto.indexOf(espacio));

            apellido = nombreCompleto.substring(nombreCompleto.indexOf(espacio)+1,nombreCompleto.length());

            sesionMovilUser smu=new sesionMovilUser();
            requestQueue = Volley.newRequestQueue(this);

           // Toast.makeText(this, "Nombre: "+acct.getDisplayName(), Toast.LENGTH_LONG).show();
            //datos del telefono
            String fabricante=infoMovil.getFabricante();
            String version_so=infoMovil.getVersionSo();
            String modelo=infoMovil.getModelo();
            String marca=infoMovil.getMarca();
            String imei=infoMovil.getImei(getApplicationContext());
            String codigo_confirmacion=infoMovil.codigoConfirmacion(10);

            final ProgressDialog loading = ProgressDialog.show(iniciarSesion.this,"Verificando...","Espere por favor...",false,false);
            smu.google_id_tjsegurity(requestQueue, acct.getId(), "", nombre, apellido, "O","", "", fabricante, version_so,  modelo, marca, imei, codigo_confirmacion, "google", loading, ""+personPhoto,iniciarSesion.this);


        } else {
            //If login fails
            Toast.makeText(this, "Fallo el inicio de sesion", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {

        if (v==bingresar){
            sesionMovilUser smu = new sesionMovilUser();
            if (smu.validateEmail(etusuario.getText().toString())) {
                if ( etclave.getText().toString().length()>5 ) {
                    requestQueue = Volley.newRequestQueue(this);
                    smu.validar_usuario(iniciarSesion.this, requestQueue,etusuario.getText().toString(), etclave.getText().toString() );

                }else{
                    Toast.makeText(iniciarSesion.this, "Escribe una contraseña de por lo menos 6 dígitos.", Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(iniciarSesion.this, "Escribe un correo electonico válido.", Toast.LENGTH_LONG).show();
            }
        }
        if (v==tvregistrate){
            Intent i=new Intent(this, mail_clave.class);

            startActivity(i);
        }

        if (v == signInButton) {
            //Calling signin
            signIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}
