package com.example.smartalarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.content.Context.MODE_PRIVATE;

public class SmartAlarmFragment extends Fragment {

    private static final String SMART_ALARM_ON = "SmartAlarmOn";
    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.example.smartalarm";

    EditText hoursSetter;
    CheckBox alarmToggle;


    public static final String SMART_ALARM_MINUTES = "SmartAlarmMinutes";

    public SmartAlarmFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        boolean toggleStatement = alarmToggle.isChecked();
        preferencesEditor.putBoolean(SMART_ALARM_ON, toggleStatement);
        try {
            String alarmMinutesString = hoursSetter.getText().toString().trim();
            int alarmMinutes = Integer.parseInt(alarmMinutesString);
            preferencesEditor.putInt(SMART_ALARM_MINUTES, alarmMinutes);
        } catch (NumberFormatException e) {
            return;
        }
        preferencesEditor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View smartAlarmView = inflater.inflate(R.layout.fragment_smart_alarm, container, false);
        hoursSetter = smartAlarmView.findViewById(R.id.smart_hours_setter);
        alarmToggle = smartAlarmView.findViewById(R.id.smart_alarm_toggle);

        if (mPreferences.contains(SMART_ALARM_MINUTES)) {
            int minutesI = mPreferences.getInt(SMART_ALARM_MINUTES, 0);
            hoursSetter.setText(String.valueOf(minutesI));
            alarmToggle.setChecked(mPreferences.getBoolean(SMART_ALARM_ON, false));
        }
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent lockService = new Intent(getContext(), PhoneLockingListener.class);
                String toastText;
                if (b) {
                    if (!(hoursSetter.getText().toString().matches(""))) {
                        lockService.putExtra(SMART_ALARM_MINUTES, Integer.parseInt(hoursSetter.getText().toString()));
                        getActivity().startService(lockService);
                        toastText = "Service turned on";
                    } else {
                        toastText = "Service off, fill editText with something";
                        compoundButton.setChecked(false);
                    }
                } else {
                    getActivity().stopService(lockService);
                    toastText = "Service turned off";
                }
                Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
            }
        });

        return smartAlarmView;
    }
}