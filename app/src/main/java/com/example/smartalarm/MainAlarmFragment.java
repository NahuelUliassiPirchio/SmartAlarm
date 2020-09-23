package com.example.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;
import java.util.List;

import static com.example.smartalarm.MainActivity.TEXT_REQUEST;

public class MainAlarmFragment extends Fragment {
    private List<Alarm> alarmList = new LinkedList<>();
    private AlarmAdapter alarmAdapter;
    private AlarmDataBase alarmDataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        alarmDataBase = AlarmDataBase.getInstance(getContext());
        alarmDataBase.alarmDao().getAll().observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                alarmAdapter.setAlarmList(alarms);
            }
        });
        final View view = inflater.inflate(R.layout.fragment_main_alarm, container, false);

        FloatingActionButton addFAB = view.findViewById(R.id.add_fab);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), NewAlarmActivity.class), TEXT_REQUEST);
            }
        });

        RecyclerView alarmRecycler = view.findViewById(R.id.alarm_recycler);
        alarmAdapter = new AlarmAdapter(view.getContext());
        alarmRecycler.setAdapter(alarmAdapter);
        alarmRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int deletedAlarmPosition = viewHolder.getAdapterPosition();
                final Alarm deletedAlarm = alarmAdapter.getAlarmAt(deletedAlarmPosition);
                deletedAlarm.cancel(getContext());
                alarmDataBase.alarmDao().delete(deletedAlarm);
                alarmAdapter.notifyItemRemoved(deletedAlarmPosition);
                Snackbar undoSnackbar = Snackbar.make(getActivity().findViewById(R.id.fragment_layout), R.string.snackbar_message, Snackbar.LENGTH_SHORT);
                undoSnackbar.setAction(R.string.snackbar_undo_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alarmDataBase.alarmDao().insert(deletedAlarm);
                        deletedAlarm.schedule(getContext());
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