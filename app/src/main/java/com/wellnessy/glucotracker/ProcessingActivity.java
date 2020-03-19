package com.wellnessy.glucotracker;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import BluetoothConnectivity.ConncetedDevice;
import BluetoothConnectivity.ConncetedNewDevice;
import CustomControl.GIFView;
import CustomControl.TextViewGothamMedium;
import CustomControl.TextViewIconStyle;
import Database.DbHelper;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProcessingActivity extends ConncetedNewDevice {

    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;
    @BindView(R.id.processingImageView)
    ImageView processingImageView;

    @BindView(R.id.processingText)
    TextView processingText;

    @BindView(R.id.instructionFirst)
    TextView instructionFirstTV;

    @BindView(R.id.instructionLine)
    TextView instructionLineTV;

    @BindView(R.id.instructionSecond)
    TextView instructionSecondTV;

    @BindView(R.id.backIcon)
    TextViewIconStyle backIcon;

    @BindView(R.id.gifView)
    GIFView gifView;

    @BindView(R.id.instructionLine1)
    TextView instructionLine1;

    @BindView(R.id.instructionthird)
    TextView instructionThirdTV;

    @BindView(R.id.timer)
    TextView timer;

    @BindView(R.id.insertFingerTimer)
    TextView insertFingerTimer;

    @BindView(R.id.gifLayout)
    RelativeLayout gifLayout;

    @BindView(R.id.detectionTextView)
    TextViewGothamMedium detectionTextView;

    boolean isRunning = false;
    int BLUETOOTH_CONNECTIVITY = 1000;
    int tipCount = 1;
    CountDownTimer timerForTest;
    CountDownTimer executionTimeCounter;
    CountDownTimer insertFingerTimerCounter;


    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.processing_layout);
        ButterKnife.bind(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        on();
    }

    @OnClick(R.id.backIcon)
    public void backClick() {
        disconnectDevice();
        makeUtilNull();
        // disconnect(AppCommon.getInstance(this).getDeviceAddress());
        this.finish();
    }


    public String on() {
        if (bluetoothAdapter.isEnabled()) {
            connectDevice(this);
        } else {
            showAlertDialog();
        }
        return null;
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(getResources().getString(R.string.pleaseOnTheBluetooth));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getResources().getString(R.string.yesText),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(turnOn, BLUETOOTH_CONNECTIVITY);
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

    public void connect(String address) {
        ConncetedDevice device = ConncetedDevice.GetDevice(address);
        if (device != null) {
            try {
                device.Disconnect();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        ConncetedDevice dev = new ConncetedDevice(this, address);
        try {
            if (dev.Connect()) {
                ConncetedDevice.PutDevice(address, dev);
            }
        } catch (Exception e) {

        }
    }

    void disconnect(String address) {
        ConncetedDevice device = ConncetedDevice.GetDevice(address);
        if (device != null) {
            try {
                device.sendPowerOffCommand();
                device.Disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changeScreensAccordingToCase(String caseStr) {
        switch (caseStr) {
            case "Not_Connected":
                processingImageView.setImageResource(R.drawable.device_connection_failure);
                processingText.setText(getResources().getString(R.string.Device_Connection_Failure));
                instructionFirstTV.setVisibility(View.INVISIBLE);
                instructionLineTV.setVisibility(View.INVISIBLE);
                instructionThirdTV.setVisibility(View.GONE);
                instructionLine1.setVisibility(View.GONE);
                instructionSecondTV.setText(getResources().getString(R.string.Device_power_on));
                disconnectDevice();
                makeUtilNull();
                break;
            case "Env_Start":
                processingImageView.setImageResource(R.drawable.environment_testing);
                processingText.setText(getResources().getString(R.string.commplainToEnvironmentalCondition));
                break;
            case "Battery_Level_Low":
                processingImageView.setImageResource(R.drawable.insert_finger);
                processingText.setText(getResources().getString(R.string.pleaseInsertFinger) + "\n" + getResources().getString(R.string.deviceBatteryLow));
                //insertFingerCountTime();
                // processingText.setText(getResources().getString(R.string.deviceBatteryLow));
                break;
            case "Env_Temp_Normal":
                processingImageView.setImageResource(R.drawable.insert_finger);
                processingText.setText(getResources().getString(R.string.pleaseInsertFinger));
                insertFingerCountTime();
                break;
            case "Env_Temp_Low":
                processingImageView.setImageResource(R.drawable.insert_finger);
                processingText.setText(getResources().getString(R.string.temperatureLow));
                insertFingerCountTime();
                break;
            case "Env_Temp_High":
                processingImageView.setImageResource(R.drawable.insert_finger);
                processingText.setText(getResources().getString(R.string.temperatureHigh));
                insertFingerCountTime();
                break;
            case "Env_Temp_Dry":
                processingImageView.setImageResource(R.drawable.insert_finger);
                processingText.setText(getResources().getString(R.string.environmentDry));
                insertFingerCountTime();
                break;
            case "Env_Temp_Humidity":
                processingImageView.setImageResource(R.drawable.insert_finger);
                processingText.setText(getResources().getString(R.string.humidityHigh));
                insertFingerCountTime();
                break;
            case "Finger_Temp_Normal":
                fetchDataFromDevice();
                break;
            case "Finger_Temp_Low":
                processingImageView.setImageResource(R.drawable.something_wrong);
                processingText.setText(getResources().getString(R.string.fingerTemperatureLow));
                insertFingerTimer.setVisibility(View.GONE);
                disconnectDevice();
                makeUtilNull();
                break;
            case "Finger_Temp_High":
                processingImageView.setImageResource(R.drawable.something_wrong);
                processingText.setText(getResources().getString(R.string.fingerTemperatureHigh));
                insertFingerTimer.setVisibility(View.GONE);
                disconnectDevice();
                makeUtilNull();
                break;
            case "ERBW":
                processingImageView.setImageResource(R.drawable.finger_not_inserted);
                processingText.setText(getResources().getString(R.string.ERBW));
                disconnectDevice();
                makeUtilNull();
                break;
            case "ERAD":
                processingImageView.setImageResource(R.drawable.finger_not_inserted);
                processingText.setText(getResources().getString(R.string.ERAD));
                disconnectDevice();
                makeUtilNull();
                break;
            case "ERTT":
                processingImageView.setImageResource(R.drawable.finger_not_inserted);
                processingText.setText(getResources().getString(R.string.ERTT));
                insertFingerTimer.setVisibility(View.GONE);
                disconnectDevice();
                makeUtilNull();
                break;
            case "ERRO":
                processingImageView.setImageResource(R.drawable.finger_not_inserted);
                processingText.setText(getResources().getString(R.string.ERRO));
                disconnectDevice();
                makeUtilNull();
                break;
            case "CCFRCS":
                processingImageView.setImageResource(R.drawable.finger_not_inserted);
                processingText.setText(getResources().getString(R.string.CCFRCS));
                disconnectDevice();
                makeUtilNull();
                break;
            case "SomeThing_Wrong":
                processingImageView.setImageResource(R.drawable.something_wrong);
                processingText.setText(getResources().getString(R.string.somethingWentWrong));
                disconnectDevice();
                makeUtilNull();
                break;
        }
    }

    public void disconnectDevice() {
        if (timerForTest != null) {
            timerForTest.cancel();
            timerForTest = null;
        }

        if (executionTimeCounter != null) {
            executionTimeCounter.cancel();
            executionTimeCounter = null;
        }
        timer.setVisibility(View.GONE);
        insertFingerTimer.setVisibility(View.GONE);
        gifView.setVisibility(View.GONE);
        gifLayout.setVisibility(View.GONE);
        // makeUtilNull();
    }

    public void insertFingerCountTime() {
        insertFingerTimerCounter = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "%02d ",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                insertFingerTimer.setText(text);
            }

            public void onFinish() {
                insertFingerTimer.setVisibility(View.GONE);
                changeScreensAccordingToCase("CCFRCS");
            }
        };
        insertFingerTimerCounter.start();
    }

    public void cancelFingerTimer() {
        if (insertFingerTimerCounter != null) {
            insertFingerTimerCounter.cancel();
            insertFingerTimerCounter = null;
        }
    }

    public void countTime() {
        executionTimeCounter = new CountDownTimer(60000, 900) {
            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
                detectionTextView.setText(getResources().getString(R.string.processing));
                insertFingerTimer.setVisibility(View.GONE);
            }

            public void onFinish() {
            }
        };
        executionTimeCounter.start();
    }

    public void fetchDataFromDevice() {
        cancelFingerTimer();
        countTime();
        timerForTest = new CountDownTimer(65000, 10000) {
            public void onTick(long millisUntilFinished) {
                changeTips(tipCount);
                tipCount++;
            }

            public void onFinish() {
                //saveDataAndMoveToNextScreen();
            }
        };
        timerForTest.start();
    }

    public void saveDataAndMoveToNextScreen() {
        if (!glucose.equals("") && !oxygenSaturation.equals("") && !hemoglobin.equals("") && !speed.equals("") && !pulse.equals("")) {
            DbHelper.getInstance(ProcessingActivity.this).insertTestDetails(AppCommon.getInstance(ProcessingActivity.this).getUserId(),
                    AppCommon.getInstance(ProcessingActivity.this).getCurrentDate(),
                    speed, glucose, hemoglobin, pulse,
                    oxygenSaturation, envTemp,
                    envHumidity, surfaceTemp, surfaceHumidity,
                    batteryLevel, String.valueOf(AppCommon.getInstance(ProcessingActivity.this).getMeal()), Integer.toString(AppCommon.getInstance(ProcessingActivity.this).getMedicineState()), AppCommon.getInstance(ProcessingActivity.this).getCurrentTime());
            int testCount = AppCommon.getInstance(ProcessingActivity.this).getTestCount();
            testCount = testCount + 1;
            disconnectDevice();
            AppCommon.getInstance(ProcessingActivity.this).setTestCount(testCount);
            Intent resultIntent = new Intent(ProcessingActivity.this, ResultActivity.class);
            startActivity(resultIntent);
            ProcessingActivity.this.finish();
            AppCommon.getInstance(ProcessingActivity.this).setLastTestDateTime(Calendar.getInstance().getTimeInMillis());
        }
    }

    public void changeTips(int tipCount) {
        switch (tipCount) {
            case 1:
                processingImageView.setImageResource(R.drawable.processing_tip6);
                processingText.setText(getResources().getString(R.string.checkHealthRegularly));
                break;
            case 2:
                processingImageView.setImageResource(R.drawable.processing_tip5);
                processingText.setText(getResources().getString(R.string.partcipateInPhysicalActivity));
                break;
            case 3:
                processingImageView.setImageResource(R.drawable.processing_tip4);
                processingText.setText(getResources().getString(R.string.manageStress));
                break;
            case 4:
                processingImageView.setImageResource(R.drawable.processing_tip3);
                processingText.setText(getResources().getString(R.string.eatWell));
                break;
            case 5:
                processingImageView.setImageResource(R.drawable.processing_tip2);
                processingText.setText(getResources().getString(R.string.getSleep));
                break;
            case 6:
                processingImageView.setImageResource(R.drawable.processing_tip1);
                processingText.setText(getResources().getString(R.string.stayHydrated));
                break;
        }
        instructionSecondTV.setVisibility(View.GONE);
        instructionLineTV.setVisibility(View.INVISIBLE);
        instructionLine1.setVisibility(View.GONE);
        instructionThirdTV.setVisibility(View.GONE);
        instructionFirstTV.setText(getResources().getString(R.string.stayCalm));
        instructionFirstTV.setTextColor(getResources().getColor(R.color.blue));
        gifView.setVisibility(View.VISIBLE);
        gifLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_CONNECTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                connect(AppCommon.getInstance(this).getDeviceAddress());
            }
        }
    }

    public void devicePowerOff() {
        ConncetedDevice device = ConncetedDevice.GetDevice(AppCommon.getInstance(this).getDeviceAddress());
        device.sendPowerOffCommand();
    }
}
