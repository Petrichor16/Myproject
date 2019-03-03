package com.ombre.woodhouse.Helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by OMBRE on 2018/5/22.
 */

public class ReplaceFragfmentHelper {
    AppCompatActivity activity;
    public ReplaceFragfmentHelper( AppCompatActivity activity) {
        this.activity = activity;
    }

    //碎片切换
    public void replaceFragment(int id,Fragment fragment){
        //处理碎片，显示、移除等
        //这里要用碎片的实物事物完成
        FragmentManager fragmentManager=activity.getSupportFragmentManager();//创建添加碎片实例
        FragmentTransaction transaction=fragmentManager.beginTransaction();//调用beginTransaction()开启事务
        transaction.replace(id,fragment);
        transaction.commit();//提交事务
    }

}
