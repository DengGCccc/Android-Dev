package com.export.vipshop.util;

import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;


/**
 * @author Devin.Hu
 * @version V1.0
 * @date 2011-6-24
 * @description JsonUtils工具类：采用Gson，google的一个开源项目,实现json对象的解析以及json对象跟java对象的互转
 */
public class JsonUtils {

    private static Gson gson = new Gson();

    /**
     * 将json对象转换成java对象
     */
    public static String getJson2String(String jsonData, String key) throws JSONException {

        if (null == key) return null;

        JSONObject obj = new JSONObject(jsonData.trim());
        String value = obj.get(key).toString();
        return value;
    }

    /**
     * 将java对象转换成json对象
     *
     * @param obj
     * @return
     */
    public static String parseObj2Json(Object obj) {

        if (null == obj) return null;
        String objstr = gson.toJson(obj);
        return objstr;
    }


    /**
     * 将java对象的属性转换成json的key
     *
     * @param obj
     * @return
     */
    public static String parseObj2JsonOnField(Object obj) {

        if (null == obj) return null;

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
        String objstr = gson.toJson(obj);
        return objstr;
    }

    /**
     * 将json对象转换成java对象
     *
     * @param <T>
     * @param jsonData
     * @param c
     * @return
     */
    public static <T> T parseJson2Obj(String jsonData, Class<T> c) {
        if (null == jsonData) return null;
        T obj = gson.fromJson(jsonData.trim(), c);
        return obj;
    }

    public static <T> T parseJson2Obj(String jsonData, Type typeOfT) {
        if (!isJsonType(jsonData)) return null;
        return gson.fromJson(jsonData.trim(), typeOfT);
    }

    private static boolean isJsonType(String data) {
        if(TextUtils.isEmpty(data)){
            return false;
        }

        String firstChar = data.trim().substring(0,1);

        if("{".equals(firstChar) || "[".equals(firstChar)){
            return true;
        }

        return false;
    }

    /**
     * 将json对象转换成数组对象
     *
     * @param <T>
     * @param jsonData
     * @param c
     * @return
     * @throws JSONException
     */
    public static <T> ArrayList<T> parseJson2List(String jsonData, Class<T> c) throws JSONException {

        if (null == jsonData || "".equals(jsonData)) return null;
        ArrayList<T> list = new ArrayList<T>();
        JSONArray jsonArray = new JSONArray(jsonData.trim());
        JSONObject objItem = null;
        T objT = null;
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            objItem = (JSONObject) jsonArray.get(i);
            if (null != objItem) {
                objT = gson.fromJson(objItem.toString(), c);
                list.add(objT);
            }
        }

//		if(Config.DEBUG)
//        	Log.i("parseJson2List", list.toString());
        return list;
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Map<String, String> fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }
}