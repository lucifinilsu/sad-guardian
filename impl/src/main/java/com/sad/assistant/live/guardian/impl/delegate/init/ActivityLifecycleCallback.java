package com.sad.assistant.live.guardian.impl.delegate.init;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.sad.assistant.live.guardian.api.init.IBackgroundSwitchListener;
import com.sad.assistant.live.guardian.impl.utils.PushTokenManager;

import java.util.Set;

public class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private IBackgroundSwitchListener backgroundSwitchListener;
    private int activityStateFlagCount = 0;
    protected boolean isBackground= true;

    public void setBackgroundSwitchListener(IBackgroundSwitchListener backgroundSwitchListener) {
        this.backgroundSwitchListener = backgroundSwitchListener;
    }

    protected ActivityLifecycleCallback(){}

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {


        String action=activity.getIntent().getAction();
        Set<String> c=activity.getIntent().getCategories();
        boolean isLauncher= Intent.ACTION_MAIN.equals(action) && c!=null &&c.contains(Intent.CATEGORY_LAUNCHER);
        if (isLauncher){
            //connectKeepLiveSysService(activity);
        }

        if (activityStateFlagCount == 0){
            if (backgroundSwitchListener!=null){
                isBackground = false;
                backgroundSwitchListener.onChanged(activity,false);
            }
        }
        activityStateFlagCount++;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        activityStateFlagCount--;
        if (activityStateFlagCount == 0) {
            if (backgroundSwitchListener!=null){
                isBackground = true;
                backgroundSwitchListener.onChanged(activity,true);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    private static void connectKeepLiveSysService(Activity activity){
        HMSAgent.connect(activity, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                Log.e("GUARDIAN","------------------------->sys service connected !! rst="+rst);
                PushTokenManager.updateToken(new GetTokenHandler() {
                    @Override
                    public void onResult(int rtnCode) {
                        Log.e("GUARDIAN","------------------------->getToken is finished !! code="+rtnCode);
                    }
                });
            }
        });
    }
}
