package com.sad.assistant.live.guardian.api;

import android.content.Context;

public final class GuardianSDK {

    private static Context context=null;
    private static IGuardian guardian= null;

    public static Context getContext() {
        return context;
    }

    public static IGuardian getDefault() {
        return guardian;
    }

    public static void init(Context context){
        GuardianSDK.context=context;
        try {
            String clsn="com.sad.assistant.live.guardian.impl.Repository";
            Class<IRepository> cls= (Class<IRepository>) Class.forName(clsn);
            IRepository repository=cls.getDeclaredConstructor().newInstance();
            guardian=repository.registerIn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
