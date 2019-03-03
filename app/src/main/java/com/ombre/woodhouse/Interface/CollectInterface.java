package com.ombre.woodhouse.Interface;

import java.util.Map;

/**
 * Created by OMBRE on 2018/6/11.
 */

public interface CollectInterface {
    //对商品进行收藏与取消收藏
    void CollectOperationtype(Map<String ,String> map,Callbacks callbacks);
    //加载所有收藏商品
    void LoadAllCollectGoods(Map<String ,String> map,Callbacks callbacks);
}
