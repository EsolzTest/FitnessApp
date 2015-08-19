package com.esolz.fitnessapp.dialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.datatype.AppointDataType;
import com.esolz.fitnessapp.datatype.CalendarEventDataType;
import com.esolz.fitnessapp.datatype.ClickDateDataType;
import com.esolz.fitnessapp.datatype.DiaryDataType;
import com.esolz.fitnessapp.datatype.EventDataType;
import com.esolz.fitnessapp.datatype.MealDateDataType;
import com.esolz.fitnessapp.datatype.ProgramDateDataType;
import com.esolz.fitnessapp.fragment.BookAppointmentFragment;
import com.esolz.fitnessapp.fragment.CalenderFragment;
import com.esolz.fitnessapp.fragment.DietFragment;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ReturnCalendarDetails;

public class ShowCalendarPopUp extends PopupWindow implements OnClickListener {

    Context context;
    String TYPE = "";
    TitilliumRegular[] textViewArray, txtApp;
    RelativeLayout[] llArray;
    LinearLayout[] eventViewArr;
    TitilliumRegular txt_currentdatemonth, txt_currentyear;
    LinearLayout llHide, btnNext, btnPre;
    View popupView;
    String dateChange = "";
    SimpleDateFormat simpleDateFormat;

    // -- Calendar Instance
    Calendar calendar;
    int currentYear, currentMonth, currentDay, currentDate, firstDayPosition,
            currentMonthLength, previousDayPosition, nextDayPosition;

    String month = "", day = "";
    Date date;

    int l;
    String[] positionPre = {}, positionNext = {};

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;

    String urlResponse = "", exception = "";
    JSONArray jArrAppointment, jArrProgram, jArrMeal, jArrDiary;
    CalendarEventDataType calEventData;
    AppointDataType appointDataType;
    ProgramDateDataType programDateDataType;
    MealDateDataType mealDateDataType;
    DiaryDataType diaryDataType;
    ArrayList<EventDataType> arrDay;
    ArrayList<EventDataType> appDay;
    EventDataType eventDataType;
    String[] AppointmentData, ProgramData, MealData, DiaryData;
    ClickDateDataType clickDateDataType;

