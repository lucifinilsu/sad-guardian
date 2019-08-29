package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sad.assistant.live.guardian.api.GuardianSDK;

public class GuardiaService4 extends Service {

    IServiceDelegate serviceDelegate;

    public IServiceDelegate getServiceDelegate() {
        if (serviceDelegate==null){
            serviceDelegate=GuardianSDK.getInstance().guardian().service(4,true,false);
        }
        return serviceDelegate;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getServiceDelegate().onBind(this,intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getServiceDelegate().onCreate(this);
    }
}
