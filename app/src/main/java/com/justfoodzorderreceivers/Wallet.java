package com.justfoodzorderreceivers;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodzorderreceivers.Model.Wallet_Model;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet extends AppCompatActivity {

    ImageView back;
    TextView txtwalletnumber,txttotalamount;
    SharedPreferences sharedPreferences;
    LinearLayout txtscan;
    RecyclerView rcwallet;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Wallet_Model> wallet_models;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    MyPref myPref;
    double total = 0;
    TextView error_msgTxt;
    LinearLayout linear_message;
   double totalWalletAmountPaidby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        requestQueue = Volley.newRequestQueue(this);
        back = (ImageView) findViewById(R.id.back);
        txtwalletnumber = (TextView) findViewById(R.id.txtwalletnumber);
        txttotalamount = (TextView) findViewById(R.id.txttotalamount);
        error_msgTxt = (TextView) findViewById(R.id.error_msgTxt);
        linear_message = (LinearLayout) findViewById(R.id.linear_message);
        rcwallet = (RecyclerView) findViewById(R.id.rcwallet);
        txtscan = (LinearLayout) findViewById(R.id.txtscan);
         myPref = new MyPref(Wallet.this);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Wallet.this,MainActivity.class);
                startActivity(i);
            }
        });
        txtwalletnumber.setText("Wallet number : "+sharedPreferences.getString("owner_wallet_number",""));
        txtscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Wallet.this,Scanner.class);
                startActivity(i);
            }
        });
        wallet_models = new ArrayList<>();
        getList();
    }

    public void getList()
    {
        wallet_models.clear();
        progressDialog = progressDialog.show(Wallet.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.wallet_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("TransactionHistory");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String error = j.getString("error");
                        if (error.equals("0")) {
                            String id = j.getString("id");
                            String restaurant_id = j.getString("restaurant_id");
                            String customer_id = j.getString("customer_id");
                            String wallet_payment_status = j.getString("wallet_payment_status");
                            String wallet_payment_heading = j.getString("wallet_payment_heading");
                            String wallet_order_id = j.getString("wallet_order_id");
                            String wallet_recieve_amount = j.getString("wallet_recieve_amount");
                            String wallet_payment_date = j.getString("wallet_payment_date");
                            String wallet_payment_time = j.getString("wallet_payment_time");
                            String Billed_from_name = j.getString("Billed_from_name");
                            String Billed_from_wallet_number = j.getString("Billed_from_wallet_number");
                            String Billed_from_email_address = j.getString("Billed_from_email_address");
                            String Billed_from_mobile_number = j.getString("Billed_from_mobile_number");
                            String Site_Company_Name = j.getString("Site_Company_Name");
                            String Site_Company_Address = j.getString("Site_Company_Address");
                            String wallet_payment_status_icon = j.getString("wallet_payment_status_icon");
                            String TotalWalletAmountPaidby = j.getString("TotalWalletAmountPaidby");
                            if (wallet_payment_status.equals("Paid to")){
                                double xx = Double.parseDouble(wallet_recieve_amount);
                                totalWalletAmountPaidby= Double.parseDouble(TotalWalletAmountPaidby);
                              total = total - xx-totalWalletAmountPaidby;

                            }else {
                                double xx = Double.parseDouble(wallet_recieve_amount);
                                total = total + xx;
                            }
                            wallet_models.add(new Wallet_Model(id,restaurant_id,customer_id, wallet_payment_status,wallet_payment_heading,wallet_order_id,wallet_recieve_amount,wallet_payment_date,
                                    wallet_payment_time,Billed_from_name,Billed_from_wallet_number,Billed_from_email_address,Billed_from_mobile_number, Site_Company_Name,
                                    Site_Company_Address,wallet_payment_status_icon));
                        }else {
                            String error_msg = j.getString("error_msg");
                            rcwallet.setVisibility(View.GONE);
                            linear_message.setVisibility(View.VISIBLE);
                            error_msgTxt.setVisibility(View.VISIBLE);
                            error_msgTxt.setText(error_msg);
                        }
                    }
                    txttotalamount.setText(Activity_Splash.currency_symbol+" "+Double.parseDouble(new DecimalFormat("##.####").format(total)));

                    HistoryList orderListView = new HistoryList(Wallet.this,wallet_models);
                    linearLayoutManager = new LinearLayoutManager(Wallet.this, LinearLayoutManager.VERTICAL, false);
                    rcwallet.setLayoutManager(linearLayoutManager);
                    rcwallet.setAdapter(orderListView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Log.e("error",""+volleyError);
                String message="";
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    message=getString(R.string.check_internet);
                }
                else {
                    message="Please Check your network connection";
                }
                Toast.makeText(Wallet.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", ""+sharedPreferences.getString("restaurant_id",""));
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("qwqw",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public class HistoryList extends RecyclerView.Adapter<HistoryList.ViewHolder> {

        Context context;
        ArrayList<Wallet_Model> ww;

        public HistoryList(Context c,ArrayList<Wallet_Model> w) {
            this.context = c;
            this.ww = w;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.wallet_history, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.e("size",""+ww.size());
            holder.txtwalletheading.setText(""+ww.get(position).getWallet_payment_heading());
            holder.txttime.setText(""+ww.get(position).getWallet_payment_date()+" "+ww.get(position).getWallet_payment_time());
            if (ww.get(position).getWallet_payment_status().equals("Paid to")){
                holder.txtamount.setTextColor(getResources().getColor(R.color.red));
                holder.txtamount.setText("- "+Activity_Splash.currency_symbol+ww.get(position).getWallet_recieve_amount());
            }else {
                holder.txtamount.setTextColor(getResources().getColor(R.color.green));
                holder.txtamount.setText("+ "+Activity_Splash.currency_symbol+ww.get(position).getWallet_recieve_amount());
            }
            Picasso.get().load(ww.get(position).getwallet_payment_status_icon()).into(holder.walletimage);
            holder.llviewdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,Transaction_details.class);
                    i.putExtra("heading",""+ww.get(position).getWallet_payment_heading());
                    i.putExtra("address",""+ww.get(position).getSite_Company_Address());
                    i.putExtra("date",""+ww.get(position).getWallet_payment_date()+" "+ww.get(position).getWallet_payment_time());
                    i.putExtra("amount",""+ww.get(position).getWallet_recieve_amount());
                    i.putExtra("name",""+ww.get(position).getBilled_from_name());
                    i.putExtra("mobile",""+ww.get(position).getBilled_from_mobile_number());
                    i.putExtra("email",""+ww.get(position).getBilled_from_email_address());
                    i.putExtra("orderid",""+ww.get(position).getWallet_order_id());
                    i.putExtra("cname",""+ww.get(position).getSite_Company_Name());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return ww.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtwalletheading,txttime,txtamount;
            LinearLayout llviewdetail;
            ImageView walletimage;

            public ViewHolder(View itemView) {
                super(itemView);
                txtwalletheading = (TextView) itemView.findViewById(R.id.txtwalletheading);
                txttime = (TextView) itemView.findViewById(R.id.txttime);
                txtamount = (TextView) itemView.findViewById(R.id.txtamount);
                walletimage = (ImageView) itemView.findViewById(R.id.walletimage);
                llviewdetail = (LinearLayout) itemView.findViewById(R.id.llviewdetail);
            }
        }
    }
}
