package com.sad.assistant.live.guardian.compiler;

import com.squareup.javapoet.TypeSpec;
import com.squareup.javawriter.JavaWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public interface IServerSourceCode {

    static void createClass(Filer filer, String implClassPackageName, String implClassName, String code) throws Exception{
        FileObject fileObject= filer.getResource(StandardLocation.SOURCE_OUTPUT,implClassPackageName,implClassName+".java");
        String pa=fileObject.getName();

        File file =new File(pa);
        if (!file.exists()){
            File dir=file.getParentFile();
            if (!dir.exists()){
                dir.mkdirs();
            }
            file.createNewFile();
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
        JavaWriter jw = new JavaWriter(writer);
        jw.emitPackage(implClassPackageName)
                .emitStatement(code)
                .close();

        //IOUtils.writeFileFromString(pa,code);
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
