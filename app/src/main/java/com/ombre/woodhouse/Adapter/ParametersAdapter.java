package com.ombre.woodhouse.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ombre.woodhouse.Bean.Param;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;

import java.util.List;

/**
 * Created by OMBRE on 2018/6/8.
 */

//参数适配器
public class ParametersAdapter extends RecyclerView.Adapter<ParametersAdapter.ViewHolder> {

    private Activity activity;
    private List<Param> paramList;
    private Context context;

    //定义监听并设set方法
    private ParametersAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(ParametersAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public ParametersAdapter(Activity activity, List<Param> paramList) {
        this.activity = activity;
        this.paramList = paramList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_para, parent, false);
       ViewHolder holder=new ViewHolder(view) ;
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            Param param = paramList.get(position);
            holder.paramName.setText(param.getParamName()+"：");
            holder.paramText.setText(param.getParamText());
            if (null != mOnItemClickListener) {
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
        return paramList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView paramName;//参数名
        TextView paramText;//参数内容
        public ViewHolder(View itemView) {
            super(itemView);
            paramName=(TextView) itemView.findViewById(R.id.paramName);
            paramText=(TextView) itemView.findViewById(R.id.paramText);
        }
    }
    //7、定义点击事件回调接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

}
