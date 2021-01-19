package com.justfoodzorderreceivers.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.justfoodzorderreceivers.Activity_Splash;
import com.justfoodzorderreceivers.R;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
                Intent intentService = new Intent(context, StartActivityService.class);
                context.startForegroundService(intentService);
            }
            else {
                Intent serviceIntent = new Intent(context, Activity_Splash.class);
                serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(serviceIntent);
            }

        }
    }
    private void ShowActivityNotification(Context context){
        Intent intent=new Intent(context,Activity_Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pi=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(0)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText("testing notification after ");
        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
