package com.sad.assistant.live.guardian.api;

import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.optimize.IAppBootOptimizer;
import com.sad.assistant.live.guardian.api.optimize.IBatteryOptimizer;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;
import com.sad.assistant.live.guardian.api.optimize.IWifiSleepOptimizer;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;

public interface IGuardian {

    IDelegateStudio delegateStudio();

    IBatteryOptimizer batteryOptimizer();

    IAppBootOptimizer AppbootOptimizer();

    IWifiSleepOptimizer wifiSleepOptimizer();

    IAppWork appWork();

    IServiceDelegate service(int id);

    IOptimizer accountSyncOptimizer();

}
