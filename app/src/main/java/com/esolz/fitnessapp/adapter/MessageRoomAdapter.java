package com.esolz.fitnessapp.adapter;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.HelveticaSemiBoldLight;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.MsgDataType;
import com.esolz.fitnessapp.fragment.ChatDetailsFragment;
import com.squareup.picasso.Picasso;


public class MessageRoomAdapter extends ArrayAdapter<MsgDataType> {

    Context context;
    LinkedList<MsgDataType> msgDataTypeLinkedList;
    LayoutInflater inflator;
    int posi;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Calendar calendar;
    Date date;

    SimpleDateFormat dateFormatCurrentDate, dateFormat, targetFormat, jsonDateFormat;


    protected class ViewHolder {
        TitilliumSemiBold currenttime, username;
        TitilliumRegular usermessage;
        ImageView image;
        LinearLayout listItemContainer;
        HelveticaSemiBoldLight chatCounter;
    }

    public MessageRoomAdapter(Context context, int resource, LinkedList<MsgDataType> msgDataTypeLinkedList) {
        super(context, resource, msgDataTypeLinkedList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.msgDataTypeLinkedList = msgDataTypeLinkedList;

        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;
        posi = position;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.messages_list_items, parent, false);
            holder.listItemContainer = (LinearLayout) convertView.findViewById(R.id.container);
            holder.username = (TitilliumSemiBold) convertView.findViewById(R.id.user_name);
            holder.currenttime = (TitilliumSemiBold) convertView.findViewById(R.id.current_time);
            holder.usermessage = (TitilliumRegular) convertView.findViewById(R.id.user_message);
            holder.image = (ImageView) convertView.findViewById(R.id.imageview);

            holder.chatCounter = (HelveticaSemiBoldLight) convertView.findViewById(R.id.imageviewreddotnumberofchat);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.username.setText(msgDataTypeLinkedList.get(position).getUser_name());
        try {
            holder.usermessage.setText((URLDecoder.decode(msgDataTypeLinkedList.get(position).getLast_message(), "UTF-8")));
        } catch (Exception e) {
            Log.i("Receiver MSG : ", e.toString());
        }
        //holder.usermessage.setText(msgDataTypeLinkedList.get(position).getLast_message());

        Picasso.with(context).load(msgDataTypeLinkedList.get(position).getUser_image()).transform(new Trns()).resize(400, 400).centerInside().placeholder(R.drawable.abc_dialog_material_background_dark) // optional
                .error(R.drawable.abc_dialog_material_background_light).into(holder.image);

        dateFormatCurrentDate = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");
        targetFormat = new SimpleDateFormat("hh:mm a");
        jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date dateCheck  = null;

        try {
            dateCheck = new Date();
            date = jsonDateFormat.parse(msgDataTypeLinkedList.get(position).getLast_send_time());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (jsonDateFormat.format(dateCheck)
                    .equals(jsonDateFormat.parse(msgDataTypeLinkedList.get(position).getLast_send_time()))) {
                holder.currenttime.setText(targetFormat.format(date));
            } else {
                holder.currenttime.setText(dateFormat.format(date));
            }
        } catch (Exception e) {
            Log.i("Date exception : ", e.toString());
        }

        holder.listItemContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(context, ChatDetailsFragment.class);
                i.putExtra("msgUserName", msgDataTypeLinkedList.get(position).getUser_name());
                i.putExtra("msgUserId", msgDataTypeLinkedList.get(position).getUser_id());
                context.startActivity(i);
            }
        });

        holder.chatCounter.setVisibility(View.GONE);

        return convertView;
    }

}
