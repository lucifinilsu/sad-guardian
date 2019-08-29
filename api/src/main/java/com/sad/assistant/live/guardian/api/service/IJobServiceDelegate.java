package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.app.job.JobParameters;

public interface IJobServiceDelegate extends IServiceDelegate {

    boolean onStartJob(Service service,JobParameters jobParameters);

    boolean onStopJob(Service service,JobParameters jobParameters);

}
