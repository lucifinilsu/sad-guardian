package com.sad.assistant.live.guardian.impl;

import com.sad.assistant.live.guardian.api.IDelegateStudio;
import com.sad.assistant.live.guardian.api.IGuardian;

public class DefaultGuardian implements IGuardian {
    protected static IGuardian newInstance(){
        return new DefaultGuardian();
    }
    protected DefaultGuardian(){}
    IDelegateStudio delegateStudio=null;
    @Override
    public IDelegateStudio delegateStudio() {
        if (delegateStudio==null){
            delegateStudio=DelegateStudioImpl.newInstance();
        }
        return delegateStudio;
    }


}
