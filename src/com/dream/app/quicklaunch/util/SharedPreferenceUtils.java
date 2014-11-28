package com.dream.app.quicklaunch.util;

import android.content.Context;
import android.preference.PreferenceActivity;

public class SharedPreferenceUtils {
    public static final String PREFERENCE_NAME = "quick_launch";

    public static String getSharedPreferences(String propertyName, Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME,
                PreferenceActivity.MODE_PRIVATE).getString(propertyName, "");
    }

    public static String getSharedPreferences(String preferenceName, String propertyName, Context context) {
        return context.getSharedPreferences(preferenceName, PreferenceActivity.MODE_PRIVATE).getString(propertyName, "");
    }

    public static int getSharedPreferences(String preferenceName, String propertyName, Context context, int defaultValue) {
        return context.getSharedPreferences(preferenceName, PreferenceActivity.MODE_PRIVATE).getInt(propertyName, defaultValue);
    }

    public static long getSharedPreferences(String preferenceName, String propertyName, Context context, long defaultValue) {
        return context.getSharedPreferences(preferenceName, PreferenceActivity.MODE_PRIVATE).getLong(propertyName, defaultValue);
    }

    public static boolean getSharedPreferences(String propertyName, Context context, boolean defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME,
                PreferenceActivity.MODE_PRIVATE).getBoolean(propertyName, defaultValue);
    }

    public static boolean getSharedPreferences(String preferenceName, String propertyName, Context context, boolean defaultValue) {
        return context.getSharedPreferences(preferenceName, PreferenceActivity.MODE_PRIVATE).getBoolean(propertyName, defaultValue);
    }

    public static long getSharedPreferences(String propertyName, Context context, long defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, PreferenceActivity.MODE_PRIVATE).getLong(propertyName, defaultValue);
    }

    public static int getSharedPreferences(String propertyName, Context context, int defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, PreferenceActivity.MODE_PRIVATE).getInt(propertyName, defaultValue);
    }

    public static boolean setSharedPreferences(String propertyName, int propertyValue, Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putInt(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String preferenceName, String propertyName, boolean propertyValue, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit().putBoolean(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String propertyName, long propertyValue, Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
                .putLong(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String preferenceName, String propertyName, String propertyValue, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit()
                .putString(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String preferenceName, String propertyName, int propertyValue, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit()
                .putInt(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String preferenceName, String propertyName, long propertyValue, Context context) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit()
                .putLong(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String propertyName, String propertyValue, Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
                .putString(propertyName, propertyValue).commit();
    }

    public static boolean setSharedPreferences(String propertyName, boolean propertyValue, Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
                .putBoolean(propertyName, propertyValue).commit();
    }
}
