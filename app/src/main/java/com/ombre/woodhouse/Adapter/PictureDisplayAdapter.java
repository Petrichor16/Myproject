package com.ombre.woodhouse.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ombre.woodhouse.Bean.Item.CommCarditem;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;

import java.util.List;

/**
 * Created by OMBRE on 2018/6/8.
 */

public class PictureDisplayAdapter extends RecyclerView.Adapter<PictureDisplayAdapter.ViewHolder> {

    private Activity activity;
    private List<String> picPathList;
    private Context context;

    //定义监听并设set方法
    private PictureDisplayAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(PictureDisplayAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public PictureDisplayAdapter(Activity activity, List<String> picPathList) {
        this.activity = activity;
        this.picPathList = picPathList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_picture, parent,
                false);
       ViewHolder holder=new ViewHolder(view) ;
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String  picPath = picPathList.get(position);
        if(!picPath.equals(""))
            new GetPicture(activity,  holder.detailspicture).loadPicture(picPath);
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
        return picPathList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView detailspicture;
        public ViewHolder(View itemView) {
            super(itemView);
            detailspicture=(ImageView)itemView.findViewById(R.id.detailspicture);
        }
    }
    //7、定义点击事件回调接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}
