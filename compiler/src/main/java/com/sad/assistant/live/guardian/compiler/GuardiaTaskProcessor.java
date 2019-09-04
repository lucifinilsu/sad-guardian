package com.sad.assistant.live.guardian.compiler;

import com.google.auto.service.AutoService;
import com.sad.assistant.live.guardian.annotation.GuardiaTask;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;

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
@SupportedOptions({"AppPackageName","ModuleName","AppGurdianId"})
@SupportedAnnotationTypes({
        "com.sad.assistant.live.guardian.annotation.GuardiaTask"
})
public class GuardiaTaskProcessor extends AbstractProcessor {

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;
    protected ProcessorLog log;

    protected String mainPkgName =null;
    protected String gurdianId=null;
    protected String moduleName=null;

    private CodeBlock codeBlockRegisterClass;
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
        moduleName=env.getOptions().get("ModuleName");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment){
        if (CollectionUtils.isNotEmpty(set)){
            Set<? extends Element> workerElements=roundEnvironment.getElementsAnnotatedWith(GuardiaTask.class);
            if (!CollectionUtils.isNotEmpty(workerElements)){
                log.error("未发现注解GuardiaTask");
                return true;
            }
            if (log.config_err(mainPkgName,moduleName,gurdianId)){
                return true;
            }
            for (Element workerElement: workerElements
                 ) {
                if (workerElement.getKind() != ElementKind.CLASS) {
                    log.error("错误的注解类型，只有【类】才能够被该 GuardiaTask 注解处理");
                    continue;
                }
                Set<Modifier> mod=workerElement.getModifiers();
                if (mod.contains(Modifier.ABSTRACT)){
                    String note=workerElement.getSimpleName().toString()+"是抽象类，故无法进行注册:";
                    log.error(note);
                    continue;
                }
                Element elementIAC=elementUtils.getTypeElement("com.sad.assistant.live.guardian.api.parameters.IGuardiaTask");
                if (!typeUtils.isSubtype(typeUtils.erasure(workerElement.asType()),typeUtils.erasure(elementIAC.asType()))){
                    String note="请注意GuardiaTask仅可以注解IGuardiaTask的子类";
                    log.error(note);
                    return true;
                }
                //编辑注册方法的代码块
                generateRigesterCodeBlock(workerElement);
            }

        }
        if (roundEnvironment.processingOver()){
            //结束解析，生成注册类
            generateRigester();
        }
        return false;
    }

    private void generateRigesterCodeBlock(Element workerElement) {

        GuardiaTask task =workerElement.getAnnotation(GuardiaTask.class);
        int tag=task.tag();
        if (codeBlockRegisterClass==null){
            codeBlockRegisterClass =CodeBlock.builder().build();
        }
        codeBlockRegisterClass = codeBlockRegisterClass.toBuilder()
                .addStatement("guardian.guardiaStudio().put("+tag+",$T.class)",
                        workerElement.asType()
                        )
                .build()
        ;

    }

    private void generateRigester() {

        try {
            TypeSpec.Builder tb=TypeSpec.classBuilder("GuardiaTaskRegister$$"+moduleName)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.bestGuess("com.sad.assistant.live.guardian.api.IGuardiaTaskRegister"))
                    ;
            //ConcurrentHashMap<String, IAppComponent> AppComponents
            MethodSpec.Builder mb_RegisterIn=MethodSpec.methodBuilder("registerIn")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(ClassName.bestGuess("com.sad.assistant.live.guardian.api.IGuardian"),"guardian").build())
                    .addCode(codeBlockRegisterClass)
                    ;
            tb.addMethod(mb_RegisterIn.build());
            JavaFile.Builder jb= JavaFile.builder("com.sad.assistant.live.guardian.api.task",tb.build());
            jb.build().writeTo(filer);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
