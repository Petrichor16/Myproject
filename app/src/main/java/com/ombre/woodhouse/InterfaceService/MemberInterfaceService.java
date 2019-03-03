package com.ombre.woodhouse.InterfaceService;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ombre.woodhouse.Bean.Member;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.Interface.MemberInterface;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.Utils.URL_Address;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by OMBRE on 2018/5/24.
 */

public class MemberInterfaceService implements MemberInterface {
    OKHttpUtils okHttpUtils ;
    Activity activity;
    String url=null ;//访问的ip地址
    String urlHead=new URL_Address().urlHead();

    public MemberInterfaceService(Activity activity) {
        this.activity=activity;
    }

    @Override
    public void  receiveLoginRequest( Map<String, String> map,Callbacks callbacks) {
            okHttpUtils = new OKHttpUtils(activity);
            url = urlHead + "/Users_Login";
            okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void receiveSelectMemberRequest( Map<String, String> map,Callbacks callbacks) {
        url = urlHead + "/SelectMember";
        okHttpUtils = new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map, callbacks);
    }

    @Override
    public void  findpassword( Map<String, String> map,Callbacks callbacks) {
        url= urlHead+"/Forget_Password";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void registerusers( Map<String, String> map,Callbacks callbacks) {
        url= urlHead+"/Users_Register";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }

    @Override
    public void updateMemberInfo(Map<String, String> map, Callbacks callbacks) {
        url= urlHead+"/UpdateMemberServlet";
        okHttpUtils=new OKHttpUtils(activity);
        okHttpUtils.getInstance(activity).MyOKHttp(url, map,callbacks);
    }


}
