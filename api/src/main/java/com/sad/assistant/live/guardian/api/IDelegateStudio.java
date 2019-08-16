package com.sad.assistant.live.guardian.api;

public interface IDelegateStudio {

    <D> D get(String s);

    IDelegateStudio put(String s,Class o);


}
