package com.esolz.fitnessapp.fitness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumLight;
import com.esolz.fitnessapp.customviews.TitilliumLightEdit;
import com.esolz.fitnessapp.datatype.LoginDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ltp on 06/08/15.
 */
public class ForgotPasswordActivity extends Activity {

    LinearLayout llBack;
    TitilliumLightEdit etEmail;
    RelativeLayout rlDone;
    ProgressBar pBar;
    TitilliumLight txtError;

    ConnectionDetector cd;

    String exception, responseMSG, urlResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_forgotpassword);

        cd = new ConnectionDetector(ForgotPasswordActivity.this);

        llBack = (LinearLayout) findViewById(R.id.back);
        etEmail = (TitilliumLightEdit) findViewById(R.id.et_email);
        rlDone = (RelativeLayout) findViewById(R.id.rl_done);
        txtError = (TitilliumLight) findViewById(R.id.txt_error);
        pBar = (ProgressBar) findViewById(R.id.prgbar);
        pBar.setVisibility(View.GONE);
        txtError.setVisibility(View.GONE);

        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (!etEmail.getText().toString().trim().equals("")) {
                        if (isEmailValid(etEmail.getText().toString().trim())) {
                            passwordForgot(etEmail.getText().toString().trim());
                        } else {
                            etEmail.requestFocus();
                            etEmail.setError("Please enter valid email id.");
                        }
                    } else {
                        etEmail.setError("Email is blank");
                        etEmail.requestFocus();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void passwordForgot(final String email) {

        AsyncTask<Void, Void, Void> forgot = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
                txtError.setVisibility(View.GONE);
                rlDone.setClickable(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    responseMSG = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/forgot_password_client?email=" + email);
                    HttpResponse response;
                    response = httpclient.execute(httpget);
                    HttpEntity httpentity = response.getEntity();
                    InputStream is = httpentity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    urlResponse = sb.toString();
                    JSONObject jOBJ = new JSONObject(urlResponse);

                    try {
                        responseMSG = jOBJ.getString("response");
                    } catch (Exception ex) {
                        Log.i("Site : ", ex.toString());
                    }

                } catch (Exception e) {
                    exception = e.toString();
                }
                Log.d("LOGIN", "http://esolz.co.in/lab6/ptplanner/app_control/forgot_password_client?email=" + email);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);
                rlDone.setClickable(true);
                if (exception.equals("")) {
                    if (responseMSG.equals("success")) {
                        Toast.makeText(ForgotPasswordActivity.this, "A Password Reset Email Has Been Sent To Your Trainer", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (responseMSG.equals("failed")) {
                        txtError.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Error....", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.i(" SerVer Error : ", exception);
                    Toast.makeText(ForgotPasswordActivity.this, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        forgot.execute();

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
