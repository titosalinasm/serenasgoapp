package com.titosalinasm.org.serenasgoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class tutorial_inicial extends AppCompatActivity {
    ImageView iv_cerrar_i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_inicial);
        iv_cerrar_i=(ImageView) findViewById(R.id.iv_cerrar_i);
        iv_cerrar_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
