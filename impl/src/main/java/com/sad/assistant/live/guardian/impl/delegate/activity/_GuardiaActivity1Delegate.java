package com.sad.assistant.live.guardian.impl.delegate.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.activity.IActivityDelegate;

@GuardiaDelegate(name = "ACTIVITY_1")
public class _GuardiaActivity1Delegate implements IActivityDelegate {
    //注册广播接受者   当屏幕开启结果成功结束一像素的activity
    BroadcastReceiver br;
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        //设定一像素的activity
        Window window = activity.getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 2;
        params.width = 100;
        window.setAttributes(params);
        //在一像素activity里注册广播接受者    接受到广播结束掉一像素
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("GUARDIAN","------------------------------>One pixel Activity：Hide");
                activity.moveTaskToBack(false);
            }
        };
        activity.registerReceiver(br, new IntentFilter(AppConstant.BR_ACTION_ACTIVITY1_HIBERNATE));
        checkScreenOn(activity,"onCreate");
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        checkScreenOn(activity,"onResume");
    }

    private void checkScreenOn(Activity activity,String methodName) {
        Log.e("GUARDIAN","------------------------------>One pixel Activity:"+methodName);
        PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            //finish();
            activity.moveTaskToBack(false);
            Log.e("GUARDIAN","------------------------------>One pixel Activity：Hide");
        }
    }

    @Override
    public void onActivityPreDestroyed(@NonNull Activity activity) {
        try {
            activity.unregisterReceiver(br);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
