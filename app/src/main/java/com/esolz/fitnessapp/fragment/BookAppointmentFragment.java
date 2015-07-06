package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.AllTrainerAdapter;
import com.esolz.fitnessapp.customviews.HelveticaHeavy;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.AltrainerDataType;
import com.esolz.fitnessapp.dialog.ShowCalendarPopUp;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ReturnCalendarDetails;

@SuppressLint("SimpleDateFormat")
public class BookAppointmentFragment extends Fragment {

    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton,
            back, showCalender;
    RelativeLayout llMessagebutton;
    ListView bookApptList;
    View fView;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    ShowCalendarPopUp showCalPopup;
    // -- Calendar Instance
    Calendar calendar;
    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition,
            currentMonthLength, previousDayPosition, nextDayPosition;
    String[] positionPre = {};
    SimpleDateFormat sdfDate, sdfDay, sdfNo, sdfMonth;
    String dateCurrent;

    // -- UI Element
    RelativeLayout nextDate, prevDate;
    HelveticaHeavy tvNumber;
    TitilliumSemiBold tvMonth, tvDay;

    ArrayList<AltrainerDataType> altrainerDataTypeArrayList;
    AltrainerDataType altrainerDataType;

    public LinkedList<AltrainerDataType> all_trainer_list;
    ViewPager trinerPageviewer;
    LinearLayout vp_next;
    LinearLayout vp_prev;
    Date date;
    AltrainerDataType all_triner_obj;
    ProgressBar pbarList;

    public int viewpageritemno, viewPagerItemNo;
    String exception = "", urlResponse = "";
    ProgressBar viewpagerPbar;

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
        trinerPageviewer = (ViewPager) fView
                .findViewById(R.id.trainer_viewpager);
        vp_next = (LinearLayout) fView.findViewById(R.id.vp_next);
        vp_prev = (LinearLayout) fView.findViewById(R.id.vp_back);
        vp_next.setVisibility(View.GONE);
        vp_prev.setVisibility(View.GONE);
        viewpagerPbar = (ProgressBar) fView.findViewById(R.id.viewpager_pbar);
        viewpagerPbar.setVisibility(View.GONE);

        bookApptList = (ListView) fView.findViewById(R.id.book_app_list);
        pbarList = (ProgressBar) fView.findViewById(R.id.pbar);
        pbarList.setVisibility(View.GONE);
        // ---------- End
        // -- Calendar
        currentDate = calendar.get(Calendar.DATE);
        currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        currentMonth = (calendar.get(Calendar.MONTH));
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

                Calendar pre = (Calendar) calendar.clone();
                pre.set(Calendar.MONTH, currentMonth);
                pre.set(Calendar.YEAR, currentYear);
                pre.set(Calendar.DATE, 1);

                positionPre = pre.getTime().toString().split(" ");
                previousDayPosition = ReturnCalendarDetails
                        .getPosition(positionPre[0]);
                showCalPopup.getCalendar(ReturnCalendarDetails.getCurrentMonth(positionPre[1]),
                        ReturnCalendarDetails.getPosition(positionPre[0]),
                        Integer.parseInt(positionPre[5]));
                showCalPopup.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0,
                        -20);


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

            getAllTrainer(getArguments().getString("DateChange"));

            tvDay.setText("" + sdfDay.format(date));
            tvNumber.setText("" + getArguments().getString("DAY"));
            tvMonth.setText("" + getArguments().getString("MONTH"));
        } catch (Exception e) {

            dateCurrent = sdfDate.format(calendar.getTime());

            getAllTrainer(dateCurrent);

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
                getAllTrainer(dateCurrent);
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
                getAllTrainer(dateCurrent);
            }
        });

        vp_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trinerPageviewer.getCurrentItem() == all_trainer_list
                        .size() - 1) {
                    trinerPageviewer.setCurrentItem(0, false);

                } else {
                    trinerPageviewer.setCurrentItem(
                            trinerPageviewer.getCurrentItem() + 1, false);
                }
            }
        });

        vp_prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trinerPageviewer.getCurrentItem() == 0) {
                    trinerPageviewer.setCurrentItem(all_trainer_list.size(),
                            false);
                } else {
                    trinerPageviewer.setCurrentItem(
                            trinerPageviewer.getCurrentItem() - 1, false);
                }
                // loadmore(all_triner_obj.getPt_id(), dateCurrent);
            }
        });

        trinerPageviewer.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                //loadmore(all_trainer_list.get(position).getPt_id(), dateCurrent);
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

    public void getAllTrainer(final String date) {

        AsyncTask<Void, Void, Void> allTrainer = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                prevDate.setClickable(false);
                nextDate.setClickable(false);

                viewpagerPbar.setVisibility(View.VISIBLE);
                trinerPageviewer.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/trainer_by_date?client_id="
                            + AppConfig.loginDatatype.getSiteUserId()
                            + "&date_val=" + date);
                    HttpResponse response;
                    response = httpclient.execute(httpget);
                    HttpEntity httpentity = response.getEntity();
                    InputStream is = httpentity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    urlResponse = sb.toString();
                    try {
                        JSONObject jOBJ = new JSONObject(urlResponse);
                        JSONArray jsonArray = jOBJ.getJSONArray("trainer");
                        viewPagerItemNo = jsonArray.length();
                        altrainerDataTypeArrayList = new ArrayList<AltrainerDataType>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            altrainerDataType = new AltrainerDataType(jsonObject.getString("pt_id"),
                                    jsonObject.getString("pt_name"),
                                    jsonObject.getString("pt_image"),
                                    jsonObject.getString("working_address"));
                            altrainerDataTypeArrayList.add(altrainerDataType);
                        }

                        Log.d("RESPONSE", jOBJ.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        JSONArray jArr = new JSONArray("");
                    }

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/trainer_by_date?client_id="
                        + AppConfig.loginDatatype.getSiteUserId()
                        + "&date_val=" + date);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                prevDate.setClickable(true);
                nextDate.setClickable(true);
                viewpagerPbar.setVisibility(View.GONE);
                trinerPageviewer.setVisibility(View.VISIBLE);
                if (exception.equals("")) {
                    AllTrainerAdapter trainerAdapter = new AllTrainerAdapter(getActivity(), 0, altrainerDataTypeArrayList);
                    trinerPageviewer.setAdapter(trainerAdapter);

                    if (altrainerDataTypeArrayList.size() > 1) {
                        vp_next.setVisibility(View.VISIBLE);
                        vp_prev.setVisibility(View.VISIBLE);
                    } else {
                        vp_next.setVisibility(View.GONE);
                        vp_prev.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Server not responding....", Toast.LENGTH_LONG)
                            .show();
                }
            }

        };
        allTrainer.execute();

    }

}
//"http://esolz.co.in/lab6/ptplanner/app_control/trainer_booking_details?trainer_id="
//        + trainer_id + "&date_val=" + date_val