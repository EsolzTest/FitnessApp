package com.esolz.fitnessapp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.HelveticaHeavy;
import com.esolz.fitnessapp.customviews.TitilliumLight;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.AllEventsDatatype;
import com.esolz.fitnessapp.datatype.AllExercisesDataType;
import com.esolz.fitnessapp.datatype.ExerciseSetsDataype;
import com.esolz.fitnessapp.dialog.ShowCalendarPopUp;
import com.esolz.fitnessapp.fitness.CustomCalendarView;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;
import com.esolz.fitnessapp.helper.ReturnCalendarDetails;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@SuppressLint("NewApi")
public class CalenderFragment extends Fragment {

    LinearLayout rlTraining, rlDiet, rlDiary, showCalender, llList;
    LinearLayout llCalenderButton, llBlockAppoinmentButton, llProgressButton;
    RelativeLayout llMessagebutton, appointment, rlContent;
    Dialog dialog;
    DatePicker datePicker;
    TimePicker timePicker;
    LinearLayout cancel, done;
    TitilliumLight txtRemindme;
    int mYear, mMonth, mDay, mHour, mMinute;
    String type, hour, min;
    View fView;
    LinearLayout dialogRemindme;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;
    SharedPreferences sharedPreferences;
    ShowCalendarPopUp showCalPopup;

    // -- Calendar Instance
    Calendar calendar;
    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition;
    SimpleDateFormat dayFormat, monthFormat, dateFormat;
    Date dateChange;
    String date = "";
    String[] positionPre = {};
    int previousDayPosition;

    TitilliumSemiBold txtMonth, txtAppointment;
    HelveticaHeavy txtDay;
    TitilliumLight txtTrainingDone, txtDiet, txtDiary;
    ProgressBar prg_appointment, prg_content;

    String urlResponse = "", exception = "";

    ConnectionDetector connectionDetector;
    AllEventsDatatype allEventsDatatype;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_calender, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();

        rlTraining = (LinearLayout) fView.findViewById(R.id.rl_training);
        rlDiet = (LinearLayout) fView.findViewById(R.id.rl_diet);
        rlDiary = (LinearLayout) fView.findViewById(R.id.rl_diary);
        appointment = (RelativeLayout) fView.findViewById(R.id.appointment);
        dialogRemindme = (LinearLayout) fView.findViewById(R.id.remindme);
        txtRemindme = (TitilliumLight) fView.findViewById(R.id.txt_remindme);
        showCalender = (LinearLayout) fView.findViewById(R.id.show_cal);

        txtDay = (HelveticaHeavy) fView.findViewById(R.id.txt_day);
        txtMonth = (TitilliumSemiBold) fView.findViewById(R.id.txt_month);
        txtAppointment = (TitilliumSemiBold) fView.findViewById(R.id.txt_appointment);
        txtAppointment.setVisibility(View.GONE);
        rlContent = (RelativeLayout) fView.findViewById(R.id.rl_content);
        rlContent.setVisibility(View.GONE);

        txtTrainingDone = (TitilliumLight) fView.findViewById(R.id.txt_training_done);
        txtDiet = (TitilliumLight) fView.findViewById(R.id.txt_diet);
        txtDiary = (TitilliumLight) fView.findViewById(R.id.txt_diary);

        llList = (LinearLayout) fView.findViewById(R.id.ll_list);
        llList.setVisibility(View.VISIBLE);

        prg_appointment = (ProgressBar) fView.findViewById(R.id.prg_appointent);
        prg_appointment.setVisibility(View.GONE);
        prg_content = (ProgressBar) fView.findViewById(R.id.prg_content);
        prg_content.setVisibility(View.GONE);

        connectionDetector = new ConnectionDetector(getActivity());

