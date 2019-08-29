package com.sad.assistant.live.guardian.api.init;

import android.app.Application;

public interface IAppWork {

    //Application.ActivityLifecycleCallbacks activityLifecycleCallbacks(IBackgroundSwitchListener switchListener);

    boolean isApplicationBackground();

    void initApplication(Application application);

}
