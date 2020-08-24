package com.example.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import static com.example.smartalarm.Alarm.IDENTIFIER;
import static com.example.smartalarm.Week.CUSTOM;
import static com.example.smartalarm.Week.DAILY;
import static com.example.smartalarm.Week.ONCE;

public class NewAlarm extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.twoactivities.extra.REPLY";
    Intent replyIntent = new Intent();
    boolean newAlarm = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        final AlarmDataBase dataBase = AlarmDataBase.getInstance(getApplicationContext());
        Alarm editedAlarm = null;

        final EditText hourSetter = findViewById(R.id.hour_setter);
        final EditText minuteSetter = findViewById(R.id.minute_setter);
        Button doneButton = findViewById(R.id.done_button);
        final EditText alarmNameSetter = findViewById(R.id.name_setter);

        final Spinner alarmModesSpinner = findViewById(R.id.alarm_modes);
        ArrayAdapter<CharSequence> alarmModesAdapter = ArrayAdapter.createFromResource(this, R.array.alarm_modes, android.R.layout.simple_spinner_item);
        alarmModesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alarmModesSpinner.setAdapter(alarmModesAdapter);

        final LinearLayout daysLayout = findViewById(R.id.days_setter);
        final ToggleButton monToggle = findViewById(R.id.monday_toggle);
        final ToggleButton tueToggle = findViewById(R.id.tuesday_toggle);
        final ToggleButton wedToggle = findViewById(R.id.wednesday_toggle);
        final ToggleButton thuToggle = findViewById(R.id.thursday_toggle);
        final ToggleButton friToggle = findViewById(R.id.friday_toggle);
        final ToggleButton satToggle = findViewById(R.id.saturday_toggle);
        final ToggleButton sunToggle = findViewById(R.id.sunday_toggle);

        final boolean[] days = new boolean[7]; //0 = monday, 6 = sunday

        final int editedAlarmID = getIntent().getIntExtra(IDENTIFIER, 0);
        if (editedAlarmID != 0) {
            editedAlarm = dataBase.alarmDao().getAlarmById(editedAlarmID);
            alarmNameSetter.setText(editedAlarm.getName());
            String[] alarmTime = editedAlarm.getTime().split(":");
            hourSetter.setText(alarmTime[0]);
            minuteSetter.setText(alarmTime[1]);
            newAlarm = false;

            int selection = -1;
            Week editedWeek = editedAlarm.getWeek();
            switch (editedWeek.alarmMode()) {
                case ONCE:
                    selection = 0;
                    break;
                case DAILY:
                    selection = 1;
                    break;
                case CUSTOM:
                    selection = 2;
                    monToggle.setChecked(editedWeek.isMon());
                    tueToggle.setChecked(editedWeek.isTue());
                    wedToggle.setChecked(editedWeek.isWed());
                    thuToggle.setChecked(editedWeek.isThu());
                    friToggle.setChecked(editedWeek.isFri());
                    satToggle.setChecked(editedWeek.isSat());
                    sunToggle.setChecked(editedWeek.isSun());
                    break;
            }

            if (selection != -1)
                alarmModesSpinner.setSelection(selection);

        } else {
            //TODO: se ve sincero
            Calendar calendar = Calendar.getInstance();
            hourSetter.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
            minuteSetter.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
        }

        alarmModesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) (adapterView.getItemIdAtPosition(i))) {
                    case 0:
                        daysLayout.setVisibility(View.GONE);
                        for (int j = 0; j < 7; j++) {
                            days[j] = false;
                        }
                        break;
                    case 1:
                        daysLayout.setVisibility(View.GONE);
                        for (int j = 0; j < 7; j++) {
                            days[j] = true;
                        }
                        break;
                    case 2:
                        daysLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final Alarm finalEditedAlarm = editedAlarm;
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalHour = hourSetter.getText().toString().trim() + ":" + minuteSetter.getText().toString().trim();
                String finalName = alarmNameSetter.getText().toString().trim();
                Week week;

                if (daysLayout.getVisibility() == View.VISIBLE) {
                    week = new Week(
                            monToggle.isChecked(),
                            tueToggle.isChecked(),
                            wedToggle.isChecked(),
                            thuToggle.isChecked(),
                            friToggle.isChecked(),
                            satToggle.isChecked(),
                            sunToggle.isChecked()
                    );
                } else week = new Week(
                        days[0],
                        days[1],
                        days[2],
                        days[3],
                        days[4],
                        days[5],
                        days[6]
                );

                if (editedAlarmID == 0) {
                    Alarm newAlarm = new Alarm(finalHour, finalName, week);
                    dataBase.alarmDao().insert(newAlarm);
                    newAlarm.schedule(getApplicationContext());
                } else {
                    finalEditedAlarm.setTime(finalHour);
                    finalEditedAlarm.setName(finalName);
                    finalEditedAlarm.setOn(true);
                    finalEditedAlarm.setWeek(week);
                    dataBase.alarmDao().update(finalEditedAlarm);
                    finalEditedAlarm.cancel(getApplicationContext());
                    finalEditedAlarm.schedule(getApplicationContext());
                }
                buttonsListener(view);
            }
        });
    }

    public void buttonsListener(View view) {
        if (newAlarm) {
            if (view.getId() == R.id.cancel_button)
                replyIntent.putExtra(EXTRA_REPLY, false);
            else
                replyIntent.putExtra(EXTRA_REPLY, true);
            setResult(RESULT_OK, replyIntent);
        } else{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        finish();
    }
}