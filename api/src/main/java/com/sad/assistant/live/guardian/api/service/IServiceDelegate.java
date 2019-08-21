package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public interface IServiceDelegate {

    IBinder onBind(Intent intent);

    void onCreate(Service service);

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();
}
