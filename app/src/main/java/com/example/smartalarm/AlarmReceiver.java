package com.example.smartalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import static com.example.smartalarm.Alarm.IDENTIFIER;
import static com.example.smartalarm.Alarm.WEEK;

public class AlarmReceiver extends BroadcastReceiver {

    private Week actualAlarmWeek;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "Alarm Reboot", Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        } else {
            String actualAlarmWeekString = intent.getStringExtra(WEEK);

            try{actualAlarmWeek = Converters.fromStringToWeek(actualAlarmWeekString);
            if (actualAlarmWeek.alarmMode() == Week.ONCE) {
                int actualAlarmID = intent.getIntExtra(IDENTIFIER, 0);
                Log.e("Facts", String.valueOf(actualAlarmID));
                Log.e("Facts", actualAlarmWeekString);
                AlarmDataBase alarmDataBase = AlarmDataBase.getInstance(context);
                Alarm a = alarmDataBase.alarmDao().getAlarmById(actualAlarmID);
                a.setOn(false);
                alarmDataBase.alarmDao().update(a);

                startAlarmService(context, intent);
            } else if (IsToday()) {
                startAlarmService(context, intent);
            }}catch (NullPointerException e){
                startAlarmService(context,intent);
            }
        }
    }

    private boolean IsToday() {
        Calendar calendar = Calendar.getInstance();
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
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

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(Alarm.NAME, intent.getStringExtra(Alarm.NAME));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startRescheduleAlarmsService(final Context context) {
        Toast.makeText(context, "Rescheduling", Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmDataBase dataBase = AlarmDataBase.getInstance(context);
                List<Alarm> allOnAlarms = dataBase.alarmDao().getAllOn();
                for (Alarm alarm : allOnAlarms
                ) {
                    alarm.schedule(context);
                }
            }
        }).start();
    }
}
