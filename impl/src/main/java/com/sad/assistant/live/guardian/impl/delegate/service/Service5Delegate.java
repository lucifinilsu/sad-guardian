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
public class Service5Delegate implements IServiceDelegate {

    private Service5Delegate(){}

    public IServiceDelegate newInstance(){
        return new Service5Delegate();
    }

    private Service service;

    SyncAdapter syncAdapter;

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate(Service service) {
        this.service=service;
        syncAdapter = new SyncAdapter(service.getApplicationContext(), true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy() {

    }

    static class SyncAdapter extends AbstractThreadedSyncAdapter {

        public SyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority,
                                  ContentProviderClient provider, SyncResult syncResult) {
            //Log.e(TAG,"同步账户");
            //与互联网 或者 本地数据库同步账户
        }
    }
}
