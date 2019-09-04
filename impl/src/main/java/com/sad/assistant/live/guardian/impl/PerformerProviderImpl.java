package com.sad.assistant.live.guardian.impl;

import android.support.annotation.NonNull;

import com.sad.assistant.live.guardian.api.IPerformerProvider;
import com.sad.assistant.live.guardian.api.InstanceProvider;
import com.sad.assistant.live.guardian.api.parameters.GuardiaTaskState;
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
    public IGuardiaFuture.Creator guardiaFutureCreator(int taskId) {
        IGuardiaFuture.Creator creator= this.instanceProvider.obtain("PERFORMER_GUARDIAFUTURE",true,false);
        creator.taskId(taskId);
        return creator;
    }

    @Override
    public IGuardiaFuture.Creator guardiaFutureCreatorDone(int taskId) {
        return guardiaFutureCreator(taskId).state(GuardiaTaskState.DONE);
    }

    @Override
    public IGuardiaFuture.Creator guardiaFutureCreatorWorking(int taskId) {
        return guardiaFutureCreator(taskId).state(GuardiaTaskState.WORKING);
    }

    @Override
    public IGuardiaFuture.Creator guardiaFutureCreatorUnworked(int taskId) {
        return guardiaFutureCreator(taskId).state(GuardiaTaskState.UNWORKED);
    }

    @Override
    public IGuardiaFuture.Creator guardiaFutureCreatorException(int taskId) {
        return guardiaFutureCreator(taskId).state(GuardiaTaskState.EXCEPTOIN);
    }
}
