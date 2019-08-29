package com.sad.assistant.live.guardian.compiler;

import com.google.auto.service.AutoService;
import com.sad.assistant.live.guardian.annotation.AppLiveGuardian;
import com.sad.assistant.live.guardian.annotation.GuardiaWorker;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.sad.assistant.live.guardian.annotation.GuardiaWorker"
})
public class GuardiaWorkerProcessor extends AbstractProcessor {

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;
    protected ProcessorLog log;
    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
        processingEnv=env;
        filer = env.getFiler();
        typeUtils = env.getTypeUtils();
        messager = env.getMessager();
        log=new ProcessorLog(messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment){
        if (CollectionUtils.isNotEmpty(set)){
            Set<? extends Element> workerElements=roundEnvironment.getElementsAnnotatedWith(GuardiaWorker.class);
            if (!CollectionUtils.isNotEmpty(workerElements)){
                log.error("未发现注解GuardiaWorker");
                return true;
            }
            for (Element workerElement: workerElements
                 ) {
                if (workerElement.getKind() != ElementKind.CLASS) {
                    log.error("错误的注解类型，只有【类】才能够被该 GuardiaWorker 注解处理");
                    continue;
                }
            }

        }

        return false;
    }

}
