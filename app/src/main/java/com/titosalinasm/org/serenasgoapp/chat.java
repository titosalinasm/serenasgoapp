package com.titosalinasm.org.serenasgoapp;

import android.database.DataSetObserver;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class chat extends AppCompatActivity {
    ImageButton btn_send;
    ImageView  ivatraz;
    TextView msg_input;
    GridView lv_mensajes;
    private adaptadorMensaje adaptadorMensajev;
    ArrayList<TMessageView> modeloMensaje = new ArrayList<>();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refChat = ref.child("chats").child("user_"+variablesGlobales.idusuario_movil);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        btn_send=(ImageButton)findViewById(R.id.btn_send);
        msg_input=(TextView)findViewById(R.id.msg_input);
        lv_mensajes=(GridView)findViewById(R.id.lv_mensajes);
        ivatraz=(ImageView)findViewById(R.id.ivatraz);
        ivatraz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        refChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

               String vidusuario=dataSnapshot.child("idusuario").getValue().toString();

                String nombre=dataSnapshot.child("nombre").getValue().toString();

                String avatar=dataSnapshot.child("avatar").getValue().toString();

                String mensaje=dataSnapshot.child("msg").getValue().toString();

                String fecha_hora=dataSnapshot.child("fecha_hora").getValue().toString();

                    TMessageView m=new TMessageView();
                    m.setIdusuariosuario(vidusuario);
                    m.setNombreRemitente(nombre);
                    m.setImgAvatar(avatar);
                    m.setMensajeRemitente(mensaje);

                modeloMensaje.add(m);
                adaptadorMensajev=new adaptadorMensaje(modeloMensaje, chat.this);

                lv_mensajes=(GridView)findViewById(R.id.lv_mensajes);
                lv_mensajes.setAdapter(adaptadorMensajev);
                adaptadorMensajev.notifyDataSetChanged();
                lv_mensajes.setSelection(adaptadorMensajev.getCount()-1);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        msg_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_mensajes.setSelection(adaptadorMensajev.getCount()-1);
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msg_input.getText().toString().trim().length()>0) {
                    metodos_practicos fech = new metodos_practicos();
                    String fecha_our = fech.fecha_hora();
                    Map<String, Object> map = new HashMap<String, Object>();
                    String temp_key = refChat.push().getKey();
                    refChat.updateChildren(map);
                    DatabaseReference message_root = refChat.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("idusuario", variablesGlobales.idusuario_movil);
                    map2.put("nombre", variablesGlobales.nombre_movil);
                    map2.put("avatar", variablesGlobales.avatar_movil);
                    map2.put("msg", msg_input.getText().toString());
                    map2.put("fecha_hora", fecha_our.toString());
                    map2.put("nombre_attend", "");
                    map2.put("avatar_attend", "");
                    message_root.updateChildren(map2);
                    msg_input.setText("");
                }
            }
        });

    }

}
