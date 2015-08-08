package com.esolz.fitnessapp.fitness;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumLight;
import com.esolz.fitnessapp.datatype.LoginDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

public class LoginActivity extends Activity {

    ConnectionDetector cd;

    EditText etEmail, etPass;
    Button btnLogin;
    LinearLayout llRememberMe, llForgotPass;
    ImageView imgChkbox;
    ProgressBar pBar;
    TitilliumLight txtErrorMSG;
    String responseMSG = "", exception = "", urlResponse = "", message = "", siteUserId = "";
    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPass = (EditText) findViewById(R.id.et_pass);

        etEmail.setText("kamalika.ait2012@gmail.com");
        etPass.setText("123456");

        btnLogin = (Button) findViewById(R.id.btn_login);

        llRememberMe = (LinearLayout) findViewById(R.id.ll_remember);
        llForgotPass = (LinearLayout) findViewById(R.id.ll_forgot);

        imgChkbox = (ImageView) findViewById(R.id.img_chkbox);

        pBar = (ProgressBar) findViewById(R.id.prgbar);
        pBar.setVisibility(View.GONE);
        txtErrorMSG = (TitilliumLight) findViewById(R.id.txt_error);
        txtErrorMSG.setVisibility(View.GONE);

        cd = new ConnectionDetector(LoginActivity.this);

        loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (cd.isConnectingToInternet()) {
                    if (!etEmail.getText().toString().trim().equals("")) {
                        if (isEmailValid(etEmail.getText().toString().trim())) {
                            if (!etPass.getText().toString().equals("")) {
                                userLogin(
                                        etEmail.getText().toString().trim(),
                                        etPass.getText().toString().trim(),
                                        AppConfig.strRemember
                                );
                            } else {
                                etPass.setError("Password is blank");
                                etPass.requestFocus();
                            }
                        } else {
                            etEmail.requestFocus();
                            etEmail.setError("Please enter valid email id.");
                        }
                    } else {
                        etEmail.setError("Email is blank");
                        etEmail.requestFocus();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        llRememberMe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (AppConfig.isRemember) {
                    AppConfig.isRemember = false;
                    imgChkbox.setBackgroundResource(R.drawable.check);
                    AppConfig.strRemember = "Y";
                } else {
                    AppConfig.isRemember = true;
                    imgChkbox.setBackgroundResource(R.drawable.uncheck);
                    AppConfig.strRemember = "N";
                }
            }
        });

        llForgotPass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void userLogin(final String email, final String password, final String remember) {

        AsyncTask<Void, Void, Void> login = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    responseMSG = "";
                    urlResponse = "";
                    message = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/login/verify_app_login?email="
                            + email + "&password=" + password
                            + "&remember_me=" + remember
                            + "&device_token=" + AppConfig.appRegId);
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
                        siteUserId = jOBJ.getString("site_user_id");
                        responseMSG = jOBJ.getString("response");
                        message = jOBJ.getString("message");

                        AppConfig.loginDatatype = new LoginDataType(
                                siteUserId,
                                email,
                                password);
                    } catch (Exception ex) {
                        Log.i("Site : ", ex.toString());

                        responseMSG = jOBJ.getString("response");
                        message = jOBJ.getString("message");
                    }

                } catch (Exception e) {
                    exception = e.toString();
                }
                Log.d("LOGIN", "http://esolz.co.in/lab6/ptplanner/login/verify_app_login?email="
                        + email + "&password=" + password
                        + "&remember_me=" + remember);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);

                if (exception.equals("")) {
                    if (responseMSG.equals("success")) {
                        if (AppConfig.strRemember.equals("Y")) {

                            Editor editor = loginPreferences.edit();
                            editor.putString("Remember", "remember");
                            editor.putString("UserId", siteUserId);
                            editor.putString("Username", etEmail.getText()
                                    .toString().trim());
                            editor.putString("Password", etPass.getText()
                                    .toString().trim());
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, LandScreenActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            Editor editor = loginPreferences.edit();
                            editor.clear();
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, LandScreenActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    } else if (responseMSG.equals("Error")) {
                        txtErrorMSG.setVisibility(View.VISIBLE);
                        txtErrorMSG.setText(message);
                    } else {
                        Toast.makeText(LoginActivity.this, "Error....",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.i(" SerVer Error : ", exception);
                    Toast.makeText(LoginActivity.this,
                            "Server not responding....", Toast.LENGTH_LONG)
                            .show();
                }
            }

        };
        login.execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
