package com.sad.assistant.live.guardian;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.sad.assistant.live.guardian.annotation.GuardiaTask;
import com.sad.assistant.live.guardian.api.parameters.ActionSource;
import com.sad.assistant.live.guardian.api.parameters.ICommunicant;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaFuture;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@GuardiaTask(tag = 62)
public class TestGuardiaTask implements IGuardiaTask {
    private ScheduledExecutorService workExecutor;
    private long c=0;
    @Override
    public <F extends IGuardiaFuture> F onWork(Context context, ActionSource source, ICommunicant communicant) {
        if (workExecutor==null){
            workExecutor= Executors.newScheduledThreadPool(1);
            workExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    c++;
                    Log.e("GUARDIAN","------------------------->任务执行第："+c+"次");
                }
            },1,30, TimeUnit.SECONDS);
        }
        //Log.e("GUARDIAN","------------------------->任务执行第："+c+"次");
        return null;
    }

    @Override
    public void onStop(Context context) {

    }
}
