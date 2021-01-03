package com.justfoodzorderreceivers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class View_driver extends AppCompatActivity {

    TextView edt_first,edt_last,edt_emailll,edt_mobile,edt_license,edt_country,edt_city,
            edt_postcode,edt_address,edt_vechiletype,edt_vechilebrand,edt_vechilemodel,
            edt_vechileyear;
    Button submit;
    ImageView back,iv_profile,iv_licence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_driver);
        initialization();
        edt_first.setText(getIntent().getStringExtra("driver_first_name"));
        edt_last.setText(getIntent().getStringExtra("driver_last_name"));
        edt_emailll.setText(getIntent().getStringExtra("driver_email"));
        edt_mobile.setText(getIntent().getStringExtra("driver_mobile_number"));
        edt_license.setText(getIntent().getStringExtra("driver_licence_number"));
        edt_country.setText(getIntent().getStringExtra("driver_country"));
        edt_city.setText(getIntent().getStringExtra("driver_city"));
        edt_postcode.setText(getIntent().getStringExtra("driver_postcode"));
        edt_address.setText(getIntent().getStringExtra("driver_address"));
        if (getIntent().getStringExtra("driver_vehicle_type").equals("")){
            edt_vechiletype.setVisibility(View.GONE);
        }else {
            edt_vechiletype.setText(getIntent().getStringExtra("driver_vehicle_type"));
        }
        if (getIntent().getStringExtra("driver_vehicle_brand").equals("")){
            edt_vechilebrand.setVisibility(View.GONE);
        }else {
            edt_vechilebrand.setText(getIntent().getStringExtra("driver_vehicle_brand"));
        }
        if (getIntent().getStringExtra("driver_vehicle_model").equals("")){
            edt_vechilemodel.setVisibility(View.GONE);
        }else {
            edt_vechilemodel.setText(getIntent().getStringExtra("driver_vehicle_model"));
        }
        if (getIntent().getStringExtra("driver_vehicle_year").equals("")){
            edt_vechileyear.setVisibility(View.GONE);
        }else {
            edt_vechileyear.setText(getIntent().getStringExtra("driver_vehicle_year"));
        }
        if (getIntent().getStringExtra("driver_image").equals("")){
        }else {
            Picasso.get().load(getIntent().getStringExtra("driver_image")).into(iv_profile);
        }
        if (getIntent().getStringExtra("driver_licence_image").equals("")){
        }else {
            Picasso.get().load(getIntent().getStringExtra("driver_licence_image")).into(iv_licence);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initialization(){
        edt_first = findViewById(R.id.edt_first);
        edt_last = findViewById(R.id.edt_last);
        edt_emailll = findViewById(R.id.edt_emailll);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_license = findViewById(R.id.edt_license);
        edt_country = findViewById(R.id.edt_country);
        edt_city = findViewById(R.id.edt_city);
        edt_postcode = findViewById(R.id.edt_postcode);
        edt_address = findViewById(R.id.edt_address);
        edt_vechiletype = findViewById(R.id.edt_vechiletype);
        edt_vechilebrand = findViewById(R.id.edt_vechilebrand);
        edt_vechilemodel = findViewById(R.id.edt_vechilemodel);
        edt_vechileyear = findViewById(R.id.edt_vechileyear);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        iv_profile = findViewById(R.id.iv_profile);
        iv_licence = findViewById(R.id.iv_licence);
    }

}
