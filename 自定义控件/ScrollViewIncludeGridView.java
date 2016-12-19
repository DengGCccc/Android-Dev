package com.osotto.hotelmanage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by deng on 16/2/26.
 */
public class ScrollViewIncludeGridView extends GridView {

    public ScrollViewIncludeGridView(Context context) {
        super(context);
    }

    public ScrollViewIncludeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewIncludeGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}