package com.sad.assistant.live.guardian.impl;

import com.sad.assistant.live.guardian.api.IDelegateStudio;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
@SuppressWarnings("unchecked")
public class DelegateStudioImpl implements IDelegateStudio {
    private final static Map<String,Class<?>> _DELEGAETE_IMPLS = new HashMap<String,Class<?>>();
    protected static IDelegateStudio newInstance(){
        return new DelegateStudioImpl();
    }

    protected DelegateStudioImpl(){}

    @Override
    public <D> D getDelegateInstance(String s) {
        Class<D> cls= (Class<D>) _DELEGAETE_IMPLS.get(s);
        try {
            Constructor<D> constructor=cls.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IDelegateStudio put(String s, Class o) {
        _DELEGAETE_IMPLS.put(s,o);
        return this;
    }

    @Override
    public int size() {
        return _DELEGAETE_IMPLS.size();
    }
}
