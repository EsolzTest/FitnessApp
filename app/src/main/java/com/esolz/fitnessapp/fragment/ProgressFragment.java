package com.esolz.fitnessapp.fragment;



import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.HelveticaSemiBold;
import com.esolz.fitnessapp.customviews.HelveticaSemiBoldLight;
import com.esolz.fitnessapp.datatype.Graph_Client_Images;
import com.esolz.fitnessapp.datatype.Graph_client_Goal_Images;
import com.esolz.fitnessapp.datatype.Graph_client_allGraphs;
import com.esolz.fitnessapp.datatype.Graph_client_details;
import com.esolz.fitnessapp.fitness.LandScreenActivity;
import com.esolz.fitnessapp.fitness.ProgressGraphView;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class ProgressFragment extends Fragment {

    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, llGrphdetailsList;
    RelativeLayout llMessagebutton;
    LinearLayout allGraph;
    String execption = "";

    //18th june - code changed by Bodhidipta Bhattacharjee
    FragmentManager fmanage;
    ImageView profile_pic_thumb;
    ImageView current_pic;
    ImageView goal_pic;


    HelveticaSemiBoldLight current_pic_date;
    HelveticaSemiBold first_name;
    HelveticaSemiBoldLight second_name;
    HelveticaSemiBoldLight age_in_years;
    HelveticaSemiBoldLight height_of_client;
    HelveticaSemiBoldLight weight_of_client;
    public boolean isloading_client_details = false;
    final String url_client_details = "http://esolz.co.in/lab6/ptplanner/app_control/get_client_details?client_id=13";
    final String url_client_Images = "http://esolz.co.in/lab6/ptplanner/app_control/get_client_images?client_id=13";
    final String url_client_det_graph = "http://esolz.co.in/lab6/ptplanner/app_control/all_graphs?client_id=13";

    Graph_client_details client_details;
    Graph_client_Goal_Images client_goal_image;

    Graph_client_allGraphs client_all_graph;
    LinkedList<Graph_Client_Images> client_current_image_list = new LinkedList<Graph_Client_Images>();
    LinkedList<Graph_client_allGraphs> client_all_graphs_list = new LinkedList<Graph_client_allGraphs>();


    LayoutInflater inflator;

    ProgressBar progressBar;
//22jun -changes have been done in frag_progress.xml page
//frag_progress has dynamic view in the linear layout under the type-goal-deadline  tag..
//new xml holding textviews named as-->prgress_graph_det_list
    //backup views are saved on back_up_progress_graph_details.xml
    //---------changes made by bodhidipta bhattacharjee


    //code end-changes done by bodhidipta bhattacharjee


    View fView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_progress, container, false);
        fView.setVisibility(View.INVISIBLE);
        allGraph = (LinearLayout) fView.findViewById(R.id.all_graph);
