package com.sad.assistant.live.guardian.impl;

import com.sad.assistant.live.guardian.api.IDelegateStudio;
import com.sad.assistant.live.guardian.api.IGuardian;
import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.optimize.IAppBootOptimizer;
import com.sad.assistant.live.guardian.api.optimize.IBatteryOptimizer;
import com.sad.assistant.live.guardian.api.optimize.IWifiSleepOptimizer;

public class DefaultGuardian implements IGuardian {
    public static IGuardian newInstance(){
        return new DefaultGuardian();
    }
    protected DefaultGuardian(){}
    IDelegateStudio delegateStudio=null;
    @Override
    public IDelegateStudio delegateStudio() {
        if (delegateStudio==null){
            delegateStudio=DelegateStudioImpl.newInstance();
        }
        return delegateStudio;
    }

    @Override
    public IBatteryOptimizer batteryOptimizer() {
        return delegateStudio().getDelegateInstance("OPTIMIZE_BATTERY");
    }

    @Override
    public IAppBootOptimizer AppbootOptimizer() {
        return delegateStudio().getDelegateInstance("OPTIMIZE_APPBOOT");
    }

    @Override
    public IWifiSleepOptimizer wifiSleepOptimizer() {
        return delegateStudio().getDelegateInstance("OPTIMIZE_WIFISLEEP");
    }

    @Override
    public IAppWork appWork() {
        return delegateStudio().getDelegateInstance("INIT_APPWORK");
    }
}
