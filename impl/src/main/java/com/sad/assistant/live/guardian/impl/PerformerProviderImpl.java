package com.sad.assistant.live.guardian.impl;

import android.support.annotation.NonNull;

import com.sad.assistant.live.guardian.api.IPerformerProvider;
import com.sad.assistant.live.guardian.api.InstanceProvider;
import com.sad.assistant.live.guardian.api.parameters.ICommunicant;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaFuture;

public class PerformerProviderImpl implements IPerformerProvider {

    private InstanceProvider<String,Object> instanceProvider;

    protected <P extends InstanceProvider<String,Object>> PerformerProviderImpl(@NonNull P instanceProvider){
        this.instanceProvider=instanceProvider;
    }

    protected static <P extends InstanceProvider<String,Object>> IPerformerProvider newInstance(@NonNull P instanceProvider){
        return new PerformerProviderImpl(instanceProvider);
    }
    
    @Override
    public ICommunicant communicant() {
        return this.instanceProvider.obtain("PERFORMER_COMMUNICANT",true,false);
    }

    @Override
    public IGuardiaFuture.Creator guardiaFutureCreator() {
        return this.instanceProvider.obtain("PERFORMER_GUARDIAFUTURE",true,false);
    }
}
