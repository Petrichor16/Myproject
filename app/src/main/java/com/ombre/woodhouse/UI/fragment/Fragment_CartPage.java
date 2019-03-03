package com.ombre.woodhouse.UI.fragment;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.ombre.woodhouse.Adapter.Cart_CardAdapter;
import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CartInterfaceServicce;

import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.UI.activity.CommoditydetailsAcitivity;
import com.ombre.woodhouse.UI.activity.GeneratingorderActivity;
import com.ombre.woodhouse.UI.activity.GoodsBrowsingActivity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.ombre.woodhouse.Utils.IconColor_Change.setImageViewColor;

/**
 * Created by OMBRE on 2018/5/22.
 */
//购物车
public class Fragment_CartPage extends Fragment implements View.OnClickListener {

    private Activity activity;
    //购物车为空时
    private LinearLayout cartEnpty;//为空时对的布局
    private ImageView icon_cart_empty;//购物车为空
    private TextView look_around;//随便逛逛
    //购物车不为空时
    private LinearLayout cartInEnpty;//不为空时的布局
    private RecyclerView cartRecycleView;//购物车不为空时的列表
    private CheckBox checkAll;//全选

    private TextView cartManage;//购物车管理
   private LinearLayout settlement_layout;//结算栏
    private TextView allCheckCart_price;//所有选中栏的价格和
    private TextView CheckCart_count;//选中的数量
    private TextView btn_settlement;//结算按键
  private SpinKitView cartLoading;

    Loading_Dialog dialog;
    //管理购物车时
    private LinearLayout delcart_layout;//shanchu
    private TextView del_cartItem;//删除选中的条目

    private List<CartItem> cartItemList;//购物车不为空时的数列
    SharePreferences_getData preferences_getData;
    boolean login_state;
    String userName;

    public static final int CARTCONNECTFAILS=4201;//网络连接失败
    public static final int CARTCONNECTSUCCESS=4202;//网络连接成功
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CARTCONNECTFAILS:
                    cartLoading.setVisibility(View.INVISIBLE);
                    Toast.makeText(activity,"网络不给力",Toast.LENGTH_SHORT).show();
                    break;
                case CARTCONNECTSUCCESS:
                    cartLoading.setVisibility(View.INVISIBLE);
                    cartItemList=new analysisXMLHelper().analysisGoodsCart(msg.obj.toString());
                    if(cartItemList!=null&&cartItemList.size()>0){
                        cartEnpty.setVisibility(View.INVISIBLE);
                        cartManage.setVisibility(View.VISIBLE);
                        cartInEnpty.setVisibility(View.VISIBLE);
                        loadCartList();
                    }else{
                        cartInEnpty.setVisibility(View.INVISIBLE);
                        cartManage.setVisibility(View.INVISIBLE);
                        cartEnpty.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };
    private void initViews(View view){
        icon_cart_empty=(ImageView)view.findViewById(R.id.icon_cart_empty);
        cartRecycleView=(RecyclerView) view.findViewById(R.id.cartRecycleView);
        checkAll=(CheckBox) view.findViewById(R.id.checkAll);
        cartEnpty=(LinearLayout) view.findViewById(R.id.cartEnpty);
        look_around=(TextView) view.findViewById(R.id.look_around);
        cartInEnpty=(LinearLayout) view.findViewById(R.id.cartInEnpty);
        cartManage=(TextView) view.findViewById(R.id.cartManage);
        settlement_layout=(LinearLayout) view.findViewById(R.id.settlement_layout);
        allCheckCart_price=(TextView) view.findViewById(R.id.allCheckCart_price);
        CheckCart_count=(TextView) view.findViewById(R.id.CheckCart_count);
        btn_settlement=(TextView) view.findViewById(R.id.btn_settlement);
        delcart_layout=(LinearLayout) view.findViewById(R.id.delcart_layout);
        del_cartItem=(TextView) view.findViewById(R.id.del_cartItem);
        cartLoading=(SpinKitView)view.findViewById(R.id.cartLoading);
    }

