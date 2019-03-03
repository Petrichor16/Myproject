package com.ombre.woodhouse.UI.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ombre.woodhouse.R;

public class AboutShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_shop);
    }
    public void onClick(View view){
        finish();
    }
}
