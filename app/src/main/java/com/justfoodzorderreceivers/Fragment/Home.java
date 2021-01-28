    package com.justfoodzorderreceivers.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
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
//            order_reference_nfindBT()umber, collectionTime, Table_Booking_Number, DriverFirstName, DriverLastName,
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
        rl_booking.setVisibility(View.GONE);


//        if (myPref.getBookType().equals("1")) {
//            rl_booking.setVisibility(View.VISIBLE);
//        }
//        if (myPref.getBookType().equals("0")) {
//            rl_booking.setVisibility(View.GONE);
//        }
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
        progressDialog = progressDialog.show(getActivity(), "", parseLanguage.getParseString("Please_wait"), false, true);
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
                    if(orderLists.size()>0){
                        Log.i("reason",orderLists.get(0).getOrderid());
//                        getorderdetails(orderLists.get(0).getOrderid(),"1");
                    }
                    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rchome.setLayoutManager(linearLayoutManager);
                    rchome.setAdapter(orderListViewToday);
//                    SwipeHelper swipeHelper = new SwipeHelper(getContext()) {
//                        @Override
//                        public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons, int pos) {
//                            final OrderList item = orderListViewToday.getData().get(pos);
//                            if (item.getOrder_status_msg().equalsIgnoreCase("Kitchen")) {
//                                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                                        parseLanguage.getParseString("Complete"),
//                                        0,
//                                        Color.parseColor("#9fd836"),
//                                        new SwipeHelper.UnderlayButtonClickListener() {
//                                            @Override
//                                            public void onClick(final int pos) {
//                                                Log.e("SliderClick", "dd");
//                                                final OrderList item = orderListViewToday.getData().get(pos);
//                                                player.stop();
//                                                //    final OrderList item = orderListViewPending.getData().get(pos);
//
//
//                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//
//                                                // Setting Dialog Title
////                                                alertDialog.setTitle("Harperskebab Order Receiver");
//
//                                                // Setting Dialog Message
//                                                alertDialog.setMessage(parseLanguage.getParseString("Order_Complete_Alert"));
//
//                                                alertDialog.setPositiveButton(parseLanguage.getParseString("YESText"), new DialogInterface.OnClickListener() {
//                                                    public void onClick(final DialogInterface dialog, int which) {
//
//                                                        ordermark_complete(item.getOrderid());
//
//
//                                                    }
//
//                                                });
//                                                alertDialog.setNegativeButton(parseLanguage.getParseString("NOText"), new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//
//                                                        dialog.cancel();
//                                                    }
//                                                });
//                                                // Showing Alert Message
//                                                alertDialog.show();
//
//                                            }
//                                        }
//                                ));
//
//                                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                                        parseLanguage.getParseString("Change_Time"),
//                                        0,
//                                        Color.parseColor("#ffa500"),
//                                        new SwipeHelper.UnderlayButtonClickListener() {
//                                            @Override
//                                            public void onClick(final int pos) {
//                                                Log.e("SliderClick", "dd");
//                                                final OrderList item = orderListViewToday.getData().get(pos);
//                                                player.stop();
//                                                //    final OrderList item = orderListViewPending.getData().get(pos);
//                                                Intent i = new Intent(getContext(), AcceptButton_activity1.class);
//                                                i.putExtra("orderid", "" + item.getOrderid());
//                                                startActivity(i);
//                                            }
//                                        }
//                                ));
//
//
//                            }
//                            if (item.getOrder_status_msg().equalsIgnoreCase("Confirmed")) {
//
//
//                                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                                        parseLanguage.getParseString("Processing"),
//                                        0,
//                                        Color.parseColor("#ffa500"),
//                                        new SwipeHelper.UnderlayButtonClickListener() {
//                                            @Override
//                                            public void onClick(final int pos) {
//                                                final OrderList item = orderListViewToday.getData().get(pos);
//                                                player.stop();
//                                                changestatus(item.getOrderid(), "1");
//                                            }
//                                        }
//                                ));
//
//                                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                                        parseLanguage.getParseString("Complete"),
//                                        0,
//                                        Color.parseColor("#9fd836"),
//                                        new SwipeHelper.UnderlayButtonClickListener() {
//                                            @Override
//                                            public void onClick(final int pos) {
//                                                final OrderList item = orderListViewToday.getData().get(pos);
//                                                player.stop();
//                                                changestatus(item.getOrderid(), "2");
//                                            }
//                                        }
//                                ));
//
//
//                                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                                        parseLanguage.getParseString("DeclineButton"),
//                                        0,
//                                        Color.parseColor("#fe4c3a"),
//                                        new SwipeHelper.UnderlayButtonClickListener() {
//                                            @Override
//                                            public void onClick(final int pos) {
//                                                final OrderList item = orderListViewToday.getData().get(pos);
//
//
//                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//
//                                                // Setting Dialog Title
//                                                alertDialog.setTitle("Harperskebab Order Receiver");
//
//
//                                                // Setting Dialog Message
//                                                alertDialog.setMessage(parseLanguage.getParseString("Are_you_sure_to_decline_order"));
//
//                                                alertDialog.setPositiveButton(parseLanguage.getParseString("YESText"), new DialogInterface.OnClickListener() {
//                                                    public void onClick(final DialogInterface dialog, int which) {
//
//                                                        dialog.dismiss();
//                                                        dialog.cancel();
//
//                                                        // Write your code here to invoke YES event
////                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
//                                                        {
//                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                                            LayoutInflater inflater = getLayoutInflater();
//                                                            final View dialogLayout = inflater.inflate(R.layout.custom_alertdialog, null);
//                                                            builder.setView(dialogLayout);
//                                                            final AlertDialog aa = builder.create();
//                                                            final EditText edt_reason = (EditText) dialogLayout.findViewById(R.id.edt_reason);
//                                                            TextView ordernumber = dialogLayout.findViewById(R.id.ordernumber);
//                                                            Button submit_reason = (Button) dialogLayout.findViewById(R.id.submit_reason);
//                                                            submit_reason.setText(parseLanguage.getParseString("Submit"));
//                                                            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
//                                                            if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
//                                                                edt_reason.setHint(getString(R.string.write_notes_customer));
//                                                                ordernumber.setText(getString(R.string.decline_reason));
//                                                                submit_reason.setText(getString(R.string.submit_german));
//                                                            }
//
//                                                            submit_reason.setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
//                                                                    if (player.isPlaying() == true) {
//                                                                        player.stop();
//                                                                    } else {
//
//                                                                    }
////                                    Toast.makeText(Activity_Booking.this, "asakhdsakhdgs", Toast.LENGTH_SHORT).show();
//                                                                    Decline("" + edt_reason.getText().toString(), item.getOrderid());
//                                                                }
//                                                            });
//
//                                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
////                                    finish();
//                                                                    player.start();
//
//                                                                    aa.dismiss();
//
//                                                                }
//                                                            });
//                                                            aa.show();
//
//                                                        }
//
//                                                    }
//
//                                                });
//                                                alertDialog.setNegativeButton(parseLanguage.getParseString("NOText"), new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//
//                                                        dialog.cancel();
//                                                    }
//                                                });
//
//                                                // Showing Alert Message
//                                                alertDialog.show();
//
//
//                                            }
//                                        }
//                                ));
//
//                            } else {
//
//                                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                                        parseLanguage.getParseString("AcceptButton"),
//                                        0,
//                                        Color.parseColor("#9fd836"),
//                                        new SwipeHelper.UnderlayButtonClickListener() {
//                                            @Override
//                                            public void onClick(final int pos) {
//                                                final OrderList item = orderListViewToday.getData().get(pos);
//                                                player.stop();
//                                                Intent i = new Intent(getContext(), AcceptButton_activity.class);
//                                                i.putExtra("orderid", "" + item.getOrderid());
//                                                startActivity(i);
//                                            }
//                                        }
//                                ));
//
//
//                                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                                        parseLanguage.getParseString("DeclineButton"),
//                                        0,
//                                        Color.parseColor("#fe4c3a"),
//                                        new SwipeHelper.UnderlayButtonClickListener() {
//                                            @Override
//                                            public void onClick(final int pos) {
//                                                final OrderList item = orderListViewToday.getData().get(pos);
//
//
//                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//
//                                                // Setting Dialog Title
//                                                alertDialog.setTitle("Harperskebab Order Receiver");
//
//
//                                                // Setting Dialog Message
//                                                alertDialog.setMessage(parseLanguage.getParseString("Are_you_sure_to_decline_order"));
//
//                                                alertDialog.setPositiveButton(parseLanguage.getParseString("YESText"), new DialogInterface.OnClickListener() {
//                                                    public void onClick(final DialogInterface dialog, int which) {
//
//                                                        dialog.dismiss();
//                                                        dialog.cancel();
//
//                                                        // Write your code here to invoke YES event
////                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
//                                                        {
//                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                                            LayoutInflater inflater = getLayoutInflater();
//                                                            final View dialogLayout = inflater.inflate(R.layout.custom_alertdialog, null);
//                                                            builder.setView(dialogLayout);
//                                                            final AlertDialog aa = builder.create();
//                                                            final EditText edt_reason = (EditText) dialogLayout.findViewById(R.id.edt_reason);
//                                                            TextView ordernumber = dialogLayout.findViewById(R.id.ordernumber);
//                                                            Button submit_reason = (Button) dialogLayout.findViewById(R.id.submit_reason);
//                                                            submit_reason.setText(parseLanguage.getParseString("Submit"));
//                                                            Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);
//
//                                                            if (myPref.getCustomer_default_langauge().equalsIgnoreCase("de")) {
//                                                                edt_reason.setHint(getString(R.string.write_notes_customer));
//                                                                ordernumber.setText(getString(R.string.decline_reason));
//                                                                submit_reason.setText(getString(R.string.submit_german));
//                                                            }
//                                                            submit_reason.setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
//                                                                    if (player.isPlaying() == true) {
//                                                                        player.stop();
//                                                                    } else {
//
//                                                                    }
////                                    Toast.makeText(Activity_Booking.this, "asakhdsakhdgs", Toast.LENGTH_SHORT).show();
//                                                                    Decline("" + edt_reason.getText().toString(), item.getOrderid());
//                                                                }
//                                                            });
//
//                                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
////                                    finish();
//                                                                    player.start();
//
//                                                                    aa.dismiss();
//
//                                                                }
//                                                            });
//                                                            aa.show();
//
//                                                        }
//
//                                                    }
//
//                                                });
//                                                alertDialog.setNegativeButton(parseLanguage.getParseString("NOText"), new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//
//                                                        dialog.cancel();
//                                                    }
//                                                });
//
//                                                // Showing Alert Message
//                                                alertDialog.show();
//
//
//                                            }
//                                        }
//                                ));
//                            }
//
//                        }
//                    };
//                    swipeHelper.attachToRecyclerView(rchome);

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
        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, true);
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
        progressDialog = progressDialog.show(getActivity(), "", parseLanguage.getParseString("Please_wait"), false, true);
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
        progressDialog = progressDialog.show(getActivity(), "", parseLanguage.getParseString("Please_wait"), false, true);
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
                            Log.i("name",error);
                            String error_msg = jsonObject1.getString("error_msg");
                            linear_message.setVisibility(View.VISIBLE);
                            rchome3.setVisibility(View.GONE);
                            error_msgTxt.setText(error_msg);
                        } else {
                            Log.i("name","error_msg");
                            String error_msg = jsonObject1.getString("error_msg");
                            linear_message.setVisibility(View.GONE);
                            rchome3.setVisibility(View.GONE);
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
            if (!supportList.getContact_name().equalsIgnoreCase("null")) {
                viewHolder.tv_namee.setText(supportList.getContact_name());
            }
            viewHolder.tv_orderid.setText(supportList.getOrderIDNumber());
            if (!supportList.getOrderIssueMessage().equalsIgnoreCase("null")){
                viewHolder.tv_reply.setText(supportList.getOrderIssueMessage());
        }
            if(!supportList.getContact_phone().equalsIgnoreCase("null")) {
                viewHolder.txtnumber.setText(supportList.getContact_phone());
            }
            if(!supportList.getContact_email().equalsIgnoreCase("null")) {
                viewHolder.txtemail.setText(supportList.getContact_email());
            }
            if(!supportList.getRestaurant_order_issue_reply().equalsIgnoreCase("null")) {
                viewHolder.your_reply.setText(supportList.getRestaurant_order_issue_reply());
            }
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
                if(!parseLanguage.getParseString("Your_Reply").equalsIgnoreCase("null")) {
                    your_reply_title.setText(parseLanguage.getParseString("Your_Reply"));
                }

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
                                String auto_print_enable=j.getString("auto_print_enable");
                                if(auto_print_enable.equalsIgnoreCase("1")){
//                                    getorderdetails(orderid,auto_print_enable);
                                }


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
//                                                alertDialog.setTitle("Harperskebab Order Receiver");


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
//                        TodayOrderRepeatList();

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


        t.schedule(mTimerTask, 4000, 10000);

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
        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, true);
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




    // tries to open a connection to the bluetooth printer device







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
        progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, true);
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
                progressDialog = progressDialog.show(getContext(), "", parseLanguage.getParseString("Please_wait"), false, true);
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




