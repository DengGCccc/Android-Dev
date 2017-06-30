package com.export.vipshop.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.export.vipshop.VIPApplication;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created with Android Studio
 * User : Kent Li
 * Date: 2015/10/12
 * Time : 17:43
 * Created by kent.li on 2015/10/12.
 */
public class PreferencesUtil {

    private static PreferencesUtil prefManger;
    private Context mCtx;
    private SharedPreferences pref;

    public static PreferencesUtil getPrefManger(){
        if(null == prefManger){
            prefManger = new PreferencesUtil(VIPApplication.getInstance());
        }

        return prefManger;
    }

    private PreferencesUtil(Context ctx) {
        pref = ctx.getSharedPreferences("com.export.vipshop", Context.MODE_PRIVATE);
        mCtx = ctx;
    }

    @SuppressLint("NewApi")
    public Set<String> readStringSet(String key) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            return pref.getStringSet(key, new HashSet<String>());
        } else {
            String items = pref.getString(key, "");
            StringTokenizer st = new StringTokenizer(items, ";");
            Set<String> itemSet = new HashSet<String>();
            while (st.hasMoreTokens()) {
                String item = st.nextToken();
                itemSet.add(item);
            }
            return itemSet;
        }
    }

    @SuppressLint("NewApi")
    public Set<String> removeSetItem(String key, String item) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            Set<String> items = pref.getStringSet(key, null);
            if (items != null) {
                items = new HashSet<String>(items);
                items.remove(item);
            }
            writeStringSet(key, items);
            return items;
        } else {
            String items = pref.getString(key, "");
            items = items.replace(item + ";", "");
            writePref(key, items);
            StringTokenizer st = new StringTokenizer(items, ";");
            Set<String> itemSet = new HashSet<String>();
            while (st.hasMoreTokens()) {
                String element = st.nextToken();
                itemSet.add(element);
            }
            return itemSet;
        }
    }

    @SuppressLint("NewApi")
    public void writeStringSet(String key, Set<String> set) {
        SharedPreferences.Editor editor = pref.edit();
        if ((null != set) && !set.isEmpty()) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                editor.putStringSet(key, set);
            } else {
                StringBuilder sb = new StringBuilder();
                for (String str : set) {
                    sb.append(str);
                    sb.append(";");
                }
                editor.putString(key, sb.toString());
            }

        } else {
            editor.remove(key);
        }

        editor.apply();
    }

    public void writePref(String key, String value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public void writePref(String key, int value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public void writePref(String key, boolean value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public void writePref(String key, long value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public void writePref(String key, float value) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putFloat(key, value);
        edit.apply();
    }

    public int readInt(String key) {
        return pref.getInt(key, 0);
    }

    public long readLong(String key) {
        return pref.getLong(key, 0);
    }

    public String readString(String key) {
        return pref.getString(key, null);
    }

    public String readString(String key, String dft) {
        return pref.getString(key, dft);
    }

    public boolean readBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public boolean readBoolean(String key, boolean def) {
        return pref.getBoolean(key, def);
    }

    public void remove(String key) {
        SharedPreferences.Editor edit = pref.edit();
        edit.remove(key);
        edit.apply();
    }
}
