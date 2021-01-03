package com.justfoodzorderreceivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class DiscountActivity extends AppCompatActivity {

    AppCompatSeekBar seekBar;
    TextView discount,commission,title,discount_text,commission_text;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences,sp1;
    Button loginbutton;
    ImageView back;
    MyPref myPref;
    ParseLanguage parseLanguage;
    void getIds(){
        seekBar = findViewById(R.id.discount_seekbar);
        discount = findViewById(R.id.discount);
        commission = findViewById(R.id.commission);
        loginbutton = findViewById(R.id.loginbutton);

        discount_text = findViewById(R.id.discount_text);
        commission_text = findViewById(R.id.commission_text);

        back = findViewById(R.id.back);
        seekBar.setMax(100);
        title = findViewById(R.id.title);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        myPref = new MyPref(DiscountActivity.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),DiscountActivity.this);
        getIds();
        getCurrentDiscount();
        installiation();
        title.setText(parseLanguage.getParseString("Set_Discount"));

        discount_text.setText(parseLanguage.getParseString("Discount")+" : ");
        commission_text.setText(parseLanguage.getParseString("Commission")+" : ");
        loginbutton.setText(parseLanguage.getParseString("Set_Discount"));
    }


    public void installiation(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
              //  Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
                discount.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
             //   Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
              //  Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
             //   setCurrentDiscount();
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentDiscount();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getCurrentDiscount()
    {
        progressDialog = progressDialog.show(DiscountActivity.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.restroinformation, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("ssss",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error = jsonObject.getString("error");
                    if (error.equals("1"))
                    {
                        discount.setText(jsonObject.getString("Discount_Amount"));
                        commission.setText(jsonObject.getString("warehouse_commission"));
                        seekBar.setProgress(Integer.parseInt(jsonObject.getString("Discount_Amount")));

                    }
                    else {
                        String error_msg = jsonObject.getString("error_msg");
                        Toast.makeText(DiscountActivity.this, error_msg, Toast.LENGTH_SHORT).show();
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
              //  Toast.makeText(DiscountActivity.this, "Please Check your network connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", sharedPreferences.getString("restaurant_id",""));
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("pa",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void setCurrentDiscount()
    {
        progressDialog = progressDialog.show(DiscountActivity.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.setDiscount, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("ssss",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error = jsonObject.getString("success");
                    if (error.equals("0"))
                    {
                        commission.setText(jsonObject.getString("warehouse_commission"));

                    }
                    else {
                        String error_msg = jsonObject.getString("success");
                        Toast.makeText(DiscountActivity.this, error_msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DiscountActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", sharedPreferences.getString("restaurant_id",""));
                params.put("Discount_Amount", discount.getText().toString());
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("pa",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
