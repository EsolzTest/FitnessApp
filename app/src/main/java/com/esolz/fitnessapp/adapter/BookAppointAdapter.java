package com.esolz.fitnessapp.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
;
import java.util.LinkedList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.TrainerBookingDate;
import com.esolz.fitnessapp.datatype.TrainerDetails;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookAppointAdapter extends ArrayAdapter<TrainerDetails> {

	Context con;

	//ArrayList<HashMap<String, String>> data;
	LayoutInflater lf;
	ViewHolder holder;
	RelativeLayout listitemContainer;
	LinkedList<TrainerDetails> all_feed_list;
	LinkedList<TrainerBookingDate>all_feed_list2;
	//FragmentTransaction fragmentTransaction;
	//FragmentManager fragmentManager;
	TrainerDetails obj;
	//int position;
	boolean[] state;
	public BookAppointAdapter(Context context, int resource,LinkedList<TrainerDetails> objects)
	{
		super(context, resource,objects);
		con = context;
		all_feed_list=objects;
		lf=(LayoutInflater)con.getSystemService(con.LAYOUT_INFLATER_SERVICE);
		state=new boolean[all_feed_list.size()];
		for(int i=0;i<all_feed_list.size();i++)
		{
			String status=all_feed_list.get(i).getStatus();
			if(status.equals("NB"))
			{
				state[i]=true;

			}else
			{
				state[i]=false;//for Ex
			}

		}


	}


	@Override
	public int getCount()

	{
		return all_feed_list.size();
	}

	public View getView(  final  int position, View convertView, ViewGroup parent) {


		View row = convertView;
	final int	  pos=position;
		holder = new ViewHolder();
		//final int pos=position;
		//LayoutInflater lf=(LayoutInflater)con.getSystemService(con.LAYOUT_INFLATER_SERVICE);

		if (row == null)

		{

			LayoutInflater lf = (LayoutInflater) con.getSystemService(con.LAYOUT_INFLATER_SERVICE);

			row = lf.inflate(R.layout.book_app_list, parent, false);

			holder.timing1 = (TextView) row.findViewById(R.id.timing);
			holder.apt1 = (TextView) row.findViewById(R.id.apt);
			holder.status23 = (Button) row.findViewById(R.id.status);

			row.setTag(holder);

		}
		else {

			holder = (ViewHolder) row.getTag();


		}


		obj = all_feed_list.get(pos);
		holder.timing1.setText(obj.getSlotstart() + "-" + obj.getSlotend());
		holder.apt1.setText(obj.getCounter());
		holder.status23.setText(obj.getStatus());
		String a = obj.getStatus();
		if (state[pos] == true) {

			holder.status23.setText("Book");



			holder.status23.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					try {
						//new GetData().execute();
						new AsyncTask<Void, Void, Void>() {
							//String except = "";
							InputStream is;
							String json;
							JSONObject all_news_list_object;
							ProgressDialog dialog;
							JSONArray json_arr;
							String id = "4";
							String date23 = "2015-06-30";
							String slots;
							String slotend;
							String counter;
							String bookingid;
							String status;
							String trainerid;
							String bookingd;

							protected void onPreExecute() {


								dialog = new ProgressDialog(getContext());
								dialog.setMessage("Loading please wait...");
								dialog.show();
								super.onPreExecute();


							}

							protected Void doInBackground(Void... params) {
								//loading = true;

								try {
									DefaultHttpClient httClient = new DefaultHttpClient();
									HttpGet http_post = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/trainer_booking_details?trainer_id=" + id + "&date_val=" + date23);
									HttpResponse response = httClient.execute(http_post);
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

									String line;

									while ((line = reader.readLine()) != null) {

										sb.append(line + "\n");
									}

									is.close();

									json = sb.toString();
									Log.e("@@", "@@" + json);
								} catch (Exception e) {
									Log.e("Buffer Error", "Error converting result " + e.toString());
								}


								try {


									all_news_list_object = new JSONObject(json);

								} catch (JSONException e) {
									Log.e("JSON Parser", "Error parsing data " + e.toString());
								}

								try {

									json_arr = all_news_list_object.getJSONArray("time_slots");
									//ArrayList<TrainerDetails> arrSearchResult = new ArrayList<TrainerDetails>();
									for (int i = 0; i < json_arr.length(); i++) {
										JSONObject Json_Obj_temp;

										//setting custom data type objects

										Json_Obj_temp = json_arr.getJSONObject(i);

										TrainerDetails ob = new TrainerDetails(slots, slotend, counter, bookingid, status);
										slots = Json_Obj_temp.getString("slot_start");
										slotend = Json_Obj_temp.getString("slot_end");
										counter = Json_Obj_temp.getString("counter");
										bookingid = Json_Obj_temp.getString("booking_id");
										status = Json_Obj_temp.getString("status");


										all_feed_list.add(ob);
										//Toast.makeText(getContext(), "" + slots + "" + slotend + "" + counter + "" + bookingid + "" + status, Toast.LENGTH_LONG).show();
									}
									//trainerid = all_news_list_object.getString("trainer_id");
									//bookingd = all_news_list_object.getString("booking_date");

									//TrainerBookingDate ob1 = new TrainerBookingDate(trainerid, bookingd);
									//all_feed_list2.add(ob1);


								} catch (Exception e) {
									//except = e.toString();

									e.printStackTrace();

								}
								return null;


							}

							protected void onPostExecute(Void aVoid) {
								dialog.dismiss();

								//holder.status23.setBackgroundColor(Color.WHITE);
								Toast.makeText(getContext(), "" + slots + "" + slotend + "" + counter + "" + bookingid + "" + status, Toast.LENGTH_SHORT).show();
								//holder.status23.setTextColor(Color.parseColor("#22A7F0"));


								//holder.status23.setClickable(false);
								holder.status23.setText("Booked");
								//holder.status23.setClickable(false);
								state[pos] = false;
								notifyDataSetChanged();

							}


						}.execute();
					} catch (Exception e) {

					}
				}

			});

		}

		else if (a.equals("Ex")) {
			holder.status23.setVisibility(View.GONE);


		}

		else  {

			holder.status23.setText("Booked");
			holder.status23.setClickable(false);
		}



		return row;
	}





	protected class ViewHolder
	{


		TextView timing1;
		TextView apt1;
		Button status23;

	}




}



