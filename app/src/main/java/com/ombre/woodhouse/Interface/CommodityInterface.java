package com.ombre.woodhouse.Interface;

import com.ombre.woodhouse.Bean.Commodity;

import java.util.List;
import java.util.Map;

/**
 * Created by OMBRE on 2018/5/30.
 */

public interface CommodityInterface {
    //获取所有商品列表
    void  getAllCommodity(Map<String,String > map,Callbacks callbacks);
    //获取商品信息
    void  getCommdityDetails(Map<String,String > map,Callbacks callbacks);
    /*获取所有商品*/
    void getFirspageCommodity(Map<String,String > map,Callbacks callbacks);
    /*获取购物车中的选购数量*/
    void getCartnumber(Map<String,String > map,Callbacks callbacks);
    /*获取所有浏览记录的商品*/
    void getFootprintGoods(Map<String,String > map,Callbacks callbacks);
    /*获取所有符合关键字的搜索商品*/
    void getSearchGoods(Map<String,String > map,Callbacks callbacks);
}
