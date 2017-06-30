package com.export.vipshop.util;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.export.vipshop.VIPApplication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p> Description: Toast提示</p>
 * <p/>
 * <p> Copyright: Copyright (c) 2016 </p>
 *
 * @author: Json.Lee
 * @Version: 1.0
 * @CreateTime: 20/1/2016 16:51
 */
public class TUtil {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {Gravity.BOTTOM, Gravity.CENTER, Gravity.TOP, Gravity.LEFT, Gravity.RIGHT, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL}, flag = true)
    public @interface GravityDef {

    }

    private static Toast SHORT_T;
    private static Toast LONG_T;

    public static void shortToast(@StringRes int res) {
        shortToast(ResUtil.getString(res));
    }

    public static void longToast(@NonNull String s) {
        longShow(s);
    }

    public static void shortToast(@NonNull String s) {
        shortShow(s);
    }

    public static void longToast(@StringRes int res) {
        longToast(ResUtil.getString(res));
    }

    public static void centerLongShow(@NonNull View v) {
        showDefineToast(v, Toast.LENGTH_LONG, Gravity.CENTER, 0, 0);
    }

    public static void centerShortShow(@NonNull View v) {
        showDefineToast(v, Toast.LENGTH_SHORT, Gravity.CENTER, 0, 0);
    }

    private static void shortShow (@NonNull String msg) {
        if (null != SHORT_T && (SHORT_T.getView().isShown())
                && ((TextView)((ViewGroup)SHORT_T.getView()).getChildAt(0)).getText().toString().equals(msg)) {
            return;
        } else {
            SHORT_T = Toast.makeText(VIPApplication.getInstance(), msg, Toast.LENGTH_SHORT);
            SHORT_T.show();
        }
    }

    private static void longShow (@NonNull String msg) {
        if (null != LONG_T && (LONG_T.getView().isShown())
                && ((TextView)((ViewGroup)LONG_T.getView()).getChildAt(0)).getText().toString().equals(msg)) {
            return;
        } else {
            LONG_T = Toast.makeText(VIPApplication.getInstance(), msg, Toast.LENGTH_SHORT);
            LONG_T.show();
        }
    }

    private static void showDefineToast(@NonNull View v, int duration, @GravityDef int gravity, int xOffset, int yOffset) {
        Toast toast = new Toast(VIPApplication.getInstance());
        toast.setView(v);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        toast.show();
    }
}
