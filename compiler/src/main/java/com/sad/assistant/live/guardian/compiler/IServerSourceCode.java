package com.sad.assistant.live.guardian.compiler;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public interface IServerSourceCode {

    static FileObject createClass(Filer filer, String implClassPackageName, String implClassName, String code) throws Exception{
        FileObject fileObject= filer.getResource(StandardLocation.SOURCE_OUTPUT,implClassPackageName,implClassName+".java");
        String pa=fileObject.getName();
        IOUtils.writeFileFromString(pa,code);
        return fileObject;
    }

    String get();


    default void writeIn(Filer filer,String packageName,String className){
        String s=get();
        if (s!=null && !"".equals(s)){
            try {
                createClass(filer,packageName,className,s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
