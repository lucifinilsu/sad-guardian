package com.sad.assistant.live.guardian.api;

import com.sad.assistant.live.guardian.api.activity.IActivityDelegate;
import com.sad.assistant.live.guardian.api.broadcastreceiver.IBroadcastReceiverDelegate;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;

public interface IAndroidComponentDelegateProvider {

    <S extends IServiceDelegate> S service(int id);

    <A extends IActivityDelegate> A activity(int id);

    <B extends IBroadcastReceiverDelegate> B broadcastReceiver(int id);
}
