package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class NewAlarm extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.twoactivities.extra.REPLY";
    Intent replyIntent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        EditText hourSetter = findViewById(R.id.hour_setter);
        EditText minuteSetter = findViewById(R.id.minute_setter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            minuteSetter.setText(new SimpleDateFormat("mm", Locale.getDefault()).format(new Date()));
            hourSetter.setText(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
        }
      //  hourSetter.setText(DateFormat.getDateInstance(DateFormat.HOUR0_FIELD).format(new Date()));

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonsListener(view);
            }
        });
    }

    public void buttonsListener(View view) {
        if (view.getId()==R.id.cancel_button)
        replyIntent.putExtra(EXTRA_REPLY, false);
        else
            replyIntent.putExtra(EXTRA_REPLY, true);

        setResult(RESULT_OK, replyIntent);
        finish();
    }
}