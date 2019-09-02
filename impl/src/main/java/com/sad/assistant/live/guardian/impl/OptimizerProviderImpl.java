package com.sad.assistant.live.guardian.impl;

import com.sad.assistant.live.guardian.api.IOptimizerProvider;
import com.sad.assistant.live.guardian.api.InstanceProvider;
import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;

public class OptimizerProviderImpl implements IOptimizerProvider {

    private InstanceProvider<String,Object> instanceProvider;

    protected <P extends InstanceProvider<String,Object>> OptimizerProviderImpl(P instanceProvider){
        this.instanceProvider=instanceProvider;
    }
    public static <P extends InstanceProvider<String,Object>> IOptimizerProvider newInstance(P instanceProvider){
        return new OptimizerProviderImpl(instanceProvider);
    }

    @Override
    public IOptimizer batteryOptimizer() {
        return this.instanceProvider.obtain("OPTIMIZE_BATTERY",true,false);
    }

    @Override
    public IOptimizer appBootOptimizer() {
        return this.instanceProvider.obtain("OPTIMIZE_APPBOOT",true,false);
    }

    @Override
    public IOptimizer wifiSleepOptimizer() {
        return this.instanceProvider.obtain("OPTIMIZE_WIFISLEEP",true,false);
    }

    @Override
    public IOptimizer accountSyncOptimizer() {
        return this.instanceProvider.obtain("OPTIMIZE_ACCOUNTSYNC",true,false);
    }

    @Override
    public IAppWork appWork(boolean isNewInstance, boolean update) {
        return this.instanceProvider.obtain("INIT_APPWORK",isNewInstance,update);
    }
}
