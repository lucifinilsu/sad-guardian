package com.sad.assistant.live.guardian.compiler;


import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Created by Administrator on 2018/5/21 0021.
 */

public class ProcessorLog {

    private Messager messager;
    public ProcessorLog(Messager messager){
        this.messager=messager;
    }
    //打印错误信息
    public void error( String err) {
        messager.printMessage(Diagnostic.Kind.ERROR,err);
    }

    public void info(String info){
        messager.printMessage(Diagnostic.Kind.NOTE, info);
    }

}
