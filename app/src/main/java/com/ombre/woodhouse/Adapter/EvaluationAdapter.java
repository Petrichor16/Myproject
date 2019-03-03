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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Bean.Address;
import com.ombre.woodhouse.Bean.Evaluation;
import com.ombre.woodhouse.DB.DBManager.Address_Manager;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.UI.activity.AddNewAddressActivity;

import java.util.List;


/**
 * Created by OMBRE on 2017/12/10.
 */
//评论适配器
public class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.ViewHolder> {


    private Context context;
    private List<Evaluation> evaluationList;
    private Activity activity;

    //定义监听并设set方法
    private EvaluationAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(EvaluationAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //构造方法
    public EvaluationAdapter(Activity activity, List<Evaluation> evaluationList) {
        this.activity = activity;
        this.evaluationList = evaluationList;
    }

    //创建一个静态类
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_EvaName;//用户名或昵称
        TextView item_EvaContent;//评论内容
        RatingBar item_startLevel;//评价星级
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            item_EvaName = (TextView) itemView.findViewById(R.id.item_EvaName);
            item_EvaContent = (TextView) itemView.findViewById(R.id.item_EvaContent);
            item_startLevel=(RatingBar) itemView.findViewById(R.id.item_startLevel);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_evaluation, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Evaluation evaluation=evaluationList.get(position);
            holder.item_EvaName.setText(evaluation.getUserName());
            holder.item_EvaContent.setText(evaluation.getContent());
            holder.item_startLevel.setRating(evaluation.getStartLevel());
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
        return evaluationList.size();
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