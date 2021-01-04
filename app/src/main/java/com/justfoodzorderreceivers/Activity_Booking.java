 package com.justfoodzorderreceivers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.printer.PrintTaskCallback;
import com.android.printer.Printer;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodzorderreceivers.Model.FoodItemList;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Activity_Booking extends AppCompatActivity implements PrinterObserver {

    ArrayList<Model_Combo> model_combos;
    private TextSetting textSetting;
    private String mChartsetName = "UTF-8";
    TextView name, tv_number, tv_delivery, tv_address, tv_payment_type, tv_deliveryon, tv_asap, tvDilerveryinstructions, ordernumber,
            deleiveryaddress, tv_no_foodItems, steak2, regardpoint, gifcardprice, tv_Servicecost, tv_servicetax_price, paybywallet,
            totaldiscount_price, tv_instructions, tv_accpetdate,
            drivertip, food_costprice, tv_discount, tv_subtotal, tv_deliveryfee, tv_packagingfee,
            vat, total, tv_ready, dyiningtale, tv_no_drinkItems, tv_meal;
    RelativeLayout rl_ins;
    ProgressDialog progressDialog;

    RequestQueue requestQueue;
    RecyclerView recycler_fooditem, recycler_drinktem, rcmeal;
    ArrayList<FoodItemList> drinkItemLists, meallist;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1, linearLayoutManager2;
    Button btn_decline, btn_accept;
    ImageView back, phonecall;
    // LinearLayout  takeprint1;
    Button btn_change_orderstatus, btn_track_order, btn_markComplete, takeprint, orderclosed, btn_status;
    public static String phonenumber = "";
    RelativeLayout print, printwithsmall, printwithbig;
    CardView btn_statusCard;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    String Innerprinter_Address = "00:11:22:33:44:55";
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    RelativeLayout rl_foodcost, rl_totol_discount, rl_redeem, rl_paywallet, rl_giftpay, rl_subtotal, rl_deleiverycharge, rl_servicecost, rl_packgingcost, rl_servicetax, rl_vattax, rl_ridertrip, rl_total;

    String rider_idAssign;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    AuthPreference authPreference;
    public static int orderidd;

    TextView back_text;
    MyPref myPref;
    ParseLanguage parseLanguage;

    Printer mPrinter;
    public static ArrayList<String> item_size, item_name, item_price, item_quant, extra_toping,
            drink_item_name, drink_item_price, drink_item_quant, drink_extra_toping,
            meal_item_name, meal_item_price, meal_item_quant, meal_extra_toping, item_instruction, drink_instruction, meal_instruction;

    public static String restaurant_mobile_number, customer_email, status, PaymentMethod, name_customer, customer_phone, OrderType, customer_address, customer_instruction,
            number_of_items_order, subTotal, DeliveryCharge, PackageFees, FoodCosts, DiscountPrice, VatTax, CouponPrice, getFoodTaxTotal7, getFoodTaxTotal19,
            OrderPrice, PayByLoyality, GiftCardPay, ServiceFees, extraTipAddAmount, WalletPay, SalesTaxAmount,
            RequestAtDate, RequestAtTime, OrderAcceptedDate, OrderAcceptedTime, order_status_color_code,
            order_reference_number, collectionTime, Table_Booking_Number, DriverFirstName, DriverLastName,
            DriverMobileNo, DriverPhoto, rider_id, PayOptionStatus, restaurant_name, restaurant_address, TotalSavedDiscount,
            website_copy_right_text, instruction_note, order_confirmed_time, order_kitchen_time, order_delivery_time, number_of_customer_order, discountOfferFreeItems;

    TextView printText, summaryText;

    String Currency = Activity_Splash.currency_symbol;
    @BaseEnum.ConnectType
    private int checkedConType = BaseEnum.CON_COM;
    private RTPrinter rtPrinter = null;
    private PrinterFactory printerFactory;
    private Object configObj;
    private PrinterPowerUtil printerPowerUtil;//To switch AP02 printer power.


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this, "backpressed ", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Activity_Booking.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    TextView food_cost, total_discount, redeem_point, pay_by_wallet, gift_card, sub_total, delivery_charge,
            service_cost, package_cost, service_tax, vat_text, rider_tipt, printTextwithsmall, printTextwithbig;
    private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 20);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail_new);
        init();
        model_combos = new ArrayList<>();
        mPrinter = new Printer();
        rl_foodcost = findViewById(R.id.rl_foodcost);
        rl_totol_discount = findViewById(R.id.rl_totol_discount);
        rl_redeem = findViewById(R.id.rl_redeem);
        rl_paywallet = findViewById(R.id.rl_paywallet);
        rl_giftpay = findViewById(R.id.rl_giftpay);
        rl_subtotal = findViewById(R.id.rl_subtotal);
        rl_deleiverycharge = findViewById(R.id.rl_deleiverycharge);
        rl_servicecost = (RelativeLayout) findViewById(R.id.rl_servicecost);
        rl_packgingcost = (RelativeLayout) findViewById(R.id.rl_packgingcost);
        rl_servicetax = (RelativeLayout) findViewById(R.id.rl_servicetax);
        rl_vattax = (RelativeLayout) findViewById(R.id.rl_vattax);
        rl_ridertrip = (RelativeLayout) findViewById(R.id.rl_ridertrip);
        rl_total = (RelativeLayout) findViewById(R.id.rl_total);
        rl_ins = findViewById(R.id.rl_ins);
        printTextwithsmall=findViewById(R.id.printTextwithsmall);
        printTextwithbig=findViewById(R.id.printTextwithbig);
        //  ready = (RelativeLayout) findViewById(R.id.ready);

        requestQueue = Volley.newRequestQueue(this);
        recycler_fooditem = (RecyclerView) findViewById(R.id.recycler_fooditem);
        recycler_drinktem = (RecyclerView) findViewById(R.id.recycler_drinktem);


        myPref = new MyPref(Activity_Booking.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(), Activity_Booking.this);

        rcmeal = (RecyclerView) findViewById(R.id.rcmeal);

        drinkItemLists = new ArrayList<>();
        meallist = new ArrayList<>();

        printText = findViewById(R.id.printText);
        summaryText = findViewById(R.id.summaryText);


        food_cost = findViewById(R.id.food_cost);
        total_discount = findViewById(R.id.total_discount);
        redeem_point = findViewById(R.id.redeem_point);
        pay_by_wallet = findViewById(R.id.pay_by_wallet);
        gift_card = findViewById(R.id.gift_card);
        sub_total = findViewById(R.id.sub_total);
        delivery_charge = findViewById(R.id.delivery_charge);
        service_cost = findViewById(R.id.service_cost);
        package_cost = findViewById(R.id.package_cost);
        service_tax = findViewById(R.id.service_tax);
        vat_text = findViewById(R.id.vat_text);
        rider_tipt = findViewById(R.id.rider_tip);

        food_cost.setText(parseLanguage.getParseString("Food_Cost"));
        total_discount.setText(parseLanguage.getParseString("Total_Discount"));
        redeem_point.setText(parseLanguage.getParseString("Regard_Redeem_Point"));
        pay_by_wallet.setText(parseLanguage.getParseString("Pay_By_Wallet"));
        gift_card.setText(parseLanguage.getParseString("Gift_Card_Pay"));
        sub_total.setText(parseLanguage.getParseString("Sub_Total"));
        delivery_charge.setText(parseLanguage.getParseString("Delivery_Charge"));
        service_cost.setText(parseLanguage.getParseString("Packaging_Cost"));
        package_cost.setText(parseLanguage.getParseString("Package_Cost"));
        service_tax.setText(parseLanguage.getParseString("Service_Tax"));
        vat_text.setText(parseLanguage.getParseString("VAT_Tax"));
        rider_tipt.setText(parseLanguage.getParseString("Rider_Tip"));


        item_size = new ArrayList<>();
        item_name = new ArrayList<>();
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
        item_instruction = new ArrayList<>();
        drink_instruction = new ArrayList<>();
        meal_instruction = new ArrayList<>();


        back = (ImageView) findViewById(R.id.back);
        //    phonecall = (ImageView) findViewById(R.id.tv_number);
        name = (TextView) findViewById(R.id.name);
        tv_ready = (TextView) findViewById(R.id.tv_ready);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_delivery = (TextView) findViewById(R.id.tv_ordertype);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_no_drinkItems = (TextView) findViewById(R.id.tv_no_drinkItems);
        tv_payment_type = (TextView) findViewById(R.id.tv_payment_type);
        tv_deliveryon = (TextView) findViewById(R.id.tv_deliveryon);
        tv_instructions = (TextView) findViewById(R.id.tv_instructions);
        tvDilerveryinstructions = (TextView) findViewById(R.id.tvDilerveryinstructions);
        deleiveryaddress = (TextView) findViewById(R.id.deleiveryaddress);
        steak2 = (TextView) findViewById(R.id.steak2);
        food_costprice = (TextView) findViewById(R.id.food_costprice);
        tv_servicetax_price = (TextView) findViewById(R.id.tv_servicetax_price);
        paybywallet = (TextView) findViewById(R.id.paybywallet);
        drivertip = (TextView) findViewById(R.id.drivertip);
        tv_accpetdate = (TextView) findViewById(R.id.tv_accpetdate);

        tv_no_foodItems = (TextView) findViewById(R.id.tv_no_foodItems);
        tv_Servicecost = (TextView) findViewById(R.id.tv_Servicecost);
        tv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
        tv_deliveryfee = (TextView) findViewById(R.id.tv_deliveryfee);
        totaldiscount_price = (TextView) findViewById(R.id.totaldiscount_price);
        tv_packagingfee = (TextView) findViewById(R.id.tv_packagingfee);
        vat = (TextView) findViewById(R.id.tv_vat_tax);
        regardpoint = (TextView) findViewById(R.id.regardpoint);
        gifcardprice = (TextView) findViewById(R.id.gifcardprice);
        tv_meal = (TextView) findViewById(R.id.tv_meal);

        total = (TextView) findViewById(R.id.tv_total);
        ordernumber = (TextView) findViewById(R.id.ordernumber);
        dyiningtale = (TextView) findViewById(R.id.dyiningtale);
        btn_status = (Button) findViewById(R.id.btn_status);
        btn_statusCard = findViewById(R.id.btn_statusCard);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_decline = (Button) findViewById(R.id.btn_decline);
        //liner_back = (LinearLayout) findViewById(R.id.liner_back);
        btn_change_orderstatus = (Button) findViewById(R.id.btn_change_orderstatus);
        btn_track_order = (Button) findViewById(R.id.btn_track_order);
        btn_markComplete = (Button) findViewById(R.id.btn_markComplete);
