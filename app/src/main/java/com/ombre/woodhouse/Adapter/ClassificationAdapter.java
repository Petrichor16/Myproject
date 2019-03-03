package com.ombre.woodhouse.Adapter;


import android.app.Activity;
import android.content.Context;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ombre.woodhouse.Bean.Item.ClassificationItem;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by OMBRE on 2017/12/10.
 */
//二级分类适配器
public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationAdapter.ViewHolder> {

    private Context context;
    private List<ClassificationItem> classificationList;
    private Activity activity;

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //构造方法
    public ClassificationAdapter(Activity activity, List<ClassificationItem> classificationList) {
        this.activity = activity;
        this.classificationList = classificationList;
        }

    //创建一个静态类
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView calssfication_name;//种类名称
        CircleImageView calssfication_pic;//图片
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            calssfication_name = (TextView) itemView.findViewById(R.id.calssfication_name);
            calssfication_pic = (CircleImageView) itemView.findViewById(R.id.calssfication_pic);
        }
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_classificationview, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.calssfication_name.setText(classificationList.get(position).getClassificationName());
        if(classificationList.get(position).getClassificationName().equals("more"))
            holder.calssfication_pic.setImageResource(R.mipmap.more);
        else if(classificationList.get(position).getPicPath()!=null){
            String picPath="";
            GetPicture getPicture=new GetPicture(activity,holder.calssfication_pic);
            List<String> picList=getPicture.picPath(classificationList.get(position).getPicPath());
            if(picList.size()>0)
                picPath=picList.get(0);
            getPicture.loadPicture(picPath);
        }
          //  new GetPicture(activity,holder.calssfication_pic).loadPicture(classificationList.get(position).getPicPath());//加载图片
        if(mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classificationList.size();
    }

    //7、定义点击事件回调接口
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }


}