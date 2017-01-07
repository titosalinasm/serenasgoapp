package com.titosalinasm.org.serenasgoapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        TextView tvcodigo;

        ImageView fotoperfil;
        TextView tvnombresapellidos;
        TextView tvcorreoelectronico;



    //listview tito
    private ListView lista;
    private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvcodigo=(TextView)findViewById(R.id.tvcodigo);

        //ImageView pro=(ImageView)header.findViewById(R.id.profile_image);

        Bundle bundle=getIntent().getExtras();
        String idusuario=bundle.getString("idusuario");
        String usuario=bundle.getString("usuario");
        String foto=bundle.getString("foto");
        String nombres=bundle.getString("nombres");
        String apellidos=bundle.getString("apellidos");


        ArrayList<TCardview> model=new ArrayList<>();

        TCardview m=new TCardview();
        m.setNombre("nombre 1");
        m.setDescripcion("Descripcion 1");
        m.setCodigo(1);
        model.add(m);

        m=new TCardview();
        m.setNombre("nombre 2");
        m.setDescripcion("Descripcion 2");
        m.setCodigo(2);
        model.add(m);


        m=new TCardview();
        m.setNombre("nombre 2");
        m.setDescripcion("Descripcion 2");
        m.setCodigo(2);
        model.add(m);

        adapter=new Adaptador(model, this);

        lista=(ListView)findViewById(R.id.h_lv_modelo);
        lista.setAdapter((ListAdapter) adapter);

        lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    TCardview tCardview=(TCardview)adapter.getItem(position);

                    Log.e("tCardview", tCardview.getCodigo()+"_"+tCardview.getNombre());

                    Toast.makeText(getBaseContext(), "tu codigo es: "+tCardview.getNombre(), Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // tvcodigo.setText(imei);
        //TAB
        Resources res = getResources();
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", res.getDrawable(R.mipmap.maps2));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", res.getDrawable(R.mipmap.news2));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("",res.getDrawable(R.mipmap.categoria2));
        tabs.addTab(spec);
        tabs.setCurrentTab(1);
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
}
