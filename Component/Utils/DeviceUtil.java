package com.export.vipshop.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.export.vipshop.R;
import com.export.vipshop.VIPApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.lang.reflect.Method;

/**
 * Created by kent.li on 2015/9/10.
 */
public class DeviceUtil {
    private static String sDeviceScreenSize = "";//1920*1080
    private static String NETWORk_2G = "2g";
    private static String NETWORK_3G = "3g";
    private static String NETWORK_4G = "4g";
    private static String NETWORK_WIFI = "wifi";

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static String getIMEI(Context context) {
        if (!hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        if (imei == null) {
            imei = "";
        }
        return imei;
    }

    public static String getIMSI(Context context) {
        if (!hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        if (imsi == null) {
            imsi = "";
        }
        return imsi;
    }

    public static String getLocalMacAddress(Context context) {
        String macAddress = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "";
        }
        return macAddress;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static String getScreenSizeString(Context context) {
        if (TextUtils.isEmpty(sDeviceScreenSize)) {
            sDeviceScreenSize = new StringBuilder(String.valueOf(getScreenHeight(context))).append("*").append(String.valueOf(getScreenWidth(context))).toString();
        }
        return sDeviceScreenSize;
    }

    public static String getPhoneName() {
        return StringUtil.replaceBlank(Build.MODEL);
    }

    public static int getOSIntVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    public static String getNetworkType(){
        String netType = NETWORk_2G;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) VIPApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = NETWORK_WIFI;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) VIPApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NETWORK_4G;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NETWORK_3G;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NETWORk_2G;
            } else {
                netType = NETWORk_2G;
            }
        }
        return netType;
    }

    public boolean checkGoogleService(Activity context) {
        final int REQ_GS_CHECK_CODE = 1234567890;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS != result) {
            if (apiAvailability.isUserResolvableError(result)) {
                GooglePlayServicesUtil.getErrorDialog(result, context, REQ_GS_CHECK_CODE).show();
                return false;
            } else {
                LogUtils.i("DeviceUtil", "The device is not supported!");
                return false;
            }
        }
        return true;
    }



    //获取虚拟按键的高度
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        if (DeviceUtil.getPhoneName().equals("LG-D858"))    //特殊机型
            return false;

        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
//            // 未经验证－－－－－
//            if ("1".equals(sNavBarOverride)) {
//                hasNav = false;
//            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }
    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return sNavBarOverride;
    }

    public static boolean hasPermission(Context context, String permisson) {
        PermissionsChecker permissionsChecker = new PermissionsChecker(context);
        boolean b = permissionsChecker.lacksPermissions(permisson);
        return !b;
    }

    public static boolean checkPermission(Context context, Activity activity, String... permissons) {

        boolean b = true;

        for (String permission : permissons) {
            if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 99);
                    Toast.makeText(context, R.string.no_permission_tips, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.no_permission_tips, Toast.LENGTH_SHORT).show();
                }

                b = false;
            }
        }

        return b;
    }
}
