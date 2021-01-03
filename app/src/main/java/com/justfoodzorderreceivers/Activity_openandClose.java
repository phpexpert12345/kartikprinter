package com.justfoodzorderreceivers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activity_openandClose extends AppCompatActivity {

    Button buttonsubmitt;
    EditText edt_time1,edt_time2,edt_time3,edt_time4;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    AuthPreference authPreference;
    RadioButton radio_open,radio_close,radio_busy;
    String radiotosend="";
    LinearLayout linearback;
    MyPref myPref;
    ParseLanguage parseLanguage;
    TextView esti_title_pickup,esti_title_deli,resto_status,title;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Activity_openandClose.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openand_close);

        requestQueue = Volley.newRequestQueue(this);
        authPreference = new AuthPreference(this);
        myPref = new MyPref(Activity_openandClose.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),Activity_openandClose.this);
        edt_time1 = (EditText)findViewById(R.id.edt_time1);
        edt_time2 = (EditText)findViewById(R.id.edt_time2);
        edt_time3 = (EditText)findViewById(R.id.edt_time3);
        edt_time4 = (EditText)findViewById(R.id.edt_time4);
        title = findViewById(R.id.title);
        title.setText(parseLanguage.getParseString("Open_CloseText"));


        esti_title_pickup=findViewById(R.id.esti_title_pickup);
        esti_title_deli=findViewById(R.id.esti_title_deli);
        //title=findViewById(R.id.title);
        resto_status=findViewById(R.id.resto_status);


        radio_busy= (RadioButton)findViewById(R.id.radio_busy);
        radio_close= (RadioButton)findViewById(R.id.radio_close);
        radio_open= (RadioButton)findViewById(R.id.radio_open);
        linearback =(LinearLayout)findViewById(R.id.linearback);
        linearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_openandClose.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        buttonsubmitt = (Button) findViewById(R.id.buttonsubmitt);
        buttonsubmitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_time1.getText().toString().equals("")){
                    edt_time1.setError("This field cannot be blank");
                    edt_time1.requestFocus();
                }else if (edt_time2.getText().toString().equals("")){
                    edt_time2.setError("This field cannot be blank");
                    edt_time2.requestFocus();
                }else if (edt_time3.getText().toString().equals("")) {
                    edt_time3.setError("This field cannot be blank");
                    edt_time3.requestFocus();

            }else if (edt_time4.getText().toString().equals("")) {
                    edt_time4.setError("This field cannot be blank");
                    edt_time4.requestFocus();
                }else
                updateapi();
            }
        });

        restroinfoset();

      radio_open.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (radio_open.isChecked()){
                  radiotosend ="1";
//                  Toast.makeText(Activity_openandClose.this, "yes", Toast.LENGTH_SHORT).show();
              }else {}
          }
      });

      radio_close.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (radio_close.isChecked()){
                  radiotosend = "3";
              }else {}
          }
      });

        radio_busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_busy.isChecked()){
                    radiotosend = "2";
                }else {}
            }
        });

    //    title.setText(parseLanguage.getParseString("Open_Close"));
        resto_status.setText(parseLanguage.getParseString("Restaurant_status")+" : ");
        esti_title_deli.setText(parseLanguage.getParseString("Estimated_time_of_delivery"));
        esti_title_pickup.setText(parseLanguage.getParseString("Estimated_pickup_time"));
        buttonsubmitt.setText(parseLanguage.getParseString("SubmitText"));
        radio_open.setText(parseLanguage.getParseString("OpenText"));
        radio_busy.setText(parseLanguage.getParseString("BusyText"));
        radio_close.setText(parseLanguage.getParseString("CloseText"));


    }

    private void updateapi() {


        progressDialog = progressDialog.show(Activity_openandClose.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.openAndclose, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error = jsonObject.getInt("error");
                    if (error == 0)
                    {
                        String error_msg = jsonObject.getString("error_msg");
                        showCustomDialog(error_msg);

                    }
                    else {
                        String error_msg1 = jsonObject.getString("error_msg");
                        showCustomDialog1(error_msg1);
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
                Toast.makeText(Activity_openandClose.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", ""+authPreference.getrestaurant_id());
                params.put("Home_delivery_Time", edt_time1.getText().toString());
                params.put("Home_delivery_Time2", edt_time2.getText().toString());
                params.put("Pickup_time", edt_time3.getText().toString());
                params.put("Pickup_time2", edt_time4.getText().toString());
                params.put("open_status", ""+radiotosend);
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("qwqw",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void restroinfoset() {


        progressDialog = progressDialog.show(Activity_openandClose.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.restroinformation, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error = jsonObject.getString("error");
                    if (error.equals("1"))
                    {
                        String Home_delivery_Time = jsonObject.getString("Home_delivery_Time");
                        String Home_delivery_Time2 = jsonObject.getString("Home_delivery_Time2");
                        String Pickup_time = jsonObject.getString("Pickup_time");
                        String Pickup_time2 = jsonObject.getString("Pickup_time2");
                        int open_status = jsonObject.getInt("open_status");
                        String open_status_msg = jsonObject.getString("open_status_msg");
                        edt_time1.setText(Home_delivery_Time);
                        edt_time2.setText(Home_delivery_Time2);
                        edt_time3.setText(Pickup_time);
                        edt_time4.setText(Pickup_time2);
                        radiotosend = ""+open_status;
                        if (open_status==1){
                            radio_open.setChecked(true);
                        }else if (open_status==2){
                            radio_busy.setChecked(true);
                        }else if (open_status==3){
                            radio_close.setChecked(true);
                        }


                    }
                    else {
                        String success_msg = jsonObject.getString("success_msg");
                        showCustomDialog1(success_msg);
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
                Toast.makeText(Activity_openandClose.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", ""+authPreference.getrestaurant_id());
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("anuj",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    private void showCustomDialog (String s)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_openandClose.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Activity_openandClose.this,MainActivity.class);
                startActivity(intent);
                finish();
//                        Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void showCustomDialog1 (String s)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_openandClose.this).create();
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
