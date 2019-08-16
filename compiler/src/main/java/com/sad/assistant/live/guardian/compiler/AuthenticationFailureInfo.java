package com.sad.assistant.live.guardian.compiler;

public class AuthenticationFailureInfo {

    public final static int AGENCIES_HAVE_EXPIRED=2001;
    public final static int INVALID_PACKAGE_NAME=2002;
    public final static int PARSE_ERR=2003;

    private String msg="";
    private int code=0;
    private Throwable throwable=null;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
