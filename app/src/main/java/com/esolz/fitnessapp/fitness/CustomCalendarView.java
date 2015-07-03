package com.esolz.fitnessapp.fitness;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.CalanderFragmentDairyDataType;
import com.esolz.fitnessapp.helper.ConnectionDetector;
import com.esolz.fitnessapp.helper.ReturnCalendarDetails;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class CustomCalendarView extends Activity {

	TextView[] textViewArray;
	LinearLayout[] llArray;
	LinearLayout btnPre, btnNext, txt_hide, coverLayout;
	TextView txt_currentdatemonth, txt_currentyear;
	boolean isToday = false;
	ScrollView scrollView1maincon;

	Calendar calendar;
	int currentYear, currentMonth, currentDay, currentDate, firstDayPosition,
			currentMonthLength, previousDayPosition, nextDayPosition;

	String[] positionPre = {}, positionNext = {};

	HashMap<String, String> calDateEvent;
	ArrayList<HashMap<String, String>> calEventList;
	// *******************************************

	CalanderFragmentDairyDataType obj;
	ImageView image;
	TextView text_name;
	TextView status;
	TextView detail;
	ConnectionDetector connect;


	//*********************************************
	float balencing_coefficient_ = -1000.0f;

	float CENTRE_Y_OF_THE_SCREEN = 0.0f;
	float OPEN_CLOSE_BOUNDERY_Y = 0.0f;

	int c = 0;

	// *********************************
	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.frag_diary);


		// ------------------ Layout Element-----------------

		coverLayout = (LinearLayout) findViewById(R.id.coverlayout);

		getLayouts();

		scrollView1maincon = (ScrollView) findViewById(R.id.scrollView1maincon);
		//*************************************
		image=(ImageView)findViewById(R.id.imageView3);
		text_name=(TextView)findViewById(R.id.textView1);
		status=(TextView)findViewById(R.id.textView123);
		detail=(TextView)findViewById(R.id.textView123scroll);
		btnPre = (LinearLayout) findViewById(R.id.btn_pre);
		btnNext = (LinearLayout) findViewById(R.id.btn_next);
		txt_currentdatemonth = (TextView) findViewById(R.id.txt_currentdatemonth);
		txt_currentyear = (TextView) findViewById(R.id.txt_currentyear);
		txt_hide = (LinearLayout) findViewById(R.id.txt_hide);

		// ----------------------------------------- END-----

		// ---------------------- Event Calculation ----------

		calDateEvent = new HashMap<String, String>();
		calDateEvent.put("date", "28");
		calDateEvent.put("event", "Event");
		calDateEvent.put("date", "11");
		calDateEvent.put("event", "Event1");

		calEventList = new ArrayList<HashMap<String, String>>();
		calEventList.add(calDateEvent);

		// ----------------------------------------- END-----

		calendar = Calendar.getInstance(Locale.getDefault());

		currentDate = calendar.get(Calendar.DATE);
		currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		currentMonth = (calendar.get(Calendar.MONTH) + 1);
		currentYear = calendar.get(Calendar.YEAR);
		firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

		// ------------------- Previous Month
		btnPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				getLayouts();

				Calendar pre = (Calendar) calendar.clone();

				currentMonth--;

				if (currentMonth == 0) {
					currentMonth = 12;
					currentYear--;
				}
				pre.set(Calendar.MONTH, currentMonth);
				pre.set(Calendar.YEAR, currentYear);
				pre.set(Calendar.DATE, 1);

				positionPre = pre.getTime().toString().split(" ");
				previousDayPosition = ReturnCalendarDetails
						.getPosition(positionPre[0]);

				getCalendar(
						ReturnCalendarDetails.getCurrentMonth(positionPre[1]),
						ReturnCalendarDetails.getPosition(positionPre[0]),
						Integer.parseInt(positionPre[5]));

				System.out.println("!!" + pre.getTime());
			}
		});
		// ------------------- Previous Month
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				getLayouts();

				Calendar next = (Calendar) calendar.clone();

				if (currentMonth > 11) {
					currentMonth = 1;
					currentYear++;
				} else {
					currentMonth++;
				}

				next.set(Calendar.MONTH, currentMonth);
				next.set(Calendar.YEAR, currentYear);
				next.set(Calendar.DATE, 1);

				positionNext = next.getTime().toString().split(" ");
				getCalendar(
						ReturnCalendarDetails.getCurrentMonth(positionNext[1]),
						ReturnCalendarDetails.getPosition(positionNext[0]),
						Integer.parseInt(positionNext[5]));
			}
		});
		// -- change
		txt_hide.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(final View view) {
				// TODO Auto-generated method stub
				coverLayout.clearAnimation();
				TranslateAnimation tAnim = new TranslateAnimation(0.0f, 0.0f,
						0.0f, (-coverLayout.getY()));
				tAnim.setDuration(500);
				tAnim.setFillAfter(true);
				tAnim.setInterpolator(new AnticipateOvershootInterpolator(1.0f,
						1.0f));
				coverLayout.startAnimation(tAnim);
				tAnim.setAnimationListener(new AnimationListener() {

					@SuppressLint("NewApi")
					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						coverLayout.clearAnimation();
						coverLayout.setY(0.0f);
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}

				});
			}
		});

		llArray[0].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[1].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[2].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[3].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[4].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[5].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[6].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[7].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[8].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[9].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[10].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[11].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[12].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[13].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[14].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[15].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[16].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[17].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[18].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[19].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[20].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[21].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[22].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[23].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[24].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[25].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[26].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[27].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[28].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[29].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[30].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[31].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[32].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[33].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[34].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[35].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[36].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[37].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[38].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[39].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[40].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		llArray[41].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		
