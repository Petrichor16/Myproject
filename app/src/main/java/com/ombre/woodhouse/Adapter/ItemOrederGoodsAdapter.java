package com.ombre.woodhouse.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.UI.activity.CommoditydetailsAcitivity;

import java.util.List;


/**
 * Created by OMBRE on 2017/12/10.
 */
//订单列表适配器
public class ItemOrederGoodsAdapter extends RecyclerView.Adapter<ItemOrederGoodsAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> mData;
    private Activity activity;
    CartItem cartItem;

    //定义监听并设set方法
    private ItemOrederGoodsAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(ItemOrederGoodsAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //构造方法
    public ItemOrederGoodsAdapter(Activity activity, List<CartItem> mData) {
        this.activity = activity;
        this.mData = mData;
    }

    //创建一个静态类
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView orderGoodsPic;//商品图片
        TextView orderGoodsName;//商品名称
        TextView orderGoodsPrice;//选择件数
        TextView orderGoodsNumber;//选购数量
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            orderGoodsPic = (ImageView) itemView.findViewById(R.id.orderGoodsPic);
            orderGoodsName = (TextView) itemView.findViewById(R.id.orderGoodsName);
            orderGoodsPrice = (TextView) itemView.findViewById(R.id.orderGoodsPrice);
            orderGoodsNumber = (TextView) itemView.findViewById(R.id.orderGoodsNumber);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_oder_goods, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(mData!=null)  {
            cartItem=mData.get(position);
      if(cartItem.getCartpicturePath()!=null){
          String picPath="";
          GetPicture getPicture=new GetPicture(activity,holder.orderGoodsPic);
          List<String> picList=getPicture.picPath(cartItem.getCartpicturePath());
          if(picList.size()>0)
              picPath=picList.get(0);
          getPicture.loadPicture(picPath);
      }
     // new GetPicture(activity,holder.orderGoodsPic).loadPicture(cartItem.getCartpicturePath());
      else
          holder.orderGoodsPic.setImageResource(R.mipmap.nopic);
        holder.orderGoodsName.setText(cartItem.getCart_comm_name());
        holder.orderGoodsNumber.setText(String.valueOf(cartItem.getValues()));
        holder.orderGoodsPrice.setText(String.valueOf(cartItem.getCartprice()));

        if(null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity,CommoditydetailsAcitivity.class);
                    intent.putExtra("goodsId",String.valueOf(mData.get(position).getGoodsID()));
                    activity.startActivity(intent);
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

}