package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.IBinder;

public interface IJobServiceDelegate extends IServiceDelegate {

    boolean onStartJob(Service service,JobParameters jobParameters);

    boolean onStopJob(Service service,JobParameters jobParameters);

    @Override
    default IBinder onBind(Service service, Intent intent) {
        return null;
    }
}
