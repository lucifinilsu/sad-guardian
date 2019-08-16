package com.sad.assistant.live.guardian.compiler;

public interface OnCompiledAuthenticationFailureCallback extends IHttpStatusErrorListener {

    void onParseResourceErr(AuthenticationFailureInfo info);

    void onInvalid(AuthenticationFailureInfo info);

}
