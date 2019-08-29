package com.sad.assistant.live.guardian.api;

import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;

public interface IOptimizerProvider {

    IOptimizer batteryOptimizer();

    IOptimizer appBootOptimizer();

    IOptimizer wifiSleepOptimizer();

    IOptimizer accountSyncOptimizer();

    IAppWork appWork(boolean isNewInstance, boolean update);

}
