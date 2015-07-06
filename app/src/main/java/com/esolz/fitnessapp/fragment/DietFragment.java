package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.DietAdapter;
import com.esolz.fitnessapp.datatype.AllEventsDatatype;
import com.esolz.fitnessapp.datatype.DietDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

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
    LinearLayout back;
    ConnectionDetector cd;
    LinkedList<DietDataType> dietDataTypeLinkedList;
    DietAdapter dietAdapter;
    View fView;
    String url;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ConnectionDetector connectionDetector;
    ProgressBar pBar;
    String exception = "", urlResponse = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_diet, container, false);

        pBar = (ProgressBar) fView.findViewById(R.id.progbar);
        pBar.setVisibility(View.GONE);
        fragmentManager = getActivity().getSupportFragmentManager();

        back = (LinearLayout) fView.findViewById(R.id.back);
        dietList = (ListView) fView.findViewById(R.id.diet_list);
        dietList.setDivider(null);

        connectionDetector = new ConnectionDetector(getActivity());
        url = "http://esolz.co.in/lab6/ptplanner/app_control/date_respective_client_meal?logged_in_user=15&date_val=2015-05-31";


        if (connectionDetector.isConnectingToInternet()) {
            getDietList("2015-07-03");
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
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/date_respective_client_meal?logged_in_user="+
                            AppConfig.loginDatatype.getSiteUserId()+"&date_val=" + date);
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
                    JSONArray jsonArray = jOBJ.getJSONArray("meal");
                    dietDataTypeLinkedList = new LinkedList<DietDataType>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DietDataType dietDataType = new DietDataType(jsonObject.getString("meal_id"),
                                jsonObject.getString("custom_meal_id"),
                                jsonObject.getString("meal_title"),
                                jsonObject.getString("meal_description"),
                                jsonObject.getString("meal_image"));
                        dietDataTypeLinkedList.add(dietDataType);
                    }

                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                if (exception.equals("")) {
                    pBar.setVisibility(View.GONE);
                    dietList.setVisibility(View.VISIBLE);
                    dietAdapter = new DietAdapter(getActivity(), 0, dietDataTypeLinkedList);
                    dietList.setAdapter(dietAdapter);
                } else {
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        dietListDetails.execute();

    }

}
