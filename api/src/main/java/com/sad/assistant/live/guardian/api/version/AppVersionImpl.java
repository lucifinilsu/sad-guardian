package com.sad.assistant.live.guardian.api.version;

import android.support.annotation.NonNull;

public class AppVersionImpl implements IAppVersion<AppVersionImpl>,IAppVersion.Api{

    private long code=-1;
    private String des="";
    private String name="";

    public static IAppVersion newInstance(){
        return new AppVersionImpl();
    }

    protected AppVersionImpl(){

    }

    @Override
    public Api api() {
        return this;
    }

    @Override
    public AppVersionImpl code(long code) {
        this.code=code;
        return this;
    }

    @Override
    public AppVersionImpl des(@NonNull String des) {
        this.des=des;
        return this;
    }

    @Override
    public AppVersionImpl name(@NonNull String name) {
        this.name=name;
        return this;
    }

    @NonNull
    @Override
    public String getName() {
        return this.name;
    }

    @NonNull
    @Override
    public String getDes() {
        return this.des;
    }

    @Override
    public long getCode() {
        return this.code;
    }
}
