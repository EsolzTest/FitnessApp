package com.esolz.fitnessapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.datatype.AltrainerDataType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by su on 18/6/15.
 */
public class AllTrainerAdapter extends PagerAdapter {


    Context context;
    ArrayList<AltrainerDataType> altrainerDataTypeArrayList;
    LayoutInflater inflater;
    int Size;
    AltrainerDataType cust_data;
    View itemview;


    public AllTrainerAdapter(Context context, int i, ArrayList<AltrainerDataType> altrainerDataTypeArrayList) {

        this.context = context;
        this.altrainerDataTypeArrayList = altrainerDataTypeArrayList;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //Size=tariner.size();
        return altrainerDataTypeArrayList.size();
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
        ImageView pic = (ImageView) itemview.findViewById(R.id.imgv_trainer);
        cust_data = altrainerDataTypeArrayList.get(position);

        t_name.setText(cust_data.getPt_name());
        t_address.setText(cust_data.getWorking_address());

        Picasso.with(context).load(cust_data.getPt_image()).transform(new Trns()).fit().error(R.drawable.placeholdericon).into(pic);

        // Add viewpager_item.xml to ViewPager

        ((ViewPager) container).addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((View) object);

    }


}
