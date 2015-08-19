package com.esolz.fitnessapp.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.ImageTransformation.Trns;
import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.adapter.DietAdapter;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.customviews.TitilliumRegularEdit;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.DateRespectiveDiaryDataType;
import com.esolz.fitnessapp.datatype.DietDataType;
import com.esolz.fitnessapp.datatype.EventDataType;
import com.esolz.fitnessapp.dialog.ShowCalendarPopUp;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;
import com.esolz.fitnessapp.helper.ReturnCalendarDetails;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class DiaryFragment extends Fragment {

    View fView;

    LinearLayout back, showCalender;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    // -- Calendar Instance
    Calendar calendar;
    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition,
            currentMonthLength, previousDayPosition, nextDayPosition;
    Date date;

    int l;
    String[] positionPre = {}, positionNext = {};

    float balencing_coefficient_ = -1000.0f;
    float CENTRE_Y_OF_THE_SCREEN = 0.0f;
    float OPEN_CLOSE_BOUNDERY_Y = 0.0f;
    int c = 0;
    LinearLayout coverLayout;
    ScrollView scrollView1maincon;

    TitilliumRegular[] textViewArray, txtApp;
    RelativeLayout[] llArray;
    LinearLayout[] eventViewArr;
    TitilliumRegular txt_currentdatemonth, txt_currentyear;
    LinearLayout llHide, btnNext, btnPre;

    ArrayList<EventDataType> arrDay;
    EventDataType eventDataType;

    ShowCalendarPopUp showCalPopup;

    LinearLayout llMiddle, llCoverLayout;
    TitilliumRegular txtView;
    boolean isBottom = false;
    ViewGroup.LayoutParams params;
    RelativeLayout ll_header;

    ImageView imgClient;
    TitilliumSemiBold txtClientName, txtDate, txtDiaryHeading;
    LinearLayout llEditDiary, llAddDiary;
    TitilliumRegularEdit etDetails;
    ProgressBar pBar;
    ScrollView scrView;

    ConnectionDetector cd;
    String dateChange = "", formattedDate = "";
    DateRespectiveDiaryDataType dateRespectiveDiaryDataType;
    String exception = "", urlResponse = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fView = inflater.inflate(R.layout.frag_diary, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();

        cd = new ConnectionDetector(getActivity());

        back = (LinearLayout) fView.findViewById(R.id.back);
        showCalender = (LinearLayout) fView.findViewById(R.id.show_cal);

        llMiddle = (LinearLayout) fView.findViewById(R.id.middle);
        llCoverLayout = (LinearLayout) fView.findViewById(R.id.coverlayout);
        txtView = (TitilliumRegular) fView.findViewById(R.id.txt_view);
        txtView.setText("Calendar");
        ll_header = (RelativeLayout) fView.findViewById(R.id.ll_header);

        txt_currentyear = (TitilliumRegular) fView
                .findViewById(R.id.txt_currentyear);
        txt_currentdatemonth = (TitilliumRegular) fView
                .findViewById(R.id.txt_currentdatemonth);
        btnPre = (LinearLayout) fView.findViewById(R.id.btn_pre);
        btnNext = (LinearLayout) fView.findViewById(R.id.btn_next);

        calendar = Calendar.getInstance(Locale.getDefault());

        currentDate = calendar.get(Calendar.DATE);
        currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        currentMonth = (calendar.get(Calendar.MONTH) + 1);
        currentYear = calendar.get(Calendar.YEAR);
        firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        showCalPopup = new ShowCalendarPopUp(getActivity(), "diary");

        // ------------------- Previous Month
        btnPre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                getLayouts();

                Calendar pre = (Calendar) calendar.clone();

                currentMonth--;

                if (currentMonth == 0) {
                    currentMonth = 12;
                    currentYear--;
                }
                pre.set(Calendar.MONTH, currentMonth);
                pre.set(Calendar.YEAR, currentYear);
                pre.set(Calendar.DATE, 1);

                positionPre = pre.getTime().toString().split(" ");
                previousDayPosition = ReturnCalendarDetails
                        .getPosition(positionPre[0]);

                getCalendar(
                        ReturnCalendarDetails.getCurrentMonth(positionPre[1]),
                        ReturnCalendarDetails.getPosition(positionPre[0]),
                        Integer.parseInt(positionPre[5]));

            }
        });
        // ------------------- Next Month
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                getLayouts();

                Calendar next = (Calendar) calendar.clone();

                if (currentMonth > 11) {
                    currentMonth = 1;
                    currentYear++;
                } else {
                    currentMonth++;
                }

                next.set(Calendar.MONTH, currentMonth);
                next.set(Calendar.YEAR, currentYear);
                next.set(Calendar.DATE, 1);

                positionNext = next.getTime().toString().split(" ");
                getCalendar(
                        ReturnCalendarDetails.getCurrentMonth(positionNext[1]),
                        ReturnCalendarDetails.getPosition(positionNext[0]),
                        Integer.parseInt(positionNext[5]));
            }
        });
        btnPre.performClick();

