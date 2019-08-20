package com.sad.assistant.live.guardian.compiler;

import com.google.auto.service.AutoService;
import com.sad.assistant.live.guardian.annotation.AppLiveGuardian;
import com.sad.assistant.live.guardian.annotation.GuardiaDelegate;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
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
            AuthenticationFuture future=AuthenticationMaster.start(gurdianId,mainPkgName, new OnCompiledAuthenticationSuccessCallback() {
                @Override
                public void onDone(AuthenticationSuccessInfo successInfo) {

                }
            },this,log);
            if (!future.isSuccessful()){
                return true;
            }
            else {
                log.info("-------------------->开始工作");
                AuthenticationSuccessInfo successInfo=future.get();
                List<SourceCodeInfo> data=successInfo.getData();
                for (SourceCodeInfo info:data
                     ) {
                    String baseUrl=info.getBaseUrl();
                    String targetPkg=info.getPackageName();
                    String targetClass=info.getClassName();
                    String path=targetPkg.replace(".","\\")+"\\";
                    String url=baseUrl+path+targetClass+".java";
                    doCreateJavaCode(filer,info.getTag(),url,targetPkg,targetClass);
                }
                TypeElement element= (TypeElement) list.get(0);
                createRepositoryCode(elementUtils.getPackageOf(element).getQualifiedName().toString());
            }
        }
        return false;
    }

    private void createRepositoryCode(String pkg){
        try {
            TypeSpec.Builder tb=TypeSpec.classBuilder("Repository")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.bestGuess("com.sad.assistant.live.guardian.api.IRepository"))
                    .addSuperinterface(ClassName.bestGuess("com.sad.basic.utils.clazz.ClassScannerFilter"))
                    ;
            FieldSpec f_guardian=FieldSpec.builder(
                    ClassName.bestGuess("com.sad.assistant.live.guardian.api.IGuardian"),
                    "guardian",
                    Modifier.PRIVATE
            ).initializer("$T.newInstance()",ClassName.bestGuess("com.sad.assistant.live.guardian.impl.DefaultGuardian"))
            .build();

            MethodSpec m_constructor=MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PROTECTED)
                    .build();
            MethodSpec m_registerIn=MethodSpec.methodBuilder("registerIn")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.bestGuess("com.sad.assistant.live.guardian.api.IGuardian"))
                    .addParameter(ParameterSpec.builder(ClassName.bestGuess("android.content.Context"),"context").build())
                    .beginControlFlow("try")
                    .addStatement(" $T<String> clsNames=$T.with(context)\n" +
                            "                    .instantRunSupport(true)\n" +
                            "                    .build()\n" +
                            "                    .scan(\"com.sad.assistant.live.guardian.impl.delegate\",this);",

                            Set.class,
                            ClassName.bestGuess("com.sad.basic.utils.clazz.ClassScannerClient")

                    )
                    .endControlFlow()
                    .beginControlFlow("catch($T e)",Exception.class)
                    .addStatement("e.printStackTrace()")
                    .endControlFlow()
                    .addStatement("return guardian")
                    .build()
                    ;


            MethodSpec m_accept=MethodSpec.methodBuilder("accept")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(boolean.class)
                    .addAnnotation(Override.class)
                    .addParameter(ParameterSpec.builder(
                            ParameterizedTypeName.get(
                                    ClassName.get(Class.class), TypeVariableName.get("?")),
                            "cls"
                    ).build())
                    .addStatement("$T guardiaDelegate=cls.getAnnotation($T.class)", GuardiaDelegate.class,GuardiaDelegate.class)
                    .beginControlFlow("if(guardiaDelegate==null)")
                    .addStatement("return false")
                    .endControlFlow()
                    .addStatement("String name=guardiaDelegate.name()")
                    .addStatement("guardian.delegateStudio()\n" +
                            "                .put(name,cls)")
                    .addStatement("return true")
                    .build()
                    ;


            tb.addMethod(m_constructor)
                    .addField(f_guardian)
                    .addMethod(m_registerIn)
                    .addMethod(m_accept);

            JavaFile.Builder jb= JavaFile.builder(pkg,tb.build());
            jb.build().writeTo(filer);
        }catch (Exception e){

        }
    }

    private void doCreateJavaCode(
            Filer filer,
            int tag,
            String url,
            String targetPackgeName,
            String targetClassName
    ){
        SourceCodeFactoryImpl.newInstance()
                .tag(tag)
                .url(url)
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
