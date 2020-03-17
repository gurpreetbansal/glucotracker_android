package com.wellnessy.glucotracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import CustomControl.BluetoothConnectedDeviceCV;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BluetoothActivity extends Activity {
    @BindView(R.id.bluetoothConnectivityView)
    BluetoothConnectedDeviceCV bluetoothConnectivityView;
    int BLUETOOTH_CONNECTIVITY = 1000;

    @BindView(R.id.bluetoothHeadingLayout)
    RelativeLayout bluetoothHeadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        requestPermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_CONNECTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothConnectivityView.list();
            }
        } else {
            bluetoothConnectivityView.unregisterReciever();
        }
    }

    @OnClick(R.id.backIcon)
    public void backIconClick() {
        bluetoothConnectivityView.unregisterReciever();
        this.finish();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_PRIVILEGED)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.BLUETOOTH_PRIVILEGED, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    200);
        } else {
            bluetoothConnectivityView.intializeViews(this);
            bluetoothConnectivityView.on();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    bluetoothConnectivityView.intializeViews(this);
                    bluetoothConnectivityView.on();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void stopScanning() {
        bluetoothConnectivityView.destroy();
    }
}

