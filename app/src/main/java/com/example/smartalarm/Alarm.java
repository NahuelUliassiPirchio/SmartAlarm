package com.example.smartalarm;

import androidx.room.PrimaryKey;

import java.time.OffsetTime;

public class Alarm {
    
    private String time;
    private String name;
    private boolean isOn;

    public Alarm(String time, String name, boolean isOn) {
        this.time = time;
        this.name = name;
        this.isOn = isOn;
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
}
