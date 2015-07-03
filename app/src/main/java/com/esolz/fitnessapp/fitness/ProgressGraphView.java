package com.esolz.fitnessapp.fitness;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.fragment.MeasurementDialogFragment;

public class ProgressGraphView extends FragmentActivity {

	WebView webView;
	int num1, num2, num3, num4, num5, num6, num7, num8, num9, num10;
	LinearLayout back;
	LinearLayout add_measurement;
	Dialog dialog;
	TextView parentWeight, parentDeadline;

	FragmentTransaction fragmentTransaction;
	FragmentManager fragmentManager;

	SharedPreferences measurementSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_graph_view);
		back = (LinearLayout) findViewById(R.id.back);
		add_measurement = (LinearLayout) findViewById(R.id.add_measurement);
		fragmentManager = getSupportFragmentManager();

		// -- Sharedpreference
		measurementSharedPreferences = getSharedPreferences("Measument",
				Context.MODE_PRIVATE);
		// -- END

		parentDeadline = (TextView) findViewById(R.id.deadline);
		parentWeight = (TextView) findViewById(R.id.weight);

		if (measurementSharedPreferences.getString("DATE", "").equals("")) {
			parentDeadline.setText("");
		} else {
			parentDeadline.setText(""
					+ measurementSharedPreferences.getString("DATE", ""));
		}

		if (measurementSharedPreferences.getString("WEIGHT", "").equals("")) {
			parentWeight.setText("");
		} else {
			parentWeight.setText(""
					+ measurementSharedPreferences.getString("WEIGHT", ""));
		}
		// -- END

		num1 = 5;
		num2 = 4;
		num3 = 6;
		num4 = 6;
		num5 = 5;
		num6 = 3;
		num7 = 4;
		num8 = 5;
		num9 = 6;
		num10 = 7;

		webView = (WebView) findViewById(R.id.webView);
		webView.addJavascriptInterface(new WebAppInterface(), "Android");
		webView.setScrollbarFadingEnabled(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new HelloWebViewClient());
		webView.loadUrl("file:///android_asset/chart.html");

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				 * FragmentManager fm =
				 * getActivity().getSupportFragmentManager();
				 * fm.popBackStack("dF",
				 * FragmentManager.POP_BACK_STACK_INCLUSIVE);
				 */

				finish();
			}
		});

		/*
		 * dialog = new Dialog(ProgressGraphView.this);
		 * dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
		 * WindowManager.LayoutParams.MATCH_PARENT);
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * dialog.setContentView(R.layout.measurement_dialog);
		 */

		add_measurement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// dialog.show();

				MeasurementDialogFragment MDF = new MeasurementDialogFragment();
				MDF.show(getSupportFragmentManager(), "Dialog Fragment");
			}
		});

	}

	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	public class WebAppInterface {

		@JavascriptInterface
		public int getNum1() {
			return num1;
		}

		@JavascriptInterface
		public int getNum2() {
			return num2;
		}

		@JavascriptInterface
		public int getNum3() {
			return num3;
		}

		@JavascriptInterface
		public int getNum4() {
			return num4;
		}

		@JavascriptInterface
		public int getNum5() {
			return num5;
		}

		@JavascriptInterface
		public int getNum6() {
			return num6;
		}

		@JavascriptInterface
		public int getNum7() {
			return num7;
		}

		@JavascriptInterface
		public int getNum8() {
			return num8;
		}

		@JavascriptInterface
		public int getNum9() {
			return num9;
		}

		@JavascriptInterface
		public int getNum10() {
			return num10;
		}

	}
}