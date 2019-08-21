package com.sad.assistant.live.guardian.impl.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.DeleteTokenHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.sad.assistant.datastore.sharedPreferences.SharedPerferencesClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PushTokenManager {

    private final static String uploadTokenUrl="http://push.anywn.xin/index.php/Index/Index/save_token?token=%s";
    private final static String deleteTokenUrl="http://push.anywn.xin/index.php/index/Index/delete_token?token=%s";
    private final static String SAD_KEEPLIVE_LOCAL="SAD_KEEPLIVE_LOCAL";
    private final static String SAD_KEEPLIVE_LOCAL_TOKEN=SAD_KEEPLIVE_LOCAL+"_TOKEN";
    public static void saveTokenToLocal(Context context, String token){
        SharedPerferencesClient.with(context)
                .initValueWhenReadError(true)
                .name(SAD_KEEPLIVE_LOCAL)
                .build()
                .put(SAD_KEEPLIVE_LOCAL_TOKEN,token).end();
    }

    public static String readTokenToLocal(Context context){
        return SharedPerferencesClient.with(context)
                .initValueWhenReadError(true)
                .name(SAD_KEEPLIVE_LOCAL)
                .build()
                .get(SAD_KEEPLIVE_LOCAL_TOKEN,"");
    }

    public static void refreshToken(Context context,boolean isRemoveFromHWSDK, DeleteTokenCallback deleteCallback){
        String old=readTokenToLocal(context);
        SharedPerferencesClient.with(context)
                .name(SAD_KEEPLIVE_LOCAL)
                .build()
                .remove(SAD_KEEPLIVE_LOCAL_TOKEN)
                .end();

        if (isRemoveFromHWSDK && !TextUtils.isEmpty(old)){
            if (deleteCallback!=null){
                deleteCallback.setOldToken(old);
            }
            HMSAgent.Push.deleteToken(old, deleteCallback);
        }
    }


    public static void updateToken(GetTokenHandler callback){
        HMSAgent.Push.getToken(callback);
    }

    private static void opToken(String url_template, String token, String des){
        //Log.e("keeplive","------------------------->token"+des+"开始："+token);
        OkHttpClient client = new OkHttpClient();
        String url= String.format(url_template,token);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request)
                .enqueue(new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        //Log.e("keeplive","------------------------->token"+des+"异常："+token);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()){
                            ResponseBody responseBody=response.body();
                            if (responseBody!=null){
                                String j=responseBody.string();
                                try {
                                    JSONObject jsonObject=new JSONObject(j);
                                    int status=jsonObject.optInt("code",0);
                                    if (status==1){
                                        //Log.e("keeplive","------------------------->token"+des+"完成："+token);
                                    }
                                    else {
                                        //Log.e("keeplive","------------------------->token"+des+"失败，"+j+"---------："+token);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    public static void uploadTokenToServer(String token)  {
        opToken(uploadTokenUrl,token,"上传");
    }

    public static void deleteTokenToServer(String token) {
        opToken(deleteTokenUrl,token,"删除");
    }

    public static abstract class DeleteTokenCallback implements DeleteTokenHandler {

        private String oldToken="";

        public String getOldToken() {
            return oldToken;
        }

        public void setOldToken(String oldToken) {
            this.oldToken = oldToken;
        }
    }

}
