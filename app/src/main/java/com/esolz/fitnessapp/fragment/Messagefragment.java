package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.MessageRoomAdapter;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.datatype.MsgDataType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Messagefragment extends Fragment {

	LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton;
	RelativeLayout llMessagebutton;
	ProgressBar pbar;
	ListView messageRoomList;
	MessageRoomAdapter msgAdapter;
	String url;
	LinkedList<MsgDataType> data;

	TitilliumBold username;
	String user_name;

	View fView;




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fView = inflater.inflate(R.layout.frag_message, container, false);
		pbar = (ProgressBar) fView.findViewById(R.id.progbar);
		pbar.setVisibility(View.GONE);

		data = new LinkedList<MsgDataType>();
		messageRoomList = (ListView) fView
				.findViewById(R.id.listviewmessageroom);
		messageRoomList.setDivider(null);


		//llmsg=(LinearLayout)fView.findViewById(R.id.llMsg);


		llCalenderButton = (LinearLayout) getActivity().findViewById(
				R.id.calenderbutton);
		llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(
				R.id.blockappoinmentbutton);
		llProgressButton = (LinearLayout) getActivity().findViewById(
				R.id.progressbutton);
		llMessagebutton = (RelativeLayout) getActivity().findViewById(
				R.id.messagebutton);
		llCalenderButton.setClickable(true);
		llBlockAppoinmentButton.setClickable(true);
		llProgressButton.setClickable(true);
		llMessagebutton.setClickable(false);


//
//		messageRoomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				TitilliumBold username=(TitilliumBold)view.findViewById(R.id.user_name);
//				String txt=username.getText().toString();
//
//				user_name=txt;
//				Intent intent=new Intent(getActivity(),ChatDetailsFragment.class);
//				intent.putExtra("username",txt);
//				intent.putExtra("id",id);
//				startActivity(intent);
//
//
//
//	}
//		});


		new Getmessage().execute();

		return fView;

}





			public class Getmessage extends  AsyncTask<Void, Void, Void>{


				InputStream is;
				String json;
				JSONArray json_arr;

				JSONObject all_news_list_object;

				@Override
				protected void onPreExecute() {

					pbar.setVisibility(View.VISIBLE);

					super.onPreExecute();
				}


				@Override
				protected Void doInBackground(Void... params) {

					try {


						DefaultHttpClient httClient = new DefaultHttpClient();


						HttpGet http_get= new HttpGet("http://esolz.co.in/lab6/ptplanner/dashboard/get_sender_list_app?logged_in_user=15");

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

					} catch (Exception e) {
						Log.e("Buffer Error", "Error converting result " + e.toString());
					}

					try {

						all_news_list_object = new JSONObject(json);

					} catch (JSONException e) {
						Log.e("JSON Parser", "Error parsing data " + e.toString());
					}
					try {
						data = new LinkedList<MsgDataType>();
						//String total_data = all_news_list_object.getString("all_user");
						 json_arr = all_news_list_object.getJSONArray("all_user");


						if (json_arr.length() != 0) {

							for (int i = 0; i < json_arr.length(); i++) {
								JSONObject Json_Obj_temp;
								Json_Obj_temp = json_arr.getJSONObject(i);
								MsgDataType obj=new MsgDataType(Json_Obj_temp.getString("user_id"),Json_Obj_temp.getString("last_send_time"),Json_Obj_temp.getString("user_name"),Json_Obj_temp.getString("user_image"),Json_Obj_temp.getString("last_message"));
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
					pbar.setVisibility(View.GONE);
					msgAdapter = new MessageRoomAdapter(getActivity(), 0, data);
					messageRoomList.setAdapter(msgAdapter);


				}


			}


		}





//		MessageRoomAdapter msgAdapter = new MessageRoomAdapter(getActivity(), 0, data);
//		messageRoomList.setAdapter(msgAdapter);



