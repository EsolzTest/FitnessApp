package com.esolz.fitnessapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.HelveticaSemiBold;
import com.esolz.fitnessapp.customviews.HelveticaSemiBoldLight;
import com.esolz.fitnessapp.datatype.GraphClientAllDataType;
import com.esolz.fitnessapp.datatype.GraphClientDetailsDataType;
import com.esolz.fitnessapp.datatype.GraphClientGoalImages;
import com.esolz.fitnessapp.datatype.GraphClientImagesDataType;
import com.esolz.fitnessapp.fitness.LandScreenActivity;
import com.esolz.fitnessapp.fitness.ProgressGraphView;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class ProgressFragment extends Fragment {
    View fView;
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton, llGrphdetailsList;
    RelativeLayout llMessagebutton;
    LinearLayout allGraph;

    ImageView imgThumb, currentPicture, goalPicture;
    HelveticaSemiBold first_name;
    HelveticaSemiBoldLight last_name, current_picture_date, age_years, height_of_client, weight_of_client;
    ProgressBar progBar;
    ScrollView scrollView;

    RelativeLayout uploadCurrentImg, uploadGoalImg;

    String exception = "", urlResponse = "", exceptionImg = "", urlResponseImg = "", exceptionGraph = "", urlResponseGraph = "";
    GraphClientDetailsDataType graphClientDetailsDataType;
    GraphClientImagesDataType graphClientImagesDataType;
    GraphClientGoalImages graphClientGoalImages;
    LinkedList<GraphClientImagesDataType> graphClientImagesDataTypeLinkedList;
    GraphClientAllDataType graphClientAllDataType;
    LinkedList<GraphClientAllDataType> graphClientAllDataTypeLinkedList;

    ConnectionDetector connectionDetector;
    LayoutInflater inflator;

    Dialog dialogChooser;
    LinearLayout llGallery, llCamera, llCancel;

    // --- For Camera and Gallery ---
    String URL = "", Current_PATH = "", statingProfileImageURL = "", exceptionC = "";
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_GALLERY = 2;
    // --- End ---

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fView = inflater.inflate(R.layout.frag_progress, container, false);
        connectionDetector = new ConnectionDetector(getActivity());
        inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        allGraph = (LinearLayout) fView.findViewById(R.id.all_graph);

        progBar = (ProgressBar) fView.findViewById(R.id.prog_bar);
        scrollView = (ScrollView) fView.findViewById(R.id.scrl_body);
        progBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);

        imgThumb = (ImageView) fView.findViewById(R.id.image_thumb);
        first_name = (HelveticaSemiBold) fView.findViewById(R.id.first_name);
        last_name = (HelveticaSemiBoldLight) fView.findViewById(R.id.last_name);
        age_years = (HelveticaSemiBoldLight) fView.findViewById(R.id.age_years);
        height_of_client = (HelveticaSemiBoldLight) fView.findViewById(R.id.height_of_client);
        weight_of_client = (HelveticaSemiBoldLight) fView.findViewById(R.id.weight_of_client);

        currentPicture = (ImageView) fView.findViewById(R.id.current_picture);
        goalPicture = (ImageView) fView.findViewById(R.id.goal_picture);
        current_picture_date = (HelveticaSemiBoldLight) fView.findViewById(R.id.current_picture_date);

        llGrphdetailsList = (LinearLayout) fView.findViewById(R.id.Grph_list_details);

        uploadCurrentImg = (RelativeLayout) fView.findViewById(R.id.upload_current_img);
        uploadGoalImg = (RelativeLayout) fView.findViewById(R.id.upload_goal_img);

        dialogChooser = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialogChooser.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChooser.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogChooser.getWindow().setGravity(Gravity.BOTTOM);
        dialogChooser.setContentView(R.layout.dialog_camera_selection);
        dialogChooser.setCanceledOnTouchOutside(false);

        llGallery = (LinearLayout) dialogChooser.findViewById(R.id.ll_gallery);
        llCamera = (LinearLayout) dialogChooser.findViewById(R.id.ll_camera);
        llCancel = (LinearLayout) dialogChooser.findViewById(R.id.ll_cancel);

        uploadCurrentImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.show();
            }
        });
        uploadGoalImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.show();
            }
        });

        llGallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, ACTION_TAKE_GALLERY);

                dialogChooser.dismiss();
            }
        });

        llCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent jh = new Intent(getActivity(), EsolzCamera.class);
