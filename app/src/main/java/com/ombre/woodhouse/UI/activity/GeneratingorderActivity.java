package com.ombre.woodhouse.UI.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.ItemOrederGoodsAdapter;
import com.ombre.woodhouse.Bean.Address;
import com.ombre.woodhouse.Bean.Commodity;
import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.DB.DBManager.Address_Manager;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CartInterfaceServicce;
import com.ombre.woodhouse.InterfaceService.CommodityInterfaceService;
import com.ombre.woodhouse.InterfaceService.OrderInterfaceService;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

//生成订单
public class GeneratingorderActivity extends AppCompatActivity {

    private LinearLayout choiceTrue;
    private LinearLayout choiceFalse;
    private TextView generation_order_phone;//电话
    private TextView generation_order_name;//姓名
    private TextView generation_order_address;//详细地址
    private RecyclerView generationOrderList;//商品列表
    private TextView generationNumber;//商品总数
    private TextView generationPrice;//商品金额
    private TextView ActualPrice;//实际价格
    SharePreferences_getData preferences_getData;
    String addressId;//地址id
    Address address;//地址
    String goodsId;//商品id
    Commodity commodity;//商品类
    List<CartItem> cartItemList;
    int values;
    private void init(){
        choiceTrue=(LinearLayout)findViewById(R.id.choiceTrue);
        choiceFalse=(LinearLayout)findViewById(R.id.choiceFalse);
        generation_order_phone=(TextView)findViewById(R.id.generation_order_phone);
        generation_order_name=(TextView)findViewById(R.id.generation_order_name);
        generation_order_address=(TextView)findViewById(R.id.generation_order_address);
        generationNumber=(TextView)findViewById(R.id.generationNumber);
        generationPrice=(TextView)findViewById(R.id.generationPrice);
        ActualPrice=(TextView)findViewById(R.id.ActualPrice);

        generationOrderList=(RecyclerView)findViewById(R.id.generationOrderList);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generatingorder);
        init();
        preferences_getData=new SharePreferences_getData(GeneratingorderActivity.this);
        generationOrderList.setHasFixedSize(true);
        generationOrderList.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
       initViews();
    }
    public  void onClick_Generating_order(View view){
        switch (view.getId()){
            case R.id.generatingorder_back:
                finish();
                break;
            case R.id.choiceAddress://选择地址
                Intent intent_address=new Intent(this,AddressActivity.class);
                intent_address.putExtra("type","order");
                startActivity(intent_address);
                break;
            case R.id.btn_generationOrder://提交订单
                if(generation_order_phone.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"请选择您的收货地址！",Toast.LENGTH_SHORT).show();
                    else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog alertDialog;
                alertDialog = builder
                        .setTitle("提示")
                        .setMessage("是否确认付款？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SubmitOrder("1");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SubmitOrder("0");
                            }
                        }).create();
                alertDialog.show();
            }
                break;
        }
    }

    //提交订单
    private void SubmitOrder(final String Paystate){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Paystate",Paystate);
                map.put("memberPhone",preferences_getData.getUserID());
                if(goodsId!=null) {
                    map.put("goodsId", goodsId);
                    map.put("goodsValue", String.valueOf(values));
                }
                else
                {
                    map.put("goodsId","");
                    map.put("goodsValue","");}
                map.put("AddressPhone",address.getUser_phone());
                map.put("AddressName",address.getUsername());
                map.put("AddressArea",address.getArea()+address.getAddress_remark());
                OrderInterfaceService service=new OrderInterfaceService(GeneratingorderActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String flag;
                                if (result.length()>4)
                                    flag=result.substring(result.length()-4,result.length());//取出字符窜最后四位，用于判断库存是否足够
                                else
                                    flag=result;
                                if(!flag.equals("库存不足")){
                                    if(Paystate.equals("0")){
                                        Intent intent=new Intent(GeneratingorderActivity.this,OrderDetailsActivity.class);
                                        intent.putExtra("orderID",flag);
                                        Log.e("FSSAFA",flag);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                        finish();
                                }
                                else
                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
                service.GenerationOrder(map,callbacks);
            }
        }).start();

    }
    //加载布局
    private void initViews(){
        Intent intent=getIntent();
        goodsId=intent.getStringExtra("goodsId");
        if(goodsId!=null){
            values=Integer.valueOf(intent.getStringExtra("values"));
            getGoodsDetails();
        }else
        {
            getCartSelectGoods();
        }
        loadAddress();
    }
    //加载地址
    private void loadAddress(){
        addressId=preferences_getData.getAddressID();
        if(addressId!=null&&!addressId.equals("")){
            address=new Address_Manager(this).selectOneAddress(addressId);
            if(address!=null&&address.getId()!=null&&address.getAccount_num().equals(preferences_getData.getUserID())){
                generation_order_phone.setText(address.getUser_phone().substring(0,3)+"****"+address.getUser_phone().substring( 7,11));
                generation_order_name.setText("*"+address.getUsername().substring(1,address.getUsername().length()));
                generation_order_address.setText(address.getArea()+"***");
                choiceFalse.setVisibility(View.INVISIBLE);
                choiceTrue.setVisibility(View.VISIBLE);
            }else
            {
                choiceTrue.setVisibility(View.INVISIBLE);
                choiceFalse.setVisibility(View.VISIBLE);
            }
        }else{
            choiceTrue.setVisibility(View.INVISIBLE);
            choiceFalse.setVisibility(View.VISIBLE);
        }
    }
    //胡哦去购物车选购清单
    private void getCartSelectGoods(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("userPhone",preferences_getData.getUserID());
                CartInterfaceServicce service=new CartInterfaceServicce(GeneratingorderActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result1=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cartItemList=new analysisXMLHelper().analysisGoodsCart(result1);
                                if(cartItemList!=null&&cartItemList.size()>0){
                                    List<CartItem> cartItems=new ArrayList<CartItem>();
                                    for(int i=0;i<cartItemList.size();i++){
                                        if(cartItemList.get(i).isselect())
                                            cartItems.add(cartItemList.get(i));
                                    }
                                    loadGoodsList(cartItems);
                                }
                            }
                        });
                    }
                };
                service.getCart(map,callbacks);
            }
        }).start();
    }
    //加载购物清单---当界面从详情界面跳转时
    private void  getGoodsDetails(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("goodsId",goodsId);
                CommodityInterfaceService service=new CommodityInterfaceService(GeneratingorderActivity.this);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commodity=new analysisXMLHelper().analysisCommodityDetails(result);
                                if(commodity!=null)
                                {
                                    cartItemList=new ArrayList<CartItem>();
                                    CartItem cartItem=new CartItem(0,commodity.getId(),commodity.getPicture(),commodity.getMerName(),values,commodity.getPrice(),true);
                                    cartItemList.add(cartItem);
                                   loadGoodsList(cartItemList);
                                }
                            }
                        });
                    }
                };
                service.getCommdityDetails(map,callbacks);
            }
        }).start();
    }
    //加载商品列表
    private void loadGoodsList(List<CartItem> cartItemList){
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        generationOrderList.setLayoutManager(gridLayoutManager);
        ItemOrederGoodsAdapter myAdapter=new ItemOrederGoodsAdapter(this,cartItemList);
        myAdapter.setOnItemClickListener(new ItemOrederGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {}});
        generationOrderList.setAdapter(myAdapter);
        int Allvalues=0;//选择的数量
        double totalpriceDouble=0;//总价格
        for(int i=0;i<cartItemList.size();i++){
                totalpriceDouble=totalpriceDouble+cartItemList.get(i).getValues()*cartItemList.get(i).getCartprice();
                Allvalues=Allvalues+cartItemList.get(i).getValues();
        }
        BigDecimal totalprice = new BigDecimal(totalpriceDouble);
        BigDecimal totalpriceTwo = totalprice.setScale(2, BigDecimal.ROUND_HALF_DOWN);//将double 四舍五入 保留两位小数

        generationPrice.setText("¥"+totalpriceTwo);
        generationNumber.setText(""+Allvalues+"件商品");
        ActualPrice.setText("¥"+totalpriceTwo);

    }
    @Override
    protected void onResume() {
        initViews();
        loadAddress();
        super.onResume();
    }
}