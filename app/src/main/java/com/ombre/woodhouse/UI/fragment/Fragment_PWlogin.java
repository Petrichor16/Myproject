package com.ombre.woodhouse.UI.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.MemberInterfaceService;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.ombre.woodhouse.Utils.PhoneVerification.checkPhoneNumber;

/**
 * Created by OMBRE on 2018/5/2.
 */

//密码登录块
public class Fragment_PWlogin extends Fragment {

    private EditText login_pw_edit;//登录密码框
    private Button btn_login_pw;//登录按键
    SharePreferences_getData preferences_getData;
    Loading_Dialog dialog;//登录等待精度条
    String  result;
    private static final int PWLOGINSUCCESS=1001;//登录成功
    private static final int PWLOGINFAILS=1002;//登录失败
    private static final int PWLOGINCONNCTFAILS=1111;//网络连接失败
    public Fragment_PWlogin() {
     super();
    }

    Handler pwloginHandler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case  PWLOGINCONNCTFAILS:
                    dialog.dismiss();
                    Toast.makeText(getActivity(),"网络不给力",Toast.LENGTH_SHORT).show();
                    break;
                case PWLOGINSUCCESS:
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                    new SharePreferences_Manager(getActivity()).savePW_VER("");
                    new SharePreferences_Manager(getActivity()).saveLoginState(true);
                    getActivity().finish();
                    break;
                case PWLOGINFAILS:
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "密码或账号错误，请重新输入。", Toast.LENGTH_SHORT).show();//运用dialog显示
                    break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_login_pw,container,false);
        preferences_getData=new SharePreferences_getData(getActivity());
        login_pw_edit=(EditText)view.findViewById(R.id.login_pw_edit);
        btn_login_pw=(Button)view.findViewById(R.id.btn_login_pw);
        dialog=new Loading_Dialog(getActivity(),R.style.dialog);

                login_pw_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                new SharePreferences_Manager(getActivity()).savePW_VER(login_pw_edit.getText().toString());
            }
        });

        btn_login_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences_getData.getLoginWay().equals("password")){
                    if(JudeEnpty()) {
                        doLogin();
                    }
                }
            }
        });
        return view;
    }

    //登录操作实现方法
    private void doLogin(){
        if(checkPhoneNumber(preferences_getData.getUserID())) {
            dialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域不关闭Dialog
            dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userID", preferences_getData.getUserID());
                    map.put("userPW", preferences_getData.getPW_VER());
                    MemberInterfaceService memberInterfaceService=new MemberInterfaceService(getActivity());
                    Callbacks callbacks=new Callbacks() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = PWLOGINCONNCTFAILS;
                            pwloginHandler.sendMessage(message);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            result=response.body().string();
                            Message message=new Message();
                           if (result.equals("true")) {
                               message.what=PWLOGINSUCCESS;
                            } else {
                              message.what=PWLOGINFAILS;
                            }
                            pwloginHandler.sendMessage(message);
                        }
                    };
                    memberInterfaceService.receiveLoginRequest(map,callbacks);
            }
        }).start();
        }else {
            Toast.makeText(getActivity(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        }
    }

    //碎片点show()后的执行的方法
    public void onHiddenChanged(boolean hidden) {
// TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
            btn_login_pw.setClickable(false);
        } else {// 重新显示到最前端中
            login_pw_edit.setText("");
            new SharePreferences_Manager(getActivity()).savePW_VER("");
            btn_login_pw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(preferences_getData.getLoginWay().equals("password")){
                        if(JudeEnpty()) {
                            doLogin();
                        }}
                }
            });
        }
    }
    //密码(验证码)或手机号是否输入
    private boolean JudeEnpty(){

        if(preferences_getData.getUserID().equals("")){
            Toast.makeText(getActivity(),"请输入手机号码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(preferences_getData.getPW_VER().equals("")){
            Toast.makeText(getActivity(),"请输入登录密码",Toast.LENGTH_SHORT).show();
           return false;
        }
        else{
         return true;
        }
    }

    @Override
    public void onResume() {
        login_pw_edit.setText("");
        super.onResume();
    }
}
