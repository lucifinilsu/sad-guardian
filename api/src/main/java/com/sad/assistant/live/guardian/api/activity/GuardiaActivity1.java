package com.sad.assistant.live.guardian.api.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sad.assistant.live.guardian.api.GuardianSDK;

public class GuardiaActivity1 extends AppCompatActivity {
    IActivityDelegate delegate;


    public IActivityDelegate getActivityDelegate() {
        if (delegate==null){
            delegate= GuardianSDK.getInstance().guardian().activity(1,true,false);
        }
        return delegate;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityDelegate().onActivityCreated(this,savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        getActivityDelegate().onActivityPreDestroyed(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getActivityDelegate().onActivityResumed(this);
    }
}
