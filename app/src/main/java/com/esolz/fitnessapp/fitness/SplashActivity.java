//package com.esolz.fitnessapp.fitness;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.esolz.fitnessapp.R;
//import com.esolz.fitnessapp.datatype.LoginDataType;
//import com.esolz.fitnessapp.helper.AppConfig;
//import com.esolz.fitnessapp.helper.ConnectionDetector;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
//import java.io.IOException;
//
//public class SplashActivity extends Activity {
//
//    SharedPreferences loginPreferences;
//    ConnectionDetector cd;
//
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//    public static final String EXTRA_MESSAGE = "message";
//    public static final String PROPERTY_REG_ID = "registration_id";
//    private static final String PROPERTY_APP_VERSION = "appVersion";
//    static final String TAG = "GCMRegistration";
//    String SENDER_ID = "735362043127";   //751757095331  //104883704281 lasso
//    String regid = "", msg = "";
//    GoogleCloudMessaging gcm;
//
//
//    //APA91bHhuMGK1fKzCoURHHjtdNQRjmJVi-7l3sa3aejoobocxi3IIJwI0CYnh3KgX4pe2dyQ4CMof-
//    // nLSR-hmhvBxKyMbLVrqRRqlSoud1GjqiTPuVlpfy0EYpdXZ3WoaH6oLs0O9ut9
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        setContentView(R.layout.activity_splash);
//
//        cd = new ConnectionDetector(SplashActivity.this);
//
//        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
//
//        if (cd.isConnectingToInternet()) {
//            gcm = GoogleCloudMessaging.getInstance(SplashActivity.this);
//            regid = getRegistrationId(SplashActivity.this);
//
//            if (regid.isEmpty()) {
//                registerInBackground();
//            } else {
//                Log.d("Reg", "ID Shared Pref => " + regid);
//                AppConfig.appRegId = regid;
//                sendRegistrationIdToBackend();
//            }
//        }
//
//
//        System.out.println("!! " + loginPreferences.getString("Remember", ""));
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//                try {
//
//                    //   if (cd.isConnectingToInternet()) {
//
//                    if (loginPreferences.getString("Remember", "").equals("remember")) {
//
//                        AppConfig.loginDatatype = new LoginDataType(
//                                loginPreferences.getString("UserId", ""),
//                                loginPreferences.getString("Username", ""),
//                                loginPreferences.getString("Password", ""));
//                        Intent intent = new Intent(SplashActivity.this, LandScreenActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
////                    } else {
////                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
////                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, 1000);
//    }
//
//    private void registerInBackground() {
//        (new getGCMID()).execute();
//    }
//
//    class getGCMID extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            // TODO Auto-generated method stub
//            try {
//                if (gcm == null) {
//                    gcm = GoogleCloudMessaging.getInstance(SplashActivity.this);
//                }
//                regid = gcm.register(SENDER_ID);
//                msg = regid;
//            } catch (IOException ex) {
//                msg = "";
//            }
//            Log.d("GCM", msg);
//            Log.d("GCM--------", regid);
//            return null;
//        }
//
//        protected void onPostExecute(Void resultt) {
//            super.onPostExecute(resultt);
//            if (msg.equals("")) {
//
//            } else {
//                sendRegistrationIdToBackend();
//            }
//
//        }
//    }
//
//
//    private void sendRegistrationIdToBackend() {
//        // Your implementation here.
//        Log.d("In", "########My own Method ######");
//        Log.v("MY", "Registration ID: " + regid);
//        AppConfig.appRegId = regid;
//        SavePreferences("DEVICE_TOKEN", regid);
//    }
//
//    private void SavePreferences(String key, String value) {
//        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//    private String getRegistrationId(Context context) {
//        final SharedPreferences prefs = getGCMPreferences(context);
//        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
//        if (registrationId.isEmpty()) {
//            // Log.i(TAG, "Registration not found.");
//            return "";
//        }
//        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//        int currentVersion = getAppVersion(context);
//        if (registeredVersion != currentVersion) {
//            //  Log.i(TAG, "App version changed.");
//            return "";
//        }
//        return registrationId;
//    }
//
//    /**
//     * @return Application's {@code SharedPreferences}.
//     */
//    private SharedPreferences getGCMPreferences(Context context) {
//        return getSharedPreferences(SplashActivity.class.getSimpleName(), Context.MODE_PRIVATE);
//    }
//
//    /**
//     * @return Application's version code from the {@code PackageManager}.
//     */
//    private static int getAppVersion(Context context) {
//        try {
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            return packageInfo.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            throw new RuntimeException("Could not get package name: " + e);
//        }
//    }
//
//
//}
