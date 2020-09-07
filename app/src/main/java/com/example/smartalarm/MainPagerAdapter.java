package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {
    int itemCount;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity, int itemCount) {
        super(fragmentActivity);
        this.itemCount = itemCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new MainAlarm();
            case 1: return new SmartAlarm();
            case 2: return new Stopwatch();
        }
        return new MainAlarm();
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
