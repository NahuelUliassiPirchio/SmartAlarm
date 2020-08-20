package com.example.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class NewAlarm extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.twoactivities.extra.REPLY";
    Intent replyIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        final EditText hourSetter = findViewById(R.id.hour_setter);
        final EditText minuteSetter = findViewById(R.id.minute_setter);
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

        final boolean[] days = new boolean[7]; //0 = monday , 6 = sunday

        alarmModesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch ((int) (adapterView.getItemIdAtPosition(i))) {
                    case 0:
                        daysLayout.setVisibility(View.GONE);
                        for (int j = 0; j < 6; j++) {
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

        //TODO: se ve sincero
        Calendar calendar = Calendar.getInstance();
        hourSetter.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        minuteSetter.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalHour = hourSetter.getText().toString().trim() + ":" + minuteSetter.getText().toString().trim();

                if (daysLayout.getVisibility()==View.VISIBLE) {
                    days[0] = monToggle.isChecked();
                    days[1] = tueToggle.isChecked();
                    days[2] = wedToggle.isChecked();
                    days[3] = thuToggle.isChecked();
                    days[4] = friToggle.isChecked();
                    days[5] = satToggle.isChecked();
                    days[6] = sunToggle.isChecked();
                }

                AlarmDataBase dataBase = AlarmDataBase.getInstance(getApplicationContext());
                Alarm newAlarm = new Alarm(finalHour, alarmNameSetter.getText().toString().trim(), new Week(
                        days[0],days[1],days[2],days[3],days[4],days[5],days[6]));
                dataBase.alarmDao().insert(newAlarm);
                newAlarm.schedule(getApplicationContext());
                buttonsListener(view);
            }
        });
    }

    public void buttonsListener(View view) {
        if (view.getId() == R.id.cancel_button)
            replyIntent.putExtra(EXTRA_REPLY, false);
        else
            replyIntent.putExtra(EXTRA_REPLY, true);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}