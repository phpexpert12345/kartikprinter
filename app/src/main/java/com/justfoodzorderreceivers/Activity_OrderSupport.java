package com.justfoodzorderreceivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.justfoodzorderreceivers.Model.OrderSupportList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_OrderSupport extends AppCompatActivity {

    Button replybutton;
    LinearLayout liner_back;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    RecyclerView recycler_complain;
    ArrayList<OrderSupportList> orderSupportLists;
    LinearLayoutManager linearLayoutManager;
    public static String Complaint_id ="";
    public static String OrderIDNumber ="";
    public static String Contact_name ="";
    public static String Contact_email ="";
    public static String Resid ="";
    SharedPreferences sharedPreferences;;
    ImageView back;
    LinearLayout linear_message,linearcomplain;
    TextView error_msgTxt,title;
    MyPref myPref ;
    ParseLanguage parseLanguage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__order_support);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        myPref = new MyPref(Activity_OrderSupport.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),Activity_OrderSupport.this);

        recycler_complain = (RecyclerView) findViewById(R.id.recycler_complain);
        orderSupportLists = new ArrayList<>();

        linear_message =(LinearLayout)findViewById(R.id.linear_message) ;
        error_msgTxt = (TextView)findViewById(R.id.error_msgTxt);

        back = (ImageView) findViewById(R.id.back);
        title = findViewById(R.id.title);
         title.setText(parseLanguage.getParseString("Order_Complaints"));

        liner_back = (LinearLayout) findViewById(R.id.liner_back);
        linearcomplain = (LinearLayout) findViewById(R.id.linearcomplain);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_OrderSupport.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        liner_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(Activity_OrderSupport.this,MainActivity.class);
              startActivity(i);
              finish();
            }
        });

        ordercomplains();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Activity_OrderSupport.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    public void ordercomplains()

    {
        progressDialog = progressDialog.show(Activity_OrderSupport.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.ordercomplainhistory, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewComplaintsHistory");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String error = jsonObject1.getString("error");
                        if (error.equals("0"))
                        {
                            String id = jsonObject1.getString("id");
                            String complaint_id = jsonObject1.getString("complaint_id");

                            String orderIssue = jsonObject1.getString("orderIssue");
                            String contact_name = jsonObject1.getString("contact_name");
                            String contact_email = jsonObject1.getString("contact_email");
                            String contact_phone = jsonObject1.getString("contact_phone");
                            String resid = jsonObject1.getString("resid");
                            String orderIDNumber = jsonObject1.getString("orderIDNumber");
                            String orderIssueEmail = jsonObject1.getString("orderIssueEmail");
                            String orderIssueMessage = jsonObject1.getString("orderIssueMessage");
                            String restaurant_order_issue_reply = jsonObject1.getString("restaurant_order_issue_reply");
                            String restaurant_order_issue_date = jsonObject1.getString("restaurant_order_issue_date");
                            String restaurant_name = jsonObject1.getString("restaurant_name");
                            String restaurant_address = jsonObject1.getString("restaurant_address");
                            String restaurant_city = jsonObject1.getString("restaurant_city");
                            String restaurant_postcode = jsonObject1.getString("restaurant_postcode");
                            String restaurant_mobile_number = jsonObject1.getString("restaurant_mobile_number");

//                            Resid = resid;
//                            Complaint_id = complaint_id;
//                            Contact_name = contact_name;
//                            Contact_email = contact_email;
//                            Contact_name = contact_name;
//                            OrderIDNumber = orderIDNumber;


                            orderSupportLists.add(new OrderSupportList(id,complaint_id,orderIssue,contact_name,contact_email,contact_phone,resid,
                              orderIDNumber,orderIssueEmail,orderIssueMessage,restaurant_order_issue_reply,restaurant_order_issue_date,
                              restaurant_name,restaurant_mobile_number));

                            linear_message.setVisibility(View.GONE);
                            linearcomplain.setVisibility(View.VISIBLE);

                        }
                        else if (error.equals("1")){
                            String error_msg = jsonObject1.getString("error_msg");
//                            Toast.makeText(Activity_OrderSupport.this, error_msg, Toast.LENGTH_SHORT).show();

                            linear_message.setVisibility(View.VISIBLE);
                            linearcomplain.setVisibility(View.GONE);
                            error_msgTxt.setText(error_msg);
                        }
                        else {
                            String error_msg = jsonObject1.getString("error_msg");
//                            Toast.makeText(Activity_OrderSupport.this, error_msg, Toast.LENGTH_SHORT).show();

                            linear_message.setVisibility(View.GONE);
                            linearcomplain.setVisibility(View.VISIBLE);
                            error_msgTxt.setText(error_msg);
                        }


                    }
                    OrderSupportListREcycler OrderSupportListREcycler = new OrderSupportListREcycler(Activity_OrderSupport.this,orderSupportLists);
                    linearLayoutManager = new LinearLayoutManager(Activity_OrderSupport.this, LinearLayoutManager.VERTICAL, false);
                    recycler_complain.setLayoutManager(linearLayoutManager);
                    recycler_complain.setAdapter(OrderSupportListREcycler);

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
                Toast.makeText(Activity_OrderSupport.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id",""+sharedPreferences.getString("restaurant_id",""));
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("anuj",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public class OrderSupportListREcycler extends RecyclerView.Adapter<Activity_OrderSupport.OrderSupportListREcycler.ViewHolder> {

        Context context;
        List<OrderSupportList> anujs;
        MyPref myPref;
        ParseLanguage parseLanguage;

        public OrderSupportListREcycler(Context c, ArrayList<OrderSupportList> p) {
            this.context = c;
            this.anujs = p;
            myPref = new MyPref(context);
            parseLanguage = new ParseLanguage(myPref.getBookingData(),context);
        }

        public Activity_OrderSupport.OrderSupportListREcycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.single_complain_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Activity_OrderSupport.OrderSupportListREcycler.ViewHolder viewHolder, int i) {
            Log.e("cc",""+anujs);

            final OrderSupportList supportList=anujs.get(i);
            viewHolder.tv_issue.setText(supportList.getOrderIssue());
            viewHolder.tv_namee.setText(supportList.getContact_name());
            viewHolder.tv_orderid.setText(supportList.getOrderIDNumber());
            viewHolder.tv_reply.setText(supportList.getOrderIssueMessage());
            viewHolder.txtnumber.setText(supportList.getContact_phone());
            viewHolder.txtemail.setText(supportList.getContact_email());
            viewHolder.your_reply.setText(supportList.getRestaurant_order_issue_reply());
            viewHolder.replybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Activity_OrderSupport.this,ReasonActivity.class);
                    i.putExtra("complaint_id",supportList.getComplaint_id());
                    i.putExtra("orderIDNumber",supportList.getOrderIDNumber());
                    i.putExtra("contact_name",supportList.getContact_name());
                    i.putExtra("contact_email",supportList.getContact_email());
                    i.putExtra("resid",supportList.getResid());


                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return anujs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView tv_issue,tv_namee,tv_reply,your_reply,tv_orderid,txtnumber,txtemail;
            TextView issue_title,name_title,order_title,mobile_title,email_title,comment_title,your_reply_title;
            Button replybutton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_issue = (TextView) itemView.findViewById(R.id.tv_issue);
                tv_namee = (TextView) itemView.findViewById(R.id.tv_namee);
                tv_reply = (TextView) itemView.findViewById(R.id.tv_reply);
                your_reply = (TextView) itemView.findViewById(R.id.your_reply);
                tv_orderid = (TextView) itemView.findViewById(R.id.tv_orderid);
                txtnumber = (TextView) itemView.findViewById(R.id.txtnumber);
                txtemail = (TextView) itemView.findViewById(R.id.txtemail);
                replybutton = (Button) itemView.findViewById(R.id.replybutton);
                issue_title = (TextView) itemView.findViewById(R.id.issue_title);
                name_title = (TextView) itemView.findViewById(R.id.name_title);
                order_title = (TextView) itemView.findViewById(R.id.order_title);
                mobile_title = (TextView) itemView.findViewById(R.id.mobile_title);
                email_title = (TextView) itemView.findViewById(R.id.email_title);
                comment_title = (TextView) itemView.findViewById(R.id.comment_title);
                your_reply_title = (TextView) itemView.findViewById(R.id.your_reply_title);

                issue_title.setText(parseLanguage.getParseString("Issue_Type"));
                name_title.setText(parseLanguage.getParseString("Name"));
                order_title.setText(parseLanguage.getParseString("Order_No"));
                mobile_title.setText(parseLanguage.getParseString("Mobile_No"));
                email_title.setText(parseLanguage.getParseString("Email_Id"));
                comment_title.setText(parseLanguage.getParseString("Issue_Comment"));
                your_reply_title.setText(parseLanguage.getParseString("Your_Reply"));

                replybutton.setText(parseLanguage.getParseString("Reply"));



            }
        }
    }
}