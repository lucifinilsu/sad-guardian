package com.sad.assistant.live.guardian.api;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.sad.assistant.live.guardian.annotation.AppLiveGuardian;
import com.sad.assistant.live.guardian.api.init.IAppWork;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;
import com.sad.assistant.live.guardian.api.parameters.GuardiaTaskParameters;
import com.sad.assistant.live.guardian.api.parameters.INotificationStyle;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class GuardianSDK {

    private static GuardianSDK sdk;

    private IGuardian guardian;
    private long heartBeat=12;
    private TimeUnit heartBeatTimeUnit=TimeUnit.SECONDS;
    private Application application;

    public long getHeartBeat() {
        return heartBeat;
    }

    public TimeUnit getHeartBeatTimeUnit() {
        return heartBeatTimeUnit;
    }

    public IGuardian guardian() {
        return guardian;
    }

    private GuardianSDK(Application application){
        this.application=application;
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
                            Constructor<IRepository> constructor=cls.getDeclaredConstructor();
                            constructor.setAccessible(true);
                            IRepository repository=constructor.newInstance();
                            sdk=new GuardianSDK(application);
                            sdk.heartBeat=appLiveGuardian.heartbeat();
                            sdk.heartBeatTimeUnit=appLiveGuardian.timeunit();
                            sdk.guardian=repository.registerIn(application);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (sdk!=null){
                IAppWork appWork=sdk.guardian
                        .delegateStudio()
                        .optimizerProvider()
                        .appWork(false,true);
                if (appWork!=null){
                    appWork.initApplication(application);
                }
            }
        }
    }

    public void start(GuardiaTaskParameters parameters){
        if (guardian!=null){
            Bundle bundle=new Bundle();
            if(parameters==null){
                parameters=new GuardiaTaskParameters();
            }
            bundle.putParcelable(AppConstant.INTENT_KEY_SERVICEAIDLPARAMETERS,parameters);
            guardian.start(bundle);
        }
    }

}
