package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ombre.woodhouse.Adapter.GuidePageAdapter;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.util.ArrayList;
import java.util.List;



//引导页
public class GuidePageActivity extends AppCompatActivity {

    private ViewPager guidepageview;//引导页面
    private TextView startShop;//开始按钮
    private LinearLayout duidepageDot;//下标圆点

    int prePosition=0;
    List<View> viewList;
    int img[];
    private void init(){
        guidepageview=(ViewPager)findViewById(R.id.guidepageview);
        startShop=(TextView) findViewById(R.id.startShop);
        duidepageDot=(LinearLayout) findViewById(R.id.duidepageDot);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        init();
        realizeSowingmap();
        startShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuidePageActivity.this,Main_PageActivity.class);
                startActivity(intent);
                finish();
                int times=new SharePreferences_getData(GuidePageActivity.this).getStartTimews();
                times++;
                new SharePreferences_Manager(GuidePageActivity.this).saveStartTimes(times);
            }
        });
    }
    //初始化页面
    private void initViewList(){
        viewList=new ArrayList<>();
        img=new int[]{R.drawable.guide_icon1,R.drawable.guide_icon2,R.drawable.guide_icon3};
        for (int i=0;i<img.length;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(img[i]);
            viewList.add(imageView);
        }
    }
    //实现图片轮播
    private void realizeSowingmap(){
        initViewList();
        initDots();
        guidepageview.setPageMargin(10);//设置页与页之间的间距
        guidepageview.setOffscreenPageLimit(1);//限定预加载的页面个数
        GuidePageAdapter myAdapter=new GuidePageAdapter(viewList);
        guidepageview.setAdapter(myAdapter);
        guidepageview.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if(viewList.size()>0) {
                    duidepageDot.getChildAt(prePosition).setEnabled(false);
                    duidepageDot.getChildAt(position % viewList.size()).setEnabled(true);
                    prePosition = position % viewList.size();
                }
                if (position==viewList.size()-1)
                    startShop.setVisibility(View.VISIBLE);
                else
                    startShop.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    //添加圆点
    private void initDots() {
        if (viewList.size()>0) {
            if (null != duidepageDot) {
                duidepageDot.removeAllViews();//清除所有的布局控件
            }
            for (int i = 0; i < viewList.size(); i++) {
                ImageView dot = new ImageView(this);
                dot.setEnabled(false);
                dot.setImageResource(R.drawable.dot);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 10;
                dot.setLayoutParams(params);
                duidepageDot.addView(dot);
            }
            duidepageDot.getChildAt(0).setEnabled(true);
        }
    }
}
