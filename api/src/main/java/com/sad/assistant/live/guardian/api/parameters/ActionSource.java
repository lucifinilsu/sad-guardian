package com.sad.assistant.live.guardian.api.parameters;

import java.io.Serializable;

public enum ActionSource implements Serializable {

    LOCAL_WAKE("-- --"),
    REMOTE_WAKE("- ---"),
    PUSH("--- -");
    public String getName(){
        return this.name;
    }
    private String name="";
    private ActionSource(String s){
        this.name=s;
    }

}
