package com.titosalinasm.org.serenasgoapp;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("titulo"),remoteMessage.getData().get("message"),
                remoteMessage.getData().get("img"), remoteMessage.getData().get("avatar"), remoteMessage.getData().get("entidad"),
                remoteMessage.getData().get("fecha_hora"));
    }
    private void showNotification(String titulo, String message, String foto, String av, String entit, String fh) {
        Intent i = new Intent(this, plus_noticia.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.putExtra("titulo", titulo);
        i.putExtra("descripcion", message);
        i.putExtra("img", variablesGlobales.url_img_pub+foto);

        i.putExtra("avatar", av);
        i.putExtra("entidad", entit);
        i.putExtra("fecha_h", fh);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(titulo)
                .setContentText(message)
                .setSmallIcon(R.mipmap.logicon1)
                .setVibrate(new long[] {0, 1000, 50, 2000})
                .setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }
}