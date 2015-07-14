package com.esolz.fitnessapp.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.datatype.ChatDataType;
import com.esolz.fitnessapp.datatype.UserRespectiveMSGDatatype;
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
import java.util.LinkedList;

/**
 * Created by su on 18/6/15.
 */
public class MessageChatAdapter extends ArrayAdapter<UserRespectiveMSGDatatype> {

    Context context;
    LinkedList<UserRespectiveMSGDatatype> userRespectiveMSGDatatypes, userRespectiveMSGDatatypesLazy;
    UserRespectiveMSGDatatype userRespectiveMSGDatatype;
    LayoutInflater inflater;
    FragmentManager fragmentManager;
    ViewHolder holder;
    String exception = "", urlResponse = "";
    ConnectionDetector cd;

    public MessageChatAdapter(Context context, int resource, LinkedList<UserRespectiveMSGDatatype> userRespectiveMSGDatatypes) {
        super(context, resource, userRespectiveMSGDatatypes);

        this.context = context;
        this.userRespectiveMSGDatatypes = userRespectiveMSGDatatypes;
        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        cd = new ConnectionDetector(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.msg_receiver, parent, false);
            holder = new ViewHolder();

            holder.rlMSGContainer = (RelativeLayout) convertView.findViewById(R.id.rl_msg_container);

            holder.chatSendMsg = (TitilliumRegular) convertView.findViewById(R.id.chat_send_msg);
            holder.chatReceiveMsg = (TitilliumRegular) convertView.findViewById(R.id.chat_receive_msg);

            holder.llReceiver = (RelativeLayout) convertView.findViewById(R.id.rl_receiver);
            holder.rlSender = (RelativeLayout) convertView.findViewById(R.id.rl_sender);
            holder.chatSendTxtarea = (RelativeLayout) convertView.findViewById(R.id.chat_send_txtarea);
            holder.chatReceiveTxtarea = (RelativeLayout) convertView.findViewById(R.id.chat_receive_txtarea);

            holder.imgSender = (ImageView) convertView.findViewById(R.id.img_sender);
            holder.imgReceiver = (ImageView) convertView.findViewById(R.id.img_receiver);

            holder.pbarLoadMore = (ProgressBar) convertView.findViewById(R.id.pbar_load_more);
            holder.pbarLoadMore.setVisibility(View.GONE);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (userRespectiveMSGDatatypes.get(position).getSent_by().equals(AppConfig.loginDatatype.getSiteUserId())) {
            userRespectiveMSGDatatypes.get(position).setIsSender(false);
        } else {
            userRespectiveMSGDatatypes.get(position).setIsSender(true);
        }

        if (userRespectiveMSGDatatypes.get(position).getIsSender()) {
            holder.llReceiver.setVisibility(View.GONE);
            holder.rlSender.setVisibility(View.VISIBLE);

            holder.chatSendMsg.setText(userRespectiveMSGDatatypes.get(position).getMessage());

            Picasso.with(context).load(userRespectiveMSGDatatypes.get(position).getSender_image())
                    .transform(new Trns()).resize(400, 400).centerInside().into(holder.imgSender);

            Picasso.with(context).load(userRespectiveMSGDatatypes.get(position).getReceiver_image())
                    .transform(new Trns()).resize(400, 400).centerInside().into(holder.imgReceiver);

        } else {
            holder.llReceiver.setVisibility(View.VISIBLE);
            holder.rlSender.setVisibility(View.GONE);

            holder.chatReceiveMsg.setText(userRespectiveMSGDatatypes.get(position).getMessage());

            Picasso.with(context).load(userRespectiveMSGDatatypes.get(position).getReceiver_image())
                    .transform(new Trns()).resize(400, 400).centerInside().into(holder.imgSender);

            Picasso.with(context).load(userRespectiveMSGDatatypes.get(position).getSender_image())
                    .transform(new Trns()).resize(400, 400).centerInside().into(holder.imgReceiver);

        }
        int pageCount = 1;
        if (position == 0) {
            if (cd.isConnectingToInternet()) {
                //if (userRespectiveMSGDatatypes.get(position).getStartPosition() >= 0)
                getAllMessage(pageCount);
                pageCount++;
            } else {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        return convertView;
    }

    protected class ViewHolder {
        TitilliumRegular chatSendMsg, chatReceiveMsg;
        RelativeLayout llReceiver, rlSender, chatSendTxtarea, chatReceiveTxtarea, rlMSGContainer;
        ImageView imgSender, imgReceiver;
        ProgressBar pbarLoadMore;
    }

    public void getAllMessage(final int position) {

        AsyncTask<Void, Void, Void> allMSG = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                holder.pbarLoadMore.setVisibility(View.VISIBLE);
                holder.llReceiver.setVisibility(View.GONE);
                holder.rlSender.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/dashboard/get_user_respective_messages?user_id=" +
                            position + "&logged_in_user=" +
                            AppConfig.loginDatatype.getSiteUserId() +
                            "&start=" + position);
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
                    try {
                        userRespectiveMSGDatatypesLazy = new LinkedList<UserRespectiveMSGDatatype>();
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
                            userRespectiveMSGDatatypesLazy.add(userRespectiveMSGDatatype);
                        }
                    } catch (Exception ex) {
                    }
                    Log.i("RESPONSE", jOBJ.toString());
                    Log.i("URL", "http://esolz.co.in/lab6/ptplanner/dashboard/get_user_respective_messages?user_id=" +
                            AppConfig.PT_ID + "&logged_in_user=" +
                            AppConfig.loginDatatype.getSiteUserId() +
                            "&start=" + position);
                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                holder.pbarLoadMore.setVisibility(View.GONE);
                if (exception.equals("")) {
                    holder.llReceiver.setVisibility(View.VISIBLE);
                    holder.rlSender.setVisibility(View.VISIBLE);
                    userRespectiveMSGDatatypes.addAll(0, userRespectiveMSGDatatypesLazy);
                    notifyDataSetChanged();
                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(context, "Server not responding....", Toast.LENGTH_LONG)
                            .show();
                }
            }

        };
        allMSG.execute();

    }

}