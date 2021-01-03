package com.justfoodzorderreceivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodzorderreceivers.Model.DriverList;
import com.justfoodzorderreceivers.Model.TimeModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AcceptButton_activity1 extends AppCompatActivity implements TimeValuesListener
{


    Spinner spinner;
    String[] aa = {"10 Min","15 Min","20 Min","25 Min","30 Min","35 Min","40 Min","45 Min","50 Min","55 Min","60 Min","1 Hour 5 Min","1 Hour 10 Min","1 Hour 15 Min","1 Hour 20 Min","1 Hour 25 Min","1 Hour 30 Min","1 Hour 35 Min"};
    Button button_confirmorder;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ImageView back;
    EditText edt_comment;
    RadioButton rbyes,rbno;
    private String sCity = "", stime = "", sDate = "";
    RadioGroup rgall;
    Dialog dialog;
    RecyclerView driverrecycler;
    ArrayList<DriverList> driverLists;
    SharedPreferences sharedPreferences;
    LinearLayoutManager linearLayoutManager;
    String driver_id="";
    TextView txtdriver,txtdrivernumber,ins_text;
    LinearLayout lldriverdetail;
    ImageView imgdrive;
    private RecyclerView rvTime;
    MyPref myPref;
    ParseLanguage parseLanguage;
    ArrayList<TimeModel> stringList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_button_activity1);


        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        driverLists = new ArrayList<>();
        myPref = new MyPref(AcceptButton_activity1.this);
        button_confirmorder = (Button)findViewById(R.id.button_confirmorder);
        back = (ImageView)findViewById(R.id.iv_back);
        spinner = (Spinner)findViewById(R.id.spinner);
        ins_text= findViewById(R.id.ins_text);

        edt_comment = (EditText)findViewById(R.id.edt_comment);
        rbyes = findViewById(R.id.rbyes);
        rbno = (RadioButton)findViewById(R.id.rbno);
        rgall = (RadioGroup)findViewById(R.id.rgall);
        txtdriver = (TextView)findViewById(R.id.txtdriver);
        txtdrivernumber = (TextView)findViewById(R.id.txtdrivernumber);
        lldriverdetail = (LinearLayout)findViewById(R.id.lldriverdetail);
        imgdrive = (ImageView)findViewById(R.id.imgdrive);
        rvTime = findViewById(R.id.timeRecycler);

        parseLanguage = new ParseLanguage(myPref.getBookingData(),AcceptButton_activity1.this);

        stringList = new ArrayList<TimeModel>();
        stringList.clear();

        for(int i =0;i<aa.length;i++){
            TimeModel sTmodel = new TimeModel();
            sTmodel.setTime(aa[i]);
            sTmodel.setSelectCategory("0");
            stringList.add(sTmodel);
        }
        ins_text.setText(parseLanguage.getParseString("Select_delivery_pickup_time_for_this_order"));
        button_confirmorder.setText(parseLanguage.getParseString("Confirm_Order"));

        rgall.check(R.id.rbno);

        rbyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDriverPoup();
            }
        });

        rbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lldriverdetail.setVisibility(View.GONE);
                driver_id="";
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AcceptButton_activity1.this,android.R.layout.simple_dropdown_item_1line,aa);
        spinner.setAdapter(arrayAdapter);

        button_confirmorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AcceptButton_activity.this, ""+spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                orderconfirm();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AcceptButton_activity.this, ""+spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AcceptButton_activity1.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        setTimeAdapter();
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Intent i = new Intent(AcceptButton_activity.this,MainActivity.class);
//        startActivity(i);
//        finish();
//
//        }


    private void setTimeAdapter() {
        rvTime.setLayoutManager(new GridLayoutManager(AcceptButton_activity1.this, 3));
        rvTime.setAdapter(new TimeAdapter(AcceptButton_activity1.this, stringList, this));
    }

    @Override
    public void onSelectTime(String time) {
        stime = time;
    }

    public void orderconfirm()
    {
        progressDialog = progressDialog.show(AcceptButton_activity1.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.orderdeliverytimechange, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("response",""+s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int error = jsonObject.getInt("error");
                    if (error == 0)
                    {
                        String error_msg = jsonObject.getString("error_msg");
                        showCustomDialog(error_msg);
//                        Intent intent = new Intent(AcceptButton_activity.this,Activity_Booking.class);
//                        intent.putExtra("orderid",""+getIntent().getStringExtra("orderid"));
//                        startActivity(intent);

                    }
                    else {
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
                Log.e("error",""+volleyError);
                String message="";
                if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                    message=getString(R.string.check_internet);
                }
                else {
                    message="Please Check your network connection";
                }
                Toast.makeText(AcceptButton_activity1.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", ""+getIntent().getStringExtra("orderid"));
                params.put("collectionTime", stime);
                params.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("qwqw",""+params);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showCustomDialog (String s)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(AcceptButton_activity1.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AcceptButton_activity1.this,MainActivity.class);
                startActivity(intent);
                finish();

//                        Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }

    private void showCustomDialog1 (String s)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(AcceptButton_activity1.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);

        alertDialog.setIcon(R.drawable.cancel);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

//                Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();

    }



    public void showDriverPoup() {
        dialog = new Dialog(AcceptButton_activity1.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popupdriver);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.diologAnimatioLocation;
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        ImageView iv_back = (ImageView) dialog.findViewById(R.id.iv_back);
        driverrecycler=(RecyclerView) dialog.findViewById(R.id.driverrecycler);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driver_id="";
                rbno.setChecked(true);
                rbyes.setChecked(false);
                dialog.dismiss();
            }
        });
        getDriver();


        dialog.show();
    }

    public void getDriver(){
        driverLists.clear();
        progressDialog = progressDialog.show(AcceptButton_activity1.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.DriversDisplay, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("responseprofile",""+s);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RestaurantDriver");
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int error = jsonObject1.getInt("error");
                        if (error==0){
                            int id = jsonObject1.getInt("id");
                            int DriverID = jsonObject1.getInt("DriverID");
                            int RestaurantID = jsonObject1.getInt("RestaurantID");
                            String DriverFirstName = jsonObject1.getString("DriverFirstName");
                            String DriverLastName = jsonObject1.getString("DriverLastName");
                            String DriverEmailAddress = jsonObject1.getString("DriverEmailAddress");
                            String DriverPhoto = jsonObject1.getString("DriverPhoto");
                            String DriverLiencePhoto = jsonObject1.getString("DriverLiencePhoto");
                            String DriverCountry = jsonObject1.getString("DriverCountry");
                            String DriverState = jsonObject1.getString("DriverState");
                            String DriverCity = jsonObject1.getString("DriverCity");
                            String DriverPostcode = jsonObject1.getString("DriverPostcode");
                            String DriverAddress = jsonObject1.getString("DriverAddress");
                            String DriverCommisionCharge = jsonObject1.getString("DriverCommisionCharge");
                            String DriverCommission_Type = jsonObject1.getString("DriverCommission_Type");
                            String customerCountry = jsonObject1.getString("customerCountry");
                            String customerState = jsonObject1.getString("customerState");
                            String customerCity = jsonObject1.getString("customerCity");
                            String driver_vehicle_types = jsonObject1.getString("driver_vehicle_types");
                            String driver_vehicle_year = jsonObject1.getString("driver_vehicle_year");
                            String driver_vehicle_brand = jsonObject1.getString("driver_vehicle_brand");
                            String driver_vehicle_model = jsonObject1.getString("driver_vehicle_model");
                            String DriverLicence = jsonObject1.getString("DriverLicence");
                            String DriverMobileNo = jsonObject1.getString("DriverMobileNo");
                            String created_date = jsonObject1.getString("created_date");
                            String status = jsonObject1.getString("status");
                            String DriverStatus = jsonObject1.getString("DriverStatus");
                            driverLists.add(new DriverList(id,DriverID,RestaurantID,DriverFirstName,DriverLastName,DriverEmailAddress,DriverPhoto,DriverLiencePhoto,
                                    DriverCountry,DriverState,DriverCity,DriverPostcode,DriverAddress,DriverCommisionCharge,
                                    DriverCommission_Type,customerCountry,customerState,customerCity,
                                    driver_vehicle_types,driver_vehicle_year,driver_vehicle_brand,driver_vehicle_model,
                                    DriverLicence,DriverMobileNo,created_date,status,DriverStatus));
                            ReviewAdapter reviewAdapter = new ReviewAdapter(AcceptButton_activity1.this, driverLists);
                            linearLayoutManager  = new LinearLayoutManager(AcceptButton_activity1.this, LinearLayoutManager.VERTICAL,false);
                            driverrecycler.setLayoutManager(linearLayoutManager);
                            driverrecycler.setAdapter(reviewAdapter);
                        }else {
                            String error_msg = jsonObject1.getString("error_msg");
                            Toast.makeText(AcceptButton_activity1.this, error_msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exception",""+e);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyErrorprofile",""+volleyError);
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> param = new HashMap<>();
                param.put("RestaurantID",""+sharedPreferences.getString("restaurant_id",""));
                Log.e("param",""+param);
                param.put("lang_code", myPref.getCustomer_default_langauge());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.VIewHolder>{
        Context context;
        ArrayList<DriverList> review;

        public ReviewAdapter(Context c,ArrayList<DriverList> r) {
            this.context = c;
            this.review = r;
        }

        @Override
        public ReviewAdapter.VIewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.dd_list, viewGroup, false);
            return new ReviewAdapter.VIewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ReviewAdapter.VIewHolder vIewHolder, final int i) {
            vIewHolder.tv_driver_name.setText(review.get(i).getDriverFirstName()+" "+review.get(i).getDriverLastName());
            vIewHolder.tv_driver_number.setText(review.get(i).getDriverMobileNo());
            vIewHolder.tv_driver_Lic_number.setText(review.get(i).getDriverLicence());
            if (review.get(i).getDriverPhoto().equals("")){
            }else {
                Picasso.get().load(review.get(i).getDriverPhoto()).into(vIewHolder.imgdriver);
            }
            vIewHolder.driverchecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vIewHolder.driverchecked.setChecked(true);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            driver_id = ""+review.get(i).getDriverID();
                            lldriverdetail.setVisibility(View.VISIBLE);
                            txtdriver.setText(review.get(i).getDriverFirstName()+" "+review.get(i).getDriverLastName());
                            txtdrivernumber.setText(review.get(i).getDriverMobileNo());
                            if (review.get(i).getDriverPhoto().equals("")){
                            }else {
                                Picasso.get().load(review.get(i).getDriverPhoto()).into(imgdrive);
                            }
                            dialog.dismiss();

                        }
                    }, 500);
                }
            });
        }

        @Override
        public int getItemCount() {
            return review.size();
        }

        public class VIewHolder extends RecyclerView.ViewHolder {

            TextView tv_driver_name,tv_driver_number,tv_driver_Lic_number;
            RadioButton driverchecked;
            ImageView imgdriver;

            public VIewHolder( View itemView) {
                super(itemView);
                tv_driver_name = (TextView) itemView.findViewById(R.id.tv_driver_name);
                tv_driver_number = (TextView) itemView.findViewById(R.id.tv_driver_number);
                tv_driver_Lic_number = (TextView) itemView.findViewById(R.id.tv_driver_Lic_number);
                driverchecked = (RadioButton) itemView.findViewById(R.id.driverchecked);
                imgdriver = (ImageView) itemView.findViewById(R.id.imgdriver);

            }
        }
    }

    }

