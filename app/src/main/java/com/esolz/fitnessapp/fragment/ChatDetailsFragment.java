package com.esolz.fitnessapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.MessageChatAdapter;
import com.esolz.fitnessapp.customviews.TitilliumRegularEdit;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.UserRespectiveMSGDatatype;
import com.esolz.fitnessapp.fitness.LandScreenActivity;
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
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;

public class ChatDetailsFragment extends FragmentActivity {

    LinearLayout llViewDetails, back;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ListView chat_history;
    MessageChatAdapter messageChatAdapter;
    ProgressBar pbarChat;

    LinkedList<UserRespectiveMSGDatatype> userRespectiveMSGDatatypeLinkedList;
    UserRespectiveMSGDatatype userRespectiveMSGDatatype;

    ImageView ed;
    TitilliumSemiBold titleChatDetails;
    EditText etSendMsg;

    String msgUserName = "", msgUserId = "",
            exception = "", urlResponse = "", exceptionError = "",
            exceptionSend = "", urlResponseSend = "";
    ConnectionDetector cd;
    TitilliumSemiBold txtError;

    int nextChatStart = 0;

    RelativeLayout rlSendMsg, rlLoader, rlBlank;

    TitilliumRegularEdit etSendMSG;

    boolean isBlankVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.msg_history);

        cd = new ConnectionDetector(ChatDetailsFragment.this);

        titleChatDetails = (TitilliumSemiBold) findViewById(R.id.title_chat_details);

        pbarChat = (ProgressBar) findViewById(R.id.pbar_chat);
        chat_history = (ListView) findViewById(R.id.listviewchat);
        txtError = (TitilliumSemiBold) findViewById(R.id.txt_error);
        txtError.setVisibility(View.GONE);
        pbarChat.setVisibility(View.GONE);
        chat_history.setVisibility(View.GONE);

        llViewDetails = (LinearLayout) findViewById(R.id.ll_viewdetails);
        back = (LinearLayout) findViewById(R.id.back);

        etSendMsg = (TitilliumRegularEdit) findViewById(R.id.et_send_msg);

        ed = (ImageView) findViewById(R.id.send_msg);

        rlSendMsg = (RelativeLayout) findViewById(R.id.rl_send_msg);
        rlLoader = (RelativeLayout) findViewById(R.id.rl_loader);
        rlBlank = (RelativeLayout) findViewById(R.id.rl_blank);
        rlBlank.setVisibility(View.GONE);

        fragmentManager = getSupportFragmentManager();

        try {
            msgUserName = getIntent().getExtras().getString("msgUserName");
            msgUserId = getIntent().getExtras().getString("msgUserId");
            AppConfig.PT_ID = msgUserId;
            AppConfig.PT_NAME = msgUserName;
            titleChatDetails.setText(msgUserName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cd.isConnectingToInternet()) {
            getAllMessage();
        } else {
            Toast.makeText(ChatDetailsFragment.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        rlSendMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (etSendMsg.getText().toString().equals("")) {
                        rlBlank.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            sendMessage(
                                    msgUserId,
                                    AppConfig.loginDatatype.getSiteUserId(),
                                    URLEncoder.encode(etSendMsg.getText().toString(), "UTF-8")
                            );
                        } catch (Exception e) {
                            Log.i("Send ex : ", e.toString());
                        }
                        Log.i("Send To ID : ", msgUserId);
                        Log.i("Send To BY : ", AppConfig.loginDatatype.getSiteUserId());
                    }
                } else {
                    Toast.makeText(ChatDetailsFragment.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llViewDetails.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ChatDetailsFragment.this, LandScreenActivity.class);
                intent.putExtra("MSG", "ChatDetailsFragment");
                startActivity(intent);
            }
        });

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ChatDetailsFragment.this, LandScreenActivity.class);
                intent.putExtra("MSG", "MSGFragment");
                startActivity(intent);
            }
        });

    }

    public void sendMessage(final String sendTo, final String sendFrom, final String MSG) {

        AsyncTask<Void, Void, Void> sendMSG = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                rlLoader.setVisibility(View.VISIBLE);
                rlBlank.setVisibility(View.GONE);
                rlSendMsg.setClickable(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionSend = "";
                    urlResponseSend = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/dashboard/send_message_through_app?sent_to=" + sendTo
                            + "&sent_by=" + sendFrom + "&message=" + MSG);
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
                    urlResponseSend = sb.toString();
                    JSONObject jOBJ = new JSONObject(urlResponseSend);

                    userRespectiveMSGDatatype = new UserRespectiveMSGDatatype(
                            true,
                            jOBJ.getString("id"),
                            sendTo,
                            sendFrom,
                            jOBJ.getString("message"),
                            "",
                            jOBJ.getString("send_time"),
                            jOBJ.getString("sender"),
                            jOBJ.getString("receiver"),
                            jOBJ.getString("sender_image"),
                            jOBJ.getString("receiver_image"),
                            jOBJ.getString("status"),
                            0
                    );

                } catch (Exception e) {
                    exceptionSend = e.toString();
                }

                Log.i("RESPONSE", urlResponseSend);
                Log.i("URL", "http://esolz.co.in/lab6/ptplanner/dashboard/send_message_through_app?sent_to=" + sendTo
                        + "&sent_by=" + sendFrom + "&message=" + MSG);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                rlLoader.setVisibility(View.GONE);
                rlSendMsg.setClickable(true);
                if (exceptionSend.equals("")) {
                    messageChatAdapter.addFromReceiver(userRespectiveMSGDatatype);
                    etSendMsg.setText("");
                } else {
                    Log.i("exceptionSend : ", exceptionSend);
                    Toast.makeText(ChatDetailsFragment.this, "Server not responding...." + exceptionSend, Toast.LENGTH_LONG).show();
                }
            }
        };
        sendMSG.execute();

    }

    public void getAllMessage() {

        AsyncTask<Void, Void, Void> allMSG = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pbarChat.setVisibility(View.VISIBLE);
                chat_history.setVisibility(View.GONE);
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
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/dashboard/get_user_respective_messages?user_id=" +
                            msgUserId + "&logged_in_user=" +
                            AppConfig.loginDatatype.getSiteUserId() +
                            "&start=0");
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

                    nextChatStart = jOBJ.getInt("next_start");

                    try {
                        userRespectiveMSGDatatypeLinkedList = new LinkedList<UserRespectiveMSGDatatype>();
                        JSONArray jsonArray = jOBJ.getJSONArray("all_message");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            userRespectiveMSGDatatype = new UserRespectiveMSGDatatype(
                                    true,
                                    jsonObject.getString("id"),
                                    jsonObject.getString("sent_to"),
                                    jsonObject.getString("sent_by"),
                                    jsonObject.getString("message"),
                                    jsonObject.getString("read_status"),
                                    jsonObject.getString("send_time"),
                                    jsonObject.getString("sender"),
                                    jsonObject.getString("receiver"),
                                    jsonObject.getString("sender_image"),
                                    jsonObject.getString("receiver_image"),
                                    jsonObject.getString("status"),
                                    jOBJ.getInt("next_start")
                            );
                            userRespectiveMSGDatatypeLinkedList.add(userRespectiveMSGDatatype);
                        }
                    } catch (Exception ex) {
                        exceptionError = ex.toString();
                    }
                    Log.i("RESPONSE", jOBJ.toString());
                    Log.i("URL", "http://esolz.co.in/lab6/ptplanner/dashboard/get_user_respective_messages?user_id=" +
                            msgUserId + "&logged_in_user=" +
                            AppConfig.loginDatatype.getSiteUserId() +
                            "&start=0");
                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pbarChat.setVisibility(View.GONE);
                if (exception.equals("")) {
                    chat_history.setVisibility(View.VISIBLE);
                    if (exceptionError.equals("")) {
                        Collections.reverse(userRespectiveMSGDatatypeLinkedList);
                        messageChatAdapter = new MessageChatAdapter(ChatDetailsFragment.this, 0,
                                userRespectiveMSGDatatypeLinkedList,
                                pbarChat,
                                nextChatStart,
                                msgUserId,
                                chat_history);
                        chat_history.setAdapter(messageChatAdapter);
                    } else {
                        txtError.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(ChatDetailsFragment.this, "Server not responding....", Toast.LENGTH_LONG)
                            .show();
                }
            }
        };
        allMSG.execute();
    }
}







