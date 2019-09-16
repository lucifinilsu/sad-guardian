package com.sad.assistant.live.guardian.api.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;
import com.sad.assistant.live.guardian.api.GuardianSDK;

public class GuardiaBroadcastReceiver2 extends PushReceiver {

    IHWPushReceiverDelegate delegate;

    public IHWPushReceiverDelegate getDelegate() {
        if (delegate==null){
            delegate= GuardianSDK.getInstance()
                    .guardian()
                    .delegateStudio()
                    .androidComponentDelegateProvider()
                    .broadcastReceiver(2);
        }
        return delegate;
    }

    @Override
    public void onToken(Context context, String token) {
        super.onToken(context, token);
        getDelegate().onToken(this,context,token);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msgBytes, Bundle extras) {
        super.onPushMsg(context, msgBytes, extras);
        return getDelegate().onPushMsg(this,context,msgBytes,extras);
    }

}
