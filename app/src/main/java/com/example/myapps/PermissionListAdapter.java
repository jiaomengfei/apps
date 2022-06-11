package com.example.myapps;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PermissionListAdapter extends RecyclerView.Adapter<PermissionListAdapter.ViewHolder>{
    private List<PermissionListBean> listdata;
    Context context;

    // RecyclerView recyclerView;
    public PermissionListAdapter(Context context, List<PermissionListBean> listdata) {
        Log.d("PermissionListAda", "PermissionListAdapter: "+listdata.toString());
        this.context=context;
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.permissions_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PermissionListBean myListData = listdata.get(position);
        holder.permissionTxt.setText(listdata.get(position).getPermissionName()+" "+listdata.get(position).getPermissionVal());

        Log.d("gradle", "onBindViewHolder: "+listdata.get(position).getPermissionName());
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AppDetailActivity.class);
//
//                intent.putExtra("app_info", listdata.get(position).getPermisionList());
//                context.startActivity(intent);
//                Toast.makeText(view.getContext(),"click on item: "+myListData.getAppName(),Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView permissionTxt;
        public RelativeLayout permissionRL;
        public ViewHolder(View itemView) {
            super(itemView);
            this.permissionTxt = (TextView) itemView.findViewById(R.id.permissionTxt);
            permissionRL = (RelativeLayout)itemView.findViewById(R.id.permissionRL);
        }
    }
}
