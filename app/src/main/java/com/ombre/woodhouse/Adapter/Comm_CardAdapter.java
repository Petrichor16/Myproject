package com.ombre.woodhouse.Adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ombre.woodhouse.Bean.Item.CommCarditem;
import com.ombre.woodhouse.DB.DBManager.Footprint_Manager;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_getData;
import com.ombre.woodhouse.UI.activity.CommoditydetailsAcitivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMBRE on 2017/12/10.
 */
//商品卡片适配器
public class Comm_CardAdapter extends RecyclerView.Adapter<Comm_CardAdapter.ViewHolder> {


    private Context context;
    private List<CommCarditem> commCarditemList;
    private Activity activity;
    SharePreferences_getData preferences_getData;

    //定义监听并设set方法
    private Comm_CardAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(Comm_CardAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //构造方法
    public Comm_CardAdapter(Activity activity, List<CommCarditem> commCarditemList) {
        this.activity = activity;
        this.commCarditemList = commCarditemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
            View view = LayoutInflater.from(context).inflate(R.layout.item_comm_exhibition_card, parent,
                    false);
         ViewHolder holder=new ViewHolder(view);

        return holder;
        
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            final CommCarditem commCarditem = commCarditemList.get(position);
            if (commCarditem.getComm_card_exhibition()== null)
                 holder.comm_card_exhibition.setImageResource(R.mipmap.nopic);
            else{
                String picPath="";
                GetPicture getPicture=new GetPicture(activity,holder.comm_card_exhibition);
                List<String> picList=getPicture.picPath(commCarditem.getComm_card_exhibition());
                if(picList.size()>0)
                    picPath=picList.get(0);
                getPicture.loadPicture(picPath);
            }
                //new GetPicture(activity,  holder.comm_card_exhibition).loadPicture(commCarditem.getComm_card_exhibition());
             holder.comm_state.setText(commCarditem.getComm_state());
            holder.comm_card_name.setText(commCarditem.getComm_card_name());
            if (commCarditem.getComm_card_price() == 0.0) {
                 holder.comm_card_price.setText("");
            } else
                holder.comm_card_price.setText(String.valueOf("¥" + commCarditem.getComm_card_price()));
            if (commCarditem.getComm_values() == null) {
                holder.sale_values.setText("");
            } else {
                holder.sale_values.setText(commCarditem.getComm_values() + "件已售出");
            }

            //点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, CommoditydetailsAcitivity.class);
                    intent.putExtra("goodsId",String.valueOf(commCarditem.getCommId()));
                    preferences_getData=new SharePreferences_getData(activity);
                    if (preferences_getData.getLoginState()) {
                        ContentValues values = new ContentValues();
                        values.put("userPhone", preferences_getData.getUserID());
                        values.put("goodsID", commCarditem.getCommId());
                        new Footprint_Manager(activity).insertFootprint(values,preferences_getData.getUserID(),commCarditem.getCommId());
                        values.clear();
                    }
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

    @Override
    public int getItemCount() {
        return commCarditemList.size();
    }


    //创建一个静态类--商品列表item
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView comm_card_exhibition;//卡片的展示图片
        TextView comm_state;//商品的新旧状态
        TextView comm_card_name;//商品的名称
        TextView comm_card_price;//商品的价格
        TextView sale_values;//销量
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            comm_card_exhibition = (ImageView) itemView.findViewById(R.id.comm_card_exhibition);
            comm_state = (TextView) itemView.findViewById(R.id.comm_state);
            comm_card_name = (TextView) itemView.findViewById(R.id.comm_card_name);
            comm_card_price = (TextView) itemView.findViewById(R.id.comm_card_price);
            sale_values = (TextView) itemView.findViewById(R.id.sale_values);
        }
    }
    //7、定义点击事件回调接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}