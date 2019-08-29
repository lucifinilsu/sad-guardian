package com.sad.assistant.live.guardian.impl.delegate.optimize;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;

@GuardiaDelegate(name = "OPTIMIZE_WIFISLEEP")
public class WifiSleepOptimizerImpl implements IOptimizer {


    @Override
    public boolean isOptimized(Context context){
        int p = getWifiSleepPolicy(context);
        return p==2;
    }

    protected WifiSleepOptimizerImpl(){}

    private int getWifiSleepPolicy(Context context) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.WIFI_SLEEP_POLICY);//Settings.Global.putInt(context.getContentResolver(), "wifi_sleep_policy", 2);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return -2;
    }

    @Override
    public void optimize(Context context) {
        try {
            String targetClass="com.huawei.systemmanager.power.ui.PowerSettingActivity";
            Intent intent = new Intent();
            intent.setPackage("com.huawei.systemmanager");
            intent.setClassName("com.huawei.systemmanager",targetClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
