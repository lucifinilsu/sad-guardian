package com.sad.assistant.live.guardian.api;

import com.sad.assistant.live.guardian.api.optimize.IAppBootOptimizer;
import com.sad.assistant.live.guardian.api.optimize.IBatteryOptimizer;
import com.sad.assistant.live.guardian.api.optimize.IWifiSleepOptimizer;

public interface IGuardian {

    IDelegateStudio delegateStudio();

    IBatteryOptimizer batteryOptimizer();

    IAppBootOptimizer AppbootOptimizer();

    IWifiSleepOptimizer wifiSleepOptimizer();

}
