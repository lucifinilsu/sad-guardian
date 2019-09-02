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

    private IJobServiceDelegate getDelegate(){
        if (jobServiceDelegate==null){
            jobServiceDelegate= GuardianSDK.getInstance()
                                    .guardian()
                                    .delegateStudio()
                                    .androidComponentDelegateProvider()
                                    .service(0)
                                    ;
        }
        return jobServiceDelegate;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getDelegate().onCreate(this);
    }



    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return getDelegate().onStartJob(this,jobParameters);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return getDelegate().onStopJob(this,jobParameters);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return getDelegate().onStartCommand(this,intent,flags,startId);
    }
}