    public ShowCalendarPopUp(final Context context, final String TYPE) {
        super(context);

        this.context = context;
        this.TYPE = TYPE;

        setContentView(LayoutInflater.from(context).inflate(
                R.layout.popup_showcalender, null));
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupView = getContentView();
        setFocusable(true);

        llHide = (LinearLayout) popupView.findViewById(R.id.ll_hide);
        txt_currentyear = (TitilliumRegular) popupView
                .findViewById(R.id.txt_currentyear);
        txt_currentdatemonth = (TitilliumRegular) popupView
                .findViewById(R.id.txt_currentdatemonth);
        btnPre = (LinearLayout) popupView.findViewById(R.id.btn_pre);
        btnNext = (LinearLayout) popupView.findViewById(R.id.btn_next);

        calendar = Calendar.getInstance(Locale.getDefault());

        currentDate = calendar.get(Calendar.DATE);
        currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        currentMonth = (calendar.get(Calendar.MONTH) + 1);
        currentYear = calendar.get(Calendar.YEAR);
        firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        getLayouts();

        if (AppConfig.appointmentArrayList.size() <= 0) {
            getAllEvent();
        }

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

        llHide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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

            textViewArray[i].setOnClickListener(this);

            String textValue = "";

//            if (textViewArray[i].getText().toString().length() < 2) {
//                textValue = "0" + textViewArray[i].getText().toString();
//            } else {
//                textValue = textViewArray[i].getText().toString();
//            }
//
//            try {
//                if (textValue.equals(clickDateDataType.getDay())) {
//                    textViewArray[i].setTextColor(Color.parseColor("#FAAC58"));
//                    textViewArray[i].setBackgroundResource(R.drawable.selected_day);
//                }
//            } catch (Exception e) {
//                Log.i("ex tag : ", e.toString());
//            }


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

    public void getLayouts() {
        llArray = new RelativeLayout[42];
        llArray[0] = (RelativeLayout) popupView.findViewById(R.id.ll7);
        llArray[1] = (RelativeLayout) popupView.findViewById(R.id.ll8);
        llArray[2] = (RelativeLayout) popupView.findViewById(R.id.ll9);
        llArray[3] = (RelativeLayout) popupView.findViewById(R.id.ll10);
        llArray[4] = (RelativeLayout) popupView.findViewById(R.id.ll11);
        llArray[5] = (RelativeLayout) popupView.findViewById(R.id.ll12);
        llArray[6] = (RelativeLayout) popupView.findViewById(R.id.ll13);
        llArray[7] = (RelativeLayout) popupView.findViewById(R.id.ll14);
        llArray[8] = (RelativeLayout) popupView.findViewById(R.id.ll15);
        llArray[9] = (RelativeLayout) popupView.findViewById(R.id.ll16);
        llArray[10] = (RelativeLayout) popupView.findViewById(R.id.ll17);
        llArray[11] = (RelativeLayout) popupView.findViewById(R.id.ll18);
        llArray[12] = (RelativeLayout) popupView.findViewById(R.id.ll19);
        llArray[13] = (RelativeLayout) popupView.findViewById(R.id.ll20);
        llArray[14] = (RelativeLayout) popupView.findViewById(R.id.ll21);
        llArray[15] = (RelativeLayout) popupView.findViewById(R.id.ll22);
        llArray[16] = (RelativeLayout) popupView.findViewById(R.id.ll23);
        llArray[17] = (RelativeLayout) popupView.findViewById(R.id.ll24);
        llArray[18] = (RelativeLayout) popupView.findViewById(R.id.ll25);
        llArray[19] = (RelativeLayout) popupView.findViewById(R.id.ll26);
        llArray[20] = (RelativeLayout) popupView.findViewById(R.id.ll27);
        llArray[21] = (RelativeLayout) popupView.findViewById(R.id.ll28);
        llArray[22] = (RelativeLayout) popupView.findViewById(R.id.ll29);
        llArray[23] = (RelativeLayout) popupView.findViewById(R.id.ll30);
        llArray[24] = (RelativeLayout) popupView.findViewById(R.id.ll31);
        llArray[25] = (RelativeLayout) popupView.findViewById(R.id.ll32);
        llArray[26] = (RelativeLayout) popupView.findViewById(R.id.ll33);
        llArray[27] = (RelativeLayout) popupView.findViewById(R.id.ll34);
        llArray[28] = (RelativeLayout) popupView.findViewById(R.id.ll35);
        llArray[29] = (RelativeLayout) popupView.findViewById(R.id.ll36);
        llArray[30] = (RelativeLayout) popupView.findViewById(R.id.ll37);
        llArray[31] = (RelativeLayout) popupView.findViewById(R.id.ll38);
        llArray[32] = (RelativeLayout) popupView.findViewById(R.id.ll39);
        llArray[33] = (RelativeLayout) popupView.findViewById(R.id.ll40);
        llArray[34] = (RelativeLayout) popupView.findViewById(R.id.ll41);
        llArray[35] = (RelativeLayout) popupView.findViewById(R.id.ll42);
        llArray[36] = (RelativeLayout) popupView.findViewById(R.id.ll43);
        llArray[37] = (RelativeLayout) popupView.findViewById(R.id.ll44);
        llArray[38] = (RelativeLayout) popupView.findViewById(R.id.ll45);
        llArray[39] = (RelativeLayout) popupView.findViewById(R.id.ll46);
        llArray[40] = (RelativeLayout) popupView.findViewById(R.id.ll47);
        llArray[41] = (RelativeLayout) popupView.findViewById(R.id.ll48);

        for (int a = 0; a < llArray.length; a++) {
            llArray[a].setBackgroundColor(Color.TRANSPARENT);
        }

        textViewArray = new TitilliumRegular[42];
        textViewArray[0] = (TitilliumRegular) popupView.findViewById(R.id.txt7);
        textViewArray[1] = (TitilliumRegular) popupView.findViewById(R.id.txt8);
        textViewArray[2] = (TitilliumRegular) popupView.findViewById(R.id.txt9);
        textViewArray[3] = (TitilliumRegular) popupView
                .findViewById(R.id.txt10);
        textViewArray[4] = (TitilliumRegular) popupView
                .findViewById(R.id.txt11);
        textViewArray[5] = (TitilliumRegular) popupView
                .findViewById(R.id.txt12);
        textViewArray[6] = (TitilliumRegular) popupView
                .findViewById(R.id.txt13);
        textViewArray[7] = (TitilliumRegular) popupView
                .findViewById(R.id.txt14);
        textViewArray[8] = (TitilliumRegular) popupView
                .findViewById(R.id.txt15);
        textViewArray[9] = (TitilliumRegular) popupView
                .findViewById(R.id.txt16);
        textViewArray[10] = (TitilliumRegular) popupView
                .findViewById(R.id.txt17);
        textViewArray[11] = (TitilliumRegular) popupView
                .findViewById(R.id.txt18);
        textViewArray[12] = (TitilliumRegular) popupView
                .findViewById(R.id.txt19);
        textViewArray[13] = (TitilliumRegular) popupView
                .findViewById(R.id.txt20);
        textViewArray[14] = (TitilliumRegular) popupView
                .findViewById(R.id.txt21);
        textViewArray[15] = (TitilliumRegular) popupView
                .findViewById(R.id.txt22);
        textViewArray[16] = (TitilliumRegular) popupView
                .findViewById(R.id.txt23);
        textViewArray[17] = (TitilliumRegular) popupView
                .findViewById(R.id.txt24);
        textViewArray[18] = (TitilliumRegular) popupView
                .findViewById(R.id.txt25);
        textViewArray[19] = (TitilliumRegular) popupView
                .findViewById(R.id.txt26);
        textViewArray[20] = (TitilliumRegular) popupView
                .findViewById(R.id.txt27);
        textViewArray[21] = (TitilliumRegular) popupView
                .findViewById(R.id.txt28);
        textViewArray[22] = (TitilliumRegular) popupView
                .findViewById(R.id.txt29);
        textViewArray[23] = (TitilliumRegular) popupView
                .findViewById(R.id.txt30);
        textViewArray[24] = (TitilliumRegular) popupView
                .findViewById(R.id.txt31);
        textViewArray[25] = (TitilliumRegular) popupView
                .findViewById(R.id.txt32);
        textViewArray[26] = (TitilliumRegular) popupView
                .findViewById(R.id.txt33);
        textViewArray[27] = (TitilliumRegular) popupView
                .findViewById(R.id.txt34);
        textViewArray[28] = (TitilliumRegular) popupView
                .findViewById(R.id.txt35);
        textViewArray[29] = (TitilliumRegular) popupView
                .findViewById(R.id.txt36);
        textViewArray[30] = (TitilliumRegular) popupView
                .findViewById(R.id.txt37);
        textViewArray[31] = (TitilliumRegular) popupView
                .findViewById(R.id.txt38);
        textViewArray[32] = (TitilliumRegular) popupView
                .findViewById(R.id.txt39);
        textViewArray[33] = (TitilliumRegular) popupView
                .findViewById(R.id.txt40);
        textViewArray[34] = (TitilliumRegular) popupView
                .findViewById(R.id.txt41);
        textViewArray[35] = (TitilliumRegular) popupView
                .findViewById(R.id.txt42);
        textViewArray[36] = (TitilliumRegular) popupView
                .findViewById(R.id.txt43);
        textViewArray[37] = (TitilliumRegular) popupView
                .findViewById(R.id.txt44);
        textViewArray[38] = (TitilliumRegular) popupView
                .findViewById(R.id.txt45);
        textViewArray[39] = (TitilliumRegular) popupView
                .findViewById(R.id.txt46);
        textViewArray[40] = (TitilliumRegular) popupView
                .findViewById(R.id.txt47);
        textViewArray[41] = (TitilliumRegular) popupView
                .findViewById(R.id.txt48);

        for (int i = 0; i < textViewArray.length; i++) {
            //textViewArray[i].setOnClickListener(this);
        }

        eventViewArr = new LinearLayout[42];
        eventViewArr[0] = (LinearLayout) popupView
                .findViewById(R.id.event_style1);
        eventViewArr[1] = (LinearLayout) popupView
                .findViewById(R.id.event_style2);
        eventViewArr[2] = (LinearLayout) popupView
                .findViewById(R.id.event_style3);
        eventViewArr[3] = (LinearLayout) popupView
                .findViewById(R.id.event_style4);
        eventViewArr[4] = (LinearLayout) popupView
                .findViewById(R.id.event_style5);
        eventViewArr[5] = (LinearLayout) popupView
                .findViewById(R.id.event_style6);
        eventViewArr[6] = (LinearLayout) popupView
                .findViewById(R.id.event_style7);
        eventViewArr[7] = (LinearLayout) popupView
                .findViewById(R.id.event_style8);
        eventViewArr[8] = (LinearLayout) popupView
                .findViewById(R.id.event_style9);
        eventViewArr[9] = (LinearLayout) popupView
                .findViewById(R.id.event_style10);
        eventViewArr[10] = (LinearLayout) popupView
                .findViewById(R.id.event_style11);
        eventViewArr[11] = (LinearLayout) popupView
                .findViewById(R.id.event_style12);
        eventViewArr[12] = (LinearLayout) popupView
                .findViewById(R.id.event_style13);
        eventViewArr[13] = (LinearLayout) popupView
                .findViewById(R.id.event_style14);
        eventViewArr[14] = (LinearLayout) popupView
                .findViewById(R.id.event_style15);
        eventViewArr[15] = (LinearLayout) popupView
                .findViewById(R.id.event_style16);
        eventViewArr[16] = (LinearLayout) popupView
                .findViewById(R.id.event_style17);
        eventViewArr[17] = (LinearLayout) popupView
                .findViewById(R.id.event_style18);
        eventViewArr[18] = (LinearLayout) popupView
                .findViewById(R.id.event_style19);
        eventViewArr[19] = (LinearLayout) popupView
                .findViewById(R.id.event_style20);
        eventViewArr[20] = (LinearLayout) popupView
                .findViewById(R.id.event_style21);
        eventViewArr[21] = (LinearLayout) popupView
                .findViewById(R.id.event_style22);
        eventViewArr[22] = (LinearLayout) popupView
                .findViewById(R.id.event_style23);
        eventViewArr[23] = (LinearLayout) popupView
                .findViewById(R.id.event_style24);
        eventViewArr[24] = (LinearLayout) popupView
                .findViewById(R.id.event_style25);
        eventViewArr[25] = (LinearLayout) popupView
                .findViewById(R.id.event_style26);
        eventViewArr[26] = (LinearLayout) popupView
                .findViewById(R.id.event_style27);
        eventViewArr[27] = (LinearLayout) popupView
                .findViewById(R.id.event_style28);
        eventViewArr[28] = (LinearLayout) popupView
                .findViewById(R.id.event_style29);
        eventViewArr[29] = (LinearLayout) popupView
                .findViewById(R.id.event_style30);
        eventViewArr[30] = (LinearLayout) popupView
                .findViewById(R.id.event_style31);
        eventViewArr[31] = (LinearLayout) popupView
                .findViewById(R.id.event_style32);
        eventViewArr[32] = (LinearLayout) popupView
                .findViewById(R.id.event_style33);
        eventViewArr[33] = (LinearLayout) popupView
                .findViewById(R.id.event_style34);
        eventViewArr[34] = (LinearLayout) popupView
                .findViewById(R.id.event_style35);
        eventViewArr[35] = (LinearLayout) popupView
                .findViewById(R.id.event_style36);
        eventViewArr[36] = (LinearLayout) popupView
                .findViewById(R.id.event_style37);
        eventViewArr[37] = (LinearLayout) popupView
                .findViewById(R.id.event_style38);
        eventViewArr[38] = (LinearLayout) popupView
                .findViewById(R.id.event_style39);
        eventViewArr[39] = (LinearLayout) popupView
                .findViewById(R.id.event_style40);
        eventViewArr[40] = (LinearLayout) popupView
                .findViewById(R.id.event_style41);
        eventViewArr[41] = (LinearLayout) popupView
                .findViewById(R.id.event_style42);

        for (int i = 0; i < eventViewArr.length; i++) {
            eventViewArr[i].setVisibility(View.GONE);
        }

        txtApp = new TitilliumRegular[42];
        txtApp[0] = (TitilliumRegular) popupView.findViewById(R.id.app1);
        txtApp[1] = (TitilliumRegular) popupView.findViewById(R.id.app2);
        txtApp[2] = (TitilliumRegular) popupView.findViewById(R.id.app3);
        txtApp[3] = (TitilliumRegular) popupView.findViewById(R.id.app4);
        txtApp[4] = (TitilliumRegular) popupView.findViewById(R.id.app5);
        txtApp[5] = (TitilliumRegular) popupView.findViewById(R.id.app6);
        txtApp[6] = (TitilliumRegular) popupView.findViewById(R.id.app7);
        txtApp[7] = (TitilliumRegular) popupView.findViewById(R.id.app8);
        txtApp[8] = (TitilliumRegular) popupView.findViewById(R.id.app9);
        txtApp[9] = (TitilliumRegular) popupView.findViewById(R.id.app10);
        txtApp[10] = (TitilliumRegular) popupView.findViewById(R.id.app11);
        txtApp[11] = (TitilliumRegular) popupView.findViewById(R.id.app12);
        txtApp[12] = (TitilliumRegular) popupView.findViewById(R.id.app13);
        txtApp[13] = (TitilliumRegular) popupView.findViewById(R.id.app14);
        txtApp[14] = (TitilliumRegular) popupView.findViewById(R.id.app15);
        txtApp[15] = (TitilliumRegular) popupView.findViewById(R.id.app16);
        txtApp[16] = (TitilliumRegular) popupView.findViewById(R.id.app17);
        txtApp[17] = (TitilliumRegular) popupView.findViewById(R.id.app18);
        txtApp[18] = (TitilliumRegular) popupView.findViewById(R.id.app19);
        txtApp[19] = (TitilliumRegular) popupView.findViewById(R.id.app20);
        txtApp[20] = (TitilliumRegular) popupView.findViewById(R.id.app21);
        txtApp[21] = (TitilliumRegular) popupView.findViewById(R.id.app22);
        txtApp[22] = (TitilliumRegular) popupView.findViewById(R.id.app23);
        txtApp[23] = (TitilliumRegular) popupView.findViewById(R.id.app24);
        txtApp[24] = (TitilliumRegular) popupView.findViewById(R.id.app25);
        txtApp[25] = (TitilliumRegular) popupView.findViewById(R.id.app26);
        txtApp[26] = (TitilliumRegular) popupView.findViewById(R.id.app27);
        txtApp[27] = (TitilliumRegular) popupView.findViewById(R.id.app28);
        txtApp[28] = (TitilliumRegular) popupView.findViewById(R.id.app29);
        txtApp[29] = (TitilliumRegular) popupView.findViewById(R.id.app30);
        txtApp[30] = (TitilliumRegular) popupView.findViewById(R.id.app31);
        txtApp[31] = (TitilliumRegular) popupView.findViewById(R.id.app32);
        txtApp[32] = (TitilliumRegular) popupView.findViewById(R.id.app33);
        txtApp[33] = (TitilliumRegular) popupView.findViewById(R.id.app34);
        txtApp[34] = (TitilliumRegular) popupView.findViewById(R.id.app35);
        txtApp[35] = (TitilliumRegular) popupView.findViewById(R.id.app36);
        txtApp[36] = (TitilliumRegular) popupView.findViewById(R.id.app37);
        txtApp[37] = (TitilliumRegular) popupView.findViewById(R.id.app38);
        txtApp[38] = (TitilliumRegular) popupView.findViewById(R.id.app39);
        txtApp[39] = (TitilliumRegular) popupView.findViewById(R.id.app40);
        txtApp[40] = (TitilliumRegular) popupView.findViewById(R.id.app41);
        txtApp[41] = (TitilliumRegular) popupView.findViewById(R.id.app42);

        for (int i = 0; i < txtApp.length; i++) {
            txtApp[i].setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        dateChange = txt_currentyear.getText().toString() + "-"
                + txt_currentdatemonth.getText().toString() + "-"
                + view.getTag();
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MMM-dd",
                Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = originalFormat.parse(dateChange);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        AppConfig.changeDate = formattedDate;

        bundle = new Bundle();
        bundle.putString("DateChange", formattedDate);
        bundle.putString("MONTH", txt_currentdatemonth.getText().toString());
        bundle.putString("DAY", view.getTag().toString());
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        clickDateDataType = new ClickDateDataType(
                view.getTag().toString(),
                txt_currentdatemonth.getText().toString(),
                txt_currentyear.getText().toString(),
                true
        );
        view.setBackgroundResource(R.drawable.selected_day);

        if (TYPE.equals("program")) {
            fragmentTransaction = fragmentManager.beginTransaction();
            CalenderFragment cal_fragment = new CalenderFragment();
            cal_fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, cal_fragment);
            fragmentTransaction.commit();
        } else if (TYPE.equals("appointment")) {
            fragmentTransaction = fragmentManager.beginTransaction();
            BookAppointmentFragment bookapp_fragment = new BookAppointmentFragment();
            bookapp_fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container,
                    bookapp_fragment);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction = fragmentManager.beginTransaction();
            DietFragment dietFragment = new DietFragment();
            dietFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container,
                    dietFragment);
            fragmentTransaction.commit();
        }

