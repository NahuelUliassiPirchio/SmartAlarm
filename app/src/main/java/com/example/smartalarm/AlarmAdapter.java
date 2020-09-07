package com.example.smartalarm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.smartalarm.Alarm.IDENTIFIER;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private final List<Alarm> alarmList;
    private Context context;
    private AlarmDataBase dataBase;

    public AlarmAdapter(Context context, List<Alarm> alarmList) {
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
        dataBase = AlarmDataBase.getInstance(context);
        holder.bindTo(currentAlarm);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView alarmNameTextView;
        public final TextView alarmTimeTextView;
        public final TextView alarmOnDaysTextView;
        public final Switch alarmOnSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmOnSwitch = itemView.findViewById(R.id.on_switch);
            alarmTimeTextView = itemView.findViewById(R.id.hour_item);
            alarmNameTextView = itemView.findViewById(R.id.name_item);
            alarmOnDaysTextView = itemView.findViewById(R.id.days_item);

            itemView.setOnClickListener(this);
        }

        void bindTo(final Alarm currentAlarm) {
            alarmNameTextView.setText(currentAlarm.getName());
            alarmOnSwitch.setChecked(currentAlarm.isOn());
            alarmTimeTextView.setText(currentAlarm.getHours() + ":" + currentAlarm.getMinutes());
            alarmOnDaysTextView.setText(currentAlarm.getWeek().toString());
            alarmOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    currentAlarm.setOn(b);
                    if (b)
                        currentAlarm.schedule(context);
                    else currentAlarm.cancel(context);
                    dataBase.alarmDao().update(currentAlarm);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onClick(View view) {
            final Alarm editedAlarm = alarmList.get(getAdapterPosition());

            String alarmName = editedAlarm.getName();

            final Dialog alarmSetterDialog = new Dialog(context);
            alarmSetterDialog.setContentView(R.layout.alarm_setter_dialog);

            TextView dialogName = alarmSetterDialog.findViewById(R.id.dialog_alarm_name);
            dialogName.setText(alarmName);

            final TimePicker dialogTimePicker = alarmSetterDialog.findViewById(R.id.dialog_time_editor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dialogTimePicker.setHour(editedAlarm.getHours());
                dialogTimePicker.setMinute(editedAlarm.getMinutes());
            } else {
                dialogTimePicker.setCurrentHour(editedAlarm.getHours());
                dialogTimePicker.setCurrentMinute(editedAlarm.getMinutes());
            }


            final Button dialogDoneButton = alarmSetterDialog.findViewById(R.id.done_dialog_button);
            final Button dialogMoreOptionsButton = alarmSetterDialog.findViewById(R.id.more_dialog_button);
            dialogDoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmSetterDialog.dismiss();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        editedAlarm.setHours(dialogTimePicker.getHour());
                        editedAlarm.setMinutes(dialogTimePicker.getMinute());
                    } else {
                        editedAlarm.setHours(dialogTimePicker.getCurrentHour());
                        editedAlarm.setMinutes(dialogTimePicker.getCurrentMinute());
                    }
                    dataBase.alarmDao().update(editedAlarm);
                    editedAlarm.schedule(context);
                    notifyDataSetChanged();
                }
            });
            dialogMoreOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NewAlarm.class);
                    intent.putExtra(IDENTIFIER, editedAlarm.getID());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });
            alarmSetterDialog.show();
        }
    }
}