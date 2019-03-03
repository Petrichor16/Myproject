package com.ombre.woodhouse.SharedPreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;

/**
 * Created by OMBRE on 2018/5/2.
 */

public class SharePreferences_getData {
    Activity activity;
    SharedPreferences pref;
    public SharePreferences_getData(Activity activity) {
        this.activity=activity;
    }

    //获取用户id
    public String  getUserID(){
        pref = new SharePreferences_Manager(activity).getData("pre_userID");
        return pref.getString("userID","");
    }

    //获取密码或验证码
    public String  getPW_VER(){
        pref= new SharePreferences_Manager(activity).getData("pref_pw_ver");
        return pref.getString("pw_ver","");
    }

    //获取选择的登录方式
    public  String getLoginWay(){
        pref= new SharePreferences_Manager(activity).getData("LoginWay");
        return pref.getString("login_way","");
    }
    //获取一级分类名
    public  String getFirstClassification(){
        pref= new SharePreferences_Manager(activity).getData("First_Classification");
        return pref.getString("firstClassification","");
    }
    //获取二级分类名
    public  String getSecondClassification(){
        pref= new SharePreferences_Manager(activity).getData("Second_Classification");
        return pref.getString("secondClassification","");
    }
    //获取图片地址
    public String getPicPath(){
        pref= new SharePreferences_Manager(activity).getData("PicturePath");
        return pref.getString("picturePath","");
    }
    public  String getClassificationID(){
        pref= new SharePreferences_Manager(activity).getData("ClassID");
        return pref.getString("classID","");
    }

    public  int getFlag(){
        pref= new SharePreferences_Manager(activity).getData("Flag");
        return Integer.parseInt(pref.getString("flag",""));
    }
    //获取排序方式
    public  String getSortType(){
        pref= new SharePreferences_Manager(activity).getData("SortType");
        return pref.getString("sortType","");
    }

    //获取地址id
    public  String getAddressID(){
        pref= new SharePreferences_Manager(activity).getData("AddressID");
        return pref.getString("addressID","");
    }
    //获取启动次数
    public  int  getStartTimews(){
        pref= new SharePreferences_Manager(activity).getData("StartTimes");
        if(pref.getString("startTimes","").equals(""))
            return 0;
        else
            return Integer.parseInt(pref.getString("startTimes",""));
    }
    //获取登录状态
    public  boolean getLoginState() {

        pref = new SharePreferences_Manager(activity).getData("LoginState");
        if (pref.getString("login_state", "") != null) {
            return Boolean.parseBoolean(pref.getString("login_state", ""));
        } else {
            return  false;
        }
    }
}
