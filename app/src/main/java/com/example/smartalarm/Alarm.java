package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "alarm_table")
public class Alarm {
    public static final String NAME = "Alarm.NAME";
    public static final String WEEK = "Alarm.WEEK";
    public static final String IDENTIFIER = "Alarm.IDENTIFIER";

    @PrimaryKey(autoGenerate = true)
    private int ID;
    @ColumnInfo(name = "hours")
    private int hours;
    @ColumnInfo(name = "minutes")
    private int minutes;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "on")
    private boolean isOn;
    @ColumnInfo(name = "week")
    private Week week;

    public Alarm(int hours, int minutes, String name, Week week) {
        this.hours = hours;
        this.minutes = minutes;
        this.name = name;
        this.isOn = true;
        this.week = week;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getName() {
        return name;
    }

    public boolean isOn() {
        return isOn;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public void schedule(Context context) {
        String contextText;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra(IDENTIFIER, getID());
        alarmIntent.putExtra(NAME, getName());
        String weekString = Converters.fromWeekToString(week);
        alarmIntent.putExtra(WEEK, weekString);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, getID(), alarmIntent, 0);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (week.alarmMode() == Week.ONCE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),alarmPendingIntent),alarmPendingIntent);
                /*alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        alarmPendingIntent
                );*/
            }else alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmPendingIntent
            );
            contextText = "Alarm " + getName() + " set for " + calendar.get(Calendar.DAY_OF_WEEK) + " at " + hours + ":" + minutes;
        } else {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    24 * 60 * 60000,
                    alarmPendingIntent
            );
            String activeDaysString = week.alarmMode() == Week.DAILY ? " daily" : " at " + week.toString();
            contextText = "Alarm " + getName() + " set " + activeDaysString + " at " + hours + ":" + minutes; //TODO: mejorar toasts
        }
        Toast.makeText(context, contextText, Toast.LENGTH_LONG).show();
        isOn = true;
    }

    public void cancel(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, getID(), intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        isOn = false;
    }
}
