package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sad.assistant.live.guardian.api.GuardianSDK;

public class GuardiaService2 extends Service{
    IServiceDelegate delegate;
    private IServiceDelegate getDelegate(){
        if (delegate==null){
            delegate= GuardianSDK.getInstance().guardian().service(2,true,false);
        }
        return delegate;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getDelegate().onBind(this,intent);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        getDelegate().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return getDelegate().onStartCommand(this,intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy(this);
    }
}
