package com.ombre.woodhouse.InterfaceService;

import android.app.Activity;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.Utils.URL_Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by OMBRE on 2018/5/30.
 */

public class GetPicture {
    Activity activity;
    ImageView  imageView;
    String url=null ;//访问的ip地址
    String urlHead=new URL_Address().urlPicHead();
    OKHttpUtils okHttpUtils ;
    String urlpathHead=new URL_Address().urlHead();

    public GetPicture() {
    }

    public GetPicture(Activity activity, ImageView imageView) {
        this.activity = activity;
        this.imageView = imageView;
    }
    //加载图片
    public void loadPicture(String picPath){
        url = urlHead + picPath;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap[] bitmap = new Bitmap[1];
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url)
                            .build();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    final byte[] bytes = response.body().bytes();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageView.setImageBitmap(bitmap[0]);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getPicPath(Map<String, String> map, Callbacks callbacks) {
        url= urlpathHead+"/GoodsPictureServlet";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    public List<String > picPath(String path){
        String [] temp;
        List<String> list=new ArrayList<>();
        String delimeter="-";
        temp=path.split(delimeter);
        for (int i=0;i< temp.length;i++)
        {
            list.add(temp[i]);
        }
        return list;
    }
 }
