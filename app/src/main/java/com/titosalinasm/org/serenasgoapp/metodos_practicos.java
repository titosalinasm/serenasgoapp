package com.titosalinasm.org.serenasgoapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Salinas on 11/02/2017.
 */

public class metodos_practicos {

    public String fecha_hora(){
        Date date = new Date();
        DateFormat fecha_our = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return fecha_our.format(date)+"";
    }

}
