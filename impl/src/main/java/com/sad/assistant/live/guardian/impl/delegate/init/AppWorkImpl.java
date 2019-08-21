package com.sad.assistant.live.guardian.impl.delegate.init;

import android.app.Application;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.init.IBackgroundSwitchListener;
@GuardiaDelegate(name = "INIT_APPWORK")
public class AppWorkImpl implements IAppWork {
    private ActivityLifecycleCallback callback;
    protected AppWorkImpl(){}
    @Override
    public Application.ActivityLifecycleCallbacks activityLifecycleCallbacks(IBackgroundSwitchListener switchListener) {
        callback=new ActivityLifecycleCallback();
        callback.setBackgroundSwitchListener(switchListener);
        return callback;
    }

    @Override
    public boolean isBackground() {
        return callback.isBackground;
    }
}
