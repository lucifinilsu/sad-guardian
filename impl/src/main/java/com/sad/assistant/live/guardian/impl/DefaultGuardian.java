package com.sad.assistant.live.guardian.impl;

import com.sad.assistant.live.guardian.api.IDelegateStudio;
import com.sad.assistant.live.guardian.api.IGuardiaTaskStudio;
import com.sad.assistant.live.guardian.api.IGuardian;
import com.sad.assistant.live.guardian.api.activity.IActivityDelegate;
import com.sad.assistant.live.guardian.api.broadcastreceiver.IBroadcastReceiverDelegate;
import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;

import java.util.HashMap;
import java.util.Map;

public class DefaultGuardian implements IGuardian {


    public static IGuardian newInstance(){
        return new DefaultGuardian();
    }
    protected DefaultGuardian(){}
    IDelegateStudio delegateStudio=null;
    IGuardiaTaskStudio guardiaWorkerStudio=null;

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

}
