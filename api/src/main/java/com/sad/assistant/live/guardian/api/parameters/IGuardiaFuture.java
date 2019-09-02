package com.sad.assistant.live.guardian.api.parameters;

public interface IGuardiaFuture {

    INotificationStyle notificationStyle();

    <D> D get();

    Creator creator();

    interface Creator{

        Creator notificationStyle(INotificationStyle notificationStyle);

        Creator set(Object o);

        IGuardiaFuture create();

    }

}
