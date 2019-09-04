package com.sad.assistant.live.guardian.api.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sad.assistant.live.guardian.api.GuardianSDK;

public class GuardiaBroadcastReceiver1 extends BroadcastReceiver{

    IBroadcastReceiverDelegate delegate;

    public IBroadcastReceiverDelegate getDelegate() {
        if (delegate==null){
            delegate= GuardianSDK.getInstance()
                    .guardian()
                    .delegateStudio()
                    .androidComponentDelegateProvider()
                    .broadcastReceiver(1);
        }
        return delegate;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getDelegate().onReceive(this,context,intent);
    }


}
