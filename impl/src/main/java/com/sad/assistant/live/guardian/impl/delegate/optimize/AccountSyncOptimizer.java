package com.sad.assistant.live.guardian.impl.delegate.optimize;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;
import com.sad.assistant.live.guardian.api.provider.AccountSyncProvider;

@GuardiaDelegate(name = "OPTIMIZE_ACCOUNTSYNC")
public class AccountSyncOptimizer implements IOptimizer {
    private String accountType="com.sad.assistant.live.guardian";
    protected AccountSyncOptimizer(){

    }
    @Override
    public void optimize(Context context) {
        //accountType=context.getPackageName();
        if (isOptimized(context)){
            autoSync(context);
            return;
        }
        AccountManager am = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        //添加账户context.getPackageName()
        Account account = new Account("xxx",accountType);
        am.addAccountExplicitly(account, "628157", new Bundle());
        autoSync(context);
    }

    @Override
    public boolean isOptimized(Context context) {
        accountType=context.getPackageName();
        AccountManager am = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        //获得此类型的账户
        Account[] accounts = am.getAccountsByType(accountType);
        if (accounts.length > 0) {
            //账户存在
            return true;
        }
        return false;
    }

    /**
     * 设置 账户自动同步
     */
    private void autoSync(Context context) {
        Account dongnao = new Account(context.getPackageName(), accountType);
        //设置同步
        ContentResolver.setIsSyncable(dongnao, context.getPackageName()+".provider", 1);
        //自动同步
        ContentResolver.setSyncAutomatically(dongnao, context.getPackageName()+".provider", true);
        //设置同步周期
        ContentResolver.addPeriodicSync(dongnao, context.getPackageName()+".provider", new Bundle(), 1);

    }
}
