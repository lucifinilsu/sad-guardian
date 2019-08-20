package com.sad.assistant.live.guardian.api;

import android.content.Context;

public class GuardianSDK {

    private static Context context=null;
    private static IGuardian guardian= null;

    public static Context getContext() {
        return context;
    }

    public static IGuardian getDefault() {
        if (guardian == null) {
            synchronized (IGuardian.class) {
                if (guardian == null) {
                    try {
                        String clsn="com.sad.assistant.live.guardian.impl.Repository";
                        Class<IRepository> cls= (Class<IRepository>) Class.forName(clsn,true,IRepository.class.getClassLoader());
                        IRepository repository=cls.getDeclaredConstructor().newInstance();
                        guardian=repository.registerIn(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return guardian;
    }

    public static void init(Context context){
        GuardianSDK.context=context;
    }


}
