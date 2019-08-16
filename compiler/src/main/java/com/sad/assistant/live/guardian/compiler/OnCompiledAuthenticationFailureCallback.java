package com.sad.assistant.live.guardian.compiler;

public interface OnCompiledAuthenticationFailureCallback extends IHttpIOListener{

    void onParseResourceErr(AuthenticationFailureInfo info);

    void onInvalid(AuthenticationFailureInfo info);

}
