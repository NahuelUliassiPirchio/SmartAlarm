package com.example.smartalarm;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static com.example.smartalarm.SmartAlarm.SMART_ALARM_MINUTES;

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
        Log.i("onDestroy Reciever", "Called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        minutes = intent.getIntExtra(SMART_ALARM_MINUTES,0);
        smartReceiver.setAlarmMinutes(minutes);

        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            Log.i("screenON", "Called");
            Log.i("viaService", "CountOn =" + countOn);

            //Toast.makeText(getApplicationContext(), "Awake", Toast.LENGTH_LONG).show();
        } else {
            Log.i("screenOFF", "Called");
            Log.i("viaService", "CountOff =" + countOff);
        }


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
