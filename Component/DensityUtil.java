package com.export.vipshop.util;

import android.content.Context;

/**
 * Created by kent.li on 2015/9/10.
 */

public class DensityUtil {

	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
	
	public static int getHeightPixels(Context context) {
		if(context!=null)
			return context.getResources().getDisplayMetrics().heightPixels;
		return 0;
	}
	
	public static int getWidthPixels(Context context) {
		if(context!=null)
			return context.getResources().getDisplayMetrics().widthPixels;
		return 0;
	}
}
