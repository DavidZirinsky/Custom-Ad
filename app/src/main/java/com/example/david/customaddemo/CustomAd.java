package com.example.david.customaddemo;

/**
 * Created by david on 1/27/18.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomAd extends DialogFragment {

    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    protected boolean firstTime=true;
    private SharedPreferences preferences;

    //Put the URL of your ad here!
    private String URL= "https://freestock4you.github.io";

    //empty constructor required
    public CustomAd(){

    }

    //set custom window dimensions
    @Override
    public void onStart()
    {
        super.onStart();

        //hide dialog before webpage has loaded
        Dialog dialog = getDialog();
        dialog.hide();

        if (dialog != null)
        {
            int width = (int) (getResources().getDisplayMetrics().widthPixels*0.9f);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.9f);
            dialog.getWindow().setLayout(width, height);

        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        final Dialog dialog = getDialog();

        dialog.setCanceledOnTouchOutside(true);

        View view= inflater.inflate(R.layout.webview, container);
        //is this our very first time through? (e.g. there's
        //no history for when we first showed an ad?)

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firstTime = preferences.getBoolean("first_time_ad", firstTime);

        if(checkdate() || firstTime) {

            preferences.edit().putBoolean("first_time_ad", false).apply();

            WebView web = (WebView) view.findViewById(R.id.web);

            WebSettings settings = web.getSettings();
            settings.setJavaScriptEnabled(true);
            web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

            web.setWebViewClient(new WebViewClient() {
                //if you url is in a link it will open up in the webview, otherwise the link
                //will be opened in the default browser
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))
                            && !url.contains(URL)) {
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    } else {
                        view.loadUrl(url);
                        return false;
                    }
                }
                public void onPageFinished(WebView view, String url) {

                    dialog.show();
                    //write date to shared preferences
                    writeToSharedPrefs();
                }

            });

            web.loadUrl(URL);

        }
        else{
            dialog.dismiss();
        }
        return view;
    }

    public boolean checkdate(){
        //get current date time with Date()
        Date today = new Date();
        Date pastDate = new Date();

        today=new Date(dateFormat.format(today));

        try {
            pastDate = dateFormat.parse(readSharedPrefs());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //has it been a day since the ad was last shown?
        return  (today.compareTo(pastDate) > 0) ? true:false ;
    }

    public void writeToSharedPrefs(){
        //get current date time with Date()
        Date date = new Date();
        SharedPreferences pref = getActivity().getSharedPreferences("Ad", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        System.out.println("writing "+dateFormat.format(date));
        editor.putString("AdDate", dateFormat.format(date)).commit();

    }

    public String readSharedPrefs(){

        SharedPreferences pref = getActivity().getSharedPreferences("Ad", Context.MODE_PRIVATE);
        String date = pref.getString("AdDate", "");
        System.out.println("shared prefs has given us"+ date);

        return date;
    }



}

