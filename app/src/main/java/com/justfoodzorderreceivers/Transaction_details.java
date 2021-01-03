package com.justfoodzorderreceivers;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Transaction_details extends AppCompatActivity {

    ImageView back;
    TextView txtdwnld,txtorderid,txtheader,txtaddress,txtdate,txtamount,txtname,txtmobile,txtemail,
            txtcname;
    ConstraintLayout cc;

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= 23&& context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    final int REQUEST = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        back = (ImageView) findViewById(R.id.back);
        txtdwnld = (TextView) findViewById(R.id.txtdwnld);
        txtheader = (TextView) findViewById(R.id.txtheader);
        txtaddress = (TextView) findViewById(R.id.txtaddress);
        txtorderid = (TextView) findViewById(R.id.txtorderid);
        txtdate = (TextView) findViewById(R.id.txtdate);
        txtamount = (TextView) findViewById(R.id.txtamount);
        txtname = (TextView) findViewById(R.id.txtname);
        txtmobile = (TextView) findViewById(R.id.txtmobile);
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtcname = (TextView) findViewById(R.id.txtcname);
        cc = (ConstraintLayout) findViewById(R.id.cc);

        txtheader.setText(""+getIntent().getStringExtra("heading"));
        txtaddress.setText(""+getIntent().getStringExtra("address"));
        txtdate.setText(""+getIntent().getStringExtra("date"));
        txtamount.setText(Activity_Splash.currency_symbol+" "+getIntent().getStringExtra("amount"));
        txtname.setText(""+getIntent().getStringExtra("name"));
        txtmobile.setText(""+getIntent().getStringExtra("mobile"));
        txtemail.setText(""+getIntent().getStringExtra("email"));
        txtcname.setText(""+getIntent().getStringExtra("cname"));
        txtorderid.setText("Order Id:"+getIntent().getStringExtra("orderid"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtdwnld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(Transaction_details.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(Transaction_details.this, PERMISSIONS, REQUEST );
                        // q=true;
                    } else {
                        //Do here
                        getScreenShot();
                    }
                } else {
                    //Do here
                    getScreenShot();
                }
            }
        });
    }
    private void getScreenShot() {
        View v = cc.getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap b = v.getDrawingCache();
        String extr = Environment.getExternalStorageDirectory().toString();
        File myPath = new File(extr, getIntent().getStringExtra("orderid") + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(this, "Download Completed", Toast.LENGTH_SHORT).show();
           // MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}