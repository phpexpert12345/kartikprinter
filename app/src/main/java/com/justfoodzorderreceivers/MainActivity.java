package com.justfoodzorderreceivers;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.justfoodzorderreceivers.Model.FoodItemList;
import com.justfoodzorderreceivers.Model.TimeModel;
import com.rt.printerlibrary.bean.SerialPortConfigBean;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.factory.connect.PIFactory;
import com.rt.printerlibrary.factory.connect.SerailPortFactory;
import com.rt.printerlibrary.factory.printer.PrinterFactory;
import com.rt.printerlibrary.factory.printer.ThermalPrinterFactory;
import com.rt.printerlibrary.observer.PrinterObserver;
import com.rt.printerlibrary.observer.PrinterObserverManager;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.TextSetting;
import com.rt.printerlibrary.utils.PrinterPowerUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PrinterObserver {

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
    int flag = 0;
    ParseLanguage parseLanguage;
    ProgressDialog progressDialog;

    TextView current_time;
    RequestQueue requestQueue;
    ImageView refresh;
    LatestOrder latestOrder;
    ArrayList<Model_Combo> model_combos;
    public static int orderidd;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    String Innerprinter_Address = "00:11:22:33:44:55";
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int printed=-1;
    String Currency = Activity_Splash.currency_symbol;
    ArrayList<FoodItemList> foodItemLists, drinkItemLists, meallist;
    public static ArrayList<String> item_size, item_name, item_price, item_quant, extra_toping,
            drink_item_name, drink_item_price, drink_item_quant, drink_extra_toping,
            meal_item_name, meal_item_price, meal_item_quant, meal_extra_toping, item_instruction, drink_instruction, meal_instruction;

    public static String restaurant_mobile_number, customer_email, status, PaymentMethod, name_customer, customer_phone, OrderType, customer_address, customer_instruction,
            number_of_items_order, subTotal, DeliveryCharge, PackageFees, FoodCosts, DiscountPrice, VatTax, CouponPrice, getFoodTaxTotal7, getFoodTaxTotal19,
            OrderPrice, PayByLoyality, GiftCardPay, ServiceFees, extraTipAddAmount, WalletPay, SalesTaxAmount,
            RequestAtDate, RequestAtTime, OrderAcceptedDate, OrderAcceptedTime, order_status_color_code,
            order_reference_number, collectionTime, Table_Booking_Number, DriverFirstName, DriverLastName,
            DriverMobileNo, DriverPhoto, rider_id, PayOptionStatus, restaurant_name, restaurant_address, TotalSavedDiscount,
            website_copy_right_text, instruction_note, order_confirmed_time, order_kitchen_time, order_delivery_time, number_of_customer_order, discountOfferFreeItems, CompanyName,customer_city,customer_postcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        myPref = new MyPref(MainActivity.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),MainActivity.this);
        requestQueue = Volley.newRequestQueue(this);
        //getRestroinformation();
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


//        if(myPref.getIsOnline().equalsIgnoreCase("")){
//            switch_button_delivery.setImageResource(R.drawable.on);
//        }
//        if(myPref.getIsOnline().equalsIgnoreCase("1")) {
//
//            switch_button_delivery.setImageResource(R.drawable.on);
//
//        }
//
//        if(myPref.getIsOnline().equalsIgnoreCase("0")) {
//
//            switch_button_delivery.setImageResource(R.drawable.off);
//
//        }
//        if(myPref.getMode().equalsIgnoreCase("")){
//            switch_mode.setImageResource(R.drawable.on);
//        }
//        if(myPref.getMode().equalsIgnoreCase("1")) {
//
//            switch_mode.setImageResource(R.drawable.on);
//
//        }
//
//        if(myPref.getMode().equalsIgnoreCase("0")) {
//
//            switch_mode.setImageResource(R.drawable.off);
//
//        }


//        if(myPref.getIsRideAccepted().equalsIgnoreCase("")){
//            switch_button_pickup.setImageResource(R.drawable.on);
//        }
//        if(myPref.getIsRideAccepted().equalsIgnoreCase("1")) {
//
//            switch_button_pickup.setImageResource(R.drawable.on);
//
//        }
//
//        if(myPref.getIsRideAccepted().equalsIgnoreCase("0")) {
//
//            switch_button_pickup.setImageResource(R.drawable.off);
//
//        }

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
        init();



        final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
      // player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //player.start()
                handleIntent(getIntent());;

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
                        String Language_Enable=json.optString("Language_Enable");
                        String auto_print_enable=json.optString("auto_print_enable");
                        myPref.setAuto_print_enable(auto_print_enable);

                        myPref.setIsRideAccepted(pickup);
                        myPref.setIsOnline(home_delivery);
                        myPref.setMode(OrderAcceptanceEnable+"");
                        if(Language_Enable.equalsIgnoreCase("Yes")){
                            lang_button.setVisibility(View.VISIBLE);
                        }
                        else{
                            lang_button.setVisibility(View.GONE);
                        }
                        if(OrderAcceptanceEnable==0){
                            switch_mode.setImageResource(R.drawable.off);
                        }
                        else if(OrderAcceptanceEnable==1){
                            switch_mode.setImageResource(R.drawable.on);
                        }
                        if(home_delivery.equalsIgnoreCase("0")){
                            switch_button_delivery.setImageResource(R.drawable.on);
                        }
                        else if(home_delivery.equalsIgnoreCase("1")){
                            switch_button_delivery.setImageResource(R.drawable.off);
                        }
                        if(pickup.equalsIgnoreCase("0")){
                            switch_button_pickup.setImageResource(R.drawable.on);
                        }
                        else if(pickup.equalsIgnoreCase("1")){
                            switch_button_pickup.setImageResource(R.drawable.off);
                        }

                        String ringtone_url=json.optString("RingTone");
                        player = MediaPlayer.create(MainActivity.this, Uri.parse(ringtone_url));
                        player1 = MediaPlayer.create(MainActivity.this, Uri.parse(ringtone_url));
                        myPref.setRingtone_url(ringtone_url);
