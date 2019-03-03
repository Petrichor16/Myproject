package com.ombre.woodhouse.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ombre.woodhouse.InterfaceService.GetPicture;
import com.ombre.woodhouse.SharedPreferences.SharePreferences_Manager;
import com.ombre.woodhouse.UI.activity.CommoditydetailsAcitivity;
import com.ombre.woodhouse.UI.activity.PicExhibitionActivity;

import java.util.List;

import static android.R.attr.maxWidth;
import static android.R.attr.width;

/**
 * Created by OMBRE on 2018/5/22.
 */

public class SowingmapAdapter extends PagerAdapter {
    Activity activity;
    List<String> imgRes;
    //定义监听并设set方法
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public SowingmapAdapter(Activity activity, List<String> imgRes) {
        this.activity = activity;
        this.imgRes=imgRes;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView view = new ImageView(activity);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        if(imgRes!=null){
        String picPath=imgRes.get(position%imgRes.size());
        new GetPicture(activity, view).loadPicture(picPath);}
        container.addView(view);
        if(null != mOnItemClickListener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            });
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    //7、定义点击事件回调接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}
