package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {
    int itemCount;
    Stopwatch stopwatchFragment;
    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity, int itemCount) {
        super(fragmentActivity);
        stopwatchFragment = new Stopwatch();
        this.itemCount = itemCount;
    }

    public Stopwatch getStopwatchFragment() {
        return stopwatchFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new MainAlarm();
            case 1: return new SmartAlarm();
            case 2: return stopwatchFragment;
        }
        return new MainAlarm();
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
