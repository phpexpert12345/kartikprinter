package com.justfoodzorderreceivers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class AuthPreference {
    private static final String MY_PREFERENCES = "MY_PREFERENCES";
    private static final int MODE = Context.MODE_PRIVATE;
    private static AuthPreference pref;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public AuthPreference(Context context) {
        sharedPreference = context.getSharedPreferences(MY_PREFERENCES, MODE);
        editor = sharedPreference.edit();
    }



    public void setrestaurant_id(String value) {
        editor.putString("restaurant_id", value).commit();
    }

    public String getrestaurant_id() {
        return sharedPreference.getString("restaurant_id", "");
    }

       public void setrestaurant_lat(String value) {
        editor.putString("restaurant_lat", value).commit();
    }

    public String getrestaurant_lat() {
        return sharedPreference.getString("restaurant_lat", "");
    }

    public void setrestaurant_lng(String value) {
        editor.putString("restaurant_lng", value).commit();
    }

    public String getrestaurant_lng() {
        return sharedPreference.getString("restaurant_lng", "");
    }


    public void setcustomer_lat(String value) {
        editor.putString("customer_lat", value).commit();
    }

    public String getcustomer_lat() {
        return sharedPreference.getString("customer_lat", "");
    }

    public void setcustomer_lng(String value) {
        editor.putString("customer_lng", value).commit();
    }

    public String getcustomer_lng() {
        return sharedPreference.getString("customer_lng", "");
    }


    public void setrider_last_lat(String value) {
        editor.putString("rider_last_lat", value).commit();
    }

    public String getrider_last_lat() {
        return sharedPreference.getString("rider_last_lat", "");
    }

    public void setrider_last_lng(String value) {
        editor.putString("rider_last_lng", value).commit();
    }

    public String getrider_last_lng() {
        return sharedPreference.getString("rider_last_lng", "");
    }

    public void setDriverFirstName(String value) {
        editor.putString("DriverFirstName", value).commit();
    }

    public String getDriverFirstName() {
        return sharedPreference.getString("DriverFirstName", "");
    }

    public void setDriverLastName(String value) {
        editor.putString("DriverLastName", value).commit();
    }

    public String getDriverLastName() {
        return sharedPreference.getString("DriverLastName", "");
    }


    public void setDriverMobileNo(String value) {
        editor.putString("DriverMobileNo", value).commit();
    }

    public String getDriverMobileNo() {
        return sharedPreference.getString("DriverMobileNo", "");
    }


    public void setDriverPhoto(String value) {
        editor.putString("DriverPhoto", value).commit();
    }

    public String getDriverPhoto() {
        return sharedPreference.getString("DriverPhoto", "");
    }
    public void setorderid(int value) {
        editor.putInt("orderid", value).commit();
    }

    public int getorderid() {
        return sharedPreference.getInt("orderid", Integer.parseInt(""));
    }

}