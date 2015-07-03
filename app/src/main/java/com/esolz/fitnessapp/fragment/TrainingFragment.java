package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.TrainingAdapter;
import com.esolz.fitnessapp.datatype.TrainingDataType;
import com.esolz.fitnessapp.dialog.ShowMorePopUp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrainingFragment extends Fragment {

	ShowMorePopUp showmorePopup;
	ListView traingList;
	LinearLayout back,more;
	LinkedList<TrainingDataType> data;
	TrainingAdapter trainingAdapter;
	String url;
	View fView;
	FragmentTransaction fragmentTransaction;
	FragmentManager fragmentManager;
	ProgressBar prog_training;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		fView = inflater.inflate(R.layout.frag_training, container, false);
		final ProgressBar prog_training=(ProgressBar)fView.findViewById(R.id.prog_training);
		prog_training.setVisibility(View.GONE);
		traingList = (ListView) fView.findViewById(R.id.list_trn);
		traingList.setDivider(null);
		back = (LinearLayout) fView.findViewById(R.id.back);
		more=(LinearLayout)fView.findViewById(R.id.more);
		data = new LinkedList<TrainingDataType>();
		// ---------------------- YouTube Player
		fragmentManager = getActivity().getSupportFragmentManager();
		/*
		 * Thread background = new Thread() { public void run() {
		 */

		try {
			/* sleep(2 * 1000); */
//			fragmentTransaction = fragmentManager.beginTransaction();
//			YoutubePlayerFragment fragYoutube = YoutubePlayerFragment
//					.newInstance("x6JOSWmAofQ");
//			fragmentTransaction.replace(R.id.ll_topbody, fragYoutube);
//			fragmentTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * } }; background.start();
		 */
		// ---------------------- YouTube Player END

		//data = new ArrayList<HashMap<String, String>>();

		//for (int i = 0; i < 50; i++) {
			//HashMap<String, String> hMap = new HashMap<String, String>();
			//hMap.put("value", "" + i);
			//data.add(hMap);
		//}

		//TrainingAdapter trainingAdapter = new TrainingAdapter(getActivity(), 0,
			//	0, data);
		//traingList.setAdapter(trainingAdapter);

		url="http://esolz.co.in/lab6/ptplanner/app_control/get_particular_exercise_details?user_program_id=5&client_id=15&exercise_id=2091";

		try {
			new AsyncTask<Void, Void, Void>() {


				InputStream is;
				String json;
				JSONArray json_arr;

				JSONObject all_news_list_object;

				@Override
				protected void onPreExecute() {

					prog_training.setVisibility(View.VISIBLE);

					super.onPreExecute();
				}


				@Override
				protected Void doInBackground(Void... params) {

					try {


						DefaultHttpClient httClient = new DefaultHttpClient();


						HttpGet http_get= new HttpGet(url);

						HttpResponse response = httClient.execute(http_get);

						HttpEntity httpEntity = response.getEntity();

						is = httpEntity.getContent();


					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {

						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is));

						StringBuilder sb = new StringBuilder();

						String line = null;

						while ((line = reader.readLine()) != null) {

							sb.append(line + "\n");
						}

						is.close();

						json = sb.toString();

					} catch (Exception e) {
						Log.e("Buffer Error", "Error converting result " + e.toString());
					}

					try {

						all_news_list_object = new JSONObject(json);

					} catch (JSONException e) {
						Log.e("JSON Parser", "Error parsing data " + e.toString());
					}
					try {
						data = new LinkedList<TrainingDataType>();
						//String total_data = all_news_list_object.getString("all_user");
						json_arr = all_news_list_object.getJSONArray("exercise_sets");


						if (json_arr.length() != 0) {

							for (int i = 0; i < json_arr.length(); i++) {
								JSONObject Json_Obj_temp;
								Json_Obj_temp = json_arr.getJSONObject(i);

								TrainingDataType obj=new TrainingDataType(Json_Obj_temp.getString("reps"),
										Json_Obj_temp.getString("kg"),
										Json_Obj_temp.getString("exercise_id"),
										Json_Obj_temp.getString("exercise_title"),
										Json_Obj_temp.getString("exercise_description"),
										Json_Obj_temp.getString("instruction"));
								data.add(obj);

							}
						}
					}catch (Exception e){

						e.printStackTrace();

					}


					return null;
				}



				@Override
				protected void onPostExecute(Void aVoid) {

					super.onPostExecute(aVoid);
					prog_training.setVisibility(View.GONE);
					trainingAdapter = new TrainingAdapter(getActivity(),0,data);
					traingList.setAdapter(trainingAdapter);

				}


			}.execute();


		} catch (Exception e) {

			e.printStackTrace();

		}




		//more.setOnClickListener(new OnClickListener() {

			//@Override
			//public void onClick(View v) {
				// TODO Auto-generated method stub
				//fragmentTransaction = fragmentManager.beginTransaction();
			//	MoreTrainingFragment more_fragment = new MoreTrainingFragment();
				//fragmentTransaction.replace(R.id.more_container,
				//		more_fragment);
				//fragmentTransaction.commit();
			//}
		//});


		showmorePopup = new ShowMorePopUp(getActivity());

		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub


				showmorePopup.getmoreLayouts();



				showmorePopup.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0,
						-20);
			}
		});



		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				fragmentTransaction = fragmentManager.beginTransaction();
				CalenderFragment cal_fragment = new CalenderFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						cal_fragment);
				fragmentTransaction.commit();
			}
		});

		return fView;
	}

}
