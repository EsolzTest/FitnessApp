package com.esolz.fitnessapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.TrainingDataType;

public class TrainingAdapter extends ArrayAdapter<TrainingDataType> {

	Context context;
	LinkedList<TrainingDataType> data;
	LayoutInflater inflator;
	TrainingDataType obj;
	LayoutInflater listItemContainer;

	protected class ViewHolder {

		TextView settxt;
		TextView reptxt;
		TextView kgtxt;
		ImageView chktxt;
	}


	public TrainingAdapter(Context context, int resource,
			LinkedList<TrainingDataType> objects) {
		super(context, resource,objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = objects;
		inflator = ((Activity) this.context).getLayoutInflater();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null)

		{
			convertView = inflator.inflate(R.layout.training_item, parent, false);

			holder = new ViewHolder();
			holder.settxt=(TextView)convertView.findViewById(R.id.settext);
			holder.reptxt=(TextView)convertView.findViewById(R.id.reptext);
			holder.kgtxt=(TextView)convertView.findViewById(R.id.kgtext);
			holder.chktxt=(ImageView)convertView.findViewById(R.id.chktext);


		}
		else {

			holder = (ViewHolder) convertView.getTag();
		}

		obj=data.get(position);

		holder.settxt.setText(obj.getReps());
		holder.kgtxt.setText(obj.getKg());



		return convertView;
	}

}
