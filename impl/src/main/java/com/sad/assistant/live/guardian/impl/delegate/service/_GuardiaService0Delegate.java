package com.sad.assistant.live.guardian.impl.delegate.service;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.os.IBinder;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.service.IJobServiceDelegate;
@GuardiaDelegate(name = "SERVICE_0")
public class _GuardiaService0Delegate implements IJobServiceDelegate {

    private JobScheduler mJobScheduler;

    @Override
    public boolean onStartJob(Service service,JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(Service service,JobParameters jobParameters) {
        return false;
    }

    @Override
    public IBinder onBind(Service service, Intent intent) {
        return null;
    }

    @Override
    public void onCreate(Service service) {

    }

    @Override
    public int onStartCommand(Service service,Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy(Service service) {

    }
}
