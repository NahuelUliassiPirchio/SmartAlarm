package com.example.smartalarm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.LinkedList;

public class MainAlarm extends Fragment {
    private final LinkedList<Alarm> alarmList = new LinkedList<>();
    private RecyclerView alarmRecycler;
    private AlarmAdapter alarmAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        for (int i = 0; i < 10 ; i++) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String hour = "0"+i+":00";
                boolean isOn = i % 2 == 0;
                alarmList.add(new Alarm(hour,"Alarm" + i, isOn));
            }
        }


        final View view = inflater.inflate(R.layout.fragment_main_alarm, container, false);
        alarmRecycler = view.findViewById(R.id.alarm_recycler);
        alarmAdapter = new AlarmAdapter(view.getContext(),alarmList);
        alarmRecycler.setAdapter(alarmAdapter);
        alarmRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Alarm deletedAlarm = alarmList.get(viewHolder.getAdapterPosition());
                final int deletedAlarmPosition = viewHolder.getAdapterPosition();
                alarmList.remove(viewHolder.getAdapterPosition());
                alarmAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Snackbar undoSnackbar = Snackbar.make(getActivity().findViewById(R.id.fragment_layout), R.string.snackbar_message, Snackbar.LENGTH_SHORT);
                undoSnackbar.setAction(R.string.snackbar_undo_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    alarmList.add(deletedAlarmPosition,deletedAlarm);
                    alarmAdapter.notifyDataSetChanged();
                    }
                });
                undoSnackbar.show();
            }
        });
        helper.attachToRecyclerView(alarmRecycler);
        return view;
    }
}