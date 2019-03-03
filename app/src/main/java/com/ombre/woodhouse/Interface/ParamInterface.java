package com.ombre.woodhouse.Interface;

import java.util.Map;

/**
 * Created by OMBRE on 2018/6/8.
 */
//参数接口
public interface ParamInterface {
    //获取商品参数
    void getParam(Map<String,String> map,Callbacks callbacks);
}
