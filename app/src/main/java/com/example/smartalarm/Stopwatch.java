package com.example.smartalarm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.example.smartalarm.ChronometerService.CHRONOMETER_BASE;
import static com.example.smartalarm.ChronometerService.TYPE_OF_SERVICE;

public class Stopwatch extends Fragment {
    private Chronometer swChronometer;
    private FloatingActionButton playPauseFAB, resetFAB, lapFAB;
    private long pauseOffset;
    private boolean isRunning;
    private int progress = -1;
    private ContentLoadingProgressBar materialProgressBar;
    private RecyclerView lapRecycler;
    private ArrayList<String> laps;
    private StopwatchAdapter lapStopwatchAdapter;

    public Stopwatch() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        playPauseFAB = fragmentView.findViewById(R.id.play_pause_fab);
        resetFAB = fragmentView.findViewById(R.id.reset_fab);
        lapFAB = fragmentView.findViewById(R.id.lap_fab);

        lapRecycler = fragmentView.findViewById(R.id.lap_recycler);
        laps = new ArrayList<>();
        lapStopwatchAdapter = new StopwatchAdapter(laps, getContext());
        lapRecycler.setAdapter(lapStopwatchAdapter);
        lapRecycler.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));

        materialProgressBar = fragmentView.findViewById(R.id.chronometer_progressBar);
        swChronometer = fragmentView.findViewById(R.id.stopwatch_chronometer);
        swChronometer.setBase(SystemClock.elapsedRealtime());
        isRunning = false;
        swChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                progress++;
                if (progress > 60)
                    progress = 0;
                materialProgressBar.setProgress(progress);
            }
        });

        playPauseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable fabDrawable;
                if (isRunning) {
                    fabDrawable = getResources().getDrawable(R.drawable.ic_play, requireActivity().getTheme());
                    stopChronometer();
                } else {
                    fabDrawable = getResources().getDrawable(R.drawable.ic_pause, requireActivity().getTheme());
                    startChronometer();
                }
                playPauseFAB.setImageDrawable(fabDrawable);
            }
        });

        resetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetChronometer();
            }
        });

        lapFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeLap();
            }
        });

        return fragmentView;
    }

    private void stopChronometer() {
        swChronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - swChronometer.getBase();
        resetFAB.setVisibility(View.VISIBLE);
        lapFAB.setVisibility(View.GONE);
        isRunning = false;
    }

    private void startChronometer() {
        swChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        swChronometer.start();
        resetFAB.setVisibility(View.GONE);
        lapFAB.setVisibility(View.VISIBLE);
        isRunning = true;
    }

    public void resetChronometer() {
        swChronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        materialProgressBar.setProgress(0);
        laps.clear();
        lapStopwatchAdapter.notifyDataSetChanged();
        progress = -1;
        resetFAB.setVisibility(View.GONE);
    }

    private void makeLap() {
        laps.add(swChronometer.getText().toString());
        lapStopwatchAdapter.notifyDataSetChanged();
        lapRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Intent serviceIntent = new Intent(getContext(),ChronometerService.class);
        serviceIntent.putExtra(TYPE_OF_SERVICE,"Stopwatch");
        serviceIntent.putExtra(CHRONOMETER_BASE,swChronometer.getBase());
        getContext().startService(serviceIntent);
    }
}
