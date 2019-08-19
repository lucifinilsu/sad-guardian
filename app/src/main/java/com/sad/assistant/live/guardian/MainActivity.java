package com.sad.assistant.live.guardian;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.assistant.live.guardian.api.optimize.IBatteryOptimizer;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PKGPlaceHolder pkgPlaceHolder=new PKGPlaceHolder();
        IBatteryOptimizer batteryOptimizer= GuardianSDK.getDefault().delegateStudio().getDelegateInstance("OPTIMIZE_BATTERY");
        batteryOptimizer.onOptimize(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("GUARDIAN","-------------------------------->从其他Activity返回");
    }
}
