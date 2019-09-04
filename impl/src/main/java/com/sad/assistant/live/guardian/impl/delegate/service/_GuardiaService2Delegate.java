package com.sad.assistant.live.guardian.impl.delegate.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.AppConstant;
import com.sad.assistant.live.guardian.api.parameters.INotificationStyle;
import com.sad.assistant.live.guardian.api.parameters.GuardiaTaskParameters;
import com.sad.assistant.live.guardian.api.service.IService2AidlInterface;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;
import com.sad.assistant.live.guardian.api.service.GuardiaService1;
import com.sad.assistant.live.guardian.impl.utils.NotificationUtils;

@GuardiaDelegate(name = "SERVICE_2")
public class _GuardiaService2Delegate implements IServiceDelegate {

    private ServiceBinder mBinder;

    public ServiceBinder getServiceBinlder() {
        if (mBinder ==null){
            mBinder =new ServiceBinder();
        }
        return mBinder;
    }

    private Service service;
    private Bundle bundle;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (service!=null){
                if (service!=null){
                    //restart
                    Intent startRemoteServiceIntent = new Intent(service, GuardiaService1.class);
                    if (bundle!=null){
                        startRemoteServiceIntent.putExtras(bundle);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        service.startForegroundService(startRemoteServiceIntent);
                    }
                    else {
                        service.startService(startRemoteServiceIntent);
                    }
                    //rebind
                    Intent bindRemoteServiceIntent = new Intent(service,GuardiaService1.class);
                    if (bundle!=null){
                        bindRemoteServiceIntent.putExtras(bundle);
                    }
                    service.bindService(bindRemoteServiceIntent, connection, Context.BIND_ABOVE_CLIENT);
                }

            }
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            if (bundle!=null){
                GuardiaTaskParameters parameters=bundle.getParcelable(AppConstant.INTENT_KEY_SERVICEAIDLPARAMETERS);
                if (parameters!=null){
                    try {
                        IService2AidlInterface aidl= IService2AidlInterface.Stub.asInterface(service);
                        aidl.action(parameters);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };

    @Override
    public void onCreate(Service service) {
        this.service=service;
    }

    @Override
    public IBinder onBind(Service service, Intent intent) {
        this.service=service;
        this.bundle=intent.getExtras();
        return getServiceBinlder();
    }

    @Override
    public int onStartCommand(Service service, Intent intent, int flags, int startId) {
        try {
            bundle=intent.getExtras();
            Intent intent1=new Intent(service, GuardiaService1.class);
            if (bundle!=null){
                intent1.putExtras(bundle);
            }
            INotificationStyle style=null;
            if (bundle!=null){
                GuardiaTaskParameters parameters=bundle.getParcelable(AppConstant.INTENT_KEY_SERVICEAIDLPARAMETERS);
                if (parameters!=null){
                    style=parameters.getNotificationStyle();
                }
            }
            updateNotification(service,style);
            service.bindService(intent1, connection, Context.BIND_ABOVE_CLIENT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Service.START_REDELIVER_INTENT;
    }

    private final class ServiceBinder extends IService2AidlInterface.Stub {

        @Override
        public void action(GuardiaTaskParameters parameters) throws RemoteException {
            /*if (parameters!=null){
                INotificationStyle style=parameters.getNotificationStyle();
                if (style!=null){
                    updateNotification(style);
                }
            }*/
        }
    }

    private void updateNotification(Service context,INotificationStyle style){
        /*Intent intent2 = new Intent(getApplicationContext(), NotificationClickReceiver.class);
        intent2.setAction(NotificationClickReceiver.CLICK_NOTIFICATION);
        Notification notification = NotificationUtils.createNotification(this, style.api().getTitle(), style.api().getDescription(), style.api().getIconRes(), intent2);
        startForeground(AppConstant.NOTIFICATION_CHANNEL, notification);*/
        NotificationUtils.updateNotification(context,style);

    }

    @Override
    public void onDestroy(Service service) {
        service.unbindService(connection);
    }
}
