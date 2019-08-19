package com.sad.assistant.live.guardian.api.optimize;

import android.content.Context;

public interface IWifiSleepOptimizer {

    int getWifiSleepPolicy(Context context);

    void onOptimize(Context context);

}
