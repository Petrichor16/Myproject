package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

//启动界面
public class Start_upActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        final int times=new SharePreferences_getData(Start_upActivity.this).getStartTimews();
       handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (times!=0) {
                    Intent intent1 = new Intent(Start_upActivity.this, Main_PageActivity.class);
                    startActivity(intent1);
                    finish();
                }
                else{
                    Intent intent = new Intent(Start_upActivity.this, GuidePageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }
}
