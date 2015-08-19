package com.esolz.fitnessapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumLight;
import com.esolz.fitnessapp.datatype.ProfileViewDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ProfileFragment extends Fragment {

    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton,
            back;
    RelativeLayout llMessagebutton;

    ProfileViewDataType profileViewDataType;
    String url;
    View fView;

    ImageView imgPic;
    TitilliumBold txtName;
    TitilliumLight txtNameSub, txtDesc, txtEmail, txtLocationWork, txtLocationBilling;
    ScrollView scrlBody;
    ProgressBar progBar;

    ConnectionDetector cd;
    String exception = "", urlResponse = "";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.profile_details, container, false);
        cd = new ConnectionDetector(getActivity());

        back = (LinearLayout) fView.findViewById(R.id.back);
        imgPic = (ImageView) fView.findViewById(R.id.img_pic);
        txtName = (TitilliumBold) fView.findViewById(R.id.txt_name);
        txtNameSub = (TitilliumLight) fView.findViewById(R.id.txt_name_sub);
        scrlBody = (ScrollView) fView.findViewById(R.id.scrl_body);
        txtDesc = (TitilliumLight) fView.findViewById(R.id.txt_desc);
        txtEmail = (TitilliumLight) fView.findViewById(R.id.txt_email);
        txtLocationWork = (TitilliumLight) fView.findViewById(R.id.txt_location_work);
        txtLocationBilling = (TitilliumLight) fView.findViewById(R.id.txt_location_billing);
        progBar = (ProgressBar) fView.findViewById(R.id.prog_bar);

        if (cd.isConnectingToInternet()) {
            getProfileDetails(AppConfig.PT_ID);
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        llCalenderButton = (LinearLayout) getActivity().findViewById(
                R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(
                R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(
                R.id.progressbutton);
        llMessagebutton = (RelativeLayout) getActivity().findViewById(
                R.id.messagebutton);
        llCalenderButton.setClickable(true);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(false);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    String val = getArguments().getString("AllTrainer");
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    BookAppointmentFragment bookapnt_fragment = new BookAppointmentFragment();
                    fragmentTransaction.replace(R.id.fragment_container, bookapnt_fragment);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    Intent intent = new Intent(getActivity(), ChatDetailsFragment.class);
                    intent.putExtra("msgUserName", AppConfig.PT_NAME);
                    intent.putExtra("msgUserId", AppConfig.PT_ID);
                    getActivity().startActivity(intent);
                }
            }
        });

        return fView;
    }

    public void getProfileDetails(final String pt_id) {

        AsyncTask<Void, Void, Void> profileDetais = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progBar.setVisibility(View.VISIBLE);
                scrlBody.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/user_details?pt_id=" + pt_id);
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
                    profileViewDataType = new ProfileViewDataType(
                            jOBJ.getString("id"),
                            jOBJ.getString("user_type"),
                            jOBJ.getString("name"),
                            jOBJ.getString("image"),
                            jOBJ.getString("email"),
                            jOBJ.getString("address"),
                            jOBJ.getString("company"),
                            jOBJ.getString("work_address"),
                            jOBJ.getString("billing_address"),
                            jOBJ.getString("phone"),
                            jOBJ.getString("about"));

                    Log.d("RESPONSE", jOBJ.toString());
                } catch (Exception e) {
                    exception = e.toString();
                }
                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/user_details?pt_id=" + pt_id);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    scrlBody.setVisibility(View.VISIBLE);

                    txtName.setText(profileViewDataType.getName());
                    txtNameSub.setText(profileViewDataType.getCompany());
                    txtDesc.setText(profileViewDataType.getAbout());
                    txtEmail.setText(profileViewDataType.getEmail());
                    txtLocationWork.setText(profileViewDataType.getWork_address());
                    txtLocationBilling.setText(profileViewDataType.getBilling_address());

                    Picasso.with(getActivity()).load(profileViewDataType.getImage()).fit().transform(new Trns()).into(imgPic);
                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        profileDetais.execute();

    }
//
//    //*************Asynk
//    public class Getdatails extends AsyncTask<Void, Void, Void> {
//
//
//        InputStream is;
//        String json;
//        JSONArray json_arr;
//        //String id;
//        JSONObject all_news_list_object;
//
//        @Override
//        protected Void doInBackground(Void... Void) {
//
//            try {
//
//
//                DefaultHttpClient httClient = new DefaultHttpClient();
//                // HttpClient client = HttpClientBuilder.create().build();
//
//                HttpGet http_get = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/user_details?pt_id=3");
//
//                HttpResponse response = httClient.execute(http_get);
//
//                HttpEntity httpEntity = response.getEntity();
//
//                is = httpEntity.getContent();
//
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(is));
//
//                StringBuilder sb = new StringBuilder();
//
//                String line = null;
//
//                while ((line = reader.readLine()) != null) {
//
//                    sb.append(line + "\n");
//                }
//
//                is.close();
//
//                json = sb.toString();
//                all_news_list_object = new JSONObject(json);
//
//
//                //data = new LinkedList<MsgDataType>();
//                //String total_data = all_news_list_object.getString("all_user");
//                //json_arr = all_news_list_object.getJSONArray("all_user");
//
//                profileViewDataType = new ProfileViewDataType(
//                        all_news_list_object.getString("id"),
//                        all_news_list_object.getString("user_type"),
//                        all_news_list_object.getString("name"),
//                        all_news_list_object.getString("image"),
//                        all_news_list_object.getString("email"),
//                        all_news_list_object.getString("address"),
//                        all_news_list_object.getString("company"),
//                        all_news_list_object.getString("work_address"),
//                        all_news_list_object.getString("billing_address"),
//                        all_news_list_object.getString("phone"),
//                        all_news_list_object.getString("about"));
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            //pb_profile.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//            // Toast.makeText(getActivity(), "Starting...", Toast.LENGTH_LONG).show();
//            //  pb_profile.setVisibility(View.VISIBLE);
//            //  linear_profile.setVisibility(View.GONE);
//        }
//
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            //  pb_profile.setVisibility(View.GONE);
//
//            try {
//                //Toast.makeText(getActivity(), "" + obj2.getAddress(), Toast.LENGTH_SHORT).show();
//                //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_SHORT).show();
//                //  text_name.setText("" + obj_profile.getName());
//                //// about.setText("" + obj_profile.getAbout());
//                //  address.setText("" + obj_profile.getAddress());
//            } catch (Exception e) {
//                Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
//                Log.i("INFO", json);
//            }
//
//
//            // pb_profile.setVisibility(View.GONE);
//            // linear_profile.setVisibility(View.VISIBLE);
//
//            //**********Setting data
//
////            Picasso.with(getActivity()).load(obj2.getImage()).fit().centerCrop().placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).
////                    transform(new Trns()).into(image);
//
//            //          text_name.setText(""+obj2.getName());
//
//
//        }
//
//
//    }
}








