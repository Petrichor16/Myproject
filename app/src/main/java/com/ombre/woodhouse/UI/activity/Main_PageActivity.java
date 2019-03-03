package com.ombre.woodhouse.UI.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Helper.BottomNavigationViewHelper;
import com.ombre.woodhouse.Helper.ReplaceFragfmentHelper;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.UI.fragment.Fragment_CartPage;
import com.ombre.woodhouse.UI.fragment.Fragment_Classification;
import com.ombre.woodhouse.UI.fragment.Fragment_FirstPage;
import com.ombre.woodhouse.UI.fragment.Fragment_MyInformation;

public class Main_PageActivity extends AppCompatActivity {


    ReplaceFragfmentHelper fragfmentHelper;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home://首页
                    fragfmentHelper.replaceFragment(R.id.mainpage_fragment,new Fragment_FirstPage(Main_PageActivity.this));
                    return true;
                case R.id.navigation_dashboard://分类
                    fragfmentHelper.replaceFragment(R.id.mainpage_fragment,new Fragment_Classification(Main_PageActivity.this));
                    return true;
                case R.id.navigation_cart://购物车
                    fragfmentHelper.replaceFragment(R.id.mainpage_fragment,new Fragment_CartPage(Main_PageActivity.this));
                    return true;
                case R.id.navigation_my://我的信息
                    fragfmentHelper.replaceFragment(R.id.mainpage_fragment,new Fragment_MyInformation(Main_PageActivity.this));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__page);
        fragfmentHelper=new ReplaceFragfmentHelper(this);
        fragfmentHelper.replaceFragment(R.id.mainpage_fragment,new Fragment_FirstPage(Main_PageActivity.this));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
    }


    //双击退出
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(Main_PageActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
