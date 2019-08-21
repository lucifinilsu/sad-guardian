package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sad.assistant.live.guardian.api.GuardianSDK;

public class Service5 extends Service {

    IServiceDelegate serviceDelegate;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceDelegate.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceDelegate= GuardianSDK.getInstance().guardian().service(5);
        serviceDelegate.onCreate(this);
    }
}
