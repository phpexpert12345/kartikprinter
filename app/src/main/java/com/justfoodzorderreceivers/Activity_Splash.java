package com.justfoodzorderreceivers;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;


import android.content.SharedPreferences;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Activity_Splash extends AppCompatActivity  {

    SharedPreferences sp;
    String s;
    double latitude, longitude;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    LocationManager locationManager;
    Location location;
    String lat = "", lng = "";
    RequestQueue requestQueue;
    private final int SPLASH_TIME_OUT = 1000;

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= 23 && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    final int REQUEST = 112;
    public static String currency_symbol="$", currency;

    LinearLayout llgps;
    TextView txtgps, txtfetch;
    ImageView imgnoapp;

    MyPref myPref;


    @Override
    protected void onResume() {
        super.onResume();
        displayFirebaseRegId();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__splash);
        requestQueue = Volley.newRequestQueue(this);
        txtgps = (TextView) findViewById(R.id.txtgps);
        myPref = new MyPref(getApplicationContext());
        txtfetch = (TextView) findViewById(R.id.txtfetch);
        displayFirebaseRegId();
        llgps = (LinearLayout) findViewById(R.id.llgps);
        imgnoapp = (ImageView) findViewById(R.id.imgnoapp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getData();
            }
        }, 3000);

        //getParseLanuage("en");

    }

    /*public void act() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }*/

//    @Override
//    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    toStart();
//                    //Do here
//                } else {
//                    //Toast.makeText(this, "The app was not allowed to write to your storage.", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

  /*  @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
    }*/

    public void toStart() {
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                          boolean gps_enabled = false;
                                          boolean network_enabled = false;
                                          try {
                                              gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                          } catch (Exception ex) {
                                          }

                                          try {
                                              network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                                          } catch (Exception ex) {
                                          }

                                          if (!gps_enabled && !network_enabled) {
                                              // notify user
                                              showDilog();
                                          } else {
                                              llgps.setVisibility(View.GONE);

//                                              fn_getlocation();
                                          }
                                      }
                                  }, SPLASH_TIME_OUT
        );

    }

   /* private void fn_getlocation() {
        txtfetch.setVisibility(View.GONE);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {

            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, (LocationListener) this);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        lat = String.valueOf(latitude);
                        lng = String.valueOf(longitude);
//                         Toast.makeText(this, "networl"+lat+lng, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            if (isGPSEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
//                        lat = String.valueOf(latitude);
//                        lng = String.valueOf(longitude);
                        lat = (String.format("%.6f", latitude));
                        lng = (String.format("%.6f", longitude));
//                          Toast.makeText(this, "gps"+lat+lng, Toast.LENGTH_SHORT).show();

                    }
                }
            }

        }

        if (lat.equals("") || lat.equals(null)) {
            fn_getlocation();
        } else {
            getData();
        }
    }*/

    public void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.open_location, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("searchdata", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("0")) {
                        currency = jsonObject.getString("customer_currency");

                        myPref.setCustomer_default_langauge(jsonObject.getString("customer_default_langauge"));

                        Log.i("default lang",jsonObject.getString("customer_default_langauge"));
//                        getParseLanuage("en");
                        getParseLanuage(jsonObject.getString("customer_default_langauge"));
//                        getParseLanuage(jsonObject.getString("customer_default_langauge"));

                        if (!jsonObject.getString("customer_currency").equalsIgnoreCase("")) {
                            currency_symbol = Utils.getCurrencySymbol(jsonObject.getString("customer_currency"));

                        } else {
                            currency_symbol = "$";
                        }
                        sp = getSharedPreferences("Order", MODE_PRIVATE);
                        s = sp.getString("restaurant_id", "");
                        if (s.equals("")) {

                            Intent i = new Intent(Activity_Splash.this, LoginActivity.class);
                            i.putExtra("lang", jsonObject.getString("customer_default_langauge"));
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(Activity_Splash.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        imgnoapp.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }



        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lang_code", myPref.getCustomer_default_langauge());
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
                try {                    JSONObject jsonObject = new JSONObject(response);



                    myPref.setBookingData(response);


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


    static class Utils {
        public static SortedMap<Currency, Locale> currencyLocaleMap;

        static {
            currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
                public int compare(Currency c1, Currency c2) {
                    return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
                }
            });
            for (Locale locale : Locale.getAvailableLocales()) {
                try {
                    Currency currency = Currency.getInstance(locale);
                    currencyLocaleMap.put(currency, locale);
                } catch (Exception e) {
                }
            }
        }


        public static String getCurrencySymbol(String currencyCode) {
            Currency currency = Currency.getInstance(currencyCode);
            System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
            return currency.getSymbol(currencyLocaleMap.get(currency));
        }

    }


    private void showDilog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Activity_Splash.this);
        dialog.setMessage("Gps network not enabled please enable to continue");
        dialog.setPositiveButton("Open_location_settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                llgps.setVisibility(View.VISIBLE);
            }
        });
        dialog.show();
    }

    private void displayFirebaseRegId() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Activity_Splash.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                myPref.setFirebaseTokenId(newToken);

            }
        });

    }
}
