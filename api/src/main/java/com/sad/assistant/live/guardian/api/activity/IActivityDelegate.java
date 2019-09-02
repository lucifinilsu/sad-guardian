package com.sad.assistant.live.guardian.api.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sad.assistant.live.guardian.api.IDelegate;


public interface IActivityDelegate extends IDelegate {
    
    default void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
       
    }

    default void onActivityCreated(@NonNull Activity var1, @Nullable Bundle var2){
        
    }

    default void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        
    }

    default void onActivityPreStarted(@NonNull Activity activity) {
        
    }

    default void onActivityStarted(@NonNull Activity var1){

    }

    default void onActivityPostStarted(@NonNull Activity activity) {
        
    }

    default void onActivityPreResumed(@NonNull Activity activity) {
        
    }

    default void onActivityResumed(@NonNull Activity var1){

    }

    default void onActivityPostResumed(@NonNull Activity activity) {
        
    }

    default void onActivityPrePaused(@NonNull Activity activity) {
        
    }

    default void onActivityPaused(@NonNull Activity var1){

    }

    default void onActivityPostPaused(@NonNull Activity activity) {
        
    }

    default void onActivityPreStopped(@NonNull Activity activity) {
        
    }

    default void onActivityStopped(@NonNull Activity var1){

    }

    default void onActivityPostStopped(@NonNull Activity activity) {
        
    }

    default void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        
    }

    default void onActivitySaveInstanceState(@NonNull Activity var1, @NonNull Bundle var2){

    }

    default void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        
    }

    default void onActivityPreDestroyed(@NonNull Activity activity) {
        
    }

    default void onActivityDestroyed(@NonNull Activity var1){

    }

    default void onActivityPostDestroyed(@NonNull Activity activity) {
        
    }

}
