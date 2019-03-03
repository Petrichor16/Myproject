package com.ombre.woodhouse.MyView;

/**
 * Created by OMBRE on 2018/6/7.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class LoadmoreScrollView extends ScrollView {

    private OnScrollToBottomListener onScrollToBottom;

    public LoadmoreScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadmoreScrollView(Context context) {
        super(context);
    }

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