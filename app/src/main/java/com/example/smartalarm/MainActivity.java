package com.example.smartalarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import static com.example.smartalarm.AlarmService.CHANNEL_ID;
import static com.example.smartalarm.ChronometerService.CHRONOMETER_CHANNEL;
import static com.example.smartalarm.SmartAlarmReceiver.SMARTALARM_CHANNEL;


public class MainActivity extends AppCompatActivity {
    public static final int TEXT_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createChannel(CHRONOMETER_CHANNEL);
        createChannel(SMARTALARM_CHANNEL);
        createChannel(CHANNEL_ID);

        Toolbar activityToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(activityToolbar);

        TabLayout mainTabLayout = findViewById(R.id.main_tabLayout);
        final ViewPager2 mainViewPager = findViewById(R.id.main_viewPager);
        final MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this, mainTabLayout.getTabCount());

        mainViewPager.setAdapter(mainPagerAdapter);
        new TabLayoutMediator(mainTabLayout, mainViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setIcon(R.drawable.ic_alarm);
                        tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent),PorterDuff.Mode.SRC_IN);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_stopwatch);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.ic_brain);
                }
            }
        }).attach();

        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                mainViewPager.setCurrentItem(tabPosition);
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.gray);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
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
                startActivity(new Intent(this, RingingAlarm.class));
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
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