        dismiss();
    }

    public void drawEvent(int indexOfDayOne) {
        arrDay = new ArrayList<EventDataType>();
        appDay = new ArrayList<EventDataType>();


        if (TYPE.equals("program")) {
            for (int i = 0; i < AppConfig.programArrayList.size(); i++) {
                //for (int j = 0; j < AppConfig.appointmentArrayList.size(); j++) {

                if (AppConfig.programArrayList.get(i).getYear()
                        .equals(txt_currentyear.getText().toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("MM");
                    DateFormat targetFormat = new SimpleDateFormat("MMMM");
                    try {
                        date = originalFormat.parse(AppConfig.programArrayList
                                .get(i).getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedMonth = targetFormat.format(date);
                    if (formattedMonth.equals(txt_currentdatemonth.getText()
                            .toString())) {
                        eventDataType = new EventDataType(
                                AppConfig.programArrayList.get(i).getDay(),
                                TYPE, false);
                        arrDay.add(eventDataType);
                    }
                    // }
                }

                for (int j = 0; j < AppConfig.appointmentArrayList.size(); j++) {
                    if (AppConfig.appointmentArrayList.get(j).getYear().equals(txt_currentyear.getText().toString())) {
                        DateFormat originalFormat = new SimpleDateFormat("MM");
                        DateFormat targetFormat = new SimpleDateFormat("MMMM");
                        try {
                            date = originalFormat.parse(AppConfig.appointmentArrayList.get(j).getMonth());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedMonth = targetFormat.format(date);
                        if (formattedMonth.equals(txt_currentdatemonth.getText().toString())) {
                            eventDataType = new EventDataType(
                                    AppConfig.appointmentArrayList.get(j).getDay(),
                                    TYPE, true);
                            arrDay.add(eventDataType);
                        }
                    }
                }
            }
        } else if (TYPE.equals("appointment"))

        {
            for (int i = 0; i < AppConfig.appointmentArrayList.size(); i++) {

                //Log.i("-->> App : ArrLi : ", AppConfig.appointmentArrayList.get(i).getDay());

                if (AppConfig.appointmentArrayList.get(i).getYear()
                        .equals(txt_currentyear.getText().toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("MM");
                    DateFormat targetFormat = new SimpleDateFormat("MMMM");
                    try {
                        date = originalFormat
                                .parse(AppConfig.appointmentArrayList.get(i)
                                        .getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedMonth = targetFormat.format(date);
                    if (formattedMonth.equals(txt_currentdatemonth.getText()
                            .toString())) {
                        eventDataType = new EventDataType(
                                AppConfig.appointmentArrayList.get(i).getDay(),
                                TYPE, false);
                        arrDay.add(eventDataType);
                    }
                }
            }
        } else

        {
            for (int i = 0; i < AppConfig.mealArrayList.size(); i++) {
                if (AppConfig.mealArrayList.get(i).getYear()
                        .equals(txt_currentyear.getText().toString())) {
                    DateFormat originalFormat = new SimpleDateFormat("MM");
                    DateFormat targetFormat = new SimpleDateFormat("MMMM");
                    try {
                        date = originalFormat.parse(AppConfig.mealArrayList
                                .get(i).getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedMonth = targetFormat.format(date);
                    if (formattedMonth.equals(txt_currentdatemonth.getText()
                            .toString())) {
                        eventDataType = new EventDataType(
                                AppConfig.mealArrayList.get(i).getDay(), TYPE, false);
                        arrDay.add(eventDataType);
                    }
                }
            }
        }


        for (
                int j = 0;
                j < textViewArray.length; j++)

        {
            for (int i = 0; i < arrDay.size(); i++) {

                String textValue = "";

                if (textViewArray[j].getText().toString().length() < 2) {
                    textValue = "0" + textViewArray[j].getText().toString();
                } else {
                    textValue = textViewArray[j].getText().toString();
                }

                if (textValue.equals(arrDay.get(i).getMarkedDay())) {

                    if (arrDay.get(i).getTypeEvent().equals("appointment")) {

                        eventViewArr[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(arrDay.get(i).getMarkedDay())]
                                .setVisibility(View.VISIBLE);
                        txtApp[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(arrDay.get(i).getMarkedDay())]
                                .setVisibility(View.VISIBLE);
                    } else if (arrDay.get(i).getTypeEvent().equals("program")) {

                        if (arrDay.get(i).isSelected()) {
                            eventViewArr[(indexOfDayOne - 1)
                                    + Integer
                                    .parseInt(arrDay.get(i).getMarkedDay())]
                                    .setVisibility(View.VISIBLE);
                            txtApp[(indexOfDayOne - 1)
                                    + Integer
                                    .parseInt(arrDay.get(i).getMarkedDay())]
                                    .setVisibility(View.VISIBLE);
                        } else {
                            eventViewArr[(indexOfDayOne - 1)
                                    + Integer
                                    .parseInt(arrDay.get(i).getMarkedDay())]
                                    .setVisibility(View.VISIBLE);
                            txtApp[(indexOfDayOne - 1)
                                    + Integer
                                    .parseInt(arrDay.get(i).getMarkedDay())]
                                    .setVisibility(View.GONE);
                        }

                    } else {
                        eventViewArr[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(arrDay.get(i).getMarkedDay())]
                                .setVisibility(View.VISIBLE);
                        txtApp[(indexOfDayOne - 1)
                                + Integer
                                .parseInt(arrDay.get(i).getMarkedDay())]
                                .setVisibility(View.GONE);
                    }
                }
            }
        }

    }

    public void getAllEvent() {

        AsyncTask<Void, Void, Void> event = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    exception = "";
                    urlResponse = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(AppConfig.HOST
                            + "app_control/mark_calender?client_id="
                            + AppConfig.loginDatatype.getSiteUserId());
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

                    jArrProgram = jOBJ.getJSONArray("program_date");
                    jArrMeal = jOBJ.getJSONArray("meal_date");
                    jArrDiary = jOBJ.getJSONArray("diary_date");
                    jArrAppointment = jOBJ.getJSONArray("appointment_date");

                    calEventData = new CalendarEventDataType();
                    for (int i = 0; i < jArrProgram.length(); i++) {
                        calEventData.setProgram_date(jArrProgram.getString(i));

                        ProgramData = jArrProgram.getString(i).split("-");
                        programDateDataType = new ProgramDateDataType(
                                ProgramData[2], ProgramData[1], ProgramData[0]);

                        AppConfig.programArrayList.add(programDateDataType);
                    }
                    for (int j = 0; j < jArrMeal.length(); j++) {
                        calEventData.setMeal_date(jArrMeal.getString(j));

                        MealData = jArrMeal.getString(j).split("-");
                        mealDateDataType = new MealDateDataType(MealData[2],
                                MealData[1], MealData[0]);

                        AppConfig.mealArrayList.add(mealDateDataType);

                    }
                    for (int k = 0; k < jArrDiary.length(); k++) {
                        calEventData.setDiary_date(jArrDiary.getString(k));

                        DiaryData = jArrDiary.getString(k).split("-");
                        diaryDataType = new DiaryDataType(DiaryData[2],
                                DiaryData[1], DiaryData[0]);

                        AppConfig.diaryArrayList.add(diaryDataType);
                    }
                    for (int l = 0; l < jArrAppointment.length(); l++) {

                        String[] appDate = jArrAppointment.getString(l).split(" ");

                        calEventData.setAppointment_date(appDate[0]);

                        AppointmentData = appDate[0].split("-");
                        appointDataType = new AppointDataType(
                                AppointmentData[2], AppointmentData[1],
                                AppointmentData[0]);

                        AppConfig.appointmentArrayList.add(appointDataType);
                    }

                } catch (Exception e) {
                    exception = e.toString();
                }

                Log.i("@@MArk Cal Url : ", AppConfig.HOST
                        + "app_control/mark_calender?client_id="
                        + AppConfig.loginDatatype.getSiteUserId());

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (exception.equals("")) {
                } else {
                    System.out.println("@@ Exception: " + exception);
                    Toast.makeText(context, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        event.execute();
    }
}