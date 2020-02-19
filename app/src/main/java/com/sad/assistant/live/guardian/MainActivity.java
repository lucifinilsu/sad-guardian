package com.sad.assistant.live.guardian;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.assistant.live.guardian.api.optimize.IOptimizer;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IOptimizer batteryOptimizer= GuardianSDK.getInstance()
                .guardian()
                .delegateStudio()
                .optimizerProvider()
                .batteryOptimizer();
        batteryOptimizer.optimize(this);

        /*IWifiSleepOptimizer wifiSleepOptimizer=GuardianSDK.getDefault().delegateStudio().getDelegateInstance("OPTIMIZE_WIFISLEEP");
        wifiSleepOptimizer.optimize(this);
        */

        /*IOptimizer appBootOptimizer= GuardianSDK.getInstance()
                .guardian()
                .delegateStudio()
                .optimizerProvider()
                .appBootOptimizer()
                ;
        appBootOptimizer.optimize(this);*/

        //registerContentObserver(this);
    }

    private void bootStatus(){

    }


    private void registerContentObserver(Context context){
        ContentObserver observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange,Uri uri) {
                Log.e("GUARDIAN","--------------------------------->数据变化:"+uri.toString());
            }
        };
        //Uri uri=Settings.Global.getUriFor( Settings.Global.WIFI_SLEEP_POLICY);
        //Uri uri= Uri.parse("content://android.provider/");
        Uri uri= Settings.Secure.CONTENT_URI;
        Log.e("GUARDIAN","--------------------------------->uri="+uri.toString());
        context.getContentResolver().registerContentObserver(uri,true,observer);


    }

}
