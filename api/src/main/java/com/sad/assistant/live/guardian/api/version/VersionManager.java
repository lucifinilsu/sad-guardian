package com.sad.assistant.live.guardian.api.version;

import android.content.Context;

import com.sad.assistant.datastore.sharedPreferences.SharedPerferencesClient;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.basic.utils.app.AppInfoUtil;

public class VersionManager {

    public static void saveVersion(Context context,IAppVersion appVersion){
        String s=appVersion.api().toJsonString();
        SharedPerferencesClient.with(context)
                .name(AppConstant.SHAREDPERFERENCES_CLIENT)
                .build()
                .put(AppConstant.SHAREDPERFERENCES_CLIENT_NAME_VERSION,s);
    }

    public static void saveCurrVersion(Context context){
        IAppVersion version=AppVersionImpl.newInstance()
                .code(AppInfoUtil.getVersionCode(context))
                .name(AppInfoUtil.getVersionName(context));
        saveVersion(context,version);
    }

    public static IAppVersion.Api getLastVersion(Context context){
        IAppVersion version=AppVersionImpl.newInstance();
        IAppVersion last= SharedPerferencesClient.with(context)
                .name(AppConstant.SHAREDPERFERENCES_CLIENT)
                .build()
                .get(AppConstant.SHAREDPERFERENCES_CLIENT_NAME_VERSION,AppVersionImpl.newInstance().code(-999));
        if (last==null || last.api().getCode()==-999){
            saveVersion(context,version);
            return version.api();
        }
        return last.api();
    }

    public static boolean versionIsDiff(Context context){
        IAppVersion.Api last=getLastVersion(context);
        return last.getCode()!= AppInfoUtil.getVersionCode(context);
    }

}
