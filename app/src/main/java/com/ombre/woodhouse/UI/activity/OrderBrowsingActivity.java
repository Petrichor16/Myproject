package com.ombre.woodhouse.UI.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.ItemOrederAdapter;
import com.ombre.woodhouse.Adapter.OrderBrowsingAdapter;
import com.ombre.woodhouse.Bean.Orders;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.OrderInterfaceService;
import com.ombre.woodhouse.MyView.SelectedLoadViewPager;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;

public class OrderBrowsingActivity extends AppCompatActivity {

    private TabLayout order_type;
    private ViewPager typeDataList;
    private SwipeRefreshLayout order_refresh;//
    private List<String> order_typeList;//订单 状态
    private List<RecyclerView> typeDataView;//页面布局

    OrderBrowsingAdapter OrderPagerAdapter;
    Loading_Dialog dialog;
    int CurrentItem;
    private void initViews(){
        typeDataList=(ViewPager)findViewById(R.id.typeDataList);
        order_type=(TabLayout)findViewById(R.id.order_type);
        order_refresh=(SwipeRefreshLayout)findViewById(R.id.order_refresh);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_browsing);
        initViews();
        dialog=new Loading_Dialog(this,R.style.dialog);
        Intent  intent=getIntent();
        CurrentItem= Integer.parseInt(intent.getStringExtra("CurrentItem"));
        typeDataList.setCurrentItem(CurrentItem);
        dialog.show();
        getOrderList(CurrentItem);

        typeDataList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {CurrentItem=typeDataList.getCurrentItem();}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        order_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderList(CurrentItem);
            }
        });
    }

    public  void onClick(View view){
        finish();
    }
    //初始化加载布局
    private void initLayout(){
        //设置tab的模式
        order_type.setTabMode(TabLayout.MODE_SCROLLABLE);
        //添加tab选项卡
        for (int i = 0; i < order_typeList.size(); i++) {
            order_type.addTab(order_type.newTab().setText(order_typeList.get(i)));
        }
        //把TabLayout和ViewPager关联起来
        order_type.setupWithViewPager(typeDataList);
        //实例化适配器
       OrderPagerAdapter= new OrderBrowsingAdapter(typeDataView, order_typeList);
        //给ViewPager绑定Adapter
        typeDataList.setAdapter(OrderPagerAdapter);
    }
    //获取相对应状态的订单
    private void getOrderList( final int position){
       // dialog.show();
        CurrentItem=position;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrderInterfaceService service=new OrderInterfaceService(OrderBrowsingActivity.this);
                Map<String,String > map=new HashMap<String, String>();
                map.put("memberPhone",new SharePreferences_getData(OrderBrowsingActivity.this).getUserID());
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                order_refresh.setRefreshing(false);
                                dialog.dismiss();
                                Toast.makeText(OrderBrowsingActivity.this,"网络不给力",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                order_refresh.setRefreshing(false);
                                dialog.dismiss();
                                List<Orders> ordersList=new analysisXMLHelper().analysisOrderDetails(result);
                                order_typeList=new ArrayList<>();
                                typeDataView=new ArrayList<>();
                                order_typeList.add("全部");
                                order_typeList.add("待付款");
                                order_typeList.add("待发货");
                                order_typeList.add("待收货");
                                order_typeList.add("待评论");
                                order_typeList.add("退/换货");
                                for(int i=0;i<order_typeList.size();i++){
                                    RecyclerView recyclerView = new RecyclerView(OrderBrowsingActivity.this);
                                    GridLayoutManager layoutManager = new GridLayoutManager(OrderBrowsingActivity.this, 1);
                                    recyclerView.setLayoutManager(layoutManager);
                                    Orders orders;
                                List<Orders> orderses=new ArrayList<Orders>();//用于显示订单信息
                                for(int j=0;j<ordersList.size();j++) {
                                    orders=new Orders();
                                    if (order_typeList.get(i).equals("全部")) {
                                        orders=ordersList.get(j);
                                    }
                                    if (order_typeList.get(i).equals("待付款")) {
                                        if (ordersList.get(j).getOrderStatus()==0)
                                            orders=ordersList.get(j);
                                    }
                                    if (order_typeList.get(i).equals("待收货")) {
                                        if (ordersList.get(j).getOrderStatus()==2)
                                            orders=ordersList.get(j);
                                    }
                                    if (order_typeList.get(i).equals("待发货")) {
                                        if (ordersList.get(j).getOrderStatus()==1)
                                            orders=ordersList.get(j);
                                    }
                                    if (order_typeList.get(i).equals("待评论")) {
                                        if (ordersList.get(j).getOrderStatus()==3)
                                            orders=ordersList.get(j);
                                    }
                                    if (order_typeList.get(i).equals("退/换货")) {
                                        if (ordersList.get(j).getOrderStatus()==4||ordersList.get(j).getOrderStatus()==5)
                                            orders=ordersList.get(j);
                                    }
                                    if(orders.getId()!=null){
                                        orderses.add(orders);
                                    }
                                }
                               if(orderses.size()>0){
                                ItemOrederAdapter myAdapter=new ItemOrederAdapter(OrderBrowsingActivity.this,orderses,order_typeList.get(i));
                                recyclerView.setAdapter(myAdapter);}
                                typeDataView.add(recyclerView);
                                }
                                initLayout();
                                typeDataList.setCurrentItem(CurrentItem);
                            }
                        });
                    }
                };
                service.getStateOrder(map,callbacks);
            }
        }).start();
    }

    @Override
    protected void onResume() {
        dialog.show();
        getOrderList(CurrentItem);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
         finish();
         super.onBackPressed();//注释掉这行,back键不退出activity
    }
}
