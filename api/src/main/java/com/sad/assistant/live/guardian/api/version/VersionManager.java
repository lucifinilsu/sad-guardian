package com.sad.assistant.live.guardian.api.version;

import com.sad.assistant.datastore.sharedPreferences.SharedPerferencesClient;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.basic.utils.app.AppInfoUtil;

public class VersionManager {

    public static void saveVersion(IAppVersion appVersion){
        String s=appVersion.api().toJsonString();
        SharedPerferencesClient.with(GuardianSDK.getContext())
                .name(AppConstant.SHAREDPERFERENCES_CLIENT)
                .build()
                .put(AppConstant.SHAREDPERFERENCES_CLIENT_NAME_VERSION,s);
    }

    public static void saveCurrVersion(){
        IAppVersion version=AppVersionImpl.newInstance()
                .code(AppInfoUtil.getVersionCode(GuardianSDK.getContext()))
                .name(AppInfoUtil.getVersionName(GuardianSDK.getContext()));
        saveVersion(version);
    }

    public static IAppVersion.Api getLastVersion(){
        IAppVersion version=AppVersionImpl.newInstance();
        IAppVersion last= SharedPerferencesClient.with(GuardianSDK.getContext())
                .name(AppConstant.SHAREDPERFERENCES_CLIENT)
                .build()
                .get(AppConstant.SHAREDPERFERENCES_CLIENT_NAME_VERSION,AppVersionImpl.newInstance().code(-999));
        if (last==null || last.api().getCode()==-999){
            saveVersion(version);
            return version.api();
        }
        return last.api();
    }

    public static boolean versionIsDiff(){
        IAppVersion.Api last=getLastVersion();
        return last.getCode()!= AppInfoUtil.getVersionCode(GuardianSDK.getContext());
    }

}
