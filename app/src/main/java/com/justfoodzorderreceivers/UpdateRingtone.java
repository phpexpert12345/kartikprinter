package com.justfoodzorderreceivers;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.justfoodzorderreceivers.Model.RingtoneItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateRingtone extends AppCompatActivity {
    MyPref myPref;
    List<RingtoneItem> ringtoneItemList=new ArrayList<>();
    RecyclerView recycle_ringtone;
    Button button_update;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    RingtoneAdapter ringtoneAdapter;
    SharedPreferences sharedPreferences;
    ParseLanguage parseLanguage;
    TextView txt_order_ringtone;
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update_ringtone);
        myPref=new MyPref(this);
        parseLanguage=new ParseLanguage(myPref.getBookingData(),this);
        recycle_ringtone=findViewById(R.id.recyler_ringtones);
        button_update=findViewById(R.id.btn_update_ringtone);
        txt_order_ringtone=findViewById(R.id.txt_ringtone_title);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("Order", MODE_PRIVATE);
//        String order_ringtone=parseLanguage.getParseString("Set_Default_order_ringtone");
        String submit=parseLanguage.getParseString("SubmitText");
//        if(order_ringtone.equalsIgnoreCase("No Response")){
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                txt_order_ringtone.setText(getString(R.string.order_ringtone));
            }
//        }
//        else {
//            txt_order_ringtone.setText(order_ringtone);
//        }
        if(submit.equalsIgnoreCase("No Response")){
            if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
                button_update.setText(getString(R.string.submit_german));
            }
        }
        else {
            button_update.setText(submit);
        }
//
       getRingtones();
       button_update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
if(ringtoneAdapter!=null){
    int selected_position=ringtoneAdapter.getSelected_position();
    String ring_tone_url=ringtoneItemList.get(selected_position).ring_tone_url;
    UpdateRingtone(ring_tone_url);
}
           }
       });

    }
    public void getRingtones(){


            progressDialog = progressDialog.show(this, "", parseLanguage.getParseString("Please_wait"), false, false);

        String url=Url.ringtone_list;
        url=url+"?lang_code=" +myPref.getCustomer_default_langauge();
        Log.e("printer", url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    if(ringtoneItemList.size()>0){
                        ringtoneItemList.clear();
                    }
                    JSONObject jsonObject=new JSONObject(s);
                    if(jsonObject.has("RingToneList")){
                        JSONArray jsonArray=jsonObject.getJSONArray("RingToneList");
                        if(jsonArray!=null && jsonArray.length()>0){
                            for (int i=0; i<jsonArray.length();i++){
                                JSONObject json=jsonArray.getJSONObject(i);
                                RingtoneItem ringtoneItem=new RingtoneItem();
                                ringtoneItem.id=json.optString("id");
                                ringtoneItem.ring_tone_url=json.optString("ring_tone_url");
                                ringtoneItemList.add(ringtoneItem);


                            }
                        }
                        if(ringtoneItemList.size()>0){
                            setRingtoneAdapter();
                        }
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
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public void setRingtoneAdapter(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

         ringtoneAdapter=new RingtoneAdapter(ringtoneItemList,myPref);
        recycle_ringtone.setAdapter(ringtoneAdapter);
        recycle_ringtone.setLayoutManager(layoutManager);
    }
    public void UpdateRingtone(final String ring_tone_url){
        progressDialog = progressDialog.show(this, "", parseLanguage.getParseString("Please_wait"), false, false);
StringRequest stringRequest=new StringRequest(Request.Method.POST, Url.ringtone_update, new Response.Listener<String>() {
    @Override
    public void onResponse(String s) {
progressDialog.dismiss();
finish();
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
        params.put("ring_tone_url",ring_tone_url );
        params.put("lang_code",myPref.getCustomer_default_langauge());
        params.put("restaurant_id", sharedPreferences.getString("restaurant_id", ""));
        return params;
    }
};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
