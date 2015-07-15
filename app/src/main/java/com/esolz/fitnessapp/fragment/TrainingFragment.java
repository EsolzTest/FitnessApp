package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.TrainingAdapter;
import com.esolz.fitnessapp.adapter.TrainingViewPagerAdapter;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.AllEventsDatatype;
import com.esolz.fitnessapp.datatype.AllExercisesDataType;
import com.esolz.fitnessapp.datatype.ExerciseSetsDataype;
import com.esolz.fitnessapp.datatype.ParticularExerciseDetailsDataType;
import com.esolz.fitnessapp.datatype.TrainingDataType;
import com.esolz.fitnessapp.datatype.TrainingPerticularExerciseSetsDatatype;
import com.esolz.fitnessapp.dialog.ShowMorePopUp;
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

public class TrainingFragment extends Fragment {

    ShowMorePopUp showmorePopup;
    ListView traingList;
    LinearLayout back, more;
    TrainingAdapter trainingAdapter;
    String url;
    View fView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ProgressBar progTraining, viewpagerPBAR, progFinish;
    ConnectionDetector cd;

    RelativeLayout rlLeftClick, rlRightClick;
    TitilliumRegular txtLeft, txtRight, txtFinish;
    LinearLayout llFinish;
    TitilliumSemiBold txtExerciseTitle;
    ViewPager viewpagerExcercise;

    ArrayList<ParticularExerciseDetailsDataType> particularExerciseDetailsDataTypeArrayList;
    ParticularExerciseDetailsDataType perParticularExerciseDetailsDataType;
    ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList;
    TrainingPerticularExerciseSetsDatatype trainingPerticularExerciseSetsDatatype;

    String exception = "", urlResponse = "", exceptionFinish = "", urlResponseFinish = "", statusFinish = "";
    ArrayList<String> exerciseTitleArr;
    ArrayList<String> exerciseIDArr;
    ArrayList<String> userProgramIdArr;

    TrainingViewPagerAdapter trainingViewPagerAdapter;

    int t = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_training, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        cd = new ConnectionDetector(getActivity());

        traingList = (ListView) fView.findViewById(R.id.list_trn);
        traingList.setDivider(null);
        progTraining = (ProgressBar) fView.findViewById(R.id.prog_training);
        progTraining.setVisibility(View.GONE);
        back = (LinearLayout) fView.findViewById(R.id.back);
        more = (LinearLayout) fView.findViewById(R.id.more);

        rlLeftClick = (RelativeLayout) fView.findViewById(R.id.rl_left_click);
        rlRightClick = (RelativeLayout) fView.findViewById(R.id.rl_right_click);
        txtLeft = (TitilliumRegular) fView.findViewById(R.id.txt_left);
        txtRight = (TitilliumRegular) fView.findViewById(R.id.txt_right);
        txtFinish = (TitilliumRegular) fView.findViewById(R.id.txt_finish);
        llFinish = (LinearLayout) fView.findViewById(R.id.ll_finish);

        txtExerciseTitle = (TitilliumSemiBold) fView.findViewById(R.id.txt_title_exercise);
        viewpagerExcercise = (ViewPager) fView.findViewById(R.id.view_pager_exercise);
        viewpagerPBAR = (ProgressBar) fView.findViewById(R.id.viewpager_pbar);
        viewpagerPBAR.setVisibility(View.GONE);
        progFinish = (ProgressBar) fView.findViewById(R.id.prog_finish);
        progFinish.setVisibility(View.GONE);

        exerciseTitleArr = new ArrayList<String>();
        exerciseIDArr = new ArrayList<String>();
        userProgramIdArr = new ArrayList<String>();

        for (int i = 0; i < AppConfig.allExercisesDataTypeArrayList.size(); i++) {
            exerciseTitleArr.add(AppConfig.allExercisesDataTypeArrayList.get(i).getExercise_title());
        }

        for (int j = 0; j < AppConfig.allExercisesDataTypeArrayList.size(); j++) {
            exerciseIDArr.add(AppConfig.allExercisesDataTypeArrayList.get(j).getExercise_id());
        }

        for (int k = 0; k < AppConfig.allExercisesDataTypeArrayList.size(); k++) {
            userProgramIdArr.add(AppConfig.allExercisesDataTypeArrayList.get(k).getUser_program_id());
        }

