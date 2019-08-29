package com.sad.assistant.live.guardian.impl.delegate.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.assistant.live.guardian.api.parameters.INotificationStyle;
import com.sad.assistant.live.guardian.api.parameters.ServiceAidlParameters;
import com.sad.assistant.live.guardian.api.service.GuardiaService2;
import com.sad.assistant.live.guardian.api.service.IService2AidlInterface;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;
import com.sad.assistant.live.guardian.api.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.app.Service.START_REDELIVER_INTENT;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@GuardiaDelegate(name = "SERVICE_1")
public class _GuardiaService1Delegate implements IServiceDelegate {

    private final class ServiceBinder extends IService2AidlInterface.Stub {

        @Override
        public void action(ServiceAidlParameters parameters) throws RemoteException {

        }
    }

    public ServiceBinder getServiceBinder() {
        if (mBinder==null){
            mBinder=new ServiceBinder();
        }
        return mBinder;
    }

    private ServiceBinder mBinder;
    private MediaPlayer mediaPlayer;
    private Service service;
    private Bundle bundle;
    @Override
    public void onCreate(Service service) {
        this.service=service;

    }
    @Override
    public IBinder onBind(Service service,Intent intent) {
        this.service=service;
        this.bundle=intent.getExtras();
        return getServiceBinder();
    }

    @Override
    public int onStartCommand(Service service, Intent intent, int flags, int startId) {
        try {
            bundle=intent.getExtras();
            ServiceAidlParameters parameters=bundle.getParcelable(AppConstant.INTENT_KEY_SERVICEAIDLPARAMETERS);
            if (parameters!=null && parameters.getNotificationStyle()!=null && mediaPlayer!=null){
                updateNotification(parameters.getNotificationStyle());
                return START_REDELIVER_INTENT;
            }
            //foreground service
            if (parameters!=null){
                updateNotification(parameters.getNotificationStyle());
            }
            //heartBeat start
            long h= GuardianSDK.getInstance().getHeartBeat();
            if (h<8){
                h=8;
            }
            heartBeat(h,GuardianSDK.getInstance().getHeartBeatTimeUnit());
            //Demon Service
            try {
                Intent intent3 = new Intent(service, GuardiaService2.class);
                if (bundle!=null){
                    intent3.putExtras(bundle);
                }
                service.bindService(intent3, connection, Context.BIND_ABOVE_CLIENT);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return START_REDELIVER_INTENT;
    }

    private void updateNotification(INotificationStyle style){

    }
    private ScheduledExecutorService audioExecutor;
    private void heartBeat(long t,TimeUnit unit){
        if (audioExecutor==null){
            audioExecutor= Executors.newScheduledThreadPool(3);
            audioExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Log.e("GUARDIAN","------------------------->心跳维持 !!");
                    if (service!=null && mediaPlayer == null){
                        mediaPlayer = MediaPlayer.create(service, R.raw.lowvoice);
                        mediaPlayer.setVolume(0f, 0f);
                    }
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }
            },1,t, unit);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (service!=null){
                //restart
                Intent startRemoteServiceIntent = new Intent(service, GuardiaService2.class);
                if (bundle!=null){
                    startRemoteServiceIntent.putExtras(bundle);
                }
                service.startService(startRemoteServiceIntent);
                //rebind
                Intent bindRemoteServiceIntent = new Intent(service,GuardiaService2.class);
                if (bundle!=null){
                    bindRemoteServiceIntent.putExtras(bundle);
                }
                service.bindService(bindRemoteServiceIntent, connection,
                        Context.BIND_ABOVE_CLIENT);
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                if (bundle!=null){
                    ServiceAidlParameters parameters=bundle.getParcelable(AppConstant.INTENT_KEY_SERVICEAIDLPARAMETERS);
                    if (parameters!=null){
                        IService2AidlInterface aidlInterface=IService2AidlInterface.Stub.asInterface(service);
                        aidlInterface.action(parameters);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy(Service service) {
        service.unbindService(connection);
    }
}
