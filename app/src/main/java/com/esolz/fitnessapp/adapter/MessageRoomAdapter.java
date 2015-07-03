package com.esolz.fitnessapp.adapter;

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
import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.MsgDataType;
import com.esolz.fitnessapp.fragment.ChatDetailsFragment;
import com.squareup.picasso.Picasso;


public class MessageRoomAdapter extends ArrayAdapter<MsgDataType> {

	Context context;
	LinkedList<MsgDataType> data;
	LayoutInflater inflator;
	LinearLayout listItemContainer;
	int posi;
	FragmentTransaction fragmentTransaction;
	FragmentManager fragmentManager;
	MsgDataType obj;


	protected class ViewHolder {
		TextView username;
		TextView currenttime;
		TextView usermessage;
		ImageView image;
	}


	public MessageRoomAdapter(Context context, int resource, LinkedList<MsgDataType> objects) {
		super(context, resource,objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = objects;


		//inflator = ((Activity) this.context).getLayoutInflater();
		fragmentManager = ((FragmentActivity) this.context)
				.getSupportFragmentManager();
		inflator=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder;
		posi=position;

		if (convertView == null) {
			convertView = inflator.inflate(R.layout.messages_list_items,
					parent, false);
			listItemContainer = (LinearLayout) convertView
					.findViewById(R.id.container);
			holder = new ViewHolder();
			holder.username=(TextView)convertView.findViewById(R.id.user_name);
			holder.currenttime=(TextView)convertView.findViewById(R.id.current_time);
			holder.usermessage=(TextView)convertView.findViewById(R.id.user_message);
			holder.image=(ImageView)convertView.findViewById(R.id.imageview);
			convertView.setTag(holder);

		}else {

			holder = (ViewHolder) convertView.getTag();
		}

		obj=data.get(position);


		holder.username.setText(obj.getUser_name());
		holder.currenttime.setText(obj.getLast_send_time());
		holder.usermessage.setText(obj.getLast_message());
		//holder.image.setBackgroundResource(R.drawable.pic5);

		Picasso.with(context).load(obj.getUser_image()).transform(new Trns()).resize(400, 400).centerInside().placeholder(R.drawable.abc_dialog_material_background_dark) // optional
				.error(R.drawable.abc_dialog_material_background_light)  .into(holder.image);


		Calendar cal =Calendar.getInstance(Locale.getDefault());
		int CurrentDate=cal.get(Calendar.DATE);
		int CurrentDay=cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		int CurrentMonth=(cal.get(Calendar.MONTH)+1);
		int CurrentYear=cal.get(Calendar.YEAR);
		int CurrentTime=cal.get(Calendar.HOUR);
		int CurrentTime1=cal.get(Calendar.MINUTE);
		Log.d("CurrentTime", CurrentDate + " " + CurrentTime + " " + CurrentTime1);



		Date dt=new Date();
		DateFormat df=new SimpleDateFormat("HH");
		String h=df.format(dt);


        String abc=obj.getLast_send_time().substring(0, 10);
		if(abc.equals(CurrentYear + "-" + CurrentMonth + "-" + CurrentDate))
		{String xyz=  obj.getLast_send_time().substring(11, 13);
			int difftime=Integer.parseInt(h)-Integer.parseInt(xyz);
			     	holder.currenttime.setText(difftime+"Hour ago");

		}else {
			String xyz=obj.getLast_send_time().substring(8,10);
			int diff_Day=CurrentDate-Integer.parseInt(xyz);
			String mon=CurrentYear+" "+CurrentMonth+" "+CurrentDay+" ";


			holder.currenttime.setText(diff_Day+"Days ago" );

		}










		listItemContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, ChatDetailsFragment.class);
				i.putExtra("name", obj.getUser_name());
				i.putExtra("id",obj.getUser_id());
				context.startActivity(i);


			}
		});



		return convertView;
	}

}
