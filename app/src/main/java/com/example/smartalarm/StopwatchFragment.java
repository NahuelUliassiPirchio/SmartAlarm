package com.example.smartalarm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class StopwatchFragment extends Fragment {
    public static final String STOPWATCH = "STOPWATCH_TYPE";

    private com.example.smartalarm.Chronometer swChronometer;
    private FloatingActionButton playPauseFAB, resetFAB, lapFAB;
    private long pauseOffset;
    private boolean isRunning;
    private int progress = 0;
    private ContentLoadingProgressBar materialProgressBar;
    private RecyclerView lapRecycler;
    private ArrayList<String> laps;
    private StopwatchAdapter lapStopwatchAdapter;

    public StopwatchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View stopwatchView = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        playPauseFAB = stopwatchView.findViewById(R.id.play_pause_fab);
        resetFAB = stopwatchView.findViewById(R.id.reset_fab);
        lapFAB = stopwatchView.findViewById(R.id.lap_fab);

        lapRecycler = stopwatchView.findViewById(R.id.lap_recycler);
        laps = new ArrayList<>();
        lapStopwatchAdapter = new StopwatchAdapter(laps, getContext());
        lapRecycler.setAdapter(lapStopwatchAdapter);
        lapRecycler.setLayoutManager(new LinearLayoutManager(stopwatchView.getContext()));

        materialProgressBar = stopwatchView.findViewById(R.id.chronometer_progressBar);
        swChronometer = stopwatchView.findViewById(R.id.stopwatch_chronometer);
        swChronometer.setBase(SystemClock.elapsedRealtime());
        isRunning = false;
        swChronometer.setOnChronometerTickListener(new com.example.smartalarm.Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(com.example.smartalarm.Chronometer chronometer) {
                progress++;
                if (progress > 590)
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

        return stopwatchView;
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
        lapRecycler.smoothScrollToPosition(laps.size()-1);
        lapStopwatchAdapter.notifyDataSetChanged();
        lapRecycler.setVisibility(View.VISIBLE);
    }

    public void setChronometerBase (long chronometerBase){
        swChronometer.setBase(chronometerBase);
        swChronometer.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (isRunning) {
            Intent serviceIntent = new Intent(getContext(), ChronometerService.class);
            serviceIntent.putExtra(TYPE_OF_CHRONOMETER, STOPWATCH);
            serviceIntent.putExtra(CHRONOMETER_BASE, swChronometer.getBase());
            getContext().startService(serviceIntent);
        } TODO: STOPWATCH*/
    }
}
