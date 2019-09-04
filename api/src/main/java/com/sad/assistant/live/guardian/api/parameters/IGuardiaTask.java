package com.sad.assistant.live.guardian.api.parameters;

import android.content.Context;

public interface IGuardiaTask {

    <F extends IGuardiaFuture> F onWork(Context context, ActionSource source,ICommunicant communicant);

    void onStop(Context context);

}
