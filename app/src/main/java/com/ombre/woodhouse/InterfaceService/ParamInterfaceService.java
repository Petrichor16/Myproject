package com.ombre.woodhouse.InterfaceService;

import android.app.Activity;

import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.Interface.ParamInterface;
import com.ombre.woodhouse.Utils.URL_Address;

import java.util.Map;

/**
 * Created by OMBRE on 2018/6/8.
 */

public class ParamInterfaceService implements ParamInterface {
    OKHttpUtils okHttpUtils ;
    Activity activity;
    String url=null ;//访问的ip地址
    String urlHead=new URL_Address().urlHead();

    public ParamInterfaceService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void getParam(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/GoodParamServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }
}
