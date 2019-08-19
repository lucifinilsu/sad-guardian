package com.sad.assistant.live.guardian.api;

public interface IDelegateStudio {

    <D> D getDelegateInstance(String s);

    IDelegateStudio put(String s,Class o);


}
