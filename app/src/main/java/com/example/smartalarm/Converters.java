package com.example.smartalarm;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

public class Converters {
    @TypeConverter
    public static String fromWeekToString(Week week) {
        StringBuilder stringWeek;
        if (week.isMon())
            stringWeek = new StringBuilder("1");
        else stringWeek = new StringBuilder("0");
        stringWeek.append(week.isTue() ? "1" : "0");
        stringWeek.append(week.isWed() ? "1" : "0");
        stringWeek.append(week.isThu() ? "1" : "0");
        stringWeek.append(week.isFri() ? "1" : "0");
        stringWeek.append(week.isSat() ? "1" : "0");
        stringWeek.append(week.isSun() ? "1" : "0");
        return stringWeek.toString().trim();
    }

    @TypeConverter
    public static Week fromStringToWeek(String stringWeek){
        Week week = new Week();

        week.setMon(stringWeek.charAt(0) == '1');
        week.setTue(stringWeek.charAt(1) == '1');
        week.setWed(stringWeek.charAt(2) == '1');
        week.setThu(stringWeek.charAt(3) == '1');
        week.setFri(stringWeek.charAt(4) == '1');
        week.setSat(stringWeek.charAt(5) == '1');
        week.setSun(stringWeek.charAt(6) == '1');

        return week;
    }
}
