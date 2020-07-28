package com.example.smartalarm;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.OffsetTime;

@Entity(tableName = "alarm_table")
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "on")
    private boolean isOn;

    public Alarm(String time, String name) {
        this.time = time;
        this.name = name;
        this.isOn = true;
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
}
