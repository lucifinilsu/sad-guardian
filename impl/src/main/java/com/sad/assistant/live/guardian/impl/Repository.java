package com.sad.assistant.live.guardian.impl;

import android.content.Context;

import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.sad.assistant.live.guardian.annotation.GuardiaTask;
import com.sad.assistant.live.guardian.api.GuardianSDK;
import com.sad.assistant.live.guardian.api.IGuardiaTaskRegister;
import com.sad.assistant.live.guardian.api.IGuardian;
import com.sad.assistant.live.guardian.api.IRepository;
import com.sad.assistant.live.guardian.api.parameters.IGuardiaTask;
import com.sad.basic.utils.clazz.ClassScannerClient;
import com.sad.basic.utils.clazz.ClassScannerFilter;

import java.lang.reflect.Constructor;
import java.util.Set;

@SuppressWarnings("unchecked")
public class Repository implements IRepository {
  private IGuardian guardian = null;

  protected Repository() {
  }

  @Override
  public IGuardian registerIn(Context context) {
    try {
      //新增
      guardian=DefaultGuardian.newInstance(context);
      Set<String> delegateClsNames=ClassScannerClient.with(context)
                              .instantRunSupport(true)
                              .build()
                              .scan("com.sad.assistant.live.guardian.impl.delegate", new ClassScannerFilter() {
                                @Override
                                public boolean accept(Class<?> cls) {
                                  GuardiaDelegate guardiaDelegate=cls.getAnnotation(GuardiaDelegate.class);
                                  if (guardiaDelegate==null){
                                    return false;
                                  }
                                  String name=guardiaDelegate.name();
                                  guardian.delegateStudio().put(name,cls);
                                  return true;
                                }
                              });
      Set<String> taskRegisterClsNames = ClassScannerClient.with(context)
                              .instantRunSupport(true)
                              .build()
                              .scan("com.sad.assistant.live.guardian.api.task", new ClassScannerFilter() {
                                @Override
                                public boolean accept(Class<?> cls) {
                                  try {
                                    Constructor<IGuardiaTaskRegister> constructor= (Constructor<IGuardiaTaskRegister>) cls.getDeclaredConstructor();
                                    constructor.setAccessible(true);
                                    IGuardiaTaskRegister register=constructor.newInstance();
                                    register.registerIn();
                                    return true;
                                  }catch (Exception e){
                                    e.printStackTrace();
                                  }
                                  return false;
                                }
                              });
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    return guardian;
  }
}
