package com.export.vipshop.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.export.vipshop.VIPApplication;

import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by kent.li on 2015/9/10.
 */
public final class AppUtil {
    static final Object sMainLock = new Object();
    static volatile Boolean sMainProcess;
    static final Object sNameLock = new Object();
    static volatile String sProcessName;


    public static final String getAppVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            localNameNotFoundException.printStackTrace();
        }
        return null;
    }

    public static final int getAppVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            localNameNotFoundException.printStackTrace();
        }
        return -1;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getTopActivity(Context context) {
        List allRunnableList = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        String str = null;
        if (allRunnableList != null) {
            str = ((ActivityManager.RunningTaskInfo) allRunnableList.get(0)).topActivity.getClassName();
        }
        return str;
    }

    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断对应的Service是否正在运行
     *
     * @param context      上下文
     * @param serviceClass 对应的Service类
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator<ActivityManager.RunningServiceInfo> servicesIt = activityManager
                .getRunningServices(Integer.MAX_VALUE).iterator();
        while (servicesIt.hasNext()) {
            ActivityManager.RunningServiceInfo info = servicesIt.next();
            if (info.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String getTopActivityName(Context context) {
        List allRunnableList = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        String str = null;
        if (allRunnableList != null) {
            str = ((ActivityManager.RunningTaskInfo) allRunnableList.get(0)).topActivity.getClassName();
        }
        return str;
    }

    @SuppressLint({"NewApi"})
    public static boolean isMainProcess(Context context) {
        if (sMainProcess != null) {
            return sMainProcess.booleanValue();
        }
        synchronized (sMainLock) {
            if (sMainProcess != null) {
                return sMainProcess.booleanValue();
            }
        }
        String str = myProcessName(context);
        if (str == null) {
            return false;
        }
        sMainProcess = Boolean.valueOf(str.equals(context.getApplicationInfo().processName));
        return sMainProcess.booleanValue();
    }

    public static boolean isRunningForeground(Context context) {
        String packagerName = getPackageName(context);
        String topActivityName = getTopActivityName(context);
        return (packagerName != null) && (topActivityName != null) && (topActivityName.startsWith(packagerName));
    }

    public static String myProcessName(Context context) {
        if (sProcessName != null) {
            return sProcessName;
        }
        synchronized (sNameLock) {
            if (sProcessName != null) {
                return sProcessName;
            }
        }
        String str1 = obtainProcessName(context);
        sProcessName = str1;
        return str1;
    }

    static String obtainProcessName(Context context) {
        List runnableActivityList = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
        Iterator iterator = null;
        if ((runnableActivityList != null) && (runnableActivityList.size() > 0)) {
            iterator = runnableActivityList.iterator();
        }

        if (null == iterator) {
            return null;
        }

        ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo;
        do {
            if (!iterator.hasNext()) {
                return null;
            }
            localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) iterator.next();
        }
        while ((localRunningAppProcessInfo == null) || (localRunningAppProcessInfo.pid != android.os.Process.myPid()));
        return localRunningAppProcessInfo.processName;
    }

    public static boolean stackHasMoreActivityAlive(Context context) {
        String packagetName = getPackageName(context);
        List runnableActivityList = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        if (runnableActivityList == null) {
            return false;
        }
        try {
            String className = ((ActivityManager.RunningTaskInfo) runnableActivityList.get(0)).topActivity
                    .getClassName();
            int numRunning = 0;
            if ((packagetName != null) && (className != null) && (className.startsWith(packagetName))) {
                numRunning = ((ActivityManager.RunningTaskInfo) runnableActivityList.get(0)).numRunning;
            }
            return 1 != numRunning;
        } catch (Exception localException) {
            //
        }
        return false;
    }

    public static long getAvailMemory(Context context) {
        ActivityManager localActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
        localActivityManager.getMemoryInfo(localMemoryInfo);
        return localMemoryInfo.availMem;
    }

    public static void requestRootPermission() {
        String apkRoot = "chmod 777 " + VIPApplication.getInstance().getPackageCodePath();
        RootCmd(apkRoot);
    }

    public static boolean RootCmd(String cmd) {
        java.lang.Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {

            }
            process.destroy();
        }
        return true;
    }

    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable) {
        if (hasJELLYBEAN()) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static void hideKeyBoard(Activity context) {
        View view = context.getCurrentFocus();
        if (null != view) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //弹出popup时，背景变灰; 消失，背景恢复
    public static void setBgGray(Activity activity, boolean isBgGray) {
        if (isBgGray) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 0.5f;
            activity.getWindow().setAttributes(lp);
        } else {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 1f;
            activity.getWindow().setAttributes(lp);
        }
    }

}

