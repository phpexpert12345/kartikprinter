package com.justfoodzorderreceivers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.PopupViewHolder> {
    List<String> titles;
   public int selected_position=-1;

    public PopupAdapter(List<String> titles){
        this.titles=titles;
    }

    @NonNull
    @Override
    public PopupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopupViewHolder holder, int position) {
String title=titles.get(position);
if(selected_position==position){
    holder.popup_radio.setChecked(true);
}
else{
    holder.popup_radio.setChecked(false);
}
holder.txt_popup.setText(title);
holder.linear_popup.setTag(position);
holder.linear_popup.setOnClickListener(v->{
    int pos= (int) v.getTag();
    selected_position=pos;
    notifyDataSetChanged();
});
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class PopupViewHolder extends RecyclerView.ViewHolder {
        TextView txt_popup;
        LinearLayout linear_popup;
        AppCompatRadioButton popup_radio;
        public PopupViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_popup=itemView.findViewById(R.id.txt_popup);
            linear_popup=itemView.findViewById(R.id.linear_popup);
            popup_radio=itemView.findViewById(R.id.popup_radio);
        }
    }
}
