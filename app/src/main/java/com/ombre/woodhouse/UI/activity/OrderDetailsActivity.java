package com.ombre.woodhouse.UI.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Adapter.ItemOrederGoodsAdapter;
import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.Bean.Orders;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.OrderInterfaceService;
import com.ombre.woodhouse.R;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

//订单详情
public class OrderDetailsActivity extends AppCompatActivity {

    private TextView details_orderType;//订单状态
    private TextView OrderPrompt;//订单提示
    private TextView detail_order_phone;//电话
    private TextView detail_order_name;//姓名
    private TextView detail_order_address;//地址
    private TextView detailsorderNumber;//订单中的所有商品件数
    private TextView detailsorderPrice;//商品总价格
    private TextView detailsOrderActualPrice;//实付价格
    private TextView orderNumber;//订单编号
    private TextView details_order_time;//下单时间
    private TextView btn_cancelOrder;//对订单的操作
    private TextView btn_orderType;//订单状态对应操作
    private RecyclerView detailsOrderList;//商品清单
    List<CartItem> cartItemList;
    private String orderID;//订单id
    Loading_Dialog dialog;

    private void init(){
        details_orderType=(TextView)findViewById(R.id.details_orderType);
        OrderPrompt=(TextView)findViewById(R.id.OrderPrompt);
        detail_order_phone=(TextView)findViewById(R.id.detail_order_phone);
        detail_order_name=(TextView)findViewById(R.id.detail_order_name);
        detail_order_address=(TextView)findViewById(R.id.detail_order_address);
        detailsorderNumber=(TextView)findViewById(R.id.detailsorderNumber);
        detailsorderPrice=(TextView)findViewById(R.id.detailsorderPrice);
        detailsOrderActualPrice=(TextView)findViewById(R.id.detailsOrderActualPrice);
        orderNumber=(TextView)findViewById(R.id.orderNumber);
        details_order_time=(TextView)findViewById(R.id.details_order_time);
        btn_cancelOrder=(TextView)findViewById(R.id.btn_cancelOrder);
        btn_orderType=(TextView)findViewById(R.id.btn_orderType);
        detailsOrderList=(RecyclerView)findViewById(R.id.detailsOrderList);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        init();
        Intent intent=getIntent();
        orderID=intent.getStringExtra("orderID");
        dialog=new Loading_Dialog(OrderDetailsActivity.this,R.style.dialog);
        detailsOrderList.setHasFixedSize(true);
        detailsOrderList.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
        getOrderDetails();
    }
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.details_order_back://返回
                finish();
                break;
            case  R.id.btn_cancelOrder://
                cancelOrupdateOrder("","delete");
                finish();
                break;
            case R.id.btn_orderType:
                String type=btn_orderType.getText().toString();
                if(type.equals("立即付款"))
                { AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final AlertDialog alertDialog;
                    alertDialog = builder
                            .setTitle("提示")
                            .setMessage("是否确认付款？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelOrupdateOrder("1","update");
                                    finish();
                                }
                            }).setNegativeButton("取消",null).create();
                    alertDialog.show();
                }
                if(type.equals("请求退货")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final AlertDialog alertDialog;
                    alertDialog = builder
                            .setTitle("提示")
                            .setMessage("是否确认退货？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelOrupdateOrder("4","update");
                                    finish();
                                }
                            }).setNegativeButton("否",null).create();
                    alertDialog.show();

                }
                if(type.equals("确认收货")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final AlertDialog alertDialog;
                    alertDialog = builder
                            .setTitle("提示")
                            .setMessage("是否确认收货？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelOrupdateOrder("3","update");
                                    finish();
                                }
                            }).setNegativeButton("否",null).create();
                    alertDialog.show();
                }
                if(type.equals("评价")) {
                Intent intent_eva=new Intent(this,EvaluationGoodsActivity.class);
                    intent_eva.putExtra("orderID",orderID);
                    intent_eva.putExtra("orderNum",orderNumber.getText().toString());
                    startActivity(intent_eva);
                    finish();
                }
                break;
        }
    }
    //获取订单信息
    private void getOrderDetails(){
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrderInterfaceService service=new OrderInterfaceService(OrderDetailsActivity.this);
                Map<String,String > map=new HashMap<String, String>();
                map.put("orderID",orderID);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(OrderDetailsActivity.this,"网络不给力",Toast.LENGTH_SHORT).show();
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
                                Orders orders=new Orders();
                              List<Orders> ordersList=new analysisXMLHelper().analysisOrderDetails(result);
                                if(ordersList!=null&&ordersList.size()>0)
                                    orders=ordersList.get(0);
                                loadOrderDetails(orders);
                                getOrderGoods();
                            }
                        });
                    }
                };
                service.getOrderDetails(map,callbacks);
            }
        }).start();
    }
    //加载地址，订单号和下单时间
    private void loadOrderDetails(Orders orders){
        detail_order_name.setText("*"+orders.getAdName().substring(1,orders.getAdName().length()));
        detail_order_address.setText(orders.getAddressId().substring(0,10)+"***");
        detail_order_phone.setText(orders.getAdTelphone().substring(0,3)+"****"+orders.getAdTelphone().substring(7,11));
        orderNumber.setText(orders.getOrderNo());
        details_order_time.setText( orders.getOrderDate());
        if(orders.getOrderStatus()==0){//0为未支付状态
            btn_orderType.setText("立即付款");
            details_orderType.setText("待付款");
            OrderPrompt.setText("您的订单已提交，请你即使完成支付，以免给您带来不便！");
            btn_cancelOrder.setText("取消订单");
        }
        if(orders.getOrderStatus()==1){//1为待发货状态
            btn_orderType.setText("请求退货");
            details_orderType.setText("待发货");
            OrderPrompt.setText("您的订单还在等待店家发货，请耐心等待，如有特殊情况请自行联系店家！");
            btn_cancelOrder.setVisibility(View.INVISIBLE);
        }
        if (orders.getOrderStatus()==2){//2为发货后待收货状态
            btn_orderType.setText("确认收货");
            details_orderType.setText("待收货");
            OrderPrompt.setText("您的订单已发货，请注意查收！");
            btn_cancelOrder.setVisibility(View.INVISIBLE);
        }
        if(orders.getOrderStatus()==3){//3为确认收货后的待评价状态
            btn_orderType.setText("评价");
            details_orderType.setText("待评价");
            OrderPrompt.setText("您的订单已签收，请您为您的的商品给出一个合理的评价！");
            btn_cancelOrder.setVisibility(View.INVISIBLE);
        }
        if(orders.getOrderStatus()==4){
            btn_orderType.setText("等待退货");
            details_orderType.setText("待处理");
            OrderPrompt.setText("您的退货请求已发送成功，正在等待店家处理，请您耐心等待");
            btn_cancelOrder.setVisibility(View.INVISIBLE);
            btn_orderType.setBackgroundResource(R.drawable.btn_login_bg_focused);
        }
        if(orders.getOrderStatus()==5){
            btn_orderType.setText("退货成功");
            details_orderType.setText("退货成功");
            OrderPrompt.setText("店家已同意您的退货请求，请注意查收退款状态！");
            btn_cancelOrder.setVisibility(View.INVISIBLE);
            btn_orderType.setBackgroundResource(R.drawable.btn_login_bg_focused);
        }
    }
    //获取订单中的商品
    private void getOrderGoods(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrderInterfaceService service=new OrderInterfaceService(OrderDetailsActivity.this);
                Map<String,String > map=new HashMap<String, String>();
                map.put("orderID",orderID);
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                cartItemList=new analysisXMLHelper().analysisGoodsCart(result);
                                loadOrderGoods();
                            }
                        });
                    }
                };
                service.getOrderGoods(map,callbacks);
            }
        }).start();
    }
    //加载商品列表
    private void loadOrderGoods(){
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        detailsOrderList.setLayoutManager(gridLayoutManager);
        ItemOrederGoodsAdapter myAdapter=new ItemOrederGoodsAdapter(this,cartItemList);
        myAdapter.setOnItemClickListener(new ItemOrederGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        detailsOrderList.setAdapter(myAdapter);
        int Allvalues=0;//选择的数量
        double totalpriceDouble=0;//总价格
        for(int i=0;i<cartItemList.size();i++){
            totalpriceDouble=totalpriceDouble+cartItemList.get(i).getValues()*cartItemList.get(i).getCartprice();
            Allvalues=Allvalues+cartItemList.get(i).getValues();
        }
        BigDecimal totalprice = new BigDecimal(totalpriceDouble);
        BigDecimal totalpriceTwo = totalprice.setScale(2, BigDecimal.ROUND_HALF_DOWN);//将double 四舍五入 保留两位小数

        detailsorderPrice.setText("¥"+totalpriceTwo);
        detailsorderNumber.setText(""+Allvalues+"件商品");
        detailsOrderActualPrice.setText("¥"+totalpriceTwo);
    }
    //删除或修改指定订单
    private void cancelOrupdateOrder(final String OrderState, final String OperationType ){//OperationType:数据库操作类型
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrderInterfaceService service=new OrderInterfaceService(OrderDetailsActivity.this);
                Map<String,String > map=new HashMap<String, String>();
                map.put("orderID",orderID);
                map.put("OrderState",OrderState);
                map.put("OperationType",OperationType);
                Callbacks callbacks=null;
                service.updateOrdeleteOrder(map,callbacks);
            }
        }).start();
    }

    @Override
    protected void onResume() {
        getOrderDetails();
        super.onResume();
    }
}
