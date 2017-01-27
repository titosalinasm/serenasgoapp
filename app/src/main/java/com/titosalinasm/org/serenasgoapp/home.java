package com.titosalinasm.org.serenasgoapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{

    TextView tvcodigo;

    ImageView fotoperfil;
    TextView tvnombresapellidos;
    TextView tvcorreoelectronico;
    TextView tv_finpublic;
    RequestQueue requestQueue;
    ArrayList<TCardview>  model=new ArrayList<>();
    //listview tito
    private ListView lista;
    private Adaptador adapter;
    private SwipeRefreshLayout swipeContainer;
    //TAB


    //FIN TAB
    //Real Time
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference publicacionRef = ref.child("publicacion");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvcodigo=(TextView)findViewById(R.id.tvcodigo);
        tv_finpublic=(TextView)findViewById(R.id.tv_finpublic);
        tv_finpublic.setVisibility(View.GONE);
        //ImageView pro=(ImageView)header.findViewById(R.id.profile_image);

        Bundle bundle=getIntent().getExtras();
        String idusuario=bundle.getString("idusuario");
        String usuario=bundle.getString("usuario");
        String foto=bundle.getString("foto");
        String nombres=bundle.getString("nombres");
        String apellidos=bundle.getString("apellidos");

        requestQueue = Volley.newRequestQueue(this);
        recupera_ultimas_publicaciones(requestQueue,home.this);
        lista=(ListView)findViewById(R.id.h_lv_modelo);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.srlContainer);
        swipeContainer.setOnRefreshListener(this);


        publicacionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //String valuess = dataSnapshot.getValue(String.class);
                if(variablesGlobales.sucesofirebase!=1) {
                    displayNotification();
                    swipeContainer.setRefreshing(true);
                    requestQueue = Volley.newRequestQueue(home.this);
                    model=new ArrayList<>();
                    variablesGlobales.codigo_layout=1;
                    variablesGlobales.limite_inferior_publicacion=2;
                    recupera_ultimas_publicaciones(requestQueue, home.this);

                }else{
                    variablesGlobales.sucesofirebase++;
                }
                //Toast.makeText(home.this, "Se publico algo nuevo "+valuess , Toast.LENGTH_SHORT).show();
                //Log.d("abelerror", valuess);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        lista.setOnScrollListener(new AbsListView.OnScrollListener() {
            int cont=0;
            boolean userScrolled = false;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (variablesGlobales.cantidadpublicaciones>variablesGlobales.limite_inferior_publicacion) {
                    if (userScrolled
                            && firstVisibleItem + visibleItemCount == totalItemCount) {
                        userScrolled = false;
                        requestQueue = Volley.newRequestQueue(home.this);
                        swipeContainer.setRefreshing(true);

                        agrega_publicacion_mas(requestQueue, home.this);
                    }
                }else {
                    swipeContainer.setRefreshing(false);
                    tv_finpublic.setVisibility(View.VISIBLE);
                }
            }
        });
        // tvcodigo.setText(imei);
        //TAB

        Resources res = getResources();
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", res.getDrawable(R.mipmap.news3));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", res.getDrawable(R.mipmap.maps3));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", res.getDrawable(R.mipmap.category3));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("",res.getDrawable(R.mipmap.info3));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
        //FIN TAB

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final View header = navigationView.getHeaderView(0);

        fotoperfil = (ImageView)header.findViewById(R.id.fotoperfil);
        tvnombresapellidos = (TextView) header.findViewById(R.id.tvnombresapellidos);
        tvcorreoelectronico = (TextView) header.findViewById(R.id.tvcorreoelectronico);

        Glide.with(home.this).load(foto).diskCacheStrategy(DiskCacheStrategy.ALL).into(fotoperfil);
        tvnombresapellidos.setText(nombres+" "+apellidos);
        tvcorreoelectronico.setText(usuario);

        navigationView.setNavigationItemSelectedListener(null);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onRefresh() {
        swipeContainer.setRefreshing(false);
    }
    public void recupera_ultimas_publicaciones(final RequestQueue req, final Context context){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"recupera_ultimas_publicaciones.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                variablesGlobales.cantidadpublicaciones=Integer.parseInt(respuestaJSON.getJSONObject("cantidadpub").getString("cantidadpublicaciones"));
                                //Log.d("abelsalinas", variablesGlobales.cantidadpublicaciones+"");

                                JSONArray resultadopublicaciones = respuestaJSON.getJSONArray("ultimaspublicaciones");
                                for(int i=0; i<resultadopublicaciones.length(); i++){
                                    JSONObject resultadoItem = (JSONObject)resultadopublicaciones.get(i);
                                    TCardview m=new TCardview();
                                    m.setFotoperfil(resultadoItem.getString("avatar"));
                                    // Log.d("titover: ", resultadoItem.getString("avatar"));
                                    m.setNombre_entidad(resultadoItem.getString("nombres")+" "+resultadoItem.getString("apellidos"));
                                    m.setHora_fech("Publicado: "+resultadoItem.getString("fecha_hora"));
                                    m.setImagen(variablesGlobales.url_img_pub+resultadoItem.getString("img"));
                                    m.setNombre(resultadoItem.getString("titulo"));
                                    m.setDescripcion(resultadoItem.getString("descripcion"));
                                    m.setCodigo(variablesGlobales.codigo_layout);
                                    model.add(m);
                                    variablesGlobales.codigo_layout++;
                                    // devuelve+=productItem.getString("idAlumno")+" "+productItem.getString("Nombre")+" "+productItem.getString("Direccion")+ "\n";
                                }
                                adapter=new Adaptador(model, home.this);
                                adapter.notifyDataSetChanged();
                                lista=(ListView)findViewById(R.id.h_lv_modelo);

                                lista.setAdapter(adapter);
                                swipeContainer.setRefreshing(false);
                                //envia al activity principal
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // loading.dismiss();
                Toast.makeText(context, "No se pudo establecer conexión con el servidor. Revisa tu conexión a internet e intenta conectarte: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }

    public void agrega_publicacion_mas(final RequestQueue req, final Context context){
        // Toast.makeText(this, "probando llegando", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, variablesGlobales.paginaweb+"mas_publicaciones.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response.toString());
                            //verifica si el usuario google esta registrado
                            if (respuestaJSON.getString("estado").equals("1")){
                                //variablesGlobales.cantidadpublicaciones=Integer.parseInt(respuestaJSON.getJSONObject("cantidadpub").getString("cantidadpublicaciones"));
                                //Log.d("abelsalinas", variablesGlobales.cantidadpublicaciones+"");

                                JSONArray resultadopublicaciones = respuestaJSON.getJSONArray("agrega_noticia");
                                for(int i=0; i<resultadopublicaciones.length(); i++){
                                    JSONObject resultadoItem = (JSONObject)resultadopublicaciones.get(i);
                                    TCardview m=new TCardview();
                                    m.setFotoperfil(resultadoItem.getString("avatar"));
                                    // Log.d("titover: ", resultadoItem.getString("avatar"));
                                    m.setNombre_entidad(resultadoItem.getString("nombres")+" "+resultadoItem.getString("apellidos"));
                                    m.setHora_fech("Publicado: "+resultadoItem.getString("fecha_hora"));
                                    m.setImagen(variablesGlobales.url_img_pub+resultadoItem.getString("img"));
                                    m.setNombre(resultadoItem.getString("titulo"));
                                    m.setDescripcion(resultadoItem.getString("descripcion"));
                                    m.setCodigo(variablesGlobales.codigo_layout);
                                    model.add(m);
                                    variablesGlobales.codigo_layout++;
                                    // devuelve+=productItem.getString("idAlumno")+" "+productItem.getString("Nombre")+" "+productItem.getString("Direccion")+ "\n";
                                }

                                adapter=new Adaptador(model, home.this);
                                adapter.notifyDataSetChanged();
                                lista=(ListView)findViewById(R.id.h_lv_modelo);
                                Log.d("errortitin", variablesGlobales.limite_inferior_publicacion+"");
                                Parcelable state = lista.onSaveInstanceState();
                                lista.setAdapter(adapter);
                                lista.onRestoreInstanceState(state);
                                swipeContainer.setRefreshing(false);

                                //envia al activity principal
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeContainer.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // loading.dismiss();
                swipeContainer.setRefreshing(false);
                Toast.makeText(context, "No se pudo establecer conexión con el servidor. Revisa tu conexión a internet e intenta conectarte: "+error, Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("limite", variablesGlobales.limite_inferior_publicacion+"");
                Log.d("titove", variablesGlobales.limite_inferior_publicacion+"");
                variablesGlobales.limite_inferior_publicacion++;
                return params;
            }
        };
// Add the request to the RequestQueue.
        req.add(stringRequest);
    }
    protected void displayNotification(){
        Intent i = new Intent(this, home.class);
        i.putExtra("notificationID", 1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        CharSequence ticker ="Nueva entrada en SekthDroid";
        CharSequence contentTitle = "SekthDroid";
        CharSequence contentText = "Visita ahora SekthDroid!";
        Notification noti = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setTicker(ticker)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.com_facebook_button_icon)
                .addAction(R.drawable.com_facebook_button_icon, ticker, pendingIntent)
                .setVibrate(new long[] {100, 250, 100, 500})
                .build();
        nm.notify(1, noti);
    }
}
