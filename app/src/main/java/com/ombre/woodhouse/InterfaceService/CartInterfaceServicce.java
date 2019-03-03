package com.ombre.woodhouse.InterfaceService;

import android.app.Activity;

import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.Interface.CartInterface;
import com.ombre.woodhouse.Utils.URL_Address;
import java.util.Map;

/**
 * Created by OMBRE on 2018/6/8.
 */

public class CartInterfaceServicce implements CartInterface {
    OKHttpUtils okHttpUtils ;
    Activity activity;
    String url=null ;//访问的ip地址
    String urlHead=new URL_Address().urlHead();

    public CartInterfaceServicce(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void getCart(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/CartServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }
    @Override
    public void updateChecked(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/updateCartServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }
    @Override
    public void addCart(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/addCartServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void deleteCart(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/deleteCartServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

}
