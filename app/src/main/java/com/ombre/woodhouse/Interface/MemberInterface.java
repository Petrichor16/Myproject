package com.ombre.woodhouse.Interface;

import com.ombre.woodhouse.Bean.Member;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by OMBRE on 2018/5/24.
 */

public interface MemberInterface {
    public void receiveLoginRequest( Map<String,String> map,Callbacks callbacks);//接收登录请求返回的数据
    public void receiveSelectMemberRequest( Map<String,String> map,Callbacks callbacks);//接收查询指定用户信息
    public void findpassword( Map<String,String> map,Callbacks callbacks);//找回密码
    public void registerusers( Map<String,String> map,Callbacks callbacks) ;//用户注册
    void updateMemberInfo(Map<String,String> map,Callbacks callbacks);//修改个人信息
}