        if (cd.isConnectingToInternet()) {
            getExerCiseDetails(userProgramIdArr.get(0), exerciseIDArr.get(0));
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        txtExerciseTitle.setText(exerciseTitleArr.get(0));

        rlLeftClick.setVisibility(View.GONE);
        txtRight.setText(exerciseTitleArr.get(0));
        rlRightClick.setVisibility(View.VISIBLE);

        rlLeftClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t > 0 && t < exerciseTitleArr.size()) {
                    t--;
                    if (t == 0) {
                        rlLeftClick.setVisibility(View.GONE);
                        txtRight.setText(exerciseTitleArr.get(t + 1));
                        rlRightClick.setVisibility(View.VISIBLE);
                    } else if (t == (exerciseTitleArr.size() - 1)) {
                        rlLeftClick.setVisibility(View.VISIBLE);
                        txtLeft.setText(exerciseTitleArr.get(t - 1));
                        rlRightClick.setVisibility(View.GONE);
                    } else {
                        rlLeftClick.setVisibility(View.VISIBLE);
                        txtRight.setText(exerciseTitleArr.get(t + 1));
                        txtLeft.setText(exerciseTitleArr.get(t - 1));
                        rlRightClick.setVisibility(View.VISIBLE);
                    }
                    if (cd.isConnectingToInternet()) {
                        getExerCiseDetails(userProgramIdArr.get(t), exerciseIDArr.get(t));
                    } else {
                        Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getActivity(), exerciseIDArr.get(t), Toast.LENGTH_SHORT).show();
                }
            }
        });
        rlRightClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t < exerciseTitleArr.size()) {
                    t++;
                    if (t == 0) {
                        rlLeftClick.setVisibility(View.GONE);
                        txtRight.setText(exerciseTitleArr.get(t + 1));
                        rlRightClick.setVisibility(View.VISIBLE);
                    } else if (t == (exerciseTitleArr.size() - 1)) {
                        rlLeftClick.setVisibility(View.VISIBLE);
                        txtLeft.setText(exerciseTitleArr.get(t - 1));
                        rlRightClick.setVisibility(View.GONE);
                    } else {
                        rlLeftClick.setVisibility(View.VISIBLE);
                        txtRight.setText(exerciseTitleArr.get(t + 1));
                        txtLeft.setText(exerciseTitleArr.get(t - 1));
                        rlRightClick.setVisibility(View.VISIBLE);
                    }
                    if (cd.isConnectingToInternet()) {
                        getExerCiseDetails(userProgramIdArr.get(t), exerciseIDArr.get(t));
                    } else {
                        Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getActivity(), exerciseIDArr.get(t), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerExcercise.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showmorePopup.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, -20);
            }
        });

        llFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    finishExercise(userProgramIdArr.get(0), perParticularExerciseDetailsDataType.getExercise_id());
                } else {
                    Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                fragmentTransaction = fragmentManager.beginTransaction();
                CalenderFragment cal_fragment = new CalenderFragment();
                fragmentTransaction.replace(R.id.fragment_container,
                        cal_fragment);
                fragmentTransaction.commit();
            }
        });

        return fView;
    }

    public void finishExercise(final String userProgramId, final String excerciseId) {

        AsyncTask<Void, Void, Void> finishExcercise = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progFinish.setVisibility(View.VISIBLE);
                llFinish.setClickable(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionFinish = "";
                    urlResponseFinish = "";
                    statusFinish = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/update_finish_status?user_program_id=" +
                            userProgramId + "&client_id=" + AppConfig.loginDatatype.getSiteUserId() + "&exercise_id=" + excerciseId);
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
                    urlResponseFinish = sb.toString();
                    JSONObject jOBJ = new JSONObject(urlResponseFinish);
                    statusFinish = jOBJ.getString("finished");
                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exceptionFinish = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/update_finish_status?user_program_id=" +
                        userProgramId + "&client_id=" + AppConfig.loginDatatype.getSiteUserId() + "&exercise_id=" + excerciseId);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progFinish.setVisibility(View.GONE);
                llFinish.setClickable(false);
                if (exceptionFinish.equals("")) {
                    if (statusFinish.equals("TRUE")) {
                        if (t == exerciseTitleArr.size()) {
                            rlLeftClick.performClick();
                        } else {
                            rlRightClick.performClick();
                        }
                        perParticularExerciseDetailsDataType.setFinished("TRUE");
                    }
                } else {
                    Log.d("@  Exception Finish ", exceptionFinish);
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        finishExcercise.execute();

    }

    public void getExerCiseDetails(final String userProgramId, final String excerciseId) {

        AsyncTask<Void, Void, Void> excerciseDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progTraining.setVisibility(View.VISIBLE);
                traingList.setVisibility(View.GONE);
                rlLeftClick.setClickable(false);
                rlRightClick.setClickable(false);
                viewpagerPBAR.setVisibility(View.VISIBLE);
                viewpagerExcercise.setVisibility(View.GONE);
                llFinish.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_particular_exercise_details?user_program_id=" +
                            userProgramId + "&client_id=" + AppConfig.loginDatatype.getSiteUserId() + "&exercise_id=" + excerciseId);
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
                    JSONArray jsonArray = jOBJ.getJSONArray("exercise_sets");
                    particularExerciseDetailsDataTypeArrayList = new ArrayList<ParticularExerciseDetailsDataType>();
                    trainingPerticularExerciseSetsDatatypeArrayList = new ArrayList<TrainingPerticularExerciseSetsDatatype>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            trainingPerticularExerciseSetsDatatype = new TrainingPerticularExerciseSetsDatatype(
                                    jsonObject.getString("reps"),
                                    jsonObject.getString("kg"),
                                    false
                            );
                            trainingPerticularExerciseSetsDatatypeArrayList.add(trainingPerticularExerciseSetsDatatype);
                        } catch (Exception e) {
                            Log.d("Inner Exception : ", e.toString());
                        }
                    }
                    perParticularExerciseDetailsDataType = new ParticularExerciseDetailsDataType(
                            jOBJ.getString("exercise_id"),
                            jOBJ.getString("exercise_title"),
                            jOBJ.getString("exercise_description"),
                            jOBJ.getString("exercise_image"),
                            jOBJ.getString("exercise_video"),
                            jOBJ.getString("instruction"),
                            jOBJ.getString("finished"),
                            trainingPerticularExerciseSetsDatatypeArrayList
                    );
                    particularExerciseDetailsDataTypeArrayList.add(perParticularExerciseDetailsDataType);
                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/get_particular_exercise_details?user_program_id=" +
                        userProgramId + "&client_id=" + AppConfig.loginDatatype.getSiteUserId() + "&exercise_id=" + excerciseId);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progTraining.setVisibility(View.GONE);
                viewpagerPBAR.setVisibility(View.GONE);
                if (exception.equals("")) {
                    traingList.setVisibility(View.VISIBLE);
                    viewpagerExcercise.setVisibility(View.VISIBLE);
                    rlLeftClick.setClickable(true);
                    rlRightClick.setClickable(true);
                    llFinish.setVisibility(View.VISIBLE);

                    if (perParticularExerciseDetailsDataType.getFinished().equals("FALSE")) {
                        llFinish.setClickable(true);
                        llFinish.setBackgroundResource(R.drawable.finish_button);
                        txtFinish.setText("Finish Exercise");
                        txtFinish.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        llFinish.setClickable(false);
                        llFinish.setBackgroundColor(Color.parseColor("#BDBDBD"));
                        txtFinish.setText("Finished");
                        txtFinish.setTextColor(Color.parseColor("#000000"));
                    }

                    txtExerciseTitle.setText(perParticularExerciseDetailsDataType.getExercise_title());

                    trainingAdapter = new TrainingAdapter(getActivity(), 0,
                            particularExerciseDetailsDataTypeArrayList.get(0).getTrainingPerticularExerciseSetsDatatypeArrayList());
                    traingList.setAdapter(trainingAdapter);

                    trainingViewPagerAdapter = new TrainingViewPagerAdapter(getActivity(), 0,
                            perParticularExerciseDetailsDataType.getExercise_image(),
                            perParticularExerciseDetailsDataType.getExercise_video());
                    viewpagerExcercise.setAdapter(trainingViewPagerAdapter);

                    showmorePopup = new ShowMorePopUp(getActivity(),
                            perParticularExerciseDetailsDataType.getExercise_title(),
                            perParticularExerciseDetailsDataType.getExercise_description(),
                            perParticularExerciseDetailsDataType.getInstruction());
                } else {
                    Log.d("@  Exception ", exception);
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        excerciseDetails.execute();

    }
}
