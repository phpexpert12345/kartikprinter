package com.justfoodzorderreceivers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Scanner extends AppCompatActivity implements View.OnClickListener {

   ImageView buttonScan,back;
    private TextView txtsubmit, txtname;
    private IntentIntegrator qrScan;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    EditText edt_amount,edt_pin;
    String customer_wallet_number;
    SharedPreferences sharedPreferences;
    MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        buttonScan =  findViewById(R.id.buttonScan);
        myPref = new MyPref(Scanner.this);
        back =  findViewById(R.id.back);
        txtsubmit = findViewById(R.id.txtsubmit);
        txtname =  findViewById(R.id.txtname);
        edt_amount =  findViewById(R.id.edt_amount);
        edt_pin =  findViewById(R.id.edt_pin);
        buttonScan.setOnClickListener(this);
        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);


        qrScan.initiateScan();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_amount.getText().toString().equals("")) {
                    edt_amount.setError("Enter Amount");
                    edt_amount.requestFocus();
                } else if (edt_pin.getText().toString().equals("")) {
                    edt_pin.setError("Enter your pin number");
                    edt_pin.requestFocus();
                }else {
                    getSubmit();
                }
            }
        });
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                showAlert("Result Not Found", "1");
            } else {

                    String rr = result.getContents();
                    String[] separated = rr.split("_");
                    txtname.setText(""+separated[1]);
                    customer_wallet_number = separated[0];

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }

    public void getSubmit()
    {
        progressDialog = progressDialog.show(Scanner.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.money_from_customer, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("scanresponse",""+s);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int success = jsonObject.getInt("success");
                    String success_msg = jsonObject.getString("success_msg");
                    if (success==0) {
                        showAlert(success_msg,"1");
                    }else {
                        showAlert(success_msg,"2");
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
                Toast.makeText(Scanner.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", ""+sharedPreferences.getString("restaurant_id",""));
                params.put("customer_wallet_number", customer_wallet_number);
                params.put("transfer_wallet_money", ""+edt_amount.getText().toString());
                params.put("restaurant_wallet_pin", ""+edt_pin.getText().toString());
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("scanpa",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showAlert (String a, final String b)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(Scanner.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setMessage(a);
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                 if (b.equals("1")) {
                     Intent i = new Intent(Scanner.this,Wallet.class);
                     startActivity(i);
                     finish();
                 }else {
                     alertDialog.dismiss();
                 }
            }
        });
        alertDialog.show();
    }
}