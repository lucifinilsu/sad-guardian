package com.sad.assistant.live.guardian.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.sad.assistant.live.guardian.api.IDelegateStudio;
import com.sad.assistant.live.guardian.api.IGuardiaTaskStudio;
import com.sad.assistant.live.guardian.api.IGuardian;
import com.sad.assistant.live.guardian.api.activity.IActivityDelegate;
import com.sad.assistant.live.guardian.api.broadcastreceiver.IBroadcastReceiverDelegate;
import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;
import com.sad.assistant.live.guardian.api.service.GuardiaService0;
import com.sad.assistant.live.guardian.api.service.GuardiaService1;
import com.sad.assistant.live.guardian.api.service.GuardiaService2;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;
import com.sad.assistant.live.guardian.impl.utils.ProcessUtils;

import java.util.HashMap;
import java.util.Map;

public class DefaultGuardian implements IGuardian {


    public static IGuardian newInstance(Context context){
        return new DefaultGuardian(context);
    }
    protected DefaultGuardian(Context context){
        this.context=context;
    }
    IDelegateStudio delegateStudio=null;
    IGuardiaTaskStudio guardiaWorkerStudio=null;
    private Context context;

    @Override
    public IDelegateStudio delegateStudio() {
        if (delegateStudio==null){
            delegateStudio=DelegateStudioImpl.newInstance();
        }
        return delegateStudio;
    }

    @Override
    public IGuardiaTaskStudio guardiaStudio() {
        if (guardiaWorkerStudio==null){
            guardiaWorkerStudio= GuardiaTaskStudioImpl.newInstance();
        }
        return guardiaWorkerStudio;
    }

    @Override
    public void start(Bundle bundle) {
        if(ProcessUtils.isMainProcess(context)) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent(context, GuardiaService0.class);
                intent.putExtras(bundle);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent);
                }
                else {
                    context.startService(intent);
                }
                context.startService(intent);
            }
            else {
                Intent localIntent = new Intent(context, GuardiaService1.class);
                localIntent.putExtras(bundle);
                Intent guardIntent = new Intent(context, GuardiaService2.class);
                guardIntent.putExtras(bundle);
                context.startService(localIntent);
                context.startService(guardIntent);
            }
        }

    }
}
