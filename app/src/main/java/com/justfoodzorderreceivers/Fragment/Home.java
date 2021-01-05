    package com.justfoodzorderreceivers.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;


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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.justfoodzorderreceivers.AcceptButton_activity;
import com.justfoodzorderreceivers.AcceptButton_activity1;
import com.justfoodzorderreceivers.Activity_Booking;

import com.justfoodzorderreceivers.Activity_OrderSupport;
import com.justfoodzorderreceivers.Activity_Splash;
import com.justfoodzorderreceivers.AuthPreference;
import com.justfoodzorderreceivers.BaseApplication;
import com.justfoodzorderreceivers.BaseEnum;
import com.justfoodzorderreceivers.BitmapUtils;
import com.justfoodzorderreceivers.MainActivity;
import com.justfoodzorderreceivers.Model.BookTableList;
import com.justfoodzorderreceivers.Model.FoodItemList;
import com.justfoodzorderreceivers.Model.OrderList;
import com.justfoodzorderreceivers.Model.OrderSupportList;
import com.justfoodzorderreceivers.Model.TimeModel;
import com.justfoodzorderreceivers.Model_Combo;
import com.justfoodzorderreceivers.Model_OrderComboItemExtra;
import com.justfoodzorderreceivers.Model_OrderComboItemOption;
import com.justfoodzorderreceivers.MyPref;
import com.justfoodzorderreceivers.ParseLanguage;
import com.justfoodzorderreceivers.PrinterCommands;
import com.justfoodzorderreceivers.R;
import com.justfoodzorderreceivers.ReasonActivity;
import com.justfoodzorderreceivers.Status_Change_Activity;
import com.justfoodzorderreceivers.SwipeHelper;
import com.justfoodzorderreceivers.SwipeHelper2;
import com.justfoodzorderreceivers.TimeAdapter;
import com.justfoodzorderreceivers.TimeAdapter2;
import com.justfoodzorderreceivers.TimeValuesListener;
import com.justfoodzorderreceivers.Url;
import com.justfoodzorderreceivers.UtilsForPrinter;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.justfoodzorderreceivers.MainActivity.isNet;
import static com.justfoodzorderreceivers.MainActivity.player;
import static com.justfoodzorderreceivers.MainActivity.player1;

public class Home extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PrinterObserver {
    RelativeLayout rlorderday, rl_pendingorder, rl_allorder, rl_booking;
    RecyclerView rchome, rchome2, rchome3, rc_bookingtable;
    ArrayList<OrderSupportList> orderSupportLists;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ArrayList<OrderList> orderLists;
    ArrayList<BookTableList> bookTableLists;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    String anujorderId = "";
    TextView tv1, processingTv, allorder, booking;
    public static String whatopen = "";
    public static String toopen = "";
    RelativeLayout linear_message;
    TextView error_msgTxt;
    //        private SearchView mSearchView;
    public static TimerTask mTimerTask;
    final Handler handler = new Handler();
    Timer t = new Timer();
    OrderListView orderListViewToday, orderListViewPending, orderListViewComplete, orderListViewAll;
    ArrayList<FoodItemList> foodItemLists, drinkItemLists, meallist;
    String[] aa = {" 30 ", " 45 ", " 60 ", " 75 ", " 90 ", " 105 "};
    ArrayList<TimeModel> stringList;
    AuthPreference authPreference;
    // SwipeRefreshLayout swipeRefreshLayout;
    private String sCity = "", stime = "", sDate = "";
    MyPref myPref;
    ParseLanguage parseLanguage;

    int flag = 0;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    String Innerprinter_Address = "00:11:22:33:44:55";
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String rider_idAssign;


    public static int orderidd;

    TextView back_text;

    AppCompatDialog dialog;
    private String mChartsetName = "UTF-8";
    ArrayList<Model_Combo> model_combos;
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

    //    public static ArrayList<String> item_name, item_instruction, item_price, item_quant, extra_toping,
//            drink_item_name, drink_item_price, drink_item_quant, drink_instruction, drink_extra_toping,
//            meal_item_name, meal_item_price, meal_item_quant, meal_extra_toping, meal_instruction;
//
//    public static String status, PaymentMethod, name_customer, customer_phone, OrderType, customer_address, customer_instruction,
//            number_of_items_order, subTotal, DeliveryCharge, PackageFees, FoodCosts, DiscountPrice, VatTax,
//            OrderPrice, PayByLoyality, GiftCardPay, ServiceFees, extraTipAddAmount, WalletPay, SalesTaxAmount,
//            RequestAtDate, RequestAtTime, OrderAcceptedDate, OrderAcceptedTime, order_status_color_code,
//            order_reference_number, collectionTime, Table_Booking_Number, DriverFirstName, DriverLastName,
//            DriverMobileNo, DriverPhoto, rider_id, PayOptionStatus, restaurant_name, restaurant_address,
//            website_copy_right_text, instruction_note, number_of_customer_order, restaurant_mobile_number, discountOfferFreeItems, customer_email;
//
    String Currency = Activity_Splash.currency_symbol;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        rchome = (RecyclerView) v.findViewById(R.id.rchome);
        rchome2 = (RecyclerView) v.findViewById(R.id.rchome1);
        rchome3 = (RecyclerView) v.findViewById(R.id.rchome2);
        myPref = new MyPref(getContext());
        parseLanguage = new ParseLanguage(myPref.getBookingData(), getContext());
        myPref.setDocCode("");
        rc_bookingtable = (RecyclerView) v.findViewById(R.id.rc_bookingtable);
        sharedPreferences = getContext().getSharedPreferences("Order", MODE_PRIVATE);
        authPreference = new AuthPreference(getContext());
        // swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
//        mSearchView = (SearchView) v.findViewById(R.id.simpleSearchView); // inititate a search view
//        mSearchView.setQueryHint("Search View"); // set the hint text to display in the query text field
        rlorderday = (RelativeLayout) v.findViewById(R.id.rlorderday);
        rl_pendingorder = (RelativeLayout) v.findViewById(R.id.rl_pendingorder);
        rl_allorder = (RelativeLayout) v.findViewById(R.id.rl_allorder);
        rl_booking = (RelativeLayout) v.findViewById(R.id.rl_booking);
//        rldrink = (RelativeLayout) v.findViewById(R.id.rldrink);
        linear_message = (RelativeLayout) v.findViewById(R.id.linear_message);
        error_msgTxt = (TextView) v.findViewById(R.id.error_msg);
        orderLists = new ArrayList<>();
        bookTableLists = new ArrayList<>();

        tv1 = v.findViewById(R.id.tv1);
        processingTv = v.findViewById(R.id.processingTv);
        allorder = v.findViewById(R.id.allorder);
        booking = v.findViewById(R.id.booking);

        tv1.setText(parseLanguage.getParseString("Today"));

        processingTv.setText(parseLanguage.getParseString("Processing"));
        allorder.setText(parseLanguage.getParseString("All_Order"));
        booking.setText(parseLanguage.getParseString("Order_Complaints"));

        stringList = new ArrayList<TimeModel>();
        stringList.clear();
        model_combos = new ArrayList<>();
        init();
        for (int i = 0; i < aa.length; i++) {
            TimeModel sTmodel = new TimeModel();
            sTmodel.setTime(aa[i]);
            sTmodel.setSelectCategory("0");
            stringList.add(sTmodel);
        }

        //  swipeRefreshLayout.setOnRefreshListener(this);

//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                Toast.makeText(getActivity(), query, Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
////                friendListView.setTextFilterEnabled(true);
//
//                return false;
//            }
//        });

        toCheck();

        rlorderday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTimerTask();
                rchome.setVisibility(View.VISIBLE);
                rchome2.setVisibility(View.GONE);
                rchome3.setVisibility(View.GONE);

                rlorderday.setBackgroundResource(R.color.red);
                rl_pendingorder.setBackgroundResource(R.color.darkred);
                rl_allorder.setBackgroundResource(R.color.darkred);
                rl_booking.setBackgroundResource(R.color.darkred);
                TodayOrderList();
            }
        });


        if (myPref.getBookType().equals("1")) {
            rl_booking.setVisibility(View.VISIBLE);
        }
        if (myPref.getBookType().equals("0")) {
            rl_booking.setVisibility(View.GONE);
        }
        rl_pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 orderLists.clear();
                stopTask();
                rchome.setVisibility(View.VISIBLE);
                rchome2.setVisibility(View.GONE);
                rchome3.setVisibility(View.GONE);
                rlorderday.setBackgroundResource(R.color.darkred);
                rl_pendingorder.setBackgroundResource(R.color.red);
                rl_allorder.setBackgroundResource(R.color.darkred);
                rl_booking.setBackgroundResource(R.color.darkred);
