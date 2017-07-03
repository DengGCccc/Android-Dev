package com.export.vipshop.util;

import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.export.vipshop.R;
import com.export.vipshop.helper.Constants;
import com.export.vipshop.model.simple.AddressBean;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kent.li on 2015/9/7.
 */
public class StringUtil {

    /**
     * 替换空格 换行符 回车 空格
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     *  抽取 URL 里的传参
     *  @param url 
     *  @return map
     */
    public static TreeMap<String, String> getParam(URL url) throws UnsupportedEncodingException {
        TreeMap<String, String> query_pairs = new TreeMap<>();
        String query = url.getQuery();

        if (null != query) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }

        return query_pairs;
    }

    /**
     * 描述：是否是邮箱.
     *
     * @param str 指定的字符串
     * @return 是否是邮箱:是为true，否则false
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    /**
     * 是否存在数字和字母
     *
     * @param str
     * @return
     */
    public static Boolean hasNumberLetter(String str) {
        Boolean isNoLetter = false;
        String letterRegex = "^.*[a-zA-Z]+.*$";
        String numRegex = "^.*[0-9]+.*$";
        if (str.matches(letterRegex) && str.matches(numRegex)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    /**
     * 转译html代码, 将实体转化为 、 < 、> 、 & 、 " 、' 、等
     *
     * @param str
     * @return
     */
    public static String convertHtml(@NonNull String str) {
        str = str.replace("&nbsp;", " ");
        str = str.replace("&quot;", "\"");
        str = str.replace("&apos;", "\'");
        str = str.replace("&acute;", "\'");
        str = str.replace("<br/>", "\n");
        str = str.replace("&gt;", ">");
        str = str.replace("&lt;", "<");
        str = str.replace("&amp;", "&");
        return str;
    }

    /**
     * 对 Url 里的传参进行编码
     *
     * @param param
     * @return
     */
    public static String encodeParam(String param) {
        if(TextUtils.isEmpty(param))return "";

        String result = param;
        try {
            result = URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * MD5 编码
     *
     * @param plainText
     * @return
     */
    public static String string2MD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
   
}
