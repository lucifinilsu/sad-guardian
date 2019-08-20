package com.sad.assistant.live.guardian.compiler;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.IOException;

public class SourceCodeFactoryImpl implements ISourceCodeFactory {

    private String url ="";
    private int tag=-1;

    protected SourceCodeFactoryImpl(){}

    protected static ISourceCodeFactory newInstance(){
        return new SourceCodeFactoryImpl();
    }

    @Override
    public ISourceCodeFactory url(String url) {
        this.url =url;
        return this;
    }

    @Override
    public ISourceCodeFactory tag(int tag) {
        this.tag=tag;
        return this;
    }

    @Override
    public IServerSourceCodeFuture  get(OnGetCodeSuccessListener successListener, OnGetCodeFailureListener failureListener) {
        //String url=baseUrl+ this.url;
        IServerSourceCodeFuture future=ServerSourceCodeFutureImpl.newInstance();
        try {
            Connection connection= Jsoup.connect(url);
            connection.method(Connection.Method.GET);
            connection.timeout(10000);
            connection.followRedirects(true);
            //connection.header("Accept","text/xml;charset=utf-8");
            connection.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
            Connection.Response response=connection.execute();
            int code=response.statusCode();
            String msg=response.statusMessage();
            String sourceCode=response.charset("GBK").body();//new String(response.charset("utf-8").body().getBytes(),"GBK");
            future.creator()
                    .statusCode(code)
                    .statusMsg(msg)
                    .successful(true)
                    .set(new ServerSourceCodeImpl(sourceCode))
                    .create();
            if (successListener!=null){
                successListener.onGetCode(future);
            }

        } catch (HttpStatusException e){
            e.printStackTrace();
            int code=e.getStatusCode();
            String msg=e.getMessage();
            future.creator()
                    .statusCode(code)
                    .statusMsg(msg)
                    .successful(false)
                    .throwable(e)
                    .create()
            ;
            if (failureListener!=null){
                failureListener.onHttpIOErr(tag,code,msg);
            }

        }catch (IOException ioe){
            future.creator()
                    .statusCode(OnGetCodeFailureListener.IO_ERR)
                    .statusMsg("")
                    .throwable(ioe)
                    .successful(false)
                    .create();
            if (failureListener!=null){
                failureListener.onIOErr(ioe);
            }

        }catch (Error err){
            future.creator()
                    .statusCode(OnGetCodeFailureListener.ERR)
                    .statusMsg("")
                    .throwable(err)
                    .successful(false)
                    .create();
            if (failureListener!=null){
                failureListener.onErr(err);
            }
        }

        return future;
    }
}
