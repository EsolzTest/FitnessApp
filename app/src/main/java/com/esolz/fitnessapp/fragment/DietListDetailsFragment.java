package com.esolz.fitnessapp.fragment;

import android.net.ConnectivityManager;
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
import android.widget.ScrollView;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.DietAdapter;
import com.esolz.fitnessapp.customviews.TitilliumBold;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.DietDataType;
import com.esolz.fitnessapp.datatype.DietDetailDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;
import com.esolz.fitnessapp.helper.ObservableScrollView;
import com.esolz.fitnessapp.helper.ScrollViewListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class DietListDetailsFragment extends Fragment implements ScrollViewListener {

    LinearLayout backDiet;
    ObservableScrollView mainS;
    ScrollView child;
    DietDetailDataType dietDetailDataType;

    View fView;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    TitilliumBold title;
    TitilliumSemiBold txtTitle;
    TitilliumRegular description;
    ImageView mealimage;
    LinearLayout llContainer;
    ProgressBar pBar;

    ConnectionDetector cd;
    String exception = "", urlResponse = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_dietlist_details, container, false);
        title = (TitilliumBold) fView.findViewById(R.id.details_title);
        description = (TitilliumRegular) fView.findViewById(R.id.details_desc);
        mealimage = (ImageView) fView.findViewById(R.id.meal_image);
        llContainer = (LinearLayout) fView.findViewById(R.id.container);
        pBar = (ProgressBar) fView.findViewById(R.id.pbar);
        backDiet = (LinearLayout) fView.findViewById(R.id.back_diet);
        txtTitle = (TitilliumSemiBold) fView.findViewById(R.id.txt_title);

        fragmentManager = getActivity().getSupportFragmentManager();
        cd = new ConnectionDetector(getActivity());

        if (cd.isConnectingToInternet()) {
            getDietDetails();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        backDiet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                fragmentTransaction = fragmentManager.beginTransaction();
                DietFragment diet_fragment = new DietFragment();
                fragmentTransaction.replace(R.id.fragment_container,
                        diet_fragment);
                fragmentTransaction.commit();
            }
        });

        return fView;
    }

    public void getDietDetails() {

        AsyncTask<Void, Void, Void> dietListDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
                llContainer.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_custom_meal_details?custom_meal_id=" + getArguments().getString("CustomMealID")
                            + "&client_id=" + AppConfig.loginDatatype.getSiteUserId()
                            + "&meal_id=" + getArguments().getString("MealID"));
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
                    dietDetailDataType = new DietDetailDataType(
                            jOBJ.getString("meal_title"),
                            jOBJ.getString("meal_image"),
                            jOBJ.getString("meal_description")
                    );


                    Log.d("Diet URL : ", "http://esolz.co.in/lab6/ptplanner/app_control/get_custom_meal_details?custom_meal_id=" + getArguments().getString("CustomMealID")
                            + "&client_id=" + AppConfig.loginDatatype.getSiteUserId()
                            + "&meal_id=" + getArguments().getString("MealID"));

                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);
                if (exception.equals("")) {
                    llContainer.setVisibility(View.VISIBLE);

                    txtTitle.setText(dietDetailDataType.getMeal_title());
                    title.setText(dietDetailDataType.getMeal_title());
                    description.setText(dietDetailDataType.getMeal_description());
                    Picasso.with(getActivity()).load(dietDetailDataType.getMeal_image()).fit().into(mealimage);
                } else {
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        dietListDetails.execute();

    }


    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (oldx > x) {

        } else {
            // System.out.println("Scroll value : " + y);
            if (y > 448) {
            } else {
            }
            child.scrollTo(x, y / 2);
            // header.setAlpha((float) y / 1200);
        }
    }


}