//22jun changes done by- bodhidipta bhattacharjee
        inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //************************************infalting progress bar
        ViewGroup layout = (ViewGroup) getActivity().findViewById(android.R.id.content).getRootView();

        progressBar = new ProgressBar(getActivity(),null);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(getActivity());

        rl.setGravity(Gravity.CENTER);
        rl.addView(progressBar);

        layout.addView(rl,params);
        //************************************


        //end here

        fmanage = getFragmentManager();
        allGraph.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), ProgressGraphView.class);
                startActivity(intent);
            }
        });


        //18th june - code changed by Bodhidipta Bhattacharjee
        //layout id has been changed by id
        profile_pic_thumb = (ImageView) fView.findViewById(R.id.image_thumb);
        first_name = (HelveticaSemiBold) fView.findViewById(R.id.first_name);
        second_name = (HelveticaSemiBoldLight) fView.findViewById(R.id.last_name);
        age_in_years = (HelveticaSemiBoldLight) fView.findViewById(R.id.age_years);
        height_of_client = (HelveticaSemiBoldLight) fView.findViewById(R.id.height_of_client);
        weight_of_client = (HelveticaSemiBoldLight) fView.findViewById(R.id.weight_of_client);
        current_pic = (ImageView) fView.findViewById(R.id.current_picture);
        current_pic_date = (HelveticaSemiBoldLight) fView.findViewById(R.id.current_picture_date);
        goal_pic = (ImageView) fView.findViewById(R.id.goal_picture);

        llGrphdetailsList = (LinearLayout) fView.findViewById(R.id.Grph_list_details);

        //code end


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
        llProgressButton.setClickable(false);
        llMessagebutton.setClickable(true);

        load_client_details();

        current_pic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                LandScreenActivity land = (LandScreenActivity) getActivity();
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < client_current_image_list.size(); i++) {
                    list.add(client_current_image_list.get(i).getImage_link());
                }
                land.Show_FullScreen_ViewPager(land, list);



            }
        });

        goal_pic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LandScreenActivity land = (LandScreenActivity) getActivity();
                ArrayList<String> list = new ArrayList<String>();
                list.add(client_goal_image.getImage());
                land.Show_FullScreen_ViewPager(land, list);
            }
        });

        return fView;
    }


    //18th june - code changed by Bodhidipta Bhattacharjee
    //corresponding id modified changed
    //graph is not list view , inflating data on static layout

    public void load_client_details() {

        new AsyncTask<Void, Void, Void>() {
            InputStream is;
            String json;
            JSONObject all_graph_list_object;

            @Override
            protected void onPreExecute() {
                //super.onPreExecute();


                getActivity().findViewById(R.id.calenderbutton).setClickable(false);
                getActivity().findViewById(R.id.blockappoinmentbutton).setClickable(false);
                getActivity().findViewById(R.id.progressbutton).setClickable(false);
                getActivity().findViewById(R.id.messagebutton).setClickable(false);

                //Toast.makeText(getActivity(),"Loading user details",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    isloading_client_details = true;


                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url_client_details);
                    HttpResponse response = httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();


                    all_graph_list_object = new JSONObject(json);


                    //get data from client details url
                    String id = all_graph_list_object.getString("id");
                    String user_type = all_graph_list_object.getString("user_type");
                    String name = all_graph_list_object.getString("name");
                    String image = all_graph_list_object.getString("image");
                    String email = all_graph_list_object.getString("email");
                    String address = all_graph_list_object.getString("address");
                    String company = all_graph_list_object.getString("company");
                    String work_address = all_graph_list_object.getString("work_address");
                    String billing_address = all_graph_list_object.getString("billing_address");
                    String phone = all_graph_list_object.getString("phone");
                    String about = all_graph_list_object.getString("about");
                    String date_of_birth = all_graph_list_object.getString("date_of_birth");
                    String height = all_graph_list_object.getString("height");
                    String weight = all_graph_list_object.getString("weight");

                    client_details = new Graph_client_details(id, user_type, name, image, email, address, company, work_address, billing_address, phone, about, date_of_birth, height, weight);


                } catch (JSONException e) {
                    execption = e.toString();
                } catch (IOException e) {
                    execption = e.toString();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //super.onPostExecute(aVoid);
                if (!execption.isEmpty()) {
                    Toast.makeText(getActivity(), "Exception Occured..", Toast.LENGTH_SHORT).show();
                } else {
// cirle crop class is on com.esolz.fitness.customviews named as image_transformation-bodhidipta bhattacharjee
                    Picasso.with(getActivity()).load(client_details.getImage()).transform(new Trns()).fit().centerCrop().into(profile_pic_thumb);
                    int space_pos = 0;

                    for (int i = 0; i < client_details.getName().length(); i++) {
                        if (client_details.getName().charAt(i) == ' ') {
                            space_pos = i;
                        }
                    }

                    String first = client_details.getName().substring(0, space_pos);
                    String last = client_details.getName().substring(space_pos, client_details.getName().length());
                    first_name.setText(first);
                    second_name.setText(last);

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    String Birth_day = client_details.getDate_of_birth();
                    String BirthYear = Birth_day.substring(0, 4);
                    int dob = Integer.parseInt(BirthYear);
                    int diff = year - dob;
                    age_in_years.setText(String.valueOf(diff));

                    height_of_client.setText(client_details.getHeight());
                    weight_of_client.setText(client_details.getWeight());

                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            isloading_client_details = true;

                            try {
                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                HttpGet httpGet = new HttpGet(url_client_Images);
                                HttpResponse response = httpClient.execute(httpGet);
                                HttpEntity entity = response.getEntity();
                                is = entity.getContent();
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(is));
                                StringBuilder sb = new StringBuilder();
                                String line = null;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line + "\n");
                                }
                                is.close();
                                json = sb.toString();

                                all_graph_list_object = new JSONObject(json);
//getting  target array
                                JSONArray current_image = all_graph_list_object.getJSONArray("current_images");
                                for (int i = 0; i < current_image.length(); i++) {
                                    JSONObject obj = current_image.getJSONObject(i);

                                    String id = obj.getString("id");
                                    String image = obj.getString("image");
                                    String image_thumbnail = obj.getString("image_thumbnail");
                                    String uploaded_date = obj.getString("uploaded_date");
                                    Graph_Client_Images client_target_image = new Graph_Client_Images(uploaded_date, image_thumbnail, image, id);
                                    client_current_image_list.add(client_target_image);
                                }

//getting goal array
                                current_image = all_graph_list_object.getJSONArray("goal_image");

                                JSONObject obj = current_image.getJSONObject(0);

                                String id_goal = obj.getString("id");
                                String image_goal = obj.getString("image");
                                String image_thumbnail_goal = obj.getString("image_thumbnail");
                                String uploaded_date_goal = obj.getString("uploaded_date");

                                client_goal_image = new Graph_client_Goal_Images(id_goal, uploaded_date_goal, image_thumbnail_goal, image_goal);


                            } catch (Exception e) {
                                execption = e.toString();
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if (!execption.isEmpty()) {
                                Toast.makeText(getActivity(), "Exception Occured..", Toast.LENGTH_SHORT).show();


                            } else {
                                Picasso.with(getActivity()).load(client_current_image_list.get(0).getImage_link().toString()).fit().centerCrop().into(current_pic);


                                Picasso.with(getActivity()).load(client_goal_image.getImage()).fit().centerCrop().into(goal_pic);
                                String current_show_month = client_current_image_list.get(0).getUploaded_date().substring(5, 7);
                                String current_show_date = client_current_image_list.get(0).getUploaded_date().substring(8, 10);

                                switch (current_show_month) {
                                    case "01":
                                        current_show_month = "jan";
                                        break;
                                    case "02":
                                        current_show_month = "feb";
                                        break;
                                    case "03":
                                        current_show_month = "mar";
                                        break;
                                    case "04":
                                        current_show_month = "apr";
                                        break;
                                    case "05":
                                        current_show_month = "may";
                                        break;
                                    case "06":
                                        current_show_month = "jun";
                                        break;
                                    case "07":
                                        current_show_month = "july";
                                        break;
                                    case "08":
                                        current_show_month = "aug";
                                        break;
                                    case "09":
                                        current_show_month = "sept";
                                        break;
                                    case "10":
                                        current_show_month = "oct";
                                        break;
                                    case "11":
                                        current_show_month = "nov";
                                        break;
                                    case "12":
                                        current_show_month = "dec";
                                        break;


                                }
                                current_pic_date.setText("Current," + " " + current_show_date + " " + current_show_month);
//getting goal data
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                    }

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        try {
                                            DefaultHttpClient httpClient = new DefaultHttpClient();
                                            HttpGet httpGet = new HttpGet(url_client_det_graph);
                                            HttpResponse response = httpClient.execute(httpGet);
                                            HttpEntity entity = response.getEntity();
                                            is = entity.getContent();
                                            BufferedReader reader = new BufferedReader(
                                                    new InputStreamReader(is));
                                            StringBuilder sb = new StringBuilder();
                                            String line = null;
                                            while ((line = reader.readLine()) != null) {
                                                sb.append(line + "\n");
                                            }
                                            is.close();
                                            json = sb.toString();

                                            all_graph_list_object = new JSONObject(json);
                                            JSONArray all_garphs_array = all_graph_list_object.getJSONArray("all_graphs");
                                            for (int i = 0; i < all_garphs_array.length(); i++) {
                                                JSONObject all_graphs_objects = all_garphs_array.getJSONObject(i);
                                                String id = all_graphs_objects.getString("id");
                                                String graph_type = all_graphs_objects.getString("graph_type");
                                                String graph_for = all_graphs_objects.getString("graph_for");
                                                String measure_unit = all_graphs_objects.getString("measure_unit");

                                                JSONArray points = all_graphs_objects.getJSONArray("points");
                                                String x_axis_point = points.getJSONObject(0).getString("x_axis_point");
                                                String y_axis_point = points.getJSONObject(0).getString("y_axis_point");

                                                client_all_graph = new Graph_client_allGraphs(id, y_axis_point, x_axis_point, measure_unit, graph_for, graph_type);
                                                client_all_graphs_list.add(client_all_graph);

                                            }

                                        } catch (Exception e) {
                                            execption = e.toString();
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {

//                                       change made on 22nd jun-bodhidipta bhattacharjee
                                        for (int i = 0; i < client_all_graphs_list.size(); i++) {

                                            Graph_client_allGraphs temp_one = client_all_graphs_list.get(i);
                                            View vw = inflator.inflate(R.layout.prgrs_grph_det_list, null);
                                            HelveticaSemiBoldLight text_type=(HelveticaSemiBoldLight)vw.findViewById(R.id.type_progrss_1);
                                            HelveticaSemiBold goal_progress=(HelveticaSemiBold)vw.findViewById(R.id.goal_progress_1);
                                            HelveticaSemiBoldLight goal_progress_measure=(HelveticaSemiBoldLight)vw.findViewById(R.id.goal_progress_measure_1);
                                            HelveticaSemiBoldLight goal_progress_date=(HelveticaSemiBoldLight)vw.findViewById(R.id.goal_progress_date1);
                                            text_type.setText(temp_one.getGraph_for());
                                            int pos = 0;
                                        String w8 = temp_one.getY_axis_point();
                                             for (int j = 0; j < w8.length(); j++) {
                                                    if (w8.charAt(j) == '.') {
                                                     pos = j;
                                                                            }
                                                                                    }

                                            String wt1 = w8.substring(0, pos);
                                            goal_progress.setText(wt1);
                                            goal_progress_measure.setText(" " + temp_one.getMeasure_unit());
                                            goal_progress_date.setText(temp_one.getX_axis_point());

                                            llGrphdetailsList.addView(vw);
                                        }

                                        fView.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        getActivity().findViewById(R.id.calenderbutton).setClickable(true);
                                        getActivity().findViewById(R.id.blockappoinmentbutton).setClickable(true);
                                        getActivity().findViewById(R.id.progressbutton).setClickable(false);
                                        getActivity().findViewById(R.id.messagebutton).setClickable(true);
                                        isloading_client_details = false;







                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }


            }
        }.execute();


    }
    //code end
    // coded by bodhidipta bhattacharjee
    //on 18th jun

}