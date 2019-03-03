package com.ombre.woodhouse.UI.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.ombre.woodhouse.Adapter.Comm_CardAdapter;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Item.CommCarditem;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CommodityInterfaceService;
import com.ombre.woodhouse.MyView.LoadmoreScrollView;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;

//商品浏览
public class GoodsBrowsingActivity extends AppCompatActivity {
    private TextView browsingType;//分类名称
    private String browsingTypeStr;
    private LoadmoreScrollView browsingScroll;
    private LinearLayout noWifi;//没有网络页面
    private ImageView browsing_nocomm;//没有商品
    private TextView default_sort;//默认排序
    private TextView sales_priority;//销量优先
    private TextView  price_sortType;//价格排序
    private Spinner price_sorting;//sales_priority价格排序栏
    private RecyclerView goodsRecycleView;//商品展示列表
    private JellyRefreshLayout browsingComm_refresh;//刷新
    private List<CommCarditem> commCarditemList;//商品列表
   private SpinKitView comm_Loading;//加载进度条
    private LinearLayout layout_add;//需要添加布局的layout
    SharePreferences_getData preferences_getData;
    SharePreferences_Manager preferences_manager;
    List<Commodity> commodityList;
    private int pageNo=1;//页码
    boolean flag=true;
    View view_layout=null;//需要添加的布局

