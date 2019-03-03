package com.ombre.woodhouse.Http;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//建立okhttp链接
public class OKHttpUtils {

    private Activity activity;
    public static  OKHttpUtils okhttpInstance;
    public OKHttpUtils( Activity activity) {
        this.activity = activity;
    }

    public static OKHttpUtils getInstance(Activity activity){
        if(okhttpInstance==null){
            synchronized (OKHttpUtils.class){
                if(okhttpInstance==null){
                    okhttpInstance=new OKHttpUtils(activity);
                }
            }
        }
        return okhttpInstance;
    }
    public void MyOKHttp(String url, Map<String,String> map, final Callbacks callbacks) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        if(!map.isEmpty()) {
            for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                builder.add(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }
        RequestBody body = builder.build();
        final Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              if(callbacks!=null)
                callbacks.onFailure(call,e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(callbacks!=null)
                callbacks.onResponse(call,response);
            }
        });
    }
}