//        takeprint = (Button) findViewById(R.id.takeprint);
        orderclosed = (Button) findViewById(R.id.orderclosed);


        orderclosed.setText(parseLanguage.getParseString("Order_Complete"));
        summaryText.setText(parseLanguage.getParseString("Summary"));
        printText.setText(parseLanguage.getParseString("Take_Print_keyword"));
        btn_accept.setText(parseLanguage.getParseString("AcceptButton"));
        btn_decline.setText(parseLanguage.getParseString("DeclineButton"));
        if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
            total.setText(getString(R.string.total));
        }
        else {
            total.setText("Total");
        }
//        total.setText(parseLanguage.getParseString("Total"));
        btn_markComplete.setText(parseLanguage.getParseString("Mark_As_Completed"));

        //  takeprint1 = (LinearLayout) findViewById(R.id.takeprint);
        print = (RelativeLayout) findViewById(R.id.print);
        printwithsmall = (RelativeLayout) findViewById(R.id.printwithsmall);
        printwithbig = (RelativeLayout) findViewById(R.id.printwithbig);
        String print_small=parseLanguage.getParseString("Take_print_small");
        String print_big= parseLanguage.getParseString("Take_print_big");
        if(print_small.equalsIgnoreCase("No Response")){
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                printTextwithsmall.setText(getString(R.string.take_print_small));
            }
        }
        else {
            printTextwithsmall.setText(print_small);
        }
        if(print_big.equalsIgnoreCase("No Response")){
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                printTextwithbig.setText(getString(R.string.big_print));
            }
        }
        else {
            printTextwithbig.setText(print_big);
        }

        // rl_dyingtable = (RelativeLayout) findViewById(R.id.rl_dyingtable);
        authPreference = new AuthPreference(this);
        printwithbig.setVisibility(View.GONE);


        printwithbig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*    findBT();
                    openBT();
                    sendDataWithSmallPrinter();*/
                Toast.makeText(Activity_Booking.this, "This feature is still under development", Toast.LENGTH_SHORT).show();


            }
        });


        printwithsmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doConnect();

                try {
                    PrintOrderReceipt();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    findBT();
                    openBT();
                    sendData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });

        getorderdetails();


        tv_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phonenumber;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

            }
        });


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (MainActivity.player.isPlaying() == true) {
                        MainActivity.player.pause();
                    } else {

                    }
                    Intent i = new Intent(Activity_Booking.this, AcceptButton_activity.class);
                    i.putExtra("orderid", "" + getIntent().getStringExtra("orderid"));
                    startActivity(i);
                } catch (Exception e) {

                }
            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivity.player.isPlaying() == true) {
                    MainActivity.player.pause();
                } else {

                }
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_Booking.this);

                // Setting Dialog Title
                alertDialog.setTitle("Harperskebab Order Receiver");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure to decline order?");

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        // Write your code here to invoke YES event
//                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Booking.this);
                            LayoutInflater inflater = getLayoutInflater();
                            final View dialogLayout = inflater.inflate(R.layout.custom_alertdialog, null);
                            builder.setView(dialogLayout);
                            final AlertDialog aa = builder.create();
                            final EditText edt_reason = (EditText) dialogLayout.findViewById(R.id.edt_reason);
                            TextView ordernumber=dialogLayout.findViewById(R.id.ordernumber);
                            Button submit_reason = (Button) dialogLayout.findViewById(R.id.submit_reason);
                            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
                            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                                edt_reason.setHint(getString(R.string.write_notes_customer));
                                ordernumber.setText(getString(R.string.decline_reason));
                                submit_reason.setText(getString(R.string.submit_german));
                            }


                            submit_reason.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    Toast.makeText(Activity_Booking.this, "asakhdsakhdgs", Toast.LENGTH_SHORT).show();
                                    Decline("" + edt_reason.getText().toString());
                                }
                            });

                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    finish();
                                    aa.dismiss();

                                }
                            });
                            aa.show();

                        }

                    }

                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Booking.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Booking.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_change_orderstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Activity_Booking.this, Status_Change_Activity.class);
                i.putExtra("orderid", "" + getIntent().getStringExtra("orderid"));

                startActivity(i);
            }
        });

        btn_track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rider_idAssign.equals("Not Assigned")) {
                    showCustomDialogNotAssigned();
                } else {
                    Intent i = new Intent(Activity_Booking.this, TrackOrder_activity.class);
                    startActivity(i);
                    finish();
                }

