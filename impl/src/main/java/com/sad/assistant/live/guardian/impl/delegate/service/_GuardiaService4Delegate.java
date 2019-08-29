package com.sad.assistant.live.guardian.impl.delegate.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.service.IServiceDelegate;
@GuardiaDelegate(name = "SERVICE_4")
public class _GuardiaService4Delegate implements IServiceDelegate {

    private Service service;
    private AccountAuthenticator accountAuthenticator;

    public AccountAuthenticator getAccountAuthenticator(Service service) {
        if (accountAuthenticator==null){
            accountAuthenticator = new AccountAuthenticator(service);
        }
        return accountAuthenticator;
    }

    @Override
    public IBinder onBind(Service service,Intent intent) {
        //return accountAuthenticator.getIBinder();
        //限制了只有在AccountManagerService绑定service时才返回Authenticator的binder


        if (AccountManager.ACTION_AUTHENTICATOR_INTENT.equals(intent.getAction())) {
            return getAccountAuthenticator(service).getIBinder();
        } else {
            return null;
        }
    }

    @Override
    public void onCreate(Service service) {
        getAccountAuthenticator(service);

    }


    static class AccountAuthenticator extends AbstractAccountAuthenticator {


        public AccountAuthenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                                 String authTokenType, String[] requiredFeatures, Bundle options)
                throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
                                         Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String
                authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                                        String authTokenType, Bundle options) throws
                NetworkErrorException {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
                                  String[] features) throws NetworkErrorException {
            return null;
        }
    }
}
