package com.sad.assistant.live.guardian.impl.delegate.optimize;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;

@GuardiaDelegate(name = "OPTIMIZE_BATTERY")
public class BatteryOptimizerImpl implements IOptimizer {

    protected BatteryOptimizerImpl(){}

    @Override
    public void optimize(Context context) {
        String action= Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;
        try {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.setData(Uri.parse("package:"+ context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            //((Activity)context).startActivityForResult(intent,666);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            navBatteryWhiteList(context);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean isOptimized(Context context) {
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        String packageName = context.getPackageName();
        boolean isWhite = pm.isIgnoringBatteryOptimizations(packageName);
        return isWhite;
    }

    private void navBatteryWhiteList(Context context){
        String action=Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS;
        try {
            Intent intent = new Intent();
            // 进入优化列表
            intent.setAction(action);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();


        }
    }


}
