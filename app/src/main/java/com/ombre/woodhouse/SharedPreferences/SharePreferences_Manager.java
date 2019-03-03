package com.ombre.woodhouse.SharedPreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.ombre.woodhouse.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by OMBRE on 2017/12/14.
 */

public class SharePreferences_Manager {
    private Activity activity;

    public SharePreferences_Manager(Activity activity) {
        this.activity = activity;
    }


    //将数据存入SharedPreferences文件中,使得下次进入系统是保持系统推出前的状态
    public void saveUserID(String userid){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("pre_userID",activity.MODE_PRIVATE).edit();
        editor.putString("userID",userid);
        editor.apply();//将数据提交
    }
    //存储密码或者验证码
    public void savePW_VER(String pw_ver){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("pref_pw_ver",activity.MODE_PRIVATE).edit();
        editor.putString("pw_ver",pw_ver);
        editor.apply();//将数据提交
    }

    //保存用户的选择登录方式
    public  void saveLoginWay(String login_way){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("LoginWay",activity.MODE_PRIVATE).edit();
        editor.putString("login_way",login_way);
        editor.apply();//将数据提交
    }
    //保存登录状态
    public void saveLoginState(boolean login_state){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("LoginState",activity.MODE_PRIVATE).edit();
        editor.putString("login_state", String.valueOf(login_state));
        editor.apply();//将数据提交
    }
    //保存一级分类名
    public void saveFirstClassification(String firstClassification){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("First_Classification",activity.MODE_PRIVATE).edit();
        editor.putString("firstClassification", String.valueOf(firstClassification));
        editor.apply();//将数据提交
    }
    //保存二级分类名
    public void saveSecondClassification(String secondClassification){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("Second_Classification",activity.MODE_PRIVATE).edit();
        editor.putString("secondClassification", String.valueOf(secondClassification));
        editor.apply();//将数据提交
    }
    public void savePicPath(String  picPath){

        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("PicturePath",activity.MODE_PRIVATE).edit();
        editor.putString("picturePath", picPath);
        editor.apply();//将数据提交
    }
    //保存分类父类ID，用以查询二级分类
    public void saveClassificationID(int id){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("ClassID",activity.MODE_PRIVATE).edit();
        editor.putString("classID", String.valueOf(id));
        editor.apply();//将数据提交
    }

    //保存中间传值标记
    public void saveFlag(int id){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("Flag",activity.MODE_PRIVATE).edit();
        editor.putString("flag", String.valueOf(id));
        editor.apply();//将数据提交
    }


    //排序方式
    public void saveSortType(String sortType){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("SortType",activity.MODE_PRIVATE).edit();
        editor.putString("sortType", sortType);
        editor.apply();//将数据提交
    }

    //存储地址
    public void saveAddressID(String sortType){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("AddressID",activity.MODE_PRIVATE).edit();
        editor.putString("addressID", sortType);
        editor.apply();//将数据提交
    }
    //存储启动次数
    public void saveStartTimes(int times){
        SharedPreferences.Editor editor;
        editor=activity.getSharedPreferences("StartTimes",activity.MODE_PRIVATE).edit();
        editor.putString("startTimes", String.valueOf(times));
        editor.apply();//将数据提交
    }

    //取出SharedPreferences中的数据
    public SharedPreferences getData(String pathName){
        SharedPreferences pref;//创建一个键值对存储对象
        pref= activity.getSharedPreferences(pathName,activity.MODE_PRIVATE);//获取SharedPreferences对象
       return  pref;
    }
}
