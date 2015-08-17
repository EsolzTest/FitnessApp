package com.esolz.fitnessapp.fitness;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.esolz.fitnessapp.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by ltp on 07/08/15.
 */
public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    String TAG = "GcmIntentService";

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("Soutrik", "Ghosh");

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        Log.i(TAG, "Received: " + extras.toString());

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.i(TAG, "Received: Error : " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.i(TAG, "Received: Deleted : " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(TAG, "Received: " + extras.toString());
                //sendNotificationGroup(extras.toString());
            }

        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadCastReceiver.completeWakefulIntent(intent);
    }

    private final void sendNotificationGroup(final String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        final PendingIntent contentIntentGroup;
        final Intent intentLandGroup;
        String notificationTitle = "";

//        Log.i(" msg ", msg);
//        Log.i(" name ", name);
//        Log.i(" sendFrom ", sendFrom);
//        Log.i(" chatType ", chatType);
//
        //intentLandGroup = new Intent(this, SplashActivity.class);
//        intentLandGroup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intentLandGroup.putExtra("Notification", "notify");
//        intentLandGroup.putExtra("NotificationID", "" + sendFrom);
//        intentLandGroup.putExtra("NotificationType", "" + chatType);
        //contentIntentGroup = PendingIntent.getActivity(this, 0, intentLandGroup, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationTitle = msg;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(notificationTitle)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000, 1000})
                        .setLights(Color.RED, 3000, 3000);

        mBuilder.setAutoCancel(true);
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        //mBuilder.setContentIntent(contentIntentGroup);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
