package com.sad.assistant.live.guardian.compiler;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.GsonBuildConfig;
import com.sad.assistant.live.guardian.annotation.ImplModule;
import com.squareup.javapoet.CodeBlock;

import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.StandardLocation;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


@AutoService(Processor.class)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.sad.assistant.live.guardian.annotation.ImplModule"
})
public class RemoteJavaRegisterProcessor extends AbstractProcessor {

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;
    protected ProcessorLog log;

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
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)){
            Set<? extends Element> registerElements=roundEnvironment.getElementsAnnotatedWith(ImplModule.class);
            if (!CollectionUtils.isNotEmpty(registerElements)){
                log.error("未发现注解ImplModule");
                return true;
            }
            for (Element registerElement: registerElements
            ) {
                if (registerElement.getKind() != ElementKind.CLASS) {
                    log.error("错误的注解类型，只有【类】才能够被该 ImplModule 注解处理");
                    continue;
                }
                //编辑注册方法的代码块
                createRigestersXMLFile(registerElement);
            }

        }
        return false;
    }

    private void createRigestersXMLFile(Element registerElement) {

        String[] validImplGuardianId={"123abc"};
        String[] validImplPackage={"com.sad.assistant.live.guardian.impl"};
        List<String> v=new ArrayList<>(Arrays.asList(validImplPackage));
        List<String> vGuardianIds=new ArrayList<>(Arrays.asList(validImplGuardianId));
        String mn=elementUtils.getPackageOf(registerElement).getQualifiedName().toString();
        if (v.indexOf(mn)==-1){
            return;
        }
        String[] targets=registerElement.getAnnotation(ImplModule.class).targetApp();

        //log.info("开始生成注册表");
        for (String target:targets
             ) {
            try {
                Result registryInfo=getRegistryInfo(target);
                if (registryInfo.isSuccess()){
                    String appId=ValidUtils.encryptMD5ToString(target).toLowerCase();
                    String fileName=appId+".xml";
                    String outPath=filer.getResource(StandardLocation.SOURCE_OUTPUT,"",fileName).getName();
                    String[] paths=outPath.split("build");
                    if (paths.length>1){
                        String rootPath=paths[0];
                        String finalPath=rootPath+fileName;
                        //log.info("注册表位置--------->"+finalPath);
                        String implPath=rootPath+"src\\main\\java\\"+mn.replace(".","\\");
                        String xml=createXMLContent(appId,registryInfo,implPath,mn);
                        IOUtils.writeFileFromString(finalPath,xml);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private Result getRegistryInfo(String guardianId){
        Result result=new Result();
        try  {
            JsonObject json=new JsonObject();
            json.addProperty("guardianId",guardianId);
            String data=json.toString();
            RequestBody body=RequestBody.create(data, MediaType.get("application/json; charset=utf-8"));
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://47.104.182.251:8080/v1/verify")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String res= response.body().string();
                //log.info("---------------->json="+res);
                result=Result.fromJson(res);
                //暂时写死
                result.setDeadline(1571479939000l);
                if (result==null){
                    result=new Result();
                    result.setSuccess(false);
                }
            }
            else {
                result.setSuccess(false);
            }

        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    private String createXMLContent(String appId,Result registryInfo,String implPath,String implPackage){
        long deadline=registryInfo.getDeadline();
        String appPackage=registryInfo.getAppPackage();
        String baseHead="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
        String baseXML=baseHead+"<app></app>";
        Document document=Jsoup.parse(baseXML);
        org.jsoup.nodes.Element appElement=document.getElementsByTag("app").first();
        appElement.appendElement("appId").appendText(appId);
        appElement.appendElement("deadline").appendText(deadline+"");
        appElement.appendElement("appPackage").appendText(appPackage);
        org.jsoup.nodes.Element dataElement=appElement.appendElement("data");

        FileUtils.scanAllFilesDir(new File(implPath), new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (!file.isDirectory()){
                    String ex="_ImplModuleFlag.java";
                    String fn=file.getAbsolutePath();
                    return (fn.endsWith(".java") && !file.getName().equals(ex));
                }
                else {
                    return true;
                }
            }
        }, new IFileScanedCallback() {
            long tag=0;
            @Override
            public void onScaned(File f) {
                tag=tag+1;
                String baseUrl="https://raw.githubusercontent.com/lucifinilsu/sad-guardian/dev/impl/src/main/java/";
                String fileName=f.getName().replace(".java","");
                org.jsoup.nodes.Element javaCodeElement=dataElement.appendElement("javaCode");
                javaCodeElement.appendElement("tag").appendText(tag+"");
                javaCodeElement.appendElement("baseUrl").appendText(baseUrl);
                File dir=f.getParentFile();
                String p=FileUtils.extractPackageName(dir);
                javaCodeElement.appendElement("package").appendText(p);
                javaCodeElement.appendElement("class").appendText(fileName);
            }
        });
        return baseHead+document.body().html();
    }

    private static class Result{

        static Result fromJson(String json){
            Gson gson=new Gson();
            Result result=gson.fromJson(json,Result.class);
            if (result!=null){
                result.setOrgJsonString(json);
            }
            return result;
        }

        private boolean isSuccess=false;
        private long deadline = 1571479939000l;
        private String appPackage = "";
        private transient String orgJsonString="";

        public String getOrgJsonString() {
            return orgJsonString;
        }

        public void setOrgJsonString(String orgJsonString) {
            this.orgJsonString = orgJsonString;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public long getDeadline() {
            return deadline;
        }

        public void setDeadline(long deadline) {
            this.deadline = deadline;
        }

        public String getAppPackage() {
            return appPackage;
        }

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }
    }

}
