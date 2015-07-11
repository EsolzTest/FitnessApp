package com.esolz.fitnessapp.fragment;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.MessageChatAdapter;
import com.esolz.fitnessapp.adapter.SendAdapter;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.ChatDataType;
import com.esolz.fitnessapp.datatype.SendDataType;
import com.esolz.fitnessapp.fitness.LandScreenActivity;
import com.esolz.fitnessapp.helper.AppConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class ChatDetailsFragment extends FragmentActivity {

    LinearLayout llViewDetails, back;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    JSONObject user_chat_list_object;
    String Url;

    Context con = this;
    RelativeLayout Rel;
    ListView chat_history;
    MessageChatAdapter chatAdapter;
    public boolean loading = false;
    ProgressBar pbar1;
    //LinkedList<ProfileViewDataType> user_data;

    LinkedList<ChatDataType> all_user_data = new LinkedList<ChatDataType>();
    LinkedList<SendDataType> all_chat_data = new LinkedList<SendDataType>();

    ImageView ed;
    String id, name;
    TitilliumSemiBold titleChatDetails;
    SendDataType send_data;
    EditText etSendMsg;
    String text_data;

    String msgUserName = "", msgUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.msg_history);
        titleChatDetails = (TitilliumSemiBold) findViewById(R.id.title_chat_details);
        Rel = (RelativeLayout) findViewById(R.id.msg_hstry);
        Rel.setVisibility(View.VISIBLE);
        chat_history = (ListView) findViewById(R.id.listviewchat);
        pbar1 = (ProgressBar) findViewById(R.id.progbar1);
        pbar1.setVisibility(View.GONE);

        llViewDetails = (LinearLayout) findViewById(R.id.ll_viewdetails);
        back = (LinearLayout) findViewById(R.id.back);

        etSendMsg = (EditText) findViewById(R.id.et_send_msg);

        ed = (ImageView) findViewById(R.id.send_msg);

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


//        ed.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Toast.makeText(ChatDetailsFragment.this, etSendMsg.getText().toString(), Toast.LENGTH_LONG).show();
//
//                //sendMessageToServer(utils.getSendMessageJSON(etSendMsg.getText()
//                //.toString()));
//
//
//                // Clearing the input filed once message was sent
//                //text_data.setText("");
//                text_data = etSendMsg.getText().toString();
//
//                //method
//
//                //chat_history.setAdapter(new MessageChatAdapter(con, 0, all_user_data));
//
//                new GetData().execute();
//            }
//        });


    }

    //*************Edit_chat

    //
    class GetData extends AsyncTask<Void, Void, Void> {

        InputStream is;
        String json;
        JSONArray json_arr;

        JSONObject all_news_list_object;


        @Override
        protected Void doInBackground(Void... params) {

            try {

                DefaultHttpClient httClient = new DefaultHttpClient();
                // HttpClient client = HttpClientBuilder.create().build();

                HttpGet http_get = new HttpGet("http://esolz.co.in/lab6/ptplanner/dashboard/send_message_through_app?" +
                        "sent_to=1&sent_by=14&message=loremipsum%20dolor%20sitamet" + text_data);

                HttpResponse response = httClient.execute(http_get);

                HttpEntity httpEntity = response.getEntity();

                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                StringBuilder sb = new StringBuilder();

                String line = null;

                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");
                }
                is.close();

                json = sb.toString();
                all_news_list_object = new JSONObject(json);


                //data = new LinkedList<MsgDataType>();
                //String total_data = all_news_list_object.getString("all_user");
                //json_arr = all_news_list_object.getJSONArray("all_user");

                send_data = new SendDataType(all_news_list_object.getString("id"), all_news_list_object.getString("sender"), all_news_list_object.getString("receiver"), all_news_list_object.getString("sender_image"), all_news_list_object.getString("receiver_image"), all_news_list_object.getString("message"), all_news_list_object.getString("send_time"), all_news_list_object.getString("status"));

                //SendAdapter.add(new SendDataType(chat_history, text_data));
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //pbar1.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            chat_history.setAdapter(new SendAdapter(con, 0, all_chat_data));
            etSendMsg.setText("");

            new SendAdapter(con, 0).notifyDataSetChanged();

            //pbar1.setVisibility(View.GONE);
        }

    }


    //*******************Lazzy loading


    public void loadMore(int position) {


        final int pos = position;
        if (position == 0) {

            Url = "http://esolz.co.in/lab6/ptplanner/dashboard/get_user_respective_messages?user_id=" + id + "&logged_in_user=15&start=0";

        } else {
            Url = "http://esolz.co.in/lab6/ptplanner/dashboard/get_user_respective_messages?user_id=" + id + "&logged_in_user=15&start=" + pos;
        }


        //public void asyntask_for_listview(Context context) {


        //public void asyntask_for_listview(final ListView chat_history, Context context) {

        final Context cont = con;
        //TAKING  LIST VIEW DATA FROM JSON
        //SETTING UP LIST VIEW ITEMS FROM JSON.. WHILE TAKING ID FROM THE VIEW PAGER ELEMENT..
        //SETTING ASYNTASK CLASS
        new AsyncTask<Void, Void, Void>() {
            //ProgressDialog pg;
            InputStream is_lv;
            String json_lv;
            JSONArray json_arr_lv;
            //Context cont = con;
            //ProgressDialog dialog;
            //JSONObject all_news_list_object;


            @Override
            protected void onPreExecute() {
                pbar1.setVisibility(View.VISIBLE);


                super.onPreExecute();
                //   pg.setMessage("Please wait while retrieving chat details..");
                // pg.show();


            }

            @Override
            protected Void doInBackground(Void... voids) {

                loading = true;
                try {

                    DefaultHttpClient httClient = new DefaultHttpClient();

                    HttpGet http_get = new HttpGet(Url);

                    HttpResponse response = httClient.execute(http_get);

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
                    System.out.print("!! Response : " + json_lv);

                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    user_chat_list_object = new JSONObject(json_lv);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data in chat details " + e.toString());
                }

                try {


                    json_arr_lv = user_chat_list_object.getJSONArray("all_message");


                    //getting details of array
                    if (json_arr_lv.length() != 0) {

                        for (int i = 0; i < json_arr_lv.length(); i++) {
                            JSONObject Json_Obj_temp;

                            Json_Obj_temp = json_arr_lv.getJSONObject(i);

                            //JSONObject Json_Obj_temp;


                            ChatDataType obj = new ChatDataType(Json_Obj_temp.getString("id"), Json_Obj_temp.getString("sent_to"), Json_Obj_temp.getString("sent_by"), Json_Obj_temp.getString("message"), Json_Obj_temp.getString("read_status"), Json_Obj_temp.getString("send_time"), Json_Obj_temp.getString("sender"), Json_Obj_temp.getString("receiver"), Json_Obj_temp.getString("sender_image"), Json_Obj_temp.getString("receiver_image"), Json_Obj_temp.getString("status"));
                            //obj.setTotal_data(json_lv);
                            obj.setIs_added(true);
                            all_user_data.add(obj);


                        }
                    }
                } catch (Exception e) {
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {

                //super.onPostExecute(aVoid);
                if (pos == 0) {
                    pbar1.setVisibility(View.GONE);

                    chat_history.setAdapter(new MessageChatAdapter(con, 0, all_user_data));
                } else {


                    new MessageChatAdapter(con, 0, all_user_data).notifyDataSetChanged();
                }


                loading = false;

                //Toast.makeText(getApplicationContext(),""+,Toast.LENGTH_SHORT).show();
            }
        }.execute();

    }
}







