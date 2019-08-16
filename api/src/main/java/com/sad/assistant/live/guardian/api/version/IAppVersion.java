package com.sad.assistant.live.guardian.api.version;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.Serializable;

public interface IAppVersion<I extends IAppVersion<I>> extends Serializable {

     default I fromJson(JSONObject jsonObject){
         return code(jsonObject.optLong("code",-1))
                 .name(jsonObject.optString("name",""))
                 .des(jsonObject.optString("des",""))
                 ;
     };

     default I fromJsonString(String jsonString){
         try {
             return fromJson(new JSONObject(jsonString));
         } catch (Exception e) {
             e.printStackTrace();
         }
         return (I) IAppVersion.this;
     }

     Api api();

     I code(long code);

     I des(@NonNull String des);

     I name(@NonNull String name);

     interface Api extends Serializable {

         default String toJsonString(){
             JSONObject jsonObject = new JSONObject();
             try {
                 jsonObject.put("name",getName());
                 jsonObject.put("code",getCode());
                 jsonObject.put("des",getDes());
             } catch (Exception e) {
                 e.printStackTrace();
             }
             return jsonObject.toString();
         }

         @NonNull
         String getName();

         @NonNull
         String getDes();

         long getCode();
     }

}
