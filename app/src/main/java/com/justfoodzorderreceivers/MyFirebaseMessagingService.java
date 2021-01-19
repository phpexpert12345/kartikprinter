package com.justfoodzorderreceivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;

import android.content.Context;

import android.content.Intent;

import android.media.RingtoneManager;

import android.net.Uri;

import android.os.Build;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    NotificationManager notificationManager;
    private static String ADMIN_CHANNEL_ID="1";


    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static String driverData=null;

    private static final String CHANNEL_ID = "channel_id01";
    MyPref myPref;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("NEW_TOKEN",s);
        // storeRegIdInPref(s);

        // sending reg id to your server
        sendRegistrationToServer(s);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }



    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override


    public void onMessageReceived(RemoteMessage remoteMessage) {
if(remoteMessage.getData()!=null){
    String orderId=remoteMessage.getData().get("orderid");
    if(orderId!=null){
        if(BaseApplication.isActivityVisible()){
            broadcastIntent(orderId);
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("orderId",orderId);
            intent.putExtra("type", "from_notification");
            PendingIntent pendingIntent=PendingIntent.getActivity(this,25,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels();
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);

                    notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("New Order")
                    .setContentText("Click to print order")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());



        }
    }

}


//        sendNotification(""+remoteMessage.getNotification().getBody());

    }


    private void broadcastIntent(String orderId) {
        if(BaseApplication.isActivityVisible()) {
            Intent intent = new Intent();
            intent.setAction("newOrder");
            Bundle b = new Bundle();
            b.putString("orderId", orderId);
            b.putString("type","from_broadcast");
            intent.putExtras(b);
            sendBroadcast(intent);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.app_name);
        String adminChannelDescription = getString(R.string.app_name);
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        //adminChannel.setLightCo#F123);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }


}