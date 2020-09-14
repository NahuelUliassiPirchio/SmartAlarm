package com.example.smartalarm;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static com.example.smartalarm.Alarm.NAME;

public class AlarmService extends Service {
    public static final String CHANNEL_ID = "ChannelID";
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String alarmName = intent.getStringExtra(NAME);

        Intent ringIntent = new Intent(this, RingingAlarm.class);
        ringIntent.putExtra(NAME,alarmName);
        PendingIntent ringPendingIntent = PendingIntent.getActivity(this,1,ringIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(alarmName)
                .setContentIntent(ringPendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .setAutoCancel(true)
                .setContentText("");
        startForeground(1,builder.build());

        mediaPlayer.start();

        long[] pattern = { 0, 100, 1000 };
        vibrator.vibrate(pattern, 0);
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
