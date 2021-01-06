 package com.justfoodzorderreceivers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomComboAdapter extends RecyclerView.Adapter<CustomComboAdapter.CustomHolder> {
    private Context context;
    private ArrayList<Model_OrderComboItemExtra> model_orderComboItemOptions;
    private String currencySymbol;
    MyPref myPref;

    public CustomComboAdapter(Context context, ArrayList<Model_OrderComboItemExtra> model_orderComboItemOptions, String currencySymbol) {
        this.context = context;
        this.model_orderComboItemOptions = model_orderComboItemOptions;
        this.currencySymbol = currencySymbol;
        myPref = new MyPref(context);

    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.single_foodcombo, parent, false);
        return new CustomHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {

        holder.txt_itemqty.setText(model_orderComboItemOptions.get(position).getComboExtraItemQuantity() + " " + "×" + " " + model_orderComboItemOptions.get(position).getComboExtraItemName());
//        holder.txt_item.setText(model_orderComboItemOptions.get(position).getComboExtraItemPrice());
        String combo_price=model_orderComboItemOptions.get(position).getComboExtraItemPrice();
        if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
            combo_price=combo_price.replace(".", ",");
        }
        holder.txt_itemPRice.setText(currencySymbol+" "+combo_price);
    }

    @Override
    public int getItemCount() {
        return model_orderComboItemOptions.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        private TextView txt_itemqty, txt_item, txt_itemPRice;

        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            txt_itemqty = itemView.findViewById(R.id.txt_itemqty);
            txt_item = itemView.findViewById(R.id.txt_item);
            txt_itemPRice = itemView.findViewById(R.id.txt_itemPRice);
        }
    }
}
