package com.justfoodzorderreceivers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.justfoodzorderreceivers.Fragment.About_us;
import com.justfoodzorderreceivers.Fragment.Faq;
import com.justfoodzorderreceivers.Fragment.Home;
import com.justfoodzorderreceivers.Fragment.Privacy;
import com.justfoodzorderreceivers.Fragment.Terms;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Home home=new Home();
    About_us about = new About_us();
    Faq faq = new Faq();
    Terms terms = new Terms();
    public static boolean isNet=true;  
    Privacy privacy = new Privacy();
    public static  String webUrl;
    SharedPreferences sharedPreferences;
    TextView home_title;
    TextView Fullname,Phoneno,on_text_delivery,on_text_pickup, text_mode;
    ImageView sideimage,switch_button_delivery,switch_button_pickup, switch_mode;
    private Timer autoUpdate;
    public static MediaPlayer player,player1;
    ImageView lang_button;
      MyPref myPref;
    ParseLanguage parseLanguage;
    ProgressDialog progressDialog;

    TextView current_time;
    RequestQueue requestQueue;
    ImageView refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);


        myPref = new MyPref(MainActivity.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),MainActivity.this);
        requestQueue = Volley.newRequestQueue(this);
        getRestroinformation();
//        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        player.setLooping(true);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        LinearLayout linear_header = (LinearLayout)findViewById(R.id.linear_header);
        final RelativeLayout sidebar_rl = (RelativeLayout)findViewById(R.id.sidebar_rl);
        RelativeLayout music_rl = (RelativeLayout)findViewById(R.id.music_rl);
        Button notification_rl = (Button) findViewById(R.id.notification_rl);



        isNet=isNetworkConnected();

        home_title = findViewById(R.id.home_title);
        refresh = findViewById(R.id.refresh);

        sidebar_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               drawer.openDrawer(GravityCompat.START);
            }
        });

        music_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying() == true){
                    player.pause();
                }else{
                  //  player.start();
                }
            }
        });

        notification_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(ii);
                finish();
            }
        });

        current_time= findViewById(R.id.current_time);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        final Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_allorders).setTitle(parseLanguage.getParseString("All_Order"));
       // menu.findItem(R.id.nav_setDiscount).setTitle(parseLanguage.getParseString("Discount"));
        menu.findItem(R.id.nav_odersupport).setTitle(parseLanguage.getParseString("Order_Complaints"));
        menu.findItem(R.id.nav_history).setTitle(parseLanguage.getParseString("Order_History"));
        menu.findItem(R.id.nav_openclose).setTitle(parseLanguage.getParseString("Open_CloseText")).setVisible(false);

        menu.findItem(R.id.nav_chat).setTitle(parseLanguage.getParseString("Help_Chat"));
       // menu.findItem(R.id.nav_faqs).setTitle(parseLanguage.getParseString("FAQs"));
        menu.findItem(R.id.nav_logout).setTitle(parseLanguage.getParseString("Logout"));
        String update_string=parseLanguage.getParseString("Update_Ringtone");
        MenuItem menuItem=menu.findItem(R.id.nav_update_ringtone);
        if(update_string.equalsIgnoreCase("No Response")){
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                menuItem.setTitle(getString(R.string.update_ringtone));
            }
            else {
                menuItem.setTitle("Update Ringtone");
            }
        }
        else {
            menuItem.setTitle(update_string);
        }
