package com.esolz.fitnessapp.dialog;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.datatype.TrainingDataType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by su on 25/6/15.
 */
public class ShowMorePopUp extends PopupWindow {

    Context context;
    View popupView;
    LinearLayout llHide;
    LinkedList<TrainingDataType>moredata;
    TrainingDataType kk;
    TitilliumRegular title;
    TitilliumRegular desc;
    TitilliumRegular instruction;


    public ShowMorePopUp(final Context context) {
        super(context);

        this.context = context;

        setContentView(LayoutInflater.from(context).inflate(
                R.layout.frag_more, null));
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupView = getContentView();
        setFocusable(true);
        title=(TitilliumRegular)popupView.findViewById(R.id.title_desc);
        desc=(TitilliumRegular)popupView.findViewById(R.id.detail);
        instruction=(TitilliumRegular)popupView.findViewById(R.id.instruction);
        llHide = (LinearLayout) popupView.findViewById(R.id.ll_hide);

        getmoreLayouts();



    }

    public void getmoreLayouts () {

        (new GetMoreDetail()).execute();
    }



    class GetMoreDetail extends AsyncTask<Void,Void,Void>{

        InputStream is;
        String json;


        JSONObject more_detail;


        @Override
        protected Void doInBackground(Void... params) {


            try {


                DefaultHttpClient httClient = new DefaultHttpClient();
                // HttpClient client = HttpClientBuilder.create().build();

                HttpGet http_get = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_particular_exercise_details?user_program_id=5&client_id=15&exercise_id=2091");

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
                more_detail = new JSONObject(json);




                kk = new TrainingDataType(more_detail.getString("reps"),more_detail.getString("kg"), more_detail.getString("exercise_id"), more_detail.getString("exercise_title"),more_detail.getString("exercise_description"),more_detail.getString("instruction"));


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {

                title.setText("" + kk.getExercise_title());
                desc.setText("" + kk.getExercise_description());
                instruction.setText("" + kk.getInstruction());

            } catch (Exception e) {

            }



        }
    }


}
