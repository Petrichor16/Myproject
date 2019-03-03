package com.ombre.woodhouse.Adapter;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.DB.DBManager.Footprint_Manager;
import com.ombre.woodhouse.Helper.analysisXMLHelper;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CartInterfaceServicce;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.UI.activity.AddNewAddressActivity;
import com.ombre.woodhouse.UI.activity.CommoditydetailsAcitivity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by OMBRE on 2017/12/10.
 */
//用户卡片适配器
public class Cart_CardAdapter extends RecyclerView.Adapter<Cart_CardAdapter.ViewHolder> {


    private Context context;
    private List<CartItem> mData;
    private Activity activity;
    CartItem cartItem;
    Handler handler;

    //定义监听并设set方法
    private Cart_CardAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(Cart_CardAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    private Cart_CardAdapter.OnCheckBoxListener mOnCheckBoxChangeListener;
    public void setmOnCheckBoxChangeListener(Cart_CardAdapter.OnCheckBoxListener mOnCheckBoxChangeListener){
        this.mOnCheckBoxChangeListener = mOnCheckBoxChangeListener;
    }

    //构造方法
    public Cart_CardAdapter(Activity activity, List<CartItem> mData,Handler handler) {
        this.activity = activity;
        this.mData = mData;
        this.handler=handler;
    }

    //创建一个静态类
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView card_picture;//商品图片
        TextView cart_comm_name;//商品名称
        TextView choice_numbers;//选择件数
        TextView cart_price;
        CheckBox cartgoods_check;//购物车选择框
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            card_picture = (ImageView) itemView.findViewById(R.id.card_picture);
            cart_comm_name = (TextView) itemView.findViewById(R.id.cart_comm_name);
            choice_numbers = (TextView) itemView.findViewById(R.id.choice_numbers);
            cart_price = (TextView) itemView.findViewById(R.id.cart_price);
            cartgoods_check=(CheckBox)itemView.findViewById(R.id.cartgoods_check);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_card_cart, parent, false);
        final ViewHolder holder = new ViewHolder(view);
      //  实现购物选择
        if(null!=mOnCheckBoxChangeListener){
            holder.cartgoods_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    final int position=holder.getAdapterPosition();
                    boolean checkAll_state;
                    mData.get(position).setIsselect(holder.cartgoods_check.isChecked());
                    updateCheckedType(mData.get(position).getId(),mData.get(position).isselect());
                    int j=0;
                    int values=0;//选择的数量
                    double totalpriceDouble=0;//总价格
                    for(int i=0;i<mData.size();i++){
                        if(mData.get(i).isselect()==true){
                            j++;
                            totalpriceDouble=totalpriceDouble+mData.get(i).getValues()*mData.get(i).getCartprice();
                            values=values+mData.get(i).getValues();
                        }
                    }
                    BigDecimal totalprice = new BigDecimal(totalpriceDouble);
                    BigDecimal totalpriceTwo = totalprice.setScale(2, BigDecimal.ROUND_HALF_DOWN);//将double 四舍五入 保留两位小数
                    if(j<mData.size()){
                        checkAll_state= false;
                    }else
                    {
                        checkAll_state=true;
                    }
                    mOnCheckBoxChangeListener.onCheckBox(checkAll_state,values,totalpriceTwo);
                }
            });
        }
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(mData!=null)  {
            cartItem=mData.get(position);

        holder.cartgoods_check.setChecked(cartItem.isselect());
            if(cartItem.getCartpicturePath().equals(""))
                holder.card_picture.setImageResource(R.mipmap.nopic);
            else{
                String picPath="";
                GetPicture getPicture=new GetPicture(activity,holder.card_picture);
                List<String> picList=getPicture.picPath(cartItem.getCartpicturePath());
                if(picList.size()>0)
                    picPath=picList.get(0);
                getPicture.loadPicture(picPath);
            }
        holder.cart_comm_name.setText(cartItem.getCart_comm_name());
        holder.choice_numbers.setText(String.valueOf(cartItem.getValues()));
        holder.cart_price.setText(String.valueOf(cartItem.getCartprice()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(activity,CommoditydetailsAcitivity.class);
                    intent.putExtra("goodsId",String.valueOf(mData.get(position).getGoodsID()));

                    activity.startActivity(intent);
                }
            });
        if(null != mOnItemClickListener) {
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
        void onItemClick(View view,int position);
    }
    public interface  OnCheckBoxListener{
        void onCheckBox(boolean checkAll_state,int values,BigDecimal totalpriceTwo);
    }

    //修改选中状态
    private void updateCheckedType(final int id, final boolean checkedType){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("updateType","CheckedType");
                map.put("memberPhone","");
                map.put("CartselectID", String.valueOf(id));
                map.put("updateContent", String.valueOf(checkedType));
                CartInterfaceServicce service=new CartInterfaceServicce(activity);
                Callbacks callbacks=null;
                service.updateChecked(map,callbacks);
            }
        },0);
    }


}