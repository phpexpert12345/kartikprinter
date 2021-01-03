package com.justfoodzorderreceivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodzorderreceivers.Fragment.Home;
import com.justfoodzorderreceivers.Model.BookTableList;
import com.justfoodzorderreceivers.Model.HistoryModel;
import com.justfoodzorderreceivers.Model.OrderList;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderHistory extends AppCompatActivity {

    ImageView back;
    RecyclerView order_history_recycler;
    ProgressDialog progressDialog;
    ArrayList<HistoryModel> orderLists;
    MyPref myPref;
    ParseLanguage parseLanguage;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    AuthPreference authPreference;
    RequestQueue requestQueue;
    TextView no_item , aboutus_title,date,payment,orderid, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        requestQueue = Volley.newRequestQueue(OrderHistory.this);
        back = findViewById(R.id.back);
        aboutus_title=findViewById(R.id.aboutus_title);
        date=findViewById(R.id.date);
        order_history_recycler = findViewById(R.id.order_history_recycler);
        myPref = new MyPref(OrderHistory.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),OrderHistory.this);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        authPreference = new AuthPreference(OrderHistory.this);
        no_item = findViewById(R.id.no_item);
        payment=findViewById(R.id.payment);
        price=findViewById(R.id.price);
        orderid=findViewById(R.id.orderid);
        orderLists = new ArrayList<>();

if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
    aboutus_title.setText(getString(R.string.order_history));
    date.setText(getString(R.string.order_time));
    orderid.setText(getString(R.string.order_id));
    payment.setText(getString(R.string.payment));
    price.setText(getString(R.string.price));
        }
        BookingOrderList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void BookingOrderList() {
//        bookTableLists.clear();
        progressDialog = progressDialog.show(OrderHistory.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.all_order_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("Booking list",""+response);
                try {
                    JSONObject jsonObject =new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewOrdersHistory");
                    orderLists.clear();
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String error1 = j.getString("error");

                        if (error1.equals("0")) {
                            String BookingID = j.getString("BookingID");
                            String payment_mode = j.getString("payment_mode");
                            String order_date = j.getString("order_date");
                            String order_time = j.getString("order_time");
                            String Currency = j.getString("Currency");
                            String ordPrice = j.getString("ordPrice");

                            orderLists.add(new HistoryModel(BookingID,payment_mode,order_date,order_time,Currency,ordPrice));
                            }
                        else {
                            String error_msg = j.getString("error_msg");
//                            Toast.makeText(getActivity(), error_msg, Toast.LENGTH_SHORT).show();
                            }

                    }
                    if(orderLists.size()>0){
                        HistoryListView bookingListView = new HistoryListView(OrderHistory.this,orderLists);
                        linearLayoutManager = new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false);
                        order_history_recycler.setLayoutManager(linearLayoutManager);
                        order_history_recycler.setAdapter(bookingListView);
                        no_item.setVisibility(View.GONE);
                        order_history_recycler.setVisibility(View.VISIBLE);
                        String error_msg = jsonObject.getString("error_msg");
                        no_item.setText(error_msg);
                    }else {
                        String error_msg = jsonObject.getString("error_msg");
                        no_item.setText(error_msg);
                        no_item.setVisibility(View.VISIBLE);
                        order_history_recycler.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> param = new HashMap<>();
                param.put("restaurant_id",""+sharedPreferences.getString("restaurant_id",""));
                param.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("param",""+param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    public class HistoryListView extends RecyclerView.Adapter<HistoryListView.ViewHolder> {

        Context context;

        ArrayList<HistoryModel> pp;

        public HistoryListView(Context c, ArrayList<HistoryModel> p) {
            this.context = c;
            this.pp = p;

        }

        @Override
        public HistoryListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.order_history_list, parent, false);
            return new HistoryListView.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final HistoryListView.ViewHolder holder, final int position) {

            holder.date.setText(pp.get(position).getOrder_date()+", "+ pp.get(position).getOrder_time());

            holder.payment.setText(pp.get(position).getPayment_mode());
            holder.orderid.setText(pp.get(position).getBookingID());
            if(pp.get(position).getCurrency().equalsIgnoreCase("USD")){
                holder.price.setText(Activity_Splash.currency_symbol+" "+pp.get(position).getOrdPrice());
            }else {
                holder.price.setText(Activity_Splash.currency_symbol+" "+pp.get(position).getOrdPrice());
            }

        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView date, payment, orderid, price;


            public ViewHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.date);
                payment = (TextView) itemView.findViewById(R.id.payment);
                orderid = (TextView) itemView.findViewById(R.id.orderid);
                price = (TextView) itemView.findViewById(R.id.price);
              }
        }
    }
}
