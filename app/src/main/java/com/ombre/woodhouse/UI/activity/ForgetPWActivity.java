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
import com.ombre.woodhouse.Http.OKHttpUtils;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.MemberInterfaceService;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.UI.fragment.Fragment_SMSlogin;
import com.ombre.woodhouse.Utils.MobEror_Code;
import com.ombre.woodhouse.Utils.URL_Address;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

public class ForgetPWActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText findPW_phone;//手机号
    private EditText findPWCode;//验证码
    private EditText findPW_edit;//密码
    private ImageView back_findPW;//返回按钮
    private Button verfication_findPW;//获取验证
    private Button btn_findPW;//找回密码
    private TextView findPW_size;//密码的位数
    private ImageView findPW_versionBtn;//密码是否可见

    CharSequence temp;//框中的密码位数
    boolean pw_size_jude=false;
    private boolean versionBtn_flag;//标记密码可见状态
    URL_Address urlHead;
    Loading_Dialog dialog;//登录等待精度条
    SharePreferences_getData preferences_getData;
    int i=0;//使错误码回调一次即可
    int j=0;
    public static final int FORGETPWCONNECT_FAILS=3001;//网络不支持连接相应服务器
    public static final int FORGETPWCONNECT_VERFAILS=3003;//验证失败
    public static final int FORGETPWVERFICATION_SUCCESS=3005;//验证成功
    public static final int FORGETPWCONNECTSUCCESS=3006;//网络连接成功
    public static final int FORGETPWMODIFYSUCCESS=3007;//密码修改成功
    public Handler forgetpwHandler=new Handler() {
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case FORGETPWCONNECT_FAILS://网络不能支持登录
                    dialog.dismiss();
                    Toast.makeText(ForgetPWActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    break;
                case FORGETPWVERFICATION_SUCCESS://验证成功后
                    if(j==0){
                        doFindPW();
                        j++;
                    }
                    break;
                case FORGETPWCONNECT_VERFAILS://验证失败
                    dialog.dismiss();
                    if(i==0) {
                        i++;
                        Toast.makeText(ForgetPWActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FORGETPWCONNECTSUCCESS://获取验证码
                    dialog.dismiss();
                    Member member;
                    member=new analysisXMLHelper().analysisMember(msg.obj.toString());//获取对象
                    if (member == null) {
                        Toast.makeText(ForgetPWActivity.this, "该手机号未被注册", Toast.LENGTH_SHORT).show();//运用dialog显示
                    } else {//进行验证
                        Fragment_SMSlogin.TimeCount time = new Fragment_SMSlogin.TimeCount(60000, 1000,verfication_findPW);
                        time.start();
                        getSupportedCountries();
                        getVerificationCode("86", preferences_getData.getUserID());
                    }
                    break;
                case FORGETPWMODIFYSUCCESS:
                        Toast.makeText(ForgetPWActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        if (msg.obj.toString().equals("密码修改成功")) {
                            finish();
                        }
                    break;
                default:
                    break;
            }
        }
    };

    //初始化控件
    private void init(){
        findPW_phone=(EditText)findViewById(R.id.findPW_phone);
        findPWCode=(EditText)findViewById(R.id.findPWCode);
        findPW_edit=(EditText)findViewById(R.id.findPW_edit);
        back_findPW=(ImageView)findViewById(R.id.back_findPW);
        verfication_findPW=(Button)findViewById(R.id.verfication_findPW);
        btn_findPW=(Button)findViewById(R.id.btn_findPW);
        findPW_versionBtn=(ImageView)findViewById(R.id.findPW_versionBtn);
        findPW_size=(TextView)findViewById(R.id.findPW_size);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pw);
        init();
        dialog=new Loading_Dialog(this,R.style.dialog);
        preferences_getData=new SharePreferences_getData(this);
        //默认密码不可见
        setImageViewColor(findPW_versionBtn,R.color.colorGray);
        versionBtn_flag=false;
        //显示修改的手机号码
        findPW_phone.setText(preferences_getData.getUserID());
        //判断密码位数是否输入正确
        findPW_size.setVisibility(View.INVISIBLE);
        findPW_edit.addTextChangedListener(new TextWatcher() {
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
                    findPW_size.setVisibility(View.INVISIBLE);
                } else {
                    if (temp.length() < 8) {
                        findPW_size.setVisibility(View.VISIBLE);
                        pw_size_jude = false;
                        findPW_size.setText("" + temp.length() + "/16");
                        findPW_size.setTextColor(0xFFF90303);
                    } else {
                        pw_size_jude = true;
                        findPW_size.setText("" + temp.length() + "/16");
                        findPW_size.setTextColor(0xFF414141);
                    }
                }
            }
        });
        //手机号编辑框实时监听
        findPW_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new SharePreferences_Manager(ForgetPWActivity.this).saveUserID(findPW_phone.getText().toString());
            }
        });


        urlHead=new URL_Address();
        back_findPW.setOnClickListener(this);
        verfication_findPW.setOnClickListener(this);
        btn_findPW.setOnClickListener(this);
        findPW_versionBtn.setOnClickListener(this);
    }

    //按键监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_findPW:
                finish();
                break;
            case R.id.verfication_findPW:
                startSMS();
                doVerfication();
                break;
            case R.id.btn_findPW:
                startSMS();
                if(Jude_Input()){
                   dialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域不关闭Dialog
                    dialog.show();
                    i=0;
                   submitVerificationCode("86", preferences_getData.getUserID(), findPWCode.getText().toString());
               }
                break;
            case R.id.findPW_versionBtn:
                CipherVisibility();
                break;
            default:
                break;
        }
    }
    //短信验证返回结果
    public void startSMS(){
        // 启动短信验证sdk
        //SMSSDK.initSDK(this, appKey, appSecret);
        final EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        Message msg = new Message();
                        msg.what=FORGETPWVERFICATION_SUCCESS;
                        forgetpwHandler.sendMessage(msg);}
                }else {

                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        int status = object.optInt("status");//获取错误代码
                        Message msg = new Message();
                        msg.what=FORGETPWCONNECT_VERFAILS;
                        msg.obj=new MobEror_Code().getErorString(status);
                        forgetpwHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    //判断输入框是否输入 或者输入是否符合输入标准
    private boolean Jude_Input(){
        if(!new Fragment_SMSlogin().Judge_Phone(preferences_getData)){
            return false;
        }else if(findPWCode.getText().toString().equals("")){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(findPW_edit.getText().toString().equals("")){
            Toast.makeText(this,"请输入需要修改的密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!pw_size_jude){
            Toast.makeText(this,"密码格式不正确，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
    //找回密码
    private void doFindPW(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userID", preferences_getData.getUserID());
                map.put("userPW",findPW_edit.getText().toString());
                MemberInterfaceService memberInterfaceService=new MemberInterfaceService(ForgetPWActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message message = new Message();
                        message.what = FORGETPWCONNECT_FAILS;
                        forgetpwHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message msg=new Message();
                        msg.what=FORGETPWMODIFYSUCCESS;
                        msg.obj=result;
                        forgetpwHandler.sendMessage(msg);
                    }
                };
                memberInterfaceService.findpassword(map,callbacks);
            }
        }).start();
    }
    //判断手机是否被注册
    private void doVerfication(){
        if(new Fragment_SMSlogin().Judge_Phone(preferences_getData)){
            dialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域不关闭Dialog
            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Callbacks callbacks;
                    Map<String, String> map = new HashMap<String, String>();

                        map.put("userID", preferences_getData.getUserID());
                    MemberInterfaceService memberInterfaceService = new MemberInterfaceService(ForgetPWActivity.this);
                    callbacks=new Callbacks() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = FORGETPWCONNECT_FAILS;
                            forgetpwHandler.sendMessage(message);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result=response.body().string();
                            Message msg=new Message();
                            msg.what=FORGETPWCONNECTSUCCESS;
                            msg.obj=result;
                            forgetpwHandler.sendMessage(msg);

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
            setImageViewColor(findPW_versionBtn,R.color.colorGray);//不可见
            findPW_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            versionBtn_flag=false;
        }else{
            //如果是能看到密码的状态下
            setImageViewColor(findPW_versionBtn,R.color.colorBlack);//可见
            findPW_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            versionBtn_flag=true;
        }
    }
}
