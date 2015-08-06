package com.esolz.fitnessapp.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.AllGraphAdapter;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.ClientAllGraphDataType;
import com.esolz.fitnessapp.datatype.ClientAllGraphPointDataType;
import com.esolz.fitnessapp.datatype.GraphDetailsDataType;
import com.esolz.fitnessapp.datatype.GraphDetailsPointDataType;
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

/**
 * Created by ltp on 05/08/15.
 */
public class AllGraphFragment extends Fragment {

    View fView;
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, llGrphdetailsList;
    RelativeLayout llMessagebutton;

    ConnectionDetector connectionDetector;

    LinearLayout back;
    ListView listAllGraph;
    ProgressBar pBar;
    TitilliumSemiBold txtError;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    ClientAllGraphDataType clientAllGraphDataType;
    ArrayList<ClientAllGraphDataType> clientAllGraphDataTypeArrayList;

    ClientAllGraphPointDataType clientAllGraphPointDataType;
    ArrayList<ClientAllGraphPointDataType> clientAllGraphPointDataTypeArrayList;

    String exceptionGraphDetails = "", getUrlResponseGraphDetails = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fView = inflater.inflate(R.layout.frag_allgraph, container, false);

        connectionDetector = new ConnectionDetector(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();

        back = (LinearLayout) fView.findViewById(R.id.back);
        listAllGraph = (ListView) fView.findViewById(R.id.list_allgraph);
        pBar = (ProgressBar) fView.findViewById(R.id.progbar);
        txtError = (TitilliumSemiBold) fView.findViewById(R.id.txt_error);
        listAllGraph.setVisibility(View.GONE);
        pBar.setVisibility(View.GONE);
        txtError.setVisibility(View.GONE);

        if (connectionDetector.isConnectingToInternet()) {
            getGraphDetailsById(AppConfig.loginDatatype.getSiteUserId());
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                fragmentTransaction = fragmentManager.beginTransaction();
                ProgressFragment progressFragment = new ProgressFragment();
                fragmentTransaction.replace(R.id.fragment_container, progressFragment);
                fragmentTransaction.commit();
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
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(false);
        llMessagebutton.setClickable(true);

        return fView;
    }

    public void getGraphDetailsById(final String clientId) {

        AsyncTask<Void, Void, Void> graphDetailsById = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                listAllGraph.setVisibility(View.GONE);
                pBar.setVisibility(View.VISIBLE);
                txtError.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionGraphDetails = "";
                    getUrlResponseGraphDetails = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/all_graphs?client_id=" + clientId);
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
                    getUrlResponseGraphDetails = sb.toString();
                    JSONObject jOBJ = new JSONObject(getUrlResponseGraphDetails);

                    clientAllGraphDataTypeArrayList = new ArrayList<ClientAllGraphDataType>();

                    JSONArray jArr = jOBJ.getJSONArray("all_graphs");
                    for (int i = 0; i < jArr.length(); i++) {

                        clientAllGraphPointDataTypeArrayList = new ArrayList<ClientAllGraphPointDataType>();

                        JSONObject jsonObject = jArr.getJSONObject(i);
                        JSONArray jsonArray = jsonObject.getJSONArray("points");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonOBJ = jsonArray.getJSONObject(j);
                            clientAllGraphPointDataType = new ClientAllGraphPointDataType(
                                    jsonOBJ.getString("x_axis_point"),
                                    jsonOBJ.getString("y_axis_point")
                            );
                            clientAllGraphPointDataTypeArrayList.add(clientAllGraphPointDataType);
                        }
                        clientAllGraphDataType = new ClientAllGraphDataType(
                                jsonObject.getString("id"),
                                jsonObject.getString("graph_type"),
                                jsonObject.getString("graph_for"),
                                jsonObject.getString("measure_unit"),
                                jsonObject.getString("goal"),
                                jsonObject.getString("deadline"),
                                clientAllGraphPointDataTypeArrayList
                        );
                        clientAllGraphDataTypeArrayList.add(clientAllGraphDataType);
                    }

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/all_graphs?client_id=" + clientId);
                } catch (Exception e) {
                    exceptionGraphDetails = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);

                if (exceptionGraphDetails.equals("")) {

                    listAllGraph.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Success....", Toast.LENGTH_LONG).show();
                    AllGraphAdapter allGraphAdapter = new AllGraphAdapter(
                            getActivity(), 0, clientAllGraphDataTypeArrayList
                    );
                    listAllGraph.setAdapter(allGraphAdapter);

                } else {
                    txtError.setVisibility(View.VISIBLE);
                    Log.d("Exception : ", exceptionGraphDetails);
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        graphDetailsById.execute();

    }
}
