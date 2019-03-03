package com.ombre.woodhouse.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ombre.woodhouse.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMBRE on 2017/12/10.
 */
//一级分类适配器
public class GeneraltypeAdapter extends RecyclerView.Adapter<GeneraltypeAdapter.ViewHolder> {

    private Context context;
    private List<String> mData;
    private Activity activity;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    //定义监听并设set方法
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //构造方法
    public GeneraltypeAdapter(Activity activity, List<String> mData) {
        this.activity = activity;
        this.mData = mData;
        isClicks = new ArrayList<>();
        for(int i = 0;i<mData.size();i++){
            if(i==0)
                isClicks.add(true);
            isClicks.add(false);
        }
        }

    //创建一个静态类
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView general_type_name;//种类名称
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            general_type_name = (TextView) itemView.findViewById(R.id.general_type_name);
        }
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_general_type, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      holder.general_type_name.setText(mData.get(position));
        //4：设置点击事件
        if(mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    for(int i = 0; i <isClicks.size();i++){
                        isClicks.set(i,false);
                    }
                    isClicks.set(position,true);
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        //5、记录要更改属性的控件
        holder.itemView.setTag(holder.general_type_name);
        //6、判断改变属性
        if(isClicks.get(position)){
            holder.general_type_name.setBackgroundResource(R.color.colorWhite);
        }else{
            holder.general_type_name.setBackgroundResource(R.color.generalitem_check_false);
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //7、定义点击事件回调接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}