package com.sad.assistant.live.guardian.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AppLiveGuardian {

    long heartbeat() default 12;

    TimeUnit timeunit() default TimeUnit.SECONDS;

}
