package com.ombre.woodhouse.Interface;

import java.util.Map;

/**
 * Created by OMBRE on 2018/6/8.
 */
//参数接口
public interface EvaluetionInterface {
    //获取商品参数
    void getEvaluation(Map<String, String> map, Callbacks callbacks);
    //提交评价
    void submitEva(Map<String, String> map, Callbacks callbacks);
}
