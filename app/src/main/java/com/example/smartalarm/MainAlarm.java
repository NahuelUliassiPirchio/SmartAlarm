package com.example.smartalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;
import java.util.List;

public class MainAlarm extends Fragment {
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
        alarmList = alarmDataBase.alarmDao().getAll();
        final View view = inflater.inflate(R.layout.fragment_main_alarm, container, false);
        RecyclerView alarmRecycler = view.findViewById(R.id.alarm_recycler);
        alarmAdapter = new AlarmAdapter(view.getContext(), alarmList);
        alarmRecycler.setAdapter(alarmAdapter);
        alarmRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Alarm deletedAlarm = alarmList.get(viewHolder.getAdapterPosition());
                final int deletedAlarmPosition = viewHolder.getAdapterPosition();
                deletedAlarm.cancel(getContext());
                alarmDataBase.alarmDao().delete(deletedAlarm);
                alarmList.remove(viewHolder.getAdapterPosition());
                alarmAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Snackbar undoSnackbar = Snackbar.make(getActivity().findViewById(R.id.fragment_layout), R.string.snackbar_message, Snackbar.LENGTH_SHORT);
                undoSnackbar.setAction(R.string.snackbar_undo_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alarmList.add(deletedAlarmPosition, deletedAlarm);
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



    public void update() {
        alarmList.clear();
        alarmList.addAll(alarmDataBase.alarmDao().getAll());
        alarmAdapter.notifyDataSetChanged();
    }
}