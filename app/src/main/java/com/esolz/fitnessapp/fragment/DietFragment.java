package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.DietAdapter;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.AllEventsDatatype;
import com.esolz.fitnessapp.datatype.DietDataType;
import com.esolz.fitnessapp.dialog.ShowCalendarPopUp;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;
import com.esolz.fitnessapp.helper.ReturnCalendarDetails;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DietFragment extends Fragment {

    public ListView dietList;
    LinearLayout back, showCalendar;
    TitilliumSemiBold txtError;
    ConnectionDetector cd;
    LinkedList<DietDataType> dietDataTypeLinkedList;
    DietAdapter dietAdapter;
    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ConnectionDetector connectionDetector;
    ProgressBar pBar;
    String exception = "", exceptionError = "", urlResponse = "";

    // -- Calendar Instance
    Calendar calendar;
    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition;
    SimpleDateFormat dayFormat, monthFormat, dateFormat;
    Date dateChange;
    String date = "";
    String[] positionPre = {};
    int previousDayPosition;

    ShowCalendarPopUp showCalPopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        fView = inflater.inflate(R.layout.frag_diet, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        connectionDetector = new ConnectionDetector(getActivity());

        pBar = (ProgressBar) fView.findViewById(R.id.progbar);
        pBar.setVisibility(View.GONE);
        back = (LinearLayout) fView.findViewById(R.id.back);
        showCalendar = (LinearLayout) fView.findViewById(R.id.show_cal);
        dietList = (ListView) fView.findViewById(R.id.diet_list);
        dietList.setDivider(null);
        txtError = (TitilliumSemiBold) fView.findViewById(R.id.txt_error);

        calendar = Calendar.getInstance(Locale.getDefault());
        currentDate = calendar.get(Calendar.DATE);
        currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        currentMonth = (calendar.get(Calendar.MONTH));
        currentYear = calendar.get(Calendar.YEAR);
        firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        dayFormat = new SimpleDateFormat("dd");
        monthFormat = new SimpleDateFormat("EEEE");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // -- Show Calendar
        showCalPopup = new ShowCalendarPopUp(getActivity(), "diet");

        showCalendar.setOnClickListener(new OnClickListener() {

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

            }
            //------getting date
        });

        if (connectionDetector.isConnectingToInternet()) {
            try {
                dateChange = dateFormat.parse(getArguments().getString("DateChange"));

                Log.d("DAY==", getArguments().getString("DateChange"));

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(getArguments().getString("DateChange")));
                calendar = cal;

                getDietList(getArguments().getString("DateChange"));

            } catch (Exception e) {
                Log.d("Date Exception : ", e.toString());
                date = "" + dateFormat.format(calendar.getTime());
                getDietList(date);
            }
            //getDietList("2015-07-03");
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

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

    public void getDietList(final String date) {

        AsyncTask<Void, Void, Void> dietListDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
                dietList.setVisibility(View.GONE);
                txtError.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    exceptionError = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/date_respective_client_meal?logged_in_user=" +
                            AppConfig.loginDatatype.getSiteUserId() + "&date_val=" + date);
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
                        JSONArray jsonArray = jOBJ.getJSONArray("meal");
                        dietDataTypeLinkedList = new LinkedList<DietDataType>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            DietDataType dietDataType = new DietDataType(
                                    jsonObject.getString("meal_id"),
                                    jsonObject.getString("meal_image"),
                                    jsonObject.getString("meal_description"),
                                    jsonObject.getString("meal_title"),
                                    jsonObject.getString("custom_meal_id"));
                            dietDataTypeLinkedList.add(dietDataType);
                        }
                    } catch (Exception ex) {
                        exceptionError = ex.toString();
                    }


                    Log.d("Diet URL : ", "http://esolz.co.in/lab6/ptplanner/app_control/date_respective_client_meal?logged_in_user=" +
                            AppConfig.loginDatatype.getSiteUserId() + "&date_val=" + date);

                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    if (exceptionError.equals("")) {
                        dietList.setVisibility(View.VISIBLE);
                        dietAdapter = new DietAdapter(getActivity(), 0, dietDataTypeLinkedList);
                        dietList.setAdapter(dietAdapter);
                    } else {
                        txtError.setVisibility(View.GONE);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder
                                .setMessage("Zero Calorie Diet")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        dietListDetails.execute();

    }

}
