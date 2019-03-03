package com.ombre.woodhouse.InterfaceService;

import android.app.Activity;

import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.Interface.OrderInterface;
import com.ombre.woodhouse.Utils.URL_Address;

import java.util.Map;

/**
 * Created by OMBRE on 2018/6/13.
 */

public class OrderInterfaceService implements OrderInterface {
    OKHttpUtils okHttpUtils ;
    Activity activity;
    String url=null ;//访问的ip地址
    String urlHead=new URL_Address().urlHead();

    public OrderInterfaceService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void GenerationOrder(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/SubmitOrderServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void getOrderDetails(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/DetailsOrderServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void getOrderGoods(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/OrderGoodsServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void updateOrdeleteOrder(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/updateOrdeleteOrderServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void getStateOrder(Map<String, String> map, Callbacks callbacks) {
        okHttpUtils = new OKHttpUtils(activity);
        url = urlHead + "/LoadAllOrdersServlet";
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }
}
