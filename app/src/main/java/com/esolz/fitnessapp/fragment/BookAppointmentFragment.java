package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.BookAppointAdapter;
import com.esolz.fitnessapp.adapter.Trainer_Adapter;
import com.esolz.fitnessapp.customviews.HelveticaHeavy;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.Alltriner_Setter_Gette;
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

public class BookAppointmentFragment extends Fragment {

    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton,
            back;
    RelativeLayout llMessagebutton;

    //	-------------------------Varibale by Koushik 18/jun
    String url;
    LinkedList<TrainerDetails> all_feed_list = new LinkedList<TrainerDetails>();
    LinkedList<TrainerBookingDate> all_feed_list2 = new LinkedList<TrainerBookingDate>();
    public boolean loading = false;

    TitilliumSemiBold day;
    HelveticaHeavy date_1;
    TitilliumSemiBold month;
    RelativeLayout prev_calender;
    RelativeLayout next_calender;
    ViewPager triner_pageviewer;
    public LinkedList<Alltriner_Setter_Gette> all_trainer_list;
    LinearLayout vp_next;
    LinearLayout vp_prev;
    private int viewpageritemno;
    private int click_counter;
    ImageView vp_back_img;
    private int mSelectedPageIndex = 1;
    private static final int PAGE_LEFT = 0;


//---------------------------------


    ListView bookApptList;
    ArrayList<HashMap<String, String>> data;
    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        fView = inflater.inflate(R.layout.frag_book_appointment, container, false);


        back = (LinearLayout) fView.findViewById(R.id.back);
        fragmentManager = getActivity().getSupportFragmentManager();

//		----------------------------------------Koushik Srakar 18/6/2015----------------------------------------

        final Calendar c_today = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        final SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        final SimpleDateFormat sdd = new SimpleDateFormat("dd");
        final SimpleDateFormat sdm = new SimpleDateFormat("MMMM");
        String current_date = df.format(c_today.getTime());
        method_json_viewpager(current_date);


        String date_m = sdm.format(c_today.getTime());
        String dayName = sdf_.format(c_today.getTime());
        String Date1 = sdd.format(c_today.getTime());
        date_m = sdm.format(c_today.getTime());
        dayName = sdf_.format(c_today.getTime());
        Date1 = sdd.format(c_today.getTime());

        day = (TitilliumSemiBold) fView.findViewById(R.id.tv_day);
        day.setText(dayName);
        date_1 = (HelveticaHeavy) fView.findViewById(R.id.tv_number);
        date_1.setText(Date1);
        month = (TitilliumSemiBold) fView.findViewById(R.id.tv_month);
        month.setText(date_m);
        triner_pageviewer = (ViewPager) fView.findViewById(R.id.trainer_viewpager);
        vp_next = (LinearLayout) fView.findViewById(R.id.vp_next);
        vp_prev = (LinearLayout) fView.findViewById(R.id.vp_back);
//		vp_back_img=(ImageView)fView.findViewById(R.id.vp_back_img);


//		-----------------------------------------end koushik Sarkar  19/6/2015----------------------------


        bookApptList = (ListView) fView.findViewById(R.id.book_app_list);

        loadmore(0);
        data = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 10; i++) {
            HashMap<String, String> hMap = new HashMap<String, String>();
            hMap.put("value", "" + i);
            data.add(hMap);
        }

