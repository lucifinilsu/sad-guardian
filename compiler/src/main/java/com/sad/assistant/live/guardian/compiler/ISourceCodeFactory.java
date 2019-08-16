package com.sad.assistant.live.guardian.compiler;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public interface ISourceCodeFactory {

    String baseUrl="https://raw.githubusercontent.com/lucifinilsu/sad-guardian/dev/impl/src/main/java/com/sad/assistant/live/guardian/impl/";

    ISourceCodeFactory path(String path);

    ISourceCodeFactory tag(int tag);

    IServerSourceCodeFuture get(OnGetCodeSuccessListener successListener,OnGetCodeFailureListener failureListener);

    interface OnGetCodeSuccessListener{

        void onGetCode(IServerSourceCodeFuture future);

    }

    interface OnGetCodeFailureListener extends IHttpStatusErrorListener {

        int IO_ERR=3001;

        int ERR=3002;

        void onIOErr(IOException e);

        void onErr(Error error);

    }

}
