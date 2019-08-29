package com.sad.assistant.live.guardian.api.parameters;

public interface IGuardiaFuture {

    INotificationStyle notificationStyle();

    Creator creator();

    interface Creator{

        Creator notificationStyle(INotificationStyle notificationStyle);

        IGuardiaFuture create();

    }

}
