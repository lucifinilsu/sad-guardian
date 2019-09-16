package com.sad.assistant.live.guardian.impl.delegate.broadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;
import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.assistant.live.guardian.api.activity.GuardiaActivity1;
import com.sad.assistant.live.guardian.api.broadcastreceiver.IBroadcastReceiverDelegate;
import com.sad.assistant.live.guardian.api.broadcastreceiver.IHWPushReceiverDelegate;
import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.parameters.ActionSource;
import com.sad.assistant.live.guardian.api.service.GuardiaService1;
import com.sad.assistant.live.guardian.impl.utils.ProcessUtils;
import com.sad.assistant.live.guardian.impl.utils.PushTokenManager;

@SuppressWarnings(value = {"unchecked", "deprecation"})
@GuardiaDelegate(name = "BROADCASTRECEIVER_2")
public class _GuardiaBroadcastReceiver2Delegate implements IHWPushReceiverDelegate {


    public _GuardiaBroadcastReceiver2Delegate() {

    }

    @Override
    public void onToken(PushReceiver broadcastReceiver, Context context, String token) {
        Log.e("GUARDIAN","------------------------->update push token ="+token);
        try {
            PushTokenManager.saveTokenToLocal(context,token);
            //PushTokenManager.uploadTokenToServer(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPushMsg(PushReceiver broadcastReceiver, Context context, byte[] msgBytes, Bundle extras) {
        Log.e("GUARDIAN","------------------------->sys received puhs msg !!");
        try {
            /*String locs= GuardiaService1.class.getCanonicalName();
            KeepAliveSDK.getDefault().build().start();
            Log.e("GUARDIAN","------------------------->透传唤醒,当前进程："+ ProcessUtils.isMainProcess(context));
            Intent intent=new Intent(IAction.ACTION);
            intent.putExtra("source", ActionSource.PUSH);
            intent.putExtra("msg",msgBytes);
            context.sendBroadcast(intent);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
