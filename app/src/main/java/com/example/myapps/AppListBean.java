package com.example.myapps;

import android.graphics.drawable.Drawable;
import android.util.Log;


public class AppListBean {
    private String appName;

    private Drawable appIcon;
    private String permissionList;
    private String verName;
    private String appSize;
    private String userDataSize;
    private String cacheSize;

    private int minSdkVersion;
    private int targetSdkVersion;
    private long totalSize;
    private long userDataSizeBytes;
    private String appSpendTime;



    public AppListBean(String appName, Drawable appIcon,String permissionList,String verName,int minSdkVersion,int targetSdkVersion,String appSize,String userDataSize,String cacheSize,long totalSize, long userDataSizeBytes,String appSpendTime) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.permissionList = permissionList;
        this.verName = verName;
        this.minSdkVersion = minSdkVersion;
        this.targetSdkVersion = targetSdkVersion;
        this.appSize = appSize;
        this.userDataSize = userDataSize;
        this.cacheSize = cacheSize;
        this.totalSize = totalSize;
        this.userDataSizeBytes = userDataSizeBytes;
        this.appSpendTime = appSpendTime;
        Log.d("AppListBean", "onCreate: " + appName+"=="+"--"+minSdkVersion+"**"+targetSdkVersion);
    }
    public String getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(String permissionList) {
        this.permissionList = permissionList;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public int getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(int minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public String getAppSize() {
        return appSize;
    }

    public String getCacheSize() {
        return cacheSize;
    }

    public String getUserDataSize() {
        return userDataSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getUserDataSizeBytes() {
        return userDataSizeBytes;
    }


    public String getAppSpendTime() {
        return appSpendTime;
    }

}
