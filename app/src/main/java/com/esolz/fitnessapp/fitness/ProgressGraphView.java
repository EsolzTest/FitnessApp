package com.esolz.fitnessapp.fitness;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.customviews.TitilliumRegular;
import com.esolz.fitnessapp.customviews.TitilliumSemiBold;
import com.esolz.fitnessapp.datatype.GraphDetailsDataType;
import com.esolz.fitnessapp.datatype.GraphDetailsPointDataType;
import com.esolz.fitnessapp.helper.AppConfig;
import com.esolz.fitnessapp.helper.ConnectionDetector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ProgressGraphView extends FragmentActivity {

    WebView webView;
    LinearLayout back;
    LinearLayout add_measurement;
    TitilliumSemiBold parentWeight, parentDeadline;
    TitilliumRegular txtWeightMesure;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    String exceptionGraphDetails = "", getUrlResponseGraphDetails = "", exception = "", responseUpdate = "";
    ConnectionDetector cd;
    ProgressBar graphBar;

    ArrayList<GraphDetailsDataType> graphDetailsDataTypeArrayList;
    GraphDetailsDataType graphDetailsDataType;

    ArrayList<GraphDetailsPointDataType> graphDetailsPointDataTypeArrayList;
    GraphDetailsPointDataType graphDetailsPointDataType;

    float num1, num2, num3, num4, num5, num6, num7, num8, num9, num10;
    String numYax1, numYax2, numYax3, numYax4, numYax5, numYax6, numYax7, numYax8, numYax9, numYax10;

    ArrayList<String> arrayListXAxispoint, arrayListYAxisPoint;

    Dialog dialogMeasurement;
    RelativeLayout rlBack;
    LinearLayout llDone, llMinus, llPlus;
    TitilliumSemiBold txtMeasurement;
    DatePicker pickerDate;
    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_graph_view);
        back = (LinearLayout) findViewById(R.id.back);
        add_measurement = (LinearLayout) findViewById(R.id.add_measurement);
        webView = (WebView) findViewById(R.id.webView);
        parentDeadline = (TitilliumSemiBold) findViewById(R.id.deadline);
        parentWeight = (TitilliumSemiBold) findViewById(R.id.weight);
        txtWeightMesure = (TitilliumRegular) findViewById(R.id.txt_wt_mesure);
        graphBar = (ProgressBar) findViewById(R.id.graph_pbar);

        fragmentManager = getSupportFragmentManager();

        cd = new ConnectionDetector(ProgressGraphView.this);

        dialogMeasurement = new Dialog(ProgressGraphView.this);
        dialogMeasurement.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMeasurement.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogMeasurement.getWindow().setGravity(Gravity.CENTER);
        dialogMeasurement.setContentView(R.layout.measurement_dialog);
        dialogMeasurement.setCanceledOnTouchOutside(true);

        rlBack = (RelativeLayout) dialogMeasurement.findViewById(R.id.ll_cancel);
        llDone = (LinearLayout) dialogMeasurement.findViewById(R.id.ll_done);
        llMinus = (LinearLayout) dialogMeasurement.findViewById(R.id.ll_minus);
        llPlus = (LinearLayout) dialogMeasurement.findViewById(R.id.ll_plus);
        txtMeasurement = (TitilliumSemiBold) dialogMeasurement.findViewById(R.id.txt_measurement);
        pickerDate = (DatePicker) dialogMeasurement.findViewById(R.id.picker_date);

        llPlus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                txtMeasurement.setText(""
                        + (Integer.parseInt(txtMeasurement.getText().toString()) + 1));
            }
        });

        llMinus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if ((Integer.parseInt(txtMeasurement.getText().toString()) - 1) > 0) {
                    txtMeasurement.setText(""
                            + (Integer
                            .parseInt(txtMeasurement.getText().toString()) - 1));
                }

            }
        });

        rlBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMeasurement.cancel();
            }
        });

        llDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMeasurement.cancel();

                mDay = pickerDate.getDayOfMonth();
                mMonth = pickerDate.getMonth() + 1;
                mYear = pickerDate.getYear();

                String stringDate = mYear + "-" + mMonth + "-" + mDay;

                if (cd.isConnectingToInternet()) {
                    updateMeasurement(
                            getIntent().getExtras().getString("GRAPH_ID"),
                            stringDate,
                            txtMeasurement.getText().toString()
                    );
                } else {
                    Toast.makeText(ProgressGraphView.this, "No internet connection.", Toast.LENGTH_LONG).show();
                }

            }
        });

        if (cd.isConnectingToInternet()) {
            getGraphDetailsById(getIntent().getExtras().getString("GRAPH_ID"));
        } else {
            Toast.makeText(ProgressGraphView.this, "No internet connection.", Toast.LENGTH_LONG).show();
        }

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        add_measurement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialogMeasurement.show();
            }
        });

    }

    private class FitnessWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            graphBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
    }

    public void updateMeasurement(final String graphID, final String date, final String measurement) {

        AsyncTask<Void, Void, Void> measurementUpdate = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                graphBar.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exception = "";
                    responseUpdate = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/add_measurement?graph_id=" + graphID + "&date=" + date + "&measurement=" + measurement);
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
                    responseUpdate = sb.toString();
                    JSONObject jOBJ = new JSONObject(responseUpdate);

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/add_measurement?graph_id=" + graphID + "&date=" + date + "&measurement=" + measurement);
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
                    getGraphDetailsById(graphID);
                } else {
                    Log.d("Exception : ", exception);
                    Toast.makeText(ProgressGraphView.this, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        measurementUpdate.execute();
    }


    public void getGraphDetailsById(final String graphID) {

        AsyncTask<Void, Void, Void> graphDetailsById = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                graphBar.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
                    exceptionGraphDetails = "";
                    getUrlResponseGraphDetails = "";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://esolz.co.in/lab6/ptplanner/app_control/graph_details?graph_id=" + graphID);
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
                    getUrlResponseGraphDetails = sb.toString();
                    JSONObject jOBJ = new JSONObject(getUrlResponseGraphDetails);

                    graphDetailsDataTypeArrayList = new ArrayList<GraphDetailsDataType>();
                    graphDetailsPointDataTypeArrayList = new ArrayList<GraphDetailsPointDataType>();

                    JSONArray jArr = jOBJ.getJSONArray("points");
                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject jsonObject = jArr.getJSONObject(i);

                        graphDetailsPointDataType = new GraphDetailsPointDataType(
                                jsonObject.getString("x_axis_point"),
                                jsonObject.getString("y_axis_point")
                        );
                        graphDetailsPointDataTypeArrayList.add(graphDetailsPointDataType);
                    }

                    graphDetailsDataType = new GraphDetailsDataType(
                            jOBJ.getString("client_name"),
                            jOBJ.getString("graph_type"),
                            jOBJ.getString("graph_for"),
                            jOBJ.getString("measure_unit"),
                            jOBJ.getString("goal"),
                            jOBJ.getString("deadline"),
                            graphDetailsPointDataTypeArrayList
                    );
                    graphDetailsDataTypeArrayList.add(graphDetailsDataType);

                    Log.d("RESPONSE", jOBJ.toString());
                    Log.d("URL", "http://esolz.co.in/lab6/ptplanner/app_control/graph_details?graph_id=" + graphID);
                } catch (Exception e) {
                    exceptionGraphDetails = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (exceptionGraphDetails.equals("")) {
                    Toast.makeText(ProgressGraphView.this, "Success....", Toast.LENGTH_LONG).show();

                    Log.i(" Point Size : ", "" + graphDetailsPointDataTypeArrayList.size());

                    arrayListXAxispoint = new ArrayList<String>();
                    arrayListYAxisPoint = new ArrayList<String>();

                    for (int i = 0; i < graphDetailsPointDataTypeArrayList.size(); i++) {
                        arrayListXAxispoint.add("" + graphDetailsPointDataTypeArrayList.get(i).getX_axis_point());
                        arrayListYAxisPoint.add("" + graphDetailsPointDataTypeArrayList.get(i).getY_axis_point());
                    }

                    try {

                        num1 = Float.parseFloat(arrayListYAxisPoint.get(0));
                        num2 = Float.parseFloat(arrayListYAxisPoint.get(1));
                        num3 = Float.parseFloat(arrayListYAxisPoint.get(2));
                        num4 = Float.parseFloat(arrayListYAxisPoint.get(3));
                        num5 = Float.parseFloat(arrayListYAxisPoint.get(4));
                        num6 = Float.parseFloat(arrayListYAxisPoint.get(5));
                        num7 = Float.parseFloat(arrayListYAxisPoint.get(6));
                        num8 = Float.parseFloat(arrayListYAxisPoint.get(7));
                        num9 = Float.parseFloat(arrayListYAxisPoint.get(8));
                        num10 = Float.parseFloat(arrayListYAxisPoint.get(9));

                        numYax1 = arrayListXAxispoint.get(0);
                        numYax2 = arrayListXAxispoint.get(1);
                        numYax3 = arrayListXAxispoint.get(2);
                        numYax4 = arrayListXAxispoint.get(3);
                        numYax5 = arrayListXAxispoint.get(4);
                        numYax6 = arrayListXAxispoint.get(5);
                        numYax7 = arrayListXAxispoint.get(6);
                        numYax8 = arrayListXAxispoint.get(7);
                        numYax9 = arrayListXAxispoint.get(8);
                        numYax10 = arrayListXAxispoint.get(9);

                    } catch (Exception e) {
                        Log.i("Graph set excep : ", "" + e.toString());
                    }

                    webView = (WebView) findViewById(R.id.webView);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.addJavascriptInterface(new WebAppInterface(), "Android");
                    webView.addJavascriptInterface(new WebAppInterfaceYAx(), "AndroidXAxis");
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setScrollbarFadingEnabled(true);
                    webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                    webView.setMapTrackballToArrowKeys(false); // use trackball directly
                    webView.getSettings().setBuiltInZoomControls(true);
                    webView.setWebViewClient(new FitnessWebViewClient());
                    webView.loadUrl("file:///android_asset/area_chart.html");

                    parentDeadline.setText(graphDetailsDataType.getDeadline());
                    parentWeight.setText(graphDetailsDataType.getGoal());

                    txtWeightMesure.setText("" + graphDetailsDataType.getMeasure_unit());

                } else {
                    Log.d("Exception : ", exceptionGraphDetails);
                    Toast.makeText(ProgressGraphView.this, "Server not responding....", Toast.LENGTH_LONG).show();
                }
            }

        };
        graphDetailsById.execute();

    }

    public class WebAppInterface {

        @JavascriptInterface
        public float getNum1() {
            return num10;
        }

        @JavascriptInterface
        public float getNum2() {
            return num9;
        }

        @JavascriptInterface
        public float getNum3() {
            return num8;
        }

        @JavascriptInterface
        public float getNum4() {
            return num7;
        }

        @JavascriptInterface
        public float getNum5() {
            return num6;
        }

        @JavascriptInterface
        public float getNum6() {
            return num5;
        }

        @JavascriptInterface
        public float getNum7() {
            return num4;
        }

        @JavascriptInterface
        public float getNum8() {
            return num2;
        }

        @JavascriptInterface
        public float getNum9() {
            return num2;
        }

        @JavascriptInterface
        public float getNum10() {
            return num1;
        }
    }

    public class WebAppInterfaceYAx {

        @JavascriptInterface
        public String getNumYax1() {
            try {
                return changeDateFormat(numYax10);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax2() {
            try {
                return changeDateFormat(numYax9);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax3() {
            try {
                return changeDateFormat(numYax8);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax4() {
            try {
                return changeDateFormat(numYax7);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax5() {
            try {
                return changeDateFormat(numYax6);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax6() {
            try {
                return changeDateFormat(numYax5);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax7() {
            try {
                return changeDateFormat(numYax4);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax8() {
            try {
                return changeDateFormat(numYax3);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax9() {
            try {
                return changeDateFormat(numYax2);
            } catch (Exception e) {
                return "";
            }
        }

        @JavascriptInterface
        public String getNumYax10() {
            try {
                return changeDateFormat(numYax1);
            } catch (Exception e) {
                return "";
            }
        }
    }

    public String changeDateFormat(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM");
        String finalDate = timeFormat.format(myDate);

        return finalDate;
    }


}