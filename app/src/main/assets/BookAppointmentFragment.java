import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.esolz.adapter.BookAppointAdapter;
import com.esolz.adapter.Trainer_Adapter;
import com.esolz.customviews.HelveticaHeavy;
import com.esolz.customviews.TitilliumSemiBold;
import com.esolz.datatype.Alltriner_Setter_Gette;
import com.esolz.datatype.SearchResults;
import com.esolz.datatype.SearchResults2;
import com.esolz.dialog.ShowCalendarPopUp;
import com.esolz.fitness.R;
import com.esolz.helper.AppConfig;

@SuppressLint("SimpleDateFormat")
public class BookAppointmentFragment extends Fragment {

	LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton,
			back, showCalender;
	RelativeLayout llMessagebutton;
	ListView bookApptList;
	ArrayList<HashMap<String, String>> data;
	View fView;

	FragmentTransaction fragmentTransaction;
	FragmentManager fragmentManager;

	ShowCalendarPopUp showCalPopup;
	// -- Calendar Instance
	Calendar calendar;
	int currentYear, currentMonth, currentDay, currentDate, firstDayPosition,
			currentMonthLength, previousDayPosition, nextDayPosition;

	// -- UI Element
	RelativeLayout nextDate, prevDate;
	HelveticaHeavy tvNumber;
	TitilliumSemiBold tvMonth, tvDay;

	SimpleDateFormat sdfDate, sdfDay, sdfNo, sdfMonth;
	String dateCurrent;

	public LinkedList<Alltriner_Setter_Gette> all_trainer_list;
	private int viewpageritemno;
	ViewPager triner_pageviewer;
	LinearLayout vp_next;
	LinearLayout vp_prev;
	private int click_counter;
	ImageView vp_back_img;
	Date date;

	LinkedList<SearchResults> all_feed_list = new LinkedList<SearchResults>();
	LinkedList<SearchResults2> all_feed_list2 = new LinkedList<SearchResults2>();
	Alltriner_Setter_Gette all_triner_obj;

	public boolean loading = false;

	ProgressBar pbarList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		fView = inflater.inflate(R.layout.frag_book_appointment, container,
				false);

		fragmentManager = getActivity().getSupportFragmentManager();
		calendar = Calendar.getInstance(Locale.getDefault());

		back = (LinearLayout) fView.findViewById(R.id.back);
		showCalender = (LinearLayout) fView.findViewById(R.id.show_cal);

		// ---------- Start
		prevDate = (RelativeLayout) fView.findViewById(R.id.prev_date);
		nextDate = (RelativeLayout) fView.findViewById(R.id.next_date);
		tvNumber = (HelveticaHeavy) fView.findViewById(R.id.tv_number);
		tvMonth = (TitilliumSemiBold) fView.findViewById(R.id.tv_month);
		tvDay = (TitilliumSemiBold) fView.findViewById(R.id.tv_day);
		triner_pageviewer = (ViewPager) fView
				.findViewById(R.id.trainer_viewpager);
		vp_next = (LinearLayout) fView.findViewById(R.id.vp_next);
		vp_prev = (LinearLayout) fView.findViewById(R.id.vp_back);

