package com.ombre.woodhouse.UI.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ybq.android.spinkit.SpinKitView;
import com.ombre.woodhouse.Adapter.ClassificationAdapter;
import com.ombre.woodhouse.Adapter.GeneraltypeAdapter;
import com.ombre.woodhouse.Bean.Category;
import com.ombre.woodhouse.Bean.Item.ClassificationItem;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CategoryInterfaceService;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.UI.activity.GoodsBrowsingActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;

/**
 * Created by OMBRE on 2018/5/22.
 */

//分类
public class Fragment_Classification extends Fragment{

    private AppCompatActivity activity;
    private JellyRefreshLayout type_refresh;//刷新
    private RecyclerView general_type;//第一级分类布局
    private List<String> general_typeList;//第一级分类列表数列
    private TextView txt_generalType;//第一级分类名称
    private RecyclerView allCalssificationView;//第一级分类全部
    private SpinKitView typeLoading;//加载
    private List<ClassificationItem> allCalssificationViewLsit;//第一级分类全部列表

    SharePreferences_Manager preferences_manager;
    SharePreferences_getData preferences_getData;

    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager gridLayoutManager;

    List<Category> secondcategoryList;//第二级分类数列
    List<Category>  categoryList;//第一级分类数列
    public Fragment_Classification(AppCompatActivity activity) {
        this.activity = activity;
    }

