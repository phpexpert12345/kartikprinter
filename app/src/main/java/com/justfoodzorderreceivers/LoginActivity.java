package com.justfoodzorderreceivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class LoginActivity extends Activity {

    Button verify;
    EditText edtemail, edtpassword;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences, sp1;
    ImageView hide, hide1;
    TextView tvsinj;
    AuthPreference authPreference;
    Button login, ss;
    public static String restroid = "";

    MyPref myPref;
    ParseLanguage parseLanguage;
    String lang = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        sp1 = getSharedPreferences("noti", MODE_PRIVATE);
        edtemail = (EditText) findViewById(R.id.edt_username);
        edtpassword = (EditText) findViewById(R.id.edt_password);
        tvsinj = findViewById(R.id.tvsinj);
        authPreference = new AuthPreference(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lang = bundle.getString("lang");
        }

        myPref = new MyPref(LoginActivity.this);


        hide = (ImageView) findViewById(R.id.hide);
        hide1 = (ImageView) findViewById(R.id.hide1);

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                hide.setVisibility(View.GONE);
                hide1.setVisibility(View.VISIBLE);
            }
        });

        hide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                hide.setVisibility(View.VISIBLE);
                hide1.setVisibility(View.GONE);
            }
        });

        login = (Button) findViewById(R.id.loginbutton);

//        ss = (Button) findViewById(R.id.ss);

//
//        if(BuildConfig.DEBUG){
//            edtemail.setText("lmangal@gmail.com");
//            edtpassword.setText("743246011");
//        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtemail.getText().toString().equals("")) {
                    edtemail.setError("Please enter Email id");
                    edtemail.requestFocus();
                } else if (edtpassword.getText().toString().equals("")) {
                    edtpassword.setError("Please enter Email id");
                    edtpassword.requestFocus();
                } else {

                    getLogin();
                }
            }
        });


//        ss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
//                // Setting Dialog Title
//                alertDialog.setTitle("Alert Dialog");
//
//                // Setting Dialog Message
//                alertDialog.setMessage("Welcome to AndroidHive.info");
//
//                // Setting Icon to Dialog
//                alertDialog.setIcon(R.drawable.tick);
//                alertDialog.setButton("OK",
//                        new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                             Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                // Showing Alert Message
//                alertDialog.show();
//            }
//        });
//


        getParseLanuage(lang);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void getLogin() {
        progressDialog = progressDialog.show(LoginActivity.this, "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("ssss", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error = jsonObject.getString("error");
                    if (error.equals("1")) {
                        //      String id = jsonObject.getString("id");
                        String customer_care_id = jsonObject.getString("customer_care_id");
                        String restaurant_id = jsonObject.getString("restaurant_id");
                        authPreference.setrestaurant_id(restaurant_id);
                        restroid = restaurant_id;
//                        String restoid = jsonObject.getString("restoid");
                        String warehouse_OwnerFirstName = jsonObject.getString("warehouse_OwnerFirstName");
                        String warehouse_OwnerLastName = jsonObject.getString("warehouse_OwnerLastName");
                        String BookaTableAvailable = jsonObject.getString("BookaTableAvailable");
//                        String warehouse_OwnerLoginMobile = jsonObject.getString("warehouse_OwnerLoginMobile");
//                        String warehouse_OwnerLoginState = jsonObject.getString("warehouse_OwnerLoginState");
//                        String warehouse_OwnerLoginCity = jsonObject.getString("warehouse_OwnerLoginCity");
//                        String warehouse_OwnerLoginAddress = jsonObject.getString("warehouse_OwnerLoginAddress");
                        String Restaurant_name = jsonObject.getString("Restaurant_name");
                        String Restaurant_address = jsonObject.getString("Restaurant_address");
//                        String Restaurant_phone = jsonObject.getString("Restaurant_phone");
                        String Restaurant_mobile = jsonObject.getString("Restaurant_mobile");
//                        String Restaurant_city = jsonObject.getString("Restaurant_city");
//                        String Restaurant_zipcode = jsonObject.getString("Restaurant_zipcode");
                        String Restaurant_logo = jsonObject.getString("Restaurant_logo");
                        String owner_wallet_number_pin = jsonObject.getString("owner_wallet_number_pin");
                        String owner_wallet_number = jsonObject.getString("owner_wallet_number");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("restaurant_id", restaurant_id);
                        editor.putString("customer_care_id", customer_care_id);
                        editor.putString("Restaurant_name", Restaurant_name);
                        editor.putString("Restaurant_logo", Restaurant_logo);
                        editor.putString("Restaurant_mobile", Restaurant_mobile);
                        editor.putString("warehouse_OwnerFirstName", warehouse_OwnerFirstName);
                        editor.putString("warehouse_OwnerLastName", warehouse_OwnerLastName);
                        editor.putString("Restaurant_address", Restaurant_address);
                        editor.putString("owner_wallet_number_pin", owner_wallet_number_pin);
                        myPref.setBookType(BookaTableAvailable);
                        editor.putString("owner_wallet_number", owner_wallet_number);
                        editor.commit();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        String error_msg = jsonObject.getString("error_msg");
                        Toast.makeText(LoginActivity.this, error_msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("CustomerCareEmailAddress", "" + edtemail.getText().toString());
                params.put("lang_code", myPref.getCustomer_default_langauge());
                params.put("CustomerCarePassword", "" + edtpassword.getText().toString());
                params.put("customer_care_device_id", myPref.getFirebaseTokenId());
                params.put("device_platform", "Android");
                Log.e("pa", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getParseLanuage(String lang) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url.languagefile+"?lang_code=" + lang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("searchdata", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    myPref.setBookingData(response);
                    parseLanguage = new ParseLanguage(myPref.getBookingData(), LoginActivity.this);

                    tvsinj.setText(parseLanguage.getParseString("Sign_In"));
                    edtemail.setHint(parseLanguage.getParseString("Enter_Email_id"));
                    edtpassword.setHint(parseLanguage.getParseString("Enter_Password"));
                    login.setText(parseLanguage.getParseString("Login"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}
