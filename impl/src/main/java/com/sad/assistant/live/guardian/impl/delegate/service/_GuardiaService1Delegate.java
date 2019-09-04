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
import android.os.RemoteException;
import android.util.Log;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.assistant.live.guardian.api.IGuardiaTaskStudio;
import com.sad.assistant.live.guardian.api.IGuardiaTasksClasseTraversedCallback;
import com.sad.assistant.live.guardian.api.InstanceProvider;
import com.sad.assistant.live.guardian.api.parameters.ActionSource;
import com.sad.assistant.live.guardian.api.parameters.GuardiaTaskParameters;
import com.sad.assistant.live.guardian.api.parameters.ICommunicant;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaFuture;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaTask;
import com.sad.assistant.live.guardian.api.parameters.INotificationStyle;
import com.sad.assistant.live.guardian.api.service.GuardiaService2;
import com.sad.assistant.live.guardian.api.service.IService2AidlInterface;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;
import com.sad.assistant.live.guardian.api.R;
import com.sad.assistant.live.guardian.impl.utils.NotificationUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.app.Service.START_REDELIVER_INTENT;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@GuardiaDelegate(name = "SERVICE_1")
public class _GuardiaService1Delegate implements IServiceDelegate {

    private ICommunicant communicant=new ICommunicant() {
        @Override
        public void postFuture(IGuardiaFuture future) {
            INotificationStyle style=future.notificationStyle();
            updateNotification(service,style);
        }
    };

    private final class ServiceBinder extends IService2AidlInterface.Stub {

        @Override
        public void action(GuardiaTaskParameters parameters) throws RemoteException {
            /*if (!workIsStarted){
                doWork(parameters,ActionSource.REMOTE_WAKE);
            }*/
           INotificationStyle style=parameters==null?null:parameters.getNotificationStyle();
            updateNotification(service,style);
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
        this.workIsStarted=false;

    }
    @Override
    public IBinder onBind(Service service,Intent intent) {
        this.service=service;
        this.bundle=intent.getExtras();
        if (!workIsStarted){
            startUp(service,intent,ActionSource.REMOTE_WAKE);
        }
        return getServiceBinder();
    }

    @Override
    public int onStartCommand(Service service, Intent intent, int flags, int startId) {
        if (!workIsStarted){
            startUp(service,intent,ActionSource.LOCAL_WAKE);
        }
        return START_REDELIVER_INTENT;
    }

    private boolean startUp(Service service, Intent intent,ActionSource source){
        try {
            GuardiaTaskParameters parameters=null;
            bundle=intent.getExtras();
            if (bundle!=null){
                parameters=bundle.getParcelable(AppConstant.INTENT_KEY_SERVICEAIDLPARAMETERS);
            }

            //foreground service
            if (parameters!=null){
                updateNotification(service,parameters.getNotificationStyle());
            }
            else {
                updateNotification(service,null);
            }

            //music
            if (mediaPlayer!=null){
                return false;
            }

            //heartBeat start
            long h= GuardianSDK.getInstance().getHeartBeat();
            if (h<8){
                h=8;
            }
            heartBeat(service,h,GuardianSDK.getInstance().getHeartBeatTimeUnit());

            //Demon Service
            Intent intent3 = new Intent(service, GuardiaService2.class);
            if (bundle!=null){
                intent3.putExtras(bundle);
            }
            service.bindService(intent3, connection, Context.BIND_ABOVE_CLIENT);

            //startwork
            doWork(parameters,source);
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private void updateNotification(Service context,INotificationStyle style){
        NotificationUtils.updateNotification(context,style);
    }
    private ScheduledExecutorService audioExecutor;
    private void heartBeat(Service service,long t,TimeUnit unit){
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
                    GuardiaTaskParameters parameters=bundle.getParcelable(AppConstant.INTENT_KEY_SERVICEAIDLPARAMETERS);
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

    private boolean workIsStarted=true;
    private void doWork(GuardiaTaskParameters parameters,ActionSource defaultSource){
        workIsStarted=true;
        if (parameters!=null){
            int singleTarget=parameters.getSingleTarget();
            ActionSource source = parameters.getSource();
            if (singleTarget!= GuardiaTaskParameters.NO_SINGLETARGET){
                IGuardiaTask task=GuardianSDK.getInstance()
                        .guardian()
                        .guardiaStudio()
                        .obtain(singleTarget,true,false);
                if (task!=null){
                    task.onWork(service.getApplicationContext(),source,communicant);
                }
            }
            else{
                GuardianSDK.getInstance()
                        .guardian()
                        .guardiaStudio()
                        .traverse(new IGuardiaTasksClasseTraversedCallback() {
                            @Override
                            public <D> D OnTraversed(int tag, Class<? extends IGuardiaTask> cls, InstanceProvider<Integer, IGuardiaTask> instanceProvider) {
                                IGuardiaTask task=instanceProvider.obtain(tag,true,false);
                                if (task!=null){
                                    return task.onWork(service.getApplicationContext(),source,communicant);
                                }
                                return null;
                            }
                        });
            }
        }
        else {
            GuardianSDK.getInstance()
                    .guardian()
                    .guardiaStudio()
                    .traverse(new IGuardiaTasksClasseTraversedCallback(){
                        @Override
                        public <D> D OnTraversed(int tag, Class<? extends IGuardiaTask> cls, InstanceProvider<Integer, IGuardiaTask> instanceProvider) {
                            IGuardiaTask task=instanceProvider.obtain(tag,true,false);
                            if (task!=null){
                                return task.onWork(service.getApplicationContext(),defaultSource,communicant);
                            }
                            return null;
                        }
                    });
        }
    }
}