    public static final int GOODBROWSINGCONNECTFAILS=1101;//网络连接失败
    public static final int GOODBROWSINGCONNECTSUCCESS=1102;//网络连接成功
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case GOODBROWSINGCONNECTFAILS:
                    comm_Loading.setVisibility(View.INVISIBLE);
                    noWifi.setVisibility(View.VISIBLE);
                    if(browsingComm_refresh.isRefreshing()){
                        browsingComm_refresh.finishRefreshing();
                    }
                    Toast.makeText(GoodsBrowsingActivity.this,"网络不给力",Toast.LENGTH_SHORT).show();
                    break;
                case GOODBROWSINGCONNECTSUCCESS:
                    updateUI(msg.obj.toString());
                    break;
            }
        }
    };
    private void initViews(){
        browsingType=(TextView)findViewById(R.id.browsingType);
        price_sorting=(Spinner)findViewById(R.id.price_sorting);
        goodsRecycleView=(RecyclerView)findViewById(R.id.goodsRecycleView);
        default_sort=(TextView)findViewById(R.id.default_sort);
        sales_priority=(TextView)findViewById(R.id.sales_priority);
        price_sortType=(TextView)findViewById(R.id.price_sortType);
        browsingComm_refresh=(JellyRefreshLayout)findViewById(R.id.browsingComm_refresh);
        comm_Loading=(SpinKitView)findViewById(R.id.comm_Loading);
        browsingScroll=(LoadmoreScrollView)findViewById(R.id.browsingScroll);
        noWifi=(LinearLayout)findViewById(R.id.noWifi);
        browsing_nocomm=(ImageView)findViewById(R.id.browsing_nocomm);
        layout_add=(LinearLayout)findViewById(R.id.layout_add);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_browsing);
        initViews();
         initUI();
        //刷新监听
        browsingComm_refresh.setRefreshListener(new JellyRefreshLayout.JellyRefreshListener() {
            @Override
            public void onRefresh(JellyRefreshLayout jellyRefreshLayout) {
                initFlag();
                getAllComm();
            }
        });
        //滑动到底部监听
        browsingScroll.setOnScrollToBottomLintener(new LoadmoreScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if(isBottom) {
                    if (flag ) {
                        LayoutInflater inflater3 = LayoutInflater.from(GoodsBrowsingActivity.this);
                        view_layout= inflater3.inflate(R.layout.item_foot,null);
                        layout_add.addView(view_layout);
                        pageNo++;
                        flag=false;
                        getAllComm();
                    }
                }
            }
        });
    }
    //
    private void initFlag(){
        commodityList=new ArrayList<>();
        pageNo=1;
        flag=true;
        if (view_layout != null)
            layout_add.removeView(view_layout);//移除布局
        view_layout=null;
    }
    //更新布局
    private void updateUI(String jsonData){
        noWifi.setVisibility(View.INVISIBLE);
        comm_Loading.setVisibility(View.INVISIBLE);
        if(browsingComm_refresh.isRefreshing()){
            browsingComm_refresh.finishRefreshing();
            Toast.makeText(GoodsBrowsingActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
        }
        List<Commodity> commodities=new ArrayList<>();
        commodities=new analysisXMLHelper().analysisAllCommodity(jsonData);
        if(commodities!=null&&commodities.size()>0) {
            if (view_layout != null)
                layout_add.removeView(view_layout);//移除布局
            if (commodities == null) {
                view_layout = LayoutInflater.from(GoodsBrowsingActivity.this).inflate(R.layout.item_isbottom, null);
                layout_add.addView(view_layout);
                flag = false;
            } else {
                if (commodities.size() < 20 || commodities.size() == 0) {
                    view_layout = LayoutInflater.from(GoodsBrowsingActivity.this).inflate(R.layout.item_isbottom, null);
                    layout_add.addView(view_layout);
                    flag = false;
                }
                if (commodities.size() > 0)
                    commodityList.addAll(commodities);
                if (commodities.size() == 20)
                    flag = true;
            }
            if (commodityList != null)
                initCommList();
        }
        else {
            browsingComm_refresh.setVisibility(View.INVISIBLE);
            browsing_nocomm.setVisibility(View.VISIBLE);

        }
    }
    //初始化
   private void initUI(){
       commodityList=new ArrayList<>();
       preferences_getData=new SharePreferences_getData(this);
       preferences_manager=new SharePreferences_Manager(this);
       initSpinner();
       goodsRecycleView.setHasFixedSize(true);
       goodsRecycleView.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
       browsingTypeStr=preferences_getData.getSecondClassification();
       if(!preferences_getData.getFirstClassification().equals(""))
           browsingType.setText(preferences_getData.getFirstClassification()+" - "+browsingTypeStr);
       else
           browsingType.setText(browsingTypeStr);
       preferences_manager.saveSortType("default");
       getAllComm();
    }
    //更新Recycleview
    private void initCommList() {
        String sortType = preferences_getData.getSortType();
            if (sortType.equals("default")) {//默认
                default_sort.setTextColor(getResources().getColor(R.color.colorBlack));
                sales_priority.setTextColor(getResources().getColor(R.color.colorGray_1));
                price_sortType.setTextColor(getResources().getColor(R.color.colorGray_1));
                price_sortType.setText("价格");

            }
                        if (sortType.equals("Sales_priority")) {//销量优先
                            sales_priority.setTextColor(getResources().getColor(R.color.colorBlack));
                            default_sort.setTextColor(getResources().getColor(R.color.colorGray_1));
                            price_sortType.setTextColor(getResources().getColor(R.color.colorGray_1));
                            price_sortType.setText("价格");
                        }
                        if (sortType.equals("Price_low_to_high")) {//价格从低到高
                            price_sortType.setTextColor(getResources().getColor(R.color.colorBlack));
                            sales_priority.setTextColor(getResources().getColor(R.color.colorGray_1));
                            default_sort.setTextColor(getResources().getColor(R.color.colorGray_1));
                            price_sortType.setText("价格从低到高");
                        }
                        if (sortType.equals("Price_high_to_low")) {//价格从高到低
                            price_sortType.setTextColor(getResources().getColor(R.color.colorBlack));
                            sales_priority.setTextColor(getResources().getColor(R.color.colorGray_1));
                            default_sort.setTextColor(getResources().getColor(R.color.colorGray_1));
                            price_sortType.setText("价格从高到低");
        }
        loadGoodsList();
    }
    //对下拉选择列表监听
    private void SpinnerOnclick(){
        price_sorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = (String)price_sorting.getItemAtPosition(position);//从spinner中获取被选择的数据
                initFlag();
                browsingScroll.fullScroll(ScrollView.FOCUS_UP);
                if(choice.equals("价格从低到高"))
                    preferences_manager.saveSortType("Price_low_to_high");
                if (choice.equals("价格从高到低"))
                    preferences_manager.saveSortType("Price_high_to_low");
                getAllComm();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void onClick_GoodsBrowsing(View view){
        switch (view.getId()){
            case R.id.goodsbrowsing_back:
                finish();
                break;
            case R.id.default_sort://默认排序
                preferences_manager.saveSortType("default");
                browsingScroll.fullScroll(ScrollView.FOCUS_UP);
                initFlag();
                getAllComm();
                break;
            case R.id.sales_priority://销量优先
                browsingScroll.fullScroll(ScrollView.FOCUS_UP);
                preferences_manager.saveSortType("Sales_priority");
                initFlag();
                getAllComm();
                break;
            case R.id.price_sortType://价格排序
                browsingScroll.fullScroll(ScrollView.FOCUS_UP);
               String choice= price_sorting.getSelectedItem().toString();
                initFlag();
                if(choice.equals("价格从低到高"))
                    preferences_manager.saveSortType("Price_low_to_high");
                else
                    preferences_manager.saveSortType("Price_high_to_low");
                getAllComm();
                price_sorting.performClick();
                SpinnerOnclick();
                break;
            case R.id.commReload://重新加载
                noWifi.setVisibility(View.INVISIBLE);
               initFlag();
                getAllComm();
                break;
        }
    }
    //初始化Spinner按钮
    private void initSpinner(){
        ArrayAdapter<String> typeAdapter;//适配器用于绑定数据源
        List<String> typeList;
        typeList=new ArrayList<>();
        typeList.add("价格从低到高");
        typeList.add("价格从高到低");
        typeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        price_sorting.setAdapter(typeAdapter);
    }
    //分页获取所有商品
    private void getAllComm(){
        noWifi.setVisibility(View.INVISIBLE);
        browsing_nocomm.setVisibility(View.INVISIBLE);
        comm_Loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommodityInterfaceService service=new CommodityInterfaceService(GoodsBrowsingActivity.this);
                Map<String,String> map=new HashMap<>();
                String firsttype=preferences_getData.getFirstClassification();
                String secondtype=preferences_getData.getSecondClassification();
                map.put("pageNo", String.valueOf(pageNo));
                String type=new String(preferences_getData.getSortType());
                map.put("sortType",type);
                map.put("firsttype",firsttype);
                map.put("secondtype",secondtype);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Message msg=new Message();
                        msg.what=GOODBROWSINGCONNECTFAILS;
                        handler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message msg=new Message();
                        msg.what=GOODBROWSINGCONNECTSUCCESS;
                        msg.obj=result;
                        handler.sendMessage(msg);
                    }
                };
                service.getAllCommodity(map,callbacks);
            }
        }).start();
    }
    //设置适配器
    private void loadGoodsList(){
        if(commodityList.size()==0)
        {
            browsingComm_refresh.setVisibility(View.INVISIBLE);
             browsing_nocomm.setVisibility(View.VISIBLE);
        }
        else{
            browsingComm_refresh.setVisibility(View.VISIBLE);
        commCarditemList=new ArrayList<>();
        CommCarditem commCardItem;
          for(int i=0;i<commodityList.size();i++){
              Commodity  comm=commodityList.get(i);
              commCardItem=new CommCarditem(comm.getId(),comm.getPicture()
                      ,comm.getStatus(),comm.getMerName(),comm.getPrice(),comm.getSale_value());
              commCarditemList.add(commCardItem);
          }
        StaggeredGridLayoutManager gridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        goodsRecycleView.setLayoutManager(gridLayoutManager);
        Comm_CardAdapter myAdapter=new Comm_CardAdapter(this,commCarditemList);
        goodsRecycleView.setAdapter(myAdapter);
        }
    }

}
