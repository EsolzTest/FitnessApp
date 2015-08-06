package com.esolz.fitnessapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.datatype.DietDataType;
import com.esolz.fitnessapp.fragment.DietListDetailsFragment;
import com.squareup.picasso.Picasso;

public class DietAdapter extends ArrayAdapter<DietDataType> {

    Context context;
    LinkedList<DietDataType> data;
    LayoutInflater inflator;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;
    ViewHolder holder;

    public DietAdapter(Context context, int resource, LinkedList<DietDataType> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = objects;
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.diet_item, parent, false);
            holder = new ViewHolder();

            holder.listitemContainer = (LinearLayout) convertView.findViewById(R.id.list_container);
            holder.imgDiet = (ImageView) convertView.findViewById(R.id.img_diet);
            holder.dietTitle = (TitilliumBold) convertView.findViewById(R.id.diet_title);
            holder.dietSubTitle = (TitilliumRegular) convertView.findViewById(R.id.diet_sub_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(data.get(position).getMeal_image()).fit().centerCrop().into(holder.imgDiet);
        holder.dietTitle.setText(data.get(position).getMeal_title());
        holder.dietSubTitle.setText(data.get(position).getMeal_description());

        holder.listitemContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bundle = new Bundle();
                bundle.putString("CustomMealID", data.get(position).getCustom_meal_id());
                bundle.putString("MealID", data.get(position).getMeal_id());

                fragmentTransaction = fragmentManager.beginTransaction();
                DietListDetailsFragment dietList_fragment = new DietListDetailsFragment();
                dietList_fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, dietList_fragment);
                fragmentTransaction.commit();
            }
        });

        return convertView;
    }

    protected class ViewHolder {
        LinearLayout listitemContainer;
        ImageView imgDiet;
        TitilliumBold dietTitle;
        TitilliumRegular dietSubTitle;
    }

}
