//package com.esolz.fitnessapp.gcmnotification;
//
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.os.Bundle;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.esolz.fitnessapp.R;
//import com.esolz.fitnessapp.fitness.SplashActivity;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
///**
// * Created by ltp on 07/08/15.
// */
//public class GCMIntentService extends IntentService {
//
//    public static final int NOTIFICATION_ID = 1;
//    private NotificationManager mNotificationManager;
//    NotificationCompat.Builder builder;
//
//    String TAG = "GcmIntentService";
//
//    public static String MY_EVENT_ACTION = "My_Events";
//    public static String MY_EVENT_ACTION_GROUP = "My_Events_Group";
//
//    //APA91bHY1ekLooh2G_FZGeH4dO-NEDXPi-7AFL_TRllh1Ps4QKYxISmEv51RJkVpFAssT2532piD1DIP9fSs2gmHVP50A1cobrFW3bByFpXdvtonTxPGhfpOIXCyEvRoiplDZiegRzN7
//
//    public GCMIntentService() {
//        super("GcmIntentService");
//
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        Log.i("Soutrik", "Ghosh");
//
//        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        String messageType = gcm.getMessageType(intent);
//
//        if (!extras.isEmpty()) {
//
//            if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//            } else if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_DELETED.equals(messageType)) {
//            } else if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                Log.i(TAG, "Received: " + extras.toString());
//
//                sendNotificationGroup(extras.toString());
//
////                {
////                    "details":{
////                    "chat_id":1264, "send_from":"101", "send_to":"45", "message":
////                    "nabcdhhhhhh", "type":"m",
////                            "stickername":"sticker", "chat_date":"2015-07-29", "chat_time":
////                    "15:32:01", "chat_type":"O", "name":"koushik Sarkar", "photo":"",
////                            "photo_thumb":"", "status":"Unread", "file_link":"", "file_available":
////                    "Y"
////                },"status":"success"
////                }
//
////                try {
////                    if (extras.getString("chat_type").equalsIgnoreCase("O")) {
////
////                        Log.i("Single Chat", extras.getString("chat_type"));
////
////                        if (AppController.isAppRunning().equals("YES")) {
////
////                            Intent brodIntent = new Intent();
////
////                            brodIntent.setAction(MY_EVENT_ACTION);
////
////                            brodIntent.putExtra("chat_id", "" + extras.getString("chat_id"));
////                            brodIntent.putExtra("send_from", "" + extras.getString("send_from"));
////                            brodIntent.putExtra("send_to", "" + extras.getString("send_to"));
////                            brodIntent.putExtra("message", "" + extras.getString("message"));
////                            brodIntent.putExtra("type", "" + extras.getString("type"));
////                            brodIntent.putExtra("stickername", "" + extras.getString("stickername"));
////                            brodIntent.putExtra("chat_time", "" + extras.getString("2"));
////                            brodIntent.putExtra("chat_date", "" + extras.getString("chat_date"));
////                            brodIntent.putExtra("status", "" + extras.getString("status"));
////                            brodIntent.putExtra("file_link", "" + extras.getString("file_link"));
////                            brodIntent.putExtra("file_available", "" + extras.getString("file_available"));
////                            brodIntent.putExtra("name", "" + extras.getString("name"));
////                            brodIntent.putExtra("photo", "" + extras.getString("photo"));
////                            brodIntent.putExtra("photo_thumb", "" + extras.getString("photo_thumb"));
////                            brodIntent.putExtra("chat_type", "" + extras.getString("chat_type"));
////
////                            sendBroadcast(brodIntent);
////
////                            Log.d("----TAG----", "Received new msg.....");
////                        } else {
////                            // if (AppController.isNotificationON().equals("ON")) {
////                            if (extras.getString("type").equals("s")) {
////                                sendNotification("Sent a sticker.", extras.getString("name"), extras.getString("send_from"), extras.getString("chat_type"));
////                            } else if (extras.getString("type").equals("m")) {
////                                sendNotification(extras.getString("message"), extras.getString("name"), extras.getString("send_from"), extras.getString("chat_type"));
////                            } else {
////                                sendNotification(extras.getString("message"), extras.getString("name"), extras.getString("send_from"), extras.getString("chat_type"));
////                            }
//////                            } else {
//////                                Log.i("NotificationState : ", AppController.isNotificationON());
//////                            }
////                        }
////                    } else {
////
////                        Log.i("Group Chat", extras.getString("chat_type"));
////                        Log.i("Group Page On : ", AppController.isAppRunningGroup());
//////
////                        if (AppController.isAppRunningGroup().equals("YES")) {
////                            Log.i("Group Page On Yes : ", AppController.isAppRunningGroup());
////                            Intent brodIntent = new Intent();
////
////                            brodIntent.setAction(MY_EVENT_ACTION_GROUP);
////
////                            brodIntent.putExtra("chat_id", "" + extras.getString("chat_id"));
////                            brodIntent.putExtra("send_from", "" + extras.getString("send_from"));
////                            brodIntent.putExtra("send_to", "" + extras.getString("send_to"));
////                            brodIntent.putExtra("message", "" + extras.getString("message"));
////                            brodIntent.putExtra("type", "" + extras.getString("type"));
////                            brodIntent.putExtra("stickername", "" + extras.getString("stickername"));
////                            brodIntent.putExtra("chat_time", "" + extras.getString("2"));
////                            brodIntent.putExtra("chat_date", "" + extras.getString("chat_date"));
////                            brodIntent.putExtra("status", "" + extras.getString("status"));
////                            brodIntent.putExtra("file_link", "" + extras.getString("file_link"));
////                            brodIntent.putExtra("file_available", "" + extras.getString("file_available"));
////                            brodIntent.putExtra("name", "" + extras.getString("name"));
////                            brodIntent.putExtra("photo", "" + extras.getString("photo"));
////                            brodIntent.putExtra("photo_thumb", "" + extras.getString("photo_thumb"));
////                            brodIntent.putExtra("chat_type", "" + extras.getString("chat_type"));
////
////                            sendBroadcast(brodIntent);
////
////                            Log.d("----TAG----", "Received new msg.....");
////                        } else {
////                            Log.i("Group Page On NO : ", AppController.isAppRunningGroup());
////                            //if (AppController.isNotificationON().equals("ON")) {
////                            if (extras.getString("type").equals("s")) {
////                                sendNotificationGroup("Sent a sticker.", extras.getString("name"), extras.getString("send_from"), extras.getString("chat_type"));
////                            } else if (extras.getString("type").equals("m")) {
////                                sendNotificationGroup(extras.getString("message"), extras.getString("name"), extras.getString("send_from"), extras.getString("chat_type"));
////                            } else {
////                                sendNotificationGroup(extras.getString("message"), extras.getString("name"), extras.getString("send_from"), extras.getString("chat_type"));
////                            }
//////                            } else {
//////                                Log.i("NotificationState : ", AppController.isNotificationON());
//////                            }
////                        }
////                    }
////                } catch (Exception e) {
////                    Log.i("Service exception : ", e.toString());
////                }
//            }
//
//        }
//        // Release the wake lock provided by the WakefulBroadcastReceiver.
//        GCMBroadCastReceiver.completeWakefulIntent(intent);
//    }
//
//    private final void sendNotificationGroup(final String msg) {
//        mNotificationManager = (NotificationManager)
//                this.getSystemService(Context.NOTIFICATION_SERVICE);
//        final PendingIntent contentIntentGroup;
//        final Intent intentLandGroup;
//        String notificationTitle = "";
//
////        Log.i(" msg ", msg);
////        Log.i(" name ", name);
////        Log.i(" sendFrom ", sendFrom);
////        Log.i(" chatType ", chatType);
////
//        intentLandGroup = new Intent(this, SplashActivity.class);
////        intentLandGroup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        intentLandGroup.putExtra("Notification", "notify");
////        intentLandGroup.putExtra("NotificationID", "" + sendFrom);
////        intentLandGroup.putExtra("NotificationType", "" + chatType);
//        contentIntentGroup = PendingIntent.getActivity(this, 0, intentLandGroup, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        notificationTitle = msg;
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo)
//                        .setContentTitle(notificationTitle)
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(msg))
//                        .setContentText(msg)
//                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                        .setVibrate(new long[]{1000, 1000})
//                        .setLights(Color.RED, 3000, 3000);
//
//        mBuilder.setAutoCancel(true);
//        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
//        mBuilder.setContentIntent(contentIntentGroup);
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//    }
//
//}
