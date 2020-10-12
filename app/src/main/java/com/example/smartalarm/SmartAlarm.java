package com.example.smartalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class SmartAlarm {
    public static final String SMART_ALARM_MINUTES = "SmartAlarmMinutes";
    public static final String SMART_ALARM_HOURS = "SmartAlarmHours";
    private static final String SMART_ALARM_ON = "SmartAlarmOn";
    private static final String SHAREDPREFILE = "com.example.smartalarm";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor preferencesEditor;
    private Context mContext;
    private Intent lockService;

    public SmartAlarm(Context context) {
        mContext = context;
        mPreferences = context.getSharedPreferences(SHAREDPREFILE, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        lockService = new Intent(context, PhoneLockingListener.class);
    }

    public void schedule(int hours, int minutes){
        lockService.putExtra(SMART_ALARM_HOURS, hours);
        lockService.putExtra(SMART_ALARM_MINUTES, minutes);

        preferencesEditor.putBoolean(SMART_ALARM_ON, true);
        preferencesEditor.putInt(SMART_ALARM_HOURS, hours);
        preferencesEditor.putInt(SMART_ALARM_MINUTES, minutes);
        preferencesEditor.apply();
        mContext.startService(lockService);
        makeToast("Service turned on");
    }

    public void cancel(){
        mContext.stopService(lockService);
        makeToast("Service turned off");
    }

    private void makeToast (String text){
        Toast.makeText(mContext,text,Toast.LENGTH_LONG).show();
    }
}