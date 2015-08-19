package com.esolz.fitnessapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.esolz.fitnessapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by ltp on 14/07/15.
 */
public class TrainingViewPagerAdapter extends PagerAdapter {


    Context context;
    String imgURL, videoUrl;
    LayoutInflater inflater;
    View itemview;
    ImageView imgExercise;
    WebView webView;

    public TrainingViewPagerAdapter(Context context, int i, String imgURL, String videoUrl) {
        this.context = context;
        this.imgURL = imgURL;
        this.videoUrl = videoUrl;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (position == 0) {
            itemview = inflater.inflate(R.layout.training_viewpager_adapter, container, false);
            imgExercise = (ImageView) itemview.findViewById(R.id.img_exercise);
            Picasso.with(context).load(imgURL).centerCrop().fit().into(imgExercise);
        } else {
            itemview = inflater.inflate(R.layout.training_view_pager_adaper_webview, container, false);
            webView = (WebView) itemview.findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setSupportMultipleWindows(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            webView.loadUrl(videoUrl);
        }

        ((ViewPager) container).addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}