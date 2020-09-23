package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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
            case 1: return new SmartAlarmFragment();
            case 2: return stopwatchFragment;
        }
        return new MainAlarmFragment();
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
