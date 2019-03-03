package com.ombre.woodhouse.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.Bean.Orders;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.OrderInterfaceService;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.UI.activity.AllEvaluationActivity;
import com.ombre.woodhouse.UI.activity.EvaluationGoodsActivity;
import com.ombre.woodhouse.UI.activity.OrderDetailsActivity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by OMBRE on 2017/12/10.
 */
//订单列表适配器
public class ItemOrederAdapter extends RecyclerView.Adapter<ItemOrederAdapter.ViewHolder> {

    private Context context;
    private List<Orders> mData;
    private Activity activity;
    Orders orders;
    String orderType;
    List<CartItem> cartItemList;

    //定义监听并设set方法
    private ItemOrederAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(ItemOrederAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //构造方法
    public ItemOrederAdapter(Activity activity, List<Orders> mData,String orderType) {
        this.activity = activity;
        this.mData = mData;
        this.orderType=orderType;
    }

    //创建一个静态类
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ItemorderNo;//订单编号
        TextView ItemType;//订单状态
        TextView ItemorderNumber;//总商品数量
        TextView ItemOrderActualPrice;//实际付款
        TextView Item_cancelOrder;//取消订单按钮
        TextView Item_orderType;//对账单进行操作的按钮
        RecyclerView ItemOrderList;//商品列表
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ItemorderNo = (TextView) itemView.findViewById(R.id.ItemorderNo);
            ItemType = (TextView) itemView.findViewById(R.id.ItemType);
            ItemorderNumber = (TextView) itemView.findViewById(R.id.ItemorderNumber);
            ItemOrderActualPrice = (TextView) itemView.findViewById(R.id.ItemOrderActualPrice);
            Item_cancelOrder = (TextView) itemView.findViewById(R.id.Item_cancelOrder);
            Item_orderType = (TextView) itemView.findViewById(R.id.Item_orderType);
            ItemOrderList = (RecyclerView) itemView.findViewById(R.id.ItemOrderList);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_order_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.Item_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
               cancelOrupdateOrder(mData.get(position).getId(),"","delete");
                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });

        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        orders = mData.get(position);
        if(orders!=null) {
            holder.ItemorderNo.setText(orders.getOrderNo());
            setOrderType(holder);
            getOrderGoods(holder, position);
            holder.Item_orderType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUi(holder,position);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, OrderDetailsActivity.class);
                    intent.putExtra("orderID", String.valueOf(mData.get(position).getId()));
                    activity.startActivity(intent);
                }
            });
            if (null != mOnItemClickListener) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    //7、定义点击事件回调接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    //加载订单状态
    private void setOrderType(ViewHolder holder){
        if(orders.getOrderStatus()==0){//0为未支付状态
            holder.Item_orderType.setText("立即付款");
            holder.ItemType.setText("待付款");
            holder.Item_cancelOrder.setText("取消订单");
        }
        if(orders.getOrderStatus()==1){//1为待发货状态
            holder.Item_orderType.setText("请求退货");
            holder.ItemType.setText("待发货");
            holder.Item_cancelOrder.setVisibility(View.INVISIBLE);
        }
        if (orders.getOrderStatus()==2){//2为发货后待收货状态
            holder.Item_orderType.setText("确认收货");
            holder.ItemType.setText("待收货");
            holder.Item_cancelOrder.setVisibility(View.INVISIBLE);
        }
        if(orders.getOrderStatus()==3){//3为确认收货后的待评价状态
            holder.Item_orderType.setText("评价");
            holder.ItemType.setText("待评价");
            holder.Item_cancelOrder.setVisibility(View.INVISIBLE);
        }
        if(orders.getOrderStatus()==4){
            holder.Item_orderType.setText("等待退货");
            holder.ItemType.setText("待处理");
            holder.Item_cancelOrder.setVisibility(View.INVISIBLE);
            holder.Item_orderType.setBackgroundResource(R.drawable.btn_login_bg_focused);
        }
        if(orders.getOrderStatus()==5){
            holder.Item_orderType.setText("退货成功");
            holder.ItemType.setText("已退货");
            holder.Item_cancelOrder.setText("删除订单");
            holder.Item_orderType.setBackgroundResource(R.drawable.btn_login_bg_focused);
        }
    }
    //获取订单中的商品
    private void getOrderGoods(final ViewHolder holder, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrderInterfaceService service=new OrderInterfaceService(activity);
                Map<String,String > map=new HashMap<String, String>();
                map.put("orderID", String.valueOf(mData.get(position).getId()));
                Callbacks callbacks=new Callbacks() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cartItemList=new analysisXMLHelper().analysisGoodsCart(result);
                                loadOrderGoods(holder);
                            }
                        });
                    }
                };
                service.getOrderGoods(map,callbacks);
            }
        }).start();
    }
    //加载商品列表
    private void loadOrderGoods(ViewHolder holder ){
        holder.ItemOrderList.setHasFixedSize(true);
        holder.ItemOrderList.setNestedScrollingEnabled(false); //解决Scrollview与recycleview嵌套后滑动卡顿的问题
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        holder.ItemOrderList.setLayoutManager(gridLayoutManager);
        ItemOrederGoodsAdapter myAdapter=new ItemOrederGoodsAdapter(activity,cartItemList);
        holder.ItemOrderList.setAdapter(myAdapter);
        int Allvalues=0;//选择的数量
        double totalpriceDouble=0;//总价格
        for(int i=0;i<cartItemList.size();i++){
            totalpriceDouble=totalpriceDouble+cartItemList.get(i).getValues()*cartItemList.get(i).getCartprice();
            Allvalues=Allvalues+cartItemList.get(i).getValues();
        }
        BigDecimal totalprice = new BigDecimal(totalpriceDouble);
        BigDecimal totalpriceTwo = totalprice.setScale(2, BigDecimal.ROUND_HALF_DOWN);//将double 四舍五入 保留两位小数
        holder.ItemOrderActualPrice.setText("¥"+totalpriceTwo);
        holder.ItemorderNumber.setText("共计"+Allvalues+"件商品");
    }

    //删除或修改指定订单
    private void cancelOrupdateOrder(final int orderID, final String OrderState, final String OperationType ){//OperationType:数据库操作类型
        new Thread(new Runnable() {
            @Override
            public void run() {
                OrderInterfaceService service=new OrderInterfaceService(activity);
                Map<String,String > map=new HashMap<String, String>();
                map.put("orderID", String.valueOf(orderID));
                map.put("OrderState",OrderState);
                map.put("OperationType",OperationType);
                Callbacks callbacks=null;
                service.updateOrdeleteOrder(map,callbacks);
            }
        }).start();
    }
    //订单操作按钮的监听事件
    private void updateUi(final ViewHolder holder, final int position){
        String type= holder.Item_orderType.getText().toString();
        if (!orderType.equals("全部")){
            if(type.equals("立即付款"))
            { AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alertDialog;
                alertDialog = builder
                        .setTitle("提示")
                        .setMessage("是否确认付款？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrupdateOrder(mData.get(position).getId(),"1","update");
                                mData.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,getItemCount());
                            }
                        }).setNegativeButton("取消",null).create();
                alertDialog.show();
            }
            if(type.equals("请求退货")){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alertDialog;
                alertDialog = builder
                        .setTitle("提示")
                        .setMessage("是否确认退货？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrupdateOrder(mData.get(position).getId(),"4","update");
                                mData.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,getItemCount());
                            }
                        }).setNegativeButton("否",null).create();
                alertDialog.show();

            }
            if(type.equals("确认收货")){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alertDialog;
                alertDialog = builder
                        .setTitle("提示")
                        .setMessage("是否确认收货？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrupdateOrder(mData.get(position).getId(),"3","update");
                                mData.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,getItemCount());
                            }
                        }).setNegativeButton("否",null).create();
                alertDialog.show();
            }
            if(type.equals("评价")) {
                Intent intent_eva=new Intent(activity,EvaluationGoodsActivity.class);
                intent_eva.putExtra("orderID",String.valueOf(mData.get(position).getId()));
                intent_eva.putExtra("orderNum",mData.get(position).getOrderNo());
                activity.startActivity(intent_eva);
            }
        }else
        {
            if(type.equals("立即付款"))
            { AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alertDialog;
                alertDialog = builder
                        .setTitle("提示")
                        .setMessage("是否确认付款？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrupdateOrder(mData.get(position).getId(),"1","update");
                                holder.Item_orderType.setText("请求退货");
                                holder.Item_cancelOrder.setVisibility(View.INVISIBLE);
                                holder.ItemType.setText("待收货");
                            }
                        }).setNegativeButton("取消",null).create();
                alertDialog.show();
            }
            if(type.equals("请求退货")){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alertDialog;
                alertDialog = builder
                        .setTitle("提示")
                        .setMessage("是否确认退货？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrupdateOrder(mData.get(position).getId(),"4","update");
                                holder.Item_orderType.setText("等待退货");
                                holder.Item_orderType.setBackgroundResource(R.drawable.btn_login_bg_focused);
                                holder.ItemType.setText("待处理");
                            }
                        }).setNegativeButton("否",null).create();
                alertDialog.show();

            }
            if(type.equals("确认收货")){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alertDialog;
                alertDialog = builder
                        .setTitle("提示")
                        .setMessage("是否确认收货？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrupdateOrder(mData.get(position).getId(),"3","update");
                                holder.Item_orderType.setText("评价");
                                holder.ItemType.setText("待评价");
                            }
                        }).setNegativeButton("否",null).create();
                alertDialog.show();
            }
            if(type.equals("评价")) {
               Intent intent_eva=new Intent(activity,EvaluationGoodsActivity.class);
                intent_eva.putExtra("orderID",String.valueOf(mData.get(position).getId()));
                intent_eva.putExtra("orderNum",mData.get(position).getOrderNo());
                activity.startActivity(intent_eva);
            }
        }
    }
}