package com.sad.assistant.live.guardian.api;

import com.sad.assistant.live.guardian.api.parameters.ICommunicant;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaFuture;

public interface IPerformerProvider {

    ICommunicant communicant();

    IGuardiaFuture.Creator guardiaFutureCreator(int taskId);

    IGuardiaFuture.Creator guardiaFutureCreatorDone(int taskId);

    IGuardiaFuture.Creator guardiaFutureCreatorWorking(int taskId);

    IGuardiaFuture.Creator guardiaFutureCreatorUnworked(int taskId);

    IGuardiaFuture.Creator guardiaFutureCreatorException(int taskId);

}
