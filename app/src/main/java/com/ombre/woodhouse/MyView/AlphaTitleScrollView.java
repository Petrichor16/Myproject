package com.ombre.woodhouse.MyView;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by OMBRE on 2018/5/22.
 */

//自定义scrollview实现上滑标题栏颜色渐变效果
public class AlphaTitleScrollView extends ScrollView {
    public static final String TAG = "AlphaTitleScrollView";
    private int mSlop;
    private LinearLayout toolbar;
    TextView textView;
    View view;
    int initAlpha;
    private OnScrollToBottomListener onScrollToBottom;

    public AlphaTitleScrollView(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AlphaTitleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public AlphaTitleScrollView(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        // mSlop = ViewConfiguration.get(context).getScaledDoubleTapSlop();
        mSlop = initAlpha;
        Log.i(TAG, mSlop + "");
    }

    /**
     *  @param /headLayout
     *            头部布局
     * @param /imageview
     * @param :headView
     */
   /* public void setTitleAndHead(LinearLayout toolbar, ViewPager headView) {
        this.toolbar = toolbar;
        this.headView = headView;
    }*/
 /*   public void setTitleAndHead(LinearLayout toolbar,LinearLayout person_information) {
        this.toolbar = toolbar;
        this.person_information = person_information;
    }*/
    public void setTitleAndHead(LinearLayout toolbar, TextView textView, View view,int initAlpha) {
        this.toolbar = toolbar;
        this.textView=textView;
        this.view=view;
        this.initAlpha=initAlpha;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        float headHeight = view.getMeasuredHeight()
                - toolbar.getMeasuredHeight();
        int alpha = (int) (((float) t / headHeight) * 255);
        if (alpha >= 255)
            alpha = 255;
        if (alpha <= mSlop)
            alpha = initAlpha;
        toolbar.getBackground().setAlpha(alpha);
        //textView.get().withAlpha(alpha);
        if(textView!=null)
           textView.setTextColor(textView.getTextColors().withAlpha(alpha));
        //文字透明度
        super.onScrollChanged(l, t, oldl, oldt);
    }

    //实现上拉至底部监听
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                  boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(scrollY != 0 && null != onScrollToBottom){
            onScrollToBottom.onScrollBottomListener(clampedY);
        }
    }
    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){
        onScrollToBottom = listener;
    }
    public interface OnScrollToBottomListener{
        public void onScrollBottomListener(boolean isBottom);
    }

}