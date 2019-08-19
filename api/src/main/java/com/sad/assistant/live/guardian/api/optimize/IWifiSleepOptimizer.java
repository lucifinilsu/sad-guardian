package com.sad.assistant.live.guardian.api.optimize;

import android.content.Context;

public interface IWifiSleepOptimizer extends IOptimizer{

    int getWifiSleepPolicy(Context context);

    @Override
    default boolean isOptimized(Context context){
        int p = getWifiSleepPolicy(context);
        return p==2;
    }
}
