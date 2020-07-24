package com.example.smartalarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.LinkedList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private final LinkedList<Alarm> alarmList;
    private Context context;

    public AlarmAdapter(Context context, LinkedList<Alarm> alarmList) {
        this.alarmList = alarmList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.alarm_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.ViewHolder holder, int position) {
        Alarm currentAlarm = alarmList.get(position);
        holder.bindTo(currentAlarm);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView alarmNameTextView;
        public final TextView alarmTimeTextView;
        public final Switch alarmOnSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmOnSwitch = itemView.findViewById(R.id.on_switch);
            alarmTimeTextView = itemView.findViewById(R.id.hour_item);
            alarmNameTextView = itemView.findViewById(R.id.name_item);
            itemView.setOnClickListener(this);
        }

        void bindTo(Alarm currentAlarm) {
            alarmNameTextView.setText(currentAlarm.getName());
            alarmOnSwitch.setChecked(currentAlarm.isOn());
            alarmTimeTextView.setText(currentAlarm.getTime());
        }

        @Override
        public void onClick(View view) {
            dialog(view.getContext());
        }
    }
    public static void dialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Configurar alarma")
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }
}