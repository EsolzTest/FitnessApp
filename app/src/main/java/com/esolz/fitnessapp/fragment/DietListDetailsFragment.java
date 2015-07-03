package com.esolz.fitnessapp.fragment;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.datatype.DietDetailDataType;
import com.esolz.fitnessapp.helper.ObservableScrollView;
import com.esolz.fitnessapp.helper.ScrollViewListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DietListDetailsFragment extends Fragment implements ScrollViewListener {

	LinearLayout backDiet;
	ObservableScrollView mainS;
	ScrollView child;
	DietDetailDataType object;

	View fView;

	FragmentTransaction fragmentTransaction;
	FragmentManager fragmentManager;

	TitilliumBold title;
	TitilliumRegular description;
	ImageView mealimage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)  {
		// TODO Auto-generated method stub


		fView=inflater.inflate(R.layout.frag_dietlist_details,container,false);
		title=(TitilliumBold)fView.findViewById(R.id.details_title);
		description=(TitilliumRegular)fView.findViewById(R.id.details_desc);
		mealimage=(ImageView)fView.findViewById(R.id.meal_image);

		backDiet = (LinearLayout) fView.findViewById(R.id.back_diet);

		fragmentManager = getActivity().getSupportFragmentManager();

		backDiet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fragmentTransaction = fragmentManager.beginTransaction();
				DietFragment diet_fragment = new DietFragment();
				fragmentTransaction.replace(R.id.fragment_container,
						diet_fragment);
				fragmentTransaction.commit();
			}
		});




		(new GetDietDetail()).execute();

		return fView;
	}

	class GetDietDetail extends AsyncTask<Void,Void,Void>{

		InputStream is;
		String json;


		JSONObject diet_detail;


		@Override
		protected Void doInBackground(Void... params) {


			try {


				DefaultHttpClient httClient = new DefaultHttpClient();
				// HttpClient client = HttpClientBuilder.create().build();

				HttpGet http_get = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_custom_meal_details?custom_meal_id=26&client_id=15&meal_id=12");

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
				diet_detail = new JSONObject(json);




				object = new DietDetailDataType(diet_detail.getString("meal_title"), diet_detail.getString("meal_image"), diet_detail.getString("meal_description"));


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

				title.setText("" + object.getMeal_title());
				description.setText("" + object.getMeal_description());

			} catch (Exception e) {

			}



		}
	}








	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// TODO Auto-generated method stub
		if (oldx > x) {

		} else {
			// System.out.println("Scroll value : " + y);
			if (y > 448) {
			} else {
			}
			child.scrollTo(x, y / 2);
			// header.setAlpha((float) y / 1200);
		}
	}




}
