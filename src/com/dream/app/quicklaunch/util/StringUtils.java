package com.dream.app.quicklaunch.util;

/**
 * Created by liushulong on 2014/11/27.
 */
public class StringUtils {
    /**
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() < 1;
    }
}
