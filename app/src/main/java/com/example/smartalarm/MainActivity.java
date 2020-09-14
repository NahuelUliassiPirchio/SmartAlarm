package com.example.smartalarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import static com.example.smartalarm.AlarmService.CHANNEL_ID;
import static com.example.smartalarm.ChronometerService.CHRONOMETER_BASE;
import static com.example.smartalarm.ChronometerService.CHRONOMETER_CHANNEL;
import static com.example.smartalarm.ChronometerService.TYPE_OF_CHRONOMETER;
import static com.example.smartalarm.SmartAlarmReceiver.SMARTALARM_CHANNEL;
import static com.example.smartalarm.Stopwatch.STOPWATCH;


public class MainActivity extends AppCompatActivity {
    public static final int TEXT_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createChannel(CHRONOMETER_CHANNEL);
        createChannel(SMARTALARM_CHANNEL);
        createChannel(CHANNEL_ID);

        TabLayout mainTabLayout = findViewById(R.id.main_tabLayout);
        final ViewPager2 mainViewPager = findViewById(R.id.main_viewPager);
        final MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this, mainTabLayout.getTabCount());

        mainViewPager.setAdapter(mainPagerAdapter);
        new TabLayoutMediator(mainTabLayout, mainViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Main Alarm");
                        break;
                    case 1:
                        tab.setText("Smart Alarm");
                        /*BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(Color.CYAN);
                        badgeDrawable.setVisible(true);*/
                        break;
                    case 2:
                        tab.setText("Stopwatch");
                }
            }
        }).attach();

        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                mainViewPager.setCurrentItem(tabPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        /*String typeOfService = getIntent().getStringExtra(TYPE_OF_CHRONOMETER);
        if (typeOfService != null) {
            Long chronometerBase = getIntent().getLongExtra(CHRONOMETER_BASE,0);
            TabLayout.Tab selectedTab = null;
            switch (typeOfService){
                case STOPWATCH:
                    selectedTab = mainTabLayout.getTabAt(2);
                    mainPagerAdapter.getStopwatchFragment().setChronometerBase(chronometerBase);
                    break;
            }
            selectedTab.select();
        } TODO:STOPWATCH*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_dark_mode:
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            case R.id.action_about:
                break;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createChannel(String channelId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.WHITE);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}