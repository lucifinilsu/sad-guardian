package com.sad.assistant.live.guardian.impl.delegate.performer;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.parameters.ICommunicant;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaFuture;

@GuardiaDelegate(name = "PERFORMER_COMMUNICANT")
public class CommunicantImpl implements ICommunicant {


    protected CommunicantImpl(){

    }

    @Override
    public void postFuture(IGuardiaFuture future) {

    }
}
