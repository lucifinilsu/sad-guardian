package com.sad.assistant.live.guardian.impl.delegate.service;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.parameters.GuardiaTaskParameters;
import com.sad.assistant.live.guardian.api.parameters.INotificationStyle;
import com.sad.assistant.live.guardian.api.service.GuardiaService0;
import com.sad.assistant.live.guardian.api.service.GuardiaService1;
import com.sad.assistant.live.guardian.api.service.GuardiaService2;
import com.sad.assistant.live.guardian.api.service.IJobServiceDelegate;
import com.sad.assistant.live.guardian.api.service.IService2AidlInterface;
import com.sad.assistant.live.guardian.impl.utils.NotificationUtils;
import com.sad.assistant.live.guardian.impl.utils.ProcessUtils;

import static android.app.Service.START_REDELIVER_INTENT;

@GuardiaDelegate(name = "SERVICE_0")
public class _GuardiaService0Delegate implements IJobServiceDelegate {

    private JobScheduler mJobScheduler;
    private Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JobScheduler getJobScheduler(Context context) {
        if (mJobScheduler==null){
            mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        return mJobScheduler;
    }

    @Override
    public boolean onStartJob(Service service,JobParameters jobParameters) {
        restartService(service,bundle);
        return false;
    }

    @Override
    public boolean onStopJob(Service service,JobParameters jobParameters) {
        restartService(service,bundle);
        return false;
    }


    @Override
    public void onCreate(Service service) {

    }

    @Override
    public int onStartCommand(Service service,Intent intent, int flags, int startId) {
        this.bundle=intent.getExtras();
        INotificationStyle style=null;
        if (bundle!=null){
            GuardiaTaskParameters parameters=bundle.getParcelable(AppConstant.INTENT_KEY_SERVICEAIDLPARAMETERS);
            if (parameters!=null){
                style=parameters.getNotificationStyle();
            }
        }
        updateNotification(service,style);
        startService(service.getApplicationContext(),bundle);
        startId++;
        scheduleJobService(service.getApplicationContext(),startId,bundle);
        return START_REDELIVER_INTENT;
    }

    private void updateNotification(Service context,INotificationStyle style){
        NotificationUtils.updateNotification(context,style);
    }

    @Override
    public void onDestroy(Service service) {

    }

    private void startService(Context context, Bundle bundle){
        Intent localIntent = new Intent(context, GuardiaService1.class);
        if (bundle!=null){
            localIntent.putExtras(bundle);
        }
        Intent guardIntent = new Intent(context, GuardiaService2.class);
        if (bundle!=null){
            guardIntent.putExtras(bundle);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(localIntent);
            //context.startForegroundService(guardIntent);
        }
        else {
            context.startService(localIntent);
            //context.startService(guardIntent);
        }
    }

    private void scheduleJobService(Context context,int jobInfoId,Bundle bundle){
        JobInfo jobInfo=getJobInfo(context,jobInfoId,bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && jobInfo!=null) {
            getJobScheduler(context).schedule(jobInfo);
        }
    }

    private JobInfo getJobInfo(Context context,int id,Bundle bundle){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(id,
                    new ComponentName(context.getPackageName(), GuardiaService0.class.getName()));
            if (Build.VERSION.SDK_INT >= 24) {
                builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
                builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
                builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
            } else {
                builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
            }
            builder.setPersisted(true);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setRequiresCharging(false); // 当插入充电器，执行该任务
            return builder.build();
        }
        return null;
    }


    private void restartService(Service service,Bundle bundle){
        if (!ProcessUtils.isServiceRunning(
            service.getApplicationContext(), service.getClass().getCanonicalName())
            ||
            !ProcessUtils.isRunningTaskExist(service.getApplicationContext(), service.getPackageName()+":guardiaRemote")) {

            startService(service,bundle);

        }

    }
}
