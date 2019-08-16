package com.sad.assistant.live.guardian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sad.assistant.live.guardian.api.GuardianSDK;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //GuardianSDK.getDefault()
    }
}
