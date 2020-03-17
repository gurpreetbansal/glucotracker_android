package Adapter;

import android.app.Activity;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.BluetoothActivity;
import com.wellnessy.glucotracker.NavigationActivity;
import com.wellnessy.glucotracker.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import CustomControl.TextViewGothamBook;
import Infrastructure.AppCommon;
import Response.BluetoothDeviceEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.DataHolder> {

    Activity activityCtx;
    List<BluetoothDeviceEntity> deviceEntityList;

    public BluetoothDeviceAdapter(Activity activityCtx, ArrayList<BluetoothDeviceEntity> deviceEntityList) {
        this.activityCtx = activityCtx;
        this.deviceEntityList = deviceEntityList;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_row_file, parent, false);
        return new DataHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        holder.deviceName.setText(activityCtx.getResources().getString(R.string.nameText) + "" + deviceEntityList.get(position).getName());
        holder.deviceAddress.setText(activityCtx.getResources().getString(R.string.addressText) + "" + deviceEntityList.get(position).getAddress());
        if (!AppCommon.getInstance(activityCtx).getDeviceAddress().equals("") &&
                deviceEntityList.size() > 0 &&
                !deviceEntityList.get(position).getAddress().isEmpty() &&
                AppCommon.getInstance(activityCtx).getDeviceAddress().equals(deviceEntityList.get(position).getAddress())) {
            holder.parentLayout.setBackgroundColor(activityCtx.getResources().getColor(R.color.blue));
        } else {
            holder.parentLayout.setBackgroundColor(activityCtx.getResources().getColor(R.color.white));
        }
        holder.parentLayout.setTag(Integer.toString(position));
    }


    @Override
    public int getItemCount() {
        return deviceEntityList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.deviceName)
        TextViewGothamBook deviceName;

        @BindView(R.id.deviceAddress)
        TextViewGothamBook deviceAddress;

        @BindView(R.id.parentLayout)
        LinearLayout parentLayout;

        public DataHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.parentLayout)
        public void parentLayoutClick(View v) {
            int index = Integer.parseInt(v.getTag().toString());
            BluetoothDeviceEntity entity = deviceEntityList.get(index);
            Gson gson = new Gson();
            String entityStr = gson.toJson(entity);
            AppCommon.getInstance(activityCtx).setDeviceObject(entityStr);
            AppCommon.getInstance(activityCtx).setDeviceAddress(entity.getAddress());

            if (activityCtx instanceof BluetoothActivity) {
                ((BluetoothActivity) activityCtx).stopScanning();
                Intent intent = new Intent();
                ((BluetoothActivity) activityCtx).setResult(Activity.RESULT_OK, intent);
                ((BluetoothActivity) activityCtx).finish();
            } else if (activityCtx instanceof NavigationActivity) {
                ((NavigationActivity) activityCtx).updateBluetoothFragment();
            }
        }
    }
}

