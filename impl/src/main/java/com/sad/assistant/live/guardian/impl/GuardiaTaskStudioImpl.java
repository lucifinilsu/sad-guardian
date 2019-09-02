package com.sad.assistant.live.guardian.impl;

import com.sad.assistant.live.guardian.api.IGuardiaTaskStudio;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaTask;

import org.apache.commons.lang3.concurrent.ConcurrentUtils;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

@SuppressWarnings("unchecked")
public class GuardiaTaskStudioImpl implements IGuardiaTaskStudio {
    private final static ConcurrentSkipListMap<Integer,Class<? extends IGuardiaTask>> _GUARDIA_TASKS=new ConcurrentSkipListMap<>(Collections.reverseOrder());
    private final static ConcurrentSkipListMap<Integer,IGuardiaTask> _GUARDIA_TASKS_INSTANCE=new ConcurrentSkipListMap<>(Collections.reverseOrder());

    protected GuardiaTaskStudioImpl(){

    }

    protected static IGuardiaTaskStudio newInstance(){
        return new GuardiaTaskStudioImpl();
    }

    @Override
    public IGuardiaTaskStudio put(int tag, Class<? extends IGuardiaTask> cls) {
        _GUARDIA_TASKS.put(tag,cls);
        return this;
    }

    @Override
    public <D extends IGuardiaTask> Class<D> get(int tag) {
        return (Class<D>) _GUARDIA_TASKS.get(tag);
    }

    @Override
    public NavigableSet<Integer> indexes() {
        return _GUARDIA_TASKS.keySet();
    }

    @Override
    public int size() {
        return _GUARDIA_TASKS.size();
    }

    @Override
    public void remove(int tag) {
        _GUARDIA_TASKS.remove(tag);
    }

    @Override
    public void removeInstance(int tag) {
        _GUARDIA_TASKS_INSTANCE.remove(tag);
    }

    @Override
    public void clear() {
        _GUARDIA_TASKS.clear();
    }

    @Override
    public void clearInstances() {
        _GUARDIA_TASKS_INSTANCE.clear();
    }

    @Override
    public void traverse(ITraversedCallback traversedCallback) {
        while (_GUARDIA_TASKS.entrySet().iterator().hasNext()){
            Map.Entry<Integer,Class<? extends IGuardiaTask>> entry=_GUARDIA_TASKS.entrySet().iterator().next();
            int key=entry.getKey();
            Class<? extends IGuardiaTask> cls=entry.getValue();
            if (traversedCallback!=null){
                traversedCallback.OnTraversed(key,cls,this);
            }
        }
    }

    private <D extends IGuardiaTask> D getTaskInstance(Integer s,Class<?>[] parametersType,Object... parameters) {
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
    public <O extends IGuardiaTask> O obtain(Integer k, boolean isNew, boolean update) {
        return obtain(k,isNew,update,null);
    }

    @Override
    public <O extends IGuardiaTask> O obtain(Integer k, boolean isNew, boolean update, Class<?>[] parametersType, Object... parameters) {
        O instance = null;
        if (isNew){
            instance=getTaskInstance(k,parametersType,parameters);
        }
        else {
            instance= (O) _GUARDIA_TASKS_INSTANCE.get(k);
            if (instance==null){
                instance=getTaskInstance(k,parametersType,parameters);
            }
        }
        if (update && instance!=null){
            _GUARDIA_TASKS_INSTANCE.put(k,instance);
        }
        if (instance!=null){
            return  instance;
        }
        return null;
    }
    
}
