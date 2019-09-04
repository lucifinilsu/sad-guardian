package com.sad.assistant.live.guardian.compiler;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

//            Connection connection= Jsoup.connect(url);
//            connection.method(Connection.Method.GET);
//            connection.timeout(10000);
//            connection.followRedirects(true);
//            connection.header("Accept","*/*"/*"text/xml;charset=gbk"*/);
//            connection.header("Accept-Encoding","gzip, deflate");
//            connection.ignoreContentType(true);
//            connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
//            Connection.Response response=connection.execute();
//            int code=response.statusCode();
//            String msg=response.statusMessage();
//            String sourceCode=response.charset("GBK").body();//new String(response.charset("utf-8").body().getBytes(),"GBK");




//            URL url = new URL(this.url);
//            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//            //默认就是Get，可以采用post，大小写都行，因为源码里都toUpperCase了。
//            connection.setRequestMethod("GET");
//            //是否允许缓存，默认true。
//            connection.setUseCaches(Boolean.FALSE);
//            //是否开启输出输入，如果是post使用true。默认是false
//            //connection.setDoOutput(Boolean.TRUE);
//            //connection.setDoInput(Boolean.TRUE);
//            //设置请求头信息
//            connection.addRequestProperty("Connection", "close");
//            //设置连接主机超时（单位：毫秒）
//            connection.setConnectTimeout(8000);
//            //设置从主机读取数据超时（单位：毫秒）
//            connection.setReadTimeout(8000);
//            //设置Cookie
//            //connection.addRequestProperty("Cookie","你的Cookies" );
//            String sourceCode=Jsoup.parse(connection.getInputStream(),"GBK",url.getAuthority()).text();


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    //.addHeader("Accept-Charset","gbk")
                    .build();

            Response response = client.newCall(request)
                    .execute();
            String msg=response.message();
            int code=response.code();

            /*response=response.newBuilder()
                    .header("Content-Type","text/plain; charset=GBK")
                    .build();*/
            ;
            //System.out.println(response.headers().toString());
            //ResponseBody newResponse=response.peekBody(1024*1024*10);
            if (response.isSuccessful()){
                //setBodyContentType(response,"GBK");
                byte[] bytes=response.body().bytes();
                String sourceCode =
                        //response.body().string();
                        new String(bytes, "utf-8");
                System.out.println("源码："+sourceCode);
                future.creator()
                        .statusCode(code)
                        .statusMsg(msg)
                        .successful(true)
                        .set(new ServerSourceCodeImpl(sourceCode))
                        .create();
                if (successListener!=null){
                    successListener.onGetCode(future);
                }
            }
            else {
                throw new HttpStatusException(msg,code,this.url);
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

    /**
     * set body contentType
     * @param response
     * @throws IOException
     */
    private void setBodyContentType(Response response,String encoding) throws IOException {
        ResponseBody body = response.body();
        // setting body contentTypeString using reflect
        Class<? extends ResponseBody> aClass = body.getClass();
        try {
            Field field = aClass.getDeclaredField("contentTypeString");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            String contentTypeString = String.valueOf(field.get(body));
            if (StringUtils.isNotBlank(contentTypeString) && contentTypeString.contains("charset")) {
                return;
            }
            field.set(body, (StringUtils.isNotBlank(contentTypeString) ? contentTypeString + "; ":"" ) + "charset=" + encoding);
        } catch (NoSuchFieldException e) {
            throw new IOException("use reflect to setting header occurred an error", e);
        } catch (IllegalAccessException e) {
            throw new IOException("use reflect to setting header occurred an error", e);
        }
    }
}