//                        getorderdetails("92","1");
//
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
        getRestroinformation();
        IntentFilter intentFilter=new IntentFilter("newOrder");
        latestOrder = new LatestOrder();
        this.registerReceiver(latestOrder, intentFilter);
    }
    public void getorderdetails(final String orderID, final String error_msg) {
        item_name = new ArrayList<>();
        item_size=new ArrayList<>();
        item_instruction = new ArrayList<>();
        drink_instruction = new ArrayList<>();
        meal_instruction = new ArrayList<>();
        item_price = new ArrayList<>();
        item_quant = new ArrayList<>();
        extra_toping = new ArrayList<>();
        drink_item_name = new ArrayList<>();
        drink_item_price = new ArrayList<>();
        drink_item_quant = new ArrayList<>();
        drink_extra_toping = new ArrayList<>();
        meal_item_name = new ArrayList<>();
        meal_item_price = new ArrayList<>();
        meal_item_quant = new ArrayList<>();
        meal_extra_toping = new ArrayList<>();

        item_name.clear();
        item_size.clear();
        item_instruction.clear();
        drink_instruction.clear();
        meal_instruction.clear();
        item_price.clear();
        item_quant.clear();
        extra_toping.clear();
        drink_item_name.clear();
        drink_item_price.clear();
        drink_item_quant.clear();
        drink_extra_toping.clear();
        meal_item_name.clear();
        meal_item_price.clear();
        meal_item_quant.clear();
        meal_extra_toping.clear();
        foodItemLists = new ArrayList<>();
        drinkItemLists = new ArrayList<>();
        meallist = new ArrayList<>();

//        progressDialog = progressDialog.show(this, "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.order_booking_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                progressDialog.dismiss();
                Log.e("response", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("OrderDetailItem");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        status = jsonObject1.getString("status");
                        int orderid = jsonObject1.getInt("orderid");
                        orderidd = orderid;
                        PaymentMethod = jsonObject1.getString("PaymentMethod");
                        name_customer = jsonObject1.getString("name_customer");
                        customer_phone = jsonObject1.getString("customer_phone");
                        OrderType = jsonObject1.getString("OrderType");
                        customer_address = jsonObject1.getString("customer_address");
                        customer_instruction = jsonObject1.getString("customer_instruction");
                        number_of_items_order = jsonObject1.getString("number_of_items_order");
                        number_of_customer_order = jsonObject1.getString("number_of_customer_order");
                        restaurant_mobile_number = jsonObject1.getString("restaurant_mobile_number");
                        subTotal = jsonObject1.getString("subTotal");
                        DeliveryCharge = jsonObject1.getString("DeliveryCharge");
                        PackageFees = jsonObject1.getString("PackageFees");
                        FoodCosts = jsonObject1.getString("FoodCosts");
                        DiscountPrice = jsonObject1.getString("DiscountPrice");
                        VatTax = jsonObject1.getString("VatTax");
                        OrderPrice = jsonObject1.getString("OrderPrice");
                        PayByLoyality = jsonObject1.getString("PayByLoyality");
                        GiftCardPay = jsonObject1.getString("GiftCardPay");
                        ServiceFees = jsonObject1.getString("ServiceFees");
                        extraTipAddAmount = jsonObject1.getString("extraTipAddAmount");
                        WalletPay = jsonObject1.getString("WalletPay");
                        SalesTaxAmount = jsonObject1.getString("SalesTaxAmount");
                        RequestAtDate = jsonObject1.getString("RequestAtDate");
                        RequestAtTime = jsonObject1.getString("RequestAtTime");
                        OrderAcceptedDate = jsonObject1.getString("OrderAcceptedDate");
                        OrderAcceptedTime = jsonObject1.getString("OrderAcceptedTime");
                        order_status_color_code = jsonObject1.getString("order_status_color_code");
                        order_reference_number = jsonObject1.getString("order_reference_number");
                        collectionTime = jsonObject1.getString("collectionTime");
                        Table_Booking_Number = jsonObject1.getString("Table_Booking_Number");
                        DriverFirstName = jsonObject1.getString("DriverFirstName");
                        DriverLastName = jsonObject1.getString("DriverLastName");
                        DriverMobileNo = jsonObject1.getString("DriverMobileNo");
                        DriverPhoto = jsonObject1.getString("DriverPhoto");
                        rider_id = jsonObject1.getString("rider_id");
                        TotalSavedDiscount = jsonObject1.getString("TotalSavedDiscount");
                        PayOptionStatus = jsonObject1.getString("PayOptionStatus");
                        restaurant_name = jsonObject1.getString("restaurant_name");
                        restaurant_address = jsonObject1.getString("restaurant_address");
                        website_copy_right_text = jsonObject1.getString("website_copy_right_text");
                        instruction_note = jsonObject1.getString("instruction_note");
                        CompanyName=jsonObject1.getString("CompanyName");
                        customer_city=jsonObject1.getString("customer_city");
                        customer_postcode=jsonObject1.getString("customer_postcode");

//                        rider_idAssign = rider_id;
                        discountOfferFreeItems = jsonObject1.getString("discountOfferFreeItems");
//                        rider_idAssign = rider_id;
                        customer_email = jsonObject1.getString("customer_email");

//                        authPreference.setDriverFirstName(DriverFirstName);
//                        authPreference.setDriverLastName(DriverLastName);
//                        authPreference.setDriverMobileNo(DriverMobileNo);
//                        authPreference.setDriverPhoto(DriverPhoto);
                        model_combos=new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject1.optJSONArray("OrderFoodItem");
                        if(jsonArray1!=null) {
                            if (jsonArray1.length() == 0) {
                                // tv_no_foodItems.setVisibility(View.GONE);
                                //recycler_fooditem.setVisibility(View.GONE);
                            } else {
                                //tv_no_foodItems.setText("Food Menu");
                                //tv_no_foodItems.setTextColor(getResources().getColor(R.color.green));

                                for (int ii = 0; ii < jsonArray1.length(); ii++) {
                                    JSONObject jsonObject12 = jsonArray1.getJSONObject(ii);
                                    Model_Combo model_combo = new Model_Combo();
                                    String ItemsName = jsonObject12.getString("ItemsName");
                                    String quantity = jsonObject12.getString("quantity");
                                    String menuprice = jsonObject12.getString("menuprice");
                                    String instructions = jsonObject12.getString("instructions");
                                    String item_sizea = jsonObject12.getString("item_size");
                                    String ExtraTopping = jsonObject12.getString("ExtraTopping");

                                    String Currencyy = jsonObject12.getString("Currency");


                                    item_size.add(item_sizea);
                                    item_name.add(ItemsName);
                                    item_price.add(menuprice);
                                    item_quant.add(quantity);
                                    item_instruction.add(instructions);
                                    extra_toping.add(ExtraTopping);
                                    foodItemLists.add(new FoodItemList(ItemsName, quantity, menuprice, item_sizea, ExtraTopping, Currency));
                                }

                                // Activity_Booking.FooditemListView fooditemListView = new Activity_Booking.FooditemListView(Activity_Booking.this, foodItemLists);
                            }
                        }

                        JSONArray jsonArray2 = jsonObject1.optJSONArray("OrderDrinkItem");
                        if(jsonArray2!=null) {
                            if (jsonArray2.length() == 0) {
                                //     tv_no_drinkItems.setVisibility(View.GONE);
                                //   recycler_drinktem.setVisibility(View.GONE);
                            } else {
                                // tv_no_drinkItems.setText("Drink Menu");
                                //tv_no_drinkItems.setTextColor(getResources().getColor(R.color.red));
                                for (int ii = 0; ii < jsonArray2.length(); ii++) {
                                    JSONObject jsonObject122 = jsonArray2.getJSONObject(ii);
                                    String ItemsName = jsonObject122.getString("ItemsName");
                                    String quantity = jsonObject122.getString("quantity");
                                    String menuprice = jsonObject122.getString("menuprice");
                                    String item_size = jsonObject122.getString("item_size");
                                    String instructions = jsonObject122.getString("instructions");
                                    String ExtraTopping = jsonObject122.getString("ExtraTopping");
                                    String Currencyyy = jsonObject122.getString("Currency");
                                    drink_item_name.add(ItemsName);
                                    drink_item_price.add(menuprice);
                                    drink_instruction.add(instructions);
                                    drink_item_quant.add(quantity);
                                    drink_extra_toping.add(ExtraTopping);
                                    drinkItemLists.add(new FoodItemList(ItemsName, quantity, menuprice, item_size, ExtraTopping, Currency));
                                }

                                //  Activity_Booking.DrinkitemListView drinkitemListView = new Activity_Booking.DrinkitemListView(getContext(), drinkItemLists);
                                //      linearLayoutManager1 = new LinearLayoutManager(Activity_Booking.this, LinearLayoutManager.VERTICAL, false);
                                //    recycler_drinktem.setLayoutManager(linearLayoutManager1);
                                //  recycler_drinktem.setAdapter(drinkitemListView);

                            }
                        }


                        JSONArray OrderMealItem = jsonObject1.optJSONArray("OrderMealItem");
                        if(OrderMealItem!=null) {


                            if (OrderMealItem.length() == 0) {
                                //   tv_meal.setVisibility(View.GONE);
                                // rcmeal.setVisibility(View.GONE);
                            } else {
                                //        tv_meal.setText("Meal Menu");
                                //      tv_meal.setTextColor(getResources().getColor(R.color.red));
                                model_combos = new ArrayList<>();
                                for (int ii = 0; ii < OrderMealItem.length(); ii++) {
                                    Model_Combo model_combo = new Model_Combo();
                                    JSONObject j2 = OrderMealItem.getJSONObject(ii);
                                    String ItemsName = j2.getString("ItemsName");
                                    String quantity = j2.getString("quantity");
                                    String menuprice = j2.getString("menuprice");
                                    String item_size = j2.optString("item_size");
                                    String instructions = j2.optString("instructions");
                                    String ExtraTopping = j2.optString("ExtraTopping");
                                    String Currencyyy = j2.getString("Currency");

                                    model_combo.setItemsName(ItemsName);
                                    model_combo.setQuantity(quantity);
                                    model_combo.setMenuprice(menuprice);
                                    model_combo.setCurrency(Currency);
                                    model_combo.setMenuprice(j2.getString("menuprice"));
                                    model_combo.setItemsDescriptionName(j2.getString("ItemsDescriptionName"));

                                    JSONArray orderComboItemOption = j2.getJSONArray("OrderComboItemOption");
                                    ArrayList<Model_OrderComboItemOption> model_orderComboItemOptions = new ArrayList<>();

                                    if (orderComboItemOption.length()>0) {
                                        for (int i1 = 0; i1 < orderComboItemOption.length(); i1++) {
                                            Model_OrderComboItemOption model_orderComboItemOption = new Model_OrderComboItemOption();
                                            JSONObject j21 = (JSONObject) orderComboItemOption.get(i1);
                                            String comboOptionName = j21.getString("ComboOptionName");
                                            if(comboOptionName.contains("null")){
                                                comboOptionName=comboOptionName.replace("null", "");
                                            }

                                            String comboOptionItemName = j21.getString("ComboOptionItemName");
                                            Log.i("name", comboOptionItemName);
                                            String comboOptionItemSizeName = j21.getString("ComboOptionItemSizeName");
                                            model_orderComboItemOption.setComboOptionItemName(comboOptionItemName);
                                            model_orderComboItemOption.setComboOptionItemSizeName(comboOptionItemSizeName);
                                            model_orderComboItemOption.setComboOptionName(comboOptionName);
                                            JSONArray orderComboItemOption1 = j21.getJSONArray("OrderComboItemExtra");
                                            ArrayList<Model_OrderComboItemExtra> model_orderComboItemExtras = new ArrayList<>();
                                            for (int i2 = 0; i2 < orderComboItemOption1.length(); i2++) {
                                                Model_OrderComboItemExtra model_orderComboItemExtra = new Model_OrderComboItemExtra();
                                                JSONObject jsonObject2 = orderComboItemOption1.getJSONObject(i2);
                                                String comboExtraItemName = jsonObject2.getString("ComboExtraItemName");
                                                String comboExtraItemQuantity = jsonObject2.getString("ComboExtraItemQuantity");
                                                String comboExtraItemPrice = jsonObject2.getString("ComboExtraItemPrice");
                                                model_orderComboItemExtra.setComboExtraItemName(comboExtraItemName);
                                                model_orderComboItemExtra.setComboExtraItemPrice(comboExtraItemPrice);
                                                model_orderComboItemExtra.setComboExtraItemQuantity(comboExtraItemQuantity);
                                                model_orderComboItemExtras.add(model_orderComboItemExtra);
                                            }
                                            model_orderComboItemOption.setOrderComboItemExtra(model_orderComboItemExtras);
                                            model_orderComboItemOptions.add(model_orderComboItemOption);
                                        }
//                                        Log.i("name", comboOptionName);


                                    }
                                    meal_item_name.add(ItemsName);
                                    meal_item_price.add(menuprice);
                                    meal_item_quant.add(quantity);
                                    meal_instruction.add(instructions);
                                    meal_extra_toping.add(ExtraTopping);
                                    model_combo.setOrderComboItemOption(model_orderComboItemOptions);
                                    model_combos.add(model_combo);
                                    meallist.add(new FoodItemList(ItemsName, quantity, menuprice, item_size, ExtraTopping, Currency));
                                }
                                //     Activity_Booking.MealListView mealListView = new Activity_Booking.MealListView(Activity_Booking.this, meallist);
                                //   linearLayoutManager2 = new LinearLayoutManager(Activity_Booking.this, LinearLayoutManager.VERTICAL, false);
                                //  rcmeal.setLayoutManager(linearLayoutManager2);
                                //rcmeal.setAdapter(mealListView);
                            }
                        }

                        if (Table_Booking_Number.equals("") || Table_Booking_Number.equals(null) || Table_Booking_Number.equals("null") || Table_Booking_Number.equals("NULL")) {
                            //    dyiningtale.setVisibility(View.GONE);
                        } else {

                            if (myPref.getCustomer_default_langauge().equals("en")) {
                                //dyiningtale.setText("Dining table :  " + Table_Booking_Number);
                            } else {
                                //dyiningtale.setText(parseLanguage.getParseString("Dining_table")+" :  " + Table_Booking_Number);

                            }

                            //dyiningtale.setVisibility(View.VISIBLE);
                        }

                        if (myPref.getCustomer_default_langauge().equals("en")) {
                            //  ordernumber.setText("" + "Order No" + " " + "#" + order_reference_number);
                            //tv_delivery.setText(parseLanguage.getParseString("Type")+" : " + OrderType);

                        } else {
                            //tv_delivery.setText(parseLanguage.getParseString("Type")+" : " + OrderType);

                            //   ordernumber.setText("" + parseLanguage.getParseString("Order_no") + " " + "#" + order_reference_number);
                        }


                        ////////////////////////////Food cost relative layout   /////////
                        if (FoodCosts.equals("") || FoodCosts.equals(null) || FoodCosts.equals("Null") || FoodCosts.equals("null") || FoodCosts.equals("0.00")) {
                            //   rl_foodcost.setVisibility(View.GONE);
                        } else {
                            // rl_foodcost.setVisibility(View.VISIBLE);
                            //food_costprice.setText(Currency + " " + FoodCosts);
                        }

                        if (DiscountPrice.equals("") || DiscountPrice.equals(null) || DiscountPrice.equals("Null") || DiscountPrice.equals("null") || DiscountPrice.equals("0.00")) {
                            //   rl_totol_discount.setVisibility(View.GONE);
                        } else {
                            // rl_totol_discount.setVisibility(View.VISIBLE);
                            //totaldiscount_price.setText("-" + Currency + " " + DiscountPrice);
                        }


                        if (PayByLoyality.equals("") || PayByLoyality.equals(null) || PayByLoyality.equals("Null") || PayByLoyality.equals("null") || PayByLoyality.equals("0.00")) {
                            //rl_redeem.setVisibility(View.GONE);
                        } else {
                            //rl_redeem.setVisibility(View.VISIBLE);
                            //regardpoint.setText("-" +Currency + " " + PayByLoyality);
                        }

                        if (WalletPay.equals("") || WalletPay.equals(null) || WalletPay.equals("Null") || WalletPay.equals("null") || WalletPay.equals("0.00")) {
                            //rl_paywallet.setVisibility(View.GONE);
                        } else {
                            //rl_paywallet.setVisibility(View.VISIBLE);
                            //paybywallet.setText("-" + Currency + " " + WalletPay);
                        }


                        if (GiftCardPay.equals("") || GiftCardPay.equals(null) || GiftCardPay.equals("Null") || GiftCardPay.equals("null") || GiftCardPay.equals("0.00")) {
                            //rl_giftpay.setVisibility(View.GONE);
                        } else {
                            //rl_giftpay.setVisibility(View.VISIBLE);
                            //gifcardprice.setText("-" +Currency+ " " + GiftCardPay);
                        }

                        if (collectionTime.equals("") || collectionTime.equals(null) || collectionTime.equals("Null") || collectionTime.equals("null") || collectionTime.equals("0.00")) {
                            //tv_ready.setVisibility(View.GONE);
                        } else {
                            //tv_ready.setVisibility(View.VISIBLE);
                            if (myPref.getCustomer_default_langauge().equals("en")) {
                                //  tv_ready.setText("Order ready at : " + collectionTime);

                            } else {

                                //tv_ready.setText(parseLanguage.getParseString("Order_ready_at")+": " + collectionTime);

                            }

                        }

                        if (myPref.getCustomer_default_langauge().equals("en")) {
                            //tv_deliveryon.setText("Order Placed at : " + RequestAtDate + "/" + RequestAtTime);
                            //tv_accpetdate.setText("Order Accepted at : " + OrderAcceptedDate + "/" + OrderAcceptedTime);


                        } else {

                            //tv_deliveryon.setText(parseLanguage.getParseString("Order_Placed_at")+" : " + RequestAtDate + "/" + RequestAtTime);
                            //tv_accpetdate.setText(parseLanguage.getParseString("Order_Accepted_at")+" : " + OrderAcceptedDate + "/" + OrderAcceptedTime);

                            //tv_ready.setText(parseLanguage.getParseString("Order_ready_at")+": " + collectionTime);

                        }
                        //tv_address.setText(customer_address);
                        //tv_instructions.setText(customer_instruction);
                        //     tv_no_foodItems.setText("No. of food items"+" "+"("+ number_of_items_order +")");
//                        btn_status.setText(status);

                        if (status.contains("Processing")) {
//                            Toast.makeText(Activity_Booking.this, ""+status, Toast.LENGTH_SHORT).show();
                            //                          btn_accept.setVisibility(View.GONE);
                            //                        btn_decline.setVisibility(View.GONE);
                            //                      orderclosed.setVisibility(View.GONE);
//                           takeprint.setVisibility(View.GONE);
                            //    takeprint1.setVisibility(View.GONE);
                            //                    btn_change_orderstatus.setVisibility(View.VISIBLE);
                            //                  btn_track_order.setVisibility(View.VISIBLE);
                            //                btn_markComplete.setVisibility(View.VISIBLE);

                        } else if (status.contains("Pending")) {
                            //              btn_accept.setVisibility(View.VISIBLE);
                            //            btn_decline.setVisibility(View.VISIBLE);
                            //          btn_change_orderstatus.setVisibility(View.GONE);
                            //        btn_track_order.setVisibility(View.GONE);
                            //      btn_markComplete.setVisibility(View.GONE);
                            //    orderclosed.setVisibility(View.GONE);
//                            takeprint.setVisibility(View.GONE);
                            //    takeprint1.setVisibility(View.GONE);

                        } else if (status.contains("Cancelled")) {
                            //  btn_accept.setVisibility(View.GONE);
                            //btn_decline.setVisibility(View.GONE);
                            //           btn_change_orderstatus.setVisibility(View.GONE);
                            //         btn_track_order.setVisibility(View.GONE);
                            //       btn_markComplete.setVisibility(View.GONE);
                            //     orderclosed.setVisibility(View.VISIBLE);
//                            takeprint.setVisibility(View.VISIBLE);
                            //  takeprint1.setVisibility(View.VISIBLE);

                        } else if (status.contains("Waiting")) {
                            //               btn_accept.setVisibility(View.VISIBLE);
                            //             btn_decline.setVisibility(View.VISIBLE);
                            //           btn_change_orderstatus.setVisibility(View.GONE);
                            //         btn_track_order.setVisibility(View.GONE);
                            //       btn_markComplete.setVisibility(View.GONE);
                            //     orderclosed.setVisibility(View.GONE);
//                            takeprint.setVisibility(View.GONE);
                            //  takeprint1.setVisibility(View.GONE);

                        } else if (status.contains("Delivered")) {
                            //   btn_accept.setVisibility(View.GONE);
                            // btn_decline.setVisibility(View.GONE);
                            //                     btn_change_orderstatus.setVisibility(View.GONE);
                            //                   btn_track_order.setVisibility(View.GONE);
                            //                 btn_markComplete.setVisibility(View.GONE);
                            //               orderclosed.setVisibility(View.VISIBLE);
//                            takeprint.setVisibility(View.VISIBLE);
                            //  takeprint1.setVisibility(View.VISIBLE);


                        } else if (status.contains("Accepted")) {
                            //             btn_accept.setVisibility(View.GONE);
                            //           btn_decline.setVisibility(View.GONE);
                            //         btn_change_orderstatus.setVisibility(View.VISIBLE);
                            //       btn_track_order.setVisibility(View.VISIBLE);
                            //     btn_markComplete.setVisibility(View.VISIBLE);
                            //   orderclosed.setVisibility(View.GONE);
//                            takeprint.setVisibility(View.VISIBLE);
                            //   takeprint1.setVisibility(View.GONE);


                        } else {

                        }

                        //btn_statusCard.setBackgroundColor(Color.parseColor(order_status_color_code));
                        //name.setText(name_customer);
                        //tv_number.setText(customer_phone);
                        //phonenumber = customer_phone;
                        if (myPref.getCustomer_default_langauge().equals("en")) {
                            //  tv_payment_type.setText("Payment Type : " + PaymentMethod);


                        } else {

                            //tv_payment_type.setText(parseLanguage.getParseString("Payment_Type")+" : " + PaymentMethod);

                            //tv_ready.setText(parseLanguage.getParseString("Order_ready_at")+": " + collectionTime);

                        }

//                        tv_discount.setText(DiscountPrice);


                        if (subTotal.equals("") || subTotal.equals(null) || subTotal.equals("Null") || subTotal.equals("null") || subTotal.equals("0.00")) {
                            //rl_subtotal.setVisibility(View.GONE);
                        } else {
                            //rl_subtotal.setVisibility(View.VISIBLE);
                            //tv_subtotal.setText(Currency + " " + subTotal);
                        }

                        if (DeliveryCharge.equals("") || DeliveryCharge.equals(null) || DeliveryCharge.equals("Null") || DeliveryCharge.equals("null") || DeliveryCharge.equals("0.00")) {
                            // rl_deleiverycharge.setVisibility(View.GONE);
                        } else {
                            //rl_deleiverycharge.setVisibility(View.VISIBLE);
                            //tv_deliveryfee.setText("+" + Currency + " " + DeliveryCharge);
                        }

                        if (SalesTaxAmount.equals("") || SalesTaxAmount.equals(null) || SalesTaxAmount.equals("Null") || SalesTaxAmount.equals("null") || SalesTaxAmount.equals("0.00")) {
                            // rl_servicetax.setVisibility(View.GONE);
                        } else {
                            // rl_servicetax.setVisibility(View.VISIBLE);
                            //tv_servicetax_price.setText("+" + Currency + " " + SalesTaxAmount);
                        }


                        if (ServiceFees.equals("") || ServiceFees.equals(null) || ServiceFees.equals("Null") || ServiceFees.equals("null") || ServiceFees.equals("0.00")) {
                            // rl_servicecost.setVisibility(View.GONE);
                        } else {
                            //rl_servicecost.setVisibility(View.VISIBLE);
                            //tv_Servicecost.setText("+" + Currency + " " + ServiceFees);
                        }


                        if (extraTipAddAmount.equals("") || extraTipAddAmount.equals(null) || extraTipAddAmount.equals("Null") || extraTipAddAmount.equals("null") || extraTipAddAmount.equals("0.00")) {
                            //rl_ridertrip.setVisibility(View.GONE);
                        } else {
                            //rl_ridertrip.setVisibility(View.VISIBLE);
                            //drivertip.setText("+" +Currency+ " " + extraTipAddAmount);
                        }


                        if (PackageFees.equals("") || PackageFees.equals(null) || PackageFees.equals("Null") || PackageFees.equals("null") || PackageFees.equals("0.00")) {
                            // rl_packgingcost.setVisibility(View.GONE);
                        } else {
                            //rl_packgingcost.setVisibility(View.VISIBLE);
                            //tv_packagingfee.setText("+" + Currency + " " + PackageFees);
                        }

                        if (VatTax.equals("") || VatTax.equals(null) || VatTax.equals("Null") || VatTax.equals("null") || VatTax.equals("0.00")) {
                            //rl_vattax.setVisibility(View.GONE);
                        } else {
                            //rl_vattax.setVisibility(View.VISIBLE);
                            //vat.setText("+" + Currency + " " + VatTax);
                        }

                        if (OrderPrice.equals("") || OrderPrice.equals(null) || OrderPrice.equals("Null") || OrderPrice.equals("null") || OrderPrice.equals("0.00")) {
                            //rl_total.setVisibility(View.GONE);
                        } else {
                            //rl_total.setVisibility(View.VISIBLE);
                            //total.setText(Currency + " " + OrderPrice);
                        }


                    }
                    try {
                        Log.i("reas","orderdetails");
                        if(player!=null) {
                            player.stop();
                        }

                        findBT();
                        openBT();
                        sendData();
                        doConnect();
                        if(myPref.getAuto_print_enable().equalsIgnoreCase("1")) {
                            PrintOrderReceipt(orderID, myPref.getAuto_print_enable());
                        }
                        else{
                            ShowOrderDialog(orderID,RequestAtDate,name_customer,OrderPrice,PaymentMethod,orderID);
                        }
                        printed=0;
//                                                PrintOrderReceipt(orderID, myPref.getAuto_print_enable());

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("qwerty", "" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                progressDialog.dismiss();
                Log.e("error", "" + volleyError);
                Toast.makeText(MainActivity.this, parseLanguage.getParseString("Please_Check_your_network_connection"), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", "" + orderID);
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("pa", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private RTPrinter rtPrinter = null;
    private TextSetting textSetting;
    private PrinterFactory printerFactory;
    private Object configObj;
    private PrinterPowerUtil printerPowerUtil;
    private int checkedConType = BaseEnum.CON_COM;
    private void connectSerialPort(SerialPortConfigBean serialPortConfigBean) {
        PIFactory piFactory = new SerailPortFactory();
        PrinterInterface printerInterface = piFactory.create();
        printerInterface.setConfigObject(serialPortConfigBean);
        rtPrinter.setPrinterInterface(printerInterface);
        try {
            rtPrinter.connect(serialPortConfigBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void doConnect() {
        switch (checkedConType) {
            case BaseEnum.CON_COM:
                connectSerialPort((SerialPortConfigBean) configObj);
                printerPowerUtil.setPrinterPower(true);//turn printer power on.
                break;
            default:
                break;
        }

    }
    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), img);
            if (bmp != null) {

                Double gh = (double) bmp.getWidth() / 384;

                BitmapUtils bitmapUtils = new BitmapUtils(this);
                Bitmap MBitmap2 = bitmapUtils.zoomBitmap(bmp, 384, (int) (bmp.getHeight() / gh));


                byte[] command = UtilsForPrinter.decodeBitmap(MBitmap2);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }

    }
    void sendData() throws IOException {
        try {
            byte[] printformat = new byte[]{0x1B, 0x21, 0x03};
            mmOutputStream.write(printformat);

            printPhoto(R.drawable.logo_color);
            printCustom("------------------------------", 2, 0);
            printCustom("" + OrderType, 1, 1);
            printCustom("Order Number : " + order_reference_number, 1, 1);
            printCustom("Order Date : " + RequestAtDate, 1, 1);
            printCustom("Order Ready At : " + collectionTime, 1, 1);
            printCustom("------------------------------", 2, 0);

            printCustom("Order Preferences", 1, 0);
            printCustom("Payment Method : " + PaymentMethod, 1, 0);
            printCustom("Order Status : " + status, 1, 0);

            printCustom("------------------------------", 2, 0);

            printCustom("Restaurant Info", 1, 1);
            printCustom("" + restaurant_name, 1, 1);
            printCustom("" + restaurant_address, 1, 1);

            printCustom("------------------------------", 2, 0);

            printCustom("" + PayOptionStatus, 1, 1);

            printCustom("------------------------------", 2, 0);

            printCustom("Order summary", 1, 1);
            leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Item:                    " + myPref.getCustomer_default_langauge(), 1);
            printNewLine();
            for (int i = 0; i < item_name.size(); i++) {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "" + item_quant.get(i) + " X " + item_name.get(i) + " :           " + " " + item_price.get(i), 1);
                printNewLine();
                leftRightAlign(PrinterCommands.ESC_ALIGN_CENTER, "" + extra_toping.get(i), 1);
                leftRightAlign(PrinterCommands.ESC_ALIGN_CENTER, "" + item_instruction.get(i), 1);
                printNewLine();
            }
            for (int i = 0; i < drink_item_name.size(); i++) {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "" + drink_item_quant.get(i) + " X " + drink_item_name.get(i) + " :           " + " " + drink_item_price.get(i), 1);
                printNewLine();
                leftRightAlign(PrinterCommands.ESC_ALIGN_CENTER, "" + drink_extra_toping.get(i), 1);
                leftRightAlign(PrinterCommands.ESC_ALIGN_CENTER, "" + drink_instruction.get(i), 1);
                printNewLine();
            }
            for (int i = 0; i < meal_item_name.size(); i++) {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "" + meal_item_quant.get(i) + " X " + meal_item_name.get(i) + " :           " + " " + meal_item_price.get(i), 1);
                printNewLine();
                leftRightAlign(PrinterCommands.ESC_ALIGN_CENTER, "" + meal_extra_toping.get(i), 1);
                leftRightAlign(PrinterCommands.ESC_ALIGN_CENTER, "" + meal_instruction.get(i), 1);
                printNewLine();
            }
            printCustom("------------------------------", 2, 0);

            if (FoodCosts.equals("") || FoodCosts.equals(null) || FoodCosts.equals("Null") || FoodCosts.equals("null") || FoodCosts.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Food Costs:       " + " " + FoodCosts, 1);
                printNewLine();
            }

            if (DiscountPrice.equals("") || DiscountPrice.equals(null) || DiscountPrice.equals("Null") || DiscountPrice.equals("null") || DiscountPrice.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Total Discount:    " + "-" + " " + DiscountPrice, 1);
                printNewLine();
            }


            if (PayByLoyality.equals("") || PayByLoyality.equals(null) || PayByLoyality.equals("Null") || PayByLoyality.equals("null") || PayByLoyality.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Pay by Loyalty Coins     " + "-" + " " + PayByLoyality, 1);
                printNewLine();
            }

            if (WalletPay.equals("") || WalletPay.equals(null) || WalletPay.equals("Null") || WalletPay.equals("null") || WalletPay.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Pay by Wallet     " + "-" + " " + WalletPay, 1);
                printNewLine();
            }

            if (GiftCardPay.equals("") || GiftCardPay.equals(null) || GiftCardPay.equals("Null") || GiftCardPay.equals("null") || GiftCardPay.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Pay by Gift Card     " + "-" + " " + WalletPay, 1);
                printNewLine();
            }

            leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Sub Total:       " + " " + subTotal, 1);
            printNewLine();

            if (DeliveryCharge.equals("") || DeliveryCharge.equals(null) || DeliveryCharge.equals("Null") || DeliveryCharge.equals("null") || DeliveryCharge.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Delivery Charge:     " + "+" + " " + DeliveryCharge, 1);
                printNewLine();
            }

            if (ServiceFees.equals("") || ServiceFees.equals(null) || ServiceFees.equals("Null") || ServiceFees.equals("null") || ServiceFees.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Service Fees:     " + "+" + " " + ServiceFees, 1);
                printNewLine();
            }

            if (PackageFees.equals("") || PackageFees.equals(null) || PackageFees.equals("Null") || PackageFees.equals("null") || PackageFees.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Packaging Fees:     " + "+" + " " + PackageFees, 1);
                printNewLine();
            }

            if (SalesTaxAmount.equals("") || SalesTaxAmount.equals(null) || SalesTaxAmount.equals("Null") || SalesTaxAmount.equals("null") || SalesTaxAmount.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Service Tax:     " + "+" + " " + SalesTaxAmount, 1);
                printNewLine();
            }

            if (VatTax.equals("") || VatTax.equals(null) || VatTax.equals("Null") || VatTax.equals("null") || VatTax.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "VAT:        " + "+" + " " + VatTax, 1);
                printNewLine();
            }

            if (extraTipAddAmount.equals("") || extraTipAddAmount.equals(null) || extraTipAddAmount.equals("Null") || extraTipAddAmount.equals("null") || extraTipAddAmount.equals("0.00")) {

            } else {
                leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Rider Tip:     " + "+" + " " + extraTipAddAmount, 1);
                printNewLine();
            }

            leftRightAlign(PrinterCommands.ESC_ALIGN_RIGHT, "Grand Total:       " + " " + OrderPrice, 1);
            printNewLine();

            printCustom("-------------------------------", 2, 0);

            printCustom("Customer Details :", 1, 0);
            printCustom(name_customer, 1, 0);
            printCustom(customer_address, 1, 0);

            printCustom("-------------------------------", 2, 0);


            if (!instruction_note.equals("")) {
                printCustom("Note :", 1, 0);
                printCustom(instruction_note, 1, 0);

                printCustom("-------------------------------", 2, 0);
            }


            printCustom("Order Placed at :", 1, 0);
            printCustom(RequestAtDate + "/" + RequestAtTime, 1, 0);

            printCustom("Order Accepted at :", 1, 0);
            printCustom(OrderAcceptedDate + "/" + OrderAcceptedTime, 1, 0);


            printCustom("Total customar orders :", 1, 0);
            printCustom(number_of_customer_order, 1, 0);

            printCustom("-------------------------------", 2, 0);

            printCustom(website_copy_right_text, 1, 0);

            printCustom("------------------------------", 2, 0);

            printNewLine();
            printNewLine();
            mmOutputStream.flush();
            //  myLabel.setText("Data sent.");

            Toast.makeText(this, "Data Sent", Toast.LENGTH_SHORT).show();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void leftRightAlign(byte[] align, String msg, int size) {
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1B, 0x21, 0x10};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x12};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x15}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x20}; // 3- bold with large text
        byte[] bb4 = new byte[]{0x1B, 0x21, 0x25}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
                case 4:
                    mmOutputStream.write(bb4);
                    break;
                case 5:
                    mmOutputStream.write(cc1);
                    break;
            }


            mmOutputStream.write(align);
            String space = "   ";
            int l = msg.length();
            if (l < 31) {
                for (int x = 31 - l; x >= 0; x--) {
                    space = space + " ";
                }
            }
            msg = msg.replace(" : ", space);
            mmOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1B, 0x21, 0x10};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x12};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x15}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x20}; // 3- bold with large text
        byte[] bb4 = new byte[]{0x1B, 0x21, 0x25}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
                case 4:
                    mmOutputStream.write(bb4);
                    break;
                case 5:
                    mmOutputStream.write(cc1);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(latestOrder!=null){
            this.unregisterReceiver(latestOrder);
        }
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

                    if(jsonObject.has("HomeDelivery")){
                        String home_delivery=jsonObject.optString("HomeDelivery");
                        myPref.setIsOnline(home_delivery);
                        if(home_delivery.equalsIgnoreCase("0")){
                            switch_button_delivery.setImageResource(R.drawable.on);
                        }
                        else {
                            switch_button_delivery.setImageResource(R.drawable.off);
                        }
                    }


