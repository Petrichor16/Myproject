package com.ombre.woodhouse.UI.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Bean.Member;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.MemberInterfaceService;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Utils.MobEror_Code;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.UI.fragment.Fragment_SMSlogin;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Response;
import static cn.smssdk.SMSSDK.getSupportedCountries;
import static cn.smssdk.SMSSDK.getVerificationCode;
import static cn.smssdk.SMSSDK.submitVerificationCode;
import static com.ombre.woodhouse.Utils.IconColor_Change.setImageViewColor;
import static com.ombre.woodhouse.Utils.PhoneVerification.checkPhoneNumber;

public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText register_phone;//手机号
    private EditText registerCode;//验证码
    private EditText register_pw;//密码
    private ImageView back_register;//返回按钮
    private Button verfication_register;//获取验证
    private Button btn_register;//注册
    private TextView pw_size;//密码的位数

    private ImageView pw_versionBtn;//密码是否可见
    CharSequence temp;//框中的密码位数
    boolean pw_size_jude=false;
    private boolean versionBtn_flag;//标记密码可见状态

    Loading_Dialog dialog;//登录等待精度条
    int i=0;//使错误码回调一次即可
    int j=0;
    SharePreferences_getData preferences_getData;
    String result;//服务器返回的结果
    public static final int USERREGISTERCONNECT_FAILS=2001;//网络不支持访问服务器
    public static final int USERREGISTERREGISTERRESULT=2002;//注册后的状态
    public static final int USERREGISTERCONNECT_VERFAILS=2003;//验证失败
    public static final int USERREGISTERCONNECTSUCCESS=2005;//网络连接成功
    public static final int USERREGISTERVERFICATION_SUCCESS=2006;//验证成功
    public Handler registerHandler=new Handler() {
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case USERREGISTERCONNECT_FAILS://网络不能支持登录
                    dialog.dismiss();
                    Toast.makeText(UserRegisterActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    break;
                case USERREGISTERREGISTERRESULT://注册后的状态
                    dialog.dismiss();
                    Toast.makeText(UserRegisterActivity.this,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    if ( msg.obj.toString().equals("用户注册成功"))
                        finish();
                    break;
                case USERREGISTERCONNECT_VERFAILS://验证失败
                    dialog.dismiss();
                    Toast.makeText(UserRegisterActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case USERREGISTERCONNECTSUCCESS://获取验证码
                    dialog.dismiss();
                    Member member=new analysisXMLHelper().analysisMember(msg.obj.toString());
                    if (member!=null) {
                        Toast.makeText(UserRegisterActivity.this, "该手机号已被注册", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        i=0;
                        Fragment_SMSlogin.TimeCount time = new Fragment_SMSlogin.TimeCount(60000, 1000,verfication_register);//设置重新获取验证码的等待时间
                        time.start();
                        getSupportedCountries();
                        getVerificationCode("86", preferences_getData.getUserID());
                    }
                    break;
                case USERREGISTERVERFICATION_SUCCESS://验证成功后
                    if(j==0){
                        doRegister();
                        j++;
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private void init(){
        register_phone=(EditText)findViewById(R.id.register_phone);
        registerCode=(EditText)findViewById(R.id.registerCode);
        register_pw=(EditText)findViewById(R.id.register_pw);
        back_register=(ImageView)findViewById(R.id.back_register);
        verfication_register=(Button)findViewById(R.id.verfication_register);
        btn_register=(Button)findViewById(R.id.btn_register);
        pw_versionBtn=(ImageView)findViewById(R.id.pw_versionBtn);
        pw_size=(TextView)findViewById(R.id.pw_size);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        init();
        preferences_getData=new SharePreferences_getData(this);
        //默认密码不可见
        setImageViewColor(pw_versionBtn,R.color.colorGray);
        versionBtn_flag=false;
        dialog=new Loading_Dialog(this,R.style.dialog);
        register_phone.setText(preferences_getData.getUserID());
        //判断密码位数是否输入正确
        pw_size.setVisibility(View.INVISIBLE);
        register_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp=s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() == 0) {
                    pw_size.setVisibility(View.INVISIBLE);
                } else {
                    if (temp.length() < 8) {
                        pw_size.setVisibility(View.VISIBLE);
                        pw_size_jude = false;
                        pw_size.setText("" + temp.length() + "/16");
                        pw_size.setTextColor(0xFFF90303);
                    } else {
                        pw_size_jude = true;
                        pw_size.setText("" + temp.length() + "/16");
                        pw_size.setTextColor(0xFF414141);
                    }
                }
            }
        });
        //手机号编辑框实时监听
        register_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                new SharePreferences_Manager(UserRegisterActivity.this).saveUserID(register_phone.getText().toString());
            }
        });



        back_register.setOnClickListener(this);
        verfication_register.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        pw_versionBtn.setOnClickListener(this);
    }
  //按键监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_register:
                finish();
                break;
            case R.id.verfication_register:
                startSMS();
                doVerfication();
                break;
            case R.id.btn_register:
                startSMS();
               if(Jude_Input())
               {
                  i=0;
                    dialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域不关闭Dialog
                    dialog.show();
                   submitVerificationCode("86", preferences_getData.getUserID(), registerCode.getText().toString());}
                break;
            case R.id.pw_versionBtn:
                CipherVisibility();
                break;
            default:
                break;
        }
    }
    //短信验证返回结果
    private void startSMS(){
        // 启动短信验证sdk
        //SMSSDK.initSDK(this, appKey, appSecret);

        EventHandler  eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        Message msg = new Message();
                        msg.what=USERREGISTERVERFICATION_SUCCESS;
                        registerHandler.sendMessage(msg);}
                }else {
                    if(i==0){
                        i++;
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        int status = object.optInt("status");//获取错误代码
                        Message msg = new Message();
                        msg.what=USERREGISTERCONNECT_VERFAILS;
                        msg.obj=new MobEror_Code().getErorString(status);
                        registerHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    //判断手机号
    public boolean Judge_Phone(SharePreferences_getData preferences_getData){
        if (preferences_getData.getUserID().equals("")) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!checkPhoneNumber(preferences_getData.getUserID()) ) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }else{return true;}
    }
    //判断输入框是否输入 或者输入是否符合输入标准
    private boolean Jude_Input(){
        if(!Judge_Phone(preferences_getData)){
            return false;
        }else if(registerCode.getText().toString().equals("")){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(register_pw.getText().toString().equals("")){
            Toast.makeText(this,"请输入注册密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!pw_size_jude){
            Toast.makeText(this,"密码格式不正确，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    //执行注册操作
    private void doRegister(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userID", preferences_getData.getUserID());
                    map.put("userPW", register_pw.getText().toString().trim());
                    MemberInterfaceService memberInterfaceService = new MemberInterfaceService(UserRegisterActivity.this);
                    Callbacks callbacks=new Callbacks() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = USERREGISTERCONNECT_FAILS;
                            registerHandler.sendMessage(message);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            result = response.body().string();
                            Message message = new Message();
                            message.what=USERREGISTERREGISTERRESULT;
                            message.obj=result;
                            registerHandler.sendMessage(message);
                        }
                    };
                    memberInterfaceService.registerusers(map,callbacks);
                }
            }).start();
    }
    //判断手机是否被注册
    private void doVerfication(){
        if(Judge_Phone(preferences_getData)){
            dialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域不关闭Dialog
            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MemberInterfaceService memberInterfaceService = new MemberInterfaceService(UserRegisterActivity.this);
                    final Map<String, String> map = new HashMap<String, String>();
                        map.put("userID", preferences_getData.getUserID());
                        //添加http访问服务器结果的处理事件
                        Callbacks callbacks = new Callbacks() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Message message = new Message();
                                message.what = USERREGISTERCONNECT_FAILS;
                                registerHandler.sendMessage(message);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                result = response.body().string();
                                Message message = new Message();
                                message.what=USERREGISTERCONNECTSUCCESS;
                                message.obj=result;
                                registerHandler.sendMessage(message);
                            }
                        };
                    memberInterfaceService.receiveSelectMemberRequest(map,callbacks);
                }
            }).start();}
    }
    //密码可见性操作
    private void CipherVisibility(){
        if(versionBtn_flag){
            //如果是不能看到密码的情况下，
            setImageViewColor(pw_versionBtn,R.color.colorGray);//不可见
            register_pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
            versionBtn_flag=false;
        }else{
            //如果是能看到密码的状态下
            setImageViewColor(pw_versionBtn,R.color.colorBlack);//可见
            register_pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            versionBtn_flag=true;
        }
    }
}
