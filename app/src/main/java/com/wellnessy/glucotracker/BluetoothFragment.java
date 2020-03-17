package com.wellnessy.glucotracker;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import CustomControl.BluetoothConnectedDeviceCV;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothFragment extends Fragment {
    @BindView(R.id.bluetoothConnectivityView)
    BluetoothConnectedDeviceCV bluetoothConnectivityView;
    int BLUETOOTH_CONNECTIVITY = 1000;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bluetooth_fragment, container, false);
        ButterKnife.bind(this, rootView);
        requestPermission();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_CONNECTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothConnectivityView.intializeViews(getActivity());
                bluetoothConnectivityView.list();
            }
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    200);
        } else {
            bluetoothConnectivityView.intializeViews(getActivity());
            bluetoothConnectivityView.on();
        }
    }

    public void blueToothSearchDevice(){
        bluetoothConnectivityView.on();
    }

    public void backFragment() {
        bluetoothConnectivityView.unregisterReciever();
        Fragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.bluetoothFragmentLayout, profileFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("BluetoothFragment");
        transaction.commit();
    }

    public void updateBluetoothFragment() {
        bluetoothConnectivityView.updateRecyclerView();
    }
}
