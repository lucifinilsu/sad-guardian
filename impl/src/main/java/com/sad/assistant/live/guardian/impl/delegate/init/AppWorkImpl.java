package com.sad.assistant.live.guardian.impl.delegate.init;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.huawei.android.hms.agent.HMSAgent;
import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.assistant.live.guardian.api.broadcastreceiver.GuardiaBroadcastReceiver1;
import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.init.IBackgroundSwitchListener;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;

@GuardiaDelegate(name = "INIT_APPWORK")
public class AppWorkImpl implements IAppWork {
    private ActivityLifecycleCallback callback;
    protected AppWorkImpl(){}

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks(IBackgroundSwitchListener switchListener) {
        callback=new ActivityLifecycleCallback();
        callback.setBackgroundSwitchListener(switchListener);
        return callback;
    }

    @Override
    public boolean isApplicationBackground() {
        return callback.isBackground;
    }

    @Override
    public void initApplication(Application application) {
        HMSAgent.init(application);
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks(new IBackgroundSwitchListener() {
            @Override
            public void onChanged(Activity activity, boolean isBackground) {
                Intent intent=new Intent(AppConstant.BR_ACTION_APP_BACKGROUND_STATECHANGED);
                intent.putExtra(AppConstant.INTENT_KEY_ACTIVITY_CLASSNAME,activity.getClass().getCanonicalName());
                intent.putExtra(AppConstant.INTENT_KEY_APP_ISBACKGROUND,isBackground);
                application.sendBroadcast(intent);
            }
        }));
        optimizeAccount(application);
        registerScreenStateBR(application);
    }

    private void registerScreenStateBR(Application application){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        application.registerReceiver(new GuardiaBroadcastReceiver1(),intentFilter);

    }

    private void optimizeAccount(Application application){
        IOptimizer accountSyncOptimizer= GuardianSDK.getInstance()
                .guardian()
                .delegateStudio()
                .optimizerProvider()
                .accountSyncOptimizer();
        accountSyncOptimizer.optimize(application);
    }
}
