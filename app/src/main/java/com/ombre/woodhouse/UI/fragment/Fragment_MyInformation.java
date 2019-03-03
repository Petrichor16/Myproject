package com.ombre.woodhouse.UI.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.Comm_CardAdapter;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Footprint;
import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.Bean.Item.CommCarditem;
import com.ombre.woodhouse.Bean.Member;
import com.ombre.woodhouse.DB.DBManager.Footprint_Manager;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CollectInterfaceService;
import com.ombre.woodhouse.InterfaceService.CommodityInterfaceService;
import com.ombre.woodhouse.InterfaceService.MemberInterfaceService;
import com.ombre.woodhouse.MyView.AlphaTitleScrollView;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.UI.activity.AddressActivity;
import com.ombre.woodhouse.UI.activity.CollectAndFootGoodsActivity;
import com.ombre.woodhouse.UI.activity.GoodsBrowsingActivity;
import com.ombre.woodhouse.UI.activity.LoginActivity;
import com.ombre.woodhouse.UI.activity.MyInformationActivity;
import com.ombre.woodhouse.UI.activity.OrderBrowsingActivity;
import com.ombre.woodhouse.UI.activity.ServicefeedbackActivity;
import com.ombre.woodhouse.UI.activity.SettingActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.ombre.woodhouse.Utils.IconColor_Change.setImageViewColor;

/**
 * Created by OMBRE on 2018/5/22.
 */
//我的信息主页
public class Fragment_MyInformation extends Fragment {
    private ImageView person_setting;//设置
    private ImageView my_qrCode;//商城店面

    private AlphaTitleScrollView myinfo_scrollview;//scrollview
    private LinearLayout toolbar;//标题栏
   private LinearLayout address_manager;//地址管理
    private FrameLayout person_infoColumn;//个人信息栏
    private TextView btn_ls;//登录注册按钮
    private LinearLayout my_infoPhoto;//显示头像栏
    private TextView  nickname;//昵称
    private TextView user_phone;//电话号码
    private LinearLayout Pending_payment; //待付款
    private LinearLayout Pending_delivery; //待发货
    private LinearLayout ReceiveGoods; //待收货
    private LinearLayout Pending_evaluated; //待评价
    private LinearLayout Return_goods; //退货
   private RecyclerView  infoHotgoods;//推荐商品
    private LinearLayout myoders;
    private LinearLayout footprint;//足迹
    private TextView footprintNum;//足迹数量
    private LinearLayout myCollection;//收藏
    private TextView myCollectionNum;//收藏数量
    private LinearLayout Service_feedback;//服务反馈
    SharePreferences_getData preferences_getData;//获取登录状态或用户账号等
    private AppCompatActivity activity;
    private boolean loginState;
    Intent intent_login;
    Intent intent_oder;
    List<CommCarditem> commCarditemList;
    List<Commodity> commodityList;
    public  Fragment_MyInformation(){
        super();
    }
    public Fragment_MyInformation(AppCompatActivity activity) {
        this.activity = activity;
    }

