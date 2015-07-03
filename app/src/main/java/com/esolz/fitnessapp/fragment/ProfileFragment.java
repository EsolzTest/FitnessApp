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
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumLight;
import com.esolz.fitnessapp.datatype.ProfileViewDataType;

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

    ProfileViewDataType obj_profile;
    String url;
    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    LinearLayout linear_profile;
    ProgressBar pb_profile;
    ImageView image;
    TitilliumBold text_name;
    TitilliumBold text_sub_name;
    TitilliumLight about;
    TitilliumBold location_title;
    TitilliumLight address;
    // ImageView image;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.profile_details, container, false);

        linear_profile = (LinearLayout) fView.findViewById(R.id.linear_profile);

        pb_profile = (ProgressBar) fView.findViewById(R.id.progbar_profile);

        back = (LinearLayout) fView.findViewById(R.id.back);
        fragmentManager = getActivity().getSupportFragmentManager();

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
                // TODO Auto-generated method stub

                Intent intent = new Intent(getActivity(),
                        ChatDetailsFragment.class);
                getActivity().startActivity(intent);

                //finish();
                //fragmentTransaction = fragmentManager.beginTransaction();
                //ChatDetailsFragment chat_fragment = new ChatDetailsFragment();
                //fragmentTransaction.replace(R.id.chat_container, chat_fragment);
                //fragmentTransaction.commit();

            }
        });

        //**************************
        //asyntask("3");

        image = (ImageView) fView.findViewById(R.id.img_pic1);

        text_name = (TitilliumBold) fView.findViewById(R.id.txt_name_profile);

        //text_sub_name=(TitilliumBold)fView.findViewById(R.id.txt_name_sub);
        about = (TitilliumLight) fView.findViewById(R.id.txt_desc);
        //location_title=(TitilliumBold)fView.findViewById(R.id.txt_location_title);
        address = (TitilliumLight) fView.findViewById(R.id.txt_location);

         //asyntask("id");
        new Getdatails().execute();
        return fView;
        //Void asyntask(String id);

    }

    //*************Asynk
    public class Getdatails extends AsyncTask<Void,Void,Void>{



                InputStream is;
                String json;
                JSONArray json_arr;
                //String id;
                JSONObject all_news_list_object;

                @Override
                protected Void doInBackground(Void... Void) {

                    try {


                        DefaultHttpClient httClient = new DefaultHttpClient();
                        // HttpClient client = HttpClientBuilder.create().build();

                        HttpGet http_get = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/user_details?pt_id=3");

                        HttpResponse response = httClient.execute(http_get);

                        HttpEntity httpEntity = response.getEntity();

                        is = httpEntity.getContent();

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(is));

                        StringBuilder sb = new StringBuilder();

                        String line = null;

                        while ((line = reader.readLine()) != null) {

                            sb.append(line + "\n");
                        }

                        is.close();

                        json = sb.toString();
                        all_news_list_object = new JSONObject(json);


                        //data = new LinkedList<MsgDataType>();
                        //String total_data = all_news_list_object.getString("all_user");
                        //json_arr = all_news_list_object.getJSONArray("all_user");

                        obj_profile = new ProfileViewDataType(all_news_list_object.getString("id"), all_news_list_object.getString("user_type"), all_news_list_object.getString("name"), all_news_list_object.getString("image"), all_news_list_object.getString("email"), all_news_list_object.getString("address"), all_news_list_object.getString("company"), all_news_list_object.getString("work_address"), all_news_list_object.getString("billing_address"), all_news_list_object.getString("phone"), all_news_list_object.getString("about"));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }


                @Override
                protected void onPreExecute() {
                    //pb_profile.setVisibility(View.VISIBLE);
                    super.onPreExecute();
                    // Toast.makeText(getActivity(), "Starting...", Toast.LENGTH_LONG).show();
                    pb_profile.setVisibility(View.VISIBLE);
                    linear_profile.setVisibility(View.GONE);
                }


                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pb_profile.setVisibility(View.GONE);

                    try {
                        //Toast.makeText(getActivity(), "" + obj2.getAddress(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_SHORT).show();
                        text_name.setText("" + obj_profile.getName());
                        about.setText("" + obj_profile.getAbout());
                        address.setText("" + obj_profile.getAddress());
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.i("INFO", json);
                    }


                    pb_profile.setVisibility(View.GONE);
                    linear_profile.setVisibility(View.VISIBLE);

                    //**********Setting data

//            Picasso.with(getActivity()).load(obj2.getImage()).fit().centerCrop().placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).
//                    transform(new Trns()).into(image);

                    //          text_name.setText(""+obj2.getName());


                }


            }
        }











