package com.sad.assistant.live.guardian.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sad.assistant.live.guardian.api.GuardianSDK;

public class GuardiaService5 extends AbsBaseGuardiaService {

    @Override
    int serviceId() {
        return 5;
    }
}
