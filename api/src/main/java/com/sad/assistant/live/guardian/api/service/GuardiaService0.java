package com.sad.assistant.live.guardian.api.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.sad.assistant.live.guardian.api.GuardianSDK;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GuardiaService0 extends JobService {

    IJobServiceDelegate jobServiceDelegate;

    private void initDelegate(){
        if (jobServiceDelegate==null){
            jobServiceDelegate= GuardianSDK.getInstance().guardian().service(0,true,false);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDelegate();
        jobServiceDelegate.onCreate(this);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        initDelegate();
        return jobServiceDelegate.onStartJob(this,jobParameters);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        initDelegate();
        return jobServiceDelegate.onStopJob(this,jobParameters);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        initDelegate();
        return jobServiceDelegate.onStartCommand(this,intent,flags,startId);
    }
}
