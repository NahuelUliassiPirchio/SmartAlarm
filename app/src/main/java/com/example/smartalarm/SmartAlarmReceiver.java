package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static com.example.smartalarm.AlarmReceiver.ALARM_TYPE;

public class SmartAlarmReceiver extends BroadcastReceiver {

    public static final String SMARTALARM_CHANNEL = "SmartAlarmChannel";
    public static final String ACTION_SNOOZE = "ALARM_ACTION_SNOOZE";
    private boolean screenOff;
    private int alarmMinutes;
    AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MINUTE, alarmMinutes);

            AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.putExtra(ALARM_TYPE,SMARTALARM_CHANNEL);
            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, -200, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
            } else
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
            Log.i("Received", String.valueOf(alarmMinutes));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;

            Intent snoozeIntent = new Intent(context, ActionNotificationReceiver.class);
            snoozeIntent.putExtra(ACTION_SNOOZE, true);
            PendingIntent snoozePending =
                    PendingIntent.getBroadcast(context, 6, snoozeIntent, 0);

            Intent bedIntent = new Intent(context, ActionNotificationReceiver.class);
            snoozeIntent.putExtra(ACTION_SNOOZE, false);
            PendingIntent bedPending =
                    PendingIntent.getBroadcast(context, 5, bedIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, SMARTALARM_CHANNEL)
                    .setSmallIcon(R.drawable.ic_play)
                    .setContentTitle("Te vas a despertar???")
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_play, "SNOOZE", snoozePending)
                    .addAction(R.drawable.ic_add, "IM GONNA BED", bedPending)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat.from(context).notify(-500, builder.build());

        } else if (intent.getAction().equals(Intent.ACTION_ANSWER)) {

        }

        Intent i = new Intent(context, PhoneLockingListener.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);
    }

    public void setAlarmMinutes(int alarmMinutes) {
        this.alarmMinutes = alarmMinutes;
    }
}