//                    if(HomeDelivery.equalsIgnoreCase("1")){
//                        switch_button_delivery.setImageResource(R.drawable.off);
//                    }else {
//                        switch_button_delivery.setImageResource(R.drawable.on);
//
//                    }


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
                        myPref.setAuto_print_enable(OrderAcceptanceEnable);
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
                    if(jsonObject.has("Pickup")){
                        String pickup=jsonObject.optString("Pickup");
                        myPref.setIsRideAccepted(pickup);
                        if(pickup.equalsIgnoreCase("0")){
                            switch_button_pickup.setImageResource(R.drawable.on);
                        }else {

                            switch_button_pickup.setImageResource(R.drawable.off);
                        }

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
    private void findBT() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                //myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
//
//                    if (device.getName().equals(Innerprinter_Address)) {
//                        mmDevice = device;
//                        break;

                    if (device.getAddress().equals(Innerprinter_Address)) {
                        mmDevice = device;
//                        innerprinter_device = device;

                        break;
                    }
                }
            }

            Toast.makeText(this, "Bluetooth Device Found.", Toast.LENGTH_SHORT).show();
            //myLabel.setText("Bluetooth device found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            String Innerprinter_Address = "00:11:22:33:44:55";

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            Toast.makeText(this, "Bluetooth Opened", Toast.LENGTH_SHORT).show();

            //myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    volatile boolean stopWorker;
    int readBufferPosition;
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {
                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
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


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStart() {
        super.onStart();
        BaseApplication.activityStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.activityStop();
    }

    @Override
    public void printerObserverCallback(PrinterInterface printerInterface, int i) {

    }

    @Override
    public void printerReadMsgCallback(PrinterInterface printerInterface, byte[] bytes) {

    }
    private String mChartsetName = "UTF-8";

    public class LatestOrder extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String orderId= (String) bundle.get("orderId");
            if(orderId!=null){
getorderdetails(orderId,"1");
            }
        }
    }
    private void PrintOrderReceipt(String orderid, String auto_print_enable) throws UnsupportedEncodingException {

        if (rtPrinter != null) {



                Log.i("reas", "isidePriter");
                CmdFactory escFac = new EscFactory();
                Cmd escCmd = escFac.create();
                escCmd.append(escCmd.getHeaderCmd());//初始化, Initial

                escCmd.setChartsetName(mChartsetName);// "UTF-8"

                TextSetting textSetting = new TextSetting();
                textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                if (collectionTime.equalsIgnoreCase("null")) {
                    escCmd.append(escCmd.getTextCmd(textSetting, RequestAtDate));
                } else {
                    escCmd.append(escCmd.getTextCmd(textSetting, RequestAtDate + "  " + collectionTime));
                }
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting textSetting1 = new TextSetting();
                textSetting1.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(textSetting1, restaurant_name));
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting textSetting2 = new TextSetting();
                textSetting2.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(textSetting2, restaurant_address));
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting txtmobile = new TextSetting();
                txtmobile.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(txtmobile, restaurant_mobile_number));
                escCmd.append(escCmd.getLFCRCmd());
                TextSetting txt_reastudash = new TextSetting();
                txt_reastudash.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(txt_reastudash, "--------------------------------"));
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting txtOrderType = new TextSetting();
                txtOrderType.setAlign(CommonEnum.ALIGN_MIDDLE);
                txtOrderType.setBold(SettingEnum.Enable);
                escCmd.append(escCmd.getTextCmd(txtOrderType, OrderType));
                escCmd.append(escCmd.getLFCRCmd());
                escCmd.append(escCmd.getLFCRCmd());
                TextSetting txtOrderNo = new TextSetting();
                txtOrderNo.setAlign(CommonEnum.ALIGN_MIDDLE);
                txtOrderNo.setBold(SettingEnum.Enable);
                escCmd.append(escCmd.getTextCmd(txtOrderNo, order_reference_number));
                escCmd.append(escCmd.getLFCRCmd());
                escCmd.append(escCmd.getLFCRCmd());
                TextSetting orderDate = new TextSetting();
                orderDate.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(orderDate, RequestAtDate + " / " + RequestAtTime));
                escCmd.append(escCmd.getLFCRCmd());
        /*    TextSetting orderTime = new TextSetting();
            orderTime.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(orderTime,  RequestAtTime));
            escCmd.append(escCmd.getLFCRCmd());*/
                TextSetting orderReadyAt = new TextSetting();
                orderReadyAt.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(orderReadyAt, parseLanguage.getParseString("Order_ready_at")));
                escCmd.append(escCmd.getLFCRCmd());
                if (!collectionTime.equalsIgnoreCase("null")) {
                    TextSetting textcollection = new TextSetting();
                    textcollection.setAlign(CommonEnum.ALIGN_MIDDLE);
                    escCmd.append(escCmd.getTextCmd(textcollection, collectionTime));
                    escCmd.append(escCmd.getLFCRCmd());
                }
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting text12 = new TextSetting();
                text12.setAlign(CommonEnum.ALIGN_BOTH_SIDES);
                escCmd.append(escCmd.getTextCmd(text12, parseLanguage.getParseString("Payment_Type") + " : " + PaymentMethod));
                escCmd.append(escCmd.getLFCRCmd());


                TextSetting txt_paymentdash = new TextSetting();
                txt_paymentdash.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(txt_paymentdash, "--------------------------------"));
                escCmd.append(escCmd.getLFCRCmd());


                if (!instruction_note.equals("")) {

                    TextSetting txt_customerNote = new TextSetting();
                    txt_customerNote.setAlign(CommonEnum.ALIGN_LEFT);
                    txt_customerNote.setBold(SettingEnum.Enable);
                    String customer_note = parseLanguage.getParseString("Customer_note");
                    if (customer_note.equalsIgnoreCase("No Response")) {
                        if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                            customer_note = getString(R.string.customer_note);
                        } else {
                            customer_note = "Customer Note";
                        }
                    }
                    escCmd.append(escCmd.getTextCmd(txt_customerNote, customer_note + ":"));
                    escCmd.append(escCmd.getLFCRCmd());


                    TextSetting txt = new TextSetting();
                    txt.setAlign(CommonEnum.ALIGN_LEFT);
                    escCmd.append(escCmd.getTextCmd(txt, instruction_note));
                    escCmd.append(escCmd.getLFCRCmd());

                    TextSetting txt_CustomerNotedash = new TextSetting();
                    txt_CustomerNotedash.setAlign(CommonEnum.ALIGN_MIDDLE);
                    escCmd.append(escCmd.getTextCmd(txt_CustomerNotedash, "--------------------------------"));
                    escCmd.append(escCmd.getLFCRCmd());
                }

                TextSetting txt_CustomerInfo1 = new TextSetting();
                txt_CustomerInfo1.setAlign(CommonEnum.ALIGN_LEFT);
                txt_CustomerInfo1.setBold(SettingEnum.Enable);
                escCmd.append(escCmd.getTextCmd(txt_CustomerInfo1, parseLanguage.getParseString("Customer_info") + ":"));
                escCmd.append(escCmd.getLFCRCmd());

                if (CompanyName != null) {
                    TextSetting txt_company = new TextSetting();
                    txt_company.setAlign(CommonEnum.ALIGN_LEFT);
                    txt_company.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(txt_company, CompanyName));
                    escCmd.append(escCmd.getLFCRCmd());
                }
                TextSetting txt_CustomerInfo = new TextSetting();
                txt_CustomerInfo.setAlign(CommonEnum.ALIGN_LEFT);
                txt_CustomerInfo.setBold(SettingEnum.Enable);
                escCmd.append(escCmd.getTextCmd(txt_CustomerInfo, name_customer));
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting txt_Customeradd = new TextSetting();
                txt_Customeradd.setAlign(CommonEnum.ALIGN_LEFT);
                txt_Customeradd.setBold(SettingEnum.Enable);
                escCmd.append(escCmd.getTextCmd(txt_Customeradd, customer_address));
                escCmd.append(escCmd.getLFCRCmd());
                if (customer_city != null) {
                    TextSetting city = new TextSetting();
                    city.setAlign(CommonEnum.ALIGN_LEFT);
                    city.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(city, customer_city));
                    escCmd.append(escCmd.getLFCRCmd());
                }
                if (customer_postcode != null) {
                    TextSetting postcode = new TextSetting();
                    postcode.setAlign(CommonEnum.ALIGN_LEFT);
                    postcode.setBold(SettingEnum.Enable);
                    escCmd.append(escCmd.getTextCmd(postcode, customer_postcode));
                    escCmd.append(escCmd.getLFCRCmd());
                }

                TextSetting txt_CustomerMobile = new TextSetting();
                txt_CustomerMobile.setAlign(CommonEnum.ALIGN_LEFT);
                escCmd.append(escCmd.getTextCmd(txt_CustomerMobile, customer_phone));
                escCmd.append(escCmd.getLFCRCmd());


                TextSetting txt_Customeremail = new TextSetting();
                txt_Customeremail.setAlign(CommonEnum.ALIGN_LEFT);
                escCmd.append(escCmd.getTextCmd(txt_Customeremail, customer_email));
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting txt_Customeremaildash = new TextSetting();
                txt_Customeremaildash.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(txt_Customeremaildash, "--------------------------------"));
                escCmd.append(escCmd.getLFCRCmd());


                if (!number_of_customer_order.equals("")) {
                    TextSetting txt_backorders = new TextSetting();
                    txt_backorders.setAlign(CommonEnum.ALIGN_MIDDLE);
                    String back_order = parseLanguage.getParseString("Back_orders_from_customer");
                    if (back_order.equalsIgnoreCase("No Response")) {
                        if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                            back_order = getString(R.string.back_orders);
                        } else {
                            back_order = "Back orders from customers";
                        }
                    }
                    escCmd.append(escCmd.getTextCmd(txt_backorders, back_order + ":" + number_of_customer_order));
                    escCmd.append(escCmd.getLFCRCmd());
                }
                TextSetting txt_PayOptionStatus = new TextSetting();
                txt_PayOptionStatus.setAlign(CommonEnum.ALIGN_MIDDLE);
                txt_PayOptionStatus.setBold(SettingEnum.Enable);
                escCmd.append(escCmd.getTextCmd(txt_PayOptionStatus, PayOptionStatus));
                escCmd.append(escCmd.getLFCRCmd());
                TextSetting txtpaystatusdash = new TextSetting();
                txtpaystatusdash.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(txtpaystatusdash, "--------------------------------"));
                escCmd.append(escCmd.getLFCRCmd());
                TextSetting txtitemname = new TextSetting();
                txtitemname.setAlign(CommonEnum.ALIGN_LEFT);
                txtitemname.setBold(SettingEnum.Enable);
                escCmd.append(escCmd.getTextCmd(txtitemname, parseLanguage.getParseString("Item_Name")));
                escCmd.append(escCmd.getLFCRCmd());
                TextSetting textSetting3 = new TextSetting();
                textSetting3.setAlign(CommonEnum.ALIGN_MIDDLE);
                TextSetting text_name = new TextSetting();
                text_name.setAlign(CommonEnum.ALIGN_RIGHT);
                for (int i = 0; i < item_name.size(); i++) {
                    textSetting3.setAlign(CommonEnum.ALIGN_LEFT);
                    String price = item_price.get(i);
                    if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                        price = price.replace(".", ",");
                    }
                    escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + " "));

                    escCmd.append(escCmd.getLFCRCmd());
                    if (item_size.size() > 0) {

                        if (!item_size.get(i).equalsIgnoreCase("")) {
                            escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_size.get(i)));
                            escCmd.append(escCmd.getLFCRCmd());
                        }
                    }
                    escCmd.append(escCmd.getTextCmd(text_name, Currency + price));
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
//                escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "" + Currency + price));
//                switch (item_quant.get(i).length() + item_name.get(i).length() + item_price.get(i).length()) {
//                    case 6:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                     " + Currency + price));
//                        break;
//                    case 7:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                    " + Currency + price));
//                        break;
//                    case 8:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                   " + Currency + price));
//                        break;
//                    case 9:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                  " + Currency + price));
//                        break;
//                    case 10:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                 " + Currency + price));
//                        break;
//                    case 11:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                " + Currency + price));
//                        break;
//                    case 12:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "               " + Currency + price));
//                        break;
//                    case 13:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "              " + Currency + price));
//                        break;
//                    case 14:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "             " + Currency + price));
//                        break;
//                    case 15:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "            " + Currency + price));
//                        break;
//                    case 16:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "           " + Currency + price));
//                        break;
//                    case 17:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "          " + Currency + price));
//                        break;
//                    case 18:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "         " + Currency + price));
//                        break;
//                    case 19:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "        " + Currency + price));
//                        break;
//                    case 20:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "       " + Currency + price));
//                        break;
//                    case 21:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "      " + Currency + price));
//                        break;
//                    case 22:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "     " + Currency + price));
//                        break;
//                    case 23:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "    " + Currency + price));
//                        break;
//                    case 24:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "   " + Currency + price));
//                        break;
//                    case 25:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "  " + Currency + price));
//                        break;
//                    case 26:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + " " + Currency + price));
//                        break;
//                    case 27:
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "" + Currency + price));
//                        break;
//                }

