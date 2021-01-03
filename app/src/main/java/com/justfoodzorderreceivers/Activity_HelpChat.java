package com.justfoodzorderreceivers;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class Activity_HelpChat extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextView tv_text,tv_text2,tv_text3;
    RequestQueue requestQueue;
    ImageView back;
    TextView tv_whatsappshare,tv_rates,tv_email,tv_terms,tv_privacy,title;
    MyPref myPref;
    ParseLanguage parseLanguage;
    LinearLayout email_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__help_chat);

        requestQueue = Volley.newRequestQueue(this);
        myPref = new MyPref(Activity_HelpChat.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),Activity_HelpChat.this);
        tv_text = (TextView)findViewById(R.id.tv_text);
        tv_text2 = (TextView)findViewById(R.id.tv_text2);
        tv_text3 = (TextView)findViewById(R.id.tv_text3);
        title = findViewById(R.id.title);
        title.setText(parseLanguage.getParseString("Help_Chat"));
        tv_whatsappshare = (TextView)findViewById(R.id.tv_whatsappshare);
        tv_rates = (TextView)findViewById(R.id.tv_rates);
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_terms = (TextView)findViewById(R.id.tv_terms);

        tv_privacy = (TextView)findViewById(R.id.tv_privacy);
        email_ll = findViewById(R.id.email_ll);
        Log.i("lie", myPref.getBookingData());
   String terms= parseLanguage.getParseString("Terms_of_Use");
   String privacy=parseLanguage.getParseString("Privacy_Policy");
   if(terms.equalsIgnoreCase("No Response")){
       if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
           tv_terms.setText(getString(R.string.terms_use));
       }
   }
   else {
       tv_terms.setText(terms);
   }
   if(privacy.equalsIgnoreCase("No Response")){
       if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
           tv_privacy.setText(getString(R.string.privacy_policy));
       }
   }
   else {
       tv_privacy.setText(privacy);
   }
        tv_text.setText(parseLanguage.getParseString("Happy_to_hear_you_thoughts_assist_you_Add"));


        tv_whatsappshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this food wagon app!..");
                startActivity(shareIntent);
            }
        });

        tv_rates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                startActivity(shareIntent);
            }
        });

        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_HelpChat.this,TermsActivity.class);
                startActivity(i);
                finish();
            }
        });

        tv_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_HelpChat.this,PrivacyActivity.class);
                startActivity(i);
                finish();
            }
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_HelpChat.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        Helpandchat();
    }


    public void Helpandchat()
    {
        progressDialog = progressDialog.show(Activity_HelpChat.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.help_chat, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int success = jsonObject.getInt("success");
                    if (success == 1)
                    {
                        String PhoneNumber = jsonObject.getString("PhoneNumber");
                        String MobileNumber = jsonObject.getString("MobileNumber");
                        String WhatsappNumber = jsonObject.getString("WhatsappNumber");
                        String WebsiteName = jsonObject.getString("WebsiteName");
                        String WebsiteHeadOffice = jsonObject.getString("WebsiteHeadOffice");
                        String WebsiteEmail = jsonObject.getString("WebsiteEmail");
                        String WebsiteSmallDiscription = jsonObject.getString("WebsiteSmallDiscription");
                        Log.i("des", WebsiteSmallDiscription);
                        tv_text3.setText(WhatsappNumber+" "+parseLanguage.getParseString("to_your_contact_to_whatsapp_us"));
                        if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                            tv_text2.setText(getString(R.string.make_difference));
                        }
                        else {
                            tv_text2.setText(WebsiteSmallDiscription);
                        }
                        tv_whatsappshare.setText(parseLanguage.getParseString("Whatsapp")+" "+WebsiteName+" "+parseLanguage.getParseString("Team"));
                        tv_email.setText(parseLanguage.getParseString("Email_Team")+" "+WebsiteName+" "+parseLanguage.getParseString("Team"));
                        tv_rates.setText(parseLanguage.getParseString("Rate")+" "+WebsiteName+" "+parseLanguage.getParseString("on_playstore")+" ");


                    }
                    else {
//                        String success_msg = jsonObject.getString("error_msg");
//                        Toast.makeText(Activity_HelpChat.this, success_msg, Toast.LENGTH_SHORT).show();
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
              //  Toast.makeText(Activity_HelpChat.this, "Please Check your network connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lang_code", myPref.getCustomer_default_langauge());

//                params.put("orderid", ""+getIntent().getStringExtra("orderid"));
//                params.put("OrderStatus", "2");
//                params.put("DriverComment", a);
//                Log.e("pa",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
