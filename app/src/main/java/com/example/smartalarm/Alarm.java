package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "on")
    private boolean isOn;
    @ColumnInfo(name = "week")
    private Week week;

    public Alarm(String time, String name, Week week) {
        this.time = time;
        this.name = name;
        this.isOn = true;
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setTime(String time) {
        this.time = time;
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
        alarmIntent.putExtra(IDENTIFIER,getID());
        alarmIntent.putExtra(NAME, getName());
        String weekString = Converters.fromWeekToString(week);
        alarmIntent.putExtra(WEEK, weekString);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, getID(), alarmIntent, 0);

        String[] alarmTime = getTime().split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmTime[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(alarmTime[1]));
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (week.alarmMode() == Week.ONCE) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmPendingIntent
            );
            contextText = "Alarm " + getName() + " set for " + calendar.get(Calendar.DAY_OF_WEEK) + " at" + time;
        } else {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    24 * 60 * 60000,
                    alarmPendingIntent
            );
            String activeDaysString = week.alarmMode() == Week.DAILY ? "daily" : "at " + week.toString();
            contextText = "Alarm " + getName() + "set " + activeDaysString + " at" + time;
        }
        Toast.makeText(context, contextText, Toast.LENGTH_LONG).show();
        isOn = true;
    }
    public void cancel(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, getID(), intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        isOn = false;
    }
}
