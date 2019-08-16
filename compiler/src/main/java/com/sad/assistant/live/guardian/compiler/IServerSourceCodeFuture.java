package com.sad.assistant.live.guardian.compiler;

import javax.annotation.processing.Filer;

public interface IServerSourceCodeFuture {

    boolean isSuccessful();

    <S extends IServerSourceCode> S get();

    <T extends Throwable> T throwable();

    String statusMsg();

    int statusCode();

    Creator creator();

    default void writeIn(Filer filer, String packageName, String className){
        if (isSuccessful() && get()!=null){
            get().writeIn(filer,packageName,className);
        }
    }

    interface Creator{

        IServerSourceCodeFuture create();

        Creator set(IServerSourceCode s);

        Creator throwable(Throwable t);

        Creator successful(boolean isSuccess);

        Creator statusCode(int code);

        Creator statusMsg(String msg);

    }

}
