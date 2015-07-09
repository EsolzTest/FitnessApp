package com.esolz.fitnessapp.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.AppointmentListAdapter;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumLight;
import com.esolz.fitnessapp.datatype.AppointDataType;
import com.esolz.fitnessapp.datatype.AppointmentDataType;
import com.esolz.fitnessapp.datatype.AppointmentListDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AppointmantFragment extends Fragment {

    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, back;
    RelativeLayout llMessagebutton, llCancel;

    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;
    String exception = "", urlResponse = "";

    ProgressBar progressBar, cancelPBar;
    LinearLayout mainContainer;
    ScrollView scrollView;
    AppointmentDataType appointDataType;
    ConnectionDetector cd;
    AppointmentListDataType appointmentListDataType;
    ImageView imgPic;
    TitilliumBold txtName;
    TitilliumLight txtNameSub, txtDesc, txtDatetime, txtTo, txtFrom, txtLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_appointment, container, false);

        back = (LinearLayout) fView.findViewById(R.id.back);
        fragmentManager = getActivity().getSupportFragmentManager();

        cd = new ConnectionDetector(getActivity());

        progressBar = (ProgressBar) fView.findViewById(R.id.prog_bar);
        mainContainer = (LinearLayout) fView.findViewById(R.id.main_container);
        scrollView = (ScrollView) fView.findViewById(R.id.scrl_body);
        imgPic = (ImageView) fView.findViewById(R.id.img_pic);
        txtName = (TitilliumBold) fView.findViewById(R.id.txt_name);
        txtNameSub = (TitilliumLight) fView.findViewById(R.id.txt_name_sub);
        txtDesc = (TitilliumLight) fView.findViewById(R.id.txt_desc);
        txtDatetime = (TitilliumLight) fView.findViewById(R.id.txt_datetime);
        txtTo = (TitilliumLight) fView.findViewById(R.id.txt_to);
        txtFrom = (TitilliumLight) fView.findViewById(R.id.txt_from);
        txtLocation = (TitilliumLight) fView.findViewById(R.id.txt_location);
        llCancel = (RelativeLayout) fView.findViewById(R.id.ll_cancel);
        cancelPBar = (ProgressBar) fView.findViewById(R.id.cancel_pbar);
        cancelPBar.setVisibility(View.GONE);

        if (cd.isConnectingToInternet()) {
            try {
                String id = getArguments().getString("BOOKID");
                if (id.equals("")) {
                    getAppointmentList(getArguments().getString("DateChange"));
                } else {
                    getAppointmentDetails(getArguments().getString("BOOKID"));
                }
            } catch (Exception e) {
                getAppointmentList(getArguments().getString("DateChange"));
            }
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        llCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!appointDataType.getCancel_status().equals("CAN_NOT")) {

                    try {
                        String id = getArguments().getString("BOOKID");
                        if (id.equals("")) {
                            getAppointmentListCancel(getArguments().getString("DateChange"));
                        } else {
                            cancelAppointmentDetails(getArguments().getString("BOOKID"));
                        }
                    } catch (Exception e) {
                        getAppointmentListCancel(getArguments().getString("DateChange"));
                    }
                } else {

                }
            }
        });


        llCalenderButton = (LinearLayout) getActivity().findViewById(
                R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout)
                getActivity().findViewById(R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(
                R.id.progressbutton);
        llMessagebutton = (RelativeLayout)
                getActivity().findViewById(R.id.messagebutton);
        llCalenderButton.setClickable(false);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(true);


        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

//                if (getArguments().getString("PAGE").equals("LIST")) {
//                    bundle = new Bundle();
//                    bundle.putString("DateChange", getArguments().getString("DateChange"));
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    AppointmentListFragment app_list_fragment = new AppointmentListFragment();
//                    fragmentTransaction.replace(R.id.fragment_container,
//                            app_list_fragment);
//                    fragmentTransaction.commit();
//
//                } else {
                bundle = new Bundle();
                bundle.putString("DateChange", appointDataType.getBooked_date());
                fragmentTransaction = fragmentManager.beginTransaction();
                CalenderFragment cal_fragment = new CalenderFragment();
                cal_fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,
                        cal_fragment);
                fragmentTransaction.commit();
                //}

            }
        });

        return fView;
    }

    public void getAppointmentList(final String date) {

        AsyncTask<Void, Void, Void> appointList = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_all_booking?client_id=" +
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
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    JSONArray jsonArray = jOBJ.getJSONArray("bookings");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        appointmentListDataType = new AppointmentListDataType(jsonObject.getString("id"),
                                jsonObject.getString("trainer_name"),
                                jsonObject.getString("booked_by"),
                                jsonObject.getString("booked_date"),
                                jsonObject.getString("booking_time_start"),
                                jsonObject.getString("booking_time_end"),
                                jsonObject.getString("status"),
                                jsonObject.getString("booking_date"),
                                jsonObject.getString("program_name"));
                    }

                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/get_all_booking?client_id=" +
                        AppConfig.loginDatatype.getSiteUserId() + "&date_val=" + date);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                if (exception.equals("")) {
                    getAppointmentDetails(appointmentListDataType.getId());
                } else {
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        appointList.execute();

    }

    public void getAppointmentDetails(final String booking_id) {

        AsyncTask<Void, Void, Void> appointDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_each_booking_details?booking_id=" + booking_id);
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
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    appointDataType = new AppointmentDataType(jOBJ.getString("trainer_name"),
                            jOBJ.getString("trainer_image"),
                            jOBJ.getString("trainer_about"),
                            jOBJ.getString("trainer_address"),
                            jOBJ.getString("booked_by"),
                            jOBJ.getString("booked_date"),
                            jOBJ.getString("booking_time_start"),
                            jOBJ.getString("booking_time_end"),
                            jOBJ.getString("cancel_status"),
                            jOBJ.getString("status"),
                            jOBJ.getString("booking_date"),
                            jOBJ.getString("program_name"));

                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/get_each_booking_details?booking_id=" + booking_id);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                progressBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    scrollView.setVisibility(View.VISIBLE);

                    Picasso.with(getActivity()).load(appointDataType.getTrainer_image()).transform(new Trns()).centerCrop().fit().into(imgPic);
                    txtName.setText(appointDataType.getTrainer_name());
                    txtNameSub.setText(appointDataType.getProgram_name());
                    txtDesc.setText(appointDataType.getTrainer_about());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date myDate = null;
                    try {
                        myDate = dateFormat.parse(appointDataType.getBooked_date());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE MMM dd, yyyy");
                    String finalDate = timeFormat.format(myDate);
                    txtDatetime.setText(finalDate);

                    txtTo.setText(appointDataType.getBooking_time_start());
                    txtFrom.setText(appointDataType.getBooking_time_end());
                    txtLocation.setText(appointDataType.getTrainer_address());

                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(getActivity(),
                            "Server not responding....", Toast.LENGTH_LONG)
                            .show();
                }
            }

        };
        appointDetails.execute();

    }

    public void getAppointmentListCancel(final String date) {

        AsyncTask<Void, Void, Void> appointListCancel = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                cancelPBar.setVisibility(View.VISIBLE);
                llCancel.setClickable(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_all_booking?client_id=" +
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
                    JSONObject jOBJ = new JSONObject(urlResponse);
                    JSONArray jsonArray = jOBJ.getJSONArray("bookings");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        appointmentListDataType = new AppointmentListDataType(jsonObject.getString("id"),
                                jsonObject.getString("trainer_name"),
                                jsonObject.getString("booked_by"),
                                jsonObject.getString("booked_date"),
                                jsonObject.getString("booking_time_start"),
                                jsonObject.getString("booking_time_end"),
                                jsonObject.getString("status"),
                                jsonObject.getString("booking_date"),
                                jsonObject.getString("program_name"));
                    }

                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/get_all_booking?client_id=" +
                        AppConfig.loginDatatype.getSiteUserId() + "&date_val=" + date);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                if (exception.equals("")) {
                    cancelAppointmentDetails(appointmentListDataType.getId());
                } else {
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        appointListCancel.execute();

    }

    public void cancelAppointmentDetails(final String booking_id) {

        AsyncTask<Void, Void, Void> appointDetailsCancel = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                cancelPBar.setVisibility(View.VISIBLE);
                llCancel.setClickable(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/cancel_booking?booking_id" + booking_id);
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
                    JSONObject jOBJ = new JSONObject(urlResponse);

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/cancel_booking?booking_id=" + booking_id);
                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                cancelPBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    llCancel.setClickable(true);
                    bundle = new Bundle();
                    bundle.putString("DateChange", appointDataType.getBooked_date());
                    fragmentTransaction = fragmentManager.beginTransaction();
                    CalenderFragment cal_fragment = new CalenderFragment();
                    cal_fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container,
                            cal_fragment);
                    fragmentTransaction.commit();
                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(getActivity(),
                            "Server not responding....", Toast.LENGTH_LONG)
                            .show();
                }
            }

        };
        appointDetailsCancel.execute();

    }

}
