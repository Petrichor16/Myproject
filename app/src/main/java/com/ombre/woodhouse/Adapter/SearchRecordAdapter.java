package com.ombre.woodhouse.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Bean.Address;
import com.ombre.woodhouse.DB.DBManager.Address_Manager;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.UI.activity.AddNewAddressActivity;

import java.util.List;


/**
 * Created by OMBRE on 2017/12/10.
 */
//s搜索记录适配器
public class SearchRecordAdapter extends RecyclerView.Adapter<SearchRecordAdapter.ViewHolder> {


    private Context context;
    private List<String> mData;
    private Activity activity;

    //定义监听并设set方法
    private SearchRecordAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(SearchRecordAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //构造方法
    public SearchRecordAdapter(Activity activity, List<String> mData) {
        this.activity = activity;
        this.mData = mData;
    }

    //创建一个静态类
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_searchRecord;//
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txt_searchRecord = (TextView) itemView.findViewById(R.id.txt_searchRecord);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_searchrecord, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.txt_searchRecord.setText(mData.get(position));
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