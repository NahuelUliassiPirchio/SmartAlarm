package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.smartalarm.Fragments.MainAlarmFragment;
import com.example.smartalarm.Fragments.StopwatchFragment;
import com.example.smartalarm.Fragments.TimerFragment;

public class MainPagerAdapter extends FragmentStateAdapter {
    int itemCount;
    StopwatchFragment stopwatchFragment;
    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity, int itemCount) {
        super(fragmentActivity);
        stopwatchFragment = new StopwatchFragment();
        this.itemCount = itemCount;
    }

    public StopwatchFragment getStopwatchFragment() {
        return stopwatchFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new MainAlarmFragment();
            case 1: return stopwatchFragment;
            case 2: return new TimerFragment();
        }
        return new MainAlarmFragment();
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
