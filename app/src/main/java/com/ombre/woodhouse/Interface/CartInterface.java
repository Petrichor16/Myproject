package com.ombre.woodhouse.Interface;

import java.util.Map;

/**
 * Created by OMBRE on 2018/6/8.
 */

public interface CartInterface {
    //获取所有购物车信息
    void getCart(Map<String,String > map,Callbacks callbacks);
    //修改选项框状态
   void updateChecked(Map<String, String> map, Callbacks callbacks);
    //加入购物车
    void addCart(Map<String, String> map, Callbacks callbacks);
    //删除购物车
    void deleteCart(Map<String, String> map, Callbacks callbacks);
}
