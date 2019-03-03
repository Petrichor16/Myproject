package com.ombre.woodhouse.Interface;

import java.util.Map;

/**
 * Created by OMBRE on 2018/6/13.
 */
//订单操作接口
public interface OrderInterface {
    /*s生成订单*/
    void GenerationOrder(Map<String,String> map,Callbacks callbacks);
    /*获取订单详情*/
    void getOrderDetails(Map<String,String> map,Callbacks callbacks);
    /*获取订单中的商品*/
    void getOrderGoods(Map<String,String > map,Callbacks callbacks);
    /*修改订单状态或者取消订单*/
    void updateOrdeleteOrder(Map<String,String > map,Callbacks callbacks);
    /*按订单状态获取订单*/
    void getStateOrder(Map<String,String > map,Callbacks callbacks);
}
