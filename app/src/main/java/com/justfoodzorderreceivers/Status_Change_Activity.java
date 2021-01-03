package com.justfoodzorderreceivers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Status_Change_Activity extends AppCompatActivity {

    Spinner spinner;
    String[] aa = {"Select Status","Processing","Delivered","Cancelled"};
    Button button_change_ststus;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ImageView back;
    ArrayList<String> spi_item_id,spi_item_name ;
    String item_id_to_send = "";
    MyPref myPref;
    TextView title,change_status_text,textView;
    ParseLanguage parseLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_change_);

        requestQueue = Volley.newRequestQueue(this);
        button_change_ststus = (Button)findViewById(R.id.button_change_ststus);
        back = (ImageView)findViewById(R.id.iv_back);
        myPref = new MyPref(Status_Change_Activity.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),Status_Change_Activity.this);
        spinner = (Spinner)findViewById(R.id.spinner);
        title = findViewById(R.id.title);
        change_status_text = findViewById(R.id.change_status_text);
        textView = findViewById(R.id.textView);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Status_Change_Activity.this,android.R.layout.simple_dropdown_item_1line,aa);
//        spinner.setAdapter(arrayAdapter);
        spi_item_id = new ArrayList<>();
        spi_item_name = new ArrayList<>();
        orderstatusvalues();

        title.setText(parseLanguage.getParseString("Back"));
        change_status_text.setText(parseLanguage.getParseString("Change_status_of_the_Order"));
        button_change_ststus.setText(parseLanguage.getParseString("Change_Status"));
       // textView.setText(parseLanguage.getParseString());

        button_change_ststus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(Status_Change_Activity.this, item_id_to_send, Toast.LENGTH_SHORT).show();
                changestatus();


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AcceptButton_activity.this, ""+spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                finish();
                Intent i = new Intent(Status_Change_Activity.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_id_to_send = spi_item_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public void changestatus()
    {
        progressDialog = progressDialog.show(Status_Change_Activity.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.Order_Status, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error = jsonObject.getString("error");
                    if (error.equals("0"))
                    {
                        String error_msg = jsonObject.getString("error_msg");
//                        Toast.makeText(Status_Change_Activity.this, error_msg, Toast.LENGTH_SHORT).show();
                        showCustomDialog(error_msg);



                    }
                    else {
                        String error_msg = jsonObject.getString("error_msg");
//                        Toast.makeText(Status_Change_Activity.this, error_msg, Toast.LENGTH_SHORT).show();
                        showCustomDialog1(error_msg );
                        //   finish();
                    }
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
                Toast.makeText(Status_Change_Activity.this, message+volleyError, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", ""+getIntent().getStringExtra("orderid"));
                params.put("OrderStatus", item_id_to_send);
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("qwqw",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void orderstatusvalues()
    {
        spi_item_name.clear();
        spi_item_id.clear();
        progressDialog = progressDialog.show(Status_Change_Activity.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.order_statusvalues, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("OrderStatusHistory");
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String status_name = jsonObject1.getString("status_name");
                        spi_item_id.add(id);
                        spi_item_name.add(status_name);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Status_Change_Activity.this,android.R.layout.simple_dropdown_item_1line,spi_item_name);
                    spinner.setAdapter(arrayAdapter);
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
                Toast.makeText(Status_Change_Activity.this, message, Toast.LENGTH_SHORT).show();
            }
        }) ;
        requestQueue.add(stringRequest);
    }


    private void showCustomDialog (String s)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(Status_Change_Activity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Status_Change_Activity.this,Activity_Booking.class);
                i.putExtra("orderid",""+getIntent().getStringExtra("orderid"));
                startActivity(i);
                finish();
//                        Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void showCustomDialog1 (String s)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(Status_Change_Activity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.cancel);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

//                Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }



}
