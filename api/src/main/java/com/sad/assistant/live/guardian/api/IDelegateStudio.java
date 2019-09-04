package com.sad.assistant.live.guardian.api;

import java.util.Set;

public interface IDelegateStudio extends InstanceProvider<String,Object>{

    <D> Class<D> get(String s);

    IDelegateStudio put(String s,Class o);

    int size();

    Set<String> indexes();

    IAndroidComponentDelegateProvider androidComponentDelegateProvider();

    IOptimizerProvider optimizerProvider();

    IPerformerProvider performerProvider();



}
