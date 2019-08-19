package com.sad.assistant.live.guardian.impl;

import com.sad.assistant.live.guardian.api.IGuardian;
import com.sad.assistant.live.guardian.api.IRepository;
import com.sad.assistant.live.guardian.impl.optimize.AppBootOptimizerImpl;
import com.sad.assistant.live.guardian.impl.optimize.BatteryOptimizerImpl;
import com.sad.assistant.live.guardian.impl.optimize.WifiSleepOptimizerImpl;

public class Repository implements IRepository {
    protected Repository(){}
    @Override
    public IGuardian registerIn() {
        IGuardian guardian= DefaultGuardian.newInstance();
        guardian.delegateStudio()
                .put("OPTIMIZE_BATTERY", BatteryOptimizerImpl.class)
                .put("OPTIMIZE_APPBOOT", AppBootOptimizerImpl.class)
                .put("OPTIMIZE_WIFISLEEP", WifiSleepOptimizerImpl.class)
                ;
        return guardian;
    }
}
