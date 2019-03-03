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

import com.ombre.woodhouse.Bean.Item.CommCarditem;
import com.ombre.woodhouse.Bean.Item.PartGoodsItem;
import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.R;
import com.ombre.woodhouse.UI.activity.CommoditydetailsAcitivity;

import java.util.List;

/**
 * Created by OMBRE on 2017/12/10.
 */
//商品卡片适配器
public class PartGoodsAdapter extends RecyclerView.Adapter<PartGoodsAdapter.ViewHolder> {


    private Context context;
    private List<PartGoodsItem> itemList;
    private Activity activity;

    //构造方法
    public PartGoodsAdapter(Activity activity, List<PartGoodsItem> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
            View view = LayoutInflater.from(context).inflate(R.layout.item_partgoods, parent,
                    false);
         ViewHolder holder=new ViewHolder(view);

        return holder;
        
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final PartGoodsItem item=itemList.get(position);
            if (item.getGoodsPath()== null)
                 holder.part_goodsImage.setImageResource(R.mipmap.nopic);
            else
            {
                String picPath="";
                GetPicture getPicture=new GetPicture(activity,holder.part_goodsImage);
                List<String> picList=getPicture.picPath(item.getGoodsPath());
                if(picList.size()>0)
                    picPath=picList.get(0);
                getPicture.loadPicture(picPath);
            }
             //   new GetPicture(activity,  holder.part_goodsImage).loadPicture(item.getGoodsPath());
             holder.part_goodsName.setText(item.getGoodsName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, CommoditydetailsAcitivity.class);
                    intent.putExtra("goodsId",String.valueOf(item.getGoodsId()));
                    activity.startActivity(intent);
                }
            });
        }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    //创建一个静态类--商品列表item
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView part_goodsImage;//卡片的展示图片
        TextView part_goodsName;//商品的名称
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            part_goodsImage = (ImageView) itemView.findViewById(R.id.part_goodsImage);
            part_goodsName = (TextView) itemView.findViewById(R.id.part_goodsName);

        }
    }

}