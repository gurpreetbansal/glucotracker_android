package com.wellnessy.glucotracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.HistorySelectedDateResponse;
import butterknife.BindView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class  ResultActivity extends Activity {
    Bitmap myBitmap;

    @BindView(R.id.bloodGlucoseText)
    TextViewGothamBook bloodGlucoseText;

    @BindView(R.id.pulseTextView)
    TextViewGothamBook pulseTextView;

    @BindView(R.id.bloodFlowTextView)
    TextViewGothamBook bloodFlowTextView;

    @BindView(R.id.hemoglobinText)
    TextViewGothamBook hemoglobinText;

    @BindView(R.id.oxygenSaturationTextView)
    TextViewGothamBook oxygenSaturationTextView;

    @BindView(R.id.batteryPowerTextView)
    TextViewGothamBook batteryPowerTextView;

    @BindView(R.id.surfaceTempTextView)
    TextViewGothamBook surfaceTempTextView;

    @BindView(R.id.envTempTextView)
    TextViewGothamBook envTempTextView;

    @BindView(R.id.surfaceHumidityTextView)
    TextViewGothamBook surfaceHumidityTextView;

    @BindView(R.id.envHumidityTextView)
    TextViewGothamBook envHumidityTextView;

    @BindView(R.id.heamoglobinUnit)
    TextViewGothamBook mHeamoglobinUnitText;

    @BindView(R.id.snapshotLayout)
    LinearLayout snapshotLayout;

    String glucoseValue = "";
    String pulseValue = "";
    String bloodFlowVelocity = "";
    String hemoglobinValue = "";
    String oxygenSaturation = "";
    String batteryPower = "";
    String envTemp = "";
    String surfaceTemp = "";
    String emvHumidity = "";
    String surfaceHumidity = "";
    String dietValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        ButterKnife.bind(this);
        if (this.getIntent().getSerializableExtra(getResources().getString(R.string.responseText)) != null) {
            HistorySelectedDateResponse mHistorySelectedDateResponse = (HistorySelectedDateResponse) getIntent().getSerializableExtra(getResources().getString(R.string.responseText));
            glucoseValue = mHistorySelectedDateResponse.getmGlucoseValue();
            pulseValue = mHistorySelectedDateResponse.getmPulseValue();
            bloodFlowVelocity = mHistorySelectedDateResponse.getmBloodFlowSpeed();
            hemoglobinValue = mHistorySelectedDateResponse.getmHeamoGlobinValue();
            oxygenSaturation = mHistorySelectedDateResponse.getmOxygenSaturationValue();
            batteryPower = mHistorySelectedDateResponse.getmBatteryLevel();
            envTemp = mHistorySelectedDateResponse.getmEnvironmentTemp();
            surfaceTemp = mHistorySelectedDateResponse.getmSurfaceTemp();
            emvHumidity = mHistorySelectedDateResponse.getmEnvironmentHumidity();
            surfaceHumidity = mHistorySelectedDateResponse.getmSurfaceHumidity();
            dietValue = mHistorySelectedDateResponse.getmDietValue();

        } else {
            Cursor cursor = DbHelper.getInstance(this).getTestRecord();
            if (cursor.moveToFirst()) {
                do {
                    glucoseValue = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_GLUCOSEVALUE));
                    pulseValue = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_PULSE));
                    bloodFlowVelocity = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_BLOODFLOWSPEED));
                    hemoglobinValue = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_HEAMOGLOBIN));
                    oxygenSaturation = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_OXYGENSATURATION));
                    batteryPower = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_BATTERY_LEVEL));
                    envTemp = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_ENV_TEMP));
                    surfaceTemp = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_SURFACE_TEMP));
                    emvHumidity = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_ENV_HUMIDITY));
                    surfaceHumidity = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_SURFACE_HUMIDIT));
                    dietValue = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_DIET));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        Cursor rs = DbHelper.getInstance(this).getMeasurementSettingRecord(AppCommon.getInstance(this).getUserId());
        rs.moveToFirst();
        String spo2Less = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_SPO2_LESS));
        String spo2More = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_SPO2_MORE));
        String hgbLess = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_HGB_LESS));
        String hgbMore = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_HGB_MORE));
        String bfvLess = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_BFV_LESS));
        String bfvMore = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_BFV_MORE));
        String pulseLess = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_PULSE_LESS));
        String pulseMore = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_PULSE_MORE));
        String glucoseLessValue = "";
        String glucoseMoreValue = "";

        if (dietValue.equals("0")) {
            glucoseLessValue = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLU_MORNING_FASTING_LESS));
            glucoseMoreValue = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLU_MORNING_FASTING_MORE));
        } else {
            glucoseLessValue = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLU_AFTER_MEAL_LESS));
            glucoseMoreValue = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLU_AFTER_MEAL_MORE));
        }

        if (!rs.isClosed()) {
            rs.close();
        }
        if (Double.parseDouble(oxygenSaturation) > Double.parseDouble(spo2More)) {
            oxygenSaturationTextView.setTextColor(getResources().getColor(R.color.red));
        } else if (Double.parseDouble(oxygenSaturation) < Double.parseDouble(spo2Less)) {
            oxygenSaturationTextView.setTextColor(getResources().getColor(R.color.red));
        }
        if (Double.parseDouble(pulseValue) > Double.parseDouble(pulseMore)) {
            pulseTextView.setTextColor(getResources().getColor(R.color.red));
        } else if (Double.parseDouble(pulseValue) < Double.parseDouble(pulseLess)) {
            pulseTextView.setTextColor(getResources().getColor(R.color.red));
        }
        if (Double.parseDouble(bloodFlowVelocity) > Double.parseDouble(bfvMore)) {
            bloodFlowTextView.setTextColor(getResources().getColor(R.color.red));
        } else if (Double.parseDouble(bloodFlowVelocity) < Double.parseDouble(bfvLess)) {
            bloodFlowTextView.setTextColor(getResources().getColor(R.color.red));
        }
        if (AppCommon.getInstance(this).getGlucoseUnit().equals(getResources().getString(R.string.mMolText))) {
            double mGLucoseDoubleValue = Double.parseDouble(glucoseValue);
            mGLucoseDoubleValue = (mGLucoseDoubleValue / 10);
            double mGLucoseDoubleMoreValue = Double.parseDouble(glucoseMoreValue);
            mGLucoseDoubleMoreValue = AppCommon.getInstance(this).getmMolFromMgDl(mGLucoseDoubleMoreValue);
            double mGLucoseDoubleLessValue = Double.parseDouble(glucoseLessValue);
            mGLucoseDoubleLessValue = AppCommon.getInstance(this).getmMolFromMgDl(mGLucoseDoubleLessValue);
            if (mGLucoseDoubleValue > mGLucoseDoubleMoreValue) {
                bloodGlucoseText.setTextColor(getResources().getColor(R.color.red));
            } else if (mGLucoseDoubleValue < mGLucoseDoubleLessValue) {
                bloodGlucoseText.setTextColor(getResources().getColor(R.color.red));
            }
            bloodGlucoseText.setText(String.format("%.1f", mGLucoseDoubleValue));
        } else {
            float mGLucoseDoubleValue = Float.parseFloat(glucoseValue);
            mGLucoseDoubleValue = (mGLucoseDoubleValue / 10.0f);
            float mMgDlValues = AppCommon.getInstance(this).getMgDlFrommMol(mGLucoseDoubleValue);
            double mGLucoseDoubleMoreValue = Double.parseDouble(glucoseMoreValue);
            double mGLucoseDoubleLessValue = Double.parseDouble(glucoseLessValue);
            if (mMgDlValues > mGLucoseDoubleMoreValue) {
                bloodGlucoseText.setTextColor(getResources().getColor(R.color.red));
            } else if (mMgDlValues < mGLucoseDoubleLessValue) {
                bloodGlucoseText.setTextColor(getResources().getColor(R.color.red));
            }
            bloodGlucoseText.setText(String.format("%.1f", mMgDlValues));
           // mGlucoseUnitText.setText(getResources().getString(R.string.mMgdlText));
        }

        if (AppCommon.getInstance(this).getHemoglobinUnit().equals(getResources().getString(R.string.gPerLitreText))) {

            Double mHeamoGlobinDoubleValue = Double.parseDouble(hemoglobinValue);
            mHeamoGlobinDoubleValue = (mHeamoGlobinDoubleValue / 10);
            Double mHeamoGlobinDoubleMoreValue = Double.parseDouble(hgbMore);
            Double mHeamoGlobinDoubleLessValue = Double.parseDouble(hgbLess);
            if (mHeamoGlobinDoubleValue > mHeamoGlobinDoubleMoreValue) {
                hemoglobinText.setTextColor(getResources().getColor(R.color.red));
            } else if (mHeamoGlobinDoubleValue < mHeamoGlobinDoubleLessValue) {
                hemoglobinText.setTextColor(getResources().getColor(R.color.red));
            }
            hemoglobinText.setText(String.format("%.1f", mHeamoGlobinDoubleValue));
        } else {
            Double mHeamoGlobinDoubleValue = Double.parseDouble(hemoglobinValue);
            mHeamoGlobinDoubleValue = (mHeamoGlobinDoubleValue / 100);
            Double mHeamoGlobinDoubleMoreValue = Double.parseDouble(hgbMore);
            mHeamoGlobinDoubleMoreValue = (mHeamoGlobinDoubleMoreValue / 10);
            Double mHeamoGlobinDoubleLessValue = Double.parseDouble(hgbLess);
            mHeamoGlobinDoubleLessValue = (mHeamoGlobinDoubleLessValue / 10);
            if (mHeamoGlobinDoubleValue > mHeamoGlobinDoubleMoreValue) {
                hemoglobinText.setTextColor(getResources().getColor(R.color.red));
            } else if (mHeamoGlobinDoubleValue < mHeamoGlobinDoubleLessValue) {
                hemoglobinText.setTextColor(getResources().getColor(R.color.red));
            }
            hemoglobinText.setText(String.format("%.1f", mHeamoGlobinDoubleValue));
            mHeamoglobinUnitText.setText(getResources().getString(R.string.gPerdl));
        }
        pulseTextView.setText(pulseValue);
        bloodFlowTextView.setText(bloodFlowVelocity);
        oxygenSaturationTextView.setText(oxygenSaturation);
        batteryPowerTextView.setText(batteryPower);

        float environmentTemp = Float.parseFloat(envTemp);
        float environmentTemp1 = (environmentTemp)/ 10.0f;

        float SurfaceTemp = Float.parseFloat(surfaceTemp);
        float SurfaceTemp1 = SurfaceTemp / 10.0f;

        float environmentHumidity = Float.parseFloat(emvHumidity);
       float environmentHumidity1 = environmentHumidity / 10.0f;

        float surfHumidity = Float.parseFloat(surfaceHumidity);
        float surfHumidity1 = surfHumidity / 10.0f;

        envTempTextView.setText(getResources().getString(R.string.etText) + "" + environmentTemp1);
        surfaceTempTextView.setText(getResources().getString(R.string.stText) + "" + SurfaceTemp1);
        envHumidityTextView.setText(getResources().getString(R.string.ehText) + "" + environmentHumidity1);
        surfaceHumidityTextView.setText(getResources().getString(R.string.shText) + "" + surfHumidity1);
    }

    @OnClick(R.id.guideLayout)
    public void setGuideLinearLayout() {
        Intent intent = new Intent(ResultActivity.this, GuideActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.backButton)
    public void backButtonClick() {
        this.finish();
    }

    @OnClick(R.id.historyLayout)
    public void historyClick() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void saveBitmap(Bitmap bitmap) {
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Pictures/result.png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            sendMail(filePath);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public void sendMail(String path) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.healthAlert));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.esserHealthCareTechnology));
        emailIntent.setType("image/png");
        Uri myUri = Uri.parse("file://" + path);
        File file=new File(path);
        Uri uri = FileProvider.getUriForFile(ResultActivity.this, "com.wellnessy.glucotracker.provider", file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.resultScrenshot)));
    }

    @OnClick(R.id.shareImageView)
    public void shareButton() {
        requestPermission();
    }

    public void shareImage(){
        snapshotLayout.setDrawingCacheEnabled(true);
        myBitmap = snapshotLayout.getDrawingCache();
        saveBitmap(myBitmap);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    201);

        } else {
            shareImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    shareImage();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
