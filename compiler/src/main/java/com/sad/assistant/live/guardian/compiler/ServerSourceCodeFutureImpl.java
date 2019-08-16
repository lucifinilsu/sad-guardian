package com.sad.assistant.live.guardian.compiler;

public class ServerSourceCodeFutureImpl implements IServerSourceCodeFuture, IServerSourceCodeFuture.Creator {

    private IServerSourceCode sourceCode=null;
    private boolean isSuccess=false;
    private Throwable throwable=null;
    private int statusCode=0;
    private String statusMsg="";

    protected ServerSourceCodeFutureImpl(){}

    protected static IServerSourceCodeFuture newInstance(){
        return new ServerSourceCodeFutureImpl();
    }

    @Override
    public boolean isSuccessful() {
        return this.isSuccess;
    }

    @Override
    public <S extends IServerSourceCode> S get() {
        return (S) this.sourceCode;
    }

    @Override
    public <T extends Throwable> T throwable() {
        return (T) this.throwable;
    }

    @Override
    public String statusMsg() {
        return this.statusMsg;
    }

    @Override
    public int statusCode() {
        return this.statusCode;
    }

    @Override
    public Creator creator() {
        return this;
    }
    @Override
    public IServerSourceCodeFuture create() {
        return this;
    }

    @Override
    public Creator set(IServerSourceCode s) {
        this.sourceCode=s;
        return this;
    }

    @Override
    public Creator throwable(Throwable t) {
        this.throwable=t;
        return this;
    }

    @Override
    public Creator successful(boolean isSuccess) {
        this.isSuccess=isSuccess;
        return this;
    }

    @Override
    public Creator statusCode(int code) {
        this.statusCode=code;
        return this;
    }

    @Override
    public Creator statusMsg(String msg) {
        this.statusMsg=msg;
        return this;
    }
}