//
//                if(item_size.size()>0) {
//
//                    if (!item_size.get(i).equalsIgnoreCase("")) {
//                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_size.get(i)));
//                        escCmd.append(escCmd.getLFCRCmd());
//                    }
//                }
                    if (extra_toping.size() > 0) {


                        if (!extra_toping.get(i).equalsIgnoreCase("")) {

                            ArrayList<Model_OrderComboItemExtra> model_orderComboItemExtras = prepareDataForExtraTopping(extra_toping.get(i));
                            for (int i1 = 0; i1 < model_orderComboItemExtras.size(); i1++) {
                                String comboExtraItemQuantity = model_orderComboItemExtras.get(i1).getComboExtraItemQuantity();
                                String comboExtraItemName = model_orderComboItemExtras.get(i1).getComboExtraItemName();
                                String comboExtraItemPrice = model_orderComboItemExtras.get(i1).getComboExtraItemPrice();
                                if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                                    comboExtraItemPrice = comboExtraItemPrice.replace(".", ",");
                                }
                                switch (comboExtraItemQuantity.length() + comboExtraItemName.length() + comboExtraItemPrice.length()) {
                                    case 5:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                      " + Currency + comboExtraItemPrice));
                                        break;
                                    case 6:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                     " + Currency + comboExtraItemPrice));
                                        break;
                                    case 7:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                    " + Currency + comboExtraItemPrice));
                                        break;
                                    case 8:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                   " + Currency + comboExtraItemPrice));
                                        break;
                                    case 9:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                  " + Currency + comboExtraItemPrice));
                                        break;
                                    case 10:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                 " + Currency + comboExtraItemPrice));
                                        break;
                                    case 11:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                " + Currency + comboExtraItemPrice));
                                        break;
                                    case 12:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "               " + Currency + comboExtraItemPrice));
                                        break;
                                    case 13:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "              " + Currency + comboExtraItemPrice));
                                        break;
                                    case 14:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "             " + Currency + comboExtraItemPrice));
                                        break;
                                    case 15:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "            " + Currency + comboExtraItemPrice));
                                        break;
                                    case 16:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "           " + Currency + comboExtraItemPrice));
                                        break;
                                    case 17:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "          " + Currency + comboExtraItemPrice));
                                        break;
                                    case 18:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "         " + Currency + comboExtraItemPrice));
                                        break;
                                    case 19:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "        " + Currency + comboExtraItemPrice));
                                        break;
                                    case 20:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "       " + Currency + comboExtraItemPrice));
                                        break;
                                    case 21:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "      " + Currency + comboExtraItemPrice));
                                        break;
                                    case 22:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "     " + Currency + comboExtraItemPrice));
                                        break;
                                    case 23:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "    " + Currency + comboExtraItemPrice));
                                        break;
                                    case 24:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "   " + Currency + comboExtraItemPrice));
                                        break;
                                    case 25:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "  " + Currency + comboExtraItemPrice));
                                        break;
                                    case 26:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + " " + Currency + comboExtraItemPrice));
                                        break;
                                    case 27:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "" + Currency + comboExtraItemPrice));
                                        break;
                                }

                                escCmd.append(escCmd.getLFCRCmd());

                            }
                        }

                    }


                    if (!item_instruction.get(i).equalsIgnoreCase("")) {
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_instruction.get(i)));
                        escCmd.append(escCmd.getLFCRCmd());
                    } else {
//                    escCmd.append(escCmd.getLFCRCmd());
//                    escCmd.append(escCmd.getLFCRCmd());
                    }
                    escCmd.append(escCmd.getLFCRCmd());


                }

                /*for combo starting*/

                if (model_combos.size() > 0) {
                    for (int m = 0; m < model_combos.size(); m++) {
                        Model_Combo model_combo = model_combos.get(m);
                        TextSetting txt_backorders = new TextSetting();
                        txt_backorders.setAlign(CommonEnum.ALIGN_LEFT);
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName()));
                        escCmd.append(escCmd.getLFCRCmd());
                        TextSetting txt_price = new TextSetting();
                        txt_price.setAlign(CommonEnum.ALIGN_RIGHT);
                        escCmd.append(escCmd.getTextCmd(txt_price, Currency + model_combo.getMenuprice()));
                        escCmd.append(escCmd.getLFCRCmd());
