package com.sad.assistant.live.guardian.api;

import android.app.Application;
import android.content.Context;

import com.sad.assistant.live.guardian.annotation.AppLiveGuardian;
import com.sad.assistant.live.guardian.api.init.IAppWork;

public class GuardianSDK {

    private static GuardianSDK sdk;

    private IGuardian guardian;

    public IGuardian guardian() {
        return guardian;
    }

    private GuardianSDK(){

    }

    public static GuardianSDK getInstance(){
        return sdk;
    }

    public static void init(Application application){
        AppLiveGuardian appLiveGuardian=application.getClass().getAnnotation(AppLiveGuardian.class);
        if (appLiveGuardian!=null){

            if (sdk == null) {
                synchronized (GuardianSDK.class) {
                    if (sdk == null) {
                        try {
                            String c=application.getClass().getPackage().getName()+".Repository";
                            Class<IRepository> cls= (Class<IRepository>) Class.forName(c,true,IRepository.class.getClassLoader());
                            IRepository repository=cls.getDeclaredConstructor().newInstance();
                            sdk=new GuardianSDK();
                            sdk.guardian=repository.registerIn(application);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (sdk!=null){
                IAppWork appWork=sdk.guardian.appWork();
                application.registerActivityLifecycleCallbacks(appWork.activityLifecycleCallbacks(null));
            }
        }

    }

    /*private static Context context=null;
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
    }*/


}
