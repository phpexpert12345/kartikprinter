package com.justfoodzorderreceivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TrackOrder_activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView back,driverpic,phonecall;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String RestroLat = "";
    String RestroLan = "";
    String CustomerLat = "";
    String CustomerLng = "";
    String RiderLat = "";
    String RiderLng = "";
    private Marker sourceMarker;
    private Marker destinationMarker;
    private Marker riderMarker;
    TextView driver_name,driver_number,txttime,title;
    AuthPreference authPreference;
    TimerTask mTimerTask;
    final Handler handler = new Handler();
    Timer t = new Timer();
    String driverphone_number ;
    LatLng Restaurant;
    LatLng CustomerLocation;
    LatLng Riderlocation;
    String restroname,restroadd,customername,customeradd,timeduration,distance,duration;
    MyPref myPref;
    ParseLanguage parseLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        requestQueue = Volley.newRequestQueue(this);
//        Trackorder();

        authPreference = new AuthPreference(this);
        driverpic = (ImageView)findViewById(R.id.driverpic);
        phonecall = (ImageView)findViewById(R.id.phonecall);
        driver_name= (TextView)findViewById(R.id.driver_name);
        driver_number= (TextView)findViewById(R.id.driver_number);
        txttime= (TextView)findViewById(R.id.txttime);
        title = (TextView)findViewById(R.id.title);
        myPref = new MyPref(TrackOrder_activity.this);
        parseLanguage = new ParseLanguage(myPref.getBookingData(),TrackOrder_activity.this);


        driver_name.setText(authPreference.getDriverFirstName()+" "+authPreference.getDriverLastName());
        driver_number.setText(authPreference.getDriverMobileNo());
        Picasso.get().load(authPreference.getDriverPhoto()).into(driverpic);
