package com.ombre.woodhouse.Adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.ombre.woodhouse.DialoaFile.Loading_Dialog;

import com.ombre.woodhouse.R;

import java.util.List;

/**
 * Created by OMBRE on 2018/5/30.
 */

public class OrderBrowsingAdapter extends PagerAdapter {
    private List<RecyclerView> mDataList;
    private List<String> order_type;
    public OrderBrowsingAdapter(List<RecyclerView> mDataList, List<String> order_type) {
        this.mDataList = mDataList;
        this.order_type = order_type;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    // 初始化显示的条目对象
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mDataList.get(position));
        return mDataList.get(position);
    }


    // 销毁条目对象
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mDataList.get(position));//删除页卡
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return order_type.get(position);//页卡标题
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
