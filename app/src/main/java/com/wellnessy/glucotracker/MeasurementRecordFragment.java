package com.wellnessy.glucotracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Adapter.MeasurementRecordAdapter;
import CustomControl.DividerItemDecoration;
import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.HistorySelectedDateResponse;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MeasurementRecordFragment extends Fragment {

    @BindView(R.id.measurementRecyclerView)
    RecyclerView measurementRecyclerView;

    @BindView(R.id.screenshotParentLayout)
    RelativeLayout snapshotLayout;

    DbHelper mDbHelper;
    AppCommon mAppCommon;
    Bitmap myBitmap;
    ArrayList<HistorySelectedDateResponse> mTestResponses;
    ArrayList<HistorySelectedDateResponse> testResponse= new ArrayList<>();
    int i = 0;
    int j = 1;
    int k = 2;
    int l = 3;
    String mColorSelected = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.measurement_record, container, false);
        ButterKnife.bind(this, rootView);
        mTestResponses = new ArrayList<>();
        mAppCommon = AppCommon.getInstance(getContext());
        mDbHelper = new DbHelper(getContext());
        Cursor c = mDbHelper.getAllTestDetails(AppCommon.getInstance(getActivity()).getUserId());
        mTestResponses = getArrayListFromCursor(c);
        for(int i= mTestResponses.size()-1;i>=0;i--){
            Log.i("testResult",mTestResponses.get(i).getmDate()+" "+mTestResponses.get(i).getmTime());
            testResponse.add(mTestResponses.get(i));
        }

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        measurementRecyclerView.setLayoutManager(layout);
        MeasurementRecordAdapter measurementRecordAdapter = new MeasurementRecordAdapter(getActivity(), testResponse, mDbHelper, mAppCommon);
        measurementRecyclerView.setAdapter(measurementRecordAdapter);
        measurementRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return rootView;
    }

    private ArrayList<HistorySelectedDateResponse> getArrayListFromCursor(Cursor c) {
        if (!(c.getCount() == 0)) {
            mTestResponses.clear();
            try {
                while (c.moveToNext()) {
                    int id = c.getInt(c.getColumnIndex("id"));
                    String mUsername = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_USERNAME));
                    String mTestDate = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_DATE));
                    String mBloodFlowSpeed = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_BLOODFLOWSPEED));
                    String mGlucoseValue = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_GLUCOSEVALUE));
                    String mHeamoGlobinValue = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_HEAMOGLOBIN));
                    String mPulseValue = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_PULSE));
                    String mOxygenSaturationValue = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_OXYGENSATURATION));
                    String mEnvironmentTemp = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_ENV_TEMP));
                    String mEnvironmentHumidity = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_ENV_HUMIDITY));
                    String mSurfaceTemp = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_SURFACE_TEMP));
                    String mSurfaceHumidity = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_SURFACE_HUMIDIT));
                    String mDeviceBatteryLevel = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_BATTERY_LEVEL));
                    String mDietValue = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_DIET));
                    String mMedicineValue = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_MEDICINE));
                    String mTestTime = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_TIME));
                    if (c.getPosition() == i) {
                        mColorSelected = getResources().getString(R.string.blueColor);
                        i = i + 4;
                    } else if (c.getPosition() == j) {
                        mColorSelected = getResources().getString(R.string.yellowColor);
                        j = j + 4;
                    } else if (c.getPosition() == k) {
                        mColorSelected = getResources().getString(R.string.greenColor);
                        k = k + 4;
                    } else if (c.getPosition() == l) {
                        mColorSelected = getResources().getString(R.string.redColor);
                        l = l + 4;
                    }
                    mTestResponses.add(c.getPosition(), new HistorySelectedDateResponse(id, mUsername, mTestDate, mBloodFlowSpeed, mGlucoseValue, mHeamoGlobinValue, mPulseValue, mOxygenSaturationValue, mEnvironmentTemp, mEnvironmentHumidity, mSurfaceTemp, mSurfaceHumidity, mDeviceBatteryLevel, mDietValue, mMedicineValue, mTestTime, mColorSelected));
                }
            } finally {
                c.close();
            }
        } else {
            AppCommon.showDialog(getActivity(), getResources().getString(R.string.noRecordFound));
        }
        return mTestResponses;
    }

    public void saveBitmap(Bitmap bitmap) {
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Pictures/record.png";
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
        emailIntent.setType(getResources().getString(R.string.recordEmailApplication));
        Uri myUri = Uri.parse("file://" + path);
        File file=new File(path);
        Uri uri = FileProvider.getUriForFile(getActivity(), "com.wellnessy.glucotracker.provider", file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.recordScreenshot)));
    }

    public void shareButton() {
        if (mTestResponses.size() == 0) {
            AppCommon.showDialog(getActivity(), getResources().getString(R.string.couldnotSendMail));
        } else {
            requestPermission();
        }
    }

    public void shareImage() {
        snapshotLayout.setDrawingCacheEnabled(true);
        myBitmap = snapshotLayout.getDrawingCache();
        saveBitmap(myBitmap);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
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

    public void measurementBackClick() {
        Fragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.screenshotParentLayout, profileFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("MeasurementRecordFragment");
        transaction.commit();
    }
}
