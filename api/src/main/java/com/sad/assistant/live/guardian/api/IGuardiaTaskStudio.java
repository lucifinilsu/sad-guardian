package com.sad.assistant.live.guardian.api;

import com.sad.assistant.live.guardian.api.parameters.IGuardiaTask;

import java.util.Set;

public interface IGuardiaTaskStudio extends InstanceProvider<Integer,IGuardiaTask>{

    IGuardiaTaskStudio put(int tag, Class<? extends IGuardiaTask> cls);

    <D extends IGuardiaTask> Class<D> get(int tag);

    Set<Integer> indexes();

    int size();

}
