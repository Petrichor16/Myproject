package com.ombre.woodhouse.UI.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.Comm_CardAdapter;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Footprint;
import com.ombre.woodhouse.Bean.Item.CommCarditem;
import com.ombre.woodhouse.DB.DBManager.Address_Manager;
import com.ombre.woodhouse.DB.DBManager.Footprint_Manager;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CollectInterfaceService;
import com.ombre.woodhouse.InterfaceService.CommodityInterfaceService;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mlxy.utils.T;
import okhttp3.Call;
import okhttp3.Response;

public class CollectAndFootGoodsActivity extends AppCompatActivity {

    private RecyclerView goodsList;
    private TextView Typename;
    private TextView clear;//清空浏览记录
    String type;
    SharePreferences_getData preferences_getData;
    List<Commodity> commodityList;
    Thread myThread;
    Loading_Dialog dialog;
    Comm_CardAdapter myAdapter;
    List<CommCarditem> commCarditemList;
   private SwipeRefreshLayout  collectRefresh;//刷新
    private void init(){
        goodsList=(RecyclerView) findViewById(R.id.goodsList);
        Typename=(TextView)findViewById(R.id.Typename);
        collectRefresh=(SwipeRefreshLayout)findViewById(R.id.collectRefresh);
        clear=(TextView)findViewById(R.id.clear);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_and_foot_goods);
        init();
        preferences_getData=new SharePreferences_getData(CollectAndFootGoodsActivity.this);
        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        dialog=new Loading_Dialog(this,R.style.dialog);
        loadLayout();

        collectRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLayout();
            }
        });
    }

    //加载布局
    private void loadLayout(){
        goodsList.setHasFixedSize(true);
        goodsList.setNestedScrollingEnabled(false);
        if(type.equals("collect")){
            Typename.setText("我的收藏");
            clear.setVisibility(View.INVISIBLE);
            getCollectGoods();
        }
        else {
            clear.setVisibility(View.VISIBLE);
            Typename.setText("浏览记录");
            List<Footprint> footprintList=new Footprint_Manager(this).selectFootprint(new SharePreferences_getData(this).getUserID());
            commodityList=new ArrayList<>();
            dialog.show();
            if (footprintList!=null) {
                getFootprintGoods(footprintList);
            }
        }
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.collect_back:
                finish();
                break;
            case R.id.clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog alertDialog;
                alertDialog= builder.setTitle("提示")
                        .setMessage("确定清空浏览记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Footprint_Manager(CollectAndFootGoodsActivity.this).deleteFootprint("userPhone=?",new String []{preferences_getData.getUserID()});
                                commCarditemList.clear();
                                goodsList.removeAllViews();
                                myAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消",null).create();
                alertDialog.show();
                break;
        }
    }
    //获取浏览历史商品
    private void getFootprintGoods(final List<Footprint> footprintList){
        myThread= new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                for (int i=0;i<footprintList.size();i++){
                    map.put("goodsId"+i,footprintList.get(i).getGoodsID());
                }
                map.put("size", String.valueOf(footprintList.size()));
                CommodityInterfaceService service=new CommodityInterfaceService(CollectAndFootGoodsActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                collectRefresh.setRefreshing(false);
                                Toast.makeText(getApplicationContext(),"网络不给力",Toast.LENGTH_SHORT).show();
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
                                collectRefresh.setRefreshing(false);
                                commodityList = new analysisXMLHelper().analysisAllCommodity(result);
                                if(commodityList!=null)
                                    loadGoodsList();
                            }
                        });
                    }
                };
                service.getFootprintGoods(map,callbacks);
            }
        });myThread.start();
    }

    //获取收藏商品
    private void  getCollectGoods(){
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("userPhone", preferences_getData.getUserID());
                CollectInterfaceService service = new CollectInterfaceService(CollectAndFootGoodsActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                collectRefresh.setRefreshing(false);
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
                                collectRefresh.setRefreshing(false);
                                commodityList=new analysisXMLHelper().analysisAllCommodity(result);//获取对象
                                if(commodityList!=null)
                                    loadGoodsList();
                            }
                        });
                    }
                };
                service.LoadAllCollectGoods(map,callbacks);
            }
        }).start();
    }
    //加载商品
    private void loadGoodsList(){
        commCarditemList =new ArrayList<>();
            CommCarditem commCardItem;
            for(int i=0;i<commodityList.size();i++){
                Commodity  comm=commodityList.get(i);
                commCardItem=new CommCarditem(comm.getId(),comm.getPicture()
                        ,comm.getStatus(),comm.getMerName(),comm.getPrice(),comm.getSale_value());
                commCarditemList.add(commCardItem);
            }
            StaggeredGridLayoutManager gridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        goodsList.setLayoutManager(gridLayoutManager);
        myAdapter=new Comm_CardAdapter(this,commCarditemList);
        goodsList.setAdapter(myAdapter);
    }

    @Override
    protected void onResume() {
        loadLayout();
        super.onResume();
    }
}
