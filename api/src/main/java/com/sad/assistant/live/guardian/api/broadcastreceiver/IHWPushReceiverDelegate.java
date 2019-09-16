package com.sad.assistant.live.guardian.api.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;

public interface IHWPushReceiverDelegate extends IBroadcastReceiverDelegate{

    void onToken(PushReceiver broadcastReceiver, Context context, String token);

    boolean onPushMsg(PushReceiver broadcastReceiver, Context context, byte[] msgBytes, Bundle extras);

    @Override
    default void onReceive(BroadcastReceiver broadcastReceiver, Context context, Intent intent){}
}
