package com.esolz.fitnessapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.ClientAllGraphDataType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ltp on 05/08/15.
 */
public class AllGraphAdapter extends ArrayAdapter<ClientAllGraphDataType> {

    Context context;
    LayoutInflater inflator;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;
    ViewHolder holder;
    ArrayList<ClientAllGraphDataType> clientAllGraphDataTypeLinkedList;

    float num1, num2, num3, num4, num5, num6, num7, num8, num9, num10;
    String numYax1, numYax2, numYax3, numYax4, numYax5, numYax6, numYax7, numYax8, numYax9, numYax10;

    ProgressBar pBar;

    public AllGraphAdapter(Context context, int resource, ArrayList<ClientAllGraphDataType> clientAllGraphDataTypeLinkedList) {
        super(context, resource, clientAllGraphDataTypeLinkedList);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.clientAllGraphDataTypeLinkedList = clientAllGraphDataTypeLinkedList;
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.allgraph_listitem, parent, false);
            holder = new ViewHolder();

            pBar = (ProgressBar) convertView.findViewById(R.id.pbar);
            pBar.setVisibility(View.GONE);
            holder.webView = (WebView) convertView.findViewById(R.id.webView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.i("LIST VALUE : ", clientAllGraphDataTypeLinkedList.get(position).
                getClientAllGraphPointDataTypeArrayList().get(0).getY_axis_point());

        try {

            num1 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(0).getY_axis_point());
            num2 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(1).getY_axis_point());
            num3 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(2).getY_axis_point());
            num4 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(3).getY_axis_point());
            num5 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(4).getY_axis_point());
            num6 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(5).getY_axis_point());
            num7 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(6).getY_axis_point());
            num8 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(7).getY_axis_point());
            num9 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(8).getY_axis_point());
            num10 = Float.parseFloat(clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(9).getY_axis_point());

            numYax1 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(0).getX_axis_point();
            numYax2 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(1).getX_axis_point();
            numYax3 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(2).getX_axis_point();
            numYax4 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(3).getX_axis_point();
            numYax5 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(4).getX_axis_point();
            numYax6 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(5).getX_axis_point();
            numYax7 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(6).getX_axis_point();
            numYax8 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(7).getX_axis_point();
            numYax9 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(8).getX_axis_point();
            numYax10 = clientAllGraphDataTypeLinkedList.get(position).
                    getClientAllGraphPointDataTypeArrayList().get(9).getX_axis_point();

        } catch (Exception e) {
            Log.i("Graph set excep : ", "" + e.toString());
        }

        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.addJavascriptInterface(new WebAppInterface(), "Android");
        holder.webView.addJavascriptInterface(new WebAppInterfaceYAx(), "AndroidXAxis");
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.setScrollbarFadingEnabled(true);
        holder.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        holder.webView.setMapTrackballToArrowKeys(false); // use trackball directly
        holder.webView.getSettings().setBuiltInZoomControls(true);
        holder.webView.setWebViewClient(new FitnessWebViewClient());
        holder.webView.loadUrl("file:///android_asset/area_chart_list.html");

        return convertView;
    }

    protected class ViewHolder {
        //ProgressBar pBar;
        WebView webView;
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
            pBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //pBar.setVisibility(View.VISIBLE);
        }
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
