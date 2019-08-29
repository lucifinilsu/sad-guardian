package com.sad.assistant.live.guardian.api.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public interface IBroadcastReceiverDelegate {

    void onReceive(BroadcastReceiver broadcastReceiver, Context context, Intent intent);

}
