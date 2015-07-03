package com.esolz.fitnessapp.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.media.Image;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.internal.widget.TintImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.datatype.Alltriner_Setter_Gette;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

/**
 * Created by su on 18/6/15.
 */
public class Trainer_Adapter extends PagerAdapter {


    Context ctx;
    LinkedList<Alltriner_Setter_Gette> tariner;
    LayoutInflater inflater;
    int Size;
    Alltriner_Setter_Gette cust_data;
    View itemview;


    public Trainer_Adapter(Context context, int i, LinkedList<Alltriner_Setter_Gette> all_trainer_list) {

        this.ctx = context;
        this.tariner = all_trainer_list;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //Size=tariner.size();
        return tariner.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //super.instantiateItem(container, position);

        TitilliumBold t_name;
        TitilliumRegular t_address;



        itemview = inflater.inflate(R.layout.k_triner_viewpager, container, false);


        t_name = (TitilliumBold) itemview.findViewById(R.id.tv_trainer_name);
        t_address = (TitilliumRegular) itemview.findViewById(R.id.tv_trainer_address);
        ImageView pic=(ImageView)itemview.findViewById(R.id.imgv_trainer);
        cust_data = tariner.get(position);

        t_name.setText(cust_data.getPt_name());
        t_address.setText(cust_data.getWorking_address());

       Picasso.with(ctx).load(cust_data.getPt_image()).transform(new Trns()).fit().placeholder(R.drawable.placeholdericon).error(R.drawable.placeholdericon).into(pic);

        // Add viewpager_item.xml to ViewPager

        ((ViewPager) container).addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((View) object);

    }




}
