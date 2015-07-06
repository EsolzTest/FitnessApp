package com.esolz.fitnessapp.fitness;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.Progress_ViewPgr_Adapter;
import com.esolz.fitnessapp.datatype.CalenderFragmentDatatype;
import com.esolz.fitnessapp.datatype.CalenderFrgmentAllexercises;
import com.esolz.fitnessapp.datatype.CalenderPageExSets;
import com.esolz.fitnessapp.fragment.BookAppointmentFragment;
import com.esolz.fitnessapp.fragment.CalenderFragment;
import com.esolz.fitnessapp.fragment.Messagefragment;
import com.esolz.fitnessapp.fragment.ProfileFragment;
import com.esolz.fitnessapp.fragment.ProgressFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class LandScreenActivity extends FragmentActivity {

	FrameLayout fragContainer;

	 LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton,llBottomBar;
	 RelativeLayout llMessagebutton,rllandcontainer;

	TextView txtMSGCount, txtCal, txtApnt, txtPrg, txtMsg;
	ImageView imgCal, imgApnt, imgPrg, imgMsg;

	FragmentTransaction fragmentTransaction;
	FragmentManager fragmentManager;
	String getIntentValue = "", getIntentValueMSG = "";
	//18th june - code changed by Bodhidipta Bhattacharjee
	//layout id has been changed by id
	View VPage;
	CalenderFragmentDatatype calDtype;
//end
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_landipage);

		fragContainer = (FrameLayout) findViewById(R.id.fragment_container);
		llBottomBar=(LinearLayout)findViewById(R.id.landpage_layout_bottom);
		rllandcontainer=(RelativeLayout)findViewById(R.id.landpage_holder_content);


		llCalenderButton = (LinearLayout) findViewById(R.id.calenderbutton);
		llBlockAppoinmentButton = (LinearLayout) findViewById(R.id.blockappoinmentbutton);
		llProgressButton = (LinearLayout) findViewById(R.id.progressbutton);
		llMessagebutton = (RelativeLayout) findViewById(R.id.messagebutton);

		txtMSGCount = (TextView) findViewById(R.id.txt_msgcount);
		txtMSGCount.setVisibility(View.GONE);

		txtCal = (TextView) findViewById(R.id.txt_cal);
		txtApnt = (TextView) findViewById(R.id.txt_apnt);
		txtPrg = (TextView) findViewById(R.id.txt_prg);
		txtMsg = (TextView) findViewById(R.id.txt_msg);

		imgCal = (ImageView) findViewById(R.id.img_cal);
		imgApnt = (ImageView) findViewById(R.id.img_apnt);
		imgPrg = (ImageView) findViewById(R.id.img_prg);
		imgMsg = (ImageView) findViewById(R.id.img_msg);

		fragmentManager = getSupportFragmentManager();

		try {
			getIntentValue = getIntent().getExtras().getString("MSG");

			if (getIntentValue.equals("ChatDetailsFragment")) {

				txtCal.setTextColor(Color.parseColor("#A4A4A5"));
				txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
				txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
				txtMsg.setTextColor(Color.parseColor("#22A7F0"));
				imgCal.setBackgroundResource(R.drawable.cal);
				imgApnt.setBackgroundResource(R.drawable.apnt);
				imgPrg.setBackgroundResource(R.drawable.prg);
				imgMsg.setBackgroundResource(R.drawable.msgclick);

				fragmentTransaction = fragmentManager.beginTransaction();
				ProfileFragment prl_fragment = new ProfileFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						prl_fragment);
				fragmentTransaction.commit();

			} else if (getIntentValue.equals("MSGFragment")) {

				txtCal.setTextColor(Color.parseColor("#A4A4A5"));
				txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
				txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
				txtMsg.setTextColor(Color.parseColor("#22A7F0"));
				imgCal.setBackgroundResource(R.drawable.cal);
				imgApnt.setBackgroundResource(R.drawable.apnt);
				imgPrg.setBackgroundResource(R.drawable.prg);
				imgMsg.setBackgroundResource(R.drawable.msgclick);

				fragmentTransaction = fragmentManager.beginTransaction();
				Messagefragment msg_fragment = new Messagefragment();
				fragmentTransaction.replace(R.id.fragment_container,
						msg_fragment);
				fragmentTransaction.commit();
			}
		} catch (Exception e) {

			// ------------------- Calendar Fragment
			fragmentTransaction = fragmentManager.beginTransaction();
			CalenderFragment cal_fragment = new CalenderFragment();
			fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
			fragmentTransaction.commit();
			txtCal.setTextColor(Color.parseColor("#22A7F0"));
			txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
			txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
			txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
			imgCal.setBackgroundResource(R.drawable.calclick);
			imgApnt.setBackgroundResource(R.drawable.apnt);
			imgPrg.setBackgroundResource(R.drawable.prg);
			imgMsg.setBackgroundResource(R.drawable.msg);
		}

		llCalenderButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ------------------- Calendar Fragment
				txtCal.setTextColor(Color.parseColor("#22A7F0"));
				txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
				txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
				txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
				imgCal.setBackgroundResource(R.drawable.calclick);
				imgApnt.setBackgroundResource(R.drawable.apnt);
				imgPrg.setBackgroundResource(R.drawable.prg);
				imgMsg.setBackgroundResource(R.drawable.msg);

				fragmentTransaction = fragmentManager.beginTransaction();
				CalenderFragment cal_fragment = new CalenderFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						cal_fragment);
				fragmentTransaction.commit();

			}
		});
		llBlockAppoinmentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ------------------- Book Appointment Fragment
				txtCal.setTextColor(Color.parseColor("#A4A4A5"));
				txtApnt.setTextColor(Color.parseColor("#22A7F0"));
				txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
				txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
				imgCal.setBackgroundResource(R.drawable.cal);
				imgApnt.setBackgroundResource(R.drawable.apntclick2);
				imgPrg.setBackgroundResource(R.drawable.prg);
				imgMsg.setBackgroundResource(R.drawable.msg);

				fragmentTransaction = fragmentManager.beginTransaction();
				BookAppointmentFragment bookapnt_fragment = new BookAppointmentFragment();

				fragmentTransaction.replace(R.id.fragment_container,
						bookapnt_fragment);
				fragmentTransaction.commit();

			}
		});
		llProgressButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ------------------- Progress Fragment
				txtCal.setTextColor(Color.parseColor("#A4A4A5"));
				txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
				txtPrg.setTextColor(Color.parseColor("#22A7F0"));
				txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
				imgCal.setBackgroundResource(R.drawable.cal);
				imgApnt.setBackgroundResource(R.drawable.apnt);
				imgPrg.setBackgroundResource(R.drawable.prgclick2);
				imgMsg.setBackgroundResource(R.drawable.msg);

				fragmentTransaction = fragmentManager.beginTransaction();
				ProgressFragment prg_fragment = new ProgressFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						prg_fragment);
				fragmentTransaction.commit();

			}
		});
		llMessagebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ------------------- Message Fragment
				txtCal.setTextColor(Color.parseColor("#A4A4A5"));
				txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
				txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
				txtMsg.setTextColor(Color.parseColor("#22A7F0"));
				imgCal.setBackgroundResource(R.drawable.cal);
				imgApnt.setBackgroundResource(R.drawable.apnt);
				imgPrg.setBackgroundResource(R.drawable.prg);
				imgMsg.setBackgroundResource(R.drawable.msgclick);

				fragmentTransaction = fragmentManager.beginTransaction();
				Messagefragment msg_fragment = new Messagefragment();

				fragmentTransaction.replace(R.id.fragment_container,
						msg_fragment);
				fragmentTransaction.commit();

			}
		});
	}


	public class FetchData extends AsyncTask<Void, Void, Void> {


		InputStream is;
		String resultResponce;
		String url = "http://esolz.co.in/lab6/ptplanner/app_control/get_all_events_for_date/?client_id=15&date_val=2015-06-01";
		// pre exexcute

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			//  CalenderFragmentDatatype(jObject.getInt("total_meal"),jObject.getInt("total_appointment"), jObject.getInt("total_training_exercises"), jObject.getInt("total_training_exercise_finished"), jObject.getInt("total_training_programs"), jObject.getInt("total_training_programs_finished"), jObject.getString("diary_text"),all_exercises);
			txtCal.setTextColor(Color.parseColor("#22A7F0"));
			txtApnt.setTextColor(Color.parseColor("#A4A4A5"));
			txtPrg.setTextColor(Color.parseColor("#A4A4A5"));
			txtMsg.setTextColor(Color.parseColor("#A4A4A5"));
			imgCal.setBackgroundResource(R.drawable.calclick);
			imgApnt.setBackgroundResource(R.drawable.apnt);
			imgPrg.setBackgroundResource(R.drawable.prg);
			imgMsg.setBackgroundResource(R.drawable.msg);


			// Toast.makeText(getApplicationContext(), "" + calDtype.getDiary_text(), Toast.LENGTH_SHORT).show();

//			fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.replace(R.id.fragment_container,
//					CalenderFragment.getInstance(calDtype));
//			fragmentTransaction.commit();

			//Toast.makeText(getApplicationContext(), "" + calDtype.getTotal_meal(), Toast.LENGTH_LONG).show();

		}


		@Override
		protected Void doInBackground(Void... params) {
			try {

				HttpClient httClient = new DefaultHttpClient();


				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httClient.execute(httpget);

				HttpEntity httpEntity = response.getEntity();

				is = httpEntity.getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				resultResponce = sb.toString();
				JSONObject jObject = new JSONObject(resultResponce);

				//Context con;
				LinkedList<CalenderFrgmentAllexercises> all_exercises = new LinkedList<CalenderFrgmentAllexercises>();
				JSONArray jArray = jObject.getJSONArray("all_exercises");
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject innerTempObj = jArray.getJSONObject(i);
//**********************Handeling Ex Sets
					JSONArray innerInnerJarray = innerTempObj.getJSONArray("exercise_sets");
					LinkedList<CalenderPageExSets> exercise_sets = new LinkedList<CalenderPageExSets>();
					for (int j = 0; j < innerInnerJarray.length(); j++) {
						JSONObject sets = innerInnerJarray.getJSONObject(j);
						CalenderPageExSets CPES = new CalenderPageExSets(sets.getString("reps"), sets.getString("kg"));
						exercise_sets.add(CPES);
					}
//****************************
					final CalenderFrgmentAllexercises tempCal = new CalenderFrgmentAllexercises(innerTempObj.getString("user_program_id"), innerTempObj.getString("exercise_id"), innerTempObj.getString("exercise_title"), innerTempObj.getString("instruction"), exercise_sets);
					all_exercises.add(tempCal);
				}
				calDtype = new CalenderFragmentDatatype(jObject.getInt("total_meal"), jObject.getInt("total_appointment"), jObject.getInt("total_training_exercises"), jObject.getInt("total_training_exercise_finished"), jObject.getInt("total_training_programs"), jObject.getInt("total_training_programs_finished"), jObject.getString("diary_text"), all_exercises);

//***************
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("INSIDE", e.toString());
			}
			return null;
		}


	}










	//18th june - code changed by Bodhidipta Bhattacharjee
	//layout id has been changed by id

		public void Show_FullScreen_ViewPager(Context con,ArrayList<String> obj){
			if(obj.isEmpty()){
				Toast.makeText(con,"No Images Load First...",Toast.LENGTH_SHORT).show();
			}else{
				fragContainer.setVisibility(View.GONE);
				llBottomBar.setVisibility(View.GONE);
				VPage=getLayoutInflater().inflate(R.layout.frag_progress_images_dialog, null, false);
				rllandcontainer.addView(VPage);
				ViewPager pager=(ViewPager)VPage.findViewById(R.id.pager_prog_img);
				RelativeLayout close=(RelativeLayout)VPage.findViewById(R.id.close_pager);

				PagerAdapter mPagerAdapter =new Progress_ViewPgr_Adapter(con,obj);

				pager.setAdapter(mPagerAdapter);

				close.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {


						fragContainer.setVisibility(View.VISIBLE);
						llBottomBar.setVisibility(View.VISIBLE);
						VPage.setVisibility(View.GONE);
					}
				});
				//rllandcontainer.requestLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		}

		//end code here


		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			if (fragContainer.getVisibility()==View.GONE && llBottomBar.getVisibility()==View.GONE&&VPage.getVisibility()==View.VISIBLE) {
				fragContainer.setVisibility(View.VISIBLE);
				llBottomBar.setVisibility(View.VISIBLE);
				VPage.setVisibility(View.GONE);
			}

		}
	}