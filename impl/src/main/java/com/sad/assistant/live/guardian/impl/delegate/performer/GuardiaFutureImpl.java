package com.sad.assistant.live.guardian.impl.delegate.performer;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.parameters.GuardiaTaskState;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaFuture;
import com.sad.assistant.live.guardian.api.parameters.INotificationStyle;
@SuppressWarnings("unchecked")
@GuardiaDelegate(name = "PERFORMER_GUARDIAFUTURE")
public class GuardiaFutureImpl implements IGuardiaFuture,IGuardiaFuture.Creator {

    private INotificationStyle notificationStyle;
    private Object data;
    private int taskId=-1;
    private GuardiaTaskState state;

    protected GuardiaFutureImpl(){}

    protected static IGuardiaFuture.Creator newCreator(){
        return new GuardiaFutureImpl();
    }

    @Override
    public INotificationStyle notificationStyle() {
        return this.notificationStyle;
    }

    @Override
    public int taskId() {
        return this.taskId;
    }

    @Override
    public GuardiaTaskState state() {
        return this.state;
    }

    @Override
    public <D> D get() {
        return (D) data;
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
    public Creator set(Object o) {
        this.data=o;
        return this;
    }

    @Override
    public Creator taskId(int id) {
        this.taskId=id;
        return this;
    }

    @Override
    public Creator state(GuardiaTaskState state) {
        this.state=state;
        return this;
    }

    @Override
    public IGuardiaFuture create() {
        return this;
    }
}
