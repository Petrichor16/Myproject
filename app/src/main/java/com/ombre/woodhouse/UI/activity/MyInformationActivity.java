package com.ombre.woodhouse.UI.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Bean.Member;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.MemberInterfaceService;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mlxy.utils.T;
import okhttp3.Call;
import okhttp3.Response;

public class MyInformationActivity extends AppCompatActivity {

    private TextView txt_nickName;//昵称
    private EditText edit_nickName;
    private TextView txt_Phone;//电话
    private EditText edit_Phone;
    private TextView txt_Email;//邮箱
    private EditText edit_Email;
    private TextView txt_userID;//账户号
    SharePreferences_getData preferences_getData;
    Loading_Dialog dialog;
    private void init(){
        txt_nickName=(TextView)findViewById(R.id.txt_nickName);
        txt_Phone=(TextView)findViewById(R.id.txt_Phone);
        txt_Email=(TextView)findViewById(R.id.txt_Email);
        edit_nickName=(EditText)findViewById(R.id.edit_nickName);
        edit_Phone=(EditText)findViewById(R.id.edit_Phone);
        edit_Email=(EditText)findViewById(R.id.edit_Email);
        txt_userID=(TextView)findViewById(R.id.txt_userID);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        init();
        preferences_getData=new SharePreferences_getData(MyInformationActivity.this);
        dialog=new Loading_Dialog(this,R.style.dialog);
        txt_userID.setText(preferences_getData.getUserID().substring(0,3)+"****"+preferences_getData.getUserID().substring(7,11));
        getInfo();
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.info_back:
                finish();
                break;
            case R.id.btn_Info:
                updateInfo();
                break;
            case R.id.txt_nickName:
                edit_nickName.setVisibility(View.VISIBLE);
                txt_nickName.setVisibility(View.INVISIBLE);
                edit_nickName.setText(txt_nickName.getText().toString());
                break;
            case R.id.txt_Phone:
                edit_Phone.setVisibility(View.VISIBLE);
                txt_Phone.setVisibility(View.INVISIBLE);
                edit_Phone.setText(txt_Phone.getText().toString());
                break;
            case R.id.txt_Email:
                edit_Email.setVisibility(View.VISIBLE);
                txt_Email.setVisibility(View.INVISIBLE);
                edit_Email.setText(txt_Email.getText().toString());
                break;
        }
    }
    //获取个人信息
    private void getInfo(){
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("userID", preferences_getData.getUserID());
                MemberInterfaceService memberInterfaceService = new MemberInterfaceService(MyInformationActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"网络不给力",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        final String result=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Member member;
                                member=new analysisXMLHelper().analysisMember(result);//获取对象
                                if(member!=null){
                                    txt_nickName.setText(member.getMemberName());
                                    txt_Phone.setText(member.getPhone());
                                    txt_Email.setText(member.getEmail());
                                }
                            }
                        });
                    }
                };
                memberInterfaceService.receiveSelectMemberRequest(map,callbacks);
            }
        }).start();
    }
    //修改个人信息
    private void updateInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("userID", preferences_getData.getUserID());
                if(edit_nickName.getText().toString().equals(""))
                    map.put("nickname",txt_nickName.getText().toString());
                else
                    map.put("nickname",edit_nickName.getText().toString());
                if(edit_Phone.getText().toString().equals(""))
                    map.put("phone",txt_Phone.getText().toString());
                else
                    map.put("phone",edit_Phone.getText().toString());
                if(edit_Email.getText().toString().equals(""))
                    map.put("email",txt_Email.getText().toString());
                else
                    map.put("email",edit_Email.getText().toString());
                MemberInterfaceService memberInterfaceService = new MemberInterfaceService(MyInformationActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) { runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"网络不给力",Toast.LENGTH_SHORT).show();
                        }
                    });}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        final String result=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                if(result.equals("保存成功")){
                                    if(!edit_nickName.getText().toString().equals(""))
                                        txt_nickName.setText(edit_nickName.getText().toString());
                                    if(!edit_Phone.getText().toString().equals(""))
                                        txt_Phone.setText(edit_Phone.getText().toString());
                                    if(!edit_Email.getText().toString().equals(""))
                                        txt_Email.setText(edit_Email.getText().toString());
                                    edit_nickName.setVisibility(View.INVISIBLE);
                                    edit_Phone.setVisibility(View.INVISIBLE);
                                    edit_Email.setVisibility(View.INVISIBLE);
                                    txt_nickName.setVisibility(View.VISIBLE);
                                    txt_Phone.setVisibility(View.VISIBLE);
                                    txt_Email.setVisibility(View.VISIBLE);
                                }
                                else{
                                    edit_nickName.setVisibility(View.INVISIBLE);
                                    edit_Phone.setVisibility(View.INVISIBLE);
                                    edit_Email.setVisibility(View.INVISIBLE);
                                    txt_nickName.setVisibility(View.VISIBLE);
                                    txt_Phone.setVisibility(View.VISIBLE);
                                    txt_Email.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                };
                memberInterfaceService.updateMemberInfo(map,callbacks);
            }
        }).start();
    }
}
