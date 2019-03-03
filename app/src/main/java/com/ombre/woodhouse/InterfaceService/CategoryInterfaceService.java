package com.ombre.woodhouse.InterfaceService;

import android.app.Activity;

import com.ombre.woodhouse.Bean.Category;
import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.Interface.CategoryInterface;
import com.ombre.woodhouse.Utils.URL_Address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by OMBRE on 2018/6/3.
 */

public class CategoryInterfaceService implements CategoryInterface {

    OKHttpUtils okHttpUtils ;
    Activity activity;
    String url=null ;//访问的ip地址
    String urlHead=new URL_Address().urlHead();

    public CategoryInterfaceService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void getFirstCategory(Callbacks callbacks) {
        Map<String,String> map=new HashMap<>();
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/FirstClassification_Servlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void getSecondCategory(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/SecondClassification_Servlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }
}
