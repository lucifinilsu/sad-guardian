package com.sad.assistant.live.guardian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.sad.assistant.live.guardian.impl.PKGPlaceHolder;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PKGPlaceHolder pkgPlaceHolder=new PKGPlaceHolder();
        /*IBatteryOptimizer batteryOptimizer=GuardianSDK.getDefault().delegateStudio().get("OPTIMIZE_BATTERY");
        batteryOptimizer.onOptimize(getApplicationContext());*/
    }
}
