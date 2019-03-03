package com.ombre.woodhouse.InterfaceService;

import android.app.Activity;

import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.Interface.CollectInterface;
import com.ombre.woodhouse.Utils.URL_Address;

import java.util.Map;

/**
 * Created by OMBRE on 2018/6/11.
 */

public class CollectInterfaceService implements CollectInterface {
    OKHttpUtils okHttpUtils ;
    Activity activity;
    String url=null ;//访问的ip地址
    String urlHead=new URL_Address().urlHead();

    public CollectInterfaceService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void CollectOperationtype(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/CollectServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void LoadAllCollectGoods(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/LoadCollectGoodsServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }
}
