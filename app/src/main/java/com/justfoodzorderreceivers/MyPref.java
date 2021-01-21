package com.justfoodzorderreceivers;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class MyPref {


    Context context;
    SharedPreferences sharedPreferences;
    private String userId;
    private String mobile;
    private String devideId;
    private String firebaseTokenId;
    private String isOnline;
    private String onBooking;

    public String getRingtone_url() {
        ringtone_url=sharedPreferences.getString("ringtone_url","");
        return ringtone_url;

    }

    public void setRingtone_url(String ringtone_url) {
        this.ringtone_url = ringtone_url;
        sharedPreferences.edit().putString("ringtone_url", ringtone_url).apply();
    }

    private String ringtone_url;


    public String getMode() {
        this.mode=sharedPreferences.getString("mode", "");
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
        sharedPreferences.edit().putString("mode", mode).apply();
    }

    private String mode;
    private String userName,mailid,bankDetail,ridetype,profileImage;
    private String city,state,startDate,stopDate,bookingData,rideStatus,rideCurrentStatus;
    private String vehicleCate,vehicleImage,lat,lng,emergency,vecdetail,total_balance;
    private String cateId;
    private String bookId;
    private String docCode;
    private String isRideAccepted;
    private String bookType;
    private String multiCatId;
    private String newCatID;
    private String approvalstatus;
    private String customer_default_langauge;

    public String getAuto_print_enable() {
        auto_print_enable=sharedPreferences.getString("auto_print_enable","");
        return auto_print_enable;
    }

    public void setAuto_print_enable(String auto_print_enable) {
        this.auto_print_enable = auto_print_enable;
        sharedPreferences.edit().putString("auto_print_enable",auto_print_enable).apply();
    }

    private String auto_print_enable;



    public String getCustomer_default_langauge(){
        customer_default_langauge=sharedPreferences.getString("customer_default_langauge","");
        return customer_default_langauge;
    }
    public void setCustomer_default_langauge(String customer_default_langauge) {
        this.customer_default_langauge = customer_default_langauge;
        sharedPreferences.edit().putString("customer_default_langauge", customer_default_langauge).commit();
    }




    public String getTotal_balance(){
        total_balance=sharedPreferences.getString("total_balance","");
        return total_balance;
    }
    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
        sharedPreferences.edit().putString("total_balance", total_balance).commit();
    }

    public String getApprovalstatus(){
        approvalstatus=sharedPreferences.getString("approvalstatus","");
        return approvalstatus;
    }
    public void setApprovalstatus(String approvalstatus) {
        this.approvalstatus = approvalstatus;
        sharedPreferences.edit().putString("approvalstatus", approvalstatus).commit();
    }




    public String getOnBooking(){
        onBooking=sharedPreferences.getString("onBooking","");
        return onBooking;
    }
    public void setOnBooking(String onBooking) {
        this.onBooking = onBooking;
        sharedPreferences.edit().putString("onBooking", onBooking).commit();
    }
    public String getRideCurrentStatus(){
        rideCurrentStatus=sharedPreferences.getString("rideCurrentStatus","");
        return rideCurrentStatus;
    }
    public void setRideCurrentStatus(String rideCurrentStatus) {
        this.rideCurrentStatus = rideCurrentStatus;
        sharedPreferences.edit().putString("rideCurrentStatus", rideCurrentStatus).commit();
    }
    public String getIsOnline(){
        isOnline=sharedPreferences.getString("isOnline","");
        return isOnline;
    }
    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
        sharedPreferences.edit().putString("isOnline", isOnline).commit();
    }
    public String getVecdetail(){
        vecdetail=sharedPreferences.getString("vecdetail","");
        return vecdetail;
    }
    public void setVecdetail(String vecdetail) {
        this.vecdetail = vecdetail;
        sharedPreferences.edit().putString("vecdetail", vecdetail).commit();
    }
     public String getEmergency(){
            emergency=sharedPreferences.getString("emergency","");
            return emergency;
        }
        public void setEmergency(String emergency) {
            this.emergency = emergency;
            sharedPreferences.edit().putString("emergency", emergency).commit();
        }

    public String getIsRideAccepted(){
        isRideAccepted=sharedPreferences.getString("isRideAccepted","");
        return isRideAccepted;
    }
    public void setIsRideAccepted(String isRideAccepted) {
        this.isRideAccepted = isRideAccepted;
        sharedPreferences.edit().putString("isRideAccepted", isRideAccepted).commit();
    }
    public String getDocCode(){
        docCode=sharedPreferences.getString("docCode","");
        return docCode;
    }
    public void setDocCode(String docCode) {
        this.docCode = docCode;
        sharedPreferences.edit().putString("docCode", docCode).commit();
    }
    public String getBookId(){
        bookId=sharedPreferences.getString("bookId","");
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
        sharedPreferences.edit().putString("bookId", bookId).commit();
    }


    public String getBookType(){
        bookType=sharedPreferences.getString("bookType","");
        return bookType;
    }
    public void setBookType(String bookType) {
        this.bookType = bookType;
        sharedPreferences.edit().putString("bookType", bookType).commit();
    }


    public String getNewCatID(){
        newCatID=sharedPreferences.getString("newCatID","");
        return newCatID;
    }
    public void setNewCatID(String newCatID) {
        this.newCatID = newCatID;
        sharedPreferences.edit().putString("newCatID", newCatID).commit();
    }



    public String getMultiCatId(){
        multiCatId=sharedPreferences.getString("multiCatId","");
        return multiCatId;
    }
    public void setMultiCatId(String multiCatId) {
        this.multiCatId = multiCatId;
        sharedPreferences.edit().putString("multiCatId", multiCatId).commit();
    }



    public String getCateId(){
        cateId=sharedPreferences.getString("cateId","");
        return cateId;
    }
    public void setCateId(String cateId) {
        this.cateId = cateId;
        sharedPreferences.edit().putString("cateId", cateId).commit();
    }


    public String getLng(){
        lng=sharedPreferences.getString("lng","");
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
        sharedPreferences.edit().putString("lng", lng).commit();
    }

    public String getLat(){
        lat=sharedPreferences.getString("lat","");
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
        sharedPreferences.edit().putString("lat", lat).commit();
    }

    public String getVehicleCate(){
        vehicleCate=sharedPreferences.getString("vehicleCate","");
        return vehicleCate;
    }
    public void setVehicleCate(String vehicleCate) {
        this.vehicleCate = vehicleCate;
        sharedPreferences.edit().putString("vehicleCate", vehicleCate).commit();
    }

    public String getVehicleImage(){
        vehicleImage=sharedPreferences.getString("vehicleImage","");
        return vehicleImage;
    }
    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
        sharedPreferences.edit().putString("vehicleImage", vehicleImage).commit();
    }

    public String getRideStatus(){
        rideStatus=sharedPreferences.getString("rideStatus","");
        return rideStatus;
    }
    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
        sharedPreferences.edit().putString("rideStatus", rideStatus).commit();
    }

    public String getBookingData(){
        bookingData=sharedPreferences.getString("bookingData","");
        return bookingData;
    }
    public void setBookingData(String bookingData) {
        this.bookingData = bookingData;
        sharedPreferences.edit().putString("bookingData", bookingData).commit();
    }
    public String getStopDate(){
        stopDate=sharedPreferences.getString("stopDate","");
        return stopDate;
    }
    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
        sharedPreferences.edit().putString("stopDate", stopDate).commit();
    }

    public String getStartDate(){
        startDate=sharedPreferences.getString("startDate","");
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
        sharedPreferences.edit().putString("startDate", startDate).commit();
    }
    public String getCity(){
        city=sharedPreferences.getString("city","");
        return city;
    }
    public void setCity(String city) {
        this.city = city;
        sharedPreferences.edit().putString("city", city).commit();
    }
    public String getState(){
        state=sharedPreferences.getString("state","");
        return state;
    }
    public void setState(String state) {
        this.state = state;
        sharedPreferences.edit().putString("state", state).commit();
    }


    public String getProfileImage(){
        profileImage=sharedPreferences.getString("profileImage","");
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
        sharedPreferences.edit().putString("profileImage", profileImage).commit();
    }
    public String getRidetype(){
        ridetype=sharedPreferences.getString("ridetype","");
        return ridetype;
    }
    public void setRidetype(String ridetype) {
        this.ridetype = ridetype;
        sharedPreferences.edit().putString("ridetype", ridetype).commit();
    }
    public String getBankDetail(){
        bankDetail=sharedPreferences.getString("bankDetail","");
        return bankDetail;
    }
    public void setBankDetail(String bankDetail) {
        this.bankDetail = bankDetail;
        sharedPreferences.edit().putString("bankDetail", bankDetail).commit();
    }
    public String getFirebaseTokenId(){
        firebaseTokenId=sharedPreferences.getString("firebaseTokenId","");
        return firebaseTokenId;
    }
    public void setFirebaseTokenId(String firebaseTokenId) {
        this.firebaseTokenId = firebaseTokenId;
        sharedPreferences.edit().putString("firebaseTokenId", firebaseTokenId).commit();
    }

    public String getMailid(){
        mailid=sharedPreferences.getString("mailid","");
        return mailid;
    }
    public void setMailid(String mailid) {
        this.mailid = mailid;
        sharedPreferences.edit().putString("mailid", mailid).commit();
    }

    public String getDevideId(){
        devideId=sharedPreferences.getString("devideId","");
        return devideId;
    }
    public void setDevideId(String devideId) {
        this.devideId = devideId;
        sharedPreferences.edit().putString("devideId", devideId).commit();
    }

    public String getMobile(){
        mobile=sharedPreferences.getString("mobile","");
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
        sharedPreferences.edit().putString("mobile", mobile).commit();
    }

    public String getUserName() {
        userName =sharedPreferences.getString("userName","");
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
        sharedPreferences.edit().putString("userName", userName).commit();
    }

    public String getUserId() {
        userId =sharedPreferences.getString("userId","");
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
        sharedPreferences.edit().putString("userId", userId).commit();
    }
    public MyPref(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

    }

    public void logOut()
    {
//        sharedPreferences.edit().clear().commit();

    }
    public void logOut(Context context)
    {
        sharedPreferences.edit().clear().commit();
        deleteCache(context);

    }

    //for clearing cache of app
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
