package com.export.vipshop.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.export.vipshop.R;
import com.export.vipshop.VIPApplication;

/**
 * <p> Description: </p>
 * <p/>
 * <p> Copyright: Copyright (c) 2016 </p>
 *
 * @author: Json.Lee
 * @Version: 1.0
 * @CreateTime: 25/1/2016 17:39
 */
public class ResUtil {

    public static String getString(@StringRes int strId) {
        return VIPApplication.getInstance().getString(strId);
    }

    public static int getColor(@ColorRes int colorId) {
        return VIPApplication.getInstance().getResources().getColor(colorId);
    }

    public static Drawable getDrawable(@DrawableRes int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return VIPApplication.getInstance().getResources().getDrawable(drawableId, VIPApplication.getInstance().getTheme());
        }else {
            return VIPApplication.getInstance().getResources().getDrawable(drawableId);
        }
    }

    public static int getDimen(@DimenRes int dimenId) {
        return VIPApplication.getInstance().getResources().getDimensionPixelSize(dimenId);
    }
}
