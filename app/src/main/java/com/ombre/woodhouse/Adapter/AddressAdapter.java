package com.ombre.woodhouse.Adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Bean.Address;
import com.ombre.woodhouse.Bean.Item.CartItem;
import com.ombre.woodhouse.DB.DBManager.Address_Manager;
import com.ombre.woodhouse.Interface.Callbacks;
import com.ombre.woodhouse.InterfaceService.CartInterfaceServicce;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.UI.activity.AddNewAddressActivity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by OMBRE on 2017/12/10.
 */
//用户卡片适配器
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {


    private Context context;
    private List<Address> addressList;
    private Activity activity;
    Address address;

    //定义监听并设set方法
    private AddressAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(AddressAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //构造方法
    public AddressAdapter(Activity activity, List<Address> addressList) {
        this.activity = activity;
        this.addressList = addressList;
    }

    //创建一个静态类
    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout edit_Address;//编辑地址
        LinearLayout deleteAddress;//删除地址
        TextView item_adress_name;//收货人
        TextView item_detail_address;//详细地址
        TextView item_address_phone;//电话号码
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            edit_Address = (LinearLayout) itemView.findViewById(R.id.edit_Address);
            deleteAddress = (LinearLayout) itemView.findViewById(R.id.deleteAddress);
            item_adress_name = (TextView) itemView.findViewById(R.id.item_adress_name);
            item_detail_address = (TextView) itemView.findViewById(R.id.item_detail_address);
            item_address_phone = (TextView) itemView.findViewById(R.id.item_address_phone);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_addressview, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.edit_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position= holder.getAdapterPosition();
                Intent intent=new Intent(activity, AddNewAddressActivity.class);
                intent.putExtra("type","edit");
                intent.putExtra("address_id",String.valueOf(addressList.get(position).getId()));
                activity.startActivity(intent);
            }
        });
        holder.deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alertDialog;
                alertDialog= builder.setIcon(R.mipmap.ic_info_outline_black_24dp)
                        .setTitle("提示")
                        .setMessage("确定删除该地址？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position= holder.getAdapterPosition();
                                new Address_Manager(activity).deleteAddress("address_id=?",new String[]{String.valueOf(addressList.get(position).getId())});
                                addressList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,getItemCount());
                                Toast.makeText(activity.getApplicationContext(),"该地址已被删除！",Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("取消",null).create();
                alertDialog.show();

            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(addressList!=null)  {
            address=addressList.get(position);
            holder.item_address_phone.setText(address.getUser_phone().substring(0,3)+"****"+address.getUser_phone().substring(7,11));
            holder.item_adress_name.setText("*"+address.getUsername().substring(1,address.getUsername().length()));
            holder.item_detail_address.setText(address.getArea()+"***");

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
        return addressList.size();
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