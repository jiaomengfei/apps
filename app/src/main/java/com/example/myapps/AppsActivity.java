package com.example.myapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AppsActivity extends AppCompatActivity {
    PackageManager packageManager;
    private List<AppListBean> appList = new ArrayList<AppListBean>();
    private static DecimalFormat df = new DecimalFormat("0.00");
    private AppsListAdapter mAdapter;
    public static int mBatteryLevel = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getPermission();
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        initData();
    }

    private void getPermission() {

        int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1;
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED) {
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
        }
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    }

    private void initData() {
        packageManager = getPackageManager();
        List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        List<PackageInfo> packageList1 = new ArrayList<PackageInfo>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -5);
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> lUsageStatsMap = mUsageStatsManager.
                queryAndAggregateUsageStats(cal.getTimeInMillis(), System.currentTimeMillis());
        int api_level = Build.VERSION.SDK_INT;
        String maxCupFred = getMaxCpuFreq();
        String networkType = getNetworkState(this);
        for (PackageInfo pi : packageList) {
            boolean b = isSystemPackage(pi);

            packageList1.add(pi);
            PackageInfo packageInfo = (PackageInfo) pi;


            String appName = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();


            Drawable appIcon = packageManager.getApplicationIcon(packageInfo.applicationInfo);
            List<String> granted = new ArrayList<String>();

            String packageName = pi.packageName;
            String verName = pi.versionName;
            int minSdkVersion = 0;
            int targetSdkVersion = pi.applicationInfo.targetSdkVersion;

            Log.d("packageName", packageName);
            Log.d("verName", "onCreate: " + appName + "==" + "--" + "**" + pi.applicationInfo.targetSdkVersion);

            String appSize = "0.0KB";
            String userDataSize = "";
            String cacheSize = "";
            long totalSize = 0;
            long userDataSizeBytes = 0;
            if (api_level >= 26) {
                final StorageStatsManager storageStatsManager = (StorageStatsManager) getApplicationContext().getSystemService(Context.STORAGE_STATS_SERVICE);

                try {

                    @SuppressLint("WrongThread")
                    StorageStats storageStats = storageStatsManager.queryStatsForUid(pi.applicationInfo.storageUuid, pi.applicationInfo.uid);
                    double dataSizeDouble = (double) storageStats.getDataBytes() / (1024 * 1024);
                    userDataSize = String.valueOf(df.format(dataSizeDouble)) + "MB";
                    if (dataSizeDouble < 1) {
                        dataSizeDouble = (double) storageStats.getDataBytes() / (1024);
                        userDataSize = String.valueOf(df.format(dataSizeDouble)) + "KB";
                    }
                    double appSizeDouble = (double) storageStats.getAppBytes() / (1024 * 1024);
                    appSize = String.valueOf(df.format(appSizeDouble)) + "MB";
                    if (appSizeDouble < 1) {
                        appSizeDouble = (double) storageStats.getAppBytes() / (1024);
                        appSize = String.valueOf(df.format(appSizeDouble)) + "KB";
                    }
                    double cacheSizeDouble = (double) storageStats.getCacheBytes() / (1024 * 1024);
                    cacheSize = String.valueOf(df.format(cacheSizeDouble)) + "MB";
                    if (cacheSizeDouble < 1) {
                        cacheSizeDouble = (double) storageStats.getCacheBytes() / (1024);
                        cacheSize = String.valueOf(df.format(cacheSizeDouble)) + "KB";
                    }
                    userDataSizeBytes = storageStats.getDataBytes();
                    totalSize = storageStats.getDataBytes() + storageStats.getAppBytes() + storageStats.getCacheBytes();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String permissionWithVal = "";
            if (pi.requestedPermissions != null) {
                for (int i = 0; i < pi.requestedPermissions.length; i++) {
                    Log.d("granted2", "onCreate: " + pi.requestedPermissions[i].toString());
                    String permissionName = removePackegeFromPermissionName(pi.requestedPermissions[i]);
                    granted.add(pi.requestedPermissions[i]);
                    permissionWithVal += permissionName ;
                }

                if (!permissionWithVal.equals("")) {
                    permissionWithVal = permissionWithVal.substring(0, permissionWithVal.length() - 1);
                }
            }
                JSONObject jo = new JSONObject();
                JSONArray jsArray = new JSONArray(granted);
                try {
                    jo.put("user_permissions", jsArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                UsageStats existingStats =
                        lUsageStatsMap.get(packageName);
                String appSpendTime = "-";
                if (existingStats != null) {
                    long totalTimeUsageInMillis = lUsageStatsMap.get(packageName).
                            getTotalTimeInForeground();
                    appSpendTime = String.valueOf(DateUtils.formatElapsedTime(totalTimeUsageInMillis / 1000));
                }
                appList.add(new AppListBean(appName, appIcon, jo.toString(), verName, minSdkVersion, targetSdkVersion, appSize, userDataSize, cacheSize, totalSize, userDataSizeBytes,mBatteryLevel,maxCupFred,networkType));

            }
            mAdapter.notifyDataSetChanged();
        }

        private void initView () {
            RecyclerView mRecycleView = (RecyclerView) findViewById(R.id.recyclerView);
            mRecycleView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecycleView.setLayoutManager(linearLayoutManager);
            mAdapter = new AppsListAdapter(this, appList);
            mRecycleView.setAdapter(mAdapter);
        }

    private BroadcastReceiver batteryReceiver=new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            mBatteryLevel = level;
        }
    };


        private boolean isSystemPackage (PackageInfo pkgInfo){
            return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                    : false;
        }

        public String removePackegeFromPermissionName (String permission_text){
            String[] splited = permission_text.split("\\.");
            String permissionName = splited[splited.length - 1];
            return permissionName;
        }

    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    public static String getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) {
            return "OTHER";
        }
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return "OTHER";
        }
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "WIFI";
                }
            }
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) return "OTHER";
        @SuppressLint("MissingPermission") int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            // 3G
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 19:
                return "4G";
            // 5G
//            case TelephonyManager.NETWORK_TYPE_NR:// need SdkVersion>=29
            case 20:// 当 SdkVersion<=28
                return "5G";
            default:
                return "OTHER";
        }
    }

    }