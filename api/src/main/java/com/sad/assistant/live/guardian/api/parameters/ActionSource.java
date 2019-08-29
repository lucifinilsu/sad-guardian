package com.sad.assistant.live.guardian.api.parameters;

import java.io.Serializable;

public enum ActionSource implements Serializable {

    SERVICE_STARTUP("服务启动"), FORCE_WAKE("本地启动"), REMOTE_WAKENER_WAKEUP("进程守护"),PUSH("推送");
    public String getName(){
        return this.name;
    }
    private String name="";
    private ActionSource(String s){
        this.name=s;
    }

}
