package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.UI.fragment.Fragment_PWlogin;
import com.ombre.woodhouse.UI.fragment.Fragment_SMSlogin;

//登录界面
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView login_back_bt;//退出登录界面
    private TextView message_login;//短信验证登录
    private EditText login_phone_edit;//手机号
    private TextView login_user_register;//用户注册
    private TextView login_forget_pw;//忘记密码

   SharePreferences_getData preferences_getData;

    Fragment[] fragments=new Fragment[2];//碎片数组
    int currentIndex=-1;//当前点击的游标值，默认是-1，说明还没点

    //初始化控件
    private void init(){
        login_back_bt=(ImageView)findViewById(R.id.login_back_bt);
        message_login=(TextView)findViewById(R.id.message_login);
        login_phone_edit=(EditText)findViewById(R.id.login_phone_edit);
        login_user_register=(TextView)findViewById(R.id.login_user_register);
        login_forget_pw=(TextView)findViewById(R.id.login_forget_pw);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences_getData=new SharePreferences_getData(this);
        init();
        replaceFragment(0);
        new SharePreferences_Manager(this).saveLoginWay("password");//密码登录方式
        if( preferences_getData.getUserID().equals("")){
            login_phone_edit.setCursorVisible(true);}
        else
        {
            login_phone_edit.setText(preferences_getData.getUserID());
            login_phone_edit.setCursorVisible(false);
        }
        login_back_bt.setOnClickListener(this);//退出登录界面
        message_login.setOnClickListener(this);//短信验证登录
        login_user_register.setOnClickListener(this);//用户注册
        login_forget_pw.setOnClickListener(this);//忘记密码
        login_phone_edit.setOnClickListener(this);

//对手机号编辑框实时监听,记住账户名
        login_phone_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                new SharePreferences_Manager(LoginActivity.this).saveUserID(login_phone_edit.getText().toString());
            }
        });

    }

    //按键的监听操作
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //退出登录界面
            case  R.id.login_back_bt:
                finish();
                break;
            //短信验证登录
            case  R.id.message_login:
                if(message_login.getText().equals("手机验证码登录")){
                    message_login.setText("账号密码登录");
                    new SharePreferences_Manager(this).saveLoginWay("SMS");
                    new SharePreferences_Manager(this).savePW_VER("");
                    replaceFragment(1);
                    login_forget_pw.setVisibility(View.INVISIBLE);
                }else{
                    message_login.setText("手机验证码登录");
                    new SharePreferences_Manager(this).saveLoginWay("password");
                    new SharePreferences_Manager(this).savePW_VER("");
                    replaceFragment(0);
                    login_forget_pw.setVisibility(View.VISIBLE);
                }
                break;
            //用户注册
            case  R.id.login_user_register:
                Intent intent_reg=new Intent(this,UserRegisterActivity.class);
                startActivity(intent_reg);
                break;
            //忘记密码
            case  R.id.login_forget_pw:
                Intent intent_forgetpw=new Intent(this,ForgetPWActivity.class);
                startActivity(intent_forgetpw);
                break;
            case  R.id.login_phone_edit:
                login_phone_edit.setCursorVisible(true);
                break;
            default:
                break;
        }
    }

    //创建碎片实例
    private void createFragment(int i){
        //如果碎片第一次点开就要创建碎片
        switch (i){
            case 0:
                fragments[i]=new Fragment_PWlogin();
                break;
            case 1:
                fragments[i]=new Fragment_SMSlogin();
                break;
            default:
                break;
        }
    }

    //碎片切换
    private void replaceFragment(int i){

        //处理碎片，显示、移除等
        //这里要用碎片的实物事物完成
       FragmentManager fragmentManager=getSupportFragmentManager();//创建添加碎片实例
        android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();//调用beginTransaction()开启事务
        if(currentIndex!=-1){
            //移除碎片
            transaction.hide(fragments[currentIndex]);
        }
            //显示新的碎片
            if (fragments[i] == null) {
                //创建新的碎片
                createFragment(i);

                //使用事务显示碎片
                transaction.add(R.id.login_mode_fragment, fragments[i]);
            } else {
                //如果碎片曾经显示过就显示出来
                transaction.show(fragments[i]);
            }
        //保存用户点击的游标值
        currentIndex=i;
        transaction.commit();//提交事务
    }

    //上一个活动结束后更新ui
    @Override
    protected void onRestart() {
        login_phone_edit.setText(preferences_getData.getUserID());
        new SharePreferences_Manager(this).savePW_VER("");
        super.onRestart();
    }

    /*@Override
    public void onBackPressed() {
        new SharePreferences_Manager(this).saveLoginState(true);
        finish();
        super.onBackPressed();
    }*/
}
