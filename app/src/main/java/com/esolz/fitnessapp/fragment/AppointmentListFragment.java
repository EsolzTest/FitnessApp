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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.AppointmentListAdapter;
import com.esolz.fitnessapp.datatype.AppointmentListDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ltp on 06/07/15.
 */
public class AppointmentListFragment extends Fragment {
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton,
            back;
    RelativeLayout llMessagebutton;

    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    ListView listApp;
    ProgressBar progBar;

    String exception = "", urlResponse = "";
    AppointmentListDataType appointmentListDataType;
    ArrayList<AppointmentListDataType> appointmentListDataTypeArrayList;
    ConnectionDetector cd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_applist, container, false);

        back = (LinearLayout) fView.findViewById(R.id.back);
        fragmentManager = getActivity().getSupportFragmentManager();

        listApp = (ListView) fView.findViewById(R.id.list_app);
        progBar = (ProgressBar) fView.findViewById(R.id.prog_bar);
        progBar.setVisibility(View.GONE);

        cd = new ConnectionDetector(getActivity());
        if (cd.isConnectingToInternet()) {
            getAppointmentList(getArguments().getString("DateChange"));
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
        back.setOnClickListener(new View.OnClickListener() {

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

        return fView;
    }

    public void getAppointmentList(final String date) {

        AsyncTask<Void, Void, Void> appointList = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                listApp.setVisibility(View.GONE);
                progBar.setVisibility(View.VISIBLE);
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
                    appointmentListDataTypeArrayList = new ArrayList<AppointmentListDataType>();
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
                        appointmentListDataTypeArrayList.add(appointmentListDataType);
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
                progBar.setVisibility(View.GONE);

                if (exception.equals("")) {
                    listApp.setVisibility(View.VISIBLE);

                    AppointmentListAdapter appointmentListAdapter = new AppointmentListAdapter(getActivity(), 0,
                            appointmentListDataTypeArrayList, getArguments().getString("DateChange"));
                    listApp.setAdapter(appointmentListAdapter);

                } else {
                    Toast.makeText(getActivity(),
                            "Server not responding....", Toast.LENGTH_LONG)
                            .show();
                }
            }

        };
        appointList.execute();

    }

}
