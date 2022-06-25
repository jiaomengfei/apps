package com.example.myapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppDetailActivity extends AppCompatActivity {

    public double ex = 0.0;
    public int n = 0;
    public double b = 0.0;
    public double c = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        initView();
    }

    private void initView() {
        TextView verDetailTxt = (TextView) findViewById(R.id.verDetailTxt);
        TextView appNameDetailTxt = (TextView) findViewById(R.id.appNameDetailTxt);
        TextView minSdkDetailTxt = (TextView) findViewById(R.id.minSdkDetailTxt);
        TextView targetSdkDetailTxt = (TextView) findViewById(R.id.targetSdkDetailTxt);
        TextView appSizeDetailTxt = (TextView) findViewById(R.id.appSizeDetailTxt);
        TextView dataSizeDetailTxt = (TextView) findViewById(R.id.dataSizeDetailTxt);
        TextView batteryLevelTxt = (TextView) findViewById(R.id.battery_level);
        TextView maxCpuFred = (TextView) findViewById(R.id.max_cpu_fred);
        TextView netWorkType = findViewById(R.id.network_type);
        Intent intent = getIntent();
        String app_info = intent.getStringExtra("app_info");
        String ver_name = intent.getStringExtra("ver_name");
        String min_sdk_ver = intent.getStringExtra("min_sdk_ver");
        String target_sdk_ver = intent.getStringExtra("target_sdk_ver");
        String app_name = intent.getStringExtra("app_name");
        String app_size = intent.getStringExtra("app_size");
        String data_size = intent.getStringExtra("data_size");
        String max_cup_fred = intent.getStringExtra("max_cpu_fred");
        String network_type = intent.getStringExtra("network_type");

        double totalSizeBytes = Double.parseDouble(intent.getStringExtra("total_size_bytes"));
        double userDataSizeBytes = Double.parseDouble(intent.getStringExtra("user_data_size_bytes"));

        Bundle extras = getIntent().getExtras();
        byte[] bi = extras.getByteArray("app_icon");

        b = userDataSizeBytes / 1024;
        c = totalSizeBytes / 1024;
        Log.d("total_aks", "onCreate: ==" + intent.getStringExtra("user_data_size_bytes") + "==" + b + "==" + intent.getStringExtra("total_size_bytes") + "==" + c);

        Bitmap bmp = BitmapFactory.decodeByteArray(bi, 0, bi.length);
        ImageView image = (ImageView) findViewById(R.id.appIconDetailImageView);
        image.setImageBitmap(bmp);

        verDetailTxt.setText(ver_name);
        appNameDetailTxt.setText(app_name);
        minSdkDetailTxt.setText(min_sdk_ver);
        targetSdkDetailTxt.setText(target_sdk_ver);
        appSizeDetailTxt.setText(app_size);
        dataSizeDetailTxt.setText(data_size);
        batteryLevelTxt.setText(AppsActivity.mBatteryLevel+"%");
        maxCpuFred.setText(max_cup_fred+" KHZ");
        netWorkType.setText(network_type);
        ConstantsConfig constantsConfig = new ConstantsConfig();
        List<PermissionListBean> permissionsData = constantsConfig.permissionsData();//permissionsData();
        List<PermissionListBean> appPermissionList = new ArrayList<PermissionListBean>();
        try {
            JSONObject jsonObject = new JSONObject(app_info);
            Log.d("testo", "onCreate: " + jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("user_permissions");
            Log.d("testo1", "onCreate: " + jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                n++;
                String tmpPer = jsonArray.getString(i).toString();
                String[] splited = tmpPer.replace("_", " ").split("\\.");
                String permissionName = upperCaseFirst(splited[splited.length - 1].toLowerCase());
                Double permissionVal = 0.0;
                if (data_size != "") {
                    for (int j = 0; j < permissionsData.size(); j++) {
                        Log.d("Permission1", "onCreate: ##" + splited[splited.length - 1].replace(" ", "_") + "##" + permissionsData.get(j).getPermissionName() + "**");
                        if (permissionsData.get(j).getPermissionName().equals(splited[splited.length - 1].replace(" ", "_"))) {
                            permissionVal = permissionsData.get(j).getPermissionVal();
                            ex += permissionVal;
                            break;
                        }
                    }
                }
                appPermissionList.add(new PermissionListBean(permissionName, permissionVal));
            }

        } catch (JSONException e) {
            Log.d("testo", "onCreate: " + e.toString());
            e.printStackTrace();
        }
        RecyclerView appDetailView = (RecyclerView) findViewById(R.id.appDetailView);
        PermissionListAdapter adapter = new PermissionListAdapter(AppDetailActivity.this, appPermissionList);
        appDetailView.setHasFixedSize(true);
        appDetailView.setLayoutManager(new LinearLayoutManager(this));
        appDetailView.setAdapter(adapter);
        TextView emptyDetailView = (TextView) findViewById(R.id.emptyDetailView);
        if (appPermissionList.size() <= 0) {
            appDetailView.setVisibility(View.GONE);
            emptyDetailView.setVisibility(View.VISIBLE);
        } else {
            appDetailView.setVisibility(View.VISIBLE);
            emptyDetailView.setVisibility(View.GONE);
        }

    }

    public static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }

}