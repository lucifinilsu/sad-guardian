package com.sad.assistant.live.guardian.impl.delegate.broadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.assistant.live.guardian.api.activity.GuardiaActivity1;
import com.sad.assistant.live.guardian.api.broadcastreceiver.IBroadcastReceiverDelegate;
import com.sad.assistant.live.guardian.api.init.IAppWork;

@SuppressWarnings(value = {"unchecked", "deprecation"})
@GuardiaDelegate(name = "BROADCASTRECEIVER_1")
public final class _GuardiaBroadcastReceiver1Delegate implements IBroadcastReceiverDelegate {


    public _GuardiaBroadcastReceiver1Delegate() {

    }

    @Override
    public void onReceive(BroadcastReceiver broadcastReceiver,final Context context, Intent intent) {
        Log.e("keeplive","------------------------->屏幕变化监听"+intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            //屏幕关闭的时候接受到广播
            boolean appIsForeground = IsForeground(context);
            if (!appIsForeground){
                Class<? extends Activity> activityClass= GuardiaActivity1.class;
                startActivity(context,activityClass);
            }
            //通知屏幕已关闭
            context.sendBroadcast(new Intent(AppConstant.BR_ACTION_SCREEN_OFF));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            //屏幕打开的时候发送广播  结束一像素
            context.sendBroadcast(new Intent(AppConstant.BR_ACTION_ACTIVITY1_HIBERNATE));
            //通知屏幕已点亮
            context.sendBroadcast(new Intent(AppConstant.BR_ACTION_SCREEN_OFF));
        }
    }


    private boolean IsForeground(Context context) {
        IAppWork appWork=GuardianSDK.getInstance()
                .guardian()
                .delegateStudio()
                .optimizerProvider()
                .appWork(false,true);
        if (appWork!=null){
            return appWork.isApplicationBackground();
        }
        return false;
    }

    private void startActivity(Context context, Class<? extends Activity> cls){
        try {
            Intent it = new Intent(context,cls);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
