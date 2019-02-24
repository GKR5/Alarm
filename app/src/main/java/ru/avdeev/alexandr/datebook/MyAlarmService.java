package ru.avdeev.alexandr.datebook;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;


/*

Сервис запускает в строке состояния уведомление

 */

public class MyAlarmService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Служба запущена", Toast.LENGTH_SHORT).show();

        Log.d("vip", "HELLO");
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)     // по нажатию удаляем из строки состояния уведомление
                        .setContentTitle("My notification")
                        .setContentText("Будильник сработал!");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String CHANEL_ID = "CHANEL_ID";
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, "name", NotificationManager.IMPORTANCE_HIGH);
            mBuilder.setChannelId(CHANEL_ID);
            mNotificationManager.createNotificationChannel(channel);
        }


        Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );//stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

//
        mNotificationManager.notify(1, mBuilder.build());

        stopService(intent);
        //    stopSelf();

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("vip", "удалился");
    }
}
