package com.justfoodzorderreceivers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.justfoodzorderreceivers.Model.DriverList;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageDriver extends AppCompatActivity {

    RecyclerView rc_driver;
    ImageView back;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ArrayList<DriverList> driverLists;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_driver);
        myPref = new MyPref(ManageDriver.this);
        initialization();
        getProfileData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialization(){
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        rc_driver = findViewById(R.id.rc_driver);
        back = findViewById(R.id.back);
        driverLists = new ArrayList<>();
    }

    public void getProfileData(){
        driverLists.clear();
        progressDialog = progressDialog.show(ManageDriver.this,"","Please wait...",false,false);
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
                       }else {
                          String error_msg = jsonObject1.getString("error_msg");
                           Toast.makeText(ManageDriver.this, error_msg, Toast.LENGTH_SHORT).show();
                       }
                   }
                    ReviewAdapter reviewAdapter = new ReviewAdapter(ManageDriver.this, driverLists);
                    linearLayoutManager  = new LinearLayoutManager(ManageDriver.this, LinearLayoutManager.VERTICAL,false);
                    rc_driver.setLayoutManager(linearLayoutManager);
                    rc_driver.setAdapter(reviewAdapter);
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
                param.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("param",""+param);
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

        @NonNull
        @Override
        public VIewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.driver_list, viewGroup, false);
            return new VIewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VIewHolder vIewHolder, final int i) {
            vIewHolder.txtridername.setText(review.get(i).getDriverFirstName()+" "+review.get(i).getDriverLastName());
            vIewHolder.txtlicenceno.setText(review.get(i).getDriverLicence());
            vIewHolder.txtstatus.setText(review.get(i).getStatus());

            if (review.get(i).getDriverPhoto().equals("")){
            }else {
                Picasso.get().load(review.get(i).getDriverPhoto()).into(vIewHolder.imgdriver);
            }
            vIewHolder.button_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent view_deiver = new Intent(context,View_driver.class);
                    view_deiver.putExtra("driver_image",""+review.get(i).getDriverPhoto());
                    view_deiver.putExtra("driver_licence_image",""+review.get(i).getDriverLiencePhoto());
                    view_deiver.putExtra("driver_first_name",""+review.get(i).getDriverFirstName());
                    view_deiver.putExtra("driver_last_name",""+review.get(i).getDriverLastName());
                    view_deiver.putExtra("driver_email",""+review.get(i).getDriverEmailAddress());
                    view_deiver.putExtra("driver_mobile_number",""+review.get(i).getDriverMobileNo());
                    view_deiver.putExtra("driver_licence_number",""+review.get(i).getDriverLicence());
                    view_deiver.putExtra("driver_country",""+review.get(i).getDriverCountry());
                    view_deiver.putExtra("driver_city",""+review.get(i).getDriverCity());
                    view_deiver.putExtra("driver_postcode",""+review.get(i).getDriverPostcode());
                    view_deiver.putExtra("driver_address",""+review.get(i).getDriverAddress());
                    view_deiver.putExtra("driver_vehicle_type",""+review.get(i).getDriver_vehicle_types());
                    view_deiver.putExtra("driver_vehicle_brand",""+review.get(i).getDriver_vehicle_brand());
                    view_deiver.putExtra("driver_vehicle_model",""+review.get(i).getDriver_vehicle_model());
                    view_deiver.putExtra("driver_vehicle_year",""+review.get(i).getDriver_vehicle_year());
                    startActivity(view_deiver);
                }
            });
            vIewHolder.button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ManageDriver.this)
                            .setTitle("Harperskebab receiver")
                            .setMessage("Are you sure you want to delete rider ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String dd = "" +review.get(i).getDriverID();
                                    deleteDriver(dd);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).show();
                }
            });
            vIewHolder.button_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent view_deiver = new Intent(context,Adddriver.class);
                    view_deiver.putExtra("todo","2");
                    view_deiver.putExtra("driver_id",""+review.get(i).getDriverID());
                    view_deiver.putExtra("driver_image",""+review.get(i).getDriverPhoto());
                    view_deiver.putExtra("driver_licence_image",""+review.get(i).getDriverLiencePhoto());
                    view_deiver.putExtra("driver_first_name",""+review.get(i).getDriverFirstName());
                    view_deiver.putExtra("driver_last_name",""+review.get(i).getDriverLastName());
                    view_deiver.putExtra("driver_email",""+review.get(i).getDriverEmailAddress());
                    view_deiver.putExtra("driver_mobile_number",""+review.get(i).getDriverMobileNo());
                    view_deiver.putExtra("driver_licence_number",""+review.get(i).getDriverLicence());
                    view_deiver.putExtra("driver_country",""+review.get(i).getDriverCountry());
                    view_deiver.putExtra("driver_city",""+review.get(i).getDriverCity());
                    view_deiver.putExtra("driver_postcode",""+review.get(i).getDriverPostcode());
                    view_deiver.putExtra("driver_address",""+review.get(i).getDriverAddress());
                    view_deiver.putExtra("driver_vehicle_type",""+review.get(i).getDriver_vehicle_types());
                    view_deiver.putExtra("driver_vehicle_brand",""+review.get(i).getDriver_vehicle_brand());
                    view_deiver.putExtra("driver_vehicle_model",""+review.get(i).getDriver_vehicle_model());
                    view_deiver.putExtra("driver_vehicle_year",""+review.get(i).getDriver_vehicle_year());
                    startActivity(view_deiver);
                    finish();
                }
            });
            vIewHolder.button_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = review.get(i).getDriverMobileNo();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return review.size();
        }

        public class VIewHolder extends RecyclerView.ViewHolder {

            TextView txtridername,txtlicenceno,txtstatus;
            ImageView imgdriver;
            Button button_view,button_delete,button_update,button_call;

            public VIewHolder(@NonNull View itemView) {
                super(itemView);
                txtridername = (TextView) itemView.findViewById(R.id.txtridername);
                txtlicenceno = (TextView) itemView.findViewById(R.id.txtlicenceno);
                txtstatus = (TextView) itemView.findViewById(R.id.txtstatus);
                imgdriver = (ImageView) itemView.findViewById(R.id.imgdriver);
                button_view = (Button) itemView.findViewById(R.id.button_view);
                button_delete = (Button) itemView.findViewById(R.id.button_delete);
                button_update = (Button) itemView.findViewById(R.id.button_update);
                button_call = (Button) itemView.findViewById(R.id.button_call);
            }
        }
    }
    public void deleteDriver(final String d_id){
        progressDialog = progressDialog.show(ManageDriver.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.DriversDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("responseprofile",""+s);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int success = jsonObject.getInt("success");
                    if (success==1){
                        getProfileData();
                    }else {
                        String success_msg = jsonObject.getString("success_msg");
                        Toast.makeText(ManageDriver.this, success_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                param.put("DriverID",d_id);
                param.put("lang_code", myPref.getCustomer_default_langauge());
                Log.e("param",""+param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}