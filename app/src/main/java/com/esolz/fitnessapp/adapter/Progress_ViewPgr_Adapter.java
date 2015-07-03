package com.esolz.fitnessapp.adapter;

import android.content.Context;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.esolz.fitnessapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * //19th june - code changed by Bodhidipta Bhattacharjee
 //corresponding id modified changed
 //graph is not list view , inflating data on static layout
 */
public class Progress_ViewPgr_Adapter extends PagerAdapter {



    Context mContext;
    View itemView;
    ImageView profile_pic1;
    // static  String url="http://eurotravel360.com/wp-content/themes/Milada/images/no-image-small-square.jpg";
    LayoutInflater mLayoutInflater;
    String obj;

    ArrayList<String> client_images;

    public Progress_ViewPgr_Adapter(Context mContext, ArrayList<String> people_images) {
        this.mContext = mContext;
        this.client_images = people_images;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {

        return client_images.size() ;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container,  int position) {
        itemView = mLayoutInflater.inflate(R.layout.progress_client_image_slider, container, false);
        profile_pic1 = (ImageView) itemView.findViewById(R.id.slider_image);
        obj=client_images.get(position);
        Picasso.with(mContext).load(obj).error(R.drawable.noimage).into(profile_pic1);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