    public Fragment_CartPage(Activity activity) {
        this.activity = activity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_cart,container,false);
        initViews(view);
        new SharePreferences_Manager(activity).saveFirstClassification("");
        checkAll.setOnClickListener(this);
        look_around.setOnClickListener(this);
        cartManage.setOnClickListener(this);
        btn_settlement.setOnClickListener(this);
        del_cartItem.setOnClickListener(this);
        initLayout();
        return view;
    }

    //获取购物车数据
    private void getCartItemList(){
        cartLoading.setVisibility(View.VISIBLE);
       new Thread(new Runnable() {
           @Override
           public void run() {
               final Map<String, String> map = new HashMap<String, String>();
               map.put("userPhone",preferences_getData.getUserID());
               CartInterfaceServicce service=new CartInterfaceServicce(activity);
               Callbacks callbacks=new Callbacks() {
                   @Override
                   public void onFailure(Call call, IOException e) {
                               Message msg=new Message();
                               msg.what=CARTCONNECTFAILS;
                               mHandler.sendMessage(msg);
                   }
                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       final String result1=response.body().string();
                       Message msg=new Message();
                       msg.what=CARTCONNECTSUCCESS;
                       msg.obj=result1;
                       mHandler.sendMessage(msg);
                   }
               };
               service.getCart(map,callbacks);
           }
       }).start();
    }
    //初始化购物车列表item
    private void loadCartList(){
        StaggeredGridLayoutManager   gridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        cartRecycleView.setLayoutManager(gridLayoutManager);
        Cart_CardAdapter myAdapter=new Cart_CardAdapter(activity,cartItemList,mHandler);
        myAdapter.setmOnCheckBoxChangeListener(new Cart_CardAdapter.OnCheckBoxListener() {
            @Override
            public void onCheckBox(boolean checkAll_state,int values,BigDecimal totalpriceTwo) {
                checkAll.setChecked(checkAll_state);
                CheckCart_count.setText("("+String.valueOf(values)+"件)");
                allCheckCart_price .setText(String.valueOf(totalpriceTwo));
            }
        });
        cartRecycleView.setAdapter(myAdapter);
    }
    //初始化布局
   private void  initLayout(){
       checkAll.setChecked(false);
       setImageViewColor(icon_cart_empty,R.color.cart_empty);
       preferences_getData=new SharePreferences_getData(activity);
       login_state=preferences_getData.getLoginState();//登录状态
       cartItemList=new ArrayList<>();
       if(login_state){
           getCartItemList();
       }
       else
       {
           cartLoading.setVisibility(View.INVISIBLE);
           cartManage.setVisibility(View.INVISIBLE);
           cartEnpty.setVisibility(View.VISIBLE);
           cartInEnpty.setVisibility(View.INVISIBLE);
       }
   }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkAll://全选按钮
                updateCheckedAll(checkAll.isChecked());
                if (!checkAll.isChecked()) {
                    CheckCart_count.setText("(0件)");
                    allCheckCart_price.setText("0.00");
                }
                break;
            case R.id.look_around://随便逛逛
                new SharePreferences_Manager(activity).saveSecondClassification("新品到货");
                Intent intent_1 = new Intent(activity, GoodsBrowsingActivity.class);
                startActivity(intent_1);
                break;
            case R.id.cartManage://购物车管理
                if (cartManage.getText().toString().equals("管理")) {
                    settlement_layout.setVisibility(View.INVISIBLE);
                    delcart_layout.setVisibility(View.VISIBLE);
                    cartManage.setText("完成");
                } else {
                    settlement_layout.setVisibility(View.VISIBLE);
                    delcart_layout.setVisibility(View.INVISIBLE);
                    cartManage.setText("管理");
                    getCartItemList();
                }
                break;
            case R.id.btn_settlement://结算
                int j=0;
                for(int i=0;i<cartItemList.size();i++){
                    if(cartItemList.get(i).isselect())
                        j++;
                }
                if(j>0){
                Intent intent_2 = new Intent(activity, GeneratingorderActivity.class);
                startActivity(intent_2);}
                else
                    Toast.makeText(activity,"请选择你想要购买的商品！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.del_cartItem://删除
                deleteCart();
                CheckCart_count.setText("(0件)");
                allCheckCart_price.setText("0.00");
                break;
        }
    }
    //全选设置
    private void updateCheckedAll(final boolean type){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("updateType","AllCheckedType");
                    map.put("memberPhone",preferences_getData.getUserID());
                    map.put("CartselectID","");
                    map.put("updateContent", String.valueOf(type));
                    CartInterfaceServicce service=new CartInterfaceServicce(activity);
                    Callbacks callbacks=new Callbacks() {
                        @Override
                        public void onFailure(Call call, IOException e) {}

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getCartItemList();
                                }
                            });
                        }
                    };
                    service.updateChecked(map,callbacks);
                }
            }).start();
       }
    //删除购物车
    private void deleteCart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("memberPhone",preferences_getData.getUserID());
                CartInterfaceServicce service=new CartInterfaceServicce(activity);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getCartItemList();
                            }
                        });
                    }
                };
                service.deleteCart(map,callbacks);
            }
        }).start();
    }
    @Override
    public void onResume() {
        initLayout();
        super.onResume();
    }
}
