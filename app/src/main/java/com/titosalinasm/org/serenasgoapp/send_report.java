package com.titosalinasm.org.serenasgoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class send_report extends AppCompatActivity {
    ImageView iv_categoria_rep;
    TextView tv_categoria_rep;
    TextView et_fecha_hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

        iv_categoria_rep=(ImageView)findViewById(R.id.iv_categoria_rep);
        tv_categoria_rep=(TextView)findViewById(R.id.tv_categoria_rep);
        et_fecha_hora=(TextView)findViewById(R.id.et_fecha_hora);

        Bundle bundle = getIntent().getExtras();
        String img_categoria=bundle.getString("url_img_report");
        String nombre_categoria=bundle.getString("nombre_categoria");
        String idcategoria_rep=bundle.getString("idcategoria_rep");

        Glide.with(this).load(img_categoria).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_categoria_rep);
        tv_categoria_rep.setText(nombre_categoria);

        /*calentario datapicker*/
        showDate();
         /*.calentario datapicker*/

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

}
