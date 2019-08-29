package com.sad.assistant.live.guardian.api;

import com.sad.assistant.live.guardian.api.parameters.ICommunicant;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaFuture;

public interface IPerformerProvider {

    ICommunicant communicant();

    IGuardiaFuture.Creator guardiaFutureCreator();

}
