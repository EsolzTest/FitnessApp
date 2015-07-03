package com.esolz.fitnessapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.DietDataType;
import com.esolz.fitnessapp.fragment.DietListDetailsFragment;
import com.squareup.picasso.Picasso;

public class DietAdapter extends ArrayAdapter<DietDataType> {

	Context context;
	LinkedList<DietDataType> data;
	LayoutInflater inflator;
	RelativeLayout listitemContainer;
	DietDataType obj;
	FragmentTransaction fragmentTransaction;
	FragmentManager fragmentManager;


	protected class ViewHolder {
		//TextView mealid;
		//TextView custommealid;
		TextView mealtitle;
		TextView mealdescription;
		ImageView mealimage;
	}

	public DietAdapter(Context context, int resource,
			LinkedList<DietDataType> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data=objects;
		inflator = ((Activity) this.context).getLayoutInflater();
		fragmentManager = ((FragmentActivity) this.context)
				.getSupportFragmentManager();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflator.inflate(R.layout.diet_item, parent, false);
			listitemContainer = (RelativeLayout) convertView
					.findViewById(R.id.listitem_container);
			holder = new ViewHolder();
			holder.mealtitle=(TextView)convertView.findViewById(R.id.title);
			holder.mealdescription=(TextView)convertView.findViewById(R.id.itemDetail);
			//holder.mealimage=(ImageView)convertView.findViewById(R.id.image1);
		}

		else {

			holder = (ViewHolder) convertView.getTag();
		}

		obj=data.get(position);

//		holder.mealtitle.setText(obj.getMeal_title());
//		holder.mealdescription.setText(obj.getMeal_description());
//		Picasso.with(context).load(obj.getMeal_image()).transform(new Trns
//				()).resize(400, 400).centerInside().placeholder(R.drawable.abc_dialog_material_background_dark) // optional
//				.error(R.drawable.abc_dialog_material_background_light)  .into(holder.mealimage);

		listitemContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
				fragmentTransaction = fragmentManager.beginTransaction();
				DietListDetailsFragment dietList_fragment = new DietListDetailsFragment();

				fragmentTransaction.replace(R.id.fragment_container,
						dietList_fragment);
				fragmentTransaction.commit();
			}
		});

		return convertView;
	}

}
