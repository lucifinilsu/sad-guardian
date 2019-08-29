package com.sad.assistant.live.guardian.impl.delegate.service;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;

@GuardiaDelegate(name = "SERVICE_5")
public class _GuardiaService5Delegate implements IServiceDelegate {

    private _GuardiaService5Delegate(){}

    public IServiceDelegate newInstance(){
        return new _GuardiaService5Delegate();
    }

    SyncAdapter syncAdapter;

    public SyncAdapter getSyncAdapter(Service service) {
        if (syncAdapter==null){
            syncAdapter = new SyncAdapter(service.getApplicationContext(), true);
        }
        return syncAdapter;
    }

    @Override
    public IBinder onBind(Service service,Intent intent) {
        return getSyncAdapter(service).getSyncAdapterBinder();
    }

    @Override
    public void onCreate(Service service) {
        getSyncAdapter(service);
    }


    static class SyncAdapter extends AbstractThreadedSyncAdapter {

        public SyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority,
                                  ContentProviderClient provider, SyncResult syncResult) {

            //账户同步
            Log.e("GUARDIAN","---------------------->账户同步：name="+account.name+",syncResult="+syncResult.toString());
        }
    }
}