//                    TextSetting menu_price=new TextSetting();
//                    menu_price.setAlign(CommonEnum.ALIGN_RIGHT);
//                    escCmd.append(escCmd.getTextCmd(menu_price,Currency + model_combo.getMenuprice()));

//                    switch (model_combo.getQuantity().length() + model_combo.getItemsName().length() + model_combo.getMenuprice().length()) {
//
//                        case 2:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                          " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 3:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                         " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 4:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                        " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 5:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                       " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 6:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                      " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 7:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                     " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 8:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                    " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 9:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                   " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 10:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                  " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 11:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                 " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 12:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 13:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "               " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 14:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "              " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 15:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "             " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 16:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "            " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 17:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "           " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 18:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "          " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 19:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "         " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 20:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "        " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 21:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "       " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 22:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "      " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 23:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "     " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 24:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "    " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 25:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "   " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 26:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "  " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 27:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + " " + Currency + model_combo.getMenuprice()));
//                            break;
//                        case 28:
//                            escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + Currency + model_combo.getMenuprice()));
//                            break;
//                    }
                        escCmd.append(escCmd.getLFCRCmd());
                        TextSetting txt_comboDescribe = new TextSetting();
                        txt_comboDescribe.setAlign(CommonEnum.ALIGN_LEFT);
                        escCmd.append(escCmd.getTextCmd(txt_comboDescribe, model_combo.getItemsDescriptionName()));
                        escCmd.append(escCmd.getLFCRCmd());
                        escCmd.append(escCmd.getLFCRCmd());
                        TextSetting textSetting31 = new TextSetting();
                        textSetting31.setAlign(CommonEnum.ALIGN_LEFT);
//
//                    Toast.makeText(this, model_combo.getOrderComboItemOption().get(0).getComboOptionName(), Toast.LENGTH_SHORT).show();
                        ArrayList<Model_OrderComboItemOption> orderComboItemOption = model_combo.getOrderComboItemOption();
                        for (int i = 0; i < orderComboItemOption.size(); i++) {
                            String combo_option_name = orderComboItemOption.get(i).getComboOptionName();
                            if (combo_option_name != null) {
                                if (!combo_option_name.equalsIgnoreCase("null")) {
                                    escCmd.append(escCmd.getTextCmd(textSetting31, combo_option_name));
                                    escCmd.append(escCmd.getLFCRCmd());
                                    escCmd.append(escCmd.getLFCRCmd());
                                }
                            }
                            textSetting3.setAlign(CommonEnum.ALIGN_LEFT);
                            textSetting3.setBold(SettingEnum.Enable);
                            String option_name = orderComboItemOption.get(i).getComboOptionItemName();
                            if (!option_name.equalsIgnoreCase("null")) {
                                escCmd.append(escCmd.getTextCmd(textSetting3, option_name));
                                escCmd.append(escCmd.getLFCRCmd());
                            }
                            Model_OrderComboItemOption model_orderComboItemOption = orderComboItemOption.get(i);
                            if (!model_orderComboItemOption.getComboOptionItemSizeName().equalsIgnoreCase("")) {
                                textSetting3.setAlign(CommonEnum.ALIGN_LEFT);
                                String option_size = model_orderComboItemOption.getComboOptionItemSizeName();
                                if (!option_size.equalsIgnoreCase("null")) {
                                    escCmd.append(escCmd.getTextCmd(textSetting3, option_size));
                                    escCmd.append(escCmd.getLFCRCmd());
                                    escCmd.append(escCmd.getLFCRCmd());
//                                escCmd.append(escCmd.getLFCRCmd());
                                }
                            }
                            for (int i1 = 0; i1 < model_orderComboItemOption.getOrderComboItemExtra().size(); i1++) {
                                String comboExtraItemName = model_orderComboItemOption.getOrderComboItemExtra().get(i1).getComboExtraItemName();
                                String comboExtraItemPrice = model_orderComboItemOption.getOrderComboItemExtra().get(i1).getComboExtraItemPrice();
                                String comboExtraItemQuantity = model_orderComboItemOption.getOrderComboItemExtra().get(i1).getComboExtraItemQuantity();
                                if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                                    comboExtraItemPrice = comboExtraItemPrice.replace(".", ",");
                                }
                                textSetting3.setAlign(CommonEnum.ALIGN_LEFT);
//                            escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                   " + Currency + comboExtraItemPrice));

                                switch (comboExtraItemQuantity.length() + comboExtraItemName.length() + comboExtraItemPrice.length()) {
                                    case 9:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                   " + Currency + comboExtraItemPrice));
                                        break;
                                    case 10:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                 " + Currency + comboExtraItemPrice));
                                        break;
                                    case 11:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "                " + Currency + comboExtraItemPrice));
                                        break;
                                    case 12:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "               " + Currency + comboExtraItemPrice));
                                        break;
                                    case 13:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "              " + Currency + comboExtraItemPrice));
                                        break;
                                    case 14:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "             " + Currency + comboExtraItemPrice));
                                        break;
                                    case 15:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "            " + Currency + comboExtraItemPrice));
                                        break;
                                    case 16:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "           " + Currency + comboExtraItemPrice));
                                        break;
                                    case 17:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "          " + Currency + comboExtraItemPrice));
                                        break;
                                    case 18:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "         " + Currency + comboExtraItemPrice));
                                        break;
                                    case 19:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "        " + Currency + comboExtraItemPrice));
                                        break;
                                    case 20:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "       " + Currency + comboExtraItemPrice));
                                        break;
                                    case 21:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "      " + Currency + comboExtraItemPrice));
                                        break;
                                    case 22:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "     " + Currency + comboExtraItemPrice));
                                        break;
                                    case 23:
                                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName + ":" + "    " + Currency + comboExtraItemPrice));
                                        break;
                                }


                                if (i == (model_orderComboItemOption.getOrderComboItemExtra().size() - 1)) {
                                    escCmd.append(escCmd.getLFCRCmd());
//                                escCmd.append(escCmd.getLFCRCmd());
                                } else {
//                                escCmd.append(escCmd.getLFCRCmd());
                                }
                            }

                            escCmd.append(escCmd.getLFCRCmd());
                            escCmd.append(escCmd.getLFCRCmd());
                            escCmd.append(escCmd.getLFCRCmd());
                        }
                    }
                }


                if (!discountOfferFreeItems.equals("")) {
                    TextSetting txt_backorders = new TextSetting();
                    txt_backorders.setAlign(CommonEnum.ALIGN_MIDDLE);
                    String food_available = parseLanguage.getParseString("Free_Item");
                    if (food_available.equalsIgnoreCase("No Response")) {
                        if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                            food_available = getString(R.string.food_available);
                        } else {
                            food_available = "Free food available";
                        }
                    }
                    switch (discountOfferFreeItems.length()) {
                        case 4:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + "        " + discountOfferFreeItems));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + ":       " + discountOfferFreeItems));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + ":      " + discountOfferFreeItems));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + ":     " + discountOfferFreeItems));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + ":    " + discountOfferFreeItems));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + ":   " + discountOfferFreeItems));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + ":  " + discountOfferFreeItems));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + ": " + discountOfferFreeItems));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, food_available + ":" + discountOfferFreeItems));
                            break;
                    }
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                }

                if (!Table_Booking_Number.equals("")) {
                    TextSetting txt_backorders = new TextSetting();
                    txt_backorders.setAlign(CommonEnum.ALIGN_MIDDLE);
                    String table_no = parseLanguage.getParseString("Table_no");
                    if (table_no.equalsIgnoreCase("No Response")) {
                        if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                            table_no = getString(R.string.table_no);
                        } else {

                            table_no = "Table No";
                        }
                    }
                    switch (Table_Booking_Number.length()) {
                        case 4:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":                   " + Table_Booking_Number));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":                  " + Table_Booking_Number));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":                 " + Table_Booking_Number));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":                " + Table_Booking_Number));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":               " + Table_Booking_Number));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":              " + Table_Booking_Number));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":             " + Table_Booking_Number));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":            " + Table_Booking_Number));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":           " + Table_Booking_Number));
                            break;
                        case 13:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":          " + Table_Booking_Number));
                            break;
                        case 14:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":         " + Table_Booking_Number));
                            break;
                        case 15:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":        " + Table_Booking_Number));
                            break;
                        case 16:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":       " + Table_Booking_Number));
                            break;
                        case 17:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":      " + Table_Booking_Number));
                            break;
                        case 18:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":     " + Table_Booking_Number));
                            break;
                        case 19:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":    " + Table_Booking_Number));
                            break;
                        case 20:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":   " + Table_Booking_Number));
                            break;
                        case 21:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":  " + Table_Booking_Number));
                            break;
                        case 22:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ": " + Table_Booking_Number));
                            break;
                        case 23:
                            escCmd.append(escCmd.getTextCmd(txt_backorders, table_no + ":" + Table_Booking_Number));
                            break;
                    }

                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                } else {
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                }


