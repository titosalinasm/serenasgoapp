package com.titosalinasm.org.serenasgoapp;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().get("tipo_msj").equals("publicacion")){
            showNotification(remoteMessage.getData().get("titulo"),remoteMessage.getData().get("message"),
                    remoteMessage.getData().get("img"), remoteMessage.getData().get("avatar"), remoteMessage.getData().get("entidad"),
                    remoteMessage.getData().get("fecha_hora"));
        }

        if (remoteMessage.getData().get("tipo_msj").equals("avisos")){
            showNotification_aviso(remoteMessage.getData().get("titulo"),remoteMessage.getData().get("message"));
        }
    }
    private void showNotification(String titulo, String message, String foto, String av, String entit, String fh) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(titulo)
                .setContentText(message)
                .setSmallIcon(R.mipmap.notificationimg)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 300, 200, 300})
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }
    private void showNotification_aviso(String titulo, String message) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(titulo.substring(0, 30))
                .setContentText(message.substring(0, 30))
                .setSmallIcon(R.mipmap.aviso_notificacion)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{0, 300, 200, 300})
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }
}