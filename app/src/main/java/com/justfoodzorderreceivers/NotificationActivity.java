package com.justfoodzorderreceivers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodzorderreceivers.Model.NotificationList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {


    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    RecyclerView rl_notification;
    ArrayList<NotificationList> notificationLists;
    RelativeLayout liner_back;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    public static String order_id ="";
    LinearLayout linear_message;
    TextView error_msgTxt,titletext;
    AuthPreference authPreference;
    MyPref myPref;
    ParseLanguage parseLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


//        rl_notification = (RecyclerView) findViewById(R.id.rl_notification);
//        notificationLists = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        rl_notification = (RecyclerView) findViewById(R.id.rl_notification);
        notificationLists = new ArrayList<>();
        linear_message =(LinearLayout)findViewById(R.id.linear_message) ;
        error_msgTxt = (TextView)findViewById(R.id.error_msgTxt);
        titletext = (TextView)findViewById(R.id.titletext);
        authPreference = new AuthPreference(this);

        myPref = new MyPref(NotificationActivity.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),NotificationActivity.this);

        titletext.setText(parseLanguage.getParseString("Notifications"));
        Notification();



        liner_back = findViewById(R.id.liner_back);
        liner_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotificationActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NotificationActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    public void Notification()
    {
        notificationLists.clear();
        progressDialog = progressDialog.show(NotificationActivity.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.notification, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewOrderNotification");
                    for (int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                           String error = jsonObject1.getString("error");
                           if (error.equals("0")){

                               String id = jsonObject1.getString("OrderNotificationMessage");
                               String orderid = jsonObject1.getString("orderid");
//                               order_id=orderid;

                               String BookingID = jsonObject1.getString("BookingID");
                               String order_reference_number = jsonObject1.getString("order_reference_number");
                               String number_of_items_order = jsonObject1.getString("number_of_items_order");
                               String OrderNotificationMessage = jsonObject1.getString("OrderNotificationMessage");
                               String error1 = jsonObject1.getString("error");
                               notificationLists.add(new NotificationList(id,orderid,BookingID,order_reference_number,number_of_items_order,
                                       OrderNotificationMessage,error1));
                               linear_message.setVisibility(View.GONE);
                               rl_notification.setVisibility(View.VISIBLE);
//                               error_msgTxt.setText(error_msg);
                           }else
                           {
                               String error_msg = jsonObject1.getString("error_msg");
//                               Toast.makeText(NotificationActivity.this, ""+error_msg, Toast.LENGTH_SHORT).show();
                               linear_message.setVisibility(View.VISIBLE);
                               rl_notification.setVisibility(View.GONE);
                               error_msgTxt.setText(error_msg);
                           }

                    }
                    NotificationActivity.NotificationListView notificationListView = new NotificationListView(NotificationActivity.this,notificationLists);
                    linearLayoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
                    rl_notification.setLayoutManager(linearLayoutManager);
                    rl_notification.setAdapter(notificationListView);

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
                Toast.makeText(NotificationActivity.this, message+volleyError, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("orderid", ""+getIntent().getStringExtra("orderid"));
                params.put("restaurant_id", ""+authPreference.getrestaurant_id());
                params.put("lang_code", myPref.getCustomer_default_langauge());
//                params.put("restaurant_id",""+sharedPreferences.getString("restaurant_id",""));
//                Log.e("qwqw",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public class NotificationListView extends RecyclerView.Adapter<NotificationActivity.NotificationListView.ViewHolder> {

        Context context;
        ArrayList<NotificationList> anuj;

        public NotificationListView(Context c, ArrayList<NotificationList> p) {
            this.context = c;
            this.anuj = p;
        }

        public NotificationActivity.NotificationListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.single_notification_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

            viewHolder.tv_noti.setText(anuj.get(i).getOrderNotificationMessage());
            viewHolder.lll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent inn = new Intent(NotificationActivity.this,Activity_Booking.class);

                    inn.putExtra("orderid",""+anuj.get(i).getOrderid());//                    inn.putExtra(""+anuj.get(i).getOrderid(),""+getIntent().getStringExtra("orderid"));

                    startActivity(inn);
                    finish();
                }
            });

        }


        @Override
        public int getItemCount() {
            return anuj.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView tv_noti;
            LinearLayout lll;
//            View ti;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_noti = (TextView) itemView.findViewById(R.id.textnoti);
                lll = (LinearLayout) itemView.findViewById(R.id.lll);
//                ti = (View) itemView.findViewById(R.id.ti);
            }
        }
    }

}