        calendar = Calendar.getInstance(Locale.getDefault());
        currentDate = calendar.get(Calendar.DATE);
        currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        currentMonth = (calendar.get(Calendar.MONTH));
        currentYear = calendar.get(Calendar.YEAR);
        firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);


        dayFormat = new SimpleDateFormat("dd");
        monthFormat = new SimpleDateFormat("EEEE");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // -- Show Calendar
        showCalPopup = new ShowCalendarPopUp(getActivity(), "program");

        showCalender.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                showCalPopup.getLayouts();

                Calendar pre = (Calendar) calendar.clone();
                pre.set(Calendar.MONTH, currentMonth);
                pre.set(Calendar.YEAR, currentYear);
                pre.set(Calendar.DATE, 1);

                positionPre = pre.getTime().toString().split(" ");
                previousDayPosition = ReturnCalendarDetails
                        .getPosition(positionPre[0]);
                showCalPopup.getCalendar(ReturnCalendarDetails.getCurrentMonth(positionPre[1]),
                        ReturnCalendarDetails.getPosition(positionPre[0]),
                        Integer.parseInt(positionPre[5]));
                showCalPopup.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0,
                        -20);

            }
            //------getting date
        });

        if (connectionDetector.isConnectingToInternet()) {
            try {
                dateChange = dateFormat.parse(getArguments().getString("DateChange"));

                Log.d("DAY==", getArguments().getString("DateChange"));

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(getArguments().getString("DateChange")));
                calendar = cal;

                getAllEvents(getArguments().getString("DateChange"));

                txtDay.setText("" + dayFormat.format(calendar.getTime()));
                txtMonth.setText("" + monthFormat.format(dateChange));
                Log.d("DAY=+++=", "" + monthFormat.format(dateChange));

            } catch (Exception e) {
                Log.d("Date Exception : ", e.toString());

                txtDay.setText("" + dayFormat.format(calendar.getTime()));
                txtMonth.setText("" + monthFormat.format(calendar.getTime()));
                date = "" + dateFormat.format(calendar.getTime());

                getAllEvents(date);
            }
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        // -- Shared Preference
        sharedPreferences = getActivity().getSharedPreferences("DateTime",
                Context.MODE_PRIVATE);

        if (sharedPreferences.getString("dateTime", "").equals("")) {
            txtRemindme.setText("Remind me");
        } else {
            txtRemindme.setText(sharedPreferences.getString("dateTime", ""));
        }
        // -- END

        // ---------- Reminder Dialog
        dialog = new Dialog(getActivity());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reminder_dialog);
        dialog.setCancelable(false);
        datePicker = (DatePicker) dialog.findViewById(R.id.date);
        datePicker.setCalendarViewShown(false);
        timePicker = (TimePicker) dialog.findViewById(R.id.time);
        cancel = (LinearLayout) dialog.findViewById(R.id.ll_cancel);
        done = (LinearLayout) dialog.findViewById(R.id.ll_done);

        dialogRemindme.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.show();

                done.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub

                        String month = "", day = "";

                        mDay = datePicker.getDayOfMonth();
                        mMonth = datePicker.getMonth() + 1;
                        mYear = datePicker.getYear();

                        mHour = timePicker.getCurrentHour();
                        mMinute = timePicker.getCurrentMinute();

                        if (mDay < 10) {
                            day = "0" + mDay;
                        } else {
                            day = "" + mDay;
                        }
                        if (mMonth < 10) {
                            month = "0" + mMonth;
                        } else {
                            month = "" + mMonth;
                        }

                        if (mHour > 12) {
                            hour = "0" + (mHour - 12);
                            type = "pm";
                        } else {
                            hour = "0" + mHour;
                            type = "am";
                        }

                        String dateAndTime = day + "/" + month + "  " + hour + ":" + mMinute + " " + type;

                        txtRemindme.setText("" + dateAndTime);

                        Editor editor = sharedPreferences.edit();
                        editor.putString("dateTime", dateAndTime);
                        editor.commit();

                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        txtRemindme.setText("Remind me");
                        Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        dialog.dismiss();
                    }
                });
            }
        });

        appointment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                bundle = new Bundle();
                try {
                    bundle.putString("DateChange", getArguments().getString("DateChange"));
                } catch (Exception e) {
                    bundle.putString("DateChange", date);
                }

                if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) > 0
                        && Integer.parseInt(allEventsDatatype.getTotal_appointment()) < 2) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    AppointmantFragment app_fragment = new AppointmantFragment();
                    app_fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container,
                            app_fragment);
                    fragmentTransaction.commit();
                } else if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) > 1) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    AppointmentListFragment app_list_fragment = new AppointmentListFragment();
                    app_list_fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container,
                            app_list_fragment);
                    fragmentTransaction.commit();
                } else {

                }

            }
        });

        // --- Training Fragment
        rlTraining.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                fragmentTransaction = fragmentManager.beginTransaction();
                TrainingFragment trn_fragment = new TrainingFragment();
                fragmentTransaction.replace(R.id.fragment_container,
                        trn_fragment);
                fragmentTransaction.commit();
            }
        });

        // --- Diet Fragment
        rlDiet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                fragmentTransaction = fragmentManager.beginTransaction();
                DietFragment diet_fragment = new DietFragment();
                fragmentTransaction.replace(R.id.fragment_container,
                        diet_fragment);
                fragmentTransaction.commit();
            }
        });

        // --- Diary Fragment
        rlDiary.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        CustomCalendarView.class);
                startActivity(intent);
            }
        });

        llCalenderButton = (LinearLayout) getActivity().findViewById(
                R.id.calenderbutton);
        llBlockAppoinmentButton = (LinearLayout) getActivity().findViewById(
                R.id.blockappoinmentbutton);
        llProgressButton = (LinearLayout) getActivity().findViewById(
                R.id.progressbutton);
        llMessagebutton = (RelativeLayout) getActivity().findViewById(
                R.id.messagebutton);
        llCalenderButton.setClickable(false);
        llBlockAppoinmentButton.setClickable(true);
        llProgressButton.setClickable(true);
        llMessagebutton.setClickable(true);

        return fView;
    }

    public void getAllEvents(final String date) {

        AsyncTask<Void, Void, Void> allEvents = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                prg_appointment.setVisibility(View.VISIBLE);
                prg_content.setVisibility(View.VISIBLE);
                txtAppointment.setVisibility(View.GONE);
                rlContent.setVisibility(View.GONE);
                llList.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_all_events_for_date/?" +
                            "client_id=" + AppConfig.loginDatatype.getSiteUserId() +
                            "&date_val=" + date);
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

                    allEventsDatatype = new AllEventsDatatype(jOBJ.getString("total_meal"),
                            jOBJ.getString("total_appointment"),
                            jOBJ.getString("diary_text"),
                            jOBJ.getString("total_training_exercises"),
                            jOBJ.getString("total_training_exercise_finished"),
                            jOBJ.getString("total_training_programs"),
                            jOBJ.getString("total_training_programs_finished"));

                    JSONArray jsonArray = jOBJ.getJSONArray("all_exercises");
                    AppConfig.allExercisesDataTypeArrayList = new ArrayList<AllExercisesDataType>();
                    AppConfig.exerciseSetsDataypeArrayList = new ArrayList<ExerciseSetsDataype>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        JSONArray jArr = jsonObject.getJSONArray("exercise_sets");
                        for (int k = 0; k < jArr.length(); k++) {
                            JSONObject jObject = jArr.getJSONObject(k);
                            ExerciseSetsDataype exerciseSetsDataype = new ExerciseSetsDataype(
                                    jObject.getString("reps"),
                                    jObject.getString("kg")
                            );
                            AppConfig.exerciseSetsDataypeArrayList.add(exerciseSetsDataype);
                        }
                        AllExercisesDataType allExercisesDataType = new AllExercisesDataType(
                                jsonObject.getString("user_program_id"),
                                jsonObject.getString("exercise_id"),
                                jsonObject.getString("exercise_title"),
                                jsonObject.getString("instruction"),
                                AppConfig.exerciseSetsDataypeArrayList
                        );
                        AppConfig.allExercisesDataTypeArrayList.add(allExercisesDataType);
                    }

                    Log.d("RESPONSE", jOBJ.toString());

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/get_all_events_for_date/?" +
                        "client_id=" + AppConfig.loginDatatype.getSiteUserId() +
                        "&date_val=" + date);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                if (exception.equals("")) {
                    prg_appointment.setVisibility(View.GONE);
                    prg_content.setVisibility(View.GONE);
                    txtAppointment.setVisibility(View.VISIBLE);
                    rlContent.setVisibility(View.VISIBLE);
                    llList.setVisibility(View.VISIBLE);

                    if (Integer.parseInt(allEventsDatatype.getTotal_appointment()) == 0
                            || Integer.parseInt(allEventsDatatype.getTotal_appointment()) == 1) {
                        txtAppointment.setText(allEventsDatatype.getTotal_appointment() + " " + "Appointment");
                    } else {
                        txtAppointment.setText(allEventsDatatype.getTotal_appointment() + " " + "Appointments");
                    }
                    txtTrainingDone.setText(allEventsDatatype.getTotal_training_exercise_finished() +
                            "/" + allEventsDatatype.getTotal_training_exercises() + "  exercises done");
                    txtDiet.setText(allEventsDatatype.getTotal_meal() + " meal");
                    txtDiary.setText(allEventsDatatype.getDiary_text());

                } else {
                    Log.d("@  Exception ", exception);
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        allEvents.execute();

    }

}
