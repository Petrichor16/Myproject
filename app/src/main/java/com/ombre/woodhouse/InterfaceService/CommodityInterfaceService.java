package com.ombre.woodhouse.InterfaceService;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Member;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.Interface.CommodityInterface;
import com.ombre.woodhouse.Utils.URL_Address;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by OMBRE on 2018/5/30.
 */

public class CommodityInterfaceService implements CommodityInterface {
    OKHttpUtils okHttpUtils ;
    Activity activity;
    String url=null ;//访问的ip地址
    String urlHead=new URL_Address().urlHead();


    public CommodityInterfaceService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void getAllCommodity(Map<String,String > map,Callbacks callbacks) {
        url= urlHead+"/BrowsingAllGoods";
        okHttpUtils=new OKHttpUtils(activity);
      okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }
    @Override
    public void getFirspageCommodity(Map<String,String > map,Callbacks callbacks) {
        map=new HashMap<>();
        url= urlHead+"/FirstPageGoods";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void getCartnumber(Map<String,String> map,Callbacks callbacks) {
        url= urlHead+"/GetCartgoodsNumberServlet";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void getFootprintGoods(Map<String, String> map, Callbacks callbacks) {
        url= urlHead+"/FootprintGoodsServlet";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void getSearchGoods(Map<String, String> map, Callbacks callbacks) {
        url= urlHead+"/SearchGoodsServlet";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }


    @Override
    public void getCommdityDetails(Map<String, String> map, Callbacks callbacks) {
        url= urlHead+"/GoodsDetailsServlet";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }



}
