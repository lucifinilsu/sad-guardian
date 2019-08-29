package com.sad.assistant.live.guardian.impl;

import com.sad.assistant.live.guardian.api.IAndroidComponentDelegateProvider;
import com.sad.assistant.live.guardian.api.IDelegateStudio;
import com.sad.assistant.live.guardian.api.IOptimizerProvider;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class DelegateStudioImpl implements IDelegateStudio {
    private final static Map<String,Class<?>> _DELEGAETE_IMPLS = new HashMap<String,Class<?>>();
    private final static Map<String,Object>   _DELEGAETE_IMPLS_INSTANCE = new HashMap<String,Object>();
    protected static IDelegateStudio newInstance(){
        return new DelegateStudioImpl();
    }

    protected DelegateStudioImpl(){}


    private <D> D getDelegateInstance(String s) {
        Class<D> cls= get(s);
        try {
            Constructor<D> constructor=cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private <D> D getDelegateInstance(String s,Class<?>[] parametersType,Object... parameters) {
        Class<D> cls= get(s);
        try {
            Constructor<D> constructor=cls.getDeclaredConstructor(parametersType);
            constructor.setAccessible(true);
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <D> Class<D> get(String s) {
        return (Class<D>) _DELEGAETE_IMPLS.get(s);
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

    @Override
    public Set<String> indexes() {
        return _DELEGAETE_IMPLS.keySet();
    }

    @Override
    public IAndroidComponentDelegateProvider androidComponentDelegateProvider() {
        return AndroidComponentDelegateProviderImpl.newInstance(this);
    }

    @Override
    public IOptimizerProvider optimizerProvider() {
        return OptimizerProviderImpl.newInstance(this);
    }

    @Override
    public <O> O obtain(String k, boolean isNew, boolean update) {
        Object instance = null;
        if (isNew){
            instance=getDelegateInstance(k);
        }
        else {
            instance=_DELEGAETE_IMPLS_INSTANCE.get(k);
            if (instance==null){
                instance=getDelegateInstance(k);
            }
        }
        if (update && instance!=null){
            _DELEGAETE_IMPLS_INSTANCE.put(k,instance);
        }
        if (instance!=null){
            return (O) instance;
        }
        return null;
    }

    @Override
    public <O> O obtain(String k, boolean isNew, boolean update,Class<?>[] parametersType,Object... parameters) {
        O instance = null;
        if (isNew){
            instance=getDelegateInstance(k,parametersType,parameters);
        }
        else {
            instance= (O) _DELEGAETE_IMPLS_INSTANCE.get(k);
            if (instance==null){
                instance=getDelegateInstance(k,parametersType,parameters);
            }
        }
        if (update && instance!=null){
            _DELEGAETE_IMPLS_INSTANCE.put(k,instance);
        }
        if (instance!=null){
            return instance;
        }
        return null;
    }
}
