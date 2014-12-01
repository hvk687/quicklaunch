package com.dream.app.quicklaunch.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import com.alibaba.fastjson.JSON;
import com.dream.app.quicklaunch.model.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liushulong on 2014/12/1.
 */
public class Utils {
    private static final String SELECTED_APP_LIST = "selected_app_list";

    /**
     *
     */
    public static void saveSelectedApp(List<AppInfo> appInfoList, Context context) {
        try {
            String jsonString = JSON.toJSONString(appInfoList);
            SharedPreferenceUtils.setSharedPreferences(SELECTED_APP_LIST, jsonString, context);
        } catch (Exception e) {
            SharedPreferenceUtils.setSharedPreferences(SELECTED_APP_LIST, "", context);
        }
    }

    /**
     *
     */
    public static List<AppInfo> loadSelectedApp(Context context) {
        List<AppInfo> mSelectedAppInfos = new ArrayList<AppInfo>();
        try {
            String json = SharedPreferenceUtils.getSharedPreferences(SELECTED_APP_LIST, context);
            if (!StringUtils.isNullOrEmpty(json)) {
                mSelectedAppInfos = JSON.parseArray(json, AppInfo.class);
                parseDrawable(mSelectedAppInfos, context);
                return mSelectedAppInfos;
            }
        } catch (Exception e) {
        }
        /**
         * if code run here, it means, we should load default
         * app info
         */
        AppInfo home = loadDefaultHome(context);
        if (home != null) {
            mSelectedAppInfos.add(home);
        }
        AppInfo dialer = loadDefaultPhoneApp(context);
        if (dialer != null) {
            mSelectedAppInfos.add(dialer);
        }
        return mSelectedAppInfos;
    }

    /**
     * after load saved app information,
     * we should set the drawable for them
     */
    public static void parseDrawable(List<AppInfo> slectedAppInfos, Context context) {
        if (slectedAppInfos == null || slectedAppInfos.size() <= 0) {
            return;
        }
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象

        for (AppInfo info : slectedAppInfos) {
            Intent mainIntent = new Intent();
            mainIntent.setPackage(info.getPkgName());
            List<ResolveInfo> resolveInfos = pm
                    .queryIntentActivities(mainIntent, 0);
            if (resolveInfos != null && resolveInfos.size() > 0) {
                AppInfo parsed = makeAppInfo(resolveInfos.get(0), pm);
                info.setIntent(parsed.getIntent());
                info.setAppIcon(parsed.getAppIcon());
            }
        }
    }

    /**
     * start home activity
     */
    public static AppInfo loadDefaultHome(Context context) {
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_HOME);

        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfos != null && resolveInfos.size() > 0) {
            return makeAppInfo(resolveInfos.get(0), pm);
        }

        return null;
    }

    public static AppInfo loadDefaultPhoneApp(Context context) {
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象

        Intent mainIntent = new Intent(Intent.ACTION_DIAL);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos != null && resolveInfos.size() > 0) {
            return makeAppInfo(resolveInfos.get(0), pm); // 添加至列表中
        }

        return null;
    }

    public static AppInfo makeAppInfo(ResolveInfo reInfo, PackageManager pm) {
        String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
        String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
        String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
        Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
        // 为应用程序的启动Activity 准备Intent
        Intent launchIntent = new Intent();
        launchIntent.setComponent(new ComponentName(pkgName, activityName));
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 创建一个AppInfo对象，并赋值
        AppInfo appInfo = new AppInfo();
        appInfo.setAppLabel(appLabel);
        appInfo.setPkgName(pkgName);
        appInfo.setAppIcon(icon);
        appInfo.setIntent(launchIntent);
        return appInfo;
    }

    public static List<AppInfo> queryAppInfo(Context context) {
        List<AppInfo> installedAppInfo = new ArrayList<AppInfo>();
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, 0);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        if (installedAppInfo == null) {
            installedAppInfo = new ArrayList<AppInfo>();
        }
        installedAppInfo.clear();
        //add home to the first
        installedAppInfo.add(loadDefaultHome(context));
        //add other app info
        for (ResolveInfo reInfo : resolveInfos) {
            installedAppInfo.add(makeAppInfo(reInfo, pm)); // 添加至列表中
        }

        return installedAppInfo;
    }
}
