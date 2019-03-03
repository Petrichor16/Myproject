package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ombre.woodhouse.R;
import com.ombre.woodhouse.Utils.IconColor_Change;

//服务反馈
public class ServicefeedbackActivity extends AppCompatActivity {

    private ImageView servicePhoneIcon;
    private TextView servicePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicefeedback);
        servicePhoneIcon=(ImageView)findViewById(R.id.servicePhoneIcon);
        servicePhone=(TextView) findViewById(R.id.servicePhone);
        new IconColor_Change().setImageViewColor(servicePhoneIcon,R.color.colorGray);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.service_back:
                finish();
                break;
            case R.id.connect:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + servicePhone.getText().toString()));//直接拨打电话
                startActivity(intent);
                break;
        }
    }
}
