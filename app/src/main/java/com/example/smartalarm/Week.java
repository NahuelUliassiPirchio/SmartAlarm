package com.example.smartalarm;

import android.content.Context;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Week {
    public static final int DAILY = 1;
    public static final int CUSTOM = 0;
    public static final int ONCE = -1;

    private boolean Mon;
    private boolean Tue;
    private boolean Wed;
    private boolean Thu;
    private boolean Fri;
    private boolean Sat;
    private boolean Sun;

    public Week() {
    }

    public Week(boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        Mon = mon;
        Tue = tue;
        Wed = wed;
        Thu = thu;
        Fri = fri;
        Sat = sat;
        Sun = sun;
    }

    public int alarmMode() {
        if (!Mon && !Tue && !Wed && !Thu && !Fri && !Sat && !Sun)
            return ONCE;
        else if (Mon && Tue && Wed && Thu && Fri && Sat && Sun){
            return DAILY;}
        else return CUSTOM;
    }

    public boolean isMon() {
        return Mon;
    }

    public void setMon(boolean mon) {
        Mon = mon;
    }

    public boolean isTue() {
        return Tue;
    }

    public void setTue(boolean tue) {
        Tue = tue;
    }

    public boolean isWed() {
        return Wed;
    }

    public void setWed(boolean wed) {
        Wed = wed;
    }

    public boolean isThu() {
        return Thu;
    }

    public void setThu(boolean thu) {
        Thu = thu;
    }

    public boolean isFri() {
        return Fri;
    }

    public void setFri(boolean fri) {
        Fri = fri;
    }

    public boolean isSat() {
        return Sat;
    }

    public void setSat(boolean sat) {
        Sat = sat;
    }

    public boolean isSun() {
        return Sun;
    }

    public void setSun(boolean sun) {
        Sun = sun;
    }

    public String toString() {
        if (alarmMode()==DAILY)
            return "Daily";
        else if (alarmMode()==ONCE)
            return "Once";

        StringBuilder week = null;
        Map<String, Boolean> booleanWeekList = new LinkedHashMap<>();

        booleanWeekList.put("Mon",Mon);//TODO: hacerlo que soporte idiomas
        booleanWeekList.put("Tue",Tue);
        booleanWeekList.put("Wed",Wed);
        booleanWeekList.put("Thu",Thu);
        booleanWeekList.put("Fri",Fri);
        booleanWeekList.put("Sat",Sat);
        booleanWeekList.put("Sun",Sun);
        Iterator<Map.Entry<String,Boolean>> entries = booleanWeekList.entrySet().iterator();

        boolean flag = true;
        while (entries.hasNext()){
            Map.Entry entry = entries.next();
            if ((Boolean) entry.getValue()) {
                if (flag) {
                    flag = false;
                    week = new StringBuilder(entry.getKey().toString());
                } else {
                    week.append(", ").append(entry.getKey().toString());
                }
            }
        }
        return week==null ? "" : week.toString();
    }
}
