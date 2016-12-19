package com.osotto.hotelmanage.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by deng on 16/4/25.
 */
public class RVDividerSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RVDividerSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}
