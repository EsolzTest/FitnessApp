package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.esolz.fitnessapp.adapter.MessageRoomAdapter;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.datatype.MsgDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
    LinkedList<MsgDataType> msgDataTypeLinkedList;
    String exception = "", urlResponse = "";
    ConnectionDetector cd;

    TitilliumBold username;
    String user_name;

    View fView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        fView = inflater.inflate(R.layout.frag_message, container, false);
        cd = new ConnectionDetector(getActivity());

        pbar = (ProgressBar) fView.findViewById(R.id.progbar);
        pbar.setVisibility(View.GONE);
        messageRoomList = (ListView) fView.findViewById(R.id.listviewmessageroom);
        messageRoomList.setDivider(null);

        if (cd.isConnectingToInternet()) {
            getMSGLis();
        } else {
            Toast.makeText(getActivity(), "no internet connection.", Toast.LENGTH_SHORT).show();
        }


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


//        new Getmessage().execute();

        return fView;

    }

    public void getMSGLis() {

        AsyncTask<Void, Void, Void> msgList = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pbar.setVisibility(View.VISIBLE);
                messageRoomList.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/dashboard/get_sender_list_app?logged_in_user=" +
                            AppConfig.loginDatatype.getSiteUserId());
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
                    msgDataTypeLinkedList = new LinkedList<MsgDataType>();
                    JSONArray jsonArray = jOBJ.getJSONArray("all_user");
                    if (jsonArray.length() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MsgDataType msgDataType = new MsgDataType(
                                    jsonObject.getString("user_id"),
                                    jsonObject.getString("last_send_time"),
                                    jsonObject.getString("user_name"),
                                    jsonObject.getString("user_image"),
                                    jsonObject.getString("last_message"));
                            msgDataTypeLinkedList.add(msgDataType);
                        }
                    }

                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/dashboard/get_sender_list_app?logged_in_user=" + AppConfig.loginDatatype.getSiteUserId());
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pbar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    messageRoomList.setVisibility(View.VISIBLE);
                    msgAdapter = new MessageRoomAdapter(getActivity(), 0, msgDataTypeLinkedList);
                    messageRoomList.setAdapter(msgAdapter);
                } else {
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }
        };
        msgList.execute();

    }
}


