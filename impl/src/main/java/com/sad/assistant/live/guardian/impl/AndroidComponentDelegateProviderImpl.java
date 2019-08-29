package com.sad.assistant.live.guardian.impl;

import android.support.annotation.NonNull;

import com.sad.assistant.live.guardian.api.IAndroidComponentDelegateProvider;
import com.sad.assistant.live.guardian.api.InstanceProvider;
import com.sad.assistant.live.guardian.api.activity.IActivityDelegate;
import com.sad.assistant.live.guardian.api.broadcastreceiver.IBroadcastReceiverDelegate;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;

public class AndroidComponentDelegateProviderImpl implements IAndroidComponentDelegateProvider {
    private InstanceProvider<String> instanceProvider;
    
    protected <P extends InstanceProvider<String>> AndroidComponentDelegateProviderImpl(@NonNull P instanceProvider){
        this.instanceProvider=instanceProvider;
    }
    
    protected static <P extends InstanceProvider<String>> IAndroidComponentDelegateProvider newInstance(@NonNull P instanceProvider){
        return new AndroidComponentDelegateProviderImpl(instanceProvider);
    }

    @Override
    public <S extends IServiceDelegate> S service(int id) {
        return instanceProvider.obtain("SERVICE_"+id,true,false);
    }

    @Override
    public <A extends IActivityDelegate> A activity(int id) {
        return instanceProvider.obtain("ACTIVITY_"+id,true,false);
    }

    @Override
    public <B extends IBroadcastReceiverDelegate> B broadcastReceiver(int id) {
        return instanceProvider.obtain("BROADCASTRECEIVER_"+id,true,false);
    }
}
