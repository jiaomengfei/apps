package com.example.myapps;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.ViewHolder> {
    private List<AppListBean> listdata;
    Context context;
    private static DecimalFormat df = new DecimalFormat("0.00");

    public AppsListAdapter(Context context, List<AppListBean> listdata) {

        this.context = context;
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.app_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final AppListBean myListData = listdata.get(position);
        holder.appNameTxt.setText(listdata.get(position).getAppName());

        holder.appIndexTxt.setText(String.valueOf(position + 1));


        holder.appIconImageView.setImageDrawable(listdata.get(position).getAppIcon());
        String totalSize = "0.0KB";
        double totalSizeDouble = listdata.get(position).getTotalSize() / (1024 * 1024);
        totalSize = String.valueOf(df.format(totalSizeDouble)) + "MB";
        if (totalSizeDouble < 1) {
            totalSizeDouble = listdata.get(position).getTotalSize() / (1024);
            totalSize = String.valueOf(df.format(totalSizeDouble)) + "KB";
        }

        holder.appSizeTxt.setText(totalSize);
        holder.appItemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppDetailActivity.class);
                Log.d("imag", "onClick: " + listdata.get(position).getAppIcon());
                Bitmap bitmap = getBitmapFromDrawable(listdata.get(position).getAppIcon());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] appIcon = baos.toByteArray();

                Log.d("AppListBean1", "onCreate: " + listdata.get(position).getAppName()
                        + "==" + "--" + listdata.get(position).getMinSdkVersion()
                        + "**" + listdata.get(position).getTargetSdkVersion());

                intent.putExtra("ver_name", listdata.get(position).getVerName());
                intent.putExtra("min_sdk_ver", Integer.toString(listdata.get(position).getMinSdkVersion()));
                intent.putExtra("target_sdk_ver", Integer.toString(listdata.get(position).getTargetSdkVersion()));
                intent.putExtra("app_name", listdata.get(position).getAppName());
                intent.putExtra("app_icon", appIcon);
                intent.putExtra("app_info", listdata.get(position).getPermissionList());
                intent.putExtra("app_size", listdata.get(position).getAppSize() == "" ? "0.0" : listdata.get(position).getAppSize());
                intent.putExtra("data_size", listdata.get(position).getUserDataSize() == "" ? "0.0" : listdata.get(position).getUserDataSize());
                intent.putExtra("total_size_bytes", listdata.get(position).getTotalSize() == 0 ? "0" : String.valueOf(listdata.get(position).getTotalSize()));
                intent.putExtra("user_data_size_bytes", listdata.get(position).getUserDataSizeBytes() == 0 ? "0" : String.valueOf(listdata.get(position).getUserDataSizeBytes()));
                intent.putExtra("max_cpu_fred",listdata.get(position).getMaxCpuFreq());
                intent.putExtra("network_type",listdata.get(position).getNetworkType());
                Log.d("app_icon", "onClick: " + listdata.get(position).getAppIcon().toString());


                context.startActivity(intent);
                Toast.makeText(view.getContext(), "click on item: " + myListData.getAppName(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appIndexTxt;
        public ImageView appIconImageView;
        public TextView appNameTxt;
        public TextView appSizeTxt;
        public RelativeLayout appItemRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            this.appIconImageView = (ImageView) itemView.findViewById(R.id.appIconImageView);
            this.appNameTxt = (TextView) itemView.findViewById(R.id.appNameTxt);
            this.appSizeTxt = (TextView) itemView.findViewById(R.id.appSizeTxt);
            this.appIndexTxt = (TextView) itemView.findViewById(R.id.appIndexTxt);
            this.appItemRoot = (RelativeLayout) itemView.findViewById(R.id.app_item_root);
        }
    }

    @NonNull
    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }


}
