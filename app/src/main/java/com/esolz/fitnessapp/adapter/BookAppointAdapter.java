package com.esolz.fitnessapp.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
;
import java.util.LinkedList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.TimeSlotsDataType;
import com.esolz.fitnessapp.datatype.TrainerBookingDate;
import com.esolz.fitnessapp.datatype.TrainerBookingDetailsDataType;
import com.esolz.fitnessapp.datatype.TrainerDetails;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookAppointAdapter extends ArrayAdapter<TimeSlotsDataType> {

    Context context;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    ArrayList<TimeSlotsDataType> trainerBookingDetailsDataTypeArrayList;
    ArrayList<TimeSlotsDataType> timeSlotsDataTypeArrayList;
    ConnectionDetector connectionDetector;
    String exception = "", urlResponse = "";
    ProgressDialog progressDialog;

    public BookAppointAdapter(Context context, int resource, ArrayList<TimeSlotsDataType> timeSlotsDataTypeArrayList) {
        super(context, resource, timeSlotsDataTypeArrayList);
        this.context = context;
        this.timeSlotsDataTypeArrayList = timeSlotsDataTypeArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        connectionDetector = new ConnectionDetector(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)

        {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.book_app_list, parent, false);

            holder.llBookingStatus = (LinearLayout) convertView.findViewById(R.id.ll_booking_status);
            holder.txtTimeing = (TitilliumRegular) convertView.findViewById(R.id.txt_timeing);
            holder.txtApt = (TitilliumBold) convertView.findViewById(R.id.txt_apt);
            holder.txtStatus = (TitilliumSemiBold) convertView.findViewById(R.id.txt_status);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTimeing.setText(timeSlotsDataTypeArrayList.get(position).getSlot_start()
                + " - " + timeSlotsDataTypeArrayList.get(position).getSlot_end());
        holder.txtApt.setText(timeSlotsDataTypeArrayList.get(position).getCounter());

        if (timeSlotsDataTypeArrayList.get(position).getStatusDependent().equals("NB")) {
            holder.llBookingStatus.setVisibility(View.VISIBLE);
            holder.llBookingStatus.setBackgroundResource(R.drawable.book_button);
            holder.txtStatus.setText("BOOK");
            holder.txtStatus.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (timeSlotsDataTypeArrayList.get(position).getStatusDependent().equals("Ex")) {
            holder.llBookingStatus.setVisibility(View.GONE);
        } else {
            holder.llBookingStatus.setVisibility(View.VISIBLE);
            holder.llBookingStatus.setBackgroundResource(R.drawable.booked_button);
            holder.txtStatus.setText("BOOKED");
            holder.txtStatus.setTextColor(Color.parseColor("#22A7F0"));
        }

        holder.llBookingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    addBooking(timeSlotsDataTypeArrayList.get(position).getTrainer_id(),
                            timeSlotsDataTypeArrayList.get(position).getBooking_date(),
                            timeSlotsDataTypeArrayList.get(position).getSlot_start(),
                            timeSlotsDataTypeArrayList.get(position).getSlot_end(),
                            position);
                } else {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        return convertView;
    }


    public class ViewHolder {
        TitilliumRegular txtTimeing;
        TitilliumBold txtApt;
        TitilliumSemiBold txtStatus;
        LinearLayout llBookingStatus;
    }

    public void addBooking(final String trainerId, final String date, final String slotStart, final String slotEnd, final int position) {

        AsyncTask<Void, Void, Void> booking = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/add_booking?trainer_id=" + trainerId
                            + "&client_id=" + AppConfig.loginDatatype.getSiteUserId()
                            + "&booking_date=" + date
                            + "&slot_start=" + slotStart
                            + "&slot_end=" + slotEnd);
                    HttpResponse response;
                    response = httpclient.execute(httpget);
                    HttpEntity httpentity = response.getEntity();
                    InputStream is = httpentity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    urlResponse = sb.toString();
                    JSONObject jOBJ = new JSONObject(urlResponse);


                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/add_booking?trainer_id=" + trainerId
                        + "&client_id=" + AppConfig.loginDatatype.getSiteUserId()
                        + "&booking_date=" + date
                        + "&slot_start=" + slotStart
                        + "&slot_end=" + slotEnd);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progressDialog.dismiss();
                if (exception.equals("")) {
                    timeSlotsDataTypeArrayList.get(position).setStatusDependent("B");
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        booking.execute();

    }

}