//            if (!(FoodCosts.equals("") || FoodCosts.equals(null) || FoodCosts.equals("Null") || FoodCosts.equals("null") || FoodCosts.equals("0.00"))) {
//
//                TextSetting textSetting4 = new TextSetting();
//                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
//                String food_cost=parseLanguage.getParseString("Food_Cost");
//                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
//                    FoodCosts=FoodCosts.replace(".", ",");
//                }
//                switch (FoodCosts.length()) {
//                    case 12:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":  " + Currency + FoodCosts));
//                        break;
//                    case 11:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":  " + Currency + FoodCosts));
//                        break;
//                    case 10:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":  " + Currency + FoodCosts));
//                        break;
//                    case 9:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
//                        break;
//                    case 8:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":    " + Currency + FoodCosts));
//                        break;
//                    case 7:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
//                        break;
//                    case 6:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
//                        break;
//                    case 5:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
//                        break;
//                    case 4:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":    " + Currency + FoodCosts));
//                        break;
//                    case 3:
//                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
//                        break;
//
//                }
//
//                escCmd.append(escCmd.getLFCRCmd());
//
//
//            }

                if (!(PayByLoyality.equals("") || PayByLoyality.equals(null) || PayByLoyality.equals("Null") || PayByLoyality.equals("null") || PayByLoyality.equals("0.00"))) {

                    TextSetting textSetting4 = new TextSetting();
                    textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                    String total_redeem = parseLanguage.getParseString("Total_redeem_point");
                    if (total_redeem.equalsIgnoreCase("No Response")) {
                        if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                            total_redeem = getString(R.string.total_redeem);
                        } else {
                            total_redeem = "Total Redeem Point";
                        }
                    }
                    switch (PayByLoyality.length()) {
                        case 1:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":            " + PayByLoyality));
                        case 2:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":           " + PayByLoyality));
                        case 3:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":          " + PayByLoyality));
                        case 4:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":         " + PayByLoyality));
                        case 5:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":        " + PayByLoyality));
                        case 6:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":       " + PayByLoyality));
                        case 7:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":      " + PayByLoyality));
                        case 8:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":     " + PayByLoyality));
                        case 9:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":    " + PayByLoyality));
                        case 10:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":   " + PayByLoyality));
                        case 11:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":  " + PayByLoyality));
                        case 12:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ": " + PayByLoyality));
                        case 13:
                            escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem + ":" + PayByLoyality));
                    }

                    escCmd.append(escCmd.getLFCRCmd());

                }

                if (!(WalletPay.equals("") || WalletPay.equals(null) || WalletPay.equals("Null") || WalletPay.equals("null") || WalletPay.equals("0.00"))) {

                    TextSetting textSetting4 = new TextSetting();
                    textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                    String pay_wallet = parseLanguage.getParseString("Pay_by_Wallet");
                    switch (WalletPay.length()) {
                        case 3:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":               " + WalletPay));
                            break;
                        case 4:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":              " + WalletPay));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":             " + WalletPay));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":            " + WalletPay));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":           " + WalletPay));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":          " + WalletPay));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":         " + WalletPay));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":        " + WalletPay));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":       " + WalletPay));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":      " + WalletPay));
                            break;
                        case 13:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":     " + WalletPay));
                            break;
                        case 14:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":    " + WalletPay));
                            break;
                        case 15:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":   " + WalletPay));
                            break;
                        case 16:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":  " + WalletPay));
                            break;
                        case 17:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ": " + WalletPay));
                            break;
                        case 18:
                            escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet + ":" + WalletPay));
                            break;
                    }

                    escCmd.append(escCmd.getLFCRCmd());

                }
                if (GiftCardPay != null) {
                    if (!(GiftCardPay.equalsIgnoreCase("") || GiftCardPay.equalsIgnoreCase("null") || GiftCardPay.equalsIgnoreCase("0.00"))) {

                        TextSetting textSetting4 = new TextSetting();
                        textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                        switch (GiftCardPay.length()) {
                            case 2:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":                " + GiftCardPay));
                                break;
                            case 3:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":               " + GiftCardPay));
                                break;
                            case 4:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":              " + GiftCardPay));
                                break;
                            case 5:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":             " + GiftCardPay));
                                break;
                            case 6:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":            " + GiftCardPay));
                                break;
                            case 7:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":           " + GiftCardPay));
                                break;
                            case 8:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":          " + GiftCardPay));
                                break;
                            case 9:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":         " + GiftCardPay));
                                break;
                            case 10:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":        " + GiftCardPay));
                                break;
                            case 11:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":       " + GiftCardPay));
                                break;
                            case 12:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":      " + GiftCardPay));
                                break;
                            case 13:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":     " + GiftCardPay));
                                break;
                            case 14:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":    " + GiftCardPay));
                                break;
                            case 15:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":   " + GiftCardPay));
                                break;
                            case 16:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":  " + GiftCardPay));
                                break;
                            case 17:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ": " + GiftCardPay));
                                break;
                            case 18:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet") + ":" + GiftCardPay));
                                break;
                        }

                        escCmd.append(escCmd.getLFCRCmd());
                    }
                }


                if (DiscountPrice != null) {
                    if (!(DiscountPrice.equalsIgnoreCase("") || DiscountPrice.equalsIgnoreCase("null") || DiscountPrice.equalsIgnoreCase("0.00"))) {
                        TextSetting textSetting4 = new TextSetting();
                        textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                        if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                            DiscountPrice = DiscountPrice.replace(".", ",");
                        }
                        switch (DiscountPrice.length()) {
                            case 2:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":              " + Currency + DiscountPrice));
                                break;
                            case 3:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":             " + Currency + DiscountPrice));
                                break;
                            case 4:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":                    " + Currency + DiscountPrice));
                                break;
                            case 5:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":           " + Currency + DiscountPrice));
                                break;
                            case 6:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":          " + Currency + DiscountPrice));
                                break;
                            case 7:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":         " + Currency + DiscountPrice));
                                break;
                            case 8:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":        " + Currency + DiscountPrice));
                                break;
                            case 9:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":       " + Currency + DiscountPrice));
                                break;
                            case 10:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":      " + Currency + DiscountPrice));
                                break;
                            case 11:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":     " + Currency + DiscountPrice));
                                break;
                            case 12:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":    " + Currency + DiscountPrice));
                                break;
                            case 13:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":   " + Currency + DiscountPrice));
                                break;
                            case 14:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":  " + Currency + DiscountPrice));
                                break;
                            case 15:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ": " + Currency + DiscountPrice));
                                break;
                            case 16:
                                escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount") + ":" + Currency + DiscountPrice));
                                break;
                        }

                        escCmd.append(escCmd.getLFCRCmd());
                    }


                    if (CouponPrice != null) {
                        if (!(CouponPrice.equalsIgnoreCase("") || CouponPrice.equalsIgnoreCase("null") || CouponPrice.equalsIgnoreCase("0.00"))) {
                            TextSetting textSetting4 = new TextSetting();
                            textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                            if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                                CouponPrice = CouponPrice.replace(".", ",");
                            }
                            String total_coupon_discount = parseLanguage.getParseString("Total_Coupon_Discount");
                            switch (CouponPrice.length()) {
                                case 2:
                                    escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount + ":       " + Currency + CouponPrice));
                                    break;
                                case 3:
                                    escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount + ":      " + Currency + CouponPrice));
                                    break;
                                case 4:
                                    escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount + ":     " + Currency + CouponPrice));
                                    break;
                                case 5:
                                    escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount + ":    " + Currency + CouponPrice));
                                    break;
                                case 6:
                                    escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount + ":   " + Currency + CouponPrice));
                                    break;
                                case 7:
                                    escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount + ":  " + Currency + CouponPrice));
                                    break;
                                case 8:
                                    escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount + ": " + Currency + CouponPrice));
                                    break;
                                case 9:
                                    escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount + ":" + Currency + CouponPrice));
                                    break;
                            }

                            escCmd.append(escCmd.getLFCRCmd());
                        }
                    }

                }


