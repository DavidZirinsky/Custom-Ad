package com.example.david.customaddemo;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public void loadCustomAd() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        CustomAd ad = new CustomAd();
        ad.setStyle(ad.STYLE_NORMAL, R.style.CustomDialog);

        ad.show(fragmentManager, "Input Dialog");
        if (ad.getDialog() != null){
            ad.getDialog().setCanceledOnTouchOutside(true);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadCustomAd();
            }
        };
        new Thread(runnable).start();
    }
}
