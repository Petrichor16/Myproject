package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

public class SettingActivity extends AppCompatActivity {

    SharePreferences_getData preferences_getData;
    private boolean login_state;
    Intent intent_login;
   private TextView back_loginstate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        preferences_getData=new SharePreferences_getData(this);
        login_state=preferences_getData.getLoginState();
        intent_login=new Intent(this,LoginActivity.class);
        back_loginstate=(TextView)findViewById(R.id.back_loginstate);
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.setting_back://返回按键
                finish();
                break;
            case R.id.setting_personInfo://个人信息
                if(login_state){
                    Intent intent_info=new Intent(this, MyInformationActivity.class);
                    startActivity(intent_info);
                }else{
                    startActivity(intent_login);
                }
                break;
            case R.id.setting_personAddress://个人地址
                if(login_state){
                   Intent intent_address=new Intent(SettingActivity.this,AddressActivity.class);
                    intent_address.putExtra("type","");
                    startActivity(intent_address);
                }else{
                    startActivity(intent_login);
                }
                break;
            case R.id.setting_modifyPW://个人修改密码
                if(login_state){
                    Intent intent_pw=new Intent(this,ModifyPwActivity.class);
                    startActivity(intent_pw);
                }else{
                    startActivity(intent_login);
                }
                break;
            case R.id.setting_Consultation://咨询
                Intent intent_sev=new Intent(this,ServicefeedbackActivity.class);
                startActivity(intent_sev);
                break;
            case R.id.setting_About://关于
               Intent intent_about=new Intent(SettingActivity.this,AboutShopActivity.class);
                startActivity(intent_about);
                break;
            case R.id.back_loginstate://退出登录
                new SharePreferences_Manager(this).saveLoginState(false);
                new SharePreferences_Manager(this).saveUserID("");
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        preferences_getData=new SharePreferences_getData(this);
        login_state=preferences_getData.getLoginState();
        intent_login=new Intent(this,LoginActivity.class);
        if(login_state)
            back_loginstate.setVisibility(View.VISIBLE);
        else
            back_loginstate.setVisibility(View.INVISIBLE);
        super.onResume();
    }
}
