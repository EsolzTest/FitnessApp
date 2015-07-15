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
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
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
    TitilliumSemiBold txtTitle;
    TitilliumRegular txtDescription, txtInstruction;

    String title, description, instruction;

    public ShowMorePopUp(final Context context, String title, String description, String instruction) {
        super(context);

        this.context = context;
        this.title = title;
        this.description = description;
        this.instruction = instruction;

        setContentView(LayoutInflater.from(context).inflate(R.layout.frag_more, null));
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupView = getContentView();
        setFocusable(true);

        llHide = (LinearLayout) popupView.findViewById(R.id.ll_hide);
        txtTitle = (TitilliumSemiBold) popupView.findViewById(R.id.txt_title);
        txtDescription = (TitilliumRegular) popupView.findViewById(R.id.txt_description);
        txtInstruction = (TitilliumRegular) popupView.findViewById(R.id.instruction);

        txtTitle.setText(title);
        txtDescription.setText(description);
        txtInstruction.setText(instruction);

        llHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
