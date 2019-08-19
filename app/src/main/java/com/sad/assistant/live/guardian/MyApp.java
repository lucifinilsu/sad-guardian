package com.sad.assistant.live.guardian;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.sad.assistant.live.guardian.annotation.AppLiveGuardian;
import com.sad.assistant.live.guardian.api.GuardianSDK;

@AppLiveGuardian
public class MyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        GuardianSDK.init(this);
    }
}
