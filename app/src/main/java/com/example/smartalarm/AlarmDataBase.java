package com.example.smartalarm;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Alarm.class}, version = 1, exportSchema = false)
public abstract class AlarmDataBase extends RoomDatabase {
    private static AlarmDataBase dataBase;
    private static final String DATABASE_NAME = "alarm_database";

    public synchronized static AlarmDataBase getInstance(Context context) {
        if (dataBase == null) {
            dataBase = Room.databaseBuilder(context, AlarmDataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return dataBase;
    }

    public abstract AlarmDao alarmDao();

}
