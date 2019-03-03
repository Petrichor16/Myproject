package com.ombre.woodhouse.UI.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.Comm_CardAdapter;
import com.ombre.woodhouse.Adapter.PartGoodsAdapter;
import com.ombre.woodhouse.Adapter.SowingmapAdapter;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Item.CommCarditem;
import com.ombre.woodhouse.Bean.Item.PartGoodsItem;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CommodityInterfaceService;
import com.ombre.woodhouse.MyView.AlphaTitleScrollView;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.UI.activity.GoodsBrowsingActivity;
import com.ombre.woodhouse.UI.activity.SearchGoodActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.Response;

import static com.ombre.woodhouse.Utils.IconColor_Change.setImageViewColor;

/**
 * Created by OMBRE on 2018/5/22.
 */

//首页
public class Fragment_FirstPage extends Fragment implements View.OnClickListener {

    private AppCompatActivity activity;
    private ViewPager mViewPager;
    private List<String> picSowingList;//图片资源
    private LinearLayout dotLayout;//轮播栏的小点
    private AlphaTitleScrollView firstpage_scrollview;
    private LinearLayout toolbar;
    private ImageView scanning_qrCode;//扫描二维码
    private EditText searchGoods;//搜索
    private FrameLayout all_goods;//所有商品
    private FrameLayout new_goods;//新品
    private FrameLayout hot_goods;//热门
    private FrameLayout Promotion_goods;//促销
    private LinearLayout layout_addView;//添加进度条
    private RecyclerView newComm_Exhibition;//显示部分新品
    private List<Commodity>  newDispayList;
    private RecyclerView hotComm_Exhibition;//显示部分人气单品
    private List<Commodity>  hotDispayList;
    private RecyclerView teComm_Exhibition;//显示部分特价商品
    private List<Commodity>  teDispayList;
    private RecyclerView maybeLike;//可能喜欢的
    private List<Commodity>  likeDispayList;
    private Timer timer;
    int prePosition=0;
    int i=0;
    private List<Commodity> commodityList=new ArrayList<>();
    StaggeredGridLayoutManager layoutManager;
    public static final int FISRTPAGECONNECTFAILS=1201;//网络连接失败
    public static final int FISRTPAGECONNECTSUCCESS=1202;//网络连接失败
    Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case FISRTPAGECONNECTFAILS:
                    Toast.makeText(activity,"网络不给力",Toast.LENGTH_SHORT).show();
                    break;
                case FISRTPAGECONNECTSUCCESS:
                    commodityList=new analysisXMLHelper().analysisAllCommodity(msg.obj.toString());
                    if(commodityList!=null&&commodityList.size()>0)
                        screenCommList();
                    break;
            }
        }
    };

    public Fragment_FirstPage() {
    }

    public Fragment_FirstPage(AppCompatActivity activity) {
        this.activity = activity;
    }

    //初始化控件
    private void init(View view){
        mViewPager=(ViewPager)view.findViewById(R.id.my_sowingmap);
        dotLayout = (LinearLayout) view.findViewById(R.id.dotLayout);
        firstpage_scrollview=(AlphaTitleScrollView)view.findViewById(R.id.firstpage_scrollview);
        toolbar=(LinearLayout)view.findViewById(R.id.title);
        scanning_qrCode=(ImageView)view.findViewById(R.id.scanning_qrCode);
        all_goods=(FrameLayout)view.findViewById(R.id.all_goods);
        new_goods=(FrameLayout)view.findViewById(R.id.new_goods);
        hot_goods=(FrameLayout)view.findViewById(R.id.hot_goods);
        Promotion_goods=(FrameLayout)view.findViewById(R.id.Promotion_goods);
        newComm_Exhibition=(RecyclerView)view.findViewById(R.id.newComm_Exhibition);//显示部分新品
        hotComm_Exhibition=(RecyclerView)view.findViewById(R.id.hotComm_Exhibition);//显示部分人气单品
        teComm_Exhibition=(RecyclerView)view.findViewById(R.id.teComm_Exhibition);//显示部分特价商品
        maybeLike=(RecyclerView) view.findViewById(R.id.maybeLike);//可能喜欢的
        searchGoods=(EditText)view.findViewById(R.id.searchGoods);
        layout_addView= (LinearLayout) view.findViewById(R.id.layout_addView);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_firstpage,container,false);
        init(view);
        new SharePreferences_Manager(getActivity()).saveFirstClassification("");
        initTitleContent();
        picSowingList=new ArrayList<>();
        picSowingList.add("/img/a.JPG");  picSowingList.add("/img/c.JPG");
        picSowingList.add("/img/b.JPG");picSowingList.add("/img/d.JPG");
        if (picSowingList.size()>0)
               realizeSowingmap();
        getCommList();
        maybeLike.setHasFixedSize(true);
        maybeLike.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
        all_goods.setOnClickListener(this);
        new_goods.setOnClickListener(this);
        hot_goods.setOnClickListener(this);
        Promotion_goods.setOnClickListener(this);
        scanning_qrCode.setOnClickListener(this);
        searchGoods.setOnClickListener(this);

        return view;
    }
    //初始化标题栏状态
    private void initTitleContent(){
        setImageViewColor(scanning_qrCode,R.color.colorWhite);
        firstpage_scrollview.setTitleAndHead(toolbar,null, mViewPager,30);
        toolbar.getBackground().setAlpha(30);
    }
    //实现图片轮播
    private void realizeSowingmap(){
        initDots();
        mViewPager.setPageMargin(10);//设置页与页之间的间距
        mViewPager.setOffscreenPageLimit(1);//限定预加载的页面个数
        SowingmapAdapter myAdapter=new SowingmapAdapter(activity,picSowingList);
        myAdapter.setOnItemClickListener(new SowingmapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {}
        });
        mViewPager.setAdapter(myAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if(picSowingList.size()>0) {
                    dotLayout.getChildAt(prePosition).setEnabled(false);
                    dotLayout.getChildAt(position % picSowingList.size()).setEnabled(true);
                    prePosition = position % picSowingList.size();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (null != timer) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);//用来制定初始化的页面
                    }
                });
            }
        }, 0, 4000);
    }
    //添加轮播栏的圆点
    private void initDots() {
        if (picSowingList.size()>0) {
            if (null != dotLayout) {
                dotLayout.removeAllViews();//清除所有的布局控件
            }
            for (int i = 0; i < picSowingList.size(); i++) {
                ImageView dot = new ImageView(activity);
                dot.setEnabled(false);
                dot.setImageResource(R.drawable.dot);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 10;
                dot.setLayoutParams(params);
                dotLayout.addView(dot);
            }
            dotLayout.getChildAt(0).setEnabled(true);
        }
    }
    //获取商品
    private void getCommList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommodityInterfaceService service=new CommodityInterfaceService(activity);
                Map<String,String> map=new HashMap<>();
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Message msg=new Message();
                        msg.what=FISRTPAGECONNECTFAILS;
                        mHandler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        String result=response.body().string();
                        Message msg=new Message();
                        msg.what=FISRTPAGECONNECTSUCCESS;
                        msg.obj=result;
                        mHandler.sendMessage(msg);
                    }
                };
                service.getFirspageCommodity(map,callbacks);
            }
        }).start();
    }
    //筛选展示的商品列表
    private void screenCommList(){
        Commodity commodity;
        if(commodityList.size()>0) {
            newDispayList = new ArrayList<>();
            hotDispayList = new ArrayList<>();
            teDispayList = new ArrayList<>();
            likeDispayList = new ArrayList<>();
            //新品
            for (int i = 0; i < commodityList.size(); i++) {
                commodity = commodityList.get(i);
                if (commodity.getStatus().equals("新品") || commodity.getStatus().equals("特价新品"))
                    newDispayList.add(commodity);
                if (commodity.getStatus().equals("特价") || commodity.getStatus().equals("特价新品"))
                    teDispayList.add(commodity);
                if (commodity.getStatus().equals("特价新品"))
                    likeDispayList.add(commodity);
                if (commodity.getSale_value() > 100)
                    hotDispayList.add(commodity);
            }
        }
        loadRecycleView();
    }
    //加载recycleview数据
    private void loadRecycleView(){
        //新品
        PartGoodsItem item;
      layoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        newComm_Exhibition.setLayoutManager(layoutManager);
        List<PartGoodsItem> partGoodsItems=new ArrayList<>();
        for(int i=0;i<20&&newDispayList.size()>i;i++){
            Commodity  comm=newDispayList.get(i);
            item=new PartGoodsItem(comm.getId(),comm.getPicture()
                    ,comm.getMerName());
            partGoodsItems.add(item);
        }
        PartGoodsAdapter myAdapterNew=new PartGoodsAdapter(activity,partGoodsItems);
        newComm_Exhibition.setAdapter(myAdapterNew);

        //人气
        layoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        hotComm_Exhibition.setLayoutManager(layoutManager);
        List<PartGoodsItem> partGoodsItems1=new ArrayList<>();
        for(int i=0;i<20&&hotDispayList.size()>i;i++){
            Commodity  comm=hotDispayList.get(i);
            item=new PartGoodsItem(comm.getId(),comm.getPicture()
                    ,comm.getMerName());
            partGoodsItems1.add(item);
        }
        PartGoodsAdapter myAdapterHot=new PartGoodsAdapter(activity,partGoodsItems1);
        hotComm_Exhibition.setAdapter(myAdapterHot);

        //特价
        layoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        teComm_Exhibition.setLayoutManager(layoutManager);
        List<PartGoodsItem>  partGoodsItems2=new ArrayList<>();
        for(int i=0;i<15&&teDispayList.size()>i;i++){
            Commodity  comm=teDispayList.get(i);
            item=new PartGoodsItem(comm.getId(),comm.getPicture()
                    ,comm.getMerName());
            partGoodsItems2.add(item);
        }
        PartGoodsAdapter myAdapterTE=new PartGoodsAdapter(activity,partGoodsItems2);
        teComm_Exhibition.setAdapter(myAdapterTE);


        //可能喜欢

        layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        maybeLike.setLayoutManager(layoutManager);
      List<CommCarditem>  commCarditemList1=new ArrayList<>();
        for(int i=0;i<33&&likeDispayList.size()>i;i++){
            Commodity  comm=likeDispayList.get(i);
            CommCarditem commCardItem=new CommCarditem(comm.getId(),comm.getPicture()
                    ,comm.getStatus(),comm.getMerName(),comm.getPrice(),comm.getSale_value());
            commCarditemList1.add(commCardItem);
        }
        Comm_CardAdapter myAdapterLike=new Comm_CardAdapter(activity,commCarditemList1);
        maybeLike.setAdapter(myAdapterLike);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_goods:
                Intent intent_all=new Intent(activity, GoodsBrowsingActivity.class);
                new SharePreferences_Manager(activity).saveSecondClassification("全部商品");
                activity.startActivity(intent_all);
                break;
            case R.id.new_goods://新品
                Intent intent_new=new Intent(activity, GoodsBrowsingActivity.class);
                new SharePreferences_Manager(activity).saveSecondClassification("新品到货");
                activity.startActivity(intent_new);
                break;
            case R.id.hot_goods://人气
                Intent intent_hot=new Intent(activity, GoodsBrowsingActivity.class);
                new SharePreferences_Manager(activity).saveSecondClassification("人气单品");
                activity.startActivity(intent_hot);
                break;
            case R.id.Promotion_goods://促销
                Intent intent_Promotion=new Intent(activity, GoodsBrowsingActivity.class);
                new SharePreferences_Manager(activity).saveSecondClassification("特价促销");
                activity.startActivity(intent_Promotion);
                break;
            case R.id.scanning_qrCode://进入3d店面
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://720yun.com/t/85lw7dz8ewcd4qrdt4");
                intent1.setData(content_url);
                startActivity(intent1);
                break;
            case R.id.searchGoods://搜索
                Intent intent_search=new Intent(activity, SearchGoodActivity.class);
                activity.startActivity(intent_search);
                break;
        }
    }
}
