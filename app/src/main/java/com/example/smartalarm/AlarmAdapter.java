package com.example.smartalarm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.smartalarm.Alarm.IDENTIFIER;

public class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Alarm> alarmList = new ArrayList<>();
    private Context context;
    private AlarmDataBase dataBase;
    boolean is24HS;

    public AlarmAdapter(Context context) {
        this.context = context;
        SharedPreferences sharedSettings = PreferenceManager.getDefaultSharedPreferences(context); //TODO: SINGLETON
        String hourFormat = sharedSettings.getString("hours_mode", "24");
        is24HS = hourFormat.equals("24");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new SmartAlarmViewHolder(LayoutInflater.from(context).inflate(R.layout.smartalarm_view, parent, false));
        }
        return new AlarmViewHolder(LayoutInflater.from(context).inflate(R.layout.alarm_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AlarmViewHolder) {
            Alarm currentAlarm = alarmList.get(position);
            dataBase = AlarmDataBase.getInstance(context);
            AlarmViewHolder alarmViewHolder = (AlarmViewHolder) holder;
            alarmViewHolder.bindTo(currentAlarm);
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmList = alarmList;
        alarmList.add(0, new Alarm(0, 0, "Smart", new Week()));
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 0 : 1;
    }

    public Alarm getAlarmAt(int position) {
        return alarmList.get(position);
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView alarmNameTextView;
        public final TextView alarmTimeTextView;
        public final TextView alarmOnDaysTextView;
        public final Switch alarmOnSwitch;


        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmOnSwitch = itemView.findViewById(R.id.on_switch);
            alarmTimeTextView = itemView.findViewById(R.id.hour_item);
            alarmNameTextView = itemView.findViewById(R.id.name_item);
            alarmOnDaysTextView = itemView.findViewById(R.id.days_item);

            itemView.setOnClickListener(this);
        }

        void bindTo(final Alarm currentAlarm) {

            alarmTimeTextView.setText(currentAlarm.getFormattedTime(context));
            alarmNameTextView.setText(currentAlarm.getName());
            alarmOnSwitch.setChecked(currentAlarm.isOn());
            alarmOnDaysTextView.setText(currentAlarm.getWeek().toString());
            alarmOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    currentAlarm.setOn(b);
                    if (b)
                        currentAlarm.schedule(context);
                    else currentAlarm.cancel(context);
                    dataBase.alarmDao().update(currentAlarm);
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
            dialogTimePicker.setIs24HourView(is24HS);
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
                    Intent intent = new Intent(view.getContext(), NewAlarmActivity.class);
                    intent.putExtra(IDENTIFIER, editedAlarm.getID());
                    alarmSetterDialog.dismiss();
                    context.startActivity(intent);
                }
            });
            alarmSetterDialog.show();
        }
    }

    public class SmartAlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox smartCheckBox;

        public SmartAlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            smartCheckBox = itemView.findViewById(R.id.smart_alarm_toggle);

            smartCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}