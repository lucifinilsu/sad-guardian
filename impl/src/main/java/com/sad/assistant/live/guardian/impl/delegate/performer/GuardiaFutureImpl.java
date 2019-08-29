package com.sad.assistant.live.guardian.impl.delegate.performer;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaFuture;
import com.sad.assistant.live.guardian.api.parameters.INotificationStyle;
@GuardiaDelegate(name = "PERFORMER_GUARDIAFUTURE")
public class GuardiaFutureImpl implements IGuardiaFuture,IGuardiaFuture.Creator {

    private INotificationStyle notificationStyle;

    protected GuardiaFutureImpl(){}

    protected static IGuardiaFuture.Creator newCreator(){
        return new GuardiaFutureImpl();
    }

    @Override
    public INotificationStyle notificationStyle() {
        return this.notificationStyle;
    }

    @Override
    public Creator creator() {
        return this;
    }

    @Override
    public Creator notificationStyle(INotificationStyle notificationStyle) {
        this.notificationStyle=notificationStyle;
        return this;
    }

    @Override
    public IGuardiaFuture create() {
        return this;
    }
}
