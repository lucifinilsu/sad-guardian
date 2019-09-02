// IService2AidlInterface.aidl
package com.sad.assistant.live.guardian.api.service;

// Declare any non-default types here with import statements
import com.sad.assistant.live.guardian.api.parameters.GuardiaTaskParameters;
interface IService2AidlInterface {
    //相互唤醒进程
    void action(inout GuardiaTaskParameters parameters);
}