    private final static int CALSSIFICATIONCONNECTFAILS=2201;//网络连接失败
    private final static int CALSSIFICATIONCONNECTSUCCESS_FIRST=2202;//第一次分类网络连接成功
    private final static int CALSSIFICATIONCONNECTSUCCESS_SECOND=2203;//第二次分类网络连接成功
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CALSSIFICATIONCONNECTFAILS:
                    typeLoading.setVisibility(View.INVISIBLE);
                    if(type_refresh.isRefreshing()){
                        type_refresh.finishRefreshing();//使刷新标记消失
                         }
                    Toast.makeText(activity,"网络不给力",Toast.LENGTH_SHORT).show();
                    break;
                case CALSSIFICATIONCONNECTSUCCESS_FIRST:
                    typeLoading.setVisibility(View.INVISIBLE);
                    categoryList=new analysisXMLHelper().analysisCategory(msg.obj.toString());
                    if (categoryList!=null)
                        initGnaeralTypeView(categoryList);
                    break;
                case CALSSIFICATIONCONNECTSUCCESS_SECOND:
                    typeLoading.setVisibility(View.INVISIBLE);
                    if(type_refresh.isRefreshing()){
                    type_refresh.finishRefreshing();//使刷新标记消失
                    Toast.makeText(activity, "刷新成功", Toast.LENGTH_SHORT).show();}
                    secondcategoryList= new analysisXMLHelper().analysisCategory(msg.obj.toString());
                    initAllClassificationView();
                    break;
            }
        }
    };
    private void init(View view){
        type_refresh=(JellyRefreshLayout)view.findViewById(R.id.type_refresh);
        general_type=(RecyclerView)view.findViewById(R.id.general_type);
        txt_generalType=(TextView)view.findViewById(R.id.txt_generalType);
        allCalssificationView=(RecyclerView)view.findViewById(R.id.allCalssificationView);
        typeLoading=(SpinKitView)view.findViewById(R.id.typeLoading);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_classfication,container,false);
        init(view);
        preferences_getData=new SharePreferences_getData(activity);
        preferences_manager=new SharePreferences_Manager(activity);
        refreshTypeListener();
        getFirstClassification();
        return view;
    }


    //初始化二级分类的数据----全部
    private void  initAllClassificationView() {
        ClassificationItem item;
        allCalssificationView.setHasFixedSize(true);
        allCalssificationView.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
        allCalssificationViewLsit = new ArrayList<>();
        if(secondcategoryList!=null){
            if (secondcategoryList.size() > 0) {
                for (int i = 0; i < secondcategoryList.size(); i++) {
                    item = new ClassificationItem(secondcategoryList.get(i).getCategoryName(),secondcategoryList.get(i).getCategoryPic());//secondcategoryList.get(i).getCategoryPic() "/img/IMG_5358.JPG"
                    allCalssificationViewLsit.add(item);
                }
            }
        }
        gridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        allCalssificationView.setLayoutManager(gridLayoutManager);
        ClassificationAdapter myAdapterAll=new ClassificationAdapter(activity,allCalssificationViewLsit);
        myAdapterAll.setOnItemClickListener(new ClassificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent in = new Intent(activity, GoodsBrowsingActivity.class);
                new SharePreferences_Manager(activity).saveSecondClassification(allCalssificationViewLsit.get(position).getClassificationName());
                activity.startActivity(in);
            }
        });
        allCalssificationView.setAdapter(myAdapterAll);
    }

    //获取第二级分类
    private void getSecondClassification(final Map<String,String> map){
                typeLoading.setVisibility(View.VISIBLE);
                CategoryInterfaceService categoryInterfaceService=new CategoryInterfaceService(activity);
                Callbacks  callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message message=new Message();
                        message.what=CALSSIFICATIONCONNECTSUCCESS_SECOND;
                        message.obj=result;
                        handler.sendMessage(message);
                    }
                };
                categoryInterfaceService.getSecondCategory(map,callbacks);
    }
    //获取第一分类
    private void getFirstClassification(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CategoryInterfaceService categoryInterfaceService=new CategoryInterfaceService(activity);
                Callbacks  callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message message=new Message();
                        message.what=CALSSIFICATIONCONNECTFAILS;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message message=new Message();
                        message.what=CALSSIFICATIONCONNECTSUCCESS_FIRST;
                        message.obj=result;
                        handler.sendMessage(message);
                    }
                };
                categoryInterfaceService.getFirstCategory(callbacks);
            }
        }).start();
    }
    //初始化一级分类的数据
    private void initGnaeralTypeView(final List<Category> categories){
        general_typeList=new ArrayList<>();
        if(categories.size()>0) {
            for (int i = 0; i < categories.size(); i++) {
                general_typeList.add(categories.get(i).getCategoryName());
            }
        }
        //初始化页面
        if(general_typeList!=null&&(general_typeList.size()>0)){
            txt_generalType.setText("──   全部"+general_typeList.get(0)+"   ──");
            preferences_manager.saveFirstClassification(general_typeList.get(0));//保存一级分类
            preferences_manager.saveClassificationID(categories.get(0).getId());//保存分类父类ID，用以查询二级分类
            Map<String,String> initmap=new HashMap<>();
            initmap.put("parentID",String.valueOf(categories.get(0).getId()));
            getSecondClassification(initmap);
        }
        linearLayoutManager=new LinearLayoutManager(activity);
        general_type.setLayoutManager(linearLayoutManager);
        GeneraltypeAdapter myAdapter=new GeneraltypeAdapter(activity,general_typeList);
        //给适配器添加监听事件
        myAdapter.setOnItemClickListener(new GeneraltypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                secondcategoryList=new ArrayList<>();
                preferences_manager.saveFirstClassification(general_typeList.get(position));//保存一级分类
                txt_generalType.setText("──   全部"+general_typeList.get(position)+"   ──");
                preferences_manager.saveClassificationID(categories.get(position).getId());//保存分类父类ID，用以查询二级分类
                Map<String,String> map=new HashMap<>();
                map.put("parentID",String.valueOf(categories.get(position).getId()));
                getSecondClassification(map);
            }
        });
        general_type.setAdapter(myAdapter);
    }
    //刷新监听
    private void refreshTypeListener(){
        type_refresh.setRefreshListener(new JellyRefreshLayout.JellyRefreshListener() {
            @Override
            public void onRefresh(JellyRefreshLayout jellyRefreshLayout) {
                Map<String, String> map = new HashMap<>();
                map.put("parentID", preferences_getData.getClassificationID());
                getSecondClassification(map);
            }
        });
    }
}
