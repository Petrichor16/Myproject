package com.ombre.woodhouse.UI.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.ombre.woodhouse.Bean.Member;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.MemberInterfaceService;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Utils.MobEror_Code;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

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
import static com.ombre.woodhouse.Utils.PhoneVerification.checkPhoneNumber;

/**
 * Created by OMBRE on 2018/5/2.
 */

//密码登录块
public class Fragment_SMSlogin extends Fragment {
    private EditText VerificationCode_edit;//验证码
    private Button btn_getVerificationCode;//验证按键
    private Button btn_login_sms;//登录按钮
    SharePreferences_getData preferences_getData;
    Loading_Dialog dialog;//登录等待精度条
    private EventHandler eh;
    String phone;

    public Fragment_SMSlogin() {
        super();
    }
    int i=0;//使错误码回调一次即可
    public static final int SMSLOGINCONNECT_VERFAILS=2001;//验证失败
    public static final int SMSLOGINVERFICATION_SUCCESS=2002;//验证成功
    public static final int SMSLOGINCONNCTFAILS=2003;//网络连接不成功
    public static final int SMSLOGINCONNCTSUCCESS=2004;//网络连接不成功
    public Handler smsloginHandler=new Handler() {
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case  SMSLOGINCONNCTFAILS:
                    dialog.dismiss();
                    Toast.makeText(getActivity(),"网络不给力",Toast.LENGTH_SHORT).show();
                    break;
                case SMSLOGINVERFICATION_SUCCESS://登陆成功后
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                    new SharePreferences_Manager(getActivity()).savePW_VER("");
                    new SharePreferences_Manager(getActivity()).saveLoginState(true);
                    getActivity().finish();
                    break;
                case SMSLOGINCONNECT_VERFAILS://账号或者密码错误
                    dialog.dismiss();
                    Toast.makeText(getActivity(),  msg.obj.toString(), Toast.LENGTH_SHORT).show();//运用dialog显示
                    break;
                case SMSLOGINCONNCTSUCCESS:
                    dialog.dismiss();
                    Member member;
                    member=new analysisXMLHelper().analysisMember(msg.obj.toString());//获取对象
                    if (member == null) {
                        Toast.makeText(getActivity(), "该手机号未被注册", Toast.LENGTH_SHORT).show();//运用dialog显示
                    } else {//进行验证
                        i = 0;
                        TimeCount time = new TimeCount(60000, 1000, btn_getVerificationCode);
                        time.start();
                        getSupportedCountries();
                        getVerificationCode("86", preferences_getData.getUserID());
                    }
                    break;
                default:
                    break;
            }
        }
    };




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login_sms,container,false);
        preferences_getData=new SharePreferences_getData(getActivity());
        VerificationCode_edit=(EditText)view.findViewById(R.id.VerificationCode_edit);
        btn_getVerificationCode=(Button) view.findViewById(R.id.btn_getVerificationCode);
        btn_login_sms=(Button)view.findViewById(R.id.btn_login_sms) ;
        dialog=new Loading_Dialog(getActivity(),R.style.dialog);
        VerificationCode_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new SharePreferences_Manager(getActivity()).savePW_VER(VerificationCode_edit.getText().toString());
            }
        });

        btn_login_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSMS();
                if(preferences_getData.getLoginWay().equals("SMS")){
                phone=preferences_getData.getUserID();
                //填写了验证码，进行验证

                    if(Judge_Phone(preferences_getData)) {
                        if (preferences_getData.getPW_VER().equals("")) {
                            Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            dialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域不关闭Dialog
                            dialog.show();
                            i=0;
                        submitVerificationCode("86", phone, preferences_getData.getPW_VER());}
                    }}
            }
        });


        //获取验证码
        btn_getVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSMS();
                doVerfication();
            }
        });
        return view;
    }

    //短信验证返回结果
    private void startSMS(){
        // 启动短信验证sdk
        //SMSSDK.initSDK(this, appKey, appSecret);

        eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        Message msg = new Message();
                        msg.what=SMSLOGINVERFICATION_SUCCESS;
                        smsloginHandler.sendMessage(msg);}
                }else {

                    if (i == 0) {
                        // dialog.dismiss();
                        i++;
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            int status = object.optInt("status");//获取错误代码
                            Message msg = new Message();
                            msg.what = SMSLOGINCONNECT_VERFAILS;
                            msg.obj = new MobEror_Code().getErorString(status);
                            smsloginHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    //判断是否输入手机号，手机号是否输入正确
    public boolean Judge_Phone(SharePreferences_getData preferences_getData){
        if (preferences_getData.getUserID().equals("")) {
            Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!checkPhoneNumber(preferences_getData.getUserID()) ) {
            Toast.makeText(getActivity(), "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }else{return true;}
    }
    //判断手机是否被注册
    private void doVerfication(){

        if(Judge_Phone(preferences_getData)) {
            dialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域不关闭Dialog
            dialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Map<String, String> map = new HashMap<String, String>();
                    map.put("userID", preferences_getData.getUserID());
                    MemberInterfaceService memberInterfaceService = new MemberInterfaceService(getActivity());
                    Callbacks callbacks=new Callbacks() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = SMSLOGINCONNCTFAILS;
                            smsloginHandler.sendMessage(message);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result=response.body().string();
                            Message msg=new Message();
                            msg.what=SMSLOGINCONNCTSUCCESS;
                            msg.obj=result;
                            smsloginHandler.sendMessage(msg);

                        }
                    };
                    memberInterfaceService.receiveSelectMemberRequest(map,callbacks);
                }
            }).start();
        }

        }
    //碎片点show()后的执行的方法
    public void onHiddenChanged(boolean hidden) {
// TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
        } else {// 重新显示到最前端中
            VerificationCode_edit.setText("");
            new SharePreferences_Manager(getActivity()).savePW_VER("");
        }
    }
    //实现60s重新获取验证码
    public static class TimeCount extends CountDownTimer {
        Button btn_getVerificationCode;

        public TimeCount(long millisInFuture, long countDownInterval, Button btn_getVerificationCode) {
            super(millisInFuture, countDownInterval);
            this.btn_getVerificationCode = btn_getVerificationCode;
        }

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_getVerificationCode.setClickable(false);
            btn_getVerificationCode.setBackgroundResource(R.drawable.btn_verification_after);
            btn_getVerificationCode.setTextColor(0xFF0E0E0E);
            btn_getVerificationCode.setText("重新获取" + millisUntilFinished / 1000  + "");
            btn_getVerificationCode.setClickable(false);

        }

        @Override
        public void onFinish() {
            btn_getVerificationCode.setBackgroundResource(R.drawable.btn_verification_before);
            btn_getVerificationCode.setTextColor(0xFFFFFEFE);
            btn_getVerificationCode.setText("获取验证码");
            btn_getVerificationCode.setClickable(true);
        }
    }

    @Override
    public void onResume() {
        VerificationCode_edit.setText("");
        super.onResume();
    }
}

