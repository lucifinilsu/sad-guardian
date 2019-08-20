package com.sad.assistant.live.guardian.impl;

import android.content.Context;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.api.IGuardian;
import com.sad.assistant.live.guardian.api.IRepository;
import com.sad.basic.utils.clazz.ClassScannerClient;
import com.sad.basic.utils.clazz.ClassScannerFilter;

import java.util.Set;
@SuppressWarnings("unchecked")
public class Repository implements IRepository, ClassScannerFilter {
    private IGuardian guardian= DefaultGuardian.newInstance();
    protected Repository(){}
    @Override
    public IGuardian registerIn(Context context) {

        /*guardian.delegateStudio()
                .put("OPTIMIZE_BATTERY", BatteryOptimizerImpl.class)
                .put("OPTIMIZE_APPBOOT", AppBootOptimizerImpl.class)
                .put("OPTIMIZE_WIFISLEEP", WifiSleepOptimizerImpl.class)
                ;*/
        try {
            Set<String> clsNames=ClassScannerClient.with(context)
                    .instantRunSupport(true)
                    .build()
                    .scan("com.sad.assistant.live.guardian.impl.delegate",this);
        }catch (Exception e){
            e.printStackTrace();
        }

        return guardian;
    }

    @Override
    public boolean accept(Class<?> cls) {
        GuardiaDelegate guardiaDelegate=cls.getAnnotation(GuardiaDelegate.class);
        if (guardiaDelegate==null){
            return false;
        }
        String name=guardiaDelegate.name();
        guardian.delegateStudio()
                .put(name,cls);
        return true;
    }
}
