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
    public void onMessageReceived(RemoteMessage remoteMessage) {
if(remoteMessage.getData()!=null){
    String orderId=remoteMessage.getData().get("orderid");
    if(orderId!=null){
        if(BaseApplication.isActivityVisible()){
            broadcastIntent(orderId);
        }
        else {
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("orderId",orderId);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(resultIntent);
//            startActivity(new Intent(this,MainActivity.class));
            String title=remoteMessage.getData().get("title");
            String text=remoteMessage.getData().get("text");
            Intent intent = new Intent(this, MainActivity.class);
            Random random=new Random();
            int id=random.nextInt(10000);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("type", "from_notification");
            intent.putExtra("notification_id",id);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,25,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);

                    notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("task",
                        "Task Notification",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationBuilder.setChannelId("task");
                notificationManager.createNotificationChannel(channel);

            }


            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());



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





}