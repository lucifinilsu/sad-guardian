package com.sad.assistant.live.guardian.compiler;

public class ServerSourceCodeImpl implements IServerSourceCode {
    private String code="";

    protected ServerSourceCodeImpl(String code){
        setCode(code);
    }
    @Override
    public String get() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
