package com.titosalinasm.org.serenasgoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class send_report extends AppCompatActivity implements DialogInterface.OnClickListener {
    ImageView iv_categoria_rep;
    TextView tv_categoria_rep;
    TextView et_fecha_hora;
    TextView tv_categoria_rep_body;
    ImageView iv_categoria_rep_body;
    ImageView iv_select_img;
    EditText et_descripcion_reporte;
    static String idcategoria_rep;
    Button b_enviar_reporte;

    /*camara galeria*/
    private AlertDialog _photoDialog;
    private Uri mImageUri;
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040,  ACTIVITY_SHARE = 1030;
    private PhotoUtils photoUtils;
    boolean fromShare=false;
    Bitmap imagenBitmap;
    RequestQueue requestQueue;
    /*.camara galeria*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

        iv_categoria_rep=(ImageView)findViewById(R.id.iv_categoria_rep);
        tv_categoria_rep=(TextView)findViewById(R.id.tv_categoria_rep);
        et_fecha_hora=(TextView)findViewById(R.id.et_fecha_hora);
        tv_categoria_rep_body=(TextView)findViewById(R.id.tv_categoria_rep_body);
        iv_categoria_rep_body=(ImageView)findViewById(R.id.iv_categoria_rep_body);
        iv_select_img=(ImageView)findViewById(R.id.iv_select_img);
        et_descripcion_reporte=(EditText)findViewById(R.id.et_descripcion_reporte);
        b_enviar_reporte=(Button)findViewById(R.id.b_enviar_reporte);

        Bundle bundle = getIntent().getExtras();
        String img_categoria=bundle.getString("url_img_report");
        String nombre_categoria=bundle.getString("nombre_categoria");
         idcategoria_rep=bundle.getString("idcategoria_rep");


        Glide.with(this).load(img_categoria).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_categoria_rep_body);
        Glide.with(this).load(img_categoria).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_categoria_rep);
        tv_categoria_rep.setText(nombre_categoria);
        tv_categoria_rep_body.setText(nombre_categoria);

        /*calentario datapicker*/
        showDate();
         /*.calentario datapicker*/

        /*camara galeria*/
        photoUtils = new PhotoUtils(this);
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action)&& type != null) {
            if ("text/plain".equals(type)) {
                fromShare = true;
            } else if (type.startsWith("image/")) {
                fromShare = true;
                mImageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                getImage(mImageUri);
            }
        }
        getPhotoDialog();
        setPhotoButton();
        /*.camara galeria*/

        b_enviar_reporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(send_report.this);
                enviar_reporte_sucedido(requestQueue, getApplicationContext());
            }
        });

    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            Calendar c= Calendar.getInstance();
            int  dia=c.get(Calendar.DAY_OF_MONTH);
            int   mes=c.get(Calendar.MONTH);
            int  ano=c.get(Calendar.YEAR);

            return new DatePickerDialog(this,
                    myDateListener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    show_fecha_hora( arg1, arg2+1,arg3);
                }
            };

    public void show_fecha_hora( final int dayOfMonth, final int monthOfYear,final int year){
        Calendar c= Calendar.getInstance();
        int  hora=c.get(Calendar.HOUR_OF_DAY);
        int minutos=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                et_fecha_hora.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year+" "+hourOfDay+":"+minute);
            }
        },hora,minutos,false);
        timePickerDialog.show();
    }

    private void showDate() {
        Calendar calendar = Calendar.getInstance();
        Date date=new Date();
        int  year = calendar.get(Calendar.YEAR);
        int  month = calendar.get(Calendar.MONTH);
        int  day = calendar.get(Calendar.DAY_OF_MONTH);
        String fecha=year+"-"+month+"-"+day;
        String hora_minuto=date.getHours()+":"+date.getMinutes();
        et_fecha_hora.setText(fecha+" "+hora_minuto);
    }

    //camara galeria
    private void setPhotoButton(){
        iv_select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getPhotoDialog().isShowing() && !isFinishing())
                    getPhotoDialog().show();
            }
        });
    }

    private AlertDialog getPhotoDialog() {
        if (_photoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(send_report.this);

            builder.setTitle("Elija una opción");
            builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
                }
            });
            builder.setPositiveButton("Cámara", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File photo = null;
                    try {
                        // place where to store camera taken picture
                        photo = PhotoUtils.createTemporaryFile("picture", ".jpg", send_report.this);
                        photo.delete();
                    } catch (Exception e) {
                        Log.v(getClass().getSimpleName(), "Can't create file to take picture!");
                    }
                    mImageUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
                }
            });

            _photoDialog = builder.create();

        }
        return _photoDialog;

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = null;
        try {
            // place where to store camera taken picture
            photo = PhotoUtils.createTemporaryFile("picture", ".jpg", this);
            photo.delete();
        } catch (Exception e) {
            Log.v(getClass().getSimpleName(), "Can't create file to take picture!");
        }
        mImageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null)
            outState.putString("Uri", mImageUri.toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (savedInstanceState.containsKey("Uri")) {
            mImageUri = Uri.parse(savedInstanceState.getString("Uri"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            getImage(mImageUri);
        } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
                && resultCode == RESULT_OK) {
            getImage(mImageUri);
        }
    }
    private void setImageBitmap(Bitmap bitmap){
        imagenBitmap =bitmap;
        iv_select_img.setPadding(0, 0, 0, 0);
        iv_select_img.setImageBitmap(bitmap);
    }

    public void getImage(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {
            setImageBitmap(bounds);
        } else {
            Toast.makeText(this, "error showToast", Toast.LENGTH_SHORT).show();
        }
    }
    //.camara galeria
    Bitmap thumb;
    public String getStringImage(Bitmap bmp){

        thumb=bmp;
        int ancho=bmp.getWidth();
        int alto=bmp.getHeight();
        Log.d("hola tito", ""+ancho+"x-"+alto);
        while ((ancho+alto)>1024) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;
            thumb = Bitmap.createScaledBitmap(thumb, thumb.getWidth()/2, thumb.getHeight()/2 , false);
            ancho=thumb.getWidth();
            alto=thumb.getHeight();
            Log.d("abel", ancho+"x"+alto+"");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public void enviar_reporte_sucedido(final RequestQueue req, final Context context){
        final ProgressDialog loading = ProgressDialog.show(send_report.this,"Enviando...","Espere por favor...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"apirest.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            if (respuestaJSON.getString("estado").equals("1")){
                                loading.dismiss();
                                Toast.makeText(send_report.this, "Se envio reporte con exito!", Toast.LENGTH_SHORT).show();
                            }else{
                                loading.dismiss();
                                Toast.makeText(send_report.this, "Otra cosa pero llego", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();

                loading.dismiss();
                Toast.makeText(context, "Algo salio mal :(. Por favor intentalo otra en unos minutos."+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                String image=getStringImage(imagenBitmap);
                Map<String, String> params = new HashMap<String, String>();
                params.put("idusuario", variablesGlobales.idusuario_movil);
                params.put("idcategoria_reporte", idcategoria_rep);
                params.put("imagen", image);
                params.put("fecha_hora_event", et_fecha_hora.getText().toString());
                params.put("descripcion", et_descripcion_reporte.getText().toString());
                params.put("latitud", variablesGlobales.latitud);
                params.put("longitud", variablesGlobales.longitud);
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }


}
