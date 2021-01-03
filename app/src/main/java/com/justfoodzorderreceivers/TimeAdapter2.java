package com.justfoodzorderreceivers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.justfoodzorderreceivers.Model.TimeModel;

import java.util.ArrayList;

public class TimeAdapter2 extends RecyclerView.Adapter<TimeAdapter2.Holder> {
    private ArrayList<TimeModel> list;
    private Context context;
    private TimeValuesListener valuesListener;

    public TimeAdapter2(Context context, ArrayList<TimeModel> lists, TimeValuesListener valuesListener) {
        this.context = context;
        this.list = lists;
        this.valuesListener = valuesListener;

    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slots, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        final TimeModel model = list.get(position);
        holder.Time.setText(model.getTime());

        holder.Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeAdapter2.this.setResetVisibility(model);
                TimeAdapter2.this.notifyDataSetChanged();
                valuesListener.onSelectTime(model.getTime());
            }
        });

        if (model.getSelectCategory().equalsIgnoreCase("0")) {
            // holder.view.setVisibility(View.INVISIBLE);
            holder.Time.setBackgroundResource(R.drawable.border_black);
            holder.Time.setTextColor(Color.BLACK);
        } else {
            //  holder.view.setVisibility(View.VISIBLE);
            holder.Time.setBackgroundResource(R.drawable.rounded_bg_three);
            holder.Time.setTextColor(Color.WHITE);
        }
    }

    private void setResetVisibility(TimeModel sTmodel) {
        for (TimeModel p : list) {
            if (sTmodel.getTime().equalsIgnoreCase(p.getTime())) {
                p.setSelectCategory("1");
            } else {
                p.setSelectCategory("0");
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class Holder extends RecyclerView.ViewHolder {
        private TextView Time;
        private View view;

        Holder(View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.time);
            view = itemView.findViewById(R.id.view);
        }
    }
}