//


        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerTask.cancel();
                Intent o = new Intent(TrackOrder_activity.this,MainActivity.class);
                startActivity(o);
                finish();
            }
        });

        phonecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerTask.cancel();
                String phone =   driverphone_number;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        title.setText(parseLanguage.getParseString("Track_Order"));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doTimerTask();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mTimerTask.cancel();
        Intent i = new Intent(TrackOrder_activity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;



Log.e("aaa",""+RestroLat+" "+RestroLan+" "+CustomerLat+"  "+CustomerLng+" "+RiderLat+" "+RiderLng);
        Double resturentLat = Double.valueOf(RestroLat);
        Double resturentLng = Double.valueOf(RestroLan);
        Double customerLat = Double.valueOf(CustomerLat);
        Double customerLng = Double.valueOf(CustomerLng);
        Double riderLat = Double.valueOf(RiderLat);
        Double riderLng = Double.valueOf(RiderLng);


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


//        LatLng Restaurant = new LatLng(resturentLat, resturentLng);
//        mMap.addMarker(new MarkerOptions().position(Restaurant).title("Restaurant Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.restromarker)));


//           Restaurant = new LatLng(28.8316, 77.5780);
           Restaurant = new LatLng(resturentLat, resturentLng);
       // mMap.addMarker(new MarkerOptions().position(Restaurant).title("Restaurant Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.restromarker)));


//         CustomerLocation = new LatLng(28.6208, 77.3639);
         CustomerLocation = new LatLng(customerLat, customerLng);
      //  mMap.addMarker(new MarkerOptions().position(CustomerLocation).title("Customer Location"));

//        LatLng Riderlocation = new LatLng(28.6636, 77.3698);
           Riderlocation = new LatLng(riderLat, riderLng);
     //   mMap.addMarker(new MarkerOptions().position(Riderlocation).title("Rider Location"));

        List<LatLng> path = new ArrayList();

        //Execute Directions API request
        List<LatLng> path1 = new ArrayList();

        //Execute Directions API request

        //Draw the polyline

        CameraPosition cameraPosition = new CameraPosition.Builder().target(Restaurant).zoom(13).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions sOptions = new MarkerOptions()
                .anchor(0.5f, 0.75f)
                .position(Restaurant)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotelmarker));
        MarkerOptions dOptions = new MarkerOptions()
                .anchor(0.5f, 0.75f)
                .position(CustomerLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.customermarker));

        MarkerOptions rOption = new MarkerOptions()
                .anchor(0.5f, 0.75f)
                .position(Riderlocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerscooter));

        sourceMarker = mMap.addMarker(sOptions);
        destinationMarker = mMap.addMarker(dOptions);
        riderMarker = mMap.addMarker(rOption);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(sourceMarker.getPosition());
        builder.include(destinationMarker.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);

        String url = getUrl(Double.parseDouble("28.8316"), Double.parseDouble("77.5780")
                , Double.parseDouble("28.6208"), Double.parseDouble("77.3639"));

        FetchUrl fetchUrl = new FetchUrl();
        fetchUrl.execute(url);

        LatLng TripTime = new LatLng(40.416775, -4.70379);
        mMap.addMarker(new MarkerOptions().position(TripTime).title("Marker in Time"));
    }


    private View getInfoWindow(String distance, String duration, boolean isMyLocation) {
        View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                (FrameLayout) findViewById(R.id.map), false);
        TextView tv_desc = (TextView) infoWindow.findViewById(R.id.tv_desc);
        LinearLayout info_window_time = (LinearLayout) infoWindow.findViewById(R.id.info_window_time);
        TextView tv_distance = (TextView) infoWindow.findViewById(R.id.tv_distance);
        TextView tv_title = (TextView) infoWindow.findViewById(R.id.tv_title);
        TextView tv_duration = (TextView) infoWindow.findViewById(R.id.tv_duration);
        ImageView imgNavigate = (ImageView) infoWindow.findViewById(R.id.imgNavigate);

        //  ImageView imageView = (ImageView) infoWindow.findViewById(R.id.iv_scheduled_ride);
        // distance = distance.toUpperCase();
        if (isMyLocation) {
            info_window_time.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.VISIBLE);
            tv_duration.setText(duration);
            //   imageView.setImageResource(R.drawable.amu_bubble_mask);
            tv_distance.setText(distance);
            tv_title.setText(restroname);
            tv_desc.setText(restroadd);
            tv_desc.setMaxLines(1);
        } else {
            tv_desc.setMaxLines(2);
            tv_title.setText(customername);
            tv_desc.setText(customeradd);
            info_window_time.setVisibility(View.GONE);
//            tv_title.setVisibility(View.VISIBLE);
        }
        return infoWindow;
    }


    private String getUrl(double source_latitude, double source_longitude, double dest_latitude, double dest_longitude) {

        // Origin of route
        String str_origin = "origin=" + source_latitude + "," + source_longitude;

        // Destination of route
        String str_dest = "destination=" + dest_latitude + "," + dest_longitude;


        // Sensor enabled
        String sensor = "sensor=false" + "&key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.e("url", url + "");
        return url;
    }



    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            String distance = "";
            String duration = "";
//            isDragging = false;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) {
                        duration = (String) point.get("duration");
                        continue;
                    }


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                if (Restaurant != null && CustomerLocation != null) {
                    View infoWindow = getInfoWindow(distance, duration, true);
                    addIcon(infoWindow, true, Restaurant, CustomerLocation);

                    infoWindow = getInfoWindow(distance, duration, false);
                    addIcon(infoWindow, false, Restaurant, CustomerLocation);
                }

                Display display =getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y / 6;

                System.out.println("HEIGHT IS " + height + "WIDTH IS " + width);
                mMap.setPadding(100, 100, 100, 100);


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(sourceMarker.getPosition());
                builder.include(destinationMarker.getPosition());
                LatLngBounds bounds = builder.build();
                int padding = 0; // offset from edges of the map in pixels
//                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//                    mMap.moveCamera(cu);



                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLACK);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }


