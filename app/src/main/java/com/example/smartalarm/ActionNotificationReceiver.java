package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.ALARM_SERVICE;
import static com.example.smartalarm.SmartAlarmReceiver.ACTION_SNOOZE;

public class ActionNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String toastMessage = null;
        if (intent.getBooleanExtra(ACTION_SNOOZE, false)) {
            toastMessage = "ServiceTurnedOff";

            Intent serviceIntent = new Intent(context, PhoneLockingListener.class);
            context.stopService(serviceIntent);

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -200, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            manager.cancel(pendingIntent);
        } else {
            toastMessage = "ServiceKeepsGoing";
        }
        Log.i("AcNotRec", toastMessage);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.cancel(-500);
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }
}
