package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import static com.example.smartalarm.SmartAlarmFragment.SMART_ALARM_MINUTES;

public class PhoneLockingListener extends Service {
    SmartAlarmReceiver smartReceiver;
    public static int countOn = 0;
    public static int countOff = 0;
    private int minutes;

    public PhoneLockingListener() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_ANSWER);
        smartReceiver = new SmartAlarmReceiver();
        registerReceiver(smartReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smartReceiver);
        Log.i("jjonDestroy Reciever", "Called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        minutes = intent.getIntExtra(SMART_ALARM_MINUTES, 0);
        smartReceiver.setAlarmMinutes(minutes);

        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            Log.i("jjscreenON", "Called");
            Log.i("jjviaService", "CountOn =" + countOn);

            //Toast.makeText(getApplicationContext(), "Awake", Toast.LENGTH_LONG).show();
        } else {
            Log.i("jjscreenOFF", "Called");
            Log.i("jjviaService", "CountOff =" + countOff);
        }


        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        restartServiceIntent.putExtra(SMART_ALARM_MINUTES, minutes);

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
