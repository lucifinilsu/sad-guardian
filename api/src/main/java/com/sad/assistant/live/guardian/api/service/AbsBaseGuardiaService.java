package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sad.assistant.live.guardian.api.GuardianSDK;

public abstract class AbsBaseGuardiaService extends Service {

    abstract int serviceId();

    private IServiceDelegate delegate;
    protected IServiceDelegate getDelegate(){
        if (delegate==null){
            delegate= GuardianSDK.getInstance()
                                .guardian()
                                .delegateStudio()
                                .androidComponentDelegateProvider()
                                .service(serviceId());
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
    public void onDestroy() {
        getDelegate().onDestroy(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return getDelegate().onStartCommand(this,intent,flags,startId);
    }

}
