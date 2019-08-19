package com.sad.assistant.live.guardian.api.optimize;

import android.content.Context;

public interface IAppBootOptimizer extends IOptimizer{

    @Override
    default boolean isOptimized(Context context){
        return false;
    }
}
