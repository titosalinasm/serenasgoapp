package com.titosalinasm.org.serenasgoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.support.v4.app.DialogFragment;

public class home extends AppCompatActivity
        implements OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, GoogleMap.OnMapClickListener, LocationListener {
    //Identificadores del home
    Button tvcodigo;
    ImageView fotoperfil;
    TextView tvnombresapellidos;
    TextView tvcorreoelectronico;
    TextView tv_finpublic;
    int cont=0;
    //.Identificadores del home

    //map
    private GoogleMap mapa;
    public int contador = 0;
    public LocationManager locationManager;
    public LocationListener locationListener;
    AlertDialog alert = null;
    //.map

    //Llamada Volley
    RequestQueue requestQueue;
    //.Llamada Volley

    //ArrayList para el Adaptador
    ArrayList<TCardview> model = new ArrayList<>();
    //.ArrayList para el Adaptador

    //ListView Adaptador
    private ListView lista;
    private Adaptador adapter;
    //ListView Adaptador

    //Refresh tipo facebook
    private SwipeRefreshLayout swipeContainer;
    //.Refresh tipo facebook


    //Base de datos Firebase
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference publicacionRef = ref.child("publicacion");
    //.Base de datos Firebase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //variables del archivo home.xml
        tvcodigo = (Button) findViewById(R.id.tvcodigo);
        tv_finpublic = (TextView) findViewById(R.id.tv_finpublic);
        tv_finpublic.setVisibility(View.GONE);
        //.variables del archivo home.xml


        //ImageView pro=(ImageView)header.findViewById(R.id.profile_image);
        //recupera datos del otro activity
        Bundle bundle = getIntent().getExtras();
        variablesGlobales.idusuario=bundle.getString("idusuario");
        String usuario = bundle.getString("usuario");
        String foto = bundle.getString("foto");
        String nombres = bundle.getString("nombres");
        String apellidos = bundle.getString("apellidos");
        //.recupera datos del otro activity

        //Captura conexion del servidor
        requestQueue = Volley.newRequestQueue(this);
        //Captura conexion del servidor

        //recupera las ultimas publicaciones disponibles
        recupera_ultimas_publicaciones(requestQueue, home.this);
        lista = (ListView) findViewById(R.id.h_lv_modelo);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.srlContainer);
        swipeContainer.setOnRefreshListener(this);
        publicacionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (variablesGlobales.sucesofirebase > 1) {
                    /*
                    Iterator i = dataSnapshot.getChildren().iterator();
                    String titulo="";
                    while (i.hasNext()){
                        titulo=((DataSnapshot)i.next()).child("titulo").getValue().toString();
                    }
                    */
                    swipeContainer.setRefreshing(true);
                    requestQueue = Volley.newRequestQueue(home.this);
                    model = new ArrayList<>();
                    variablesGlobales.codigo_layout = 1;
                    variablesGlobales.limite_inferior_publicacion = 2;
                    recupera_ultimas_publicaciones(requestQueue, home.this);

                } else {
                    variablesGlobales.sucesofirebase++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lista.setOnScrollListener(new AbsListView.OnScrollListener() {
            int cont = 0;
            boolean userScrolled = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (variablesGlobales.cantidadpublicaciones > variablesGlobales.limite_inferior_publicacion) {
                    if (userScrolled
                            && firstVisibleItem + visibleItemCount == totalItemCount) {
                        userScrolled = false;
                        requestQueue = Volley.newRequestQueue(home.this);
                        swipeContainer.setRefreshing(true);

                        agrega_publicacion_mas(requestQueue, home.this);
                    }
                } else {
                    swipeContainer.setRefreshing(false);
                    tv_finpublic.setVisibility(View.VISIBLE);
                }
            }
        });
        //.recupera las ultimas publicaciones disponibles

        //map

        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapa.setMyLocationEnabled(true);
         mapa.getUiSettings().setZoomControlsEnabled(true);
         mapa.getUiSettings().setCompassEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng posicionxy = new LatLng(location.getLatitude(), location.getLongitude());
                variablesGlobales.latitud=location.getLatitude()+"";
                variablesGlobales.longitud=location.getLongitude()+"";
                contador++;

                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionxy, 16));
                //mapa.addMarker(new MarkerOptions().position(posicionxy).title("Mi posición").snippet("Emergencias #5666233"));

                Circle circle = mapa.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(1).strokeColor(Color.BLUE));

                // Toast.makeText(MapsActivity.this, "coordenadas " + contador+": ("+location.getLatitude()+", "+location.getLongitude()+")", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
         //.map

        //TAB
        Resources res = getResources();
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", res.getDrawable(R.mipmap.news6));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", res.getDrawable(R.mipmap.maps6));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", res.getDrawable(R.mipmap.messenger6));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("",res.getDrawable(R.mipmap.info6));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
        //.TAB

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab_sos = (FloatingActionButton) findViewById(R.id.fab_sos);
         FloatingActionButton fab_tegoria = (FloatingActionButton) findViewById(R.id.fab_categoria);

         fab_tegoria.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

                 categorias ct=new categorias();
                 requestQueue = Volley.newRequestQueue(home.this);
                 final ProgressDialog loading = ProgressDialog.show(home.this,"Cargando categorias...","Espere por favor...",false,false);
                 ct.recupera_categoria(requestQueue, home.this, loading);
                 ct.show(manager, "Categorias");
             }
         });

        fab_sos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
                timesos d=new timesos();
                int m=event.getAction();
               switch (m){
                   case MotionEvent.ACTION_DOWN:
                       if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                           AlertNoGps();
                       }else{
                               d.show(manager, "Timer Sos");
                       }
                       break;
                   case MotionEvent.ACTION_UP:
                       if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                           Log.d("titoup", ""+(timesos.tiempo_temp));
                           if((timesos.tiempo_temp)!=0) {
                               timesos.tiempo_temp=4;
                               Toast.makeText(home.this,  "Para enviar un SOS mantenga presionado 3 seg.", Toast.LENGTH_SHORT).show();
                               d.cancelar();
                           }else{
                               Log.d("Se completo", ""+(timesos.tiempo_temp));
                           }
                       }
                       break;
                   case MotionEvent.ACTION_CANCEL:
                       if (alert!=null) {
                           d.cancelar();
                       }
                       break;
                   case MotionEvent.ACTION_BUTTON_RELEASE:
                       if (alert!=null) {
                           d.cancelar();
                       }
                       break;
                   default:
                       return true;
                        }
                return true;
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
    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


}