//        menu.findItem(R.id.nav_update_ringtone).setTitle(parseLanguage.getParseString("Update_Ringtone"));
     //   home_title.setText(parseLanguage.getParseString("StopText"));


        Fullname = (TextView) headerView.findViewById(R.id.name);
        Phoneno = (TextView) headerView.findViewById(R.id.number);
        sideimage = (ImageView) headerView.findViewById(R.id.imageView);

        lang_button =findViewById(R.id.change_lang);

        if(myPref.getCustomer_default_langauge().equalsIgnoreCase("en")){
            lang_button.setImageResource(R.drawable.icon_english);
        }
        else {
            lang_button.setImageResource(R.drawable.german_icon);
        }
        switch_button_pickup = headerView.findViewById(R.id.switch_button_pickup);
        switch_button_delivery = headerView.findViewById(R.id.switch_button_delivery);
        on_text_delivery = headerView.findViewById(R.id.on_text_delivery);
        on_text_pickup = headerView.findViewById(R.id.on_text_pickup);
        switch_mode=headerView.findViewById(R.id.switch_mode);
        text_mode=headerView.findViewById(R.id.text_mode);
        on_text_delivery.setText(parseLanguage.getParseString("HomeDelivery"));
        on_text_pickup.setText(parseLanguage.getParseString("Pickup"));
        String mode_text=parseLanguage.getParseString("Auto_self_mode");
        if(mode_text.equalsIgnoreCase("No Response")){
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                text_mode.setText(getString(R.string.auto_self));
            }
            else {
                text_mode.setText("Auto/Self Mode");
            }
        }
        else {
            text_mode.setText(mode_text);
        }


        if(myPref.getIsOnline().equalsIgnoreCase("")){
            switch_button_delivery.setImageResource(R.drawable.on);
        }
        if(myPref.getIsOnline().equalsIgnoreCase("1")) {

            switch_button_delivery.setImageResource(R.drawable.on);

        }

        if(myPref.getIsOnline().equalsIgnoreCase("0")) {

            switch_button_delivery.setImageResource(R.drawable.off);

        }
        if(myPref.getMode().equalsIgnoreCase("")){
            switch_mode.setImageResource(R.drawable.on);
        }
        if(myPref.getMode().equalsIgnoreCase("1")) {

            switch_mode.setImageResource(R.drawable.on);

        }

        if(myPref.getMode().equalsIgnoreCase("0")) {

            switch_mode.setImageResource(R.drawable.off);

        }


        if(myPref.getIsRideAccepted().equalsIgnoreCase("")){
            switch_button_pickup.setImageResource(R.drawable.on);
        }
        if(myPref.getIsRideAccepted().equalsIgnoreCase("1")) {

            switch_button_pickup.setImageResource(R.drawable.on);

        }

        if(myPref.getIsRideAccepted().equalsIgnoreCase("0")) {

            switch_button_pickup.setImageResource(R.drawable.off);

        }
        switch_button_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(myPref.getIsOnline().equalsIgnoreCase("")){

                    getDeliveryON("1",sharedPreferences.getString("restaurant_id",""));
                }
                if(myPref.getIsOnline().equalsIgnoreCase("1")) {

                    getDeliveryON("0",sharedPreferences.getString("restaurant_id",""));

                }

                if(myPref.getIsOnline().equalsIgnoreCase("0")) {

                    getDeliveryON("1",sharedPreferences.getString("restaurant_id",""));

                }


            }
        });

        switch_button_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(myPref.getIsRideAccepted().equalsIgnoreCase("")){
                    getPickup("1",sharedPreferences.getString("restaurant_id",""));
                }
                if(myPref.getIsRideAccepted().equalsIgnoreCase("1")) {

                    getPickup("0",sharedPreferences.getString("restaurant_id",""));

                }

                if(myPref.getIsRideAccepted().equalsIgnoreCase("0")) {

                    getPickup("1",sharedPreferences.getString("restaurant_id",""));

                }
            }
        });
        switch_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myPref.getMode().equalsIgnoreCase("")){

                    getMode("1",sharedPreferences.getString("restaurant_id",""));
                }
                if(myPref.getMode().equalsIgnoreCase("1")) {

                    getMode("0",sharedPreferences.getString("restaurant_id",""));

                }

                if(myPref.getMode().equalsIgnoreCase("0")) {

                    getMode("1",sharedPreferences.getString("restaurant_id",""));

                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });





        lang_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AppCompatDialog dialog = new AppCompatDialog(MainActivity.this);
                dialog.setContentView(R.layout.lang_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ImageView back = dialog.findViewById(R.id.back);
                TextView change_lang_text = dialog.findViewById(R.id.change_lang_text);
                TextView select_lang_text = dialog.findViewById(R.id.select_lang_text);
                TextView english = dialog.findViewById(R.id.english);
                TextView german = dialog.findViewById(R.id.german);
                final RelativeLayout german_layout = dialog.findViewById(R.id.german_layout);
                final RelativeLayout eng_layout = dialog.findViewById(R.id.eng_layout);

                change_lang_text.setText(parseLanguage.getParseString("Select_Language"));
                select_lang_text.setText(parseLanguage.getParseString("Select_Language"));
             //   german.setText(parseLanguage.getParseString("german"));
              //  english.setText(parseLanguage.getParseString("english"));

                switch (myPref.getCustomer_default_langauge()) {
                    case "en":
                    {
                        eng_layout.setBackgroundResource(R.drawable.side_nav_bar);
                        german_layout.setBackgroundResource(R.drawable.border);
                    }
                        break;
                    case "de":
                    {
                        german_layout.setBackgroundResource(R.drawable.side_nav_bar);
                        eng_layout.setBackgroundResource(R.drawable.border);
                    }break;
                    default:
                    {
                        eng_layout.setBackgroundResource(R.drawable.side_nav_bar);
                        german_layout.setBackgroundResource(R.drawable.border);
                    }
                }



                german_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eng_layout.setBackgroundResource(R.drawable.border);
                        german_layout.setBackgroundResource(R.drawable.side_nav_bar);
                        getParseLanuage("de",dialog);
                        myPref.setCustomer_default_langauge("de");
                        lang_button.setImageResource(R.drawable.german_icon);
                    }
                });

                eng_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eng_layout.setBackgroundResource(R.drawable.side_nav_bar);
                        german_layout.setBackgroundResource(R.drawable.border);
                        getParseLanuage("en",dialog);
                        myPref.setCustomer_default_langauge("en");
                        lang_button.setImageResource(R.drawable.icon_english);
                    }
                });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });




            }
        });
        Home.toopen="M";
        Home.whatopen="1";
        home_title.setText(sharedPreferences.getString("Restaurant_name",""));
        Fullname.setText(sharedPreferences.getString("Restaurant_name",""));
        Phoneno.setText(sharedPreferences.getString("Restaurant_address",""));
        Picasso.get().load(sharedPreferences.getString("Restaurant_logo","")).into(sideimage);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ll,home);
        fragmentTransaction.commit();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        current_time.setText(strDate);
        blink();



        final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
      // player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //player.start();

    }
    public void getRestroinformation(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url.restroinformation, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("response", s);
                try {
                    JSONObject json=new JSONObject(s);
                    if(json.has("RingTone")){
                        String pickup=json.optString("Pickup");
                        String home_delivery=json.optString("HomeDelivery");
                        int OrderAcceptanceEnable=json.optInt("OrderAcceptanceEnable");
                        if(OrderAcceptanceEnable==0){
                            switch_mode.setImageResource(R.drawable.off);
                        }
                        else if(OrderAcceptanceEnable==1){
                            switch_mode.setImageResource(R.drawable.on);
                        }
                        if(home_delivery.equalsIgnoreCase("0")){
                            switch_button_delivery.setImageResource(R.drawable.off);
                        }
                        else if(home_delivery.equalsIgnoreCase("1")){
                            switch_button_delivery.setImageResource(R.drawable.on);
                        }
                        if(pickup.equalsIgnoreCase("0")){
                            switch_button_pickup.setImageResource(R.drawable.off);
                        }
                        else if(pickup.equalsIgnoreCase("1")){
                            switch_button_pickup.setImageResource(R.drawable.on);
                        }

                        String ringtone_url=json.optString("RingTone");
                        player = MediaPlayer.create(MainActivity.this, Uri.parse(ringtone_url));
                        player1 = MediaPlayer.create(MainActivity.this, Uri.parse(ringtone_url));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("restaurant_id", sharedPreferences.getString("restaurant_id",""));
                params.put("lang_code", myPref.getCustomer_default_langauge());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


    @Override
    protected void onResume() {
        super.onResume();
        isNet=isNetworkConnected();
    }

    private void blink() {
        final Handler hander = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(550);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        if(current_time.getVisibility() == View.VISIBLE) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                            String strDate = mdformat.format(calendar.getTime());
                            current_time.setText(strDate);
                            isNet=isNetworkConnected();
                            current_time.setVisibility(View.GONE);
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                            String strDate = mdformat.format(calendar.getTime());
                            current_time.setText(strDate);
                            isNet=isNetworkConnected();
                            current_time.setVisibility(View.GONE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void getParseLanuage(final String lang, final AppCompatDialog dialog)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url.languagefile+"?lang_code="+lang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("searchdata",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    myPref.setCustomer_default_langauge(lang);

                    dialog.cancel();
                    dialog.dismiss();
                    myPref.setBookingData(response);
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);

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


    public void getDeliveryON(final String HomeDelivery,final String restaurant_id)
    {
        progressDialog = progressDialog.show(MainActivity.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.homedelivery, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("searchdata",""+response);
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    myPref.setIsOnline(HomeDelivery);
                    if(HomeDelivery.equalsIgnoreCase("1")){
                        switch_button_delivery.setImageResource(R.drawable.off);
                    }else {
                        switch_button_delivery.setImageResource(R.drawable.on);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    progressDialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                progressDialog.cancel();

            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> param = new HashMap<>();
                param.put("restaurant_id",""+sharedPreferences.getString("restaurant_id",""));
                param.put("HomeDelivery", HomeDelivery);
                Log.e("param",""+param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getMode(final String accepatance_enabled, final String resturant_id){
        progressDialog = progressDialog.show(MainActivity.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url.order_acceptance_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if(jsonObject.has("OrderAcceptanceEnable")){
                        String OrderAcceptanceEnable=jsonObject.optString("OrderAcceptanceEnable");
                        myPref.setMode(OrderAcceptanceEnable);
                        if(OrderAcceptanceEnable.equalsIgnoreCase("1")){
                            switch_mode.setImageResource(R.drawable.on);
                        }else {
                            switch_mode.setImageResource(R.drawable.off);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("OrderAcceptanceEnable",String.valueOf(accepatance_enabled ));
                params.put("lang_code", myPref.getCustomer_default_langauge());
                params.put("restaurant_id",resturant_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void getPickup(final String Pickup,final String restaurant_id)
    {
        progressDialog = progressDialog.show(MainActivity.this,"",parseLanguage.getParseString("Please_wait"),false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.pickup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("searchdata",""+response);
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    myPref.setIsRideAccepted(Pickup);
                    if(Pickup.equalsIgnoreCase("1")){
                        switch_button_pickup.setImageResource(R.drawable.off);
                    }else {

                        switch_button_pickup.setImageResource(R.drawable.on);
                    }


                } catch (JSONException e) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                progressDialog.cancel();

            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> param = new HashMap<>();
                param.put("restaurant_id",""+sharedPreferences.getString("restaurant_id",""));
                param.put("Pickup", Pickup);
                Log.e("param",""+param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_allorders) {
            Home.toopen="A";
            if (Home.whatopen.equals("1")) {
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.detach(home);
                fragmentTransaction.attach(home);
                fragmentTransaction.commit();
            } else {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ll,home);
                fragmentTransaction.commit();
            }

        }/* else if (id == R.id.nav_tablebooking) {
            Home.toopen="T";
            if (Home.whatopen.equals("1")) {
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.detach(home);
                fragmentTransaction.attach(home);
                fragmentTransaction.commit();
            } else {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ll,home);
                fragmentTransaction.commit();
            }

        }*/ else if (id == R.id.nav_odersupport) {
            Intent i = new Intent(MainActivity.this,Activity_OrderSupport.class);
            startActivity(i);

        }
        else if (id == R.id.nav_history) {
            Intent i = new Intent(MainActivity.this,OrderHistory.class);
            startActivity(i);

        }

//        else if (id == R.id.nav_setDiscount) {
//            Intent fuck = new Intent(MainActivity.this,DiscountActivity.class);
//            startActivity(fuck);
//            finish();
//
//        }
        else if (id == R.id.nav_chat) {
            Intent i = new Intent(MainActivity.this,Activity_HelpChat.class);
            startActivity(i);

        } else if (id == R.id.nav_openclose) {
            Intent i = new Intent(MainActivity.this, Activity_openandClose.class);
            startActivity(i);

        }
        else if (id == R.id.nav_logout) {
ShowlogoutDialog();

        }
        else if(id==R.id.nav_update_ringtone){
            Intent i = new Intent(MainActivity.this, UpdateRingtone.class);
            startActivity(i);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void ShowlogoutDialog(){
        String message="";
        String okay="";
        String cancel ="";
        if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
            message=getString(R.string.are_you_sure_logout);
            okay=getString(R.string.okay);
            cancel=getString(R.string.cancel);
        }
        else {
            message="Are you sure you want to logout?";
            okay ="Ok";
            cancel="Cancel";
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this).setMessage(message).setPositiveButton(okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                i.putExtra("lang",myPref.getCustomer_default_langauge());
                SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putString("restaurant_id","");
                editor.commit();
                myPref.logOut();
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            }
        }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


//    else if (id == R.id.nav_faqs) {
//        Intent fuck = new Intent(MainActivity.this,FaqActivity.class);
//        startActivity(fuck);
//        finish();
//
//    }


//    else if (id == R.id.nav_add_driver) {
//        Home.stopTask();
//        Intent i = new Intent(MainActivity.this,Adddriver.class);
//        i.putExtra("todo","1");
//        startActivity(i);
//    }else if (id == R.id.nav_manage_driver) {
//        Home.stopTask();
//        Intent i = new Intent(MainActivity.this,ManageDriver.class);
//        startActivity(i);
//    }

//    else if (id == R.id.nav_wallet) {
//        Intent i = new Intent(MainActivity.this,Wallet.class);
//        startActivity(i);
//        finish();
//    }

//
//    else if (id == R.id.nav_aboutus) {
//        Intent about = new Intent(MainActivity.this,AboutusActivity.class);
//        startActivity(about);
//        finish();
//
//    } else if (id == R.id.nav_privacy) {
//        Intent p = new Intent(MainActivity.this,PrivacyActivity.class);
//        startActivity(p);
//        finish();
//
//    } else if (id == R.id.nav_terms) {
//        Intent t = new Intent(MainActivity.this,TermsActivity.class);
//        startActivity(t);
//        finish();
//
//    }
}