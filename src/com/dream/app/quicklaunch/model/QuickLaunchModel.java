package com.dream.app.quicklaunch.model;

import android.graphics.drawable.Drawable;

/**
 * Created by liushulong on 2014/11/25.
 */
public class QuickLaunchModel {
    private String appName;
    private String packageName;
    private Drawable appLogo;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(Drawable appLogo) {
        this.appLogo = appLogo;
    }
}
