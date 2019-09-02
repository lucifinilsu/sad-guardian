package com.sad.assistant.live.guardian.api.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sad.assistant.live.guardian.api.IDelegate;

public interface IBroadcastReceiverDelegate extends IDelegate {

    void onReceive(BroadcastReceiver broadcastReceiver, Context context, Intent intent);

}
