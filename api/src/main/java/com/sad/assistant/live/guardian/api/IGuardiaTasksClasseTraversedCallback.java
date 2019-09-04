package com.sad.assistant.live.guardian.api;

import com.sad.assistant.live.guardian.api.parameters.IGuardiaTask;

public interface IGuardiaTasksClasseTraversedCallback {

    <D> D OnTraversed(int tag, Class<? extends IGuardiaTask> cls, InstanceProvider<Integer,IGuardiaTask> instanceProvider);

}