		bookApptList = (ListView) fView.findViewById(R.id.book_app_list);
		pbarList = (ProgressBar) fView.findViewById(R.id.pbar);
		// ---------- End
		// -- Calendar
		currentDate = calendar.get(Calendar.DATE);
		currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		currentMonth = (calendar.get(Calendar.MONTH) + 1);
		currentYear = calendar.get(Calendar.YEAR);
		firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);
		// -- End

		// -- Show Calendar
		showCalPopup = new ShowCalendarPopUp(getActivity(), "appointment");

		showCalender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

				showCalPopup.getLayouts();

				showCalPopup.getCalendar(currentMonth, currentDay, currentYear);

				showCalPopup.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0,
						-20);
			}
		});

		// -- Set Current Date --

		sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		sdfDay = new SimpleDateFormat("EEEE");
		sdfNo = new SimpleDateFormat("dd");
		sdfMonth = new SimpleDateFormat("MMMM");

		try {
			date = sdfDate.parse(getArguments().getString("DateChange"));

			Calendar cal = Calendar.getInstance();
			cal.setTime(sdfDate.parse(getArguments().getString("DateChange")));
			calendar = cal;
			// asynctask
			method_json_viewpager(getArguments().getString("DateChange"));

			tvDay.setText("" + sdfDay.format(date));
			tvNumber.setText("" + getArguments().getString("DAY"));
			tvMonth.setText("" + getArguments().getString("MONTH"));
		} catch (Exception e) {

			dateCurrent = sdfDate.format(calendar.getTime());
			// asynctask
			method_json_viewpager(dateCurrent);

			tvDay.setText("" + sdfDay.format(calendar.getTime()));
			tvNumber.setText("" + sdfNo.format(calendar.getTime()));
			tvMonth.setText("" + sdfMonth.format(calendar.getTime()));
		}

		// -- End

		prevDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				calendar.add(Calendar.DATE, -1);
				tvDay.setText("" + sdfDay.format(calendar.getTime()));
				tvNumber.setText("" + sdfNo.format(calendar.getTime()));
				tvMonth.setText("" + sdfMonth.format(calendar.getTime()));

				dateCurrent = sdfDate.format(calendar.getTime());
				// asynctask
				method_json_viewpager(dateCurrent);
			}
		});
		nextDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				calendar.add(Calendar.DATE, +1);
				tvDay.setText("" + sdfDay.format(calendar.getTime()));
				tvNumber.setText("" + sdfNo.format(calendar.getTime()));
				tvMonth.setText("" + sdfMonth.format(calendar.getTime()));

				dateCurrent = sdfDate.format(calendar.getTime());
				// asynctask
				method_json_viewpager(dateCurrent);
			}
		});

		vp_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (triner_pageviewer.getCurrentItem() == all_trainer_list
						.size() - 1) {
					triner_pageviewer.setCurrentItem(0, false);

				} else {
					triner_pageviewer.setCurrentItem(
							triner_pageviewer.getCurrentItem() + 1, false);
				}
				// loadmore(all_triner_obj.getPt_id(), dateCurrent);
			}
		});

		vp_prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (triner_pageviewer.getCurrentItem() == 0) {
					triner_pageviewer.setCurrentItem(all_trainer_list.size(),
							false);
				} else {
					triner_pageviewer.setCurrentItem(
							triner_pageviewer.getCurrentItem() - 1, false);
				}
				// loadmore(all_triner_obj.getPt_id(), dateCurrent);
			}
		});

		triner_pageviewer.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				loadmore(all_trainer_list.get(position).getPt_id(), dateCurrent);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				// loadmore(all_triner_obj.getPt_id(), dateCurrent);
			}
		});

		llCalenderButton = (LinearLayout) getActivity().findViewById(
				R.id.calenderbutton);
		llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(
				R.id.blockappoinmentbutton);
		llProgressButton = (LinearLayout) getActivity().findViewById(
				R.id.progressbutton);
		llMessagebutton = (RelativeLayout) getActivity().findViewById(
				R.id.messagebutton);
		llCalenderButton.setClickable(true);
		llBlockAppoinmentButton.setClickable(false);
		llProgressButton.setClickable(true);
		llMessagebutton.setClickable(true);

		return fView;
	}

	public void method_json_viewpager(String date_data) {

		final String data = date_data;

		new AsyncTask<Void, Void, Void>() {

			InputStream is_lv;
			String json_lv;
			JSONArray json_arr_lv;
			JSONObject list_object;

			@Override
			protected void onPreExecute() {

				super.onPreExecute();

			}

			@Override
			protected Void doInBackground(Void... voids) {
				// super.doInBackground();
				try {

					DefaultHttpClient httClient = new DefaultHttpClient();

					HttpGet http_post = new HttpGet(
							"http://esolz.co.in/lab6/ptplanner/app_control/trainer_by_date?client_id="
									+ AppConfig.loginDatatype.getSite_user_id()
									+ "&date_val=" + data);

					HttpResponse response = httClient.execute(http_post);

					HttpEntity httpEntity = response.getEntity();

					is_lv = httpEntity.getContent();

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is_lv));

					StringBuilder sb = new StringBuilder();

					String line = null;

					while ((line = reader.readLine()) != null) {

						sb.append(line + "\n");
					}

					is_lv.close();

					json_lv = sb.toString();

				} catch (Exception e) {
					Log.e("Buffer Error",
							"Error converting result " + e.toString());
				}
				try {

					list_object = new JSONObject(json_lv);

				} catch (JSONException e) {
					Log.e("JSON Parser", "Error parsing data " + e.toString());
				}

				try {
					all_trainer_list = new LinkedList<Alltriner_Setter_Gette>();
					json_arr_lv = list_object.getJSONArray("trainer");
					JSONObject Json_Obj_temp;
					viewpageritemno = json_arr_lv.length();

					if (json_arr_lv.length() != 0) {

						for (int i = 0; i < json_arr_lv.length(); i++) {

							Json_Obj_temp = json_arr_lv.getJSONObject(i);

							all_triner_obj = new Alltriner_Setter_Gette();
							all_triner_obj.setPt_id(Json_Obj_temp
									.getString("pt_id"));
							all_triner_obj.setPt_name(Json_Obj_temp
									.getString("pt_name"));
							all_triner_obj.setPt_image(Json_Obj_temp
									.getString("pt_image"));
							all_triner_obj.setWorking_address(Json_Obj_temp
									.getString("working_address"));
							all_trainer_list.addLast(all_triner_obj);
						}

					}
				} catch (JSONException e) {

					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);

				Trainer_Adapter adapter = new Trainer_Adapter(getActivity(), 0,
						all_trainer_list);
				triner_pageviewer.setAdapter(adapter);

				loadmore(all_triner_obj.getPt_id(), data);

			}
		}.execute();

	}

	public void loadmore(final String trainer_id, final String date_val) {

		try {

			new AsyncTask<Void, Void, Void>() {

				InputStream is;
				String json, object1, object2;
				JSONArray json_arr;
				ProgressDialog dialog;
				JSONObject all_news_list_object;
				String traineeid;
				String bookingd;

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					pbarList.setVisibility(View.VISIBLE);
					bookApptList.setVisibility(View.GONE);
				}

				@Override
				protected Void doInBackground(Void... params) {
					// loading = true;

					try {

						DefaultHttpClient httClient = new DefaultHttpClient();

						HttpGet http_post = new HttpGet(
								"http://esolz.co.in/lab6/ptplanner/app_control/trainer_booking_details?trainer_id="
										+ trainer_id + "&date_val=" + date_val);

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

						String line = null;

						while ((line = reader.readLine()) != null) {

							sb.append(line + "\n");
						}

						is.close();

						json = sb.toString();
						Log.e("@@", "@@" + json);
					} catch (Exception e) {
						Log.e("Buffer Error",
								"Error converting result " + e.toString());
					}

					try {

						all_news_list_object = new JSONObject(json);

					} catch (JSONException e) {
						Log.e("JSON Parser",
								"Error parsing data " + e.toString());
					}

					try {

						json_arr = all_news_list_object
								.getJSONArray("time_slots");

						for (int i = 0; i < json_arr.length(); i++) {
							JSONObject Json_Obj_temp;

							Json_Obj_temp = json_arr.getJSONObject(i);

							String slots = Json_Obj_temp
									.getString("slot_start");
							String slotend = Json_Obj_temp
									.getString("slot_end");
							String counter = Json_Obj_temp.getString("counter");
							String status = Json_Obj_temp.getString("status");

							SearchResults ob = new SearchResults(slots,
									slotend, counter, status);

							all_feed_list.add(ob);
						}

						traineeid = all_news_list_object
								.getString("trainee_id");
						bookingd = all_news_list_object
								.getString("booking_date");
						SearchResults2 ob1 = new SearchResults2(traineeid,
								bookingd);
						all_feed_list2.add(ob1);
					} catch (Exception e) {

						e.printStackTrace();

					}
					return null;
				}

				@Override
				protected void onPostExecute(Void aVoid) {
					super.onPostExecute(aVoid);

					pbarList.setVisibility(View.GONE);
					bookApptList.setVisibility(View.VISIBLE);

					bookApptList.setAdapter(new BookAppointAdapter(
							getActivity(), 0, all_feed_list));

					new BookAppointAdapter(getActivity(), 0, all_feed_list)
							.notifyDataSetChanged();

					// fView.setVisibility(View.VISIBLE);
					// loading = false;
				}

			}.execute();

		} catch (Exception e) {
			loading = false;

		}

	}
}
