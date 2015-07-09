package com.esolz.fitnessapp.adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumLight;
import com.esolz.fitnessapp.datatype.AppointmentListDataType;
import com.esolz.fitnessapp.fragment.AppointmantFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ltp on 06/07/15.
 */
public class AppointmentListAdapter extends ArrayAdapter<AppointmentListDataType> {
    Context context;
    ArrayList<AppointmentListDataType> appointmentListDataTypeArrayList;
    LayoutInflater inflator;
    ViewHolder holder;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bundle bundle;
    String dateRequired;

    public AppointmentListAdapter(Context context, int resource,
                                  ArrayList<AppointmentListDataType> appointmentListDataTypeArrayList, String dateRequired) {
        super(context, resource, appointmentListDataTypeArrayList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.appointmentListDataTypeArrayList = appointmentListDataTypeArrayList;
        this.dateRequired = dateRequired;
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        fragmentManager = ((FragmentActivity) this.context)
                .getSupportFragmentManager();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.applistitem, parent, false);
            holder = new ViewHolder();

            holder.llContainer = (LinearLayout) convertView.findViewById(R.id.container);
            holder.txtTitle = (TitilliumLight) convertView.findViewById(R.id.txt_title);
            holder.txtDate = (TitilliumLight) convertView.findViewById(R.id.txt_date);
            holder.txtTo = (TitilliumLight) convertView.findViewById(R.id.txt_to);
            holder.txtFrom = (TitilliumLight) convertView.findViewById(R.id.txt_from);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d("Adapter Size", "" + appointmentListDataTypeArrayList.size());

        holder.txtTitle.setText(appointmentListDataTypeArrayList.get(position).getTrainer_name());
        holder.txtTo.setText(appointmentListDataTypeArrayList.get(position).getBooking_time_start());
        holder.txtFrom.setText(appointmentListDataTypeArrayList.get(position).getBooking_time_end());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(appointmentListDataTypeArrayList.get(position).getBooked_date());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE MMM dd, yyyy");
        String finalDate = timeFormat.format(myDate);
        holder.txtDate.setText("" + finalDate);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("BOOKID", appointmentListDataTypeArrayList.get(position).getId());
//                bundle.putString("PAGE", "LIST");
//                bundle.putString("Date", dateRequired);
                fragmentTransaction = fragmentManager.beginTransaction();
                AppointmantFragment app_fragment = new AppointmantFragment();
                app_fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,
                        app_fragment);
                fragmentTransaction.commit();
            }
        });

        return convertView;
    }

    protected class ViewHolder {
        TitilliumLight txtTitle, txtDate, txtTo, txtFrom;
        LinearLayout llContainer;
    }

}

