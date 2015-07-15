package com.esolz.fitnessapp.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.customviews.TitilliumSemiBoldEdit;
import com.esolz.fitnessapp.datatype.TrainingPerticularExerciseSetsDatatype;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class TrainingAdapter extends ArrayAdapter<TrainingPerticularExerciseSetsDatatype> {

    Context context;
    ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList;
    LayoutInflater inflator;
    ViewHolder holder;
    ConnectionDetector cd;
    String exceptionFinish = "", urlResponseFinish = "", statusFinish = "";

    public TrainingAdapter(Context context, int resource,
                           ArrayList<TrainingPerticularExerciseSetsDatatype> trainingPerticularExerciseSetsDatatypeArrayList) {
        super(context, resource, trainingPerticularExerciseSetsDatatypeArrayList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.trainingPerticularExerciseSetsDatatypeArrayList = trainingPerticularExerciseSetsDatatypeArrayList;
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        cd = new ConnectionDetector(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.training_item, parent, false);
            holder = new ViewHolder();

            holder.txtSet = (TitilliumSemiBold) convertView.findViewById(R.id.txt_set);
            holder.txtReps = (TitilliumSemiBold) convertView.findViewById(R.id.txt_reps);
            holder.etWeight = (EditText) convertView.findViewById(R.id.txt_weight);

            holder.llCheck = (LinearLayout) convertView.findViewById(R.id.ll_check);
            holder.llChange = (LinearLayout) convertView.findViewById(R.id.ll_change);

            holder.llSet = (LinearLayout) convertView.findViewById(R.id.set_ll);
            holder.llReps = (LinearLayout) convertView.findViewById(R.id.reps_ll);
            holder.llKG = (LinearLayout) convertView.findViewById(R.id.kg_ll);
            holder.txtRP = (TitilliumRegular) convertView.findViewById(R.id.rp);
            holder.txtKG = (TitilliumRegular) convertView.findViewById(R.id.kg);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtSet.setText("set " + (position + 1));
        holder.txtReps.setText(trainingPerticularExerciseSetsDatatypeArrayList.get(position).getREPS());
        holder.etWeight.setText(trainingPerticularExerciseSetsDatatypeArrayList.get(position).getKg());


        if (trainingPerticularExerciseSetsDatatypeArrayList.get(position).getIsEditable()) {
            holder.etWeight.setClickable(true);
            holder.etWeight.setEnabled(true);
            holder.llCheck.setVisibility(View.GONE);
            holder.llChange.setVisibility(View.VISIBLE);

            holder.txtSet.setTextColor(Color.parseColor("#AEDFFB"));
            holder.txtReps.setTextColor(Color.parseColor("#AEDFFB"));
            holder.txtRP.setTextColor(Color.parseColor("#AEDFFB"));
            holder.txtKG.setTextColor(Color.parseColor("#FFFFFF"));
            holder.etWeight.setTextColor(Color.parseColor("#FFFFFF"));

            holder.llSet.setBackgroundColor(Color.parseColor("#26B9F6"));
            holder.llReps.setBackgroundColor(Color.parseColor("#26B9F6"));
            holder.llKG.setBackgroundColor(Color.parseColor("#00A5F4"));

        } else {
            holder.etWeight.setClickable(false);
            holder.etWeight.setEnabled(false);
            holder.llCheck.setVisibility(View.VISIBLE);
            holder.llChange.setVisibility(View.GONE);

            holder.txtSet.setTextColor(Color.parseColor("#333333"));
            holder.txtReps.setTextColor(Color.parseColor("#22A7F0"));
            holder.txtRP.setTextColor(Color.parseColor("#333333"));
            holder.txtKG.setTextColor(Color.parseColor("#22A7F0"));
            holder.etWeight.setTextColor(Color.parseColor("#333333"));

            holder.llSet.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.llReps.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.llKG.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.llCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < trainingPerticularExerciseSetsDatatypeArrayList.size(); i++) {
                    if (i == position) {
                        if (trainingPerticularExerciseSetsDatatypeArrayList.get(i).getIsEditable()) {
                            trainingPerticularExerciseSetsDatatypeArrayList.get(i).setIsEditable(false);
                        } else {
                            trainingPerticularExerciseSetsDatatypeArrayList.get(i).setIsEditable(true);
                        }
                    } else {
                        trainingPerticularExerciseSetsDatatypeArrayList.get(i).setIsEditable(false);
                    }
                }
                notifyDataSetChanged();
            }
        });

        holder.llChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    protected class ViewHolder {
        TitilliumSemiBold txtSet, txtReps;
        TitilliumRegular txtRP, txtKG;
        /*TitilliumSemiBoldEdit*/ EditText etWeight;
        LinearLayout llCheck, llChange, llSet, llReps, llKG;
    }

    public void editExcercise(final String userProgramId, final String excerciseId) {

        AsyncTask<Void, Void, Void> excerciseFinish = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
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
                if (exceptionFinish.equals("")) {

                } else {
                    Log.d("@  Exception Finish ", exceptionFinish);
                    Toast.makeText(context, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        excerciseFinish.execute();

    }

}
