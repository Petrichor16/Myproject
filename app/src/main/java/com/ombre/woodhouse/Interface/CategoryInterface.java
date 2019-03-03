package com.ombre.woodhouse.Interface;

import com.ombre.woodhouse.Bean.Category;

import java.util.List;
import java.util.Map;

/**
 * Created by OMBRE on 2018/6/3.
 */

public interface CategoryInterface {
   void getFirstCategory(Callbacks callbacks);//获取第一级分类

    void getSecondCategory(Map<String,String> map,Callbacks callbacks);//获取第二级分类
}
