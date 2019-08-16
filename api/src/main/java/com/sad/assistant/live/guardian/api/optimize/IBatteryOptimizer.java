package com.sad.assistant.live.guardian.api.optimize;

import android.content.Context;

public interface IBatteryOptimizer {

    void onOptimize(Context context);

    boolean isOptimized(Context context);

}
