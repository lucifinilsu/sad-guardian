package com.sad.assistant.live.guardian.compiler;

public class AuthenticationSuccessInfo {

    private String appId="";
    private long deadline=0;



    public long getDeadline() {
        return deadline;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

}