//            TextSetting textSetting4 = new TextSetting();
//            textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
//            String name=parseLanguage.getParseString("Sub_Total");
//            switch (subTotal.length()) {
//
//                case 2:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":               " + Currency + subTotal));
//                     break;
//                case 3:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":              " + Currency + subTotal));
//                    break;
//                case 4:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":               " + Currency + subTotal));
//                    break;
//                case 5:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":              " + Currency + subTotal));
//                    break;
//                case 6:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":             " + Currency + subTotal));
//                    break;
//                case 7:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 8:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":           " + Currency + subTotal));
//                    break;
//                case 9:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":           " + Currency + subTotal));
//                    break;
//                case 10:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":           " + Currency + subTotal));
//                    break;
//                case 11:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 12:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 13:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 14:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 15:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 16:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":           " + Currency + subTotal));
//                    break;
//                case 17:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 18:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":           " + Currency + subTotal));
//                    break;
//                case 19:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 20:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//                case 21:
//                    escCmd.append(escCmd.getTextCmd(textSetting4, name+":            " + Currency + subTotal));
//                    break;
//            }
//            escCmd.append(escCmd.getLFCRCmd());


                if (!(extraTipAddAmount.equals("") || extraTipAddAmount.equals(null) || extraTipAddAmount.equals("Null") || extraTipAddAmount.equals("null") || extraTipAddAmount.equals("0.00"))) {
                    TextSetting textextratip = new TextSetting();
                    textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                    if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                        extraTipAddAmount = extraTipAddAmount.replace(".", ",");
                    }
                    String rider_tip = parseLanguage.getParseString("Rider_Tip");
                    switch (extraTipAddAmount.length()) {
                        case 2:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 3:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 4:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 13:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 14:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 15:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 16:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 17:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 18:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 19:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                        case 20:
                            escCmd.append(escCmd.getTextCmd(textextratip, rider_tip + ":                  " + Currency + extraTipAddAmount));
                            break;
                    }

                    escCmd.append(escCmd.getLFCRCmd());
                }
                if (!(DeliveryCharge.equals("") || DeliveryCharge.equals(null) || DeliveryCharge.equals("Null") || DeliveryCharge.equals("null") || DeliveryCharge.equals("0.00"))) {
                    TextSetting textextratip = new TextSetting();
                    textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                    if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                        DeliveryCharge = DeliveryCharge.replace(".", ",");
                    }
                    String delivery_charge = parseLanguage.getParseString("Delivery_Charge");
                    switch (DeliveryCharge.length()) {
                        case 2:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 3:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 4:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 13:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 14:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                        case 15:
                            escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge + ":             " + Currency + DeliveryCharge));
                            break;
                    }

                    escCmd.append(escCmd.getLFCRCmd());
                }


                if (!(ServiceFees.equals("") || ServiceFees.equals(null) || ServiceFees.equals("Null") || ServiceFees.equals("null") || ServiceFees.equals("0.00"))) {
                    TextSetting textextratip = new TextSetting();
                    textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                    String service_cost = parseLanguage.getParseString("Service_Cost");
                    if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                        ServiceFees = ServiceFees.replace(".", ",");
                    }
                    switch (ServiceFees.length()) {
                        case 2:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":              " + Currency + ServiceFees));
                            break;
                        case 3:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":             " + Currency + ServiceFees));
                            break;
                        case 4:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":            " + Currency + ServiceFees));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":           " + Currency + ServiceFees));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":          " + Currency + ServiceFees));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":         " + Currency + ServiceFees));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":        " + Currency + ServiceFees));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":       " + Currency + ServiceFees));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":      " + Currency + ServiceFees));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":     " + Currency + ServiceFees));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":    " + Currency + ServiceFees));
                            break;
                        case 13:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":   " + Currency + ServiceFees));
                            break;
                        case 14:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":  " + Currency + ServiceFees));
                            break;
                        case 15:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ": " + Currency + ServiceFees));
                            break;
                        case 16:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_cost + ":" + Currency + ServiceFees));
                            break;
                    }

                    escCmd.append(escCmd.getLFCRCmd());
                }
                if (!(PackageFees.equals("") || PackageFees.equals(null) || PackageFees.equals("Null") || PackageFees.equals("null") || PackageFees.equals("0.00"))) {
                    TextSetting textextratip = new TextSetting();
                    textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                    if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                        PackageFees = PackageFees.replace(".", ",");
                    }
                    String packaging_charge = parseLanguage.getParseString("Packaging_Cost");
                    switch (PackageFees.length()) {
                        case 2:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":              " + Currency + PackageFees));
                            break;
                        case 3:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":             " + Currency + PackageFees));
                            break;
                        case 4:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":            " + Currency + PackageFees));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":           " + Currency + PackageFees));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":          " + Currency + PackageFees));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":         " + Currency + PackageFees));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":        " + Currency + PackageFees));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":       " + Currency + PackageFees));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":      " + Currency + PackageFees));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":     " + Currency + PackageFees));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":    " + Currency + PackageFees));
                            break;
                        case 13:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":   " + Currency + PackageFees));
                            break;
                        case 14:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":  " + Currency + PackageFees));
                            break;
                        case 15:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ": " + Currency + PackageFees));
                            break;
                        case 16:
                            escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge + ":" + Currency + PackageFees));
                            break;
                    }


                }
                if (!(SalesTaxAmount.equals("") || SalesTaxAmount.equals(null) || SalesTaxAmount.equals("Null") || SalesTaxAmount.equals("null") || SalesTaxAmount.equals("0.00"))) {
                    TextSetting textextratip = new TextSetting();
                    textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                    String service_tax = parseLanguage.getParseString("Service_Tax");
                    if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                        SalesTaxAmount = SalesTaxAmount.replace(".", ",");
                    }
                    switch (SalesTaxAmount.length()) {
                        case 2:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":                 " + Currency + SalesTaxAmount));
                            break;
                        case 3:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":                " + Currency + SalesTaxAmount));
                            break;
                        case 4:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":               " + Currency + SalesTaxAmount));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":              " + Currency + SalesTaxAmount));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":             " + Currency + SalesTaxAmount));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":            " + Currency + SalesTaxAmount));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":           " + Currency + SalesTaxAmount));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":          " + Currency + SalesTaxAmount));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":         " + Currency + SalesTaxAmount));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":        " + Currency + SalesTaxAmount));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":       " + Currency + SalesTaxAmount));
                            break;
                        case 13:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":      " + Currency + SalesTaxAmount));
                            break;
                        case 14:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":     " + Currency + SalesTaxAmount));
                            break;
                        case 15:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":    " + Currency + SalesTaxAmount));
                            break;
                        case 16:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":   " + Currency + SalesTaxAmount));
                            break;
                        case 17:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":  " + Currency + SalesTaxAmount));
                            break;
                        case 18:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ": " + Currency + SalesTaxAmount));
                            break;
                        case 19:
                            escCmd.append(escCmd.getTextCmd(textextratip, service_tax + ":" + Currency + SalesTaxAmount));
                            break;
                    }

                    escCmd.append(escCmd.getLFCRCmd());
                }
//            if (!(VatTax.equals("") || VatTax.equals(null) || VatTax.equals("Null") || VatTax.equals("null") || VatTax.equals("0.00"))) {
//                TextSetting textextratip = new TextSetting();
//                textextratip.setAlign(CommonEnum.ALIGN_LEFT);
//                String vat = parseLanguage.getParseString("VAT_Tax");
//                switch (VatTax.length()) {
//                    case 2:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                         " + Currency + VatTax));
//                        break;
//                    case 3:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                        " + Currency + VatTax));
//                        break;
//                    case 4:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                       " + Currency + VatTax));
//                        break;
//                    case 5:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                      " + Currency + VatTax));
//                        break;
//                    case 6:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                     " + Currency + VatTax));
//                        break;
//                    case 7:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                    " + Currency + VatTax));
//                        break;
//                    case 8:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                   " + Currency + VatTax));
//                        break;
//                    case 9:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                  " + Currency + VatTax));
//                        break;
//                    case 10:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                 " + Currency + VatTax));
//                        break;
//                    case 11:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":                " + Currency + VatTax));
//                        break;
//                    case 12:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":               " + Currency + VatTax));
//                        break;
//                    case 13:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":              " + Currency + VatTax));
//                        break;
//                    case 14:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":             " + Currency + VatTax));
//                        break;
//                    case 15:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":            " + Currency + VatTax));
//                        break;
//                    case 16:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":           " + Currency + VatTax));
//                        break;
//                    case 17:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":          " + Currency + VatTax));
//                        break;
//                    case 18:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":         " + Currency + VatTax));
//                        break;
//                    case 19:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":        " + Currency + VatTax));
//                        break;
//                    case 20:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":       " + Currency + VatTax));
//                        break;
//                    case 21:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":      " + Currency + VatTax));
//                        break;
//                    case 22:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":     " + Currency + VatTax));
//                        break;
//                    case 23:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":    " + Currency + VatTax));
//                        break;
//                    case 24:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":   " + Currency + VatTax));
//                        break;
//                    case 25:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":  " + Currency + VatTax));
//                        break;
//                    case 26:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+": " + Currency + VatTax));
//                        break;
//                    case 27:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":" + Currency + VatTax));
//                        break;
//                    case 28:
//                        escCmd.append(escCmd.getTextCmd(textextratip, vat+":" + Currency + VatTax));
//                        break;
//                }
//
//                escCmd.append(escCmd.getLFCRCmd());
//
//            }
                if (getFoodTaxTotal7 != null) {
                    if (!(getFoodTaxTotal7.equalsIgnoreCase("") || getFoodTaxTotal7.equalsIgnoreCase("null") || getFoodTaxTotal7.equalsIgnoreCase("0.00"))) {
                        TextSetting textextratip = new TextSetting();
                        textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                        String drink_tax = parseLanguage.getParseString("Inkl_MwSt_7");
                        switch (getFoodTaxTotal7.length()) {
                            case 2:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":               " + getFoodTaxTotal7));
                            case 3:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":              " + getFoodTaxTotal7));
                            case 4:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":                " + getFoodTaxTotal7));
                            case 5:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":               " + getFoodTaxTotal7));
                            case 6:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":            " + getFoodTaxTotal7));
                            case 7:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":           " + getFoodTaxTotal7));
                            case 8:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":          " + getFoodTaxTotal7));
                            case 9:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":         " + getFoodTaxTotal7));
                            case 10:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":        " + getFoodTaxTotal7));
                            case 11:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":        " + getFoodTaxTotal7));
                            case 12:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":       " + getFoodTaxTotal7));
                            case 13:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":      " + getFoodTaxTotal7));
                            case 14:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":     " + getFoodTaxTotal7));
                            case 15:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":    " + getFoodTaxTotal7));
                            case 16:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":   " + getFoodTaxTotal7));
                            case 17:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":  " + getFoodTaxTotal7));
                            case 18:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ": " + getFoodTaxTotal7));
                            case 19:
                                escCmd.append(escCmd.getTextCmd(textextratip, drink_tax + ":" + getFoodTaxTotal7));
                        }

                        escCmd.append(escCmd.getLFCRCmd());
                    }
                }
                if (getFoodTaxTotal19 != null) {
                    if ((getFoodTaxTotal19.equalsIgnoreCase("") || getFoodTaxTotal19.equalsIgnoreCase("null") || getFoodTaxTotal19.equalsIgnoreCase("0.00"))) {
                        TextSetting textextratip = new TextSetting();
                        textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                        String food_tax = parseLanguage.getParseString("Inkl_MwSt_19");
                        switch (getFoodTaxTotal19.length()) {
                            case 2:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":                    " + getFoodTaxTotal19));
                                break;
                            case 3:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":                   " + getFoodTaxTotal19));
                                break;
                            case 4:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":                  " + getFoodTaxTotal19));
                                break;
                            case 5:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":                 " + getFoodTaxTotal19));
                                break;
                            case 6:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":                " + getFoodTaxTotal19));
                                break;
                            case 7:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":               " + getFoodTaxTotal19));
                                break;
                            case 8:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":              " + getFoodTaxTotal19));
                                break;
                            case 9:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":             " + getFoodTaxTotal19));
                                break;
                            case 10:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":           " + getFoodTaxTotal19));
                                break;
                            case 11:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":          " + getFoodTaxTotal19));
                                break;
                            case 12:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":         " + getFoodTaxTotal19));
                                break;
                            case 13:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":        " + getFoodTaxTotal19));
                                break;
                            case 14:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":       " + getFoodTaxTotal19));
                                break;
                            case 15:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":      " + getFoodTaxTotal19));
                                break;
                            case 16:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":     " + getFoodTaxTotal19));
                                break;
                            case 17:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":    " + getFoodTaxTotal19));
                                break;
                            case 18:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":   " + getFoodTaxTotal19));
                                break;
                            case 19:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":  " + getFoodTaxTotal19));
                                break;
                            case 20:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ": " + getFoodTaxTotal19));
                                break;
                            case 21:
                                escCmd.append(escCmd.getTextCmd(textextratip, food_tax + ":" + getFoodTaxTotal19));
                                break;
                        }

                        escCmd.append(escCmd.getLFCRCmd());

                    }
                }


                TextSetting textSetting5 = new TextSetting();
                textSetting5.setAlign(CommonEnum.ALIGN_LEFT);
                textSetting5.setBold(SettingEnum.Enable);
                String total = parseLanguage.getParseString("Total");
                if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                    OrderPrice = OrderPrice.replace(".", ",");
                }
                switch (OrderPrice.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":                  " + Currency + OrderPrice));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":                " + Currency + OrderPrice));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":                " + Currency + OrderPrice));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":                 " + Currency + OrderPrice));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":                " + Currency + OrderPrice));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":               " + Currency + OrderPrice));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":               " + Currency + OrderPrice));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":              " + Currency + OrderPrice));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":             " + Currency + OrderPrice));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":              " + Currency + OrderPrice));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":             " + Currency + OrderPrice));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":            " + Currency + OrderPrice));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":           " + Currency + OrderPrice));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":          " + Currency + OrderPrice));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":         " + Currency + OrderPrice));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":        " + Currency + OrderPrice));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":       " + Currency + OrderPrice));
                        break;
                    case 19:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":      " + Currency + OrderPrice));
                        break;
                    case 20:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":     " + Currency + OrderPrice));
                        break;
                    case 21:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":    " + Currency + OrderPrice));
                        break;
                    case 22:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":   " + Currency + OrderPrice));
                        break;
                    case 23:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":  " + Currency + OrderPrice));
                        break;
                    case 24:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ": " + Currency + OrderPrice));
                        break;
                    case 25:
                        escCmd.append(escCmd.getTextCmd(textSetting5, total + ":" + Currency + OrderPrice));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());
                if (TotalSavedDiscount != null) {

                    TextSetting textSetting53 = new TextSetting();
                    textSetting53.setAlign(CommonEnum.ALIGN_LEFT);
                    String total_saved = parseLanguage.getParseString("Total_Saved");
                    if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                        TotalSavedDiscount = TotalSavedDiscount.replace(".", ",");
                    }
                    switch (TotalSavedDiscount.length()) {
                        case 1:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":     " + Currency + TotalSavedDiscount));
                            break;
                        case 2:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":    " + Currency + TotalSavedDiscount));
                            break;
                        case 3:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":    " + Currency + TotalSavedDiscount));
                            break;
                        case 4:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":    " + Currency + TotalSavedDiscount));
                            break;
                        case 5:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":   " + Currency + TotalSavedDiscount));
                            break;
                        case 6:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":   " + Currency + TotalSavedDiscount));
                            break;
                        case 7:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":   " + Currency + TotalSavedDiscount));
                            break;
                        case 8:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":    " + Currency + TotalSavedDiscount));
                            break;
                        case 9:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":    " + Currency + TotalSavedDiscount));
                            break;
                        case 10:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":    " + Currency + TotalSavedDiscount));
                            break;
                        case 11:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":   " + Currency + TotalSavedDiscount));
                            break;
                        case 12:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":   " + Currency + TotalSavedDiscount));
                            break;
                        case 13:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":  " + Currency + TotalSavedDiscount));
                            break;
                        case 14:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":  " + Currency + TotalSavedDiscount));
                            break;
                        case 15:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ": " + Currency + TotalSavedDiscount));
                            break;
                        case 16:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ": " + Currency + TotalSavedDiscount));
                            break;
                        case 17:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":  " + Currency + TotalSavedDiscount));
                            break;
                        case 18:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":   " + Currency + TotalSavedDiscount));
                            break;
                        case 19:
                            escCmd.append(escCmd.getTextCmd(textSetting53, total_saved + ":" + Currency + TotalSavedDiscount));
                            break;
                    }
                }

                escCmd.append(escCmd.getLFCRCmd());


                escCmd.append(escCmd.getHeaderCmd());
                escCmd.append(escCmd.getLFCRCmd());
                escCmd.append(escCmd.getLFCRCmd());
                escCmd.append(escCmd.getLFCRCmd());
                escCmd.append(escCmd.getLFCRCmd());