//                startActivityForResult(jh, ACTION_TAKE_PHOTO_B);

                dialogChooser.dismiss();
            }
        });

        llCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooser.dismiss();
            }
        });

        allGraph.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), ProgressGraphView.class);
                startActivity(intent);
            }
        });

        currentPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                LandScreenActivity land = (LandScreenActivity) getActivity();
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < graphClientImagesDataTypeLinkedList.size(); i++) {
                    list.add(graphClientImagesDataTypeLinkedList.get(i).getImage_link());
                }
                land.Show_FullScreen_ViewPager(land, list);
            }
        });

        goalPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LandScreenActivity land = (LandScreenActivity) getActivity();
                ArrayList<String> list = new ArrayList<String>();
                list.add(graphClientGoalImages.getImage());
                land.Show_FullScreen_ViewPager(land, list);
            }
        });

        if (connectionDetector.isConnectingToInternet()) {
            getClientDetails();
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
        llProgressButton.setClickable(false);
        llMessagebutton.setClickable(true);

        return fView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_TAKE_PHOTO_B) {

            if (resultCode == 6000) {

                Current_PATH = data.getStringExtra("Path");
            }
        } else if (requestCode == ACTION_TAKE_GALLERY) {
            if (resultCode == getActivity().RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                Current_PATH = cursor.getString(columnIndex);
                cursor.close();
                Toast.makeText(getActivity(), "hanle..." + Current_PATH, Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getActivity(), "Canceled by user",
                        Toast.LENGTH_SHORT).show();
                Current_PATH = "";
            }
        }
    }

    // *******************

    public void getClientDetails() {

        AsyncTask<Void, Void, Void> clientDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progBar.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_client_details?client_id=" +
                            AppConfig.loginDatatype.getSiteUserId());
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

                    graphClientDetailsDataType = new GraphClientDetailsDataType(
                            jOBJ.getString("id"), jOBJ.getString("user_type"), jOBJ.getString("name"),
                            jOBJ.getString("image"), jOBJ.getString("email"), jOBJ.getString("address"),
                            jOBJ.getString("company"), jOBJ.getString("work_address"), jOBJ.getString("billing_address"),
                            jOBJ.getString("phone"), jOBJ.getString("about"), jOBJ.getString("date_of_birth"),
                            jOBJ.getString("height"), jOBJ.getString("weight"), jOBJ.getString("fat")
                    );

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/get_client_details?client_id=" + AppConfig.loginDatatype.getSiteUserId());
                } catch (Exception e) {
                    exception = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (exception.equals("")) {
                    Picasso.with(getActivity()).load(graphClientDetailsDataType.getImage()).
                            transform(new Trns()).fit().centerCrop().into(imgThumb);
                    String[] name = graphClientDetailsDataType.getName().split(" ");
                    first_name.setText(name[0]);
                    last_name.setText(name[1]);

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    String Birth_day = graphClientDetailsDataType.getDate_of_birth();
                    String BirthYear = Birth_day.substring(0, 4);
                    int dob = Integer.parseInt(BirthYear);
                    int diff = year - dob;
                    age_years.setText(String.valueOf(diff));

                    height_of_client.setText(graphClientDetailsDataType.getHeight());
                    weight_of_client.setText(graphClientDetailsDataType.getWeight());

                    getClientImg();
                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        clientDetails.execute();

    }

    public void getClientImg() {

        AsyncTask<Void, Void, Void> clientImg = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionImg = "";
                    urlResponseImg = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_client_images?client_id=" +
                            AppConfig.loginDatatype.getSiteUserId());
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
                    urlResponseImg = sb.toString();
                    JSONObject jOBJ = new JSONObject(urlResponseImg);
                    JSONArray jsonArrayCurrentImg = jOBJ.getJSONArray("current_images");
                    JSONArray jsonArrayGoalImg = jOBJ.getJSONArray("goal_image");
                    graphClientImagesDataTypeLinkedList = new LinkedList<GraphClientImagesDataType>();
                    for (int i = 0; i < jsonArrayCurrentImg.length(); i++) {
                        JSONObject jsonObject = jsonArrayCurrentImg.getJSONObject(i);
                        graphClientImagesDataType = new GraphClientImagesDataType(
                                jsonObject.getString("uploaded_date"),
                                jsonObject.getString("image_thumbnail"),
                                jsonObject.getString("image"),
                                jsonObject.getString("id"));
                        graphClientImagesDataTypeLinkedList.add(graphClientImagesDataType);
                    }

                    JSONObject jsonObj = jsonArrayGoalImg.getJSONObject(0);
                    graphClientGoalImages = new GraphClientGoalImages(
                            jsonObj.getString("id"),
                            jsonObj.getString("uploaded_date"),
                            jsonObj.getString("image_thumbnail"),
                            jsonObj.getString("image"));

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/get_client_images?client_id=" + AppConfig.loginDatatype.getSiteUserId());
                } catch (Exception e) {
                    exceptionImg = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                if (exceptionImg.equals("")) {
                    Picasso.with(getActivity()).load(graphClientImagesDataTypeLinkedList.get(0).getImage_link().toString()).
                            fit().centerCrop().into(currentPicture);
                    Picasso.with(getActivity()).load(graphClientGoalImages.getImage()).fit().centerCrop().into(goalPicture);
                    String current_show_month = graphClientImagesDataTypeLinkedList.get(0).getUploaded_date().substring(5, 7);
                    String current_show_date = graphClientImagesDataTypeLinkedList.get(0).getUploaded_date().substring(8, 10);
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
                    current_picture_date.setText("Current," + " " + current_show_date + " " + current_show_month);
                    getClientGraph();
                } else {
                    Log.d("Exception : ", exceptionImg);
                    Toast.makeText(getActivity(), "Server not responding for image...." + exceptionImg, Toast.LENGTH_LONG).show();
                }
            }

        };
        clientImg.execute();

    }

    public void getClientGraph() {

        AsyncTask<Void, Void, Void> clientGraph = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionGraph = "";
                    urlResponseGraph = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/all_graphs?client_id=" +
                            AppConfig.loginDatatype.getSiteUserId());
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
                    urlResponseGraph = sb.toString();
                    JSONObject jOBJ = new JSONObject(urlResponseGraph);
                    JSONArray jsonArray = jOBJ.getJSONArray("all_graphs");
                    graphClientAllDataTypeLinkedList = new LinkedList<GraphClientAllDataType>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        JSONArray points = jsonObj.getJSONArray("points");
                        graphClientAllDataType = new GraphClientAllDataType(
                                jsonObj.getString("id"),
                                points.getJSONObject(0).getString("y_axis_point"),
                                points.getJSONObject(0).getString("x_axis_point"),
                                jsonObj.getString("measure_unit"),
                                jsonObj.getString("graph_for"),
                                jsonObj.getString("graph_type"));
                        graphClientAllDataTypeLinkedList.add(graphClientAllDataType);

                    }

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/all_graphs?client_id=" + AppConfig.loginDatatype.getSiteUserId());
                } catch (Exception e) {
                    exceptionGraph = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                if (exceptionGraph.equals("")) {
                    for (int i = 0; i < graphClientAllDataTypeLinkedList.size(); i++) {

                        GraphClientAllDataType temp_one = graphClientAllDataTypeLinkedList.get(i);
                        View view = inflator.inflate(R.layout.prgrs_grph_det_list, null);
                        HelveticaSemiBoldLight text_type = (HelveticaSemiBoldLight) view.findViewById(R.id.type_progrss_1);
                        HelveticaSemiBold goal_progress = (HelveticaSemiBold) view.findViewById(R.id.goal_progress_1);
                        HelveticaSemiBoldLight goal_progress_measure = (HelveticaSemiBoldLight) view.findViewById(R.id.goal_progress_measure_1);
                        HelveticaSemiBoldLight goal_progress_date = (HelveticaSemiBoldLight) view.findViewById(R.id.goal_progress_date1);
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

                        llGrphdetailsList.addView(view);
                    }
                } else {
                    Log.d("Exception : ", exceptionGraph);
                    Toast.makeText(getActivity(), "Server not responding for graph...." + exceptionGraph, Toast.LENGTH_LONG).show();
                }
            }

        };
        clientGraph.execute();

    }
}