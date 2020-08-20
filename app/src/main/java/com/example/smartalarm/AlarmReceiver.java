package com.example.smartalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private Week actualAlarmWeek;

    @Override
    public void onReceive(Context context, Intent intent) {
        actualAlarmWeek = Converters.fromStringToWeek(intent.getStringExtra(Alarm.WEEK));

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "Alarm Reboot", Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        } else {
            if (actualAlarmWeek.alarmMode() == Week.ONCE) {
                startAlarmService(context, intent);
            } else if (IsToday()) {
                startAlarmService(context, intent);
            }
        }
    }

    private boolean IsToday() {
        Calendar calendar = Calendar.getInstance();
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                return actualAlarmWeek.isMon();
            case Calendar.TUESDAY:
                return actualAlarmWeek.isTue();
            case Calendar.WEDNESDAY:
                return actualAlarmWeek.isWed();
            case Calendar.THURSDAY:
                return actualAlarmWeek.isThu();
            case Calendar.FRIDAY:
                return actualAlarmWeek.isFri();
            case Calendar.SATURDAY:
                return actualAlarmWeek.isSat();
            case Calendar.SUNDAY:
                return actualAlarmWeek.isSun();
        }
        return false;
    }

    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(Alarm.NAME, intent.getStringExtra(Alarm.NAME));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
