package io.github.isaacbao.scaffold.util;

import java.nio.charset.StandardCharsets;

/**
 * 字符串工具类
 * Created by rongyang_lu on 2016/1/1.
 */
public class StringUtil {
    /**
     * html中的空格
     */
    public static final String NBSP = "\u00a0";

    /**
     * 除去包含NBSP在内的前后空格
     *
     * @param str 要去空格的字符串
     * @return 去除空格后的字符串
     */
    public static String trim(String str) {
        if (null == str) {
            return "";
        }
        while (str.startsWith(NBSP)) {
            str = str.substring(1, str.length());
        }
        while (str.endsWith(NBSP)) {
            str = str.substring(0, str.length() - 1);
        }
        return str.trim();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static int getByteLength(String str) {
        byte[] buttonNameByte = str.getBytes(StandardCharsets.UTF_8);
        return buttonNameByte.length;
    }

    public static boolean isDigit(String str) {
        if (null != str) {
            return str.matches("^-?\\d+$");
        } else {
            return false;
        }
    }
}
