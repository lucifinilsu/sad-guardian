package com.sad.assistant.live.guardian.api.parameters;

public interface IGuardiaFuture {

    INotificationStyle notificationStyle();

    int taskId();

    GuardiaTaskState state();

    <D> D get();

    Creator creator();

    interface Creator{

        Creator notificationStyle(INotificationStyle notificationStyle);

        Creator set(Object o);

        Creator taskId(int id);

        Creator state(GuardiaTaskState state);

        IGuardiaFuture create();

    }

}