//        ImageView imgClient;
//        TitilliumSemiBold txtClientName, txtDate,txtDiaryHeading;
//        LinearLayout llEditDiary, llAddDiary;
//        TitilliumRegularEdit etDetails;
//        ProgressBar pBar;
//        ScrollView scrView;

        imgClient = (ImageView) fView.findViewById(R.id.img_client);

        txtClientName = (TitilliumSemiBold) fView.findViewById(R.id.txt_client_name);
        txtDate = (TitilliumSemiBold) fView.findViewById(R.id.txt_date);
        txtDiaryHeading = (TitilliumSemiBold) fView.findViewById(R.id.txt_diaryheading);
        txtDiaryHeading.setVisibility(View.GONE);

        llEditDiary = (LinearLayout) fView.findViewById(R.id.ll_editdiary);
        llAddDiary = (LinearLayout) fView.findViewById(R.id.ll_adddiary);
        llAddDiary.setVisibility(View.GONE);
        llEditDiary.setVisibility(View.GONE);

        etDetails = (TitilliumRegularEdit) fView.findViewById(R.id.et_details);
        etDetails.setEnabled(false);
        // etDetails.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

//        etDetails.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//
//                    return true;
//                }
//                return false;
//            }
//        });

        pBar = (ProgressBar) fView.findViewById(R.id.pbar);
        pBar.setVisibility(View.GONE);

        scrView = (ScrollView) fView.findViewById(R.id.scrollView1maincon);
        scrView.setVisibility(View.GONE);

        llEditDiary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etDetails.setEnabled(true);
            }
        });

        llAddDiary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etDetails.setEnabled(true);
                etDetails.setText("");
            }
        });

        if (cd.isConnectingToInternet()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            getDiaryDetails(dateFormat.format(calendar.getTime()));
        } else {
            Toast.makeText(getActivity(), "No internet coinnection.", Toast.LENGTH_SHORT).show();
        }

        params = llCoverLayout.getLayoutParams();
        showCalender.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (isBottom) {
                    llCoverLayout.clearAnimation();
                    TranslateAnimation tAnim = new TranslateAnimation(0.0f, 0.0f,
                            (llCoverLayout.getY()), 0.0f);
                    tAnim.setDuration(500);
                    tAnim.setFillAfter(true);
                    tAnim.setInterpolator(new AnticipateOvershootInterpolator(1.0f, 1.0f));
                    llCoverLayout.startAnimation(tAnim);
                    tAnim.setAnimationListener(new
                                                       Animation.AnimationListener() {

                                                           @Override
                                                           public void onAnimationEnd(Animation arg0) {

                                                               llCoverLayout.clearAnimation();
                                                               llCoverLayout.setY(ll_header.getHeight());

                                                               txtView.setText("Calendar");
                                                               isBottom = false;

                                                               params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                                                               llCoverLayout.setLayoutParams(params);
                                                           }

                                                           @Override
                                                           public void onAnimationRepeat(Animation arg0) { // TODO


                                                           }

                                                           @Override
                                                           public void onAnimationStart(Animation arg0) { // TODO


                                                           }

                                                       });
                } else {
                    llCoverLayout.clearAnimation();
                    TranslateAnimation tAnim = new TranslateAnimation(0.0f, 0.0f, 0.0f, llMiddle.getHeight());
                    tAnim.setDuration(500);
                    tAnim.setFillAfter(true);
                    tAnim.setInterpolator(new AnticipateOvershootInterpolator(1.0f, 1.0f));
                    llCoverLayout.startAnimation(tAnim);
                    tAnim.setAnimationListener(new
                                                       Animation.AnimationListener() {

                                                           @Override
                                                           public void onAnimationEnd(Animation arg0) {

                                                               llCoverLayout.clearAnimation();
                                                               llCoverLayout.setY((llMiddle.getHeight() + ll_header.getHeight()));

                                                               txtView.setText("Hide");
                                                               isBottom = true;

                                                               params.height = llCoverLayout.getHeight() -
                                                                       (llMiddle.getHeight() - ll_header.getHeight());

                                                               llCoverLayout.setLayoutParams(params);
                                                           }

                                                           @Override
                                                           public void onAnimationRepeat(Animation arg0) { // TODO


                                                           }

                                                           @Override
                                                           public void onAnimationStart(Animation arg0) { // TODO


                                                           }

                                                       });
                }
            }

        });


        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                fragmentTransaction = fragmentManager.beginTransaction();
                CalenderFragment cal_fragment = new CalenderFragment();
                fragmentTransaction.replace(R.id.fragment_container,
                        cal_fragment);
                fragmentTransaction.commit();
            }
        });


        //LinearLayout scrl_lay = (LinearLayout) fView.findViewById(R.id.scrl_lay);

