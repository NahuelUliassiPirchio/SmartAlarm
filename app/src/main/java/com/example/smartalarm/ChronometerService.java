package com.example.smartalarm;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import static com.example.smartalarm.AlarmService.CHANNEL_ID;
import static com.example.smartalarm.Stopwatch.STOPWATCH;

public class ChronometerService extends Service {
    public static final String TYPE_OF_CHRONOMETER = "ServiceType";
    public static final String CHRONOMETER_BASE = "ChronometerBase";
    public static final String CHRONOMETER_CHANNEL = "ChronometerChannel";

    public ChronometerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String serviceType = intent.getStringExtra(TYPE_OF_CHRONOMETER);
        Long chronometerBase = intent.getLongExtra(CHRONOMETER_BASE,0);
        if (chronometerBase!=0) {
            Intent chronometerIntent = new Intent(getApplicationContext(),MainActivity.class);
            chronometerIntent.putExtra(TYPE_OF_CHRONOMETER,STOPWATCH);
            chronometerIntent.putExtra(CHRONOMETER_BASE,chronometerBase);
            PendingIntent chronometerPendingIntent = PendingIntent.getActivity(
                    getApplicationContext(),0,chronometerIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHRONOMETER_CHANNEL)
                    .setContentTitle(serviceType)
                    .setContentIntent(chronometerPendingIntent)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText(chronometerBase.toString());
            startForeground(1, builder.build());
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
