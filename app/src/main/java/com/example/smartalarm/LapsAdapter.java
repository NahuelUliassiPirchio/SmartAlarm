package com.example.smartalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LapsAdapter extends RecyclerView.Adapter<LapsAdapter.ViewHolder> {

    private final ArrayList<String> lapList;
    private Context context;

    public LapsAdapter(ArrayList<String> lapList, Context context) {
        this.lapList = lapList;
        this.context = context;
    }

    @NonNull
    @Override
    public LapsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lap_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LapsAdapter.ViewHolder holder, int position) {
        holder.lapValueTextView.setText(lapList.get(position));
        holder.lapNumberTextView.setText(String.valueOf(position+1));
        holder.lapNameTextView.setText(String.format("Lap %d",(position+1)));
    }

    @Override
    public int getItemCount() {
        return lapList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView lapNumberTextView, lapValueTextView, lapNameTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lapNumberTextView = itemView.findViewById(R.id.lapnumber_item);
            lapNameTextView = itemView.findViewById(R.id.lapname_item);
            lapValueTextView = itemView.findViewById(R.id.lapvalue_item);
        }
    }
}