//            System.out.print(escCmd);
                Log.i("TAGPrintReceipt", "PrintOrderReceipt: " + escCmd);
                if (escCmd.getAppendCmds() != null) {

                    rtPrinter.writeMsg(escCmd.getAppendCmds());


                }
//            if(auto_print_enable.equalsIgnoreCase("1")){
//                setAutoPrintOff(orderid,"2");
//            }

//
            }

    }
    AppCompatDialog dialog;
    private String sCity = "", stime = "", sDate = "";
    String[] aa = {" 30 ", " 45 ", " 60 ", " 75 ", " 90 ", " 105 "};
    ArrayList<TimeModel> stringList;
    public void ShowOrderDialog(String BookingID,String order_date, String customer_name,String ordPrice,String payment_mode,String orderid){
        dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.order_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setCancelable(false);
        ImageView back = dialog.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                player.stop();
                myPref.setDocCode("1");
            }
        });
        final RecyclerView time = dialog.findViewById(R.id.time);
        stringList = new ArrayList<TimeModel>();
        stringList.clear();
        for (int i = 0; i < aa.length; i++) {
            TimeModel sTmodel = new TimeModel();
            sTmodel.setTime(aa[i]);
            sTmodel.setSelectCategory("0");
            stringList.add(sTmodel);
        }
        time.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        final TimeAdapter2 timeAdapter2 = new TimeAdapter2(this, stringList, new TimeValuesListener() {
            @Override
            public void onSelectTime(String price) {
                if(!myPref.getAuto_print_enable().equalsIgnoreCase("1")){
                    try {
                        PrintOrderReceipt(orderid,myPref.getAuto_print_enable());
                        dialog.cancel();
                        dialog.dismiss();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                stime = price;

            }
        });

        time.setAdapter(timeAdapter2);

        TextView tv_accpetdate = dialog.findViewById(R.id.tv_accpetdate);
        TextView tv_price = dialog.findViewById(R.id.tv_price);

        TextView name = dialog.findViewById(R.id.name);
        TextView tv_address = dialog.findViewById(R.id.tv_address);
        TextView tv_payment_type = dialog.findViewById(R.id.tv_payment_type);
        TextView order_id = dialog.findViewById(R.id.order_id);
        TextView order_id_text = dialog.findViewById(R.id.order_id_text);
        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView accept = dialog.findViewById(R.id.accept);

        ImageView forward = dialog.findViewById(R.id.forward);
        ImageView rewind = dialog.findViewById(R.id.rewind);

        accept.setText(parseLanguage.getParseString("AcceptButton"));
        cancel.setText(parseLanguage.getParseString("DeclineButton"));
        if (myPref.getCustomer_default_langauge().equals("en")) {
            order_id_text.setText("#" + BookingID);
        } else {
            order_id_text.setText("#" + BookingID);
        }
        order_id.setText(" #" + BookingID);
        tv_accpetdate.setText(order_date);
        name.setText(customer_name);
        tv_address.setText(restaurant_address);
        tv_price.setText(Activity_Splash.currency_symbol + ordPrice);
        tv_payment_type.setText(payment_mode);
        final CountDownTimer countDownTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {

                if (!player.isPlaying() && dialog != null) {
                    player.start();
                }
            }

            public void onFinish() {
                player.stop();
                // DO something when 1 minute is up
            }
        }.start();

        final String finalOrderid = orderid;
        accept.setVisibility(View.GONE);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stime.equals("")) {
                    Toast.makeText(MainActivity.this, parseLanguage.getParseString("Please_Select_Time"), Toast.LENGTH_SHORT).show();
                } else {
                    countDownTimer.cancel();
                    //  player.stop();
                    player.stop();
                    orderconfirm(finalOrderid);
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDeclineDialog(countDownTimer,finalOrderid);
            }
        });
        flag = 0;
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                --flag;
                time.smoothScrollToPosition(flag);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag++;
                time.smoothScrollToPosition(((timeAdapter2.getItemCount() * flag) / 2) + 1);
            }
        });
        dialog.show();

    }
    public void Decline(final String a, final String order_id, AlertDialog aa) {
        progressDialog = progressDialog.show(this, "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.decline, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                aa.dismiss();
                progressDialog.dismiss();
                Log.e("response", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error = jsonObject.getInt("error");
                    if (error == 0) {
                        String success_msg = jsonObject.getString("error_msg");
                        showCustomDialogdecline(success_msg,1);


                    } else {
                        String success_msg = jsonObject.getString("error_msg");
                        showCustomDialogdecline(success_msg,0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                aa.dismiss();
                Log.e("error", "" + volleyError);
                Toast.makeText(MainActivity.this, parseLanguage.getParseString("Please_Check_your_network_connection"), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("orderid", "" + order_id);
                params.put("OrderStatus", "2");
                params.put("DriverComment", a);
                params.put("lang_code", myPref.getCustomer_default_langauge());
//                Log.e("pa",""+params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    private void showCustomDialogdecline(String s, int type) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle(parseLanguage.getParseString("AlertText"));
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton(parseLanguage.getParseString("OKText"), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch(type){
                    case 0:
                       Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                }



            }
        });
        alertDialog.show();

    }

    public void ShowDeclineDialog(CountDownTimer countDownTimer, String finalOrderid1){
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setMessage(parseLanguage.getParseString("Are_you_sure_to_decline_order"));
        alertDialog.setPositiveButton(parseLanguage.getParseString("YESText"), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog1, int which) {
                countDownTimer.cancel();
                dialog.dismiss();
                dialog1.dismiss();

                // Write your code here to invoke YES event
//                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
AlertDialog alert;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogLayout = inflater.inflate(R.layout.custom_alertdialog, null);
                    builder.setView(dialogLayout);
                    final AlertDialog aa = builder.create();
                    final EditText edt_reason = (EditText) dialogLayout.findViewById(R.id.edt_reason);
                    TextView ordernumber = dialogLayout.findViewById(R.id.ordernumber);
                    Button submit_reason = (Button) dialogLayout.findViewById(R.id.submit_reason);
                    Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
                    submit_reason.setText(parseLanguage.getParseString("Submit"));
                    if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                        edt_reason.setHint(getString(R.string.write_notes_customer));
                        ordernumber.setText(getString(R.string.decline_reason));
                        submit_reason.setText(getString(R.string.submit_german));
                    }
                    submit_reason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (player.isPlaying() == true) {
                                player.stop();
                            } else {

                            }
                            Decline("" + edt_reason.getText().toString(), finalOrderid1,aa);
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                                    finish();
                            player.start();

                            countDownTimer.cancel();
                            aa.dismiss();

                        }
                    });
                    aa.show();


                }



        });
        alertDialog.setNegativeButton(parseLanguage.getParseString("NOText"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });


        // Showing Alert Message
        alertDialog.show();

    }
    public void orderconfirm(final String Order_id) {
        progressDialog = progressDialog.show(this, "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.order_Accept, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error = jsonObject.getInt("error");
                    if (error == 0) {
                        String error_msg = jsonObject.getString("error_msg");
                        try {
                            dialog.dismiss();
                            dialog.cancel();

//                            getorderdetails(Order_id, error_msg);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
//                        Activity_Booking activity_booking=new Activity_Booking();
//                        activity_booking.doConnect();


//                        Intent intent = new Intent(AcceptButton_activity.this,Activity_Booking.class);
//                        intent.putExtra("orderid",""+getIntent().getStringExtra("orderid"));
//                        startActivity(intent);

                    } else {
                        String error_msg = jsonObject.getString("error_msg");
//                        Toast.makeText(AcceptButton_activity.this, error_msg, Toast.LENGTH_SHORT).show();
                        //   finish();
                        showCustomDialog1(error_msg);
//                        finish();
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
                String message = "";
                if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                    message = getString(R.string.check_internet);
                } else {
                    message = "Please Check your network connection";
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", "" + Order_id);
                params.put("collectionTime", stime);
                params.put("DriverComment", "No Comments");
                params.put("DriverID", "0");
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("qwqw", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void showCustomDialog1(String s) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle(parseLanguage.getParseString("AlertText"));
        if (!s.equalsIgnoreCase("null")) {
            alertDialog.setMessage("" + s);
        }

        alertDialog.setIcon(R.drawable.cancel);

        alertDialog.setButton(parseLanguage.getParseString("OKText"), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

//                Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private ArrayList<Model_OrderComboItemExtra> prepareDataForExtraTopping(String extraTopping) {

        ArrayList<Model_OrderComboItemExtra> model_orderComboItemExtras = new ArrayList<>();
        try {

            JSONArray orderComboItemOption1 = new JSONArray(extraTopping);

     /*       JSONObject json = (JSONObject) parser.parse(extraTopping);
            JSONArray orderComboItemOption1 = json.getJSONArray("OrderComboItemExtra");*/

            for (int i2 = 0; i2 < orderComboItemOption1.length(); i2++) {
                Model_OrderComboItemExtra model_orderComboItemExtra = new Model_OrderComboItemExtra();
                JSONObject jsonObject2 = orderComboItemOption1.getJSONObject(i2);
                String comboExtraItemName = jsonObject2.getString("ComboExtraItemName");
                String comboExtraItemQuantity = jsonObject2.getString("ComboExtraItemQuantity");
                String comboExtraItemPrice = jsonObject2.getString("ComboExtraItemPrice");
                model_orderComboItemExtra.setComboExtraItemName(comboExtraItemName);
                model_orderComboItemExtra.setComboExtraItemPrice(comboExtraItemPrice);
                model_orderComboItemExtra.setComboExtraItemQuantity(comboExtraItemQuantity);
                model_orderComboItemExtras.add(model_orderComboItemExtra);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model_orderComboItemExtras;

    }

    public void init() {

        textSetting = new TextSetting();

        //Initialize Thermalprinter
        BaseApplication.instance.setCurrentCmdType(BaseEnum.CMD_ESC);
        printerFactory = new ThermalPrinterFactory();
        rtPrinter = printerFactory.create();

        PrinterObserverManager.getInstance().add(this);//添加连接状态监听

        configObj = new SerialPortConfigBean().getDefaultConfig();
        printerPowerUtil = new PrinterPowerUtil(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);

    }
    private void handleIntent(Intent intent){
        if(intent.hasExtra("type")){
            String type=intent.getStringExtra("type");
            String orderId=intent.getStringExtra("orderId");
            if(type.equalsIgnoreCase("from_notification")){
                int id=intent.getIntExtra("notification_id",0);
                init();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(id);
                getorderdetails(orderId,"1");

            }
        }
    }

}