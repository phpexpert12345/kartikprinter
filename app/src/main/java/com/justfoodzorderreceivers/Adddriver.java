package com.justfoodzorderreceivers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.justfoodzorderreceivers.Multipart.MultipartRequest;
import com.justfoodzorderreceivers.Multipart.NetworkOperationHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Adddriver extends AppCompatActivity {

    EditText edt_first,edt_last,edt_emailll,edt_mobile,edt_license,edt_country,edt_city,
            edt_postcode,edt_address,edt_vechiletype,edt_vechilebrand,edt_vechilemodel,
            edt_vechileyear;
    Button submit;
    ImageView back,iv_profile,imageselector,iv_licence,imageselectorlicence;
    String imageviewSelected = "";
    private int REQUEST_CAMERA = 0, REQUEST_GALLERY = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static int RESULT_LOAD = 1;
    private String DriverImage = "",LicenceImage="";
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    MyPref myPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddriver);
        initialization();
        imageselector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewSelected = "DriverPic";
                showPictureDialog();
            }
        });
        myPref = new MyPref(Adddriver.this);

        imageselectorlicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewSelected = "LicencePic";
                showPictureDialog();
            }
        });

        if (getIntent().getStringExtra("todo").equals("2")){
            edt_first.setText(getIntent().getStringExtra("driver_first_name"));
            edt_last.setText(getIntent().getStringExtra("driver_last_name"));
            edt_emailll.setText(getIntent().getStringExtra("driver_email"));
            edt_mobile.setText(getIntent().getStringExtra("driver_mobile_number"));
            edt_license.setText(getIntent().getStringExtra("driver_licence_number"));
            edt_country.setText(getIntent().getStringExtra("driver_country"));
            edt_city.setText(getIntent().getStringExtra("driver_city"));
            edt_postcode.setText(getIntent().getStringExtra("driver_postcode"));
            edt_address.setText(getIntent().getStringExtra("driver_address"));
            edt_vechiletype.setText(getIntent().getStringExtra("driver_vehicle_type"));
            edt_vechilebrand.setText(getIntent().getStringExtra("driver_vehicle_brand"));
            edt_vechilemodel.setText(getIntent().getStringExtra("driver_vehicle_model"));
            edt_vechileyear.setText(getIntent().getStringExtra("driver_vehicle_year"));
            if (getIntent().getStringExtra("driver_image").equals("")){
            }else {
                Picasso.get().load(getIntent().getStringExtra("driver_image")).into(iv_profile);
            }
            if (getIntent().getStringExtra("driver_licence_image").equals("")){
            }else {
                Picasso.get().load(getIntent().getStringExtra("driver_licence_image")).into(iv_licence);
            }
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
                if (edt_first.getText().toString().equals("")){
                    edt_first.setError("Enter rider first name");
                    edt_first.requestFocus();
                }else if (edt_last.getText().toString().equals("")){
                    edt_last.setError("Enter rider last name");
                    edt_last.requestFocus();
                }else if (edt_emailll.getText().toString().equals("")){
                    edt_emailll.setError("Enter rider Email id");
                    edt_emailll.requestFocus();
                }else if (edt_mobile.getText().toString().equals("")){
                    edt_mobile.setError("Enter rider Mobile Number");
                    edt_mobile.requestFocus();
                }else if (edt_license.getText().toString().equals("")){
                    edt_license.setError("Enter rider License no");
                    edt_license.requestFocus();
                }else if (edt_country.getText().toString().equals("")){
                    edt_country.setError("Enter rider country");
                    edt_country.requestFocus();
                }else if (edt_city.getText().toString().equals("")){
                    edt_city.setError("Enter rider city");
                    edt_city.requestFocus();
                }else if (edt_postcode.getText().toString().equals("")){
                    edt_postcode.setError("Enter rider postal code");
                    edt_postcode.requestFocus();
                }else if (edt_address.getText().toString().equals("")){
                    edt_address.setError("Enter rider address");
                    edt_address.requestFocus();
                }else {
                    if (getIntent().getStringExtra("todo").equals("2")){
                        Updateapi();
                    }else {
                        Updateprofileapi();
                    }
                }
            }
        });

    }
    public void initialization(){
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
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
        imageselector = findViewById(R.id.imageselector);
        iv_licence = findViewById(R.id.iv_licence);
        imageselectorlicence = findViewById(R.id.imageselectorlicence);
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select image from gallery",
                "Capture image from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                selectImageGallary();
                                break;
                            case 1:
                                selectImageCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void selectImageGallary() {
        if (checkPermissionREAD_EXTERNAL_STORAGE(Adddriver.this)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_GALLERY);
        } else {

        }
    }

    private void selectImageCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Adddriver) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Adddriver) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},

                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Adddriver) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD && resultCode == RESULT_OK && null != data) {


                if (imageviewSelected == "DriverPic") {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    DriverImage = cursor.getString(columnIndex);
                    iv_profile.setImageBitmap(decodeUri(selectedImage));
                    cursor.close();
                }

                if (imageviewSelected == "LicencePic") {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    LicenceImage = cursor.getString(columnIndex);
                    iv_licence.setImageBitmap(decodeUri(selectedImage));
                    cursor.close();
                }//LicencePic


            }  else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && null != data){

                if (imageviewSelected == "DriverPic") {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    iv_profile.setImageBitmap(photo);
                    Uri tempUri = getImageUri(getApplicationContext(), photo);

                    File finalFile = new File(getRealPathFromURI(tempUri));
                    DriverImage =  String.valueOf(finalFile);
                    Log.e("qw",""+finalFile+"     "+tempUri);
                }


                if (imageviewSelected == "LicencePic") {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    iv_licence.setImageBitmap(photo);
                    Uri tempUri = getImageUri(getApplicationContext(), photo);

                    File finalFile = new File(getRealPathFromURI(tempUri));
                    LicenceImage =  String.valueOf(finalFile);
                    Log.e("qw",""+finalFile+"     "+tempUri);
                }

            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("expception:", "" + e.getMessage());
            Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);

                cursor.close();
            }
        }
        return path;
    }
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o2);
    }


    private void Updateprofileapi() {

        progressDialog = new ProgressDialog(Adddriver.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        MultipartRequest req = null;
        HashMap<String, String> map = new HashMap<>();
//        filetype = tv.getText().toString();
        map.put("RestaurantID", ""+sharedPreferences.getString("restaurant_id",""));
        map.put("DriverFirstName", ""+edt_first.getText().toString());
        map.put("DriverLastName", ""+edt_last.getText().toString());
        map.put("DriverEmailAddress", ""+edt_emailll.getText().toString());
        map.put("DriverMobileNo", ""+edt_mobile.getText().toString());
        map.put("DriverLicence", ""+edt_mobile.getText().toString());
        map.put("DriverPostcode", ""+edt_postcode.getText().toString());
        map.put("DriverCity", ""+edt_city.getText().toString());
        map.put("DriverAddress", ""+edt_address.getText().toString());
        map.put("DriverCountry", ""+edt_country.getText().toString());
        map.put("driver_vehicle_types", ""+edt_vechiletype.getText().toString());
        map.put("driver_vehicle_brand", ""+edt_vechilebrand.getText().toString());
        map.put("lang_code", myPref.getCustomer_default_langauge());
        map.put("driver_vehicle_model", ""+edt_vechilemodel.getText().toString());
        map.put("driver_vehicle_year", ""+edt_vechileyear.getText().toString());
//                params.put("DriverPhoto", ""+edt_address.getText().toString());
//        map.put("reciept_files", ReceiptImage);
        Log.e("ee",""+map);

        try {
            req = new MultipartRequest(Url.add_rider, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("error",""+error);
                    return;
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Success", ""+response);
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        int success = object.getInt("success");
                        if (success == 1) {
                            String success_msg = object.getString("success_msg");
                            Toast.makeText(Adddriver.this, ""+success_msg, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Adddriver.this,Adddriver.class);
                            i.putExtra("todo","1");
                            startActivity(i);
                            finish();
                        } else {
                            String success_msg = object.getString("success_msg");
                            Toast.makeText(Adddriver.this, ""+success_msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, map);
            if (DriverImage.equals("")){

            }else {
                req.addImageData("DriverPhoto", new File(DriverImage));
            }
            if (LicenceImage.equals("")){

            }else {
                req.addImageData("DriverLiencePhoto", new File(LicenceImage));
            }

            Log.e("req",""+req);
            Log.e("photo",""+DriverImage+"   "+LicenceImage);
            NetworkOperationHelper.getInstance(Adddriver.this).addToRequestQueue(req);
        } catch (UnsupportedEncodingException e) {
            progressDialog.dismiss();
            Log.e("exc",""+e);
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("exc",""+e);
        }
    }

    private void Updateapi() {

        progressDialog = new ProgressDialog(Adddriver.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        MultipartRequest req = null;
        HashMap<String, String> map = new HashMap<>();
//        filetype = tv.getText().toString();
        map.put("DriverID", getIntent().getStringExtra("driver_id"));
        map.put("DriverFirstName", ""+edt_first.getText().toString());
        map.put("DriverLastName", ""+edt_last.getText().toString());
        map.put("DriverEmailAddress", ""+edt_emailll.getText().toString());
        map.put("DriverMobileNo", ""+edt_mobile.getText().toString());
        map.put("DriverLicence", ""+edt_mobile.getText().toString());
        map.put("DriverPostcode", ""+edt_postcode.getText().toString());
        map.put("DriverCity", ""+edt_city.getText().toString());
        map.put("DriverAddress", ""+edt_address.getText().toString());
        map.put("DriverCountry", ""+edt_country.getText().toString());
        map.put("driver_vehicle_types", ""+edt_vechiletype.getText().toString());
        map.put("driver_vehicle_brand", ""+edt_vechilebrand.getText().toString());
        map.put("lang_code", myPref.getCustomer_default_langauge());
        map.put("driver_vehicle_model", ""+edt_vechilemodel.getText().toString());
        map.put("driver_vehicle_year", ""+edt_vechileyear.getText().toString());
//                params.put("DriverPhoto", ""+edt_address.getText().toString());
//        map.put("reciept_files", ReceiptImage);
        Log.e("ee",""+map);

        try {
            req = new MultipartRequest(Url.DriversUpdate, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("error",""+error);
                    return;
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Success", ""+response);
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        int success = object.getInt("success");
                        if (success == 1) {
                            String success_msg = object.getString("success_msg");
                            Toast.makeText(Adddriver.this, ""+success_msg, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Adddriver.this,ManageDriver.class);
                            startActivity(i);
                            finish();
                        } else {
                            String success_msg = object.getString("success_msg");
                            Toast.makeText(Adddriver.this, ""+success_msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, map);
            if (DriverImage.equals("")){

            }else {
                req.addImageData("DriverPhoto", new File(DriverImage));
            }
            if (LicenceImage.equals("")){

            }else {
                req.addImageData("DriverLiencePhoto", new File(LicenceImage));
            }

            Log.e("req",""+req);
            Log.e("photo",""+DriverImage+"   "+LicenceImage);
            NetworkOperationHelper.getInstance(Adddriver.this).addToRequestQueue(req);
        } catch (UnsupportedEncodingException e) {
            progressDialog.dismiss();
            Log.e("exc",""+e);
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("exc",""+e);
        }
    }
}