//                Intent i = new Intent(Activity_Booking.this,TrackOrder_activity.class);
//                startActivity(i);
//                finish();


            }
        });

        btn_track_order.setText(parseLanguage.getParseString("Track_Order"));

        btn_markComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_Booking.this);

                // Setting Dialog Title
                alertDialog.setTitle("Harperskebab Order Receiver");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure want to mark this order complete");

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        ordermark_complete();


                    }

                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        });
    }


    public void init() {

        textSetting = new TextSetting();

        //Initialize Thermalprinter
        BaseApplication.instance.setCurrentCmdType(BaseEnum.CMD_ESC);
        printerFactory = new ThermalPrinterFactory();
        rtPrinter = printerFactory.create();

        PrinterObserverManager.getInstance().add(Activity_Booking.this);//添加连接状态监听

        configObj = new SerialPortConfigBean().getDefaultConfig();
        printerPowerUtil = new PrinterPowerUtil(this);
    }

    private void sendDataWithSmallPrinter() {

        try {
            mPrinter.printerInit(printerCallback);

            byte[] printformat = new byte[]{0x1B, 0x21, 0x03};
            mmOutputStream.write(printformat);

            mPrinter.printBitmap(BitmapFactory.decodeResource(Activity_Booking.this.getResources(), R.drawable.pp), printerCallback);
            mPrinter.printTextWithFont("------------------------------", "", 24, printerCallback);
            mPrinter.printText("" + OrderType, printerCallback);
            mPrinter.setAlignment(1, printerCallback);
            mPrinter.printTextWithFont("Order Number : " + order_reference_number, "", 24, printerCallback);
            mPrinter.setAlignment(1, printerCallback);

            mPrinter.printTextWithFont("Order Date : " + RequestAtDate, "", 24, printerCallback);
            mPrinter.setAlignment(1, printerCallback);

            mPrinter.printTextWithFont("Order Ready At : " + collectionTime, "", 44, printerCallback);
            mPrinter.setAlignment(1, printerCallback);

            mPrinter.printTextWithFont("------------------------------", "", 44, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("Order Preferences", "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);

            mPrinter.printTextWithFont("Payment Method : " + PaymentMethod, "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("Order Status : " + status, "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("------------------------------", "", 48, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("Restaurant Info", "", 48, printerCallback);
            mPrinter.setAlignment(1, printerCallback);


            mPrinter.printTextWithFont("" + restaurant_name, "", 24, printerCallback);
            mPrinter.setAlignment(1, printerCallback);


            mPrinter.printTextWithFont("" + restaurant_address, "", 24, printerCallback);
            mPrinter.setAlignment(1, printerCallback);


            mPrinter.printTextWithFont("------------------------------", "", 48, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("" + PayOptionStatus, "", 24, printerCallback);
            mPrinter.setAlignment(1, printerCallback);


            mPrinter.printTextWithFont("------------------------------", "", 48, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("Order summary", "", 24, printerCallback);
            mPrinter.setAlignment(1, printerCallback);

            mPrinter.printTextWithFont("Item:                    " + Activity_Splash.currency, "", 24, printerCallback);
            mPrinter.setAlignment(2, printerCallback);


            for (int i = 0; i < item_name.size(); i++) {
                mPrinter.printTextWithFont("" + item_quant.get(i) + " X " + item_name.get(i) + " :           " + " " + item_price.get(i), "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


                mPrinter.printTextWithFont("" + extra_toping.get(i), "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }
            for (int i = 0; i < drink_item_name.size(); i++) {

                mPrinter.printTextWithFont("" + drink_item_quant.get(i) + " X " + drink_item_name.get(i) + " :           " + " " + drink_item_price.get(i), "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


                mPrinter.printTextWithFont("" + drink_extra_toping.get(i), "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }
            for (int i = 0; i < meal_item_name.size(); i++) {

                mPrinter.printTextWithFont("" + meal_item_quant.get(i) + " X " + meal_item_name.get(i) + " :           " + " " + meal_item_price.get(i), "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


                mPrinter.printTextWithFont("" + meal_extra_toping.get(i), "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }


            mPrinter.printTextWithFont("------------------------------", "", 48, printerCallback);
            mPrinter.setAlignment(0, printerCallback);

            if (FoodCosts.equals("") || FoodCosts.equals(null) || FoodCosts.equals("Null") || FoodCosts.equals("null") || FoodCosts.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Food Costs:       " + " " + FoodCosts, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            if (DiscountPrice.equals("") || DiscountPrice.equals(null) || DiscountPrice.equals("Null") || DiscountPrice.equals("null") || DiscountPrice.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Total Discount:    " + "-" + " " + DiscountPrice, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }


            if (PayByLoyality.equals("") || PayByLoyality.equals(null) || PayByLoyality.equals("Null") || PayByLoyality.equals("null") || PayByLoyality.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Pay by Loyalty Coins     " + "-" + " " + PayByLoyality, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            if (WalletPay.equals("") || WalletPay.equals(null) || WalletPay.equals("Null") || WalletPay.equals("null") || WalletPay.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Pay by Wallet     " + "-" + " " + WalletPay, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            if (GiftCardPay.equals("") || GiftCardPay.equals(null) || GiftCardPay.equals("Null") || GiftCardPay.equals("null") || GiftCardPay.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Pay by Gift Card     " + "-" + " " + WalletPay, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }


            mPrinter.printTextWithFont("Sub Total:       " + " " + subTotal, "", 24, printerCallback);
            mPrinter.setAlignment(2, printerCallback);


            if (DeliveryCharge.equals("") || DeliveryCharge.equals(null) || DeliveryCharge.equals("Null") || DeliveryCharge.equals("null") || DeliveryCharge.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Delivery Charge:     " + "+" + " " + DeliveryCharge, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            if (ServiceFees.equals("") || ServiceFees.equals(null) || ServiceFees.equals("Null") || ServiceFees.equals("null") || ServiceFees.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Service Fees:     " + "+" + " " + ServiceFees, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            if (PackageFees.equals("") || PackageFees.equals(null) || PackageFees.equals("Null") || PackageFees.equals("null") || PackageFees.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Packaging Fees:     " + "+" + " " + PackageFees, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            if (SalesTaxAmount.equals("") || SalesTaxAmount.equals(null) || SalesTaxAmount.equals("Null") || SalesTaxAmount.equals("null") || SalesTaxAmount.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Service Tax:     " + "+" + " " + SalesTaxAmount, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            if (VatTax.equals("") || VatTax.equals(null) || VatTax.equals("Null") || VatTax.equals("null") || VatTax.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("VAT:        " + "+" + " " + VatTax, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            if (extraTipAddAmount.equals("") || extraTipAddAmount.equals(null) || extraTipAddAmount.equals("Null") || extraTipAddAmount.equals("null") || extraTipAddAmount.equals("0.00")) {

            } else {

                mPrinter.printTextWithFont("Rider Tip:     " + "+" + " " + extraTipAddAmount, "", 24, printerCallback);
                mPrinter.setAlignment(2, printerCallback);


            }

            mPrinter.printTextWithFont("Grand Total:       " + " " + OrderPrice, "", 24, printerCallback);
            mPrinter.setAlignment(2, printerCallback);


            mPrinter.printTextWithFont("-------------------------------", "", 48, printerCallback);
            mPrinter.setAlignment(0, printerCallback);

            mPrinter.printTextWithFont("Customer Details :", "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);

            mPrinter.printTextWithFont(name_customer, "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont(customer_address, "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("-------------------------------", "", 48, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            if (instruction_note.equals("")) {

            } else {


                mPrinter.printTextWithFont("Note :", "", 24, printerCallback);
                mPrinter.setAlignment(0, printerCallback);

                mPrinter.printTextWithFont(instruction_note, "", 24, printerCallback);
                mPrinter.setAlignment(0, printerCallback);

                mPrinter.printTextWithFont("-------------------------------", "", 48, printerCallback);
                mPrinter.setAlignment(0, printerCallback);


            }

            mPrinter.printTextWithFont("Order Placed at :", "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont(RequestAtDate + "/" + RequestAtTime, "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("Order Accepted at :", "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);

            mPrinter.printTextWithFont(OrderAcceptedDate + "/" + OrderAcceptedTime, "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);


            mPrinter.printTextWithFont("-------------------------------", "", 48, printerCallback);
            mPrinter.setAlignment(0, printerCallback);

            mPrinter.printTextWithFont(website_copy_right_text, "", 24, printerCallback);
            mPrinter.setAlignment(0, printerCallback);

            mPrinter.lineWrap(3, printerCallback);

            Toast.makeText(this, "Data Sent", Toast.LENGTH_SHORT).show();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private PrintTaskCallback printerCallback = new PrintTaskCallback() {
        @Override
        public void onRunResult(boolean isSuccess) {
            Log.i("TEST", "onRunResult:" + isSuccess);
        }

        @Override
        public void onReturnString(String result) {
            Log.i("TEST", "onReturnString:" + result);
        }

        @Override
        public void onRaiseException(int code, String msg) {
            Log.i("TEST", "onRaiseException:" + code + ", " + msg);
        }
    };

    public void getorderdetails() {

        item_size.clear();
        item_name.clear();
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
        item_instruction.clear();
        meal_instruction.clear();
        drink_instruction.clear();

        progressDialog = progressDialog.show(Activity_Booking.this, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.order_booking_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("logic", "" + s);
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
                        subTotal = jsonObject1.getString("subTotal");
                        DeliveryCharge = jsonObject1.getString("DeliveryCharge");
                        PackageFees = jsonObject1.getString("PackageFees");
                        FoodCosts = jsonObject1.getString("FoodCosts");
                        DiscountPrice = jsonObject1.getString("DiscountPrice");
                        CouponPrice = jsonObject1.getString("CouponPrice");
                        getFoodTaxTotal7 = jsonObject1.getString("getFoodTaxTotal7");
                        getFoodTaxTotal19 = jsonObject1.getString("getFoodTaxTotal19");
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
                        TotalSavedDiscount = jsonObject1.getString("TotalSavedDiscount");
                        customer_email = jsonObject1.getString("customer_email");
                        restaurant_mobile_number = jsonObject1.getString("restaurant_mobile_number");
                        DriverMobileNo = jsonObject1.getString("DriverMobileNo");
                        DriverPhoto = jsonObject1.getString("DriverPhoto");
                        rider_id = jsonObject1.getString("rider_id");
                        PayOptionStatus = jsonObject1.getString("PayOptionStatus");
                        restaurant_name = jsonObject1.getString("restaurant_name");
                        restaurant_address = jsonObject1.getString("restaurant_address");
                        website_copy_right_text = jsonObject1.getString("website_copy_right_text");
                        instruction_note = jsonObject1.getString("instruction_note");
                        discountOfferFreeItems = jsonObject1.getString("discountOfferFreeItems");
                        order_confirmed_time = jsonObject1.getString("order_confirmed_time");
                        order_kitchen_time = jsonObject1.getString("order_kitchen_time");
                        order_delivery_time = jsonObject1.getString("order_delivery_time");


                        rider_idAssign = rider_id;

                        rider_idAssign = rider_id;

                        authPreference.setDriverFirstName(DriverFirstName);
                        authPreference.setDriverLastName(DriverLastName);
                        authPreference.setDriverMobileNo(DriverMobileNo);
                        authPreference.setDriverPhoto(DriverPhoto);


                        /* food menu listing started */
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("OrderFoodItem");
                        if (jsonArray1.length() == 0) {
                            tv_no_foodItems.setVisibility(View.GONE);
                            recycler_fooditem.setVisibility(View.GONE);
                        } else {
                            tv_no_foodItems.setVisibility(View.GONE);
                            tv_no_foodItems.setTextColor(getResources().getColor(R.color.green));
                            ArrayList<FoodItemList> foodItemLists = new ArrayList<>();
                            for (int ii = 0; ii < jsonArray1.length(); ii++) {
                                JSONObject jsonObject12 = jsonArray1.getJSONObject(ii);
                                String ItemsName = jsonObject12.getString("ItemsName");
                                String quantity = jsonObject12.getString("quantity");
                                String menuprice = jsonObject12.getString("menuprice");
                                String item_sizea = jsonObject12.getString("item_size");
                                String instructions = jsonObject12.getString("instructions");
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

                            FooditemListView fooditemListView = new FooditemListView(Activity_Booking.this, foodItemLists);
                            linearLayoutManager = new LinearLayoutManager(Activity_Booking.this, LinearLayoutManager.VERTICAL, false);
                            recycler_fooditem.setLayoutManager(linearLayoutManager);
                            recycler_fooditem.setAdapter(fooditemListView);
                        }


                        /* combo listing started */
                        JSONArray OrderMealItem = jsonObject1.getJSONArray("OrderMealItem");
                        if (OrderMealItem.length() == 0) {
                            tv_meal.setVisibility(View.GONE);
                            rcmeal.setVisibility(View.GONE);
                        } else {
//                            tv_meal.setText("Combo Menu");
                            tv_meal.setVisibility(View.GONE);
                            tv_meal.setTextColor(getResources().getColor(R.color.red));
                            model_combos = new ArrayList<>();
                            for (int ii = 0; ii < OrderMealItem.length(); ii++) {
                                Model_Combo model_combo = new Model_Combo();
                                JSONObject j2 = OrderMealItem.getJSONObject(ii);
                                String ItemsName = j2.getString("ItemsName");
                                String quantity = j2.getString("quantity");
                                String menuprice = j2.getString("menuprice");

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
                                        String comboOptionItemName = j21.getString("ComboOptionItemName");
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
                                }



                                meal_item_name.add(ItemsName);
                                meal_item_price.add(menuprice);
                                meal_item_quant.add(quantity);
                                meallist.add(new FoodItemList(ItemsName, quantity, menuprice, "", "", Currency));
                                model_combo.setOrderComboItemOption(model_orderComboItemOptions);
                                model_combos.add(model_combo);
                            }

                            ComboItemAdapter mealListView = new ComboItemAdapter(Activity_Booking.this, model_combos);
                            linearLayoutManager2 = new LinearLayoutManager(Activity_Booking.this, LinearLayoutManager.VERTICAL, false);
                            rcmeal.setLayoutManager(linearLayoutManager2);
                            rcmeal.setAdapter(mealListView);
                        }

                        if (Table_Booking_Number.equals("") || Table_Booking_Number.equals(null) || Table_Booking_Number.equals("null") || Table_Booking_Number.equals("NULL")) {
                            dyiningtale.setVisibility(View.GONE);
                        } else {

                            if (myPref.getCustomer_default_langauge().equals("en")) {
                                dyiningtale.setText("Dining table :  " + Table_Booking_Number);
                            } else {
                                dyiningtale.setText(parseLanguage.getParseString("Dining_table") + " :  " + Table_Booking_Number);

                            }

                            dyiningtale.setVisibility(View.VISIBLE);
                        }

                        if (myPref.getCustomer_default_langauge().equals("en")) {
                            ordernumber.setText("" + "Order No" + " " + "#" + order_reference_number);
                            tv_delivery.setText(OrderType);

                        } else {
                            tv_delivery.setText(OrderType);

                            ordernumber.setText("" + parseLanguage.getParseString("Order_No") + " " + "#" + order_reference_number);
                        }


                        ////////////////////////////Food cost relative layout   /////////
                        if (FoodCosts.equals("") || FoodCosts.equals(null) || FoodCosts.equals("Null") || FoodCosts.equals("null") || FoodCosts.equals("0.00")) {
                            rl_foodcost.setVisibility(View.GONE);
                        } else {
                            rl_foodcost.setVisibility(View.VISIBLE);
                            food_costprice.setText(Currency + " " + FoodCosts);
                        }

                        if (DiscountPrice.equals("") || DiscountPrice.equals(null) || DiscountPrice.equals("Null") || DiscountPrice.equals("null") || DiscountPrice.equals("0.00")) {
                            rl_totol_discount.setVisibility(View.GONE);
                        } else {
                            rl_totol_discount.setVisibility(View.VISIBLE);
                            totaldiscount_price.setText("-" + Currency + " " + DiscountPrice);
                        }


                        if (PayByLoyality.equals("") || PayByLoyality.equals(null) || PayByLoyality.equals("Null") || PayByLoyality.equals("null") || PayByLoyality.equals("0.00")) {
                            rl_redeem.setVisibility(View.GONE);
                        } else {
                            rl_redeem.setVisibility(View.VISIBLE);
                            regardpoint.setText("-" + Currency + " " + PayByLoyality);
                        }

                        if (WalletPay.equals("") || WalletPay.equals(null) || WalletPay.equals("Null") || WalletPay.equals("null") || WalletPay.equals("0.00")) {
                            rl_paywallet.setVisibility(View.GONE);
                        } else {
                            rl_paywallet.setVisibility(View.VISIBLE);
                            paybywallet.setText("-" + Currency + " " + WalletPay);
                        }


                        if (GiftCardPay.equals("") || GiftCardPay.equals(null) || GiftCardPay.equals("Null") || GiftCardPay.equals("null") || GiftCardPay.equals("0.00")) {
                            rl_giftpay.setVisibility(View.GONE);
                        } else {
                            rl_giftpay.setVisibility(View.VISIBLE);
                            gifcardprice.setText("-" + Currency + " " + GiftCardPay);
                        }

                        if (collectionTime.equals("") || collectionTime.equals(null) || collectionTime.equals("Null") || collectionTime.equals("null") || collectionTime.equals("0.00")) {
                            tv_ready.setVisibility(View.GONE);
                        } else {
                            tv_ready.setVisibility(View.VISIBLE);
                            if (myPref.getCustomer_default_langauge().equals("en")) {
                                tv_ready.setText("Order ready at : " + collectionTime);

                            } else {

                                tv_ready.setText(parseLanguage.getParseString("Order_ready_at") + ": " + collectionTime);

                            }

                        }

                        if (OrderAcceptedDate.equalsIgnoreCase("")) {
                            tv_accpetdate.setVisibility(View.GONE);
                        } else {

                            tv_accpetdate.setVisibility(View.VISIBLE);

                        }
                        if (myPref.getCustomer_default_langauge().equals("en")) {
                            tv_deliveryon.setText("Order Placed at : " + RequestAtDate + "/" + RequestAtTime);


                        } else {

                            tv_deliveryon.setText(parseLanguage.getParseString("Order_Placed_at") + " : " + RequestAtDate + "/" + RequestAtTime);

                        }
                        tv_address.setText(customer_address);
                        if (customer_instruction.equalsIgnoreCase("")) {
                            rl_ins.setVisibility(View.GONE);
                        } else {
                            rl_ins.setVisibility(View.VISIBLE);
                            tv_instructions.setText(customer_instruction);
                        }
//                        btn_status.setText(status);

                        if (status.equalsIgnoreCase("KITCHEN")) {
                            btn_status.setText(parseLanguage.getParseString("Kitchen"));
                            btn_accept.setVisibility(View.GONE);
                            btn_decline.setVisibility(View.GONE);
                            orderclosed.setVisibility(View.GONE);
                            btn_change_orderstatus.setVisibility(View.VISIBLE);
                            btn_track_order.setVisibility(View.GONE);
                            btn_markComplete.setVisibility(View.VISIBLE);

                            if (!(order_kitchen_time.equals("") || order_kitchen_time.equals(null) || order_kitchen_time.equals("Null") ||
                                    order_kitchen_time.equals("null"))) {
                                if (myPref.getCustomer_default_langauge().equals("en")) {
                                    tv_accpetdate.setText("Kitchen_at : " + order_kitchen_time);


                                } else {

                                    tv_accpetdate.setText(parseLanguage.getParseString("Kitchen_at") + " : " + order_kitchen_time);
                                }
                            } else {
                                tv_accpetdate.setVisibility(View.GONE);
                            }
                        } else if (status.equalsIgnoreCase("Pending")) {
                            btn_status.setText(parseLanguage.getParseString("Pending"));
                            btn_accept.setVisibility(View.VISIBLE);
                            btn_decline.setVisibility(View.VISIBLE);
                            btn_change_orderstatus.setVisibility(View.GONE);
                            btn_track_order.setVisibility(View.GONE);
                            btn_markComplete.setVisibility(View.GONE);
                            orderclosed.setVisibility(View.GONE);
                            if (myPref.getCustomer_default_langauge().equals("en")) {
                                tv_accpetdate.setText("Requested Delivery at : " + RequestAtTime + "/" + RequestAtDate);


                            } else {

                                tv_accpetdate.setText(parseLanguage.getParseString("Requested_Delivery_at") + " : " + RequestAtTime + "/" + RequestAtDate);
                            }

                        } else if (status.equalsIgnoreCase("Cancelled")) {
                            btn_status.setText(parseLanguage.getParseString("Cancelled"));
                            btn_accept.setVisibility(View.GONE);
                            btn_decline.setVisibility(View.GONE);
                            btn_change_orderstatus.setVisibility(View.GONE);
                            btn_track_order.setVisibility(View.GONE);
                            btn_markComplete.setVisibility(View.GONE);
                            orderclosed.setVisibility(View.VISIBLE);

                        } else if (status.equalsIgnoreCase("Confirmed")) {
                            btn_status.setText(parseLanguage.getParseString("Confirmed"));
                            btn_accept.setVisibility(View.VISIBLE);
                            btn_decline.setVisibility(View.VISIBLE);
                            btn_change_orderstatus.setVisibility(View.GONE);
                            btn_track_order.setVisibility(View.GONE);
                            btn_markComplete.setVisibility(View.GONE);
                            orderclosed.setVisibility(View.GONE);
                            if (myPref.getCustomer_default_langauge().equals("en")) {
                                tv_accpetdate.setText("Confirmed at : " + order_confirmed_time);


                            } else {

                                tv_accpetdate.setText(parseLanguage.getParseString("Confirmed_at") + " : " + order_confirmed_time);
                            }

                        } else if (status.equalsIgnoreCase("Delivered")) {
                            btn_status.setText(parseLanguage.getParseString("Delivered"));
                            btn_accept.setVisibility(View.GONE);
                            btn_decline.setVisibility(View.GONE);
                            btn_change_orderstatus.setVisibility(View.GONE);
                            btn_track_order.setVisibility(View.GONE);
                            btn_markComplete.setVisibility(View.GONE);
                            orderclosed.setVisibility(View.VISIBLE);

                            if (!(order_delivery_time.equals("") || order_delivery_time.equals(null) || order_delivery_time.equals("Null") ||
                                    order_delivery_time.equals("null"))) {
                                if (myPref.getCustomer_default_langauge().equals("en")) {
                                    tv_accpetdate.setText("Delivered at : " + order_delivery_time);
                                } else {
                                    tv_accpetdate.setText(parseLanguage.getParseString("Delivered_at") + " : " + order_delivery_time);
                                }

                            } else {
                                tv_accpetdate.setVisibility(View.GONE);
                            }
                        } else if (status.equalsIgnoreCase("Accepted")) {
                            btn_status.setText(parseLanguage.getParseString("Accepted"));
                            btn_accept.setVisibility(View.GONE);
                            btn_decline.setVisibility(View.GONE);
                            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                                btn_change_orderstatus.setText(getString(R.string.change_order_status));
                            }
                            btn_change_orderstatus.setVisibility(View.VISIBLE);
                            btn_track_order.setVisibility(View.GONE);
                            btn_markComplete.setVisibility(View.VISIBLE);
                            orderclosed.setVisibility(View.GONE);

                            try {
                                findBT();
                                openBT();
                                sendData();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        } else {

                        }

                        btn_status.setBackgroundColor(Color.parseColor(order_status_color_code));
                        name.setText(name_customer);
                        tv_number.setText(customer_phone);
                        phonenumber = customer_phone;
                        if (myPref.getCustomer_default_langauge().equals("en")) {
                            tv_payment_type.setText("" + PaymentMethod);


                        } else {

                            tv_payment_type.setText("" + PaymentMethod);
                        }

                        if (subTotal.equals("") || subTotal.equals(null) || subTotal.equals("Null") || subTotal.equals("null") || subTotal.equals("0.00")) {
                            rl_subtotal.setVisibility(View.GONE);
                        } else {
                            rl_subtotal.setVisibility(View.VISIBLE);
                            tv_subtotal.setText(Currency + " " + subTotal);
                        }

                        if (DeliveryCharge.equals("") || DeliveryCharge.equals(null) || DeliveryCharge.equals("Null") || DeliveryCharge.equals("null") || DeliveryCharge.equals("0.00")) {
                            rl_deleiverycharge.setVisibility(View.GONE);
                        } else {
                            rl_deleiverycharge.setVisibility(View.VISIBLE);
                            tv_deliveryfee.setText("+" + Currency + " " + DeliveryCharge);
                        }

                        if (SalesTaxAmount.equals("") || SalesTaxAmount.equals(null) || SalesTaxAmount.equals("Null") || SalesTaxAmount.equals("null") || SalesTaxAmount.equals("0.00")) {
                            rl_servicetax.setVisibility(View.GONE);
                        } else {
                            rl_servicetax.setVisibility(View.VISIBLE);
                            tv_servicetax_price.setText("+" + Currency + " " + SalesTaxAmount);
                        }


                        if (ServiceFees.equals("") || ServiceFees.equals(null) || ServiceFees.equals("Null") || ServiceFees.equals("null") || ServiceFees.equals("0.00")) {
                            rl_servicecost.setVisibility(View.GONE);
                        } else {
                            rl_servicecost.setVisibility(View.VISIBLE);
                            tv_Servicecost.setText("+" + Currency + " " + ServiceFees);
                        }


                        if (extraTipAddAmount.equals("") || extraTipAddAmount.equals(null) || extraTipAddAmount.equals("Null") || extraTipAddAmount.equals("null") || extraTipAddAmount.equals("0.00")) {
                            rl_ridertrip.setVisibility(View.GONE);
                        } else {
                            rl_ridertrip.setVisibility(View.VISIBLE);
                            drivertip.setText("+" + Currency + " " + extraTipAddAmount);
                        }


                        if (PackageFees.equals("") || PackageFees.equals(null) || PackageFees.equals("Null") || PackageFees.equals("null") || PackageFees.equals("0.00")) {
                            rl_packgingcost.setVisibility(View.GONE);
                        } else {
                            rl_packgingcost.setVisibility(View.VISIBLE);
                            tv_packagingfee.setText("+" + Currency + " " + PackageFees);
                        }

                        if (VatTax.equals("") || VatTax.equals(null) || VatTax.equals("Null") || VatTax.equals("null") || VatTax.equals("0.00")) {
                            rl_vattax.setVisibility(View.GONE);
                        } else {
                            rl_vattax.setVisibility(View.VISIBLE);
                            vat.setText("+" + Currency + " " + VatTax);
                        }

                        if (OrderPrice.equals("") || OrderPrice.equals(null) || OrderPrice.equals("Null") || OrderPrice.equals("null") || OrderPrice.equals("0.00")) {
                            rl_total.setVisibility(View.GONE);
                        } else {
                            rl_total.setVisibility(View.VISIBLE);
                            total.setText(Currency + " " + OrderPrice);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("qwerty", "" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Log.e("response of api", "" + volleyError);
                String message ="";
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    message=getString(R.string.check_internet);
                }
                else {
                    message="Please Check your network connection";
                }
                Toast.makeText(Activity_Booking.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", "" + getIntent().getStringExtra("orderid"));
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("pa", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public class FooditemListView extends RecyclerView.Adapter<FooditemListView.ViewHolder> {

        Context context;
        ArrayList<FoodItemList> anujs;

        public FooditemListView(Context c, ArrayList<FoodItemList> p) {
            this.context = c;
            this.anujs = p;
        }

        public Activity_Booking.FooditemListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.single_fooditem_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Log.e("cc", "" + anujs);


            if (anujs.get(i).getItem_size().equals("")) {
                viewHolder.tv_foodname.setText(anujs.get(i).getquantity() + " " + "×" + " " + anujs.get(i).getItemsName());
            } else {
                viewHolder.tv_foodname.setText(anujs.get(i).getquantity() + " " + "×" + " " + anujs.get(i).getItemsName() + " (" + anujs.get(i).getItem_size() + ")");
            }

            viewHolder.tv_foodprice.setText(anujs.get(i).getCurrency() + " " + anujs.get(i).getmenuprice());

            ArrayList<Model_OrderComboItemExtra> model_orderComboItemExtras = prepareDataForExtraTopping(anujs.get(i).getExtraTopping());
            if (model_orderComboItemExtras.size() > 0) {


                CustomComboAdapter mAdapter = new CustomComboAdapter(context, model_orderComboItemExtras,Currency);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                viewHolder.recyler_combolist.setLayoutManager(mLayoutManager);
                viewHolder.recyler_combolist.setItemAnimator(new DefaultItemAnimator());
                viewHolder.recyler_combolist.setAdapter(mAdapter);


            } else {
                viewHolder.rlextratopping.setVisibility(View.GONE);
            }


        }

        @Override
        public int getItemCount() {
            return anujs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_foodname, tv_foodprice, extra;
            RelativeLayout rlextratopping;
            RecyclerView recyler_combolist;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                recyler_combolist = (RecyclerView) itemView.findViewById(R.id.recyler_combolist);
                tv_foodname = (TextView) itemView.findViewById(R.id.tv_foodname);
                tv_foodprice = (TextView) itemView.findViewById(R.id.tv_foodprice);
                extra = (TextView) itemView.findViewById(R.id.extra);
                rlextratopping = (RelativeLayout) itemView.findViewById(R.id.rlextratopping);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(Activity_Booking.this, "fdsfdf", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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


    public class DrinkitemListView extends RecyclerView.Adapter<DrinkitemListView.ViewHolder> {

        Context context;
        ArrayList<FoodItemList> anujs;

        public DrinkitemListView(Context c, ArrayList<FoodItemList> p) {
            this.context = c;
            this.anujs = p;
        }

        public DrinkitemListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.single_fooditem_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Log.e("cc", "" + anujs);


            if (anujs.get(i).getItem_size().equals("")) {
                viewHolder.tv_foodname.setText(anujs.get(i).getquantity() + " " + "×" + " " + anujs.get(i).getItemsName());
            } else {
                viewHolder.tv_foodname.setText(anujs.get(i).getquantity() + " " + "×" + " " + anujs.get(i).getItemsName() + " (" + anujs.get(i).getItem_size() + ")");
            }

            viewHolder.tv_foodprice.setText(anujs.get(i).getCurrency() + " " + anujs.get(i).getmenuprice());
            if (anujs.get(i).getExtraTopping().equals("")) {

                viewHolder.rlextratopping.setVisibility(View.GONE);
            } else {
                viewHolder.extra.setText(anujs.get(i).getExtraTopping());
            }


        }

        @Override
        public int getItemCount() {
            return anujs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_foodname, tv_foodprice, extra;
            RelativeLayout rlextratopping;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_foodname = (TextView) itemView.findViewById(R.id.tv_foodname);
                tv_foodprice = (TextView) itemView.findViewById(R.id.tv_foodprice);
                extra = (TextView) itemView.findViewById(R.id.extra);
                rlextratopping = (RelativeLayout) itemView.findViewById(R.id.rlextratopping);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(Activity_Booking.this, "fdsfdf", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    public class ComboItemAdapter extends RecyclerView.Adapter<ComboItemAdapter.ViewHolder> {

        Context context;
        ArrayList<Model_Combo> model_combos;


        public ComboItemAdapter(Context c, ArrayList<Model_Combo> model_combos) {
            this.context = c;
            this.model_combos = model_combos;
        }

        public ComboItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.single_fooditem_list_new, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            if (i == 0) {
                viewHolder.txt_comboname.setText(model_combos.get(0).getQuantity() + " × " + model_combos.get(0).getItemsName());
                viewHolder.txt_combodiscription.setText(model_combos.get(0).getItemsDescriptionName());
                viewHolder.txt_comboprince.setText(model_combos.get(0).getCurrency() + " " + model_combos.get(0).getMenuprice());

            } else {
                viewHolder.rel_combodetails.setVisibility(View.GONE);
            }

//            viewHolder.tv_foodname.setText(model_combos.get(i).getQuantity() + " " + "×" + " " + model_combos.get(i).getItemsName());
            viewHolder.tv_foodname.setText(model_combos.get(0).getOrderComboItemOption().get(i).getComboOptionName());

            viewHolder.tv_foodprice.setText(model_combos.get(0).getOrderComboItemOption().get(i).getComboOptionItemName() + " - " + model_combos.get(0).getOrderComboItemOption().get(i).getComboOptionItemSizeName());


            CustomComboAdapter mAdapter = new CustomComboAdapter(context, model_combos.get(0).getOrderComboItemOption().get(i).getOrderComboItemExtra(), model_combos.get(0).getCurrency());

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            viewHolder.recyler_combolist.setLayoutManager(mLayoutManager);
            viewHolder.recyler_combolist.setItemAnimator(new DefaultItemAnimator());
            viewHolder.recyler_combolist.setAdapter(mAdapter);


        }

        @Override
        public int getItemCount() {
            return model_combos.get(0).getOrderComboItemOption().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_foodname, tv_foodprice, extra, txt_comboname, txt_combodiscription, txt_comboprince;
            RelativeLayout rlextratopping, rel_combodetails;
            RecyclerView recyler_combolist;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                rel_combodetails = (RelativeLayout) itemView.findViewById(R.id.rel_combodetails);
                txt_comboprince = (TextView) itemView.findViewById(R.id.txt_comboprince);
                txt_combodiscription = (TextView) itemView.findViewById(R.id.txt_combodiscription);
                txt_comboname = (TextView) itemView.findViewById(R.id.txt_comboname);
                recyler_combolist = (RecyclerView) itemView.findViewById(R.id.recyler_combolist);
                tv_foodname = (TextView) itemView.findViewById(R.id.tv_foodname);
                tv_foodprice = (TextView) itemView.findViewById(R.id.tv_foodprice);
                extra = (TextView) itemView.findViewById(R.id.extra);
                rlextratopping = (RelativeLayout) itemView.findViewById(R.id.rlextratopping);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(Activity_Booking.this, "fdsfdf", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void Decline(final String a) {
        progressDialog = progressDialog.show(Activity_Booking.this, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.decline, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error = jsonObject.getInt("error");
                    if (error == 0) {
                        String success_msg = jsonObject.getString("error_msg");
                        showCustomDialogdecline(success_msg);


                    } else {
                        String success_msg = jsonObject.getString("error_msg");
                        showCustomDialog1decline(success_msg);
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
                Toast.makeText(Activity_Booking.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("orderid", "" + getIntent().getStringExtra("orderid"));
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


    public void ordermark_complete() {
        progressDialog = progressDialog.show(Activity_Booking.this, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.Order_Status, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error = jsonObject.getInt("error");
                    if (error == 0) {
                        String success_msg = jsonObject.getString("error_msg");
//                        Toast.makeText(Activity_Booking.this, success_msg, Toast.LENGTH_SHORT).show();

                        showCustomDialog(success_msg);
//                        Intent intent = new Intent(Activity_Booking.this,Activity_Booking.class);
//                        intent.putExtra("orderid",""+getIntent().getStringExtra("orderid"));
//                        startActivity(intent);
//                        finish();

                    } else {
                        String success_msg = jsonObject.getString("error_msg");
//                        Toast.makeText(Activity_Booking.this, success_msg, Toast.LENGTH_SHORT).show();
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
                Log.e("error", "" + volleyError);
                String message="";
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    message=getString(R.string.check_internet);
                }
                else {
                    message="Please Check your network connection";
                }
                Toast.makeText(Activity_Booking.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("orderid", "" + getIntent().getStringExtra("orderid"));
                params.put("OrderStatus", "2");
                params.put("lang_code", myPref.getCustomer_default_langauge());
//                params.put("DriverComment", a);
//                Log.e("pa",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showCustomDialog(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_Booking.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Activity_Booking.this, Activity_Booking.class);
                intent.putExtra("orderid", "" + getIntent().getStringExtra("orderid"));
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();

    }

    private void showCustomDialogNotAssigned() {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_Booking.this).create();

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("This trip is not assigned yet");

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Activity_Booking.this,Activity_Booking.class);
//                intent.putExtra("orderid",""+getIntent().getStringExtra("orderid"));
//                startActivity(intent);
//                finish();
//                        Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }


    private void showCustomDialog1(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_Booking.this).create();
        if (myPref.getCustomer_default_langauge().equals("en")) {
            alertDialog.setTitle(parseLanguage.getParseString("Alert"));

        } else {

            alertDialog.setTitle(parseLanguage.getParseString("Alert"));

            //tv_ready.setText(parseLanguage.getParseString("Order_ready_at")+": " + collectionTime);

        }


        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.cancel);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

//                Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void showCustomDialogdecline(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_Booking.this).create();
        if (myPref.getCustomer_default_langauge().equals("en")) {
            alertDialog.setTitle(parseLanguage.getParseString("Alert"));

        } else {

            alertDialog.setTitle(parseLanguage.getParseString("Alert"));

            //tv_ready.setText(parseLanguage.getParseString("Order_ready_at")+": " + collectionTime);

        }

        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Activity_Booking.this, Activity_Booking.class);
                intent.putExtra("orderid", "" + getIntent().getStringExtra("orderid"));
                startActivity(intent);
                finish();

//                        Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void showCustomDialog1decline(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(Activity_Booking.this).create();
        if (myPref.getCustomer_default_langauge().equals("en")) {
            alertDialog.setTitle(parseLanguage.getParseString("Alert"));

        } else {

            alertDialog.setTitle(parseLanguage.getParseString("Alert"));

            //tv_ready.setText(parseLanguage.getParseString("Order_ready_at")+": " + collectionTime);

        }

        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.cancel);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

//                Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

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

    // this will send text data to be printed by the bluetooth printer
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

            Toast.makeText(getApplicationContext(), "Data Sent", Toast.LENGTH_SHORT).show();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), img);
            if (bmp != null) {

                Double gh = (double) bmp.getWidth() / 384;

                BitmapUtils bitmapUtils = new BitmapUtils(Activity_Booking.this);
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

    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
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

    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
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
        doDisConnect();
    }


    private void doDisConnect() {
        if (rtPrinter != null && rtPrinter.getPrinterInterface() != null) {
            rtPrinter.disConnect();
        }
        printerPowerUtil.setPrinterPower(false);//turn printer power off.
    }


    public  void doConnect() {
        switch (checkedConType) {
            case BaseEnum.CON_COM:
                connectSerialPort((SerialPortConfigBean) configObj);
                printerPowerUtil.setPrinterPower(true);//turn printer power on.
                break;
            default:
                break;
        }

    }

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

    @Override
    public void printerObserverCallback(final PrinterInterface printerInterface, final int state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case CommonEnum.CONNECT_STATE_SUCCESS:

                        Toast.makeText(Activity_Booking.this, "Connect Success", Toast.LENGTH_SHORT).show();
                        rtPrinter.setPrinterInterface(printerInterface);
                        BaseApplication.getInstance().setRtPrinter(rtPrinter);
                        try {
                            textPrint();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case CommonEnum.CONNECT_STATE_INTERRUPTED:
                        if (printerInterface != null && printerInterface.getConfigObject() != null) {
                            Toast.makeText(Activity_Booking.this, "Disconnected", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Activity_Booking.this, "Disconnected", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void printerReadMsgCallback(PrinterInterface printerInterface, byte[] bytes) {

    }


    private void textPrint() throws UnsupportedEncodingException {

        switch (BaseApplication.getInstance().getCurrentCmdType()) {
            case BaseEnum.CMD_ESC:
                PrintOrderReceipt();
                break;
            default:
                break;
        }
    }


    private void PrintOrderReceipt() throws UnsupportedEncodingException {

        if (rtPrinter != null) {
            CmdFactory escFac = new EscFactory();
            Cmd escCmd = escFac.create();
            escCmd.append(escCmd.getHeaderCmd());//初始化, Initial

            escCmd.setChartsetName(mChartsetName);// "UTF-8"

            TextSetting textSetting = new TextSetting();
            textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
            if(collectionTime.equalsIgnoreCase("null")){
                escCmd.append(escCmd.getTextCmd(textSetting, RequestAtDate));
            }
            else {
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
            if(collectionTime.equalsIgnoreCase("null")){
                escCmd.append(escCmd.getTextCmd(orderReadyAt, parseLanguage.getParseString("Order_ready_at")));
            }
            else {
                escCmd.append(escCmd.getTextCmd(orderReadyAt, parseLanguage.getParseString("Order_ready_at") + collectionTime));
            }
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting text12 = new TextSetting();
            text12.setAlign(CommonEnum.ALIGN_BOTH_SIDES);
            escCmd.append(escCmd.getTextCmd(text12, parseLanguage.getParseString("Payment_Type") +"   "+ PaymentMethod));
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting txt_paymentdash = new TextSetting();
            txt_paymentdash.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(txt_paymentdash, "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());


            if (!instruction_note.equals("")) {

                TextSetting txt_customerNote = new TextSetting();
                txt_customerNote.setAlign(CommonEnum.ALIGN_LEFT);
                String customer_note=parseLanguage.getParseString("Customer_note");
                if(customer_note.equalsIgnoreCase("No Response")){
                    if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                          customer_note=getString(R.string.customer_note);
                    }
                    else {
                        customer_note="Customer Note";
                    }
                }
                escCmd.append(escCmd.getTextCmd(txt_customerNote, customer_note+":"));
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
            escCmd.append(escCmd.getTextCmd(txt_CustomerInfo1, parseLanguage.getParseString("Customer_info")+":"));
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting txt_CustomerInfo = new TextSetting();
            txt_CustomerInfo.setAlign(CommonEnum.ALIGN_LEFT);
            escCmd.append(escCmd.getTextCmd(txt_CustomerInfo, name_customer));
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting txt_Customeradd = new TextSetting();
            txt_Customeradd.setAlign(CommonEnum.ALIGN_LEFT);
            txt_Customeradd.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(txt_Customeradd, customer_address));
            escCmd.append(escCmd.getLFCRCmd());

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
                String back_order=parseLanguage.getParseString("Back_orders_from_customer");
                if(back_order.equalsIgnoreCase("No Response")){
                   if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                       back_order=getString(R.string.back_orders);
                   }
                   else {
                       back_order="Back orders from customers";
                   }
                }
                escCmd.append(escCmd.getTextCmd(txt_backorders, back_order+":" + number_of_customer_order));
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
            escCmd.append(escCmd.getTextCmd(txtitemname, parseLanguage.getParseString("Item_Name")+"       " +parseLanguage.getParseString("Price")));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting textSetting3 = new TextSetting();
            textSetting3.setAlign(CommonEnum.ALIGN_MIDDLE);
            for (int i = 0; i < item_name.size(); i++) {
                textSetting3.setAlign(CommonEnum.ALIGN_LEFT);
                switch (item_quant.get(i).length() + item_name.get(i).length() + item_price.get(i).length()) {
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                     " + Currency + item_price.get(i)));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                    " + Currency + item_price.get(i)));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                   " + Currency + item_price.get(i)));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                  " + Currency + item_price.get(i)));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                 " + Currency + item_price.get(i)));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                " + Currency + item_price.get(i)));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "               " + Currency + item_price.get(i)));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "              " + Currency + item_price.get(i)));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "             " + Currency + item_price.get(i)));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "            " + Currency + item_price.get(i)));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "           " + Currency + item_price.get(i)));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "          " + Currency + item_price.get(i)));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "         " + Currency + item_price.get(i)));
                        break;
                    case 19:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "        " + Currency + item_price.get(i)));
                        break;
                    case 20:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "       " + Currency + item_price.get(i)));
                        break;
                    case 21:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "      " + Currency + item_price.get(i)));
                        break;
                    case 22:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "     " + Currency + item_price.get(i)));
                        break;
                    case 23:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "    " + Currency + item_price.get(i)));
                        break;
                    case 24:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "   " + Currency + item_price.get(i)));
                        break;
                    case 25:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "  " + Currency + item_price.get(i)));
                        break;
                    case 26:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + " " + Currency + item_price.get(i)));
                        break;
                    case 27:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "" + Currency + item_price.get(i)));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());

                if (!item_size.get(i).equalsIgnoreCase("")) {
                    escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_size.get(i)));
                    escCmd.append(escCmd.getLFCRCmd());
                }
                if (!extra_toping.get(i).equalsIgnoreCase("")) {

                    ArrayList<Model_OrderComboItemExtra> model_orderComboItemExtras = prepareDataForExtraTopping(extra_toping.get(i));
                    for (int i1 = 0; i1 < model_orderComboItemExtras.size(); i1++) {
                        String comboExtraItemQuantity = model_orderComboItemExtras.get(i1).getComboExtraItemQuantity();
                        String comboExtraItemName = model_orderComboItemExtras.get(i1).getComboExtraItemName();
                        String comboExtraItemPrice = model_orderComboItemExtras.get(i1).getComboExtraItemPrice();
                        switch (comboExtraItemQuantity.length()+comboExtraItemName.length()+comboExtraItemPrice.length())
                        {
                             case 5: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "                      " + Currency+comboExtraItemPrice)); break;
                             case 6: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "                     " + Currency+comboExtraItemPrice)); break;
                             case 7: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "                    " + Currency+comboExtraItemPrice)); break;
                             case 8: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "                   " + Currency+comboExtraItemPrice)); break;
                             case 9: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "                  " + Currency+comboExtraItemPrice)); break;
                            case 10: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "                 " + Currency+comboExtraItemPrice)); break;
                            case 11: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "                " + Currency+comboExtraItemPrice)); break;
                            case 12: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "               " + Currency+comboExtraItemPrice)); break;
                            case 13: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "              " + Currency+comboExtraItemPrice)); break;
                            case 14: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "             " + Currency+comboExtraItemPrice)); break;
                            case 15: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "            " + Currency+comboExtraItemPrice)); break;
                            case 16: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "           " + Currency+comboExtraItemPrice)); break;
                            case 17: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "          " + Currency+comboExtraItemPrice)); break;
                            case 18: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "         " + Currency+comboExtraItemPrice)); break;
                            case 19: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "        " + Currency+comboExtraItemPrice)); break;
                            case 20: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "       " + Currency+comboExtraItemPrice)); break;
                            case 21: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "      " + Currency+comboExtraItemPrice)); break;
                            case 22: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "     " + Currency+comboExtraItemPrice)); break;
                            case 23: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "    " + Currency+comboExtraItemPrice)); break;
                            case 24: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "   " + Currency+comboExtraItemPrice)); break;
                            case 25: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "  " + Currency+comboExtraItemPrice)); break;
                            case 26: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + " " + Currency+comboExtraItemPrice)); break;
                            case 27: escCmd.append(escCmd.getTextCmd(textSetting3, "" + comboExtraItemQuantity + " X " + comboExtraItemName+ ":" + "" + Currency+comboExtraItemPrice)); break;
                        }

                        escCmd.append(escCmd.getLFCRCmd());

                    }

                }


                if (!item_instruction.get(i).equalsIgnoreCase("")) {
                    escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_instruction.get(i)));
                    escCmd.append(escCmd.getLFCRCmd());
                } else {
                    escCmd.append(escCmd.getLFCRCmd());
                    escCmd.append(escCmd.getLFCRCmd());
                }


            }

            /*for combo starting*/

            if (model_combos.size() > 0) {
                Model_Combo model_combo = model_combos.get(0);
                TextSetting txt_backorders = new TextSetting();
                txt_backorders.setAlign(CommonEnum.ALIGN_LEFT);
                switch (model_combo.getQuantity().length() + model_combo.getItemsName().length() + model_combo.getMenuprice().length()) {

                    case 2:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                          " + Currency + model_combo.getMenuprice()));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                         " + Currency + model_combo.getMenuprice()));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                        " + Currency + model_combo.getMenuprice()));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                       " + Currency + model_combo.getMenuprice()));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                      " + Currency + model_combo.getMenuprice()));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                     " + Currency + model_combo.getMenuprice()));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                    " + Currency + model_combo.getMenuprice()));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                   " + Currency + model_combo.getMenuprice()));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                  " + Currency + model_combo.getMenuprice()));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                 " + Currency + model_combo.getMenuprice()));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "                " + Currency + model_combo.getMenuprice()));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "               " + Currency + model_combo.getMenuprice()));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "              " + Currency + model_combo.getMenuprice()));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "             " + Currency + model_combo.getMenuprice()));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "            " + Currency + model_combo.getMenuprice()));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "           " + Currency + model_combo.getMenuprice()));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "          " + Currency + model_combo.getMenuprice()));
                        break;
                    case 19:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "         " + Currency + model_combo.getMenuprice()));
                        break;
                    case 20:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "        " + Currency + model_combo.getMenuprice()));
                        break;
                    case 21:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "       " + Currency + model_combo.getMenuprice()));
                        break;
                    case 22:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "      " + Currency + model_combo.getMenuprice()));
                        break;
                    case 23:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "     " + Currency + model_combo.getMenuprice()));
                        break;
                    case 24:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "    " + Currency + model_combo.getMenuprice()));
                        break;
                    case 25:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "   " + Currency + model_combo.getMenuprice()));
                        break;
                    case 26:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + "  " + Currency + model_combo.getMenuprice()));
                        break;
                    case 27:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + " " + Currency + model_combo.getMenuprice()));
                        break;
                    case 28:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, model_combo.getQuantity() + " X " + model_combo.getItemsName() + Currency + model_combo.getMenuprice()));
                        break;
                }
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting txt_comboDescribe = new TextSetting();
                txt_comboDescribe.setAlign(CommonEnum.ALIGN_LEFT);
                escCmd.append(escCmd.getTextCmd(txt_comboDescribe, model_combo.getItemsDescriptionName()));
                escCmd.append(escCmd.getLFCRCmd());

                TextSetting textSetting31 = new TextSetting();
                textSetting31.setAlign(CommonEnum.ALIGN_MIDDLE);

                ArrayList<Model_OrderComboItemOption> orderComboItemOption = model_combo.getOrderComboItemOption();
                for (int i = 0; i < orderComboItemOption.size(); i++) {


                    textSetting3.setAlign(CommonEnum.ALIGN_LEFT);
                    escCmd.append(escCmd.getTextCmd(textSetting3, orderComboItemOption.get(i).getComboOptionItemName()));
                    escCmd.append(escCmd.getLFCRCmd());
                    Model_OrderComboItemOption model_orderComboItemOption = orderComboItemOption.get(i);
                    if (!model_orderComboItemOption.getComboOptionItemSizeName().equalsIgnoreCase("")) {
                        textSetting3.setAlign(CommonEnum.ALIGN_LEFT);
                        escCmd.append(escCmd.getTextCmd(textSetting3, model_orderComboItemOption.getComboOptionItemSizeName()));
                        escCmd.append(escCmd.getLFCRCmd());
                        escCmd.append(escCmd.getLFCRCmd());
                    }
                    for (int i1 = 0; i1 < model_orderComboItemOption.getOrderComboItemExtra().size(); i1++) {
                        String comboExtraItemName = model_orderComboItemOption.getOrderComboItemExtra().get(i1).getComboExtraItemName();
                        String comboExtraItemPrice = model_orderComboItemOption.getOrderComboItemExtra().get(i1).getComboExtraItemPrice();
                        String comboExtraItemQuantity = model_orderComboItemOption.getOrderComboItemExtra().get(i1).getComboExtraItemQuantity();


                        textSetting3.setAlign(CommonEnum.ALIGN_LEFT);

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


                        if (i==(model_orderComboItemOption.getOrderComboItemExtra().size()-1)) {
                            escCmd.append(escCmd.getLFCRCmd());
                            escCmd.append(escCmd.getLFCRCmd());
                        }else
                        {
                            escCmd.append(escCmd.getLFCRCmd());
                        }
                    }


                }
            }


            if (!discountOfferFreeItems.equals("")) {
                TextSetting txt_backorders = new TextSetting();
                txt_backorders.setAlign(CommonEnum.ALIGN_MIDDLE);
String food_available= parseLanguage.getParseString("Free_Item");
if(food_available.equalsIgnoreCase("No Response")){
    if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
        food_available=getString(R.string.food_available);
    }
    else{
        food_available="Free food available";
    }
}
                switch (discountOfferFreeItems.length()) {
                    case 4:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+"        " + discountOfferFreeItems));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+":       " + discountOfferFreeItems));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+":      " + discountOfferFreeItems));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+":     " + discountOfferFreeItems));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+":    " + discountOfferFreeItems));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+":   " + discountOfferFreeItems));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+":  " + discountOfferFreeItems));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+": " + discountOfferFreeItems));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, food_available+":" + discountOfferFreeItems));
                        break;
                }
                escCmd.append(escCmd.getLFCRCmd());
                escCmd.append(escCmd.getLFCRCmd());
            }

            if (!Table_Booking_Number.equals("")) {
                TextSetting txt_backorders = new TextSetting();
                txt_backorders.setAlign(CommonEnum.ALIGN_MIDDLE);
                String table_no=parseLanguage.getParseString("Table_no");
                if(table_no.equalsIgnoreCase("No Response")){
                    if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                        table_no=getString(R.string.table_no);
                    }
                    else {

                        table_no="Table No";
                    }
                }
                switch (Table_Booking_Number.length()) {
                    case 4:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":                   " + Table_Booking_Number));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":                  " + Table_Booking_Number));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":                 " + Table_Booking_Number));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":                " + Table_Booking_Number));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":               " + Table_Booking_Number));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":              " + Table_Booking_Number));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":             " + Table_Booking_Number));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":            " + Table_Booking_Number));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":           " + Table_Booking_Number));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":          " + Table_Booking_Number));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":         " + Table_Booking_Number));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":        " + Table_Booking_Number));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":       " + Table_Booking_Number));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":      " + Table_Booking_Number));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":     " + Table_Booking_Number));
                        break;
                    case 19:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":    " + Table_Booking_Number));
                        break;
                    case 20:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":   " + Table_Booking_Number));
                        break;
                    case 21:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":  " + Table_Booking_Number));
                        break;
                    case 22:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+": " + Table_Booking_Number));
                        break;
                    case 23:
                        escCmd.append(escCmd.getTextCmd(txt_backorders, table_no+":" + Table_Booking_Number));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());
                escCmd.append(escCmd.getLFCRCmd());
            } else {
                escCmd.append(escCmd.getLFCRCmd());
                escCmd.append(escCmd.getLFCRCmd());
            }


            if (!(FoodCosts.equals("") || FoodCosts.equals(null) || FoodCosts.equals("Null") || FoodCosts.equals("null") || FoodCosts.equals("0.00"))) {

                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                String food_cost=parseLanguage.getParseString("Food_Cost");
                switch (FoodCosts.length()) {
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":  " + Currency + FoodCosts));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":  " + Currency + FoodCosts));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":  " + Currency + FoodCosts));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":    " + Currency + FoodCosts));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":    " + Currency + FoodCosts));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost+":   " + Currency + FoodCosts));
                        break;

                }

                escCmd.append(escCmd.getLFCRCmd());


            }

            if (!(PayByLoyality.equals("") || PayByLoyality.equals(null) || PayByLoyality.equals("Null") || PayByLoyality.equals("null") || PayByLoyality.equals("0.00"))) {

                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                String total_redeem=parseLanguage.getParseString("Total_redeem_point");
                if(total_redeem.equalsIgnoreCase("No Response")){
                    if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                        total_redeem=getString(R.string.total_redeem);
                    }
                    else {
                        total_redeem="Total Redeem Point";
                    }
                }
                switch (PayByLoyality.length()) {
                    case 1:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":            " + PayByLoyality));
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":           " + PayByLoyality));
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":          " + PayByLoyality));
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":         " + PayByLoyality));
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":        " + PayByLoyality));
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":       " + PayByLoyality));
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":      " + PayByLoyality));
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":     " + PayByLoyality));
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":    " + PayByLoyality));
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":   " + PayByLoyality));
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":  " + PayByLoyality));
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+": " + PayByLoyality));
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_redeem+":" + PayByLoyality));
                }

                escCmd.append(escCmd.getLFCRCmd());

            }

            if (!(WalletPay.equals("") || WalletPay.equals(null) || WalletPay.equals("Null") || WalletPay.equals("null") || WalletPay.equals("0.00"))) {

                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                String pay_wallet=parseLanguage.getParseString("Pay_by_Wallet");
                switch (WalletPay.length()) {
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":               " + WalletPay));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":              " + WalletPay));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":             " + WalletPay));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":            " + WalletPay));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":           " + WalletPay));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":          " + WalletPay));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":         " + WalletPay));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":        " + WalletPay));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":       " + WalletPay));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":      " + WalletPay));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":     " + WalletPay));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":    " + WalletPay));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":   " + WalletPay));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":  " + WalletPay));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+": " + WalletPay));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textSetting4, pay_wallet+":" + WalletPay));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());

            }

            if (!(GiftCardPay.equals("") || GiftCardPay.equals(null) || GiftCardPay.equals("Null") || GiftCardPay.equals("null") || GiftCardPay.equals("0.00"))) {

                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                switch (GiftCardPay.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":                " + GiftCardPay));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":               " + GiftCardPay));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":              " + GiftCardPay));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":             " + GiftCardPay));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":            " + GiftCardPay));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":           " + GiftCardPay));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":          " + GiftCardPay));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":         " + GiftCardPay));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":        " + GiftCardPay));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":       " + GiftCardPay));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":      " + GiftCardPay));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":     " + GiftCardPay));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":    " + GiftCardPay));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":   " + GiftCardPay));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":  " + GiftCardPay));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+": " + GiftCardPay));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Pay_by_Wallet")+":" + GiftCardPay));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());

            }


            if (!(DiscountPrice.equals("") || DiscountPrice.equals(null) || DiscountPrice.equals("Null") || DiscountPrice.equals("null") || DiscountPrice.equals("0.00"))) {
                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                switch (DiscountPrice.length()) {
                     case 2:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":              " + Currency + DiscountPrice));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":             " + Currency + DiscountPrice));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":                    " + Currency + DiscountPrice));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":           " + Currency + DiscountPrice));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":          " + Currency + DiscountPrice));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":         " + Currency + DiscountPrice));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":        " + Currency + DiscountPrice));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":       " + Currency + DiscountPrice));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":      " + Currency + DiscountPrice));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":     " + Currency + DiscountPrice));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":    " + Currency + DiscountPrice));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":   " + Currency + DiscountPrice));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":  " + Currency + DiscountPrice));
                        break;
                    case 15:
                         escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+": " + Currency + DiscountPrice));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textSetting4, parseLanguage.getParseString("Total_Discount")+":" + Currency + DiscountPrice));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());
            }
            if (!(CouponPrice.equals("") || CouponPrice.equals(null) || CouponPrice.equals("Null") || CouponPrice.equals("null") || CouponPrice.equals("0.00"))) {
                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                String total_coupon_discount=parseLanguage.getParseString("Total_Coupon_Discount");
                switch (CouponPrice.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount+":       " + Currency + CouponPrice));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount+":      " + Currency + CouponPrice));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount+":     " + Currency + CouponPrice));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount+":    " + Currency + CouponPrice));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount+":   " + Currency + CouponPrice));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount+":  " + Currency + CouponPrice));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount+": " + Currency + CouponPrice));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting4, total_coupon_discount+":" + Currency + CouponPrice));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());
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
                String rider_tip=parseLanguage.getParseString("Rider_Tip");
                switch (extraTipAddAmount.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 19:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                    case 20:
                        escCmd.append(escCmd.getTextCmd(textextratip, rider_tip+":                  " + Currency + extraTipAddAmount));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());
            }
            if (!(DeliveryCharge.equals("") || DeliveryCharge.equals(null) || DeliveryCharge.equals("Null") || DeliveryCharge.equals("null") || DeliveryCharge.equals("0.00"))) {
                TextSetting textextratip = new TextSetting();
                textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                String delivery_charge=parseLanguage.getParseString("Delivery_Charge");
                switch (DeliveryCharge.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textextratip, delivery_charge+":             " + Currency + DeliveryCharge));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());
            }


            if (!(ServiceFees.equals("") || ServiceFees.equals(null) || ServiceFees.equals("Null") || ServiceFees.equals("null") || ServiceFees.equals("0.00"))) {
                TextSetting textextratip = new TextSetting();
                textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                String service_cost=parseLanguage.getParseString("Service_Cost");
                switch (ServiceFees.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":              " + Currency + ServiceFees));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":             " + Currency + ServiceFees));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":            " + Currency + ServiceFees));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":           " + Currency + ServiceFees));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":          " + Currency + ServiceFees));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":         " + Currency + ServiceFees));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":        " + Currency + ServiceFees));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":       " + Currency + ServiceFees));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":      " + Currency + ServiceFees));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":     " + Currency + ServiceFees));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":    " + Currency + ServiceFees));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":   " + Currency + ServiceFees));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":  " + Currency + ServiceFees));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+": " + Currency + ServiceFees));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_cost+":" + Currency + ServiceFees));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());
            }
            if (!(PackageFees.equals("") || PackageFees.equals(null) || PackageFees.equals("Null") || PackageFees.equals("null") || PackageFees.equals("0.00"))) {
                TextSetting textextratip = new TextSetting();
                textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                String packaging_charge=parseLanguage.getParseString("Packaging_Cost");
                switch (PackageFees.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":              " + Currency + PackageFees));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":             " + Currency + PackageFees));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":            " + Currency + PackageFees));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":           " + Currency + PackageFees));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":          " + Currency + PackageFees));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":         " + Currency + PackageFees));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":        " + Currency + PackageFees));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":       " + Currency + PackageFees));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":      " + Currency + PackageFees));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":     " + Currency + PackageFees));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":    " + Currency + PackageFees));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":   " + Currency + PackageFees));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":  " + Currency + PackageFees));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+": " + Currency + PackageFees));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textextratip, packaging_charge+":" + Currency + PackageFees));
                        break;
                }


            }
            if (!(SalesTaxAmount.equals("") || SalesTaxAmount.equals(null) || SalesTaxAmount.equals("Null") || SalesTaxAmount.equals("null") || SalesTaxAmount.equals("0.00"))) {
                TextSetting textextratip = new TextSetting();
                textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                String service_tax=parseLanguage.getParseString("Service_Tax");
                switch (SalesTaxAmount.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":                 " + Currency + SalesTaxAmount));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":                " + Currency + SalesTaxAmount));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":               " + Currency + SalesTaxAmount));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":              " + Currency + SalesTaxAmount));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":             " + Currency + SalesTaxAmount));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":            " + Currency + SalesTaxAmount));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":           " + Currency + SalesTaxAmount));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":          " + Currency + SalesTaxAmount));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":         " + Currency + SalesTaxAmount));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":        " + Currency + SalesTaxAmount));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":       " + Currency + SalesTaxAmount));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":      " + Currency + SalesTaxAmount));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":     " + Currency + SalesTaxAmount));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":    " + Currency + SalesTaxAmount));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":   " + Currency + SalesTaxAmount));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":  " + Currency + SalesTaxAmount));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+": " + Currency + SalesTaxAmount));
                        break;
                    case 19:
                        escCmd.append(escCmd.getTextCmd(textextratip, service_tax+":" + Currency + SalesTaxAmount));
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
            if (!(getFoodTaxTotal7.equals("") || getFoodTaxTotal7.equals(null) || getFoodTaxTotal7.equals("Null") || getFoodTaxTotal7.equals("null") || getFoodTaxTotal7.equals("0.00"))) {
                TextSetting textextratip = new TextSetting();
                textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                String drink_tax=parseLanguage.getParseString("Inkl_MwSt_7");
                switch (getFoodTaxTotal7.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":               " + getFoodTaxTotal7));
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":              " + getFoodTaxTotal7));
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":                " + getFoodTaxTotal7));
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":               " + getFoodTaxTotal7));
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":            "+ getFoodTaxTotal7));
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+ ":           " + getFoodTaxTotal7));
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":          " + getFoodTaxTotal7));
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":         " + getFoodTaxTotal7));
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":        " + getFoodTaxTotal7));
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":        " + getFoodTaxTotal7));
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":       " + getFoodTaxTotal7));
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":      " + getFoodTaxTotal7));
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":     " + getFoodTaxTotal7));
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":    " + getFoodTaxTotal7));
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":   " + getFoodTaxTotal7));
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":  " + getFoodTaxTotal7));
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+": " + getFoodTaxTotal7));
                    case 19:
                        escCmd.append(escCmd.getTextCmd(textextratip, drink_tax+":" + getFoodTaxTotal7));
                }

                escCmd.append(escCmd.getLFCRCmd());
            }
            if (!(getFoodTaxTotal19.equals("") || getFoodTaxTotal19.equals(null) || getFoodTaxTotal19.equals("Null") || getFoodTaxTotal19.equals("null") || getFoodTaxTotal19.equals("0.00"))) {
                TextSetting textextratip = new TextSetting();
                textextratip.setAlign(CommonEnum.ALIGN_LEFT);
                String food_tax=parseLanguage.getParseString("Inkl_MwSt_19");
                switch (getFoodTaxTotal19.length()) {
                    case 2:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":                    " + getFoodTaxTotal19));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":                   " + getFoodTaxTotal19));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":                  " + getFoodTaxTotal19));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":                 " + getFoodTaxTotal19));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":                " + getFoodTaxTotal19));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":               " + getFoodTaxTotal19));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":              " + getFoodTaxTotal19));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":             " + getFoodTaxTotal19));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":           " + getFoodTaxTotal19));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":          " + getFoodTaxTotal19));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":         " + getFoodTaxTotal19));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":        " + getFoodTaxTotal19));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":       " + getFoodTaxTotal19));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":      " + getFoodTaxTotal19));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":     " + getFoodTaxTotal19));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":    " + getFoodTaxTotal19));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":   " + getFoodTaxTotal19));
                        break;
                    case 19:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":  " + getFoodTaxTotal19));
                        break;
                    case 20:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+": " + getFoodTaxTotal19));
                        break;
                    case 21:
                        escCmd.append(escCmd.getTextCmd(textextratip, food_tax+":" + getFoodTaxTotal19));
                        break;
                }

                escCmd.append(escCmd.getLFCRCmd());
            }


            TextSetting textSetting5 = new TextSetting();
            textSetting5.setAlign(CommonEnum.ALIGN_LEFT);
            textSetting5.setBold(SettingEnum.Enable);
            String total=parseLanguage.getParseString("Total");
            switch (OrderPrice.length()) {
                case 2:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":                  " + Currency + OrderPrice));
                    break;
                case 3:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":                " + Currency + OrderPrice));
                    break;
                case 4:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":                " + Currency + OrderPrice));
                    break;
                case 5:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":                 " + Currency + OrderPrice));
                    break;
                case 6:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":                " + Currency + OrderPrice));
                    break;
                case 7:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":               " + Currency + OrderPrice));
                    break;
                case 8:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":               " + Currency + OrderPrice));
                    break;
                case 9:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":              " + Currency + OrderPrice));
                    break;
                case 10:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":             " + Currency + OrderPrice));
                    break;
                case 11:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":              " + Currency + OrderPrice));
                    break;
                case 12:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":             " + Currency + OrderPrice));
                    break;
                case 13:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":            " + Currency + OrderPrice));
                    break;
                case 14:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":           " + Currency + OrderPrice));
                    break;
                case 15:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":          " + Currency + OrderPrice));
                    break;
                case 16:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":         " + Currency + OrderPrice));
                    break;
                case 17:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":        " + Currency + OrderPrice));
                    break;
                case 18:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":       " + Currency + OrderPrice));
                    break;
                case 19:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":      " + Currency + OrderPrice));
                    break;
                case 20:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":     " + Currency + OrderPrice));
                    break;
                case 21:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":    " + Currency + OrderPrice));
                    break;
                case 22:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":   " + Currency + OrderPrice));
                    break;
                case 23:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":  " + Currency + OrderPrice));
                    break;
                case 24:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+": " + Currency + OrderPrice));
                    break;
                case 25:
                    escCmd.append(escCmd.getTextCmd(textSetting5, total+":" + Currency + OrderPrice));
                    break;
            }

            escCmd.append(escCmd.getLFCRCmd());


            TextSetting textSetting53 = new TextSetting();
            textSetting53.setAlign(CommonEnum.ALIGN_LEFT);
            String total_saved=parseLanguage.getParseString("Total_Saved");
            switch (TotalSavedDiscount.length()) {
                case 1:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":     " + Currency + TotalSavedDiscount));
                    break;
                case 2:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":    " + Currency + TotalSavedDiscount));
                    break;
                case 3:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":    " + Currency + TotalSavedDiscount));
                    break;
                case 4:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":    " + Currency + TotalSavedDiscount));
                    break;
                case 5:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":   " + Currency + TotalSavedDiscount));
                    break;
                case 6:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":   " + Currency + TotalSavedDiscount));
                    break;
                case 7:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":   " + Currency + TotalSavedDiscount));
                    break;
                case 8:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":    " + Currency + TotalSavedDiscount));
                    break;
                case 9:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":    " + Currency + TotalSavedDiscount));
                    break;
                case 10:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":    " + Currency + TotalSavedDiscount));
                    break;
                case 11:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":   " + Currency + TotalSavedDiscount));
                    break;
                case 12:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":   " + Currency + TotalSavedDiscount));
                    break;
                case 13:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":  " + Currency + TotalSavedDiscount));
                    break;
                case 14:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":  " + Currency + TotalSavedDiscount));
                    break;
                case 15:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+": " + Currency + TotalSavedDiscount));
                    break;
                case 16:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+": " + Currency + TotalSavedDiscount));
                    break;
                case 17:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":  " + Currency + TotalSavedDiscount));
                    break;
                case 18:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":   " + Currency + TotalSavedDiscount));
                    break;
                case 19:
                    escCmd.append(escCmd.getTextCmd(textSetting53, total_saved+":" + Currency + TotalSavedDiscount));
                    break;
            }

            escCmd.append(escCmd.getLFCRCmd());


            escCmd.append(escCmd.getHeaderCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());

//            System.out.print(escCmd);
            Log.i("TAGPrintReceipt", "PrintOrderReceipt: " + escCmd);
            if(escCmd.getAppendCmds()!=null) {

                    rtPrinter.writeMsg(escCmd.getAppendCmds());


            }
//
        }
    }
}