//		for (c = 0; c < llArray.length; c++) {
//			llArray[c].setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(CustomCalendarView.this, "" + v,
//							Toast.LENGTH_LONG).show();
//				}
//			});
//		}

		btnPre.performClick();
		// **************************Coder: S.Pakira
		// ************Dt: 01-05-2015
		coverLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}

		});

		LinearLayout scrl_lay = (LinearLayout) findViewById(R.id.scrl_lay);

		scrl_lay.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				arg0.getParent().requestDisallowInterceptTouchEvent(true);
				return false;

			}
		});

		coverLayout.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(final View view, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (balencing_coefficient_ == -1000.0f) {
						balencing_coefficient_ = event.getY();
					}
					if (CENTRE_Y_OF_THE_SCREEN == 0.0f) {
						CENTRE_Y_OF_THE_SCREEN = view.getHeight() / 2.0f;
					}
					if (OPEN_CLOSE_BOUNDERY_Y == 0.0f) {
						OPEN_CLOSE_BOUNDERY_Y = CENTRE_Y_OF_THE_SCREEN / 2.0f;
					}
					// *******************************
					float act_position_ = event.getRawY()
							- balencing_coefficient_;
					if (act_position_ < CENTRE_Y_OF_THE_SCREEN
							&& act_position_ >= 0.0f) {
						view.setY(act_position_);
						System.out
								.println("actual position : " + act_position_);
					} else if (act_position_ < (-1 * (OPEN_CLOSE_BOUNDERY_Y / 3.0f))) {
						// ****************push back again
						view.setY(act_position_ / 3.0f);
					} else if (act_position_ < (-1 * (OPEN_CLOSE_BOUNDERY_Y / 1.50f))) {
						// ****************push back again
						view.setY(act_position_ / 5.0f);
					} else if (act_position_ < (-1 * OPEN_CLOSE_BOUNDERY_Y)) {
						// ****************push back again
						view.setY(act_position_ / 7.0f);
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					balencing_coefficient_ = -1000.0f;
					// *****
					System.out.println("final actual position : " + view.getY());
					if (view.getY() < OPEN_CLOSE_BOUNDERY_Y) {
						view.clearAnimation();
						TranslateAnimation tAnim = new TranslateAnimation(0.0f,
								0.0f, 0.0f, (-view.getY()));
						tAnim.setDuration(300);
						tAnim.setFillAfter(true);
						tAnim.setInterpolator(new AnticipateOvershootInterpolator(
								1.0f, 1.0f));
						view.startAnimation(tAnim);
						tAnim.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								view.clearAnimation();
								view.setY(0.0f);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub

							}

						});
					} else {
						view.clearAnimation();
						TranslateAnimation tAnim = new TranslateAnimation(0.0f,
								0.0f, 0.0f, (CENTRE_Y_OF_THE_SCREEN - view
								.getY()));
						tAnim.setDuration(300);
						tAnim.setFillAfter(true);
						tAnim.setInterpolator(new AnticipateOvershootInterpolator(
								2.0f, 1.0f));
						view.startAnimation(tAnim);
						tAnim.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationEnd(Animation arg0) {
								// TODO Auto-generated method stub
								view.clearAnimation();
								view.setY(CENTRE_Y_OF_THE_SCREEN);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onAnimationStart(Animation arg0) {
								// TODO Auto-generated method stub

							}

						});
					}

				}

				return true;
			}

		});

		connect=new ConnectionDetector(getApplication());
		if (connect.isConnectingToInternet()) {
			Toast.makeText(getApplication(),
					"connectimg...", Toast.LENGTH_LONG).show();
			(new GetDetails()).execute();
		}
		else{
			Toast.makeText(getApplication(),
					"No internet conection...", Toast.LENGTH_LONG).show();
		}

	}


	class GetDetails extends AsyncTask<Void,Void,Void>{


		InputStream is;
		String json;


		JSONObject user_detail;


		@Override
		protected Void doInBackground(Void... params) {

			try {


				DefaultHttpClient httClient = new DefaultHttpClient();
				// HttpClient client = HttpClientBuilder.create().build();

				HttpGet http_get = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_date_respective_diary?logged_in_user=15&date_val=2015-05-26");

				HttpResponse response = httClient.execute(http_get);

				HttpEntity httpEntity = response.getEntity();

				is = httpEntity.getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "\n");
				}

				is.close();

				json = sb.toString();
				user_detail = new JSONObject(json);




				obj=new CalanderFragmentDairyDataType(user_detail.getString("client_id"),
						user_detail.getString("client_name"),
						user_detail.getString("client_image"),
						user_detail.getString("client_email"),
						user_detail.getString("client_about"),
						user_detail.getString("diary_id"),
						user_detail.getString("diary_heading"),
						user_detail.getString("dairy_text")
						);

			} catch (Exception e) {
				e.printStackTrace();
			}





			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void aVoid) {

			super.onPostExecute(aVoid);

			try {

				text_name.setText("" + obj.getClient_name());
				status.setText("" + obj.getClient_email());
				detail.setText("" + obj.getClient_about());
				Picasso.with(getApplication()).load(obj.getClient_image()).transform(new Trns()).resize(400, 400).centerInside().placeholder(R.drawable.abc_dialog_material_background_dark) // optional
						.error(R.drawable.abc_dialog_material_background_light)  .into(image);
			} catch (Exception e) {

			}

		}
	}

	// *******************************
	public void getCalendar(int currentMonth, int indexOfDayOne, int curyear) {

		int i = 0, j = 0, k = 0, day = 1;
		int currentMonthLength = ReturnCalendarDetails.getMonthLenth(
				currentMonth, curyear);

		Calendar current = (Calendar) calendar.clone();

		current.set(Calendar.MONTH, currentMonth - 1);
		current.set(Calendar.YEAR, curyear);
		current.set(Calendar.DATE, currentDate);

		SimpleDateFormat sformat = new SimpleDateFormat("dd MMMM");

		txt_currentdatemonth.setText("" + sformat.format(current.getTime()));
		txt_currentyear.setText("" + curyear);

		int today = current.get(Calendar.DATE);

		// --- Set Current Month
		for (i = indexOfDayOne; i < (currentMonthLength + indexOfDayOne); i++) {
			textViewArray[i].setText("" + day);
			textViewArray[i].setTextColor(Color.BLACK);
			day++;

			if (currentMonthLength > today) {

				if (textViewArray[(today + indexOfDayOne) - 1].getText()
						.toString().equals("" + today)) {
					llArray[(today + indexOfDayOne) - 1]
							.setBackgroundResource(R.drawable.selected_day);
				}
			} else {

				if (textViewArray[(today + indexOfDayOne) - 1].getText()
						.toString().equals("" + today)) {
					llArray[(today + indexOfDayOne) - 2]
							.setBackgroundResource(R.drawable.selected_day);
				}
			}
		}
		day = 1;

		// --- Set Next Month
		for (j = i; j < textViewArray.length; j++) {
			textViewArray[j].setText("" + day);
			textViewArray[j].setTextColor(Color.GRAY);
			day++;
		}
		day = 1;

		if (currentMonth == 1) {
			currentMonth = 12;
			curyear--;
		} else {
			currentMonth--;
		}
		int tempcurrentMonthLength = ReturnCalendarDetails.getMonthLenth(
				currentMonth, curyear);

		if (indexOfDayOne != 0) {
			// --- Set Previous Month
			for (k = indexOfDayOne - 1; k >= 0; k--) {
				textViewArray[k].setText("" + tempcurrentMonthLength);
				textViewArray[k].setTextColor(Color.GRAY);
				tempcurrentMonthLength--;
			}
		}
	}

	public void getLayouts() {
		llArray = new LinearLayout[42];
		llArray[0] = (LinearLayout) findViewById(R.id.ll7);
		llArray[1] = (LinearLayout) findViewById(R.id.ll8);
		llArray[2] = (LinearLayout) findViewById(R.id.ll9);
		llArray[3] = (LinearLayout) findViewById(R.id.ll10);
		llArray[4] = (LinearLayout) findViewById(R.id.ll11);
		llArray[5] = (LinearLayout) findViewById(R.id.ll12);
		llArray[6] = (LinearLayout) findViewById(R.id.ll13);
		llArray[7] = (LinearLayout) findViewById(R.id.ll14);
		llArray[8] = (LinearLayout) findViewById(R.id.ll15);
		llArray[9] = (LinearLayout) findViewById(R.id.ll16);
		llArray[10] = (LinearLayout) findViewById(R.id.ll17);
		llArray[11] = (LinearLayout) findViewById(R.id.ll18);
		llArray[12] = (LinearLayout) findViewById(R.id.ll19);
		llArray[13] = (LinearLayout) findViewById(R.id.ll20);
		llArray[14] = (LinearLayout) findViewById(R.id.ll21);
		llArray[15] = (LinearLayout) findViewById(R.id.ll22);
		llArray[16] = (LinearLayout) findViewById(R.id.ll23);
		llArray[17] = (LinearLayout) findViewById(R.id.ll24);
		llArray[18] = (LinearLayout) findViewById(R.id.ll25);
		llArray[19] = (LinearLayout) findViewById(R.id.ll26);
		llArray[20] = (LinearLayout) findViewById(R.id.ll27);
		llArray[21] = (LinearLayout) findViewById(R.id.ll28);
		llArray[22] = (LinearLayout) findViewById(R.id.ll29);
		llArray[23] = (LinearLayout) findViewById(R.id.ll30);
		llArray[24] = (LinearLayout) findViewById(R.id.ll31);
		llArray[25] = (LinearLayout) findViewById(R.id.ll32);
		llArray[26] = (LinearLayout) findViewById(R.id.ll33);
		llArray[27] = (LinearLayout) findViewById(R.id.ll34);
		llArray[28] = (LinearLayout) findViewById(R.id.ll35);
		llArray[29] = (LinearLayout) findViewById(R.id.ll36);
		llArray[30] = (LinearLayout) findViewById(R.id.ll37);
		llArray[31] = (LinearLayout) findViewById(R.id.ll38);
		llArray[32] = (LinearLayout) findViewById(R.id.ll39);
		llArray[33] = (LinearLayout) findViewById(R.id.ll40);
		llArray[34] = (LinearLayout) findViewById(R.id.ll41);
		llArray[35] = (LinearLayout) findViewById(R.id.ll42);
		llArray[36] = (LinearLayout) findViewById(R.id.ll43);
		llArray[37] = (LinearLayout) findViewById(R.id.ll44);
		llArray[38] = (LinearLayout) findViewById(R.id.ll45);
		llArray[39] = (LinearLayout) findViewById(R.id.ll46);
		llArray[40] = (LinearLayout) findViewById(R.id.ll47);
		llArray[41] = (LinearLayout) findViewById(R.id.ll48);

		for (int a = 0; a < llArray.length; a++) {
			llArray[a].setBackgroundColor(Color.TRANSPARENT);
		}

		textViewArray = new TextView[42];
		textViewArray[0] = (TextView) findViewById(R.id.txt7);
		textViewArray[1] = (TextView) findViewById(R.id.txt8);
		textViewArray[2] = (TextView) findViewById(R.id.txt9);
		textViewArray[3] = (TextView) findViewById(R.id.txt10);
		textViewArray[4] = (TextView) findViewById(R.id.txt11);
		textViewArray[5] = (TextView) findViewById(R.id.txt12);
		textViewArray[6] = (TextView) findViewById(R.id.txt13);
		textViewArray[7] = (TextView) findViewById(R.id.txt14);
		textViewArray[8] = (TextView) findViewById(R.id.txt15);
		textViewArray[9] = (TextView) findViewById(R.id.txt16);
		textViewArray[10] = (TextView) findViewById(R.id.txt17);
		textViewArray[11] = (TextView) findViewById(R.id.txt18);
		textViewArray[12] = (TextView) findViewById(R.id.txt19);
		textViewArray[13] = (TextView) findViewById(R.id.txt20);
		textViewArray[14] = (TextView) findViewById(R.id.txt21);
		textViewArray[15] = (TextView) findViewById(R.id.txt22);
		textViewArray[16] = (TextView) findViewById(R.id.txt23);
		textViewArray[17] = (TextView) findViewById(R.id.txt24);
		textViewArray[18] = (TextView) findViewById(R.id.txt25);
		textViewArray[19] = (TextView) findViewById(R.id.txt26);
		textViewArray[20] = (TextView) findViewById(R.id.txt27);
		textViewArray[21] = (TextView) findViewById(R.id.txt28);
		textViewArray[22] = (TextView) findViewById(R.id.txt29);
		textViewArray[23] = (TextView) findViewById(R.id.txt30);
		textViewArray[24] = (TextView) findViewById(R.id.txt31);
		textViewArray[25] = (TextView) findViewById(R.id.txt32);
		textViewArray[26] = (TextView) findViewById(R.id.txt33);
		textViewArray[27] = (TextView) findViewById(R.id.txt34);
		textViewArray[28] = (TextView) findViewById(R.id.txt35);
		textViewArray[29] = (TextView) findViewById(R.id.txt36);
		textViewArray[30] = (TextView) findViewById(R.id.txt37);
		textViewArray[31] = (TextView) findViewById(R.id.txt38);
		textViewArray[32] = (TextView) findViewById(R.id.txt39);
		textViewArray[33] = (TextView) findViewById(R.id.txt40);
		textViewArray[34] = (TextView) findViewById(R.id.txt41);
		textViewArray[35] = (TextView) findViewById(R.id.txt42);
		textViewArray[36] = (TextView) findViewById(R.id.txt43);
		textViewArray[37] = (TextView) findViewById(R.id.txt44);
		textViewArray[38] = (TextView) findViewById(R.id.txt45);
		textViewArray[39] = (TextView) findViewById(R.id.txt46);
		textViewArray[40] = (TextView) findViewById(R.id.txt47);
		textViewArray[41] = (TextView) findViewById(R.id.txt48);
	}

	// ********************************************Animation maker
	public Animation fromAtoB(float fromX, float fromY, float toX, float toY,
			int speed) {
		Animation fromAtoB = new TranslateAnimation(Animation.ABSOLUTE, fromX,
				Animation.ABSOLUTE, toX, Animation.ABSOLUTE, fromY,
				Animation.ABSOLUTE, toY);
		fromAtoB.setFillAfter(true);
		fromAtoB.setDuration(speed);
		// fromAtoB.setInterpolator(new AccelerateDecelerateInterpolator());//
		// new
		// AnticipateOvershootInterpolator(1.0f));
		return fromAtoB;
	}

}