//                 rldrink.setBackgroundResource(R.color.darkred);
//                 mSearchView.setVisibility(View.GONE);
//              getOrderList(Url.pending_order_url);
                PendingOrderList();
            }
        });

        rl_allorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTask();
                rchome.setVisibility(View.GONE);
                rchome2.setVisibility(View.VISIBLE);
                rchome3.setVisibility(View.GONE);
                rlorderday.setBackgroundResource(R.color.darkred);
                rl_pendingorder.setBackgroundResource(R.color.darkred);
                rl_allorder.setBackgroundResource(R.color.red);
                rl_booking.setBackgroundResource(R.color.darkred);

                AllOrderList();

            }
        });

        rl_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rchome.setVisibility(View.GONE);
                rchome2.setVisibility(View.GONE);
                rchome3.setVisibility(View.VISIBLE);
                stopTask();
                rlorderday.setBackgroundResource(R.color.darkred);
                rl_pendingorder.setBackgroundResource(R.color.darkred);
                rl_allorder.setBackgroundResource(R.color.darkred);
                rl_booking.setBackgroundResource(R.color.red);

                ordercomplains();
            }
        });

//        rldrink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                rlorderday.setBackgroundResource(R.color.green2);
//                rl_pendingorder.setBackgroundResource(R.color.darkred);
//                rl_allorder.setBackgroundResource(R.color.darkred);
//                rl_booking.setBackgroundResource(R.color.darkred);
//                rldrink.setBackgroundResource(R.color.darkred);
//                alertdrink();
//            }
//        });
        return v;
    }


    // Today Orders Tab API Call//
    public void TodayOrderList() {
        progressDialog = progressDialog.show(getActivity(), "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.today_order_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("Today's", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewOrdersHistory");
                    orderLists.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String error1 = j.getString("error");

                        if (error1.equals("0")) {
                            String id = j.getString("id");
                            String restaurant_Logo = j.getString("restaurant_Logo");
                            String order_type_img = j.getString("order_type_img");
                            String customer_name = j.getString("customer_name");
                            String orderid = j.getString("orderid");
                            String BookingID = j.getString("BookingID");
                            String order_status_msg = j.getString("order_status_msg");
                            String order_time = j.getString("order_time");
                            String order_date = j.getString("order_date");
                            String ordPrice = j.getString("ordPrice");
                            String order_status_color_code = j.getString("order_status_color_code");
                            String FoodOrderType = j.getString("FoodOrderType");
                            String restaurant_address = j.getString("customer_address");
                            String payment_mode = j.getString("payment_mode");

                            orderLists.add(new OrderList(id, restaurant_Logo, customer_name, orderid,
                                    BookingID, order_status_msg, order_time, order_date, ordPrice, order_type_img,
                                    order_status_color_code, FoodOrderType, restaurant_address, payment_mode));
                            linear_message.setVisibility(View.GONE);
                            rchome.setVisibility(View.VISIBLE);
                        } else {
                            String error_msg = j.getString("error_msg");
                            linear_message.setVisibility(View.VISIBLE);
                            rchome.setVisibility(View.GONE);
                            error_msgTxt.setText(error_msg);
                        }

                    }


                    orderListViewToday = new OrderListView(getActivity(), orderLists);
                    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rchome.setLayoutManager(linearLayoutManager);
                    rchome.setAdapter(orderListViewToday);
                    SwipeHelper swipeHelper = new SwipeHelper(getContext()) {
                        @Override
                        public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons, int pos) {
                            final OrderList item = orderListViewToday.getData().get(pos);
                            if (item.getOrder_status_msg().equalsIgnoreCase("Kitchen")) {
                                underlayButtons.add(new SwipeHelper.UnderlayButton(
                                        parseLanguage.getParseString("Complete"),
                                        0,
                                        Color.parseColor("#9fd836"),
                                        new SwipeHelper.UnderlayButtonClickListener() {
                                            @Override
                                            public void onClick(final int pos) {
                                                Log.e("SliderClick", "dd");
                                                final OrderList item = orderListViewToday.getData().get(pos);
                                                player.stop();
                                                //    final OrderList item = orderListViewPending.getData().get(pos);


                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                                                // Setting Dialog Title
//                                                alertDialog.setTitle("Harperskebab Order Receiver");

                                                // Setting Dialog Message
                                                alertDialog.setMessage(parseLanguage.getParseString("Order_Complete_Alert"));

                                                alertDialog.setPositiveButton(parseLanguage.getParseString("YESText"), new DialogInterface.OnClickListener() {
                                                    public void onClick(final DialogInterface dialog, int which) {

                                                        ordermark_complete(item.getOrderid());


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
                                        }
                                ));

                                underlayButtons.add(new SwipeHelper.UnderlayButton(
                                        parseLanguage.getParseString("Change_Time"),
                                        0,
                                        Color.parseColor("#ffa500"),
                                        new SwipeHelper.UnderlayButtonClickListener() {
                                            @Override
                                            public void onClick(final int pos) {
                                                Log.e("SliderClick", "dd");
                                                final OrderList item = orderListViewToday.getData().get(pos);
                                                player.stop();
                                                //    final OrderList item = orderListViewPending.getData().get(pos);
                                                Intent i = new Intent(getContext(), AcceptButton_activity1.class);
                                                i.putExtra("orderid", "" + item.getOrderid());
                                                startActivity(i);
                                            }
                                        }
                                ));


                            }
                            if (item.getOrder_status_msg().equalsIgnoreCase("Confirmed")) {


                                underlayButtons.add(new SwipeHelper.UnderlayButton(
                                        parseLanguage.getParseString("Processing"),
                                        0,
                                        Color.parseColor("#ffa500"),
                                        new SwipeHelper.UnderlayButtonClickListener() {
                                            @Override
                                            public void onClick(final int pos) {
                                                final OrderList item = orderListViewToday.getData().get(pos);
                                                player.stop();
                                                changestatus(item.getOrderid(), "1");
                                            }
                                        }
                                ));

                                underlayButtons.add(new SwipeHelper.UnderlayButton(
                                        parseLanguage.getParseString("Complete"),
                                        0,
                                        Color.parseColor("#9fd836"),
                                        new SwipeHelper.UnderlayButtonClickListener() {
                                            @Override
                                            public void onClick(final int pos) {
                                                final OrderList item = orderListViewToday.getData().get(pos);
                                                player.stop();
                                                changestatus(item.getOrderid(), "2");
                                            }
                                        }
                                ));


                                underlayButtons.add(new SwipeHelper.UnderlayButton(
                                        parseLanguage.getParseString("DeclineButton"),
                                        0,
                                        Color.parseColor("#fe4c3a"),
                                        new SwipeHelper.UnderlayButtonClickListener() {
                                            @Override
                                            public void onClick(final int pos) {
                                                final OrderList item = orderListViewToday.getData().get(pos);


                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                                                // Setting Dialog Title
                                                alertDialog.setTitle("Harperskebab Order Receiver");


                                                // Setting Dialog Message
                                                alertDialog.setMessage(parseLanguage.getParseString("Are_you_sure_to_decline_order"));

                                                alertDialog.setPositiveButton(parseLanguage.getParseString("YESText"), new DialogInterface.OnClickListener() {
                                                    public void onClick(final DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                        dialog.cancel();

                                                        // Write your code here to invoke YES event
//                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                                        {
                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            LayoutInflater inflater = getLayoutInflater();
                                                            final View dialogLayout = inflater.inflate(R.layout.custom_alertdialog, null);
                                                            builder.setView(dialogLayout);
                                                            final AlertDialog aa = builder.create();
                                                            final EditText edt_reason = (EditText) dialogLayout.findViewById(R.id.edt_reason);
                                                            TextView ordernumber = dialogLayout.findViewById(R.id.ordernumber);
                                                            Button submit_reason = (Button) dialogLayout.findViewById(R.id.submit_reason);
                                                            submit_reason.setText(parseLanguage.getParseString("Submit"));
                                                            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
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
//                                    Toast.makeText(Activity_Booking.this, "asakhdsakhdgs", Toast.LENGTH_SHORT).show();
                                                                    Decline("" + edt_reason.getText().toString(), item.getOrderid());
                                                                }
                                                            });

                                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
//                                    finish();
                                                                    player.start();

                                                                    aa.dismiss();

                                                                }
                                                            });
                                                            aa.show();

                                                        }

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
                                        }
                                ));

                            } else {

                                underlayButtons.add(new SwipeHelper.UnderlayButton(
                                        parseLanguage.getParseString("AcceptButton"),
                                        0,
                                        Color.parseColor("#9fd836"),
                                        new SwipeHelper.UnderlayButtonClickListener() {
                                            @Override
                                            public void onClick(final int pos) {
                                                final OrderList item = orderListViewToday.getData().get(pos);
                                                player.stop();
                                                Intent i = new Intent(getContext(), AcceptButton_activity.class);
                                                i.putExtra("orderid", "" + item.getOrderid());
                                                startActivity(i);
                                            }
                                        }
                                ));


                                underlayButtons.add(new SwipeHelper.UnderlayButton(
                                        parseLanguage.getParseString("DeclineButton"),
                                        0,
                                        Color.parseColor("#fe4c3a"),
                                        new SwipeHelper.UnderlayButtonClickListener() {
                                            @Override
                                            public void onClick(final int pos) {
                                                final OrderList item = orderListViewToday.getData().get(pos);


                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                                                // Setting Dialog Title
                                                alertDialog.setTitle("Harperskebab Order Receiver");


                                                // Setting Dialog Message
                                                alertDialog.setMessage(parseLanguage.getParseString("Are_you_sure_to_decline_order"));

                                                alertDialog.setPositiveButton(parseLanguage.getParseString("YESText"), new DialogInterface.OnClickListener() {
                                                    public void onClick(final DialogInterface dialog, int which) {

                                                        dialog.dismiss();
                                                        dialog.cancel();

                                                        // Write your code here to invoke YES event
//                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                                        {
                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            LayoutInflater inflater = getLayoutInflater();
                                                            final View dialogLayout = inflater.inflate(R.layout.custom_alertdialog, null);
                                                            builder.setView(dialogLayout);
                                                            final AlertDialog aa = builder.create();
                                                            final EditText edt_reason = (EditText) dialogLayout.findViewById(R.id.edt_reason);
                                                            TextView ordernumber = dialogLayout.findViewById(R.id.ordernumber);
                                                            Button submit_reason = (Button) dialogLayout.findViewById(R.id.submit_reason);
                                                            submit_reason.setText(parseLanguage.getParseString("Submit"));
                                                            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);

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
//                                    Toast.makeText(Activity_Booking.this, "asakhdsakhdgs", Toast.LENGTH_SHORT).show();
                                                                    Decline("" + edt_reason.getText().toString(), item.getOrderid());
                                                                }
                                                            });

                                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
//                                    finish();
                                                                    player.start();

                                                                    aa.dismiss();

                                                                }
                                                            });
                                                            aa.show();

                                                        }

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
                                        }
                                ));
                            }

                        }
                    };
                    swipeHelper.attachToRecyclerView(rchome);

                } catch (JSONException e) {
                    e.printStackTrace();
                    linear_message.setVisibility(View.VISIBLE);
                    rchome.setVisibility(View.GONE);
                    error_msgTxt.setText("Something went wrong");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("restaurant_id", "" + sharedPreferences.getString("restaurant_id", ""));
                param.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("param", "" + param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void showCustomDialog12(String s, final String order_id) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getContext(), Activity_Booking.class);
                i.putExtra("orderid", "" + order_id);
                startActivity(i);
//                        Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }


    private void showCustomDialog123(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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


    public void changestatus(final String order_id, final String status) {
        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.Order_Status, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error = jsonObject.getString("error");
                    if (error.equals("0")) {
                        String error_msg = jsonObject.getString("error_msg");
//                        Toast.makeText(Status_Change_Activity.this, error_msg, Toast.LENGTH_SHORT).show();
                        showCustomDialog12(error_msg, order_id);


                    } else {
                        String error_msg = jsonObject.getString("error_msg");
//                        Toast.makeText(Status_Change_Activity.this, error_msg, Toast.LENGTH_SHORT).show();
                        showCustomDialog123(error_msg);
                        //   finish();
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
                Toast.makeText(getContext(), message + volleyError, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", "" + order_id);
                params.put("OrderStatus", status);
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("qwqw", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    // Pending Orders Tab API Call//
    public void PendingOrderList() {
//        orderLists.clear();
        progressDialog = progressDialog.show(getActivity(), "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.pending_order_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("Pending", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewOrdersHistory");
                    orderLists.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String error1 = j.getString("error");

                        if (error1.equals("0")) {
                            String id = j.getString("id");
                            String restaurant_Logo = j.getString("restaurant_Logo");
                            String order_type_img = j.getString("order_type_img");
                            String customer_name = j.getString("customer_name");
                            String orderid = j.getString("orderid");
                            String BookingID = j.getString("BookingID");
                            String order_status_msg = j.getString("order_status_msg");
                            String order_time = j.getString("order_time");
                            String order_date = j.getString("order_date");
                            String ordPrice = j.getString("ordPrice");
//                            String restaurant_lat = j.getString("restaurant_lat");
//                            String restaurant_lng = j.getString("restaurant_lng");
//                            String customer_lat = j.getString("customer_lat");
//                            String customer_lng = j.getString("customer_lng");
//                            String rider_last_lng = j.getString("rider_last_lng");
//                            String rider_last_lat = j.getString("rider_last_lat");
                            String order_status_color_code = j.getString("order_status_color_code");
                            String FoodOrderType = j.getString("FoodOrderType");
//                            authPreference.setrestaurant_lat(restaurant_lat);
//                            authPreference.setrestaurant_lng(restaurant_lng);
//                            authPreference.setcustomer_lat(customer_lat);
//                            authPreference.setcustomer_lng(customer_lng);
//                            authPreference.setrider_last_lat(rider_last_lat);
//                            authPreference.setrider_last_lng(rider_last_lng);


                            String restaurant_address = j.getString("customer_address");
                            String payment_mode = j.getString("payment_mode");

                            orderLists.add(new OrderList(id, restaurant_Logo, customer_name, orderid,
                                    BookingID, order_status_msg, order_time, order_date, ordPrice, order_type_img,
                                    order_status_color_code, FoodOrderType, restaurant_address, payment_mode));
                            linear_message.setVisibility(View.GONE);
                            rchome.setVisibility(View.VISIBLE);
                        } else {
                            String error_msg = j.getString("error_msg");
                            linear_message.setVisibility(View.VISIBLE);
                            rchome.setVisibility(View.GONE);
                            error_msgTxt.setText(error_msg);
                        }

                        orderListViewPending = new OrderListView(getActivity(), orderLists);
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rchome.setLayoutManager(linearLayoutManager);
                        rchome.setAdapter(orderListViewPending);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("restaurant_id", "" + sharedPreferences.getString("restaurant_id", ""));
                param.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("param", "" + param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void ordermark_complete(final String order_id) {
        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, false);
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

                        showCustomDialog(success_msg, "");
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
                String message = "";
                if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
                    message = getString(R.string.check_internet);
                } else {
                    message = "Please Check your network connection";
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("orderid", "" + order_id);
                params.put("OrderStatus", "2");
                params.put("lang_code", myPref.getCustomer_default_langauge());
//                params.put("DriverComment", a);
//                Log.e("pa",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    // All Orders Tab API Call//
    public void AllOrderList() {
        progressDialog = progressDialog.show(getActivity(), "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.all_order_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("All list", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewOrdersHistory");
                    orderLists.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String error1 = j.getString("error");

                        if (error1.equals("0")) {
                            String id = j.getString("id");
                            String restaurant_Logo = j.getString("restaurant_Logo");
                            String order_type_img = j.getString("order_type_img");
                            String customer_name = j.getString("customer_name");
                            String orderid = j.getString("orderid");
                            String BookingID = j.getString("BookingID");
                            String order_status_msg = j.getString("order_status_msg");
                            String order_time = j.getString("order_time");
                            String order_date = j.getString("order_date");
                            String ordPrice = j.getString("ordPrice");
                            String order_status_color_code = j.getString("order_status_color_code");
                            String FoodOrderType = j.getString("FoodOrderType");
                            String restaurant_address = j.getString("customer_address");
                            String payment_mode = j.getString("payment_mode");

                            orderLists.add(new OrderList(id, restaurant_Logo, customer_name, orderid,
                                    BookingID, order_status_msg, order_time, order_date, ordPrice, order_type_img,
                                    order_status_color_code, FoodOrderType, restaurant_address, payment_mode));
                            linear_message.setVisibility(View.GONE);
                            rchome2.setVisibility(View.VISIBLE);
                            orderListViewAll = new OrderListView(getActivity(), orderLists);
                            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rchome2.setLayoutManager(linearLayoutManager);
                            rchome2.setAdapter(orderListViewAll);
                        } else {

                            rchome.setVisibility(View.GONE);
                            rchome3.setVisibility(View.GONE);
                            rchome2.setVisibility(View.GONE);
                            String error_msg = j.getString("error_msg");
                            linear_message.setVisibility(View.VISIBLE);

                            error_msgTxt.setText(error_msg);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    rchome.setVisibility(View.GONE);
                    rchome3.setVisibility(View.GONE);
                    rchome2.setVisibility(View.GONE);
                    linear_message.setVisibility(View.VISIBLE);

                    error_msgTxt.setText("Something went wrong !!!");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("restaurant_id", "" + sharedPreferences.getString("restaurant_id", ""));
                param.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("param", "" + param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    // Booking Orders Tab API Call//
    public void BookingOrderList() {
//        bookTableLists.clear();
        progressDialog = progressDialog.show(getActivity(), "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.book_order_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("Booking list", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewTableBookingHistory");
                    bookTableLists.clear();
                    orderLists.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String error1 = j.getString("error");

                        if (error1.equals("0")) {
                            String id = j.getString("id");
                            String booking_id = j.getString("booking_id");
                            String noofgusts = j.getString("noofgusts");
                            String booking_name = j.getString("booking_name");
                            String booking_email = j.getString("booking_email");
                            String booking_mobile = j.getString("booking_mobile");
                            String TableNumberDisplay = j.getString("TableNumberDisplay");
                            String booking_status_msg = j.getString("booking_status_msg");
                            String orderid = j.getString("orderid");
                            String booking_instruction = j.getString("booking_instruction");
                            String booking_date = j.getString("booking_date");
                            String booking_time = j.getString("booking_time");
                            bookTableLists.add(new BookTableList(id, booking_id, noofgusts, booking_name, booking_email, booking_mobile
                                    , TableNumberDisplay, booking_status_msg, orderid, booking_instruction, booking_date, booking_time));
                            linear_message.setVisibility(View.GONE);
                            rchome3.setVisibility(View.VISIBLE);
                        } else {
                            String error_msg = j.getString("error_msg");
//                            Toast.makeText(getActivity(), error_msg, Toast.LENGTH_SHORT).show();
                            linear_message.setVisibility(View.VISIBLE);
                            rchome3.setVisibility(View.GONE);
                            error_msgTxt.setText(error_msg);
                        }
                        BookingListView bookingListView = new BookingListView(getActivity(), bookTableLists);
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rchome3.setLayoutManager(linearLayoutManager);
                        rchome3.setAdapter(bookingListView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("restaurant_id", "" + sharedPreferences.getString("restaurant_id", ""));
                param.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("param", "" + param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void ordercomplains() {
        orderSupportLists = new ArrayList<>();
        orderSupportLists.clear();
        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.ordercomplainhistory, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response", "" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewComplaintsHistory");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String error = jsonObject1.getString("error");
                        if (error.equals("0")) {
                            String id = jsonObject1.getString("id");
                            String complaint_id = jsonObject1.getString("complaint_id");

                            String orderIssue = jsonObject1.getString("orderIssue");
                            String contact_name = jsonObject1.getString("contact_name");
                            String contact_email = jsonObject1.getString("contact_email");
                            String contact_phone = jsonObject1.getString("contact_phone");
                            String resid = jsonObject1.getString("resid");
                            String orderIDNumber = jsonObject1.getString("orderIDNumber");
                            String orderIssueEmail = jsonObject1.getString("orderIssueEmail");
                            String orderIssueMessage = jsonObject1.getString("orderIssueMessage");
                            String restaurant_order_issue_reply = jsonObject1.getString("restaurant_order_issue_reply");
                            String restaurant_order_issue_date = jsonObject1.getString("restaurant_order_issue_date");
                            String restaurant_name = jsonObject1.getString("restaurant_name");
                            String restaurant_address = jsonObject1.getString("restaurant_address");
                            String restaurant_city = jsonObject1.getString("restaurant_city");
                            String restaurant_postcode = jsonObject1.getString("restaurant_postcode");
                            String restaurant_mobile_number = jsonObject1.getString("restaurant_mobile_number");


                            orderSupportLists.add(new OrderSupportList(id, complaint_id, orderIssue, contact_name, contact_email, contact_phone, resid,
                                    orderIDNumber, orderIssueEmail, orderIssueMessage, restaurant_order_issue_reply, restaurant_order_issue_date,
                                    restaurant_name, restaurant_mobile_number));

                            linear_message.setVisibility(View.GONE);
                            rchome3.setVisibility(View.VISIBLE);

                        } else if (error.equals("1")) {
                            String error_msg = jsonObject1.getString("error_msg");
                            linear_message.setVisibility(View.VISIBLE);
                            rchome3.setVisibility(View.GONE);
                            error_msgTxt.setText(error_msg);
                        } else {
                            String error_msg = jsonObject1.getString("error_msg");
                            linear_message.setVisibility(View.GONE);
                            rchome3.setVisibility(View.VISIBLE);
                            error_msgTxt.setText(error_msg);
                        }


                    }
                    OrderSupportListREcycler OrderSupportListREcycler = new OrderSupportListREcycler(getContext(), orderSupportLists);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rchome3.setLayoutManager(linearLayoutManager);
                    rchome3.setAdapter(OrderSupportListREcycler);

                } catch (JSONException e) {
                    e.printStackTrace();
                    linear_message.setVisibility(View.GONE);
                    rchome3.setVisibility(View.VISIBLE);
                    error_msgTxt.setText("Something went wrong");
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurant_id", "" + sharedPreferences.getString("restaurant_id", ""));
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("anuj", "" + params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void printerObserverCallback(PrinterInterface printerInterface, int i) {

    }

    @Override
    public void printerReadMsgCallback(PrinterInterface printerInterface, byte[] bytes) {

    }


    public class OrderSupportListREcycler extends RecyclerView.Adapter<OrderSupportListREcycler.ViewHolder> {

        Context context;
        List<OrderSupportList> anujs;
        MyPref myPref;
        ParseLanguage parseLanguage;


        public OrderSupportListREcycler(Context c, ArrayList<OrderSupportList> p) {
            this.context = c;
            this.anujs = p;
            myPref = new MyPref(context);
            parseLanguage = new ParseLanguage(myPref.getBookingData(), context);
        }

        public OrderSupportListREcycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.single_complain_list, parent, false);
            return new OrderSupportListREcycler.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderSupportListREcycler.ViewHolder viewHolder, int i) {
            Log.e("cc", "" + anujs);

            final OrderSupportList supportList = anujs.get(i);
            viewHolder.tv_issue.setText(supportList.getOrderIssue());
            viewHolder.tv_namee.setText(supportList.getContact_name());
            viewHolder.tv_orderid.setText(supportList.getOrderIDNumber());
            viewHolder.tv_reply.setText(supportList.getOrderIssueMessage());
            viewHolder.txtnumber.setText(supportList.getContact_phone());
            viewHolder.txtemail.setText(supportList.getContact_email());
            viewHolder.your_reply.setText(supportList.getRestaurant_order_issue_reply());
            viewHolder.replybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), ReasonActivity.class);
                    i.putExtra("complaint_id", supportList.getComplaint_id());
                    i.putExtra("orderIDNumber", supportList.getOrderIDNumber());
                    i.putExtra("contact_name", supportList.getContact_name());
                    i.putExtra("contact_email", supportList.getContact_email());
                    i.putExtra("resid", supportList.getResid());


                    startActivity(i);
                }
            });
            viewHolder.img_delete.setTag(i);
            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();

                    DeleteComplaint(anujs.get(pos).getComplaint_id());
                }
            });


        }

        @Override
        public int getItemCount() {
            return anujs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_issue, tv_namee, tv_reply, your_reply, tv_orderid, txtnumber, txtemail;
            TextView issue_title, name_title, order_title, mobile_title, email_title, comment_title, your_reply_title;
            Button replybutton;
            ImageView img_delete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_issue = (TextView) itemView.findViewById(R.id.tv_issue);
                tv_namee = (TextView) itemView.findViewById(R.id.tv_namee);
                tv_reply = (TextView) itemView.findViewById(R.id.tv_reply);
                your_reply = (TextView) itemView.findViewById(R.id.your_reply);
                tv_orderid = (TextView) itemView.findViewById(R.id.tv_orderid);
                txtnumber = (TextView) itemView.findViewById(R.id.txtnumber);
                txtemail = (TextView) itemView.findViewById(R.id.txtemail);
                replybutton = (Button) itemView.findViewById(R.id.replybutton);
                issue_title = (TextView) itemView.findViewById(R.id.issue_title);
                name_title = (TextView) itemView.findViewById(R.id.name_title);
                order_title = (TextView) itemView.findViewById(R.id.order_title);
                mobile_title = (TextView) itemView.findViewById(R.id.mobile_title);
                email_title = (TextView) itemView.findViewById(R.id.email_title);
                comment_title = (TextView) itemView.findViewById(R.id.comment_title);
                your_reply_title = (TextView) itemView.findViewById(R.id.your_reply_title);

                issue_title.setText(parseLanguage.getParseString("Issue_Type"));
                name_title.setText(parseLanguage.getParseString("Name"));
                order_title.setText(parseLanguage.getParseString("Order_No"));
                mobile_title.setText(parseLanguage.getParseString("Mobile_No"));
                email_title.setText(parseLanguage.getParseString("Email_Id"));
                comment_title.setText(parseLanguage.getParseString("Issue_Comment"));
                your_reply_title.setText(parseLanguage.getParseString("Your_Reply"));

                replybutton.setText(parseLanguage.getParseString("Reply"));
                img_delete = itemView.findViewById(R.id.img_delete);


            }
        }
    }


    // Today Orders without Process Tab API Call//
    public void TodayOrderRepeatList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.today_order_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressDialog.dismiss();
                Log.e("Today Repeated list", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("NewOrdersHistory");
                    orderLists.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = jsonArray.getJSONObject(i);
                        String error1 = j.getString("error");

                        if (error1.equals("0")) {
                            String id = j.getString("id");
                            String restaurant_Logo = j.getString("restaurant_Logo");
                            String order_type_img = j.getString("order_type_img");
                            String customer_name = j.getString("customer_name");
                            String orderid = j.getString("orderid");
                            String BookingID = j.getString("BookingID");
                            String order_status_msg = j.getString("order_status_msg");
                            String order_time = j.getString("order_time");
                            String order_date = j.getString("order_date");
                            String ordPrice = j.getString("ordPrice");
                            String order_status_color_code = j.getString("order_status_color_code");
                            String FoodOrderType = j.getString("FoodOrderType");

                            String restaurant_address = j.getString("customer_address");
                            String payment_mode = j.getString("payment_mode");
                            orderLists.add(new OrderList(id, restaurant_Logo, customer_name, orderid,
                                    BookingID, order_status_msg, order_time, order_date, ordPrice, order_type_img,
                                    order_status_color_code, FoodOrderType, restaurant_address, payment_mode));
                            linear_message.setVisibility(View.GONE);
                            rchome.setVisibility(View.VISIBLE);
                            if (order_status_msg.equalsIgnoreCase("Pending") || order_status_msg.equalsIgnoreCase("Waiting")) {
                                if (jsonArray.length() > 0 && dialog == null) {
                                    if (!myPref.getDocCode().equals("1")) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        customer_name = jsonObject1.getString("customer_name");
                                        orderid = jsonObject1.getString("orderid");
                                        BookingID = jsonObject1.getString("BookingID");
                                        order_status_msg = jsonObject1.getString("order_status_msg");
                                        order_time = jsonObject1.getString("order_time");
                                        order_date = jsonObject1.getString("order_date");
                                        ordPrice = jsonObject1.getString("ordPrice");
                                        order_status_color_code = jsonObject1.getString("order_status_color_code");
                                        FoodOrderType = jsonObject1.getString("FoodOrderType");

                                        restaurant_address = jsonObject1.getString("customer_address");
                                        payment_mode = jsonObject1.getString("payment_mode");
                                        dialog = new AppCompatDialog(getContext());
                                        dialog.setContentView(R.layout.order_dialog);
                                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                        dialog.show();
                                        dialog.setCancelable(false);

                                        //   player.start();

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
                                        time.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

                                        final TimeAdapter2 timeAdapter2 = new TimeAdapter2(getContext(), stringList, new TimeValuesListener() {
                                            @Override
                                            public void onSelectTime(String price) {
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
                                        accept.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                if (stime.equals("")) {
                                                    Toast.makeText(getActivity(), parseLanguage.getParseString("Please_Select_Time"), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    countDownTimer.cancel();
                                                    //  player.stop();
                                                    player.stop();
                                                    orderconfirm(finalOrderid);
                                                }

                                            }
                                        });


                                        final String finalOrderid1 = orderid;
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                                                // Setting Dialog Title
                                                alertDialog.setTitle("Harperskebab Order Receiver");


                                                // Setting Dialog Message
                                                alertDialog.setMessage(parseLanguage.getParseString("Are_you_sure_to_decline_order"));

                                                alertDialog.setPositiveButton(parseLanguage.getParseString("YESText"), new DialogInterface.OnClickListener() {
                                                    public void onClick(final DialogInterface dialog, int which) {
                                                        countDownTimer.cancel();
                                                        dialog.dismiss();
                                                        dialog.cancel();

                                                        // Write your code here to invoke YES event
//                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                                        {
                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                                                                    Decline("" + edt_reason.getText().toString(), finalOrderid1);
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

                                    }
                                }
                            }


                        } else {
                            String error_msg = j.getString("error_msg");
                            linear_message.setVisibility(View.VISIBLE);
                            rchome.setVisibility(View.GONE);
                            error_msgTxt.setText(error_msg);
//               Toast.makeText(getActivity(), error_msg, Toast.LENGTH_SHORT).show();
                        }

                        orderListViewToday = new OrderListView(getContext(), orderLists);
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rchome.setLayoutManager(linearLayoutManager);
                        rchome.setAdapter(orderListViewToday);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("restaurant_id", "" + sharedPreferences.getString("restaurant_id", ""));
                param.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("param", "" + param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {


        if (MainActivity.isNet) {

            player1.stop();

        } else {

            player1.start();
        }
        toCheck();
    }


    public class OrderListView extends RecyclerView.Adapter<OrderListView.ViewHolder> {

        Context context;

        List<OrderList> pp;
        DecimalFormat decimalformat=new DecimalFormat("##,00");

        public OrderListView(Context c, ArrayList<OrderList> p) {
            this.context = c;
            this.pp = p;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.temp, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.tv_name.setText(pp.get(position).getCustomer_name());

            if (myPref.getCustomer_default_langauge().equals("en")) {

            } else {

            }
            if (myPref.getCustomer_default_langauge().equals("en")) {
                holder.tv_ordernumber.setText(pp.get(position).getBookingID());
            } else {
                holder.tv_ordernumber.setText(pp.get(position).getBookingID());
            }

            holder.tv_date.setText(pp.get(position).getOrder_date());
//            holder.tv_status.setText(pp.get(position).getOrder_status_msg());
            holder.tv_status.setTextColor(Color.parseColor(pp.get(position).getorder_status_color_code()));
            String price=pp.get(position).getordPrice();
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                price=price.replace(".", ",");
                holder.tv_amount.setText(Activity_Splash.currency_symbol+price);
            }
            else {
                holder.tv_amount.setText(Activity_Splash.currency_symbol+price);
            }
//            holder.tv_amount.setText(Activity_Splash.currency_symbol + pp.get(position).getordPrice());
            holder.tv_time.setText(pp.get(position).getOrder_time());
            holder.foodtype.setText(pp.get(position).getPayment_mode());
            Picasso.get().load(pp.get(position).getorder_type_img()).into(holder.iv_order);
            if (pp.get(position).getRestaurant_address() != null && !pp.get(position).getRestaurant_address().equalsIgnoreCase("null")) {
                holder.tv_address.setVisibility(View.VISIBLE);
                holder.tv_address.setText(pp.get(position).getRestaurant_address());
            } else {
                holder.tv_address.setVisibility(View.GONE);
            }

            if (pp.get(position).getOrder_status_msg().contains("Pending")) {
                holder.tv_status.setText(parseLanguage.getParseString("Pending"));
                holder.liner_new.setVisibility(View.GONE);
                holder.view_color.setBackgroundResource(R.drawable.orange);
            }
           else  if (pp.get(position).getOrder_status_msg().equalsIgnoreCase("Cancelled")) {
                holder.tv_status.setText(parseLanguage.getParseString("Cancelled"));
                holder.liner_new.setVisibility(View.GONE);
                holder.view_color.setBackgroundResource(R.drawable.orange);
            }

           else  if (pp.get(position).getOrder_status_msg().equalsIgnoreCase("Delivered")) {
                holder.tv_status.setText(parseLanguage.getParseString("Delivered"));
                holder.liner_new.setVisibility(View.GONE);
                holder.view_color.setBackgroundResource(R.drawable.green);
            }

           else  if (pp.get(position).getOrder_status_msg().equalsIgnoreCase("Kitchen")) {
                holder.tv_status.setText(parseLanguage.getParseString("Processing"));
                holder.view_color.setBackgroundResource(R.drawable.yellow);
            }
           else  if (
                    pp.get(position).getOrder_status_msg().contains("Confirmed")) {
                holder.tv_status.setText(parseLanguage.getParseString("Confirmed"));
                holder.view_color.setBackgroundResource(R.drawable.blue);
            }



            holder.linear_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(context, "AAAAAA"+position, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, Activity_Booking.class);
                    i.putExtra("orderid", "" + pp.get(position).getOrderid());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public void removeItem(int position) {
            pp.remove(position);
            notifyItemRemoved(position);
        }

        public void restoreItem(OrderList item, int position) {
            pp.add(position, item);
            notifyItemInserted(position);
        }

        public List<OrderList> getData() {
            return pp;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_name, tv_ordernumber, tv_date, tv_amount, tv_time, foodtype, tv_address;
            ImageView iv_order;
            LinearLayout linear_list, liner_new;
            TextView tv_status, view_deatils;
            public CardView forground_card;
            View view_color;
            public RelativeLayout viewBackground;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_ordernumber = (TextView) itemView.findViewById(R.id.tv_ordernumber);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                view_deatils = itemView.findViewById(R.id.view_deatils);
                tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                foodtype = (TextView) itemView.findViewById(R.id.foodtype);
                tv_address = itemView.findViewById(R.id.tv_address);
                iv_order = (ImageView) itemView.findViewById(R.id.iv_order);
                linear_list = (LinearLayout) itemView.findViewById(R.id.linear_list);
                liner_new = (LinearLayout) itemView.findViewById(R.id.liner_new);
                view_color = itemView.findViewById(R.id.view_color);
                viewBackground = itemView.findViewById(R.id.background_rl);
                forground_card = itemView.findViewById(R.id.forground_card);

            }
        }
    }

    public class BookingListView extends RecyclerView.Adapter<BookingListView.ViewHolder> {
        Context context;
        ArrayList<BookTableList> pp;

        public BookingListView(Context c, ArrayList<BookTableList> p) {
            this.context = c;
            this.pp = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.single_booking_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.tv_bookingno.setText(pp.get(position).getBooking_id());
            holder.tv_guestno.setText(pp.get(position).getNoofgusts());
            holder.tv_tablebookingno.setText(pp.get(position).getTableNumberDisplay());
            holder.tv_namee.setText(pp.get(position).getBooking_name());
            holder.tv_mobileno.setText(pp.get(position).getBooking_mobile());
            holder.tv_email.setText(pp.get(position).getBooking_email());
            holder.pending.setText(pp.get(position).getBooking_status_msg());
            holder.Booking_instruction.setText(pp.get(position).getBooking_instruction());
            holder.tv_bookingdate.setText(pp.get(position).getbooking_date());
            holder.tv_bookingtime.setText(pp.get(position).getbooking_time());

            if (pp.get(position).getBooking_status_msg().contains("Cancelled")) {
                holder.status_change.setVisibility(View.GONE);
                holder.pending.setTextColor(Color.RED);
            } else if (pp.get(position).getBooking_status_msg().contains("Confirmed")) {
                holder.status_change.setVisibility(View.GONE);
//                holder.pending.setTextColor(Color.parseColor(String.valueOf(R.color.green2)));
                holder.pending.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder.status_change.setVisibility(View.VISIBLE);
                holder.pending.setTextColor(Color.BLUE);
            }
            holder.order_id.setText(pp.get(position).getorderid());

            holder.status_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    anujorderId = holder.order_id.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.custom_alertbox, null);
                    builder.setView(dialogLayout);
                    Button confirmed = (Button) dialogLayout.findViewById(R.id.confirmed);
                    Button reject = (Button) dialogLayout.findViewById(R.id.reject);
                    Button close = (Button) dialogLayout.findViewById(R.id.close);
                    final AlertDialog alertDialog = builder.create();

                    confirmed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            progressDialog = progressDialog.show(getActivity(),"","Please wait...",false,false);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.confirmed_order, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("tableconfirresponse", "" + response);
//                                    progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int success = jsonObject.getInt("error");
                                        if (success == 0) {
                                            String success_msg = jsonObject.getString("error_msg");
//                                            Toast.makeText(getActivity(), success_msg, Toast.LENGTH_SHORT).show();
//                                            alertDialog.dismiss();
                                            showCustomDialogdecline(success_msg);
                                            alertDialog.dismiss();
//                                            getBookingList();
                                        } else {
                                            String success_msg = jsonObject.getString("error_msg");
//                                            Toast.makeText(getActivity(), success_msg, Toast.LENGTH_SHORT).show();
                                            showCustomDialog1decline(success_msg);
                                            alertDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                    progressDialog.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> param = new HashMap<>();
                                    param.put("orderid", "" + anujorderId);
                                    param.put("OrderStatus", "1");
                                    param.put("lang_code", myPref.getCustomer_default_langauge());
                                    Log.e("param", "" + param);
                                    return param;
                                }
                            };
                            requestQueue.add(stringRequest);
                        }

                    });


                    //////////Close button of alert dialog ///////////////////////
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
//                            Toast.makeText(getActivity(), "close", Toast.LENGTH_SHORT).show();

                        }
                    });

                    //////////Reject button of alert dialog ///////////////////////

                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


//                            Rejectorder();

                            progressDialog = progressDialog.show(getActivity(), "", parseLanguage.getParseString("Please_wait"), false, false);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.confirmed_order, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                    Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int success = jsonObject.getInt("error");
//                                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                                        if (success == 0) {
                                            String success_msg = jsonObject.getString("error_msg");
//                                            Toast.makeText(getActivity(), success_msg, Toast.LENGTH_SHORT).show();
                                            showCustomDialogdecline(success_msg);
                                            alertDialog.dismiss();
//                                            getBookingList();

                                        } else {
                                            String success_msg = jsonObject.getString("error_msg");
//                                            Toast.makeText(getActivity(), success_msg, Toast.LENGTH_SHORT).show();
                                            showCustomDialog1decline(success_msg);
                                            alertDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> param = new HashMap<>();
                                    param.put("orderid", "" + anujorderId);
                                    param.put("OrderStatus", "2");
                                    param.put("lang_code", myPref.getCustomer_default_langauge());
                                    Log.e("param", "" + param);
                                    return param;
                                }
                            };
                            requestQueue.add(stringRequest);
                        }
                    });

                    alertDialog.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return pp.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_bookingno, tv_guestno, tv_tablebookingno, tv_namee, tv_mobileno, tv_email, tv_bookingdate, tv_bookingtime;
            ImageView iv_order;
            Button status_change;
            TextView pending, order_id, Booking_instruction;


            public ViewHolder(View itemView) {
                super(itemView);
                tv_bookingno = (TextView) itemView.findViewById(R.id.tv_bookingno);
                tv_guestno = (TextView) itemView.findViewById(R.id.tv_guestno);
                tv_tablebookingno = (TextView) itemView.findViewById(R.id.tv_tablebookingno);
                tv_namee = (TextView) itemView.findViewById(R.id.tv_namee);
                tv_mobileno = (TextView) itemView.findViewById(R.id.tv_mobileno);
                tv_email = (TextView) itemView.findViewById(R.id.tv_email);
                tv_bookingdate = (TextView) itemView.findViewById(R.id.tv_bookingdate);
                tv_bookingtime = (TextView) itemView.findViewById(R.id.tv_bookingtime);
                pending = (TextView) itemView.findViewById(R.id.pending);
                order_id = (TextView) itemView.findViewById(R.id.order_id);
//                confirmed =(Button) itemView.findViewById(R.id.confirmed);
                status_change = (Button) itemView.findViewById(R.id.status_change);
                Booking_instruction = (TextView) itemView.findViewById(R.id.special_insst);

            }
        }

    }

    public void toCheck() {
        // swipeRefreshLayout.setRefreshing(false);
        whatopen = "1";
        if (toopen.equals("M")) {
            doTimerTask();


            if (isNet) {
                if (player1 != null) {
                    player1.stop();
                }

            } else {
                if (player1 != null) {
                    player1.start();
                }
            }
            rchome.setVisibility(View.VISIBLE);
            rchome2.setVisibility(View.GONE);
            rchome3.setVisibility(View.GONE);
            rlorderday.setBackgroundResource(R.color.red);
            rl_pendingorder.setBackgroundResource(R.color.darkred);
            rl_allorder.setBackgroundResource(R.color.darkred);
            rl_booking.setBackgroundResource(R.color.darkred);
//            rldrink.setBackgroundResource(R.color.darkred);
            //  getOrderList(Url.today_order_url);
            TodayOrderList();

        } else if (toopen.equals("A")) {

            if (isNet) {

                player1.stop();

            } else {

                player1.start();
            }
            stopTask();
            rchome.setVisibility(View.GONE);
            rchome2.setVisibility(View.VISIBLE);
            rchome3.setVisibility(View.GONE);
            rlorderday.setBackgroundResource(R.color.darkred);
            rl_pendingorder.setBackgroundResource(R.color.darkred);
            rl_allorder.setBackgroundResource(R.color.red);
            rl_booking.setBackgroundResource(R.color.darkred);
//            rldrink.setBackgroundResource(R.color.darkred);
//            mSearchView.setVisibility(View.GONE);
//            getOrderList(Url.all_order_url);
            AllOrderList();

        } else if (toopen.equals("T")) {

            if (isNet) {

                player1.stop();

            } else {

                player1.start();
            }
            stopTask();
            rchome.setVisibility(View.GONE);
            rchome2.setVisibility(View.GONE);
            rchome3.setVisibility(View.VISIBLE);
            rlorderday.setBackgroundResource(R.color.darkred);
            rl_pendingorder.setBackgroundResource(R.color.darkred);
            rl_allorder.setBackgroundResource(R.color.darkred);
//            rldrink.setBackgroundResource(R.color.darkred);
            rl_booking.setBackgroundResource(R.color.red);
//            mSearchView.setVisibility(View.VISIBLE);
//            getBookingList();
            ordercomplains();
        }
    }

    public void doTimerTask() {

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        TodayOrderRepeatList();

                        if (isNet) {
                            if (player1 != null) {
                                player1.stop();
                            }

                        } else {
                            if (player1 != null) {
                                player1.start();
                            }
                        }

                        Log.d("TIMER", "TimerTask run");
                    }
                });
            }
        };


        t.schedule(mTimerTask, 1000, 10000);

    }

    public static void stopTask() {

        if (mTimerTask != null) {

//            Toast.makeText(getActivity(), "time end", Toast.LENGTH_SHORT).show();

            Log.d("TIMER", "timer canceled");
            mTimerTask.cancel();
        }

    }

    private void showCustomDialogdecline(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(parseLanguage.getParseString("AlertText"));
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton(parseLanguage.getParseString("OKText"), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.show();

    }

    private void showCustomDialog1decline(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(parseLanguage.getParseString("AlertText"));
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.cancel);

        alertDialog.setButton(parseLanguage.getParseString("OKText"), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

//                alertDialog.dismiss();
//                Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private RTPrinter rtPrinter = null;
    private TextSetting textSetting;
    private PrinterFactory printerFactory;
    private Object configObj;
    private PrinterPowerUtil printerPowerUtil;
    private int checkedConType = BaseEnum.CON_COM;

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

    public void init() {

        textSetting = new TextSetting();

        //Initialize Thermalprinter
        BaseApplication.instance.setCurrentCmdType(BaseEnum.CMD_ESC);
        printerFactory = new ThermalPrinterFactory();
        rtPrinter = printerFactory.create();

        PrinterObserverManager.getInstance().add(this);//添加连接状态监听

        configObj = new SerialPortConfigBean().getDefaultConfig();
        printerPowerUtil = new PrinterPowerUtil(getContext());
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


    public void orderconfirm(final String Order_id) {
        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, false);
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
                            getorderdetails(Order_id, error_msg);
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
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

    private void showCustomDialog(String s, final String OrderId) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(parseLanguage.getParseString("AlertText"));
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton(parseLanguage.getParseString("OKText"), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);


//                        Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void showCustomDialog1(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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

            Toast.makeText(getActivity(), "Bluetooth Device Found.", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(getActivity(), "Bluetooth Opened", Toast.LENGTH_SHORT).show();

            //myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getorderdetails(final String orderID, final String error_msg) {
        item_name = new ArrayList<>();
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


        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.order_booking_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
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
                        PayOptionStatus = jsonObject1.getString("PayOptionStatus");
                        restaurant_name = jsonObject1.getString("restaurant_name");
                        restaurant_address = jsonObject1.getString("restaurant_address");
                        website_copy_right_text = jsonObject1.getString("website_copy_right_text");
                        instruction_note = jsonObject1.getString("instruction_note");
                        rider_idAssign = rider_id;
                        discountOfferFreeItems = jsonObject1.getString("discountOfferFreeItems");
                        rider_idAssign = rider_id;
                        customer_email = jsonObject1.getString("customer_email");

                        authPreference.setDriverFirstName(DriverFirstName);
                        authPreference.setDriverLastName(DriverLastName);
                        authPreference.setDriverMobileNo(DriverMobileNo);
                        authPreference.setDriverPhoto(DriverPhoto);

                        JSONArray jsonArray1 = jsonObject.getJSONArray("OrderFoodItem");
                        if (jsonArray1.length() == 0) {
                            // tv_no_foodItems.setVisibility(View.GONE);
                            //recycler_fooditem.setVisibility(View.GONE);
                        } else {
                            //tv_no_foodItems.setText("Food Menu");
                            //tv_no_foodItems.setTextColor(getResources().getColor(R.color.green));

                            for (int ii = 0; ii < jsonArray1.length(); ii++) {
                                JSONObject jsonObject12 = jsonArray1.getJSONObject(ii);
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

                        JSONArray jsonArray2 = jsonObject.getJSONArray("OrderDrinkItem");
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


                        JSONArray OrderMealItem = jsonObject.getJSONArray("OrderMealItem");
                        if (OrderMealItem.length() == 0) {
                            //   tv_meal.setVisibility(View.GONE);
                            // rcmeal.setVisibility(View.GONE);
                        } else {
                            //        tv_meal.setText("Meal Menu");
                            //      tv_meal.setTextColor(getResources().getColor(R.color.red));
                            for (int ii = 0; ii < OrderMealItem.length(); ii++) {
                                JSONObject j2 = OrderMealItem.getJSONObject(ii);
                                String ItemsName = j2.getString("ItemsName");
                                String quantity = j2.getString("quantity");
                                String menuprice = j2.getString("menuprice");
                                String item_size = j2.getString("item_size");
                                String instructions = j2.getString("instructions");
                                String ExtraTopping = j2.getString("ExtraTopping");
                                String Currencyyy = j2.getString("Currency");


                                meal_item_name.add(ItemsName);
                                meal_item_price.add(menuprice);
                                meal_item_quant.add(quantity);
                                meal_instruction.add(instructions);
                                meal_extra_toping.add(ExtraTopping);
                                meallist.add(new FoodItemList(ItemsName, quantity, menuprice, item_size, ExtraTopping, Currency));
                            }
                            //     Activity_Booking.MealListView mealListView = new Activity_Booking.MealListView(Activity_Booking.this, meallist);
                            //   linearLayoutManager2 = new LinearLayoutManager(Activity_Booking.this, LinearLayoutManager.VERTICAL, false);
                            //  rcmeal.setLayoutManager(linearLayoutManager2);
                            //rcmeal.setAdapter(mealListView);
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
                        player.stop();
                        findBT();
                        openBT();
                        sendData();
                        doConnect();
                        PrintOrderReceipt();
                        showCustomDialog(error_msg, orderID);
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
                progressDialog.dismiss();
                Log.e("error", "" + volleyError);
                Toast.makeText(getContext(), parseLanguage.getParseString("Please_Check_your_network_connection"), Toast.LENGTH_SHORT).show();
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

    private void PrintOrderReceipt() throws UnsupportedEncodingException {
        if (rtPrinter != null) {
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
            if (collectionTime.equalsIgnoreCase("null")) {
                escCmd.append(escCmd.getTextCmd(orderReadyAt, parseLanguage.getParseString("Order_ready_at")));
            } else {
                escCmd.append(escCmd.getTextCmd(orderReadyAt, parseLanguage.getParseString("Order_ready_at") + collectionTime));
            }
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting text12 = new TextSetting();
            text12.setAlign(CommonEnum.ALIGN_BOTH_SIDES);
            escCmd.append(escCmd.getTextCmd(text12, parseLanguage.getParseString("Payment_Type") + "   " + PaymentMethod));
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
                txt_backorders.setBold(SettingEnum.Enable);
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
            escCmd.append(escCmd.getTextCmd(txtitemname, parseLanguage.getParseString("Item_Name") + "       " + parseLanguage.getParseString("Price")));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting textSetting3 = new TextSetting();
            textSetting3.setAlign(CommonEnum.ALIGN_MIDDLE);

            for (int i = 0; i < item_name.size(); i++) {
                textSetting3.setAlign(CommonEnum.ALIGN_LEFT);
                String price=item_price.get(i);
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    price=price.replace(".", ",");
                }
                switch (item_quant.get(i).length() + item_name.get(i).length() + item_price.get(i).length()) {
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                     " + Currency + price));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                    " + Currency + price));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                   " + Currency + price));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                  " + Currency + price));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                 " + Currency + price));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "                " + Currency + price));
                        break;
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "               " + Currency + price));
                        break;
                    case 13:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "              " + Currency + price));
                        break;
                    case 14:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "             " + Currency + price));
                        break;
                    case 15:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "            " + Currency +price));
                        break;
                    case 16:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "           " + Currency + price));
                        break;
                    case 17:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "          " + Currency + price));
                        break;
                    case 18:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "         " + Currency + price));
                        break;
                    case 19:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "        " + Currency + price));
                        break;
                    case 20:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "       " + Currency + price));
                        break;
                    case 21:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "      " + Currency + price));
                        break;
                    case 22:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "     " + Currency + price));
                        break;
                    case 23:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "    " + Currency + price));
                        break;
                    case 24:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "   " + Currency + price));
                        break;
                    case 25:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "  " + Currency + price));
                        break;
                    case 26:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + " " + Currency + price));
                        break;
                    case 27:
                        escCmd.append(escCmd.getTextCmd(textSetting3, "" + item_quant.get(i) + " X " + item_name.get(i) + ":" + "" + Currency + price));
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
                        if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                            comboExtraItemPrice=comboExtraItemPrice.replace(".", ",");
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
                       if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                           comboExtraItemPrice=comboExtraItemPrice.replace(".", ",");
                       }

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


                        if (i == (model_orderComboItemOption.getOrderComboItemExtra().size() - 1)) {
                            escCmd.append(escCmd.getLFCRCmd());
                            escCmd.append(escCmd.getLFCRCmd());
                        } else {
                            escCmd.append(escCmd.getLFCRCmd());
                        }
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


            if (!(FoodCosts.equals("") || FoodCosts.equals(null) || FoodCosts.equals("Null") || FoodCosts.equals("null") || FoodCosts.equals("0.00"))) {

                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    FoodCosts=FoodCosts.replace(".", ",");
                }
                String food_cost = parseLanguage.getParseString("Food_Cost");
                switch (FoodCosts.length()) {
                    case 12:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":  " + Currency + FoodCosts));
                        break;
                    case 11:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":  " + Currency + FoodCosts));
                        break;
                    case 10:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":  " + Currency + FoodCosts));
                        break;
                    case 9:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":   " + Currency + FoodCosts));
                        break;
                    case 8:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":    " + Currency + FoodCosts));
                        break;
                    case 7:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":   " + Currency + FoodCosts));
                        break;
                    case 6:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":   " + Currency + FoodCosts));
                        break;
                    case 5:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":   " + Currency + FoodCosts));
                        break;
                    case 4:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":    " + Currency + FoodCosts));
                        break;
                    case 3:
                        escCmd.append(escCmd.getTextCmd(textSetting4, food_cost + ":   " + Currency + FoodCosts));
                        break;

                }

                escCmd.append(escCmd.getLFCRCmd());


            }

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

            if (!(GiftCardPay.equals("") || GiftCardPay.equals(null) || GiftCardPay.equals("Null") || GiftCardPay.equals("null") || GiftCardPay.equals("0.00"))) {

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


            if (!(DiscountPrice.equals("") || DiscountPrice.equals(null) || DiscountPrice.equals("Null") || DiscountPrice.equals("null") || DiscountPrice.equals("0.00"))) {
                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    DiscountPrice=DiscountPrice.replace(".", ",");
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
            if (!(CouponPrice.equals("") || CouponPrice.equals(null) || CouponPrice.equals("Null") || CouponPrice.equals("null") || CouponPrice.equals("0.00"))) {
                TextSetting textSetting4 = new TextSetting();
                textSetting4.setAlign(CommonEnum.ALIGN_LEFT);
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    CouponPrice=CouponPrice.replace(".", ",");
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
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    extraTipAddAmount=extraTipAddAmount.replace(".", ",");
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
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    DeliveryCharge=DeliveryCharge.replace(".", ",");
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
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    ServiceFees=ServiceFees.replace(".", ",");
                }
                String service_cost = parseLanguage.getParseString("Service_Cost");
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
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    PackageFees=PackageFees.replace(".", ",");
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
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    SalesTaxAmount=SalesTaxAmount.replace(".", ",");
                }
                String service_tax = parseLanguage.getParseString("Service_Tax");
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
            if (!(getFoodTaxTotal7.equals("") || getFoodTaxTotal7.equals(null) || getFoodTaxTotal7.equals("Null") || getFoodTaxTotal7.equals("null") || getFoodTaxTotal7.equals("0.00"))) {
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
            if (!(getFoodTaxTotal19.equals("") || getFoodTaxTotal19.equals(null) || getFoodTaxTotal19.equals("Null") || getFoodTaxTotal19.equals("null") || getFoodTaxTotal19.equals("0.00"))) {
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


            TextSetting textSetting5 = new TextSetting();
            textSetting5.setAlign(CommonEnum.ALIGN_LEFT);
            textSetting5.setBold(SettingEnum.Enable);
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                OrderPrice=OrderPrice.replace(".", ",");
            }
            String total = parseLanguage.getParseString("Total");
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


            TextSetting textSetting53 = new TextSetting();
            textSetting53.setAlign(CommonEnum.ALIGN_LEFT);
            textSetting53.setBold(SettingEnum.Enable);
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                TotalSavedDiscount=TotalSavedDiscount.replace(".", ",");
            }
            String total_saved = parseLanguage.getParseString("Total_Saved");
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
//
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


    // this will send text data to be printed by the bluetooth printer


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

                BitmapUtils bitmapUtils = new BitmapUtils(getContext());
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

            Toast.makeText(getActivity(), "Data Sent", Toast.LENGTH_SHORT).show();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Decline(final String a, final String order_id) {
        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, false);
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
                Toast.makeText(getContext(), parseLanguage.getParseString("Please_Check_your_network_connection"), Toast.LENGTH_SHORT).show();
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

    public void DeleteComplaint(final String complaint_id) {
        String message="";
        String okay="";
        String cancel ="";
        if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
            message=getString(R.string.are_you_sure_delete);
            okay=getString(R.string.okay);
            cancel=getString(R.string.cancel);
        }
        else {
            message="Are you sure you want to delete this complaint?";
            okay ="Ok";
            cancel="Cancel";
        }
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext()).setMessage(message).setPositiveButton(okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.delete_complaint_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.has("success")) {
                                int success = jsonObject.getInt("success");
                                Toast.makeText(getContext(), "success_msg", Toast.LENGTH_SHORT).show();
                                ordercomplains();
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
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("lang_code", myPref.getCustomer_default_langauge());
                        params.put("complaint_id", complaint_id);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        6000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            }

        }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
            }
        });
        builder.create().show();

    }
}




