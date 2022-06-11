package com.example.myapps;

public class PermissionListBean {
    private String permissionName;
    private double permissionVal ;

    public PermissionListBean(String permissionName, double permissionVal) {
        this.permissionName = permissionName;
        this.permissionVal = permissionVal;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permission_name) {
        this.permissionName = permissionName;
    }

    public double getPermissionVal() {
        return permissionVal;
    }
}
