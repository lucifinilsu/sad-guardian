package com.sad.assistant.live.guardian.api.optimize;

import android.content.Context;

public interface IOptimizer {

    void onOptimize(Context context);

    boolean isOptimized(Context context);

}
