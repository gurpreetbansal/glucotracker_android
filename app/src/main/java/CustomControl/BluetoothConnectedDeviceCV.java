package CustomControl;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.R;

import java.util.ArrayList;
import java.util.Set;

import Adapter.BluetoothDeviceAdapter;
import BluetoothConnectivity.ACSUtility;
import Response.BluetoothDeviceEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BluetoothConnectedDeviceCV extends RelativeLayout {

    @BindView(R.id.availableDevicesRecyclerView)
    RecyclerView mAvialableDevicesRecyclerView;

    @BindView(R.id.pairedDevicesRecyclerView)
    RecyclerView mPairedDevicesRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.scanProgressbar)
    ProgressBar scanProgressBar;

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    ArrayList<BluetoothDeviceEntity> bluetoothPairedDeviceEntitiesList = new ArrayList<BluetoothDeviceEntity>();
    ArrayList<BluetoothDeviceEntity> bluetoothSearchingDeviceEntitiesList = new ArrayList<BluetoothDeviceEntity>();
    ArrayList<String> addressStringArray = new ArrayList<String>();

    BluetoothDeviceAdapter pairedDeviceAdapter;
    BluetoothDeviceAdapter unpairedDeviceAdapter;
    Activity ctx;
    int BLUETOOTH_CONNECTIVITY = 1000;

    private ACSUtility utility;
    private ACSUtility.blePort mNewtPort;
    private boolean utilAvaliable;

    private ACSUtility.IACSUtilityCallback userCallBack = new ACSUtility.IACSUtilityCallback() {

        @Override
        public void utilReadyForUse() {
            utilAvaliable = true;
            utility.enumAllPorts(10);
        }

        @Override
        public void didFoundPort(ACSUtility.blePort newPort) {
            mNewtPort = newPort;
            ctx.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    /*blePort port =  mNewtPort;
					ports.add(port._device.getName());
					devices.add(port);
					adapter.notifyDataSetChanged();*/
                    ctx.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addDevice(mNewtPort._device);
                        }
                    });
                }

            });
        }

        @Override
        public void didFinishedEnumPorts() {

        }

        @Override
        public void didOpenPort(ACSUtility.blePort port, Boolean bSuccess) {

        }

        @Override
        public void didClosePort(ACSUtility.blePort port) {

        }

        @Override
        public void didPackageSended(boolean succeed) {

        }

        @Override
        public void didPackageReceived(ACSUtility.blePort port, byte[] packageToSend) {

        }

        @Override
        public void heartbeatDebug() {

        }
    };

    private void addDevice(BluetoothDevice device) {
        boolean deviceFound = false;
        for (BluetoothDeviceEntity listDev : bluetoothSearchingDeviceEntitiesList) {
            if (listDev.getAddress().equals(device.getAddress())) {
                deviceFound = true;
                break;
            }
        }
        if (!deviceFound) {
            BluetoothDeviceEntity entity = new BluetoothDeviceEntity(device.getName(), device.getAddress(), false, device);
            bluetoothSearchingDeviceEntitiesList.add(entity);
            unpairedDeviceAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }

    public BluetoothConnectedDeviceCV(Context context) {
        super(context);
    }

    public BluetoothConnectedDeviceCV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BluetoothConnectedDeviceCV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void intializeViews(Activity context) {
        this.ctx = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.bluetooth_paired_device_view, this);
        ButterKnife.bind(this, v);

        utility = new ACSUtility(this.ctx, userCallBack);
        utilAvaliable = false;

        LinearLayoutManager mLinearLayout = new LinearLayoutManager(getContext());
        mPairedDevicesRecyclerView.setLayoutManager(mLinearLayout);
        mPairedDevicesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAvialableDevicesRecyclerView.setLayoutManager(layoutManager);

        pairedDeviceAdapter = new BluetoothDeviceAdapter(ctx, bluetoothPairedDeviceEntitiesList);
        mPairedDevicesRecyclerView.setAdapter(pairedDeviceAdapter);
        mPairedDevicesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        unpairedDeviceAdapter = new BluetoothDeviceAdapter(ctx, bluetoothSearchingDeviceEntitiesList);
        mAvialableDevicesRecyclerView.setAdapter(unpairedDeviceAdapter);
        mAvialableDevicesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
    }

    public void setData(BluetoothDeviceEntity device) {
        this.bluetoothSearchingDeviceEntitiesList.add(device);
        unpairedDeviceAdapter.notifyDataSetChanged();
    }

    public void unregisterReciever() {

    }

    public String on() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                list();
            } else {
                showAlertDialog();
            }
        }
        return null;
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(getResources().getString(R.string.pleaseOnTheBluetooth));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.yesText),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        ctx.startActivityForResult(turnOn, BLUETOOTH_CONNECTIVITY);
                    }
                });

        builder1.setNegativeButton(
                getResources().getString(R.string.noText),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void off(View v) {
        bluetoothAdapter.disable();
        Toast.makeText(getContext(), getResources().getString(R.string.turnedOff), Toast.LENGTH_LONG).show();
    }

    public void visible(View v) {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        ctx.startActivityForResult(getVisible, 0);
        progressBar.setVisibility(View.GONE);
    }

    public void list() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (!addressStringArray.contains(device.getAddress())) {
                BluetoothDeviceEntity entity = new BluetoothDeviceEntity(device.getName(), device.getAddress(), true, device);
                bluetoothPairedDeviceEntitiesList.add(entity);
                addressStringArray.add(device.getAddress());
            }
        }
        pairedDeviceAdapter.notifyDataSetChanged();
        try {
            bluetoothAdapter.startDiscovery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRecyclerView() {
        pairedDeviceAdapter.notifyDataSetChanged();
        unpairedDeviceAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.scanForDevicesButton)
    public void setScanForDevicesButton() {
        scanProgressBar.setVisibility(View.VISIBLE);
        if (bluetoothAdapter != null) {
            bluetoothSearchingDeviceEntitiesList.clear();
            unpairedDeviceAdapter.notifyDataSetChanged();
            bluetoothAdapter.startDiscovery();
            scanProgressBar.setVisibility(View.GONE);
        }

    }


    public void destroy() {
        if (utilAvaliable) {
            //utility.setUserCallback(null);
            utility.stopEnum();
            utility.closeACSUtility();
        }
    }
}
