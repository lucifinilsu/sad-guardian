package com.sad.assistant.live.guardian.compiler;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AuthenticationMaster {
    protected final static int ACTION_TAG_AUTHENTICATION=0;
    static AuthenticationFuture start(
            String guardianId,
            String p,
            OnCompiledAuthenticationSuccessCallback successCallback,
            OnCompiledAuthenticationFailureCallback failureCallback,
            ProcessorLog log
    ){
        AuthenticationFuture future=new AuthenticationFuture();
        try {
            Connection connection=
                    Jsoup.connect(
                            "https://raw.githubusercontent.com/lucifinilsu/sad-guardian/dev/impl/"+ValidUtils.encryptMD5ToString(guardianId).toLowerCase()+".xml"
                    );
                    //Jsoup.connect("https://raw.githubusercontent.com/lucifinilsu/SmartAndroidDeveloper/master/"+ValidUtils.encryptMD5ToString(guardianId).toLowerCase()+".xml");
            connection.method(Connection.Method.GET);
            connection.timeout(10000);
            connection.followRedirects(true);
            Connection.Response response=connection.execute();
            int code=response.statusCode();
            String msg=response.statusMessage();
            if (code>=200 && code<300){
                Document doc = response.parse();
                Element elementRoot=doc.getElementsByTag("app").first();
                String id=elementRoot.getElementsByTag("appid").first().text();
                String deadline=elementRoot.getElementsByTag("deadline").first().text();
                String pkg=elementRoot.getElementsByTag("apppackage").first().text();
                //开始鉴权
                if (!p.equals(pkg)){
                    AuthenticationFailureInfo failureInfo=new AuthenticationFailureInfo();
                    failureInfo.setCode(AuthenticationFailureInfo.INVALID_PACKAGE_NAME);
                    failureInfo.setMsg("您的保活引擎SDK的开发者授权失败：App包名无效");
                    if (failureCallback!=null){
                        failureCallback.onInvalid(failureInfo);
                    }
                    log.info("------------->鉴权成功");
                    future.set(failureInfo);
                    future.setSuccessful(false);
                }
                else {
                    long curr=System.currentTimeMillis();
                    long dead=Long.parseLong(deadline);
                    if (dead<curr){
                        AuthenticationFailureInfo failureInfo=new AuthenticationFailureInfo();
                        failureInfo.setCode(AuthenticationFailureInfo.AGENCIES_HAVE_EXPIRED);
                        failureInfo.setMsg("您的保活引擎SDK的开发者授权已经过期，请联系SDK提供方。");
                        if (failureCallback!=null){
                            failureCallback.onInvalid(failureInfo);
                        }

                        future.set(failureInfo);
                        future.setSuccessful(false);
                    }
                    else {
                        AuthenticationSuccessInfo successInfo=new AuthenticationSuccessInfo();
                        successInfo.setAppId(id);
                        successInfo.setDeadline(Long.parseLong(deadline));
                        successInfo.setAppPackage(pkg);
                        List<SourceCodeInfo> sourceCodeInfos=new ArrayList<>();
                        Element data=elementRoot.getElementsByTag("data").first();
                        Elements javaCodes=data.getElementsByTag("javaCode");
                        Iterator it = javaCodes.iterator();
                        while (it.hasNext()){
                            Element javaCode = (Element)it.next();
                            SourceCodeInfo sourceCodeInfo=new SourceCodeInfo();
                            sourceCodeInfo.setBaseUrl(javaCode.getElementsByTag("baseUrl").first().text());
                            sourceCodeInfo.setClassName(javaCode.getElementsByTag("class").first().text());
                            sourceCodeInfo.setPackageName(javaCode.getElementsByTag("package").first().text());
                            sourceCodeInfo.setTag(Integer.parseInt(javaCode.getElementsByTag("tag").first().text()));
                            sourceCodeInfos.add(sourceCodeInfo);
                        }
                        successInfo.setData(sourceCodeInfos);
                        if (successCallback!=null){
                            successCallback.onDone(successInfo);
                        }
                        future.setSuccessful(true);
                        future.set(successInfo);
                    }
                }

            }
            else {

                if (failureCallback!=null){
                    failureCallback.onHttpIOErr(ACTION_TAG_AUTHENTICATION,code,msg);
                }
                AuthenticationFailureInfo failureInfo=new AuthenticationFailureInfo();
                failureInfo.setCode(code);
                failureInfo.setMsg(msg);
                future.set(failureInfo);
                future.setSuccessful(false);
            }

        } catch (HttpStatusException e) {
            AuthenticationFailureInfo failureInfo=new AuthenticationFailureInfo();
            if (failureCallback!=null){
                failureCallback.onHttpIOErr(ACTION_TAG_AUTHENTICATION,e.getStatusCode(),e.getMessage());
            }
            failureInfo.setCode(e.getStatusCode());
            failureInfo.setMsg(e.getMessage());
            failureInfo.setThrowable(e);
            future.set(failureInfo);
            future.setSuccessful(false);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            AuthenticationFailureInfo failureInfo=new AuthenticationFailureInfo();
            failureInfo.setCode(AuthenticationFailureInfo.PARSE_ERR);
            failureInfo.setMsg(ioe.getMessage());
            failureInfo.setThrowable(ioe);
            if (failureCallback!=null){
                failureCallback.onParseResourceErr(failureInfo);
            }

            future.set(failureInfo);
            future.setSuccessful(false);
        }
        catch (Error err){
            err.printStackTrace();
            AuthenticationFailureInfo failureInfo=new AuthenticationFailureInfo();
            failureInfo.setCode(AuthenticationFailureInfo.PARSE_ERR);
            failureInfo.setMsg(err.getMessage());
            failureInfo.setThrowable(err);
            if (failureCallback!=null){
                failureCallback.onParseResourceErr(failureInfo);
            }
            future.set(failureInfo);
            future.setSuccessful(false);
        }
        return future;
    }

}