//        BookAppointAdapter bookAppAdapter = new BookAppointAdapter(getActivity(), 0, 0, data);
//        bookApptList.setAdapter(bookAppAdapter);
        prev_calender = (RelativeLayout) fView.findViewById(R.id.prev_date);
        next_calender = (RelativeLayout) fView.findViewById(R.id.next_date);
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


        /// Previeus Day-----------------------by Koushik at 18/06/15

        prev_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pre_month = "";
                String pre_day = "";
                String pre_date = "";
                c_today.add(Calendar.DATE, -1);
                pre_month = sdm.format(c_today.getTime());
                month.setText(pre_month);
                pre_day = sdd.format(c_today.getTime());
                date_1.setText(pre_day);
                pre_date = sdf_.format(c_today.getTime());
                day.setText(pre_date);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String current_date = df.format(c_today.getTime());

                method_json_viewpager(current_date);


            }
        });


        //						--------Next Day--------------- by koushik at 18/06/15

        next_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pre_month = "";
                String pre_day = "";
                String pre_date = "";
                c_today.add(Calendar.DATE, +1);
                pre_month = sdm.format(c_today.getTime());
                month.setText(pre_month);
                pre_day = sdd.format(c_today.getTime());
                date_1.setText(pre_day);
                pre_date = sdf_.format(c_today.getTime());
                day.setText(pre_date);


                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String current_date = df.format(c_today.getTime());

                method_json_viewpager(current_date);

            }
        });

        //************************************* for infinatie loop ViewPager*******************************

//		triner_pageviewer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//			@Override
//			public void onPageSelected(int position) {
//				mSelectedPageIndex = position;
//			}
//
//			@Override
//			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int state) {
//				if(state==ViewPager.SCROLL_STATE_IDLE)
//				{
//
//					// user swiped to right direction --> left page
//					if (mSelectedPageIndex == 0) {
//
//					}
//				}
//
//													  }
//												  });
//


        ////******************************** Koushik 19/6/15 for ViewPager Click************


//			vp_back_img.setVisibility(View.GONE);
        vp_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//					triner_pageviewer.get
//					triner_pageviewer.setCurrentItem(+1, true);
//					vp_back_img.setVisibility(View.VISIBLE);
                if (triner_pageviewer.getCurrentItem() == all_trainer_list.size() - 1) {
                    triner_pageviewer.setCurrentItem(0, false);

                } else
                    triner_pageviewer.setCurrentItem(triner_pageviewer.getCurrentItem() + 1, false);
            }
        });

        vp_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (triner_pageviewer.getCurrentItem() == 0) {
                    triner_pageviewer.setCurrentItem(all_trainer_list.size(), false);

                } else
                    triner_pageviewer.setCurrentItem(triner_pageviewer.getCurrentItem() - 1, false);
               // String id = all_feed_list.get(triner_pageviewer.getCurrentItem()).getTrainee_id();

            }
        });


        return fView;
    }

