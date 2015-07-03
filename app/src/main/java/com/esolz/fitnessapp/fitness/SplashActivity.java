package com.esolz.fitnessapp.fitness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.LoginDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

public class SplashActivity extends Activity {

	SharedPreferences loginPreferences;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_splash);

		cd = new ConnectionDetector(SplashActivity.this);

		loginPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

		System.out.println("!! " + loginPreferences.getString("Remember", ""));

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				try {

					if (cd.isConnectingToInternet()) {

						if (loginPreferences.getString("Remember", "").equals(
								"remember")) {

							AppConfig.loginDataType = new LoginDataType(loginPreferences.getString("UserId", ""),
									loginPreferences.getString("Username", ""), loginPreferences.getString("Password", ""));
							Intent intent = new Intent(SplashActivity.this,
									LandScreenActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(SplashActivity.this,
									LoginActivity.class);
							startActivity(intent);
							finish();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"No Internet Connection", Toast.LENGTH_LONG)
								.show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, 2000);
	}

}
