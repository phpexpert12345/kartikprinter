package com.justfoodzorderreceivers.Utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.justfoodzorderreceivers.Activity_Splash;
import com.justfoodzorderreceivers.R;

public class StartActivityService extends Service {
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    NotificationChannel notificationChannel;
    String NOTIFICATION_CHANNEL_ID = "17";

    @Override
    public void onCreate() {
        super.onCreate();
        CreateLocationNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @SuppressLint("WrongConstant")
    public void CreateLocationNotification(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Intent intent1=new Intent(this, Activity_Splash.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,25,intent1,PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText("Auto start after boot")
                .setSmallIcon(getsmallicon())
                .setPriority(Notification.PRIORITY_LOW)
                .setVisibility(Notification.VISIBILITY_SECRET)
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "StartActivity", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            notificationManager.createNotificationChannel(notificationChannel);

            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            startForeground(21, mBuilder.build());
        }else{
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            startForeground(21, mBuilder.build());
        }
    }
    public int getsmallicon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return R.drawable.life_logo;
        } else {
            return R.drawable.life_logo;
        }
    }
}
