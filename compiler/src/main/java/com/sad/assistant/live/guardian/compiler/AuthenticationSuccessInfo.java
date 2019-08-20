package com.sad.assistant.live.guardian.compiler;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationSuccessInfo {

    private String appId="";
    private long deadline=0;
    private String appPackage="";
    private List<SourceCodeInfo> data=new ArrayList<SourceCodeInfo>();

    public List<SourceCodeInfo> getData() {
        return data;
    }

    public void setData(List<SourceCodeInfo> data) {
        this.data = data;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

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