//public int counter_pager(int a)
//{
//	int counter_view_pager;
//	triner_pageviewer.getCurrentItem();
//}


    public void method_json_viewpager(String date_data) {

        ///////////////////////////////////////////////////////////JSON Persing//////////////////////////////////

        //	Method created by koushik at 18/6/2015


        final String data = date_data;

        new AsyncTask<Void, Void, Void>() {


            InputStream is_lv;
            String json_lv;
            JSONArray json_arr_lv;
            JSONObject list_object;


            @Override
            protected void onPreExecute() {

                super.onPreExecute();
//				fView.setVisibility(View.INVISIBLE);

            }


            @Override
            protected Void doInBackground(Void... voids) {
//				super.doInBackground();
                try {


                    DefaultHttpClient httClient = new DefaultHttpClient();

                    HttpGet http_post = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/trainer_by_date?client_id=15&date_val=" + data);

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
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {

                    list_object = new JSONObject(json_lv);

                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }


                try {
                    // Intilize Linked List is zero
                    all_trainer_list = new LinkedList<Alltriner_Setter_Gette>();
                    //taking array from object
                    json_arr_lv = list_object.getJSONArray("trainer");
                    JSONObject Json_Obj_temp;

                    //getting details of array
                    viewpageritemno = json_arr_lv.length();

                    if (json_arr_lv.length() != 0) {

                        for (int i = 0; i < json_arr_lv.length(); i++) {

                            Json_Obj_temp = json_arr_lv.getJSONObject(i);

                            //setting custom data type objects

                            Alltriner_Setter_Gette all_triner_obj = new Alltriner_Setter_Gette();
                            all_triner_obj.setPt_id(Json_Obj_temp.getString("pt_id"));
                            all_triner_obj.setPt_name(Json_Obj_temp.getString("pt_name"));
                            all_triner_obj.setPt_image(Json_Obj_temp.getString("pt_image"));
                            all_triner_obj.setWorking_address(Json_Obj_temp.getString("working_address"));
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

                Trainer_Adapter adapter = new Trainer_Adapter(getActivity(), 0, all_trainer_list);
                triner_pageviewer.setAdapter(adapter);
//				fView.setVisibility(View.VISIBLE);

            }
        }.execute();


    }


    //************************** JSON method by koushik 22-jun from triner appintmrnt*********************


    //*************************JSON method by Indranil Chaudhuri************************************


    public void loadmore(final int position) {


        final int pos = position;


        try {

            new AsyncTask<Void, Void, Void>() {

                InputStream is;
                String json, object1, object2;
                JSONArray json_arr;
                ProgressDialog dialog;
                JSONObject all_news_list_object;
                String id = "4";
                String date23 = "2015-06-30";
                String trainerid;
                String bookingd;

                @Override
                protected void onPreExecute() {
                    if (pos == 0) {
                        fView.setVisibility(View.GONE);
                        dialog = new ProgressDialog(getActivity());
                        dialog.setMessage("Loading please wait...");
                        dialog.show();
                        super.onPreExecute();

                    }
                }

                @Override
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

                        String line = null;

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
                        //String total_data=all_news_list_object.getString("total_count");
                        //taking array from objec
                        //String total_data=all_news_list_object.getString("total_count");

                        json_arr = all_news_list_object.getJSONArray("time_slots");


                        for (int i = position; i < json_arr.length(); i++) {
                            JSONObject Json_Obj_temp;

                            //setting custom data type objects

                            Json_Obj_temp = json_arr.getJSONObject(i);

                            //SearchResults all_feed_obj = new SearchResults(Json_Obj_temp.getString("slot_start"),Json_Obj_temp.getString("slot_end"),Json_Obj_temp.getString("counter"),Json_Obj_temp.getString("status"),Json_Obj_temp.getString("trainee_id"),Json_Obj_temp.getString("booking_date"));
                            //all_feed_list.add(all_feed_obj);


                            String slots = Json_Obj_temp.getString("slot_start");
                            String slotend = Json_Obj_temp.getString("slot_end");
                            String counter = Json_Obj_temp.getString("counter");
                            String bookingid = Json_Obj_temp.getString("booking_id");
                            String status = Json_Obj_temp.getString("status");
                            TrainerDetails ob = new TrainerDetails(slots, slotend, counter, bookingid, status);

                            all_feed_list.add(ob);
                        }

                        trainerid = all_news_list_object.getString("trainer_id");
                        // Log.e("JSON Parser", "Error parsing data " + trainerid);

                        bookingd = all_news_list_object.getString("booking_date");

                        TrainerBookingDate ob1 = new TrainerBookingDate(trainerid, bookingd);
                        all_feed_list2.add(ob1);


                    } catch (Exception e) {

                        e.printStackTrace();


                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    dialog.dismiss();
                    // Toast.makeText(getActivity(),""+json_arr+""+trainerid+""+bookingd,Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(),""+trainerid,Toast.LENGTH_LONG).show();
                    // Toast.makeText(getActivity(), "" +
                    //     json_arr.length(), Toast.LENGTH_SHORT).show();
                    bookApptList.setAdapter(new BookAppointAdapter(getActivity(), 0, all_feed_list));

                    new BookAppointAdapter(getActivity(), 0, all_feed_list).notifyDataSetChanged();

                    fView.setVisibility(View.VISIBLE);
                    loading = false;
                }


            }.execute();


        } catch (Exception e) {
            loading = false;


        }

    }
}