//        scrl_lay.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View arg0, MotionEvent event) {
//                arg0.getParent().requestDisallowInterceptTouchEvent(true);
//                return
//                        false;
//            }
//        });

//        coverLayout.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(final View view, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    if (balencing_coefficient_ == -1000.0f) {
//                        balencing_coefficient_ = event.getY();
//                    }
//                    if (CENTRE_Y_OF_THE_SCREEN
//                            == 0.0f) {
//                        CENTRE_Y_OF_THE_SCREEN = view.getHeight() / 2.0f;
//                    }
//                    if
//                            (OPEN_CLOSE_BOUNDERY_Y == 0.0f) {
//                        OPEN_CLOSE_BOUNDERY_Y =
//                                CENTRE_Y_OF_THE_SCREEN / 2.0f;
//                    } // *******************************
//                    float act_position_ = event.getRawY() - balencing_coefficient_;
//                    if
//                            (act_position_ < CENTRE_Y_OF_THE_SCREEN && act_position_ >= 0.0f) {
//                        view.setY(act_position_);
//                        System.out.println("actual position : " +
//                                act_position_);
//                    } else if (act_position_ < (-1 *
//                            (OPEN_CLOSE_BOUNDERY_Y / 3.0f))) {
//                        view.setY(act_position_ / 3.0f);
//                    } else if (act_position_ < (-1 *
//                            (OPEN_CLOSE_BOUNDERY_Y / 1.50f))) {
//                        view.setY(act_position_ / 5.0f);
//                    } else if (act_position_ < (-1
//                    OPEN_CLOSE_BOUNDERY_Y)){
//                        view.setY(act_position_ / 7.0f);
//                    }
//                } else if (event.getAction() ==
//                        MotionEvent.ACTION_UP) {
//                    balencing_coefficient_ = -1000.0f; // *****
//                    System.out.println("final actual position : " + view.getY());
//                    if
//                            (view.getY() < OPEN_CLOSE_BOUNDERY_Y) {
//                        view.clearAnimation();
//                        TranslateAnimation tAnim = new TranslateAnimation(0.0f, 0.0f, 0.0f,
//                                (-view.getY()));
//                        tAnim.setDuration(300);
//                        tAnim.setFillAfter(true);
//                        tAnim.setInterpolator(new AnticipateOvershootInterpolator(1.0f,
//                                1.0f));
//                        view.startAnimation(tAnim);
//                        tAnim.setAnimationListener(new
//                                                           Animation.AnimationListener() {
//
//                                                               @Override
//                                                               public void onAnimationEnd(Animation arg0) { // TODO
//                                                                   view.clearAnimation();
//                                                                   view.setY(0.0f);
//                                                               }
//
//                                                               @Override
//                                                               public void onAnimationRepeat(Animation arg0) { // TODO
//
//
//                                                               }
//
//                                                               @Override
//                                                               public void onAnimationStart(Animation arg0) { // TODO
//
//
//                                                               }
//
//                                                           });
//                    } else {
//                        view.clearAnimation();
//                        TranslateAnimation tAnim = new
//                                TranslateAnimation(0.0f, 0.0f, 0.0f, (CENTRE_Y_OF_THE_SCREEN - view
//                                .getY()));
//                        tAnim.setDuration(300);
//                        tAnim.setFillAfter(true);
//                        tAnim.setInterpolator(new AnticipateOvershootInterpolator(2.0f,
//                                1.0f));
//                        view.startAnimation(tAnim);
//                        tAnim.setAnimationListener(new
//                                                           Animation.AnimationListener() {
//                                                               @Override
//                                                               public void onAnimationEnd(Animation arg0) { // TODO
//                                                                   view.clearAnimation();
//                                                                   view.setY(CENTRE_Y_OF_THE_SCREEN);
//                                                               }
//
//                                                               @Override
//                                                               public void onAnimationRepeat(Animation arg0) { // TODO
//
//                                                               }
//
//                                                               @Override
//                                                               public void onAnimationStart(Animation arg0) { // TODO
//
//                                                               }
//
//                                                           });
//                    }
//
//                }
//
//                return true;
//            }
//
//        });


        return fView;
    }

    public void getDiaryDetails(final String dateVal) {
        AsyncTask<Void, Void, Void> dietListDetails = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);
                scrView.setVisibility(View.GONE);

                etDetails.setEnabled(false);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/get_date_respective_diary?logged_in_user=" + AppConfig.loginDatatype.getSiteUserId()
                            + "&date_val=" + dateVal);
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

                    try {
                        JSONObject jOBJ = new JSONObject(urlResponse);
                        dateRespectiveDiaryDataType = new DateRespectiveDiaryDataType(
                                jOBJ.getString("client_id"),
                                jOBJ.getString("client_name"),
                                jOBJ.getString("client_image"),
                                jOBJ.getString("client_email"),
                                jOBJ.getString("client_about"),
                                jOBJ.getString("diary_id"),
                                jOBJ.getString("diary_heading"),
                                jOBJ.getString("dairy_text")
                        );

                    } catch (Exception ex) {
                        exception = ex.toString();
                    }


                    Log.d("Diary @@ URL : ", "http://esolz.co.in/lab6/ptplanner/app_control/get_date_respective_diary?logged_in_user=" + AppConfig.loginDatatype.getSiteUserId()
                            + "&date_val=" + dateVal);

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
                scrView.setVisibility(View.VISIBLE);
                if (exception.equals("")) {
                    Picasso.with(getActivity()).load(dateRespectiveDiaryDataType.getClient_image())
                            .transform(new Trns()).resize(400, 400).centerInside().into(imgClient);

                    txtClientName.setText(dateRespectiveDiaryDataType.getClient_name());
                    txtDate.setText(dateVal);

                    if (dateRespectiveDiaryDataType.getDiary_heading().equals("")) {
                        txtDiaryHeading.setVisibility(View.GONE);
                    } else {
                        txtDiaryHeading.setVisibility(View.VISIBLE);
                        txtDiaryHeading.setText(dateRespectiveDiaryDataType.getDiary_heading());
                    }

                    if (dateRespectiveDiaryDataType.getDairy_text().equals("")) {
                        llAddDiary.setVisibility(View.VISIBLE);
                        llEditDiary.setVisibility(View.GONE);
                    } else {
                        llAddDiary.setVisibility(View.GONE);
                        llEditDiary.setVisibility(View.VISIBLE);

                        etDetails.setText(dateRespectiveDiaryDataType.getDairy_text());
                    }


                } else {
                    Toast.makeText(getActivity(), "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        dietListDetails.execute();
    }

    public void getCalendar(int currentMonth, int indexOfDayOne, int curyear) {

        int i = 0, j = 0, k = 0, day = 1;
        int currentMonthLength = ReturnCalendarDetails.getMonthLenth(
                currentMonth, curyear);

        Calendar current = (Calendar) calendar.clone();

        current.set(Calendar.MONTH, currentMonth - 1);
        current.set(Calendar.YEAR, curyear);
        current.set(Calendar.DATE, currentDate);

        SimpleDateFormat sformat = new SimpleDateFormat("MMMM");

        txt_currentdatemonth.setText("" + sformat.format(current.getTime()));
        txt_currentyear.setText("" + curyear);

        int today = current.get(Calendar.DATE);

        drawEvent(indexOfDayOne);

        // getAllEvent();

        // --- Set Current Month
        for (i = indexOfDayOne; i < (currentMonthLength + indexOfDayOne); i++) {
            textViewArray[i].setText("" + day);
            textViewArray[i].setTag("" + day);

            textViewArray[i].setTextColor(Color.BLACK);
            day++;

            if (currentMonthLength > today) {

                if (textViewArray[(today + indexOfDayOne) - 1].getText()
                        .toString().equals("" + today)) {
                    textViewArray[(today + indexOfDayOne) - 1]
                            .setTextColor(Color.parseColor("#FF0000"));
                }
            } else {

                if (textViewArray[(today + indexOfDayOne) - 1].getText()
                        .toString().equals("" + today)) {
                    llArray[(today + indexOfDayOne) - 2]
                            .setBackgroundResource(R.drawable.selected_day);
                    textViewArray[(today + indexOfDayOne) - 1]
                            .setTextColor(Color.parseColor("#22A7F0"));
                }
            }

            textViewArray[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dateChange = txt_currentyear.getText().toString() + "-"
                            + txt_currentdatemonth.getText().toString() + "-"
                            + view.getTag();
                    DateFormat originalFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
                    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = originalFormat.parse(dateChange);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    formattedDate = targetFormat.format(date);
                    Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
//                    getDiaryDetails(formattedDate);

                    llCoverLayout.clearAnimation();
                    TranslateAnimation tAnim = new TranslateAnimation(0.0f, 0.0f,
                            (llCoverLayout.getY()), 0.0f);
                    tAnim.setDuration(500);
                    tAnim.setFillAfter(true);
                    tAnim.setInterpolator(new AnticipateOvershootInterpolator(1.0f, 1.0f));
                    llCoverLayout.startAnimation(tAnim);
                    tAnim.setAnimationListener(new
                                                       Animation.AnimationListener() {

                                                           @Override
                                                           public void onAnimationEnd(Animation arg0) {

                                                               llCoverLayout.clearAnimation();
                                                               llCoverLayout.setY(ll_header.getHeight());

                                                               txtView.setText("Calendar");
                                                               isBottom = false;

                                                               params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                                                               llCoverLayout.setLayoutParams(params);

                                                               getDiaryDetails(formattedDate);
                                                           }

                                                           @Override
                                                           public void onAnimationRepeat(Animation arg0) { // TODO


                                                           }

                                                           @Override
                                                           public void onAnimationStart(Animation arg0) { // TODO


                                                           }

                                                       });

                }
            });
        }
        day = 1;

        // --- Set Next Month

        for (j = i; j < textViewArray.length; j++) {
            textViewArray[j].setText("" + day);
            textViewArray[j].setTextColor(Color.parseColor("#BDBDBD"));
            day++;
        }
        day = 1;

        if (currentMonth == 1) {
            currentMonth = 12;
            curyear--;
        } else {
            currentMonth--;
        }
        int tempcurrentMonthLength = ReturnCalendarDetails.getMonthLenth(
                currentMonth, curyear);

        if (indexOfDayOne != 0) {
            // --- Set Previous Month
            for (k = indexOfDayOne - 1; k >= 0; k--) {
                textViewArray[k].setText("" + tempcurrentMonthLength);
                textViewArray[k].setTextColor(Color.parseColor("#BDBDBD"));
                tempcurrentMonthLength--;
            }
        }

    }

    public void drawEvent(int indexOfDayOne) {
        arrDay = new ArrayList<EventDataType>();
        arrDay.clear();

        for (int i = 0; i < AppConfig.diaryArrayList.size(); i++) {
            if (AppConfig.diaryArrayList.get(i).getYear()
                    .equals(txt_currentyear.getText().toString())) {
                DateFormat originalFormat = new SimpleDateFormat("MM");
                DateFormat targetFormat = new SimpleDateFormat("MMMM");
                try {
                    date = originalFormat.parse(AppConfig.diaryArrayList.get(i)
                            .getMonth());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedMonth = targetFormat.format(date);
                if (formattedMonth.equals(txt_currentdatemonth.getText()
                        .toString())) {
                    eventDataType = new EventDataType(AppConfig.diaryArrayList
                            .get(i).getDay(), "diary", false);
                    arrDay.add(eventDataType);
                }
            }
        }

        for (int i = 0; i < arrDay.size(); i++) {
            for (int j = 0; j < textViewArray.length; j++) {

                String textValue = "";

                if (textViewArray[j].getText().toString().length() < 2) {
                    textValue = "0" + textViewArray[j].getText().toString();
                } else {
                    textValue = textViewArray[j].getText().toString();
                }
                if (textValue.equals(arrDay.get(i).getMarkedDay())) {
                    /*llArray[(indexOfDayOne - 1)
                            + Integer.parseInt(arrDay.get(i).getMarkedDay())]
							.setVisibility(View.VISIBLE);*/
                    llArray[(indexOfDayOne - 1)
                            + Integer.parseInt(arrDay.get(i).getMarkedDay())]
                            .setBackgroundResource(R.drawable.pen);
                }
            }

        }
    }

    public void getLayouts() {
        llArray = new RelativeLayout[42];
        llArray[0] = (RelativeLayout) fView.findViewById(R.id.ll7);
        llArray[1] = (RelativeLayout) fView.findViewById(R.id.ll8);
        llArray[2] = (RelativeLayout) fView.findViewById(R.id.ll9);
        llArray[3] = (RelativeLayout) fView.findViewById(R.id.ll10);
        llArray[4] = (RelativeLayout) fView.findViewById(R.id.ll11);
        llArray[5] = (RelativeLayout) fView.findViewById(R.id.ll12);
        llArray[6] = (RelativeLayout) fView.findViewById(R.id.ll13);
        llArray[7] = (RelativeLayout) fView.findViewById(R.id.ll14);
        llArray[8] = (RelativeLayout) fView.findViewById(R.id.ll15);
        llArray[9] = (RelativeLayout) fView.findViewById(R.id.ll16);
        llArray[10] = (RelativeLayout) fView.findViewById(R.id.ll17);
        llArray[11] = (RelativeLayout) fView.findViewById(R.id.ll18);
        llArray[12] = (RelativeLayout) fView.findViewById(R.id.ll19);
        llArray[13] = (RelativeLayout) fView.findViewById(R.id.ll20);
        llArray[14] = (RelativeLayout) fView.findViewById(R.id.ll21);
        llArray[15] = (RelativeLayout) fView.findViewById(R.id.ll22);
        llArray[16] = (RelativeLayout) fView.findViewById(R.id.ll23);
        llArray[17] = (RelativeLayout) fView.findViewById(R.id.ll24);
        llArray[18] = (RelativeLayout) fView.findViewById(R.id.ll25);
        llArray[19] = (RelativeLayout) fView.findViewById(R.id.ll26);
        llArray[20] = (RelativeLayout) fView.findViewById(R.id.ll27);
        llArray[21] = (RelativeLayout) fView.findViewById(R.id.ll28);
        llArray[22] = (RelativeLayout) fView.findViewById(R.id.ll29);
        llArray[23] = (RelativeLayout) fView.findViewById(R.id.ll30);
        llArray[24] = (RelativeLayout) fView.findViewById(R.id.ll31);
        llArray[25] = (RelativeLayout) fView.findViewById(R.id.ll32);
        llArray[26] = (RelativeLayout) fView.findViewById(R.id.ll33);
        llArray[27] = (RelativeLayout) fView.findViewById(R.id.ll34);
        llArray[28] = (RelativeLayout) fView.findViewById(R.id.ll35);
        llArray[29] = (RelativeLayout) fView.findViewById(R.id.ll36);
        llArray[30] = (RelativeLayout) fView.findViewById(R.id.ll37);
        llArray[31] = (RelativeLayout) fView.findViewById(R.id.ll38);
        llArray[32] = (RelativeLayout) fView.findViewById(R.id.ll39);
        llArray[33] = (RelativeLayout) fView.findViewById(R.id.ll40);
        llArray[34] = (RelativeLayout) fView.findViewById(R.id.ll41);
        llArray[35] = (RelativeLayout) fView.findViewById(R.id.ll42);
        llArray[36] = (RelativeLayout) fView.findViewById(R.id.ll43);
        llArray[37] = (RelativeLayout) fView.findViewById(R.id.ll44);
        llArray[38] = (RelativeLayout) fView.findViewById(R.id.ll45);
        llArray[39] = (RelativeLayout) fView.findViewById(R.id.ll46);
        llArray[40] = (RelativeLayout) fView.findViewById(R.id.ll47);
        llArray[41] = (RelativeLayout) fView.findViewById(R.id.ll48);

        for (int a = 0; a < llArray.length; a++) {
            llArray[a].setBackgroundColor(Color.TRANSPARENT);
        }

        textViewArray = new TitilliumRegular[42];
        textViewArray[0] = (TitilliumRegular) fView.findViewById(R.id.txt7);
        textViewArray[1] = (TitilliumRegular) fView.findViewById(R.id.txt8);
        textViewArray[2] = (TitilliumRegular) fView.findViewById(R.id.txt9);
        textViewArray[3] = (TitilliumRegular) fView.findViewById(R.id.txt10);
        textViewArray[4] = (TitilliumRegular) fView.findViewById(R.id.txt11);
        textViewArray[5] = (TitilliumRegular) fView.findViewById(R.id.txt12);
        textViewArray[6] = (TitilliumRegular) fView.findViewById(R.id.txt13);
        textViewArray[7] = (TitilliumRegular) fView.findViewById(R.id.txt14);
        textViewArray[8] = (TitilliumRegular) fView.findViewById(R.id.txt15);
        textViewArray[9] = (TitilliumRegular) fView.findViewById(R.id.txt16);
        textViewArray[10] = (TitilliumRegular) fView.findViewById(R.id.txt17);
        textViewArray[11] = (TitilliumRegular) fView.findViewById(R.id.txt18);
        textViewArray[12] = (TitilliumRegular) fView.findViewById(R.id.txt19);
        textViewArray[13] = (TitilliumRegular) fView.findViewById(R.id.txt20);
        textViewArray[14] = (TitilliumRegular) fView.findViewById(R.id.txt21);
        textViewArray[15] = (TitilliumRegular) fView.findViewById(R.id.txt22);
        textViewArray[16] = (TitilliumRegular) fView.findViewById(R.id.txt23);
        textViewArray[17] = (TitilliumRegular) fView.findViewById(R.id.txt24);
        textViewArray[18] = (TitilliumRegular) fView.findViewById(R.id.txt25);
        textViewArray[19] = (TitilliumRegular) fView.findViewById(R.id.txt26);
        textViewArray[20] = (TitilliumRegular) fView.findViewById(R.id.txt27);
        textViewArray[21] = (TitilliumRegular) fView.findViewById(R.id.txt28);
        textViewArray[22] = (TitilliumRegular) fView.findViewById(R.id.txt29);
        textViewArray[23] = (TitilliumRegular) fView.findViewById(R.id.txt30);
        textViewArray[24] = (TitilliumRegular) fView.findViewById(R.id.txt31);
        textViewArray[25] = (TitilliumRegular) fView.findViewById(R.id.txt32);
        textViewArray[26] = (TitilliumRegular) fView.findViewById(R.id.txt33);
        textViewArray[27] = (TitilliumRegular) fView.findViewById(R.id.txt34);
        textViewArray[28] = (TitilliumRegular) fView.findViewById(R.id.txt35);
        textViewArray[29] = (TitilliumRegular) fView.findViewById(R.id.txt36);
        textViewArray[30] = (TitilliumRegular) fView.findViewById(R.id.txt37);
        textViewArray[31] = (TitilliumRegular) fView.findViewById(R.id.txt38);
        textViewArray[32] = (TitilliumRegular) fView.findViewById(R.id.txt39);
        textViewArray[33] = (TitilliumRegular) fView.findViewById(R.id.txt40);
        textViewArray[34] = (TitilliumRegular) fView.findViewById(R.id.txt41);
        textViewArray[35] = (TitilliumRegular) fView.findViewById(R.id.txt42);
        textViewArray[36] = (TitilliumRegular) fView.findViewById(R.id.txt43);
        textViewArray[37] = (TitilliumRegular) fView.findViewById(R.id.txt44);
        textViewArray[38] = (TitilliumRegular) fView.findViewById(R.id.txt45);
        textViewArray[39] = (TitilliumRegular) fView.findViewById(R.id.txt46);
        textViewArray[40] = (TitilliumRegular) fView.findViewById(R.id.txt47);
        textViewArray[41] = (TitilliumRegular) fView.findViewById(R.id.txt48);

		/*
         * for (int i = 0; i < textViewArray.length; i++) {
		 * textViewArray[i].setOnClickListener(getActivity()); }
		 */

        eventViewArr = new LinearLayout[42];
        eventViewArr[0] = (LinearLayout) fView.findViewById(R.id.event_style1);
        eventViewArr[1] = (LinearLayout) fView.findViewById(R.id.event_style2);
        eventViewArr[2] = (LinearLayout) fView.findViewById(R.id.event_style3);
        eventViewArr[3] = (LinearLayout) fView.findViewById(R.id.event_style4);
        eventViewArr[4] = (LinearLayout) fView.findViewById(R.id.event_style5);
        eventViewArr[5] = (LinearLayout) fView.findViewById(R.id.event_style6);
        eventViewArr[6] = (LinearLayout) fView.findViewById(R.id.event_style7);
        eventViewArr[7] = (LinearLayout) fView.findViewById(R.id.event_style8);
        eventViewArr[8] = (LinearLayout) fView.findViewById(R.id.event_style9);
        eventViewArr[9] = (LinearLayout) fView.findViewById(R.id.event_style10);
        eventViewArr[10] = (LinearLayout) fView
                .findViewById(R.id.event_style11);
        eventViewArr[11] = (LinearLayout) fView
                .findViewById(R.id.event_style12);
        eventViewArr[12] = (LinearLayout) fView
                .findViewById(R.id.event_style13);
        eventViewArr[13] = (LinearLayout) fView
                .findViewById(R.id.event_style14);
        eventViewArr[14] = (LinearLayout) fView
                .findViewById(R.id.event_style15);
        eventViewArr[15] = (LinearLayout) fView
                .findViewById(R.id.event_style16);
        eventViewArr[16] = (LinearLayout) fView
                .findViewById(R.id.event_style17);
        eventViewArr[17] = (LinearLayout) fView
                .findViewById(R.id.event_style18);
        eventViewArr[18] = (LinearLayout) fView
                .findViewById(R.id.event_style19);
        eventViewArr[19] = (LinearLayout) fView
                .findViewById(R.id.event_style20);
        eventViewArr[20] = (LinearLayout) fView
                .findViewById(R.id.event_style21);
        eventViewArr[21] = (LinearLayout) fView
                .findViewById(R.id.event_style22);
        eventViewArr[22] = (LinearLayout) fView
                .findViewById(R.id.event_style23);
        eventViewArr[23] = (LinearLayout) fView
                .findViewById(R.id.event_style24);
        eventViewArr[24] = (LinearLayout) fView
                .findViewById(R.id.event_style25);
        eventViewArr[25] = (LinearLayout) fView
                .findViewById(R.id.event_style26);
        eventViewArr[26] = (LinearLayout) fView
                .findViewById(R.id.event_style27);
        eventViewArr[27] = (LinearLayout) fView
                .findViewById(R.id.event_style28);
        eventViewArr[28] = (LinearLayout) fView
                .findViewById(R.id.event_style29);
        eventViewArr[29] = (LinearLayout) fView
                .findViewById(R.id.event_style30);
        eventViewArr[30] = (LinearLayout) fView
                .findViewById(R.id.event_style31);
        eventViewArr[31] = (LinearLayout) fView
                .findViewById(R.id.event_style32);
        eventViewArr[32] = (LinearLayout) fView
                .findViewById(R.id.event_style33);
        eventViewArr[33] = (LinearLayout) fView
                .findViewById(R.id.event_style34);
        eventViewArr[34] = (LinearLayout) fView
                .findViewById(R.id.event_style35);
        eventViewArr[35] = (LinearLayout) fView
                .findViewById(R.id.event_style36);
        eventViewArr[36] = (LinearLayout) fView
                .findViewById(R.id.event_style37);
        eventViewArr[37] = (LinearLayout) fView
                .findViewById(R.id.event_style38);
        eventViewArr[38] = (LinearLayout) fView
                .findViewById(R.id.event_style39);
        eventViewArr[39] = (LinearLayout) fView
                .findViewById(R.id.event_style40);
        eventViewArr[40] = (LinearLayout) fView
                .findViewById(R.id.event_style41);
        eventViewArr[41] = (LinearLayout) fView
                .findViewById(R.id.event_style42);

        for (int i = 0; i < eventViewArr.length; i++) {
            eventViewArr[i].setVisibility(View.GONE);
        }

        txtApp = new TitilliumRegular[42];
        txtApp[0] = (TitilliumRegular) fView.findViewById(R.id.app1);
        txtApp[1] = (TitilliumRegular) fView.findViewById(R.id.app2);
        txtApp[2] = (TitilliumRegular) fView.findViewById(R.id.app3);
        txtApp[3] = (TitilliumRegular) fView.findViewById(R.id.app4);
        txtApp[4] = (TitilliumRegular) fView.findViewById(R.id.app5);
        txtApp[5] = (TitilliumRegular) fView.findViewById(R.id.app6);
        txtApp[6] = (TitilliumRegular) fView.findViewById(R.id.app7);
        txtApp[7] = (TitilliumRegular) fView.findViewById(R.id.app8);
        txtApp[8] = (TitilliumRegular) fView.findViewById(R.id.app9);
        txtApp[9] = (TitilliumRegular) fView.findViewById(R.id.app10);
        txtApp[10] = (TitilliumRegular) fView.findViewById(R.id.app11);
        txtApp[11] = (TitilliumRegular) fView.findViewById(R.id.app12);
        txtApp[12] = (TitilliumRegular) fView.findViewById(R.id.app13);
        txtApp[13] = (TitilliumRegular) fView.findViewById(R.id.app14);
        txtApp[14] = (TitilliumRegular) fView.findViewById(R.id.app15);
        txtApp[15] = (TitilliumRegular) fView.findViewById(R.id.app16);
        txtApp[16] = (TitilliumRegular) fView.findViewById(R.id.app17);
        txtApp[17] = (TitilliumRegular) fView.findViewById(R.id.app18);
        txtApp[18] = (TitilliumRegular) fView.findViewById(R.id.app19);
        txtApp[19] = (TitilliumRegular) fView.findViewById(R.id.app20);
        txtApp[20] = (TitilliumRegular) fView.findViewById(R.id.app21);
        txtApp[21] = (TitilliumRegular) fView.findViewById(R.id.app22);
        txtApp[22] = (TitilliumRegular) fView.findViewById(R.id.app23);
        txtApp[23] = (TitilliumRegular) fView.findViewById(R.id.app24);
        txtApp[24] = (TitilliumRegular) fView.findViewById(R.id.app25);
        txtApp[25] = (TitilliumRegular) fView.findViewById(R.id.app26);
        txtApp[26] = (TitilliumRegular) fView.findViewById(R.id.app27);
        txtApp[27] = (TitilliumRegular) fView.findViewById(R.id.app28);
        txtApp[28] = (TitilliumRegular) fView.findViewById(R.id.app29);
        txtApp[29] = (TitilliumRegular) fView.findViewById(R.id.app30);
        txtApp[30] = (TitilliumRegular) fView.findViewById(R.id.app31);
        txtApp[31] = (TitilliumRegular) fView.findViewById(R.id.app32);
        txtApp[32] = (TitilliumRegular) fView.findViewById(R.id.app33);
        txtApp[33] = (TitilliumRegular) fView.findViewById(R.id.app34);
        txtApp[34] = (TitilliumRegular) fView.findViewById(R.id.app35);
        txtApp[35] = (TitilliumRegular) fView.findViewById(R.id.app36);
        txtApp[36] = (TitilliumRegular) fView.findViewById(R.id.app37);
        txtApp[37] = (TitilliumRegular) fView.findViewById(R.id.app38);
        txtApp[38] = (TitilliumRegular) fView.findViewById(R.id.app39);
        txtApp[39] = (TitilliumRegular) fView.findViewById(R.id.app40);
        txtApp[40] = (TitilliumRegular) fView.findViewById(R.id.app41);
        txtApp[41] = (TitilliumRegular) fView.findViewById(R.id.app42);

        for (int i = 0; i < txtApp.length; i++) {
            txtApp[i].setVisibility(View.GONE);
        }

    }

    // ********************************************Animation maker
    public Animation fromAtoB(float fromX, float fromY, float toX, float toY,
                              int speed) {
        Animation fromAtoB = new TranslateAnimation(Animation.ABSOLUTE, fromX,
                Animation.ABSOLUTE, toX, Animation.ABSOLUTE, fromY,
                Animation.ABSOLUTE, toY);
        fromAtoB.setFillAfter(true);
        fromAtoB.setDuration(speed);
        // fromAtoB.setInterpolator(new AccelerateDecelerateInterpolator());//
        // new
        // AnticipateOvershootInterpolator(1.0f));
        return fromAtoB;
    }

}
