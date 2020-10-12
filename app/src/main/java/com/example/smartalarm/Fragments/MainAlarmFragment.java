package com.example.smartalarm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartalarm.Alarm;
import com.example.smartalarm.AlarmAdapter;
import com.example.smartalarm.AlarmDataBase;
import com.example.smartalarm.NewAlarmActivity;
import com.example.smartalarm.R;
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
        final View mainAlarmView = inflater.inflate(R.layout.fragment_main_alarm, container, false);

        FloatingActionButton addFAB = mainAlarmView.findViewById(R.id.add_fab);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), NewAlarmActivity.class), TEXT_REQUEST);
            }
        });

        RecyclerView alarmRecycler = mainAlarmView.findViewById(R.id.alarm_recycler);
        DividerItemDecoration recyclerDivider = new DividerItemDecoration(alarmRecycler.getContext(),
                DividerItemDecoration.HORIZONTAL);
        alarmRecycler.addItemDecoration(recyclerDivider);
        alarmAdapter = new AlarmAdapter(mainAlarmView.getContext());
        alarmRecycler.setAdapter(alarmAdapter);
        alarmRecycler.setLayoutManager(new LinearLayoutManager(mainAlarmView.getContext()));
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int deletedAlarmPosition = viewHolder.getAdapterPosition();
                if (deletedAlarmPosition!=0) {
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
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder.getAdapterPosition() == 0)
                    return 0;
                //TODO instead of canceling maybe would be cool to delete it (maybe)
                return super.getMovementFlags(recyclerView, viewHolder);
            }
        });
        helper.attachToRecyclerView(alarmRecycler);
        return mainAlarmView;
    }
}