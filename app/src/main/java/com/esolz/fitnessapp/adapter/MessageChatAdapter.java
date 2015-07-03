package com.esolz.fitnessapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.ChatDataType;
import com.esolz.fitnessapp.fragment.ChatDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by su on 18/6/15.
 */
public class MessageChatAdapter extends ArrayAdapter<ChatDataType> {

    Context context;
    LinkedList<ChatDataType> data_chat;
    LayoutInflater inf;
    RelativeLayout Container;
    String sent_by;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ChatDataType obj;
    //EditText ed;
    //String total_d;
    ListView listView;


    protected class ViewHolder {
        TextView message;
        //TextView sender;
        // TextView receiver;
        ImageView sender_image;
        ImageView receiver_image;
        // TextView send_time;

    }


    public MessageChatAdapter(Context context, int resource, LinkedList<ChatDataType> data) {
        super(context, resource, data);

        this.context = context;
        this.data_chat = data;


        //inflator = ((Activity) this.context).getLayoutInflater();
        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
        inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ChatDetailsFragment cd = (ChatDetailsFragment) context;
        int pos = position;


        obj = data_chat.get(position);
        sent_by = obj.getSend_by();
        final ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();


            if (sent_by.equals("15")) {

                convertView = inf.inflate(R.layout.msg_receiver, parent, false);
                Container = (RelativeLayout) convertView.findViewById(R.id.receiver_relative);
                holder.message = (TextView) convertView.findViewById(R.id.chat_send_msg1);
                holder.sender_image = (ImageView) convertView.findViewById(R.id.chat_profile_pic_send1);
                convertView.setTag(holder);
                holder.message.setText(obj.getMessage());
                Picasso.with(context).load(obj.getSender_image()).transform(new Trns()).resize(400, 400).centerInside().placeholder(R.drawable.abc_dialog_material_background_dark).error(R.drawable.abc_dialog_material_background_light).into(holder.sender_image);

                Container.setVisibility(View.VISIBLE);


            } else {

                convertView = inf.inflate(R.layout.msg_receiver, parent, false);
                Container = (RelativeLayout) convertView.findViewById(R.id.receiver_relative);
                holder.message = (TextView) convertView.findViewById(R.id.chat_send_msg);

                holder.receiver_image = (ImageView) convertView.findViewById(R.id.chat_profile_pic_send);


                convertView.setTag(holder);
                obj = data_chat.get(position);
                holder.message.setText(obj.getMessage());

                Picasso.with(context).load(obj.getReceiver_image()).transform(new Trns()).resize(400, 400).centerInside().placeholder(R.drawable.abc_dialog_material_background_dark).error(R.drawable.abc_dialog_material_background_light).into(holder.receiver_image);


                //Container.setVisibility(View.VISIBLE);

            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //obj=data_chat.get(position);
        //TextView receiver=(TextView)convertView.findViewById(R.id.actionbar);
        //receiver.setText(obj.getReceiver());

         int total_d=data_chat.size();


//        if (position == (data_chat.size() - 10) && cd.loading == false && data_chat.size() <= total_d) {
//            cd.loadMore(position + 10);
//
//
//        }
        if(position<total_d)
        {
            cd.loadMore(position + 10);


            cd.loading=false;
        }



            return convertView;
        }



}