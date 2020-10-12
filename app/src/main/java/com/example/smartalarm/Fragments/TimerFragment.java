package com.example.smartalarm.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartalarm.AlarmService;
import com.example.smartalarm.Chrono;
import com.example.smartalarm.LapsAdapter;
import com.example.smartalarm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.smartalarm.Alarm.NAME;


public class TimerFragment extends Fragment implements Chrono {

    private FloatingActionButton playPauseFAB, resetFAB, lapFAB;
    private CountDownTimer timer;
    private boolean isRunning;
    private NumberPicker secondPicker, minutePicker, hourPicker;

    private RecyclerView lapRecycler;
    private ArrayList<String> laps = new ArrayList<>();
    LapsAdapter lapsAdapter;

    public TimerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View timerView = inflater.inflate(R.layout.fragment_timer, container, false);

        playPauseFAB = timerView.findViewById(R.id.play_pause_fab);
        resetFAB = timerView.findViewById(R.id.reset_fab);
        lapFAB = timerView.findViewById(R.id.lap_fab);

        lapRecycler = timerView.findViewById(R.id.lap_recycler);
        lapsAdapter = new LapsAdapter(laps, getContext());
        lapRecycler.setAdapter(lapsAdapter);
        lapRecycler.setLayoutManager(new LinearLayoutManager(timerView.getContext()));

        secondPicker = timerView.findViewById(R.id.second_picker);
        minutePicker = timerView.findViewById(R.id.minute_picker);
        hourPicker = timerView.findViewById(R.id.hour_picker);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(99);


        resetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        playPauseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable fabDrawable;
                if (isRunning) {
                    fabDrawable = getResources().getDrawable(R.drawable.ic_play, requireActivity().getTheme());
                    stopTimer();
                } else {
                    fabDrawable = getResources().getDrawable(R.drawable.ic_pause, requireActivity().getTheme());
                    startTimer();
                }
                playPauseFAB.setImageDrawable(fabDrawable);
            }
        });

        return timerView;
    }

    @Override
    public void startTimer() {
        long secondsToMillis = secondPicker.getValue() * 1000;
        secondsToMillis += minutePicker.getValue() * 6000;
        secondsToMillis += hourPicker.getValue() * 60 * 60 * 1000;
        timer = new CountDownTimer(secondsToMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                secondPicker.setValue(seconds);
                minutePicker.setValue(minutes);
                hourPicker.setValue(hours);

                secondPicker.setEnabled(false);
                minutePicker.setEnabled(false);
                hourPicker.setEnabled(false);
            }

            @Override
            public void onFinish() {
                Intent alarmService = new Intent(getContext(), AlarmService.class);
                alarmService.putExtra(NAME, "TIMER COMPLETED");
                getActivity().startService(alarmService);
            }
        };
        timer.start();
        resetFAB.setVisibility(View.GONE);
        lapFAB.setVisibility(View.VISIBLE);
        isRunning = true;
    }

    @Override
    public void stopTimer() {
        resetFAB.setVisibility(View.VISIBLE);
        lapFAB.setVisibility(View.GONE);
        isRunning = false;

        timer.cancel();
    }

    @Override
    public void resetTimer() {
        hourPicker.setValue(0);
        minutePicker.setValue(0);
        secondPicker.setValue(0);
        secondPicker.setEnabled(true);
        minutePicker.setEnabled(true);
        hourPicker.setEnabled(true);
        resetFAB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void makeLap() {

    }
}