    private void initViews(View view){
        Pending_payment=(LinearLayout)view.findViewById(R.id.Pending_payment);
        Pending_delivery=(LinearLayout)view.findViewById(R.id.Pending_delivery);
        ReceiveGoods=(LinearLayout)view.findViewById(R.id.ReceiveGoods);
        Pending_evaluated=(LinearLayout)view.findViewById(R.id.Pending_evaluated);
        Return_goods=(LinearLayout)view.findViewById(R.id.Return_goods);
        footprint=(LinearLayout)view.findViewById(R.id.footprint);
        footprintNum=(TextView) view.findViewById(R.id.footprintNum);
        myCollection=(LinearLayout)view.findViewById(R.id.myCollection);
        myCollectionNum=(TextView) view.findViewById(R.id.myCollectionNum);
        person_setting=(ImageView)view.findViewById(R.id.person_setting);
        my_qrCode=(ImageView)view.findViewById(R.id.my_qrCode);
        myinfo_scrollview=(AlphaTitleScrollView)view.findViewById(R.id.myinfo_scrollview);
        toolbar=(LinearLayout)view.findViewById(R.id.person_title);
        person_infoColumn=(FrameLayout)view.findViewById(R.id.person_infoColumn);
        btn_ls=(TextView)view.findViewById(R.id.btn_ls);
        my_infoPhoto=(LinearLayout) view.findViewById(R.id.my_infoPhoto);
        myoders=(LinearLayout)view.findViewById(R.id.myoders);
        nickname=(TextView)view.findViewById(R.id.nickname);
        user_phone=(TextView)view.findViewById(R.id.user_phone);
        address_manager=(LinearLayout)view.findViewById(R.id.address_manager);
        Service_feedback=(LinearLayout)view.findViewById(R.id.Service_feedback);
        infoHotgoods=(RecyclerView)view.findViewById(R.id.infoHotgoods);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_myinformation,container,false);
        initViews(view);
        preferences_getData=new SharePreferences_getData(activity);
        initViewCotent();
        btn_lsOnclick();
        intent_oder=new Intent(activity, OrderBrowsingActivity.class);
        Pending_payment.setOnClickListener(new MyListener());
        Pending_delivery.setOnClickListener(new MyListener());
        ReceiveGoods.setOnClickListener(new MyListener());
        Pending_evaluated.setOnClickListener(new MyListener());
        Return_goods.setOnClickListener(new MyListener());
        person_setting.setOnClickListener(new MyListener());
        myoders.setOnClickListener(new MyListener());
        address_manager.setOnClickListener(new MyListener());
        person_infoColumn.setOnClickListener(new MyListener());
        my_infoPhoto.setOnClickListener(new MyListener());
        footprint.setOnClickListener(new MyListener());
        myCollection.setOnClickListener(new MyListener());
        Service_feedback.setOnClickListener(new MyListener());
        my_qrCode.setOnClickListener(new MyListener());
        loginState=new SharePreferences_getData(activity).getLoginState();
        intent_login=new Intent(activity,LoginActivity.class);
        getUserInfo();
        infoHotgoods.setHasFixedSize(true);
        infoHotgoods.setNestedScrollingEnabled(false);
        return view;
    }

    //定义一个监听接口类
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.person_setting://设置
                    Intent intent_setting=new Intent(activity, SettingActivity.class);
                    startActivity(intent_setting);
                    break;
                case R.id.my_qrCode://设置
                    Intent intent1 = new Intent();
                    intent1.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://720yun.com/t/85lw7dz8ewcd4qrdt4");
                    intent1.setData(content_url);
                    startActivity(intent1);
                    break;
                case R.id.myoders://我的订单
                    if (loginState){
                        intent_oder.putExtra("CurrentItem","0");
                    startActivity(intent_oder);}
                    else
                        startActivity(intent_login);
                    break;
                case R.id.address_manager://地址管理
                    if(loginState){
                    Intent intent_address=new Intent(activity, AddressActivity.class);
                    intent_address.putExtra("type","");
                    activity.startActivity(intent_address);}
                    else
                        startActivity(intent_login);
                    break;
                case R.id.my_infoPhoto:
                    Intent intent_info=new Intent(activity, MyInformationActivity.class);
                    activity.startActivity(intent_info);
                    break;
                case R.id.Pending_payment://待付款
                    if (loginState){
                        intent_oder.putExtra("CurrentItem","1");
                        startActivity(intent_oder);}
                    else
                        startActivity(intent_login);
                    break;
                case R.id.Pending_delivery://待发货
                    if (loginState){
                        intent_oder.putExtra("CurrentItem","2");
                        startActivity(intent_oder);}
                    else
                        startActivity(intent_login);
                    break;
                case R.id.ReceiveGoods://待收货
                    if (loginState){
                        intent_oder.putExtra("CurrentItem","3");
                        startActivity(intent_oder);}
                    else
                        startActivity(intent_login);
                    break;
                case R.id.Pending_evaluated://待评价
                    if (loginState){
                        intent_oder.putExtra("CurrentItem","4");
                        startActivity(intent_oder);}
                    else
                        startActivity(intent_login);
                    break;
                case R.id.Return_goods://退换货
                    if (loginState){
                        intent_oder.putExtra("CurrentItem","5");
                        startActivity(intent_oder);}
                    else
                        startActivity(intent_login);
                    break;
                case  R.id.footprint://浏览记录
                    if (loginState) {
                    Intent intent_foot=new Intent(activity, CollectAndFootGoodsActivity.class);
                    intent_foot.putExtra("type","footprint");
                    activity.startActivity(intent_foot);
                    } else
                        startActivity(intent_login);
                    break;
                case  R.id.myCollection://收藏
                    if (loginState) {
                        Intent intent_collect = new Intent(activity, CollectAndFootGoodsActivity.class);
                        intent_collect.putExtra("type", "collect");
                        activity.startActivity(intent_collect);
                    } else
                        startActivity(intent_login);
                    break;
                case  R.id.Service_feedback://反馈
                    Intent intent_sev=new Intent(activity,ServicefeedbackActivity.class);
                    startActivity(intent_sev);
                    break;
            }
        }
    }


    //登录注册按钮监听
    private void btn_lsOnclick(){
        btn_ls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
            }
        });
    }
    //
    private void initViewCotent(){
        setImageViewColor(my_qrCode,R.color.colorWhite);
        myinfo_scrollview.setTitleAndHead(toolbar,null, person_infoColumn,0);
        toolbar.getBackground().setAlpha(0);
    }
    //获取用户信息
    private void getUserInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("userID", preferences_getData.getUserID());
                MemberInterfaceService memberInterfaceService = new MemberInterfaceService(getActivity());
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Member member;
                                member=new analysisXMLHelper().analysisMember(result);//获取对象
                                if(member==null){
                                    new SharePreferences_Manager(activity).saveLoginState(false);
                                        new SharePreferences_Manager(activity).saveUserID("");
                                }else {
                                    loadInfo(member);
                                    getCollectionNum();
                                    getFootprintNumber();
                                }
                                loadHotGoods();
                            }
                        });
                    }
                };
                memberInterfaceService.receiveSelectMemberRequest(map,callbacks);
            }
        }).start();
    }
    //加载个人信息
    private void loadInfo(Member member){
        String LoginPhone=member.getLoginName();
        String maskNumber = LoginPhone.substring(0,3)+"****"+LoginPhone.substring(7,LoginPhone.length());//用****替换手机号码中间4位
        nickname.setText(member.getMemberName());
        user_phone.setText(maskNumber);
    }
    //获取所有商品
    private void loadHotGoods(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommodityInterfaceService service=new CommodityInterfaceService(activity);
                Map<String,String> map=new HashMap<>();
                map.put("pageNo", String.valueOf(1));
                map.put("sortType","Sales_priority");
                map.put("firsttype","");
                map.put("secondtype","人气单品");
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commodityList=new analysisXMLHelper().analysisAllCommodity(result);
                                if (commodityList!=null&&commodityList.size()>0){
                                commCarditemList=new ArrayList<>();
                                CommCarditem commCardItem;
                                for(int i=0;i<commodityList.size();i++){
                                    Commodity comm=commodityList.get(i);
                                    commCardItem=new CommCarditem(comm.getId(),comm.getPicture()
                                            ,comm.getStatus(),comm.getMerName(),comm.getPrice(),comm.getSale_value());
                                    commCarditemList.add(commCardItem);
                                }
                                StaggeredGridLayoutManager gridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                                infoHotgoods.setLayoutManager(gridLayoutManager);
                                Comm_CardAdapter myAdapter=new Comm_CardAdapter(activity,commCarditemList);
                                infoHotgoods.setAdapter(myAdapter);
                            }
                            }
                        });
                    }
                };
                service.getAllCommodity(map,callbacks);
            }
        }).start();
    }
    //获取足迹数目
    private void getFootprintNumber(){
        Footprint_Manager footprint_manager=new Footprint_Manager(activity);
        if(loginState) {
            List<Footprint> footprints =footprint_manager.selectFootprint(preferences_getData.getUserID());
            if(footprint!=null)
               footprintNum.setText(String.valueOf(footprints.size()));
        }
    }
    //获取收藏品的数量
    private void getCollectionNum(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("userPhone", preferences_getData.getUserID());
                CollectInterfaceService service = new CollectInterfaceService(getActivity());
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<Commodity> commodityList;
                                commodityList=new analysisXMLHelper().analysisAllCommodity(result);//获取对象
                                if(commodityList!=null)
                                    myCollectionNum.setText(String.valueOf(commodityList.size()));
                            }
                        });
                    }
                };
                service.LoadAllCollectGoods(map,callbacks);
            }
        }).start();
    }
    //上个活动结束后返回至当前活动碎片调用的回调方法  以更新ui
    @Override
    public void onResume() {
        preferences_getData=new SharePreferences_getData(activity);
        loginState=preferences_getData.getLoginState();

        if(loginState){
            my_infoPhoto.setVisibility(View.VISIBLE);
            btn_ls.setVisibility(View.INVISIBLE);
        }else{
            btn_ls.setVisibility(View.VISIBLE);
            my_infoPhoto.setVisibility(View.INVISIBLE);
            footprintNum.setText("0");
            myCollectionNum.setText("0");
        }
        getUserInfo();
        super.onResume();
    }
}
