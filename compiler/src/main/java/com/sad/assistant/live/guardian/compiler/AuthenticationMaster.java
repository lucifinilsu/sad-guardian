package com.sad.assistant.live.guardian.compiler;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class AuthenticationMaster {
    protected final static int ACTION_TAG_AUTHENTICATION=0;
    static AuthenticationFuture start(
            String appPkg,
            OnCompiledAuthenticationSuccessCallback successCallback,
            OnCompiledAuthenticationFailureCallback failureCallback,
            ProcessorLog log
    ){
        AuthenticationFuture future=new AuthenticationFuture();
        try {
            Connection connection= Jsoup.connect("https://raw.githubusercontent.com/lucifinilsu/SmartAndroidDeveloper/master/"+ValidUtils.encryptMD5ToString(appPkg).toLowerCase()+".xml");
            connection.method(Connection.Method.GET);
            connection.timeout(10000);
            connection.followRedirects(true);
            Connection.Response response=connection.execute();
            int code=response.statusCode();
            String msg=response.statusMessage();
            if (code>=200 && code<300){
                Document doc = response.parse();
                Element elementRoot=doc.getElementsByTag("app").first();
                String id=elementRoot.getElementsByTag("appId").first().text();
                String deadline=elementRoot.getElementsByTag("deadline").first().text();
                //开始鉴权
                long curr=System.currentTimeMillis();
                long dead=Long.parseLong(deadline);
                if (dead<curr){
                    AuthenticationFailureInfo failureInfo=new AuthenticationFailureInfo();
                    failureInfo.setCode(AuthenticationFailureInfo.AGENCIES_HAVE_EXPIRED);
                    failureInfo.setMsg("您的保活引擎SDK的开发者授权已经过期");
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
                    if (successCallback!=null){
                        successCallback.onDone(successInfo);
                    }
                    future.setSuccessful(true);
                    future.set(successInfo);
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
