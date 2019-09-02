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

    public boolean config_err(String mainPkgName,String moduleName,String gurdianId){
        if (mainPkgName==null || "".equals(moduleName) || gurdianId==null || "".equals(gurdianId)){
            error("未在Gradle脚本里配置AppPackageName或AppGurdianId,请按照以下格式配置app主包名和AppGurdianId：\n" +
                    "android{\n" +
                    "   ...\n"+
                    "   defaultConfig{\n" +
                    "       ...\n"+
                    "       javaCompileOptions {\n" +
                    "            annotationProcessorOptions {\n" +
                    "                arguments = [\n" +
                    "                        AppPackageName:xxx.xxx.xxx,\n" +
                    "                        ModuleName:yyy,\n" +
                    "                        AppGurdianId:'zzzzzzz'\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        }\n" +
                    "       ...\n"+
                    "   }\n" +
                    "   ...\n"+
                    "}\n"
            );
            return true;
        }
        return false;
    }

}
