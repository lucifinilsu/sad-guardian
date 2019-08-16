package com.sad.assistant.live.guardian.compiler;

import com.google.auto.service.AutoService;
import com.sad.assistant.live.guardian.annotation.AppLiveGuardian;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@SupportedOptions({"AppPackageName","AppGurdianId"})
@SupportedAnnotationTypes({
        "com.sad.assistant.live.guardian.annotation.AppLiveGuardian"
})
public class GuardianProcessor extends AbstractProcessor implements OnCompiledAuthenticationFailureCallback {

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;
    protected ProcessorLog log;
    protected boolean isLog=false;
    protected String mainPkgName =null;
    protected String gurdianId=null;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
        processingEnv=env;
        filer = env.getFiler();
        typeUtils = env.getTypeUtils();
        messager = env.getMessager();
        log=new ProcessorLog(messager);
        mainPkgName =env.getOptions().get("AppPackageName");
        gurdianId=env.getOptions().get("AppGurdianId");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)){
            Set<? extends Element> gurdianElements=roundEnvironment.getElementsAnnotatedWith(AppLiveGuardian.class);
            if (!CollectionUtils.isNotEmpty(gurdianElements)){
                log.error("未发现注解AppLiveGuardian");
                return true;
            }
            List<Element> list=new ArrayList<>(gurdianElements);
            if (list.size()>1){
                log.error("请保留唯一一个AppLiveGuardian注解对象");
                return true;
            }
            if (mainPkgName==null || "".equals(mainPkgName) || gurdianId==null || "".equals(gurdianId)){
                log.error("未在Gradle脚本里配置AppPackageName或AppGurdianId,请按照以下格式配置app主包名和AppGurdianId：\n" +
                        "android{\n" +
                        "   ...\n"+
                        "   defaultConfig{\n" +
                        "       ...\n"+
                        "       javaCompileOptions {\n" +
                        "            annotationProcessorOptions {\n" +
                        "                arguments = [\n" +
                        "                        AppPackageName:xxx.xxx.xxx,\n" +
                        "                        AppGurdianId:'xxxxxxxxx'\n" +
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
            Element elementK=list.get(0);
            if (elementK.getKind() != ElementKind.CLASS) {
                log.error("错误的注解类型，只有【类】才能够被该 AppLiveGuardian 注解处理");
                return true;
            }
            Set<Modifier> mod=elementK.getModifiers();
            if (mod.contains(Modifier.ABSTRACT)){
                String note=elementK.getSimpleName().toString()+"是抽象类，AppLiveGuardian仅能注解在实现类上:";
                log.error(note);
                return true;
            }
            Element elementIAC=elementUtils.getTypeElement("android.app.Application");
            if (!typeUtils.isSubtype(typeUtils.erasure(elementK.asType()),typeUtils.erasure(elementIAC.asType()))){
                String note="请注意AppLiveGuardian仅可以注解android.app.Application的子类";
                log.error(note);
                return true;
            }
            AuthenticationFuture future=AuthenticationMaster.start(mainPkgName, new OnCompiledAuthenticationSuccessCallback() {
                @Override
                public void onDone(AuthenticationSuccessInfo successInfo) {

                }
            },this,log);
            if (!future.isSuccessful()){
                return true;
            }
            else {
                log.info("-------------------->开始工作");
                doCreateJavaCode(
                        filer,
                        0,
                        "optimize/BatteryOptimizerImpl.java",
                        "com.sad.assistant.live.guardian.impl.optimize",
                        "BatteryOptimizerImpl"
                        );
                doCreateJavaCode(
                        filer,
                        1,
                        "optimize/AppBootOptimizerImpl.java",
                        "com.sad.assistant.live.guardian.impl.optimize",
                        "AppBootOptimizerImpl"
                );
            }
        }
        return false;
    }


    private void doCreateJavaCode(
            Filer filer,
            int tag,
            String remotePath,
            String targetPackgeName,
            String targetClassName
    ){
        SourceCodeFactoryImpl.newInstance()
                .tag(tag)
                .path(remotePath)
                .get(new ISourceCodeFactory.OnGetCodeSuccessListener() {
                    @Override
                    public void onGetCode(IServerSourceCodeFuture future) {
                        future.get().writeIn(
                                filer,
                                targetPackgeName,
                                targetClassName);
                    }
                }, new ISourceCodeFactory.OnGetCodeFailureListener() {
                    @Override
                    public void onIOErr(IOException e) {

                    }

                    @Override
                    public void onErr(Error error) {

                    }

                    @Override
                    public void onHttpIOErr(int actionTag, int code, String msg) {

                    }
                });
    }


    @Override
    public void onParseResourceErr(AuthenticationFailureInfo info) {
        log.error("鉴权校验错误："+info.getCode());
    }

    @Override
    public void onInvalid(AuthenticationFailureInfo info) {

    }

    @Override
    public void onHttpIOErr(int actionTag, int code, String msg) {
        if (actionTag==AuthenticationMaster.ACTION_TAG_AUTHENTICATION){
            log.error("鉴权校验错误："+code);
        }
    }
}
