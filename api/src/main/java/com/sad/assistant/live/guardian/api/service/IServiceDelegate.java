package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sad.assistant.live.guardian.api.IDelegate;

public interface IServiceDelegate extends IDelegate {

    default IBinder onBind(Service service,Intent intent){return null;}

    void onCreate(Service service);

    default int onStartCommand(Service service,Intent intent, int flags, int startId){
        return Service.START_REDELIVER_INTENT;
    };

    default void onDestroy(Service service){}
}