//            mMap.redirectMap(source_lat,source_lng,dest_lat,dest_lng);
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null && points != null) {
                mMap.addPolyline(lineOptions);
//                startAnim(points);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }


    private void addIcon(View infoWindow, boolean isMyLocation, LatLng pickUpCoordinates, LatLng dropCoordinates) {
        IconGenerator iconFactory = new IconGenerator(getApplicationContext());
        iconFactory.setContentView(infoWindow);
        iconFactory.setBackground(new RoundCornerDrawable());
        Bitmap icon = iconFactory.makeIcon();
        MarkerOptions markerOptions;
        if (isMyLocation) {
            markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(icon)).
                    position(pickUpCoordinates);
            Marker sourceMarker = mMap.addMarker(markerOptions);
            sourceMarker.setFlat(true);

            // sourceMarkerID = sourceMarker.getId();
            if (pickUpCoordinates.latitude > dropCoordinates.latitude &&
                    pickUpCoordinates.longitude > dropCoordinates.longitude) {
                sourceMarker.setAnchor(1.0f, 1.0f);
            } else {
                sourceMarker.setAnchor(0.0f, 0.0f);
            }
        } else {
            markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(icon)).
                    position(dropCoordinates);
            Marker destinationMarker = mMap.addMarker(markerOptions);
            destinationMarker.setFlat(true);
            // destinationMarkerID = destinationMarker.getId();
            if (pickUpCoordinates.latitude > dropCoordinates.latitude &&
                    pickUpCoordinates.longitude > dropCoordinates.longitude) {
                destinationMarker.setAnchor(0.0f, 0.0f);
            } else {
                destinationMarker.setAnchor(1.0f, 1.0f);
            }
        }
        // setMarkerClick(sourceMarkerID, destinationMarkerID);
    }

    public void Trackorder()
    {
        progressDialog = progressDialog.show(TrackOrder_activity.this, "", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.order_booking_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rerer",""+response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("OrderDetailItem");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String error = jsonObject1.getString("error");

                        String restaurant_lat = jsonObject1.getString("restaurant_lat");
                            String restaurant_lng = jsonObject1.getString("restaurant_lng");
                            String customer_lat = jsonObject1.getString("customer_lat");
                            String customer_lng = jsonObject1.getString("customer_lng");
                            String rider_last_lng = jsonObject1.getString("rider_last_lng");
                            String rider_last_lat = jsonObject1.getString("rider_last_lat");
                            String DriverMobileNo = jsonObject1.getString("DriverMobileNo");
                            String restaurant_name = jsonObject1.getString("restaurant_name");
                            String restaurant_address = jsonObject1.getString("restaurant_address");
                            String customer_name = jsonObject1.getString("customer_name");
                            String customer_address = jsonObject1.getString("customer_address");
                            String delivery_order_distance_display_rider_customer_message = jsonObject1.getString("delivery_order_distance_display_rider_customer_message");
                            driverphone_number = DriverMobileNo;
                            RestroLat= restaurant_lat;
                            RestroLan = restaurant_lng;
                            CustomerLat = customer_lat;
                            CustomerLng = customer_lng;
                            RiderLat = rider_last_lat;
                            RiderLng = rider_last_lng;
                            restroname= restaurant_name;
                            restroadd = restaurant_address;
                            customeradd = customer_address;
                            customername = customer_name;
                            txttime.setText(""+delivery_order_distance_display_rider_customer_message);

//                           Log.e("latandlong",""+RestroLat,RestroLan,CustomerLat,CustomerLng,RiderLat,RiderLng);

                       if (error.equals("0")){

                           if (RestroLat.equals("") ||RestroLat.equals("null")||RestroLat.equals("Null")||RestroLat.equals("NULL") || RestroLan.equals("") ||RestroLan.equals("null")||RestroLan.equals("Null")||RestroLan.equals("NULL") || CustomerLat.equals("") ||CustomerLat.equals("null")||CustomerLat.equals("Null")||CustomerLat.equals("NULL") || CustomerLng.equals("") ||CustomerLng.equals("null")||CustomerLng.equals("Null")||CustomerLng.equals("NULL") || RestroLan.equals("") ||RestroLan.equals("null")||RestroLan.equals("Null")||RestroLan.equals("NULL") || RiderLat.equals("") ||RiderLat.equals("null")||RiderLat.equals("Null")||RiderLat.equals("NULL")){

                               Toast.makeText(TrackOrder_activity.this, "Didn't find  latitude or longitude", Toast.LENGTH_SHORT).show();
                           }else {
//                               SupportMapFragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
//                               mapFragment.getMapAsync(TrackOrder_activity.this);


                           }

                                    }else {
//                           Toast.makeText(TrackOrder_activity.this, "Test", Toast.LENGTH_SHORT).show();
                       }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
//                params.put("orderid", "232");
                params.put("orderid", ""+Activity_Booking.orderidd);
                params.put("lang_code", myPref.getCustomer_default_langauge());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void doTimerTask(){

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Trackorderupdateapi();
//                        Toast.makeText(TrackOrder_activity.this, "Test", Toast.LENGTH_SHORT).show();
                        Log.d("TIMER", "TimerTask run");
                    }
                });
            }};

        // public void schedule (TimerTask task, long delay, long period)
        t.schedule(mTimerTask, 1000, 10000);  //

    }

    public void Trackorderupdateapi()
    {
//        progressDialog = progressDialog.show(TrackOrder_activity.this, "", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.order_booking_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("OrderDetailItem");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String error = jsonObject1.getString("error");

                        String restaurant_lat = jsonObject1.getString("restaurant_lat");
                        String restaurant_lng = jsonObject1.getString("restaurant_lng");
                        String customer_lat = jsonObject1.getString("customer_lat");
                        String customer_lng = jsonObject1.getString("customer_lng");
                        String rider_last_lng = jsonObject1.getString("rider_last_lng");
                        String rider_last_lat = jsonObject1.getString("rider_last_lat");
                        String duration_time_text = jsonObject1.getString("duration_time_text");
                        String distance_text = jsonObject1.getString("distance_text");
                        String delivery_order_distance_display_rider_customer_message = jsonObject1.getString("delivery_order_distance_display_rider_customer_message");
                        txttime.setText(""+delivery_order_distance_display_rider_customer_message);

                        RestroLat= restaurant_lat;
                        RestroLan = restaurant_lng;
                        CustomerLat = customer_lat;
                        CustomerLng = customer_lng;
                        RiderLat = rider_last_lat;
                        RiderLng = rider_last_lng;
                        duration = duration_time_text;
                        distance= distance_text;

//                           Log.e("latandlong",""+RestroLat,RestroLan,CustomerLat,CustomerLng,RiderLat,RiderLng);

                        if (error.equals("0")){

                                if (RestroLat.equals("") ||RestroLat.equals("null")||RestroLat.equals("Null")||RestroLat.equals("NULL") || RestroLan.equals("") ||RestroLan.equals("null")||RestroLan.equals("Null")||RestroLan.equals("NULL") || CustomerLat.equals("") ||CustomerLat.equals("null")||CustomerLat.equals("Null")||CustomerLat.equals("NULL") || CustomerLng.equals("") ||CustomerLng.equals("null")||CustomerLng.equals("Null")||CustomerLng.equals("NULL") || RestroLan.equals("") ||RestroLan.equals("null")||RestroLan.equals("Null")||RestroLan.equals("NULL") || RiderLat.equals("") ||RiderLat.equals("null")||RiderLat.equals("Null")||RiderLat.equals("NULL")){

//                                    Toast.makeText(TrackOrder_activity.this, "Didn't find restaurant latitude ", Toast.LENGTH_SHORT).show();
                                }else {
                                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                    mapFragment.getMapAsync(TrackOrder_activity.this);

                                }



                        }else {
//                            Toast.makeText(TrackOrder_activity.this, "Test", Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
//                params.put("orderid", "232");
                params.put("lang_code", myPref.getCustomer_default_langauge());
                params.put("orderid", ""+Activity_Booking.orderidd);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}
