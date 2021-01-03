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

public class ReasonActivity extends AppCompatActivity {

    Button replysubmit;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    EditText edt_reply;
    LinearLayout liner_back;
    String complaint_id, orderIDNumber, contact_name, contact_email, resid;
    MyPref myPref;
    TextView title, tvrply;
    ParseLanguage parseLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reason);

        requestQueue = Volley.newRequestQueue(this);
        liner_back = findViewById(R.id.liner_back);
        edt_reply = findViewById(R.id.edt_reply);
        title = findViewById(R.id.title);
        tvrply = findViewById(R.id.tvrply);


        myPref = new MyPref(ReasonActivity.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(), ReasonActivity.this);

        title.setText(parseLanguage.getParseString("Reply"));
        tvrply.setText(parseLanguage.getParseString("Your_Reply"));

        liner_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReasonActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        replysubmit = (Button) findViewById(R.id.replysubmit);
        replysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_reply.getText().toString().equals("")) {
                    edt_reply.setError("Please enter your Reason");
                    edt_reply.requestFocus();
                } else {

                    replysubmit();
                }
            }
        });
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            complaint_id = intent.getStringExtra("complaint_id");
            orderIDNumber = intent.getStringExtra("orderIDNumber");
            contact_name = intent.getStringExtra("contact_name");
            contact_email = intent.getStringExtra("contact_email");
            resid = intent.getStringExtra("resid");
            Log.e("intent", "gfet" + complaint_id + "" + orderIDNumber);
        }

        replysubmit.setText(parseLanguage.getParseString("Reply"));

    }


    public void replysubmit() {
        progressDialog = progressDialog.show(ReasonActivity.this, "", parseLanguage.getParseString("Please_wait  "), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.ordersupport_reply, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    int error = jsonObject.getInt("error");
                    if (error == 0) {
                        String error_msg = jsonObject.getString("error_msg");
                        showCustomDialog(error_msg);
                    } else {
                        String error_msg = jsonObject.getString("error_msg");

                        showCustomDialog1(error_msg);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Log.e("error", "" + volleyError);
                String message="";
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    message=getString(R.string.check_internet);
                }
                else {
                    message="Please Check your network connection";
                }
                Toast.makeText(ReasonActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("complaint_id", complaint_id);
                params.put("ReviewReply_content", "" + edt_reply.getText().toString());
                params.put("orderIDNumber", orderIDNumber);
                params.put("contact_name", contact_name);
                params.put("contact_email", contact_email);
                params.put("resid", resid);
                params.put("lang_code", myPref.getCustomer_default_langauge());


                Log.e("anuj", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showCustomDialog(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(ReasonActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ReasonActivity.this, Activity_OrderSupport.class);

                startActivity(i);
                finish();
//                        Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void showCustomDialog1(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(ReasonActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.cancel);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

//                Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

}
