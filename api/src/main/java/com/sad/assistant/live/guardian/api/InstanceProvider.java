package com.sad.assistant.live.guardian.api;

public interface InstanceProvider<K,V> {

    <W extends V> W obtain(K k,boolean isNew,boolean update);

    <W extends V> W obtain(K k, boolean isNew, boolean update,Class<?>[] parametersType,Object... parameters);

}
