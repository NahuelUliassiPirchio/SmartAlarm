package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import static com.example.smartalarm.Alarm.NAME;

public class RingingAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing_alarm);

        final String alarmNameString = getIntent().getStringExtra(NAME);
        TextView alarmName = findViewById(R.id.alarm_name);
        alarmName.setText(alarmNameString);

        Button snoozeAlarmButton = findViewById(R.id.snooze_button);
        snoozeAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 10);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                alarmIntent.putExtra(NAME, alarmNameString);

                PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), -100, alarmIntent, 0);
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        alarmPendingIntent);

                        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();

            }
        });
    }


    public void stopService(View view) {
        Intent serviceIntent = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(serviceIntent);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}