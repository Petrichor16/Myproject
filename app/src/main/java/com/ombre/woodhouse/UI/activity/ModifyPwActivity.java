package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.MemberInterfaceService;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.Utils.IconColor_Change;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.ombre.woodhouse.Utils.IconColor_Change.setImageViewColor;

public class ModifyPwActivity extends AppCompatActivity {

    private EditText txt_oldPw;//原密码
    private EditText txt_modifyPw;//新密码
    private TextView newPW_size;//密码位数提示
    private ImageView pw_version1;//原密码可见
    private ImageView pw_version2;//新密码可见

    CharSequence temp;//框中的密码位数
    boolean pw_size_jude=false;
    SharePreferences_getData preferences_getData;
    boolean flag,flag1;
    Loading_Dialog dialog;
    private void init(){
        txt_oldPw=(EditText)findViewById(R.id.txt_oldPw);
        txt_modifyPw=(EditText)findViewById(R.id.Txt_modifyPw);
        newPW_size=(TextView) findViewById(R.id.newPW_size);
        pw_version1=(ImageView) findViewById(R.id.pw_version1);
        pw_version2=(ImageView)findViewById(R.id.pw_version2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pw);
        init();
        dialog=new Loading_Dialog(this,R.style.dialog);
        preferences_getData=new SharePreferences_getData(ModifyPwActivity.this);
        new IconColor_Change().setImageViewColor(pw_version1,R.color.colorGray);
        new IconColor_Change().setImageViewColor(pw_version2,R.color.colorGray);
        flag=false;flag1=false;
        txt_modifyPw.addTextChangedListener(new TextWatcher() {
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
                    newPW_size .setVisibility(View.INVISIBLE);
                } else {
                    if (temp.length() < 8) {
                        newPW_size.setVisibility(View.VISIBLE);
                        pw_size_jude = false;
                        newPW_size.setText("" + temp.length() + "/16");
                        newPW_size.setTextColor(0xFFF90303);
                    } else {
                        pw_size_jude = true;
                        newPW_size.setText("" + temp.length() + "/16");
                        newPW_size.setTextColor(0xFF414141);
                    }
                }
            }
        });
    }
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.modifyPw_back:
                finish();
                break;
            case R.id.btn_modifyPw:
                judeOldPassword();
                break;
            case R.id.pw_version1:
                if(flag){
                    //如果是不能看到密码的情况下，
                    setImageViewColor(pw_version1,R.color.colorGray);//不可见
                    txt_oldPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag=false;
                }else{
                    //如果是能看到密码的状态下
                    setImageViewColor(pw_version1,R.color.colorBlack);//可见
                    txt_oldPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag=true;
                }
                break;
            case R.id.pw_version2:
                if(flag1){
                    //如果是不能看到密码的情况下，
                    setImageViewColor(pw_version2,R.color.colorGray);//不可见
                    txt_modifyPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag1=false;
                }else{
                    //如果是能看到密码的状态下
                    setImageViewColor(pw_version2,R.color.colorBlack);//可见
                    txt_modifyPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag1=true;
                }
                break;
        }
    }

    //判断输入是否正确
    private boolean judeInput(){
        String txt_oldPwStr=txt_oldPw.getText().toString();
        String txt_modifyPwStr=txt_modifyPw.getText().toString();
        if(txt_oldPwStr.equals("")){
            Toast.makeText(getApplicationContext(),"请输入原密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(txt_modifyPwStr.equals("")){
            Toast.makeText(getApplicationContext(),"请输入新密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if(txt_modifyPwStr.length()<8){
            Toast.makeText(getApplicationContext(),"密码位数应为8-16个字符",Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;
    }

    //判断原密码是否正确
    private void judeOldPassword(){
        if (judeInput()) {
            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userID", preferences_getData.getUserID());
                    map.put("userPW", txt_oldPw.getText().toString());
                    MemberInterfaceService memberInterfaceService = new MemberInterfaceService(ModifyPwActivity.this);
                    Callbacks callbacks = new Callbacks() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "网络不给力", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            final String result = response.body().string();
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             dialog.dismiss();
                             if (result.equals("true")) {
                                 modifyPw();
                             } else {
                                 Toast.makeText(getApplicationContext(), "原密码不正确", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                        }
                    };
                    memberInterfaceService.receiveLoginRequest(map, callbacks);
                }
            }).start();
        }
    }

    //修改密码
    private void modifyPw(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userID", preferences_getData.getUserID());
                map.put("userPW",txt_modifyPw.getText().toString());
                MemberInterfaceService memberInterfaceService=new MemberInterfaceService(ModifyPwActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(ModifyPwActivity.this, result, Toast.LENGTH_SHORT).show();
                           if (result.equals("密码修改成功")) {
                               new SharePreferences_Manager(ModifyPwActivity.this).saveLoginState(false);
                               new SharePreferences_Manager(ModifyPwActivity.this).saveUserID("");
                               Intent intent=new Intent(ModifyPwActivity.this,LoginActivity.class);
                               startActivity(intent);
                               finish();
                           }
                       }
                   });
                    }
                };
                memberInterfaceService.findpassword(map,callbacks);
            }
        }).start();
    }
}
