package com.sad.assistant.live.guardian.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javawriter.JavaWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public interface IServerSourceCode {

    static void createClass(Filer filer, String implClassPackageName, String implClassName, String code) throws Exception{


        /*File file =new File(pa);
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
                .close();*/
        try {

            FileObject fileObject= filer.getResource(StandardLocation.SOURCE_OUTPUT,implClassPackageName,implClassName+".java");

            TypeSpec.Builder tb_pkgPlaceHolder=TypeSpec.classBuilder(implClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addInitializerBlock(CodeBlock.builder()
                            .addStatement("//Hello")
                            .build())
                    ;
            JavaFile.Builder jb_keeper= JavaFile.builder(implClassPackageName,tb_pkgPlaceHolder.build());
            jb_keeper.build().writeTo(filer);

            String pa=fileObject.getName();
            //IOUtils.writeFileFromString(pa,code);
        }catch (Exception e){
            e.printStackTrace();
        }


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
