package com.esolz.fitnessapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.SendDataType;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

/**
 * Created by su on 25/6/15.
 */
public class SendAdapter extends ArrayAdapter<SendDataType>{

    RelativeLayout Container;
    LinkedList<SendDataType> send_data_chat;
    Context context;
    LayoutInflater chat;
    SendDataType send_chat;


    TextView message;
    ImageView senderimage;
    ImageView receiverimage;

    @Override
    public void add(SendDataType object) {
        send_data_chat.add(object);
        super.add(object);
    }


    public SendAdapter(Context context, int resource, LinkedList<SendDataType> data_chat) {

        super(context, resource, data_chat);
        this.send_data_chat = data_chat;
        this.context = context;
        chat = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public SendAdapter(Context context, int resource) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        send_chat=send_data_chat.get(position);



        if (send_data_chat.equals("14")){

            convertView = chat.inflate(R.layout.msg_receiver, parent, false);
            Container = (RelativeLayout) convertView.findViewById(R.id.receiver_relative);
            message = (TextView) convertView.findViewById(R.id.chat_send_msg1);
            senderimage = (ImageView) convertView.findViewById(R.id.chat_profile_pic_send1);

            message.setText(send_chat.getMessage());
            Picasso.with(context).load(send_chat.getSender_image()).transform(new Trns()).resize(400, 400).centerInside().placeholder(R.drawable.abc_dialog_material_background_dark).error(R.drawable.abc_dialog_material_background_light).into(senderimage);

        }else {

            convertView = chat.inflate(R.layout.msg_receiver, parent, false);
            Container = (RelativeLayout) convertView.findViewById(R.id.receiver_relative);
            message = (TextView) convertView.findViewById(R.id.chat_send_msg);

            receiverimage = (ImageView) convertView.findViewById(R.id.chat_profile_pic_send);

            send_chat = send_data_chat.get(position);
            message.setText(send_chat.getMessage());

            Picasso.with(context).load(send_chat.getReceiver_image()).transform(new Trns()).resize(400, 400).centerInside().placeholder(R.drawable.abc_dialog_material_background_dark).error(R.drawable.abc_dialog_material_background_light).into(receiverimage);
        }





        return convertView;
    }
}

