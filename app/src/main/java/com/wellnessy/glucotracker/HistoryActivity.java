package com.wellnessy.glucotracker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import CustomControl.BarChartCustomView;
import CustomControl.HistoryListCustomView;
import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.HistorySelectedDateResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class HistoryActivity extends AppCompatActivity {
    @BindView(R.id.dayTextView)
    TextViewGothamBook mDayTextView;

    @BindView(R.id.weekTextView)
    TextViewGothamBook mWeekTextView;

    @BindView(R.id.monthTextView)
    TextViewGothamBook mMonthTextView;

    @BindView(R.id.periodTextView)
    TextViewGothamBook mPeriodTextView;

    @BindView(R.id.mLargeUnitButton)
    TextViewGothamBook mMolButton;

    @BindView(R.id.chartButton)
    TextViewGothamBook mChartButton;

    @BindView(R.id.dateTextView)
    TextViewGothamBook mDateTextView;

    @BindView(R.id.mSmallUnitButton)
    TextViewGothamBook mMgdlButton;

    @BindView(R.id.listButton)
    TextViewGothamBook mListButton;

    @BindView(R.id.columnsView)
    LinearLayout mThreeColumnsView;

    @BindView(R.id.glucoseView)
    RelativeLayout mGlucoseView;

    @BindView(R.id.pulseView)
    RelativeLayout mPulseView;

    @BindView(R.id.bloodFlowView)
    RelativeLayout mBloodFlowView;

    @BindView(R.id.hemoglobiinView)
    RelativeLayout mHemoglobiinView;

    @BindView(R.id.sp02View)
    RelativeLayout mSp02View;

    @BindView(R.id.glucoseTextViewLine)
    TextViewGothamBook mGlucoseTextViewLine;

    @BindView(R.id.pulseTextViewLine)
    TextViewGothamBook mPulseTextViewLine;

    @BindView(R.id.hemoglobinTextViewLine)
    TextViewGothamBook mHemoglobinTextViewLine;

    @BindView(R.id.bloodFlowTextViewLine)
    TextViewGothamBook mBloodFlowTextViewLine;

    @BindView(R.id.sp02TextViewLine)
    TextViewGothamBook mSp02TextViewLine;

    @BindView(R.id.chart_fragment)
    BarChartCustomView mChartView;

    @BindView(R.id.historyListFragment)
    HistoryListCustomView mHistoryListView;

    @BindView(R.id.lowestValueTextView)
    TextViewGothamBook mLowestValue;

    @BindView(R.id.highestValueTextView)
    TextViewGothamBook mHighestValue;

    @BindView(R.id.averageValueTextView)
    TextViewGothamBook mAverageValue;

    int mYear, mMonth, mDay;

    @BindView(R.id.viewsLayout)
    LinearLayout mViewsLayout;

    Context mContext = this;
    DbHelper dbHelper;
    ArrayList<HistorySelectedDateResponse> mDateResponsesList;
    ArrayList<HistorySelectedDateResponse> mTimeResponseList;
    ArrayList<Integer> mIntegerValuesArrayList;
    ArrayList<Float> mFloatValuesArraylist;

    public String selectedColumn = "";
    public String selectedUnit = "";
    public static String getSelectedType = "";
    public String selectedMonth = "";
    public String selectedDate = "";
    public String[] mDatesArray;
    public String[] mMonthArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        dbHelper = new DbHelper(this);
        Cursor c = dbHelper.getDistinctTestDetails(AppCommon.getInstance(this).getUserId());
        mDatesArray = new String[c.getCount()];
        int position = 0;
        if (!(c.getCount() == 0)) {
            try {
                while (c.moveToNext()) {
                    position = c.getPosition();
                    mDatesArray[position] = c.getString(c.getColumnIndex(DbHelper.COLUMN_TEST_DATE));
                }
            } finally {
                c.close();
            }
        }
        mDateResponsesList = new ArrayList<>();
        mIntegerValuesArrayList = new ArrayList<>();
        mFloatValuesArraylist = new ArrayList<>();
        getSelectedType = getResources().getString(R.string.dayText);
        mChartView.setSelectedDay(getSelectedType);
        mGlucoseTextViewLine.setSelected(true);
        if (AppCommon.getInstance(this).getGlucoseUnit().equals(getResources().getString(R.string.mMolText))) {
            selectedUnit = getResources().getString(R.string.mMolText);
            mMolButton.setSelected(true);
            mMolButton.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            selectedUnit = getResources().getString(R.string.mMgdlText);
            mMgdlButton.setSelected(true);
            mMgdlButton.setTextColor(getResources().getColor(android.R.color.white));
        }
        mChartView.setSelectedUnit(selectedUnit);
        selectedColumn = mGetSelectedColumn();
        Cursor lastRecordCursor = dbHelper.getLastRecord();
        mDateResponsesList = getArrayListFromCursor(lastRecordCursor);
        if (mDateResponsesList.size() != 0) {
            selectedDate = mDateResponsesList.get(0).getmDate();
            Cursor lastSelectedDateRecordCursor = DbHelper.getInstance(this).getTestDetailsForDate(selectedDate);
            mDateResponsesList = getArrayListFromCursor(lastSelectedDateRecordCursor);
        }
        for (int i = 0; i < mDateResponsesList.size(); i++) {
            HistorySelectedDateResponse mHistorySelectedDateResponse = mDateResponsesList.get(i);
        }
        mChartView.setSelectedItem(selectedColumn);
        mHistoryListView.intializeViews(this, mDateResponsesList, selectedUnit, selectedColumn);
        mChartView.intializeViews(this, mDateResponsesList);
        setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
        mChartView.setVisibility(View.VISIBLE);
        mThreeColumnsView.setVisibility(View.VISIBLE);
        mDayTextView.setSelected(true);
        mDayTextView.setTextColor(getResources().getColor(android.R.color.white));
        mChartButton.setSelected(true);
        mChartButton.setTextColor(getResources().getColor(android.R.color.white));
        mDateTextView.setSelected(true);
        mDateTextView.setText(selectedDate);
        mDateTextView.setTextColor(getResources().getColor(android.R.color.white));
    }

    private ArrayList<HistorySelectedDateResponse> getArrayListFromCursor(Cursor c) {
        mDateResponsesList.clear();
        if (!(c.getCount() == 0)) {
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
                    mDateResponsesList.add(c.getPosition(), new HistorySelectedDateResponse(id, mUsername, mTestDate, mBloodFlowSpeed, mGlucoseValue, mHeamoGlobinValue, mPulseValue, mOxygenSaturationValue, mEnvironmentTemp, mEnvironmentHumidity, mSurfaceTemp, mSurfaceHumidity, mDeviceBatteryLevel, mDietValue, mMedicineValue, mTestTime));
                }
            } finally {
                c.close();
            }
        } else {
            AppCommon.showDialog(this, getResources().getString(R.string.noRecordFound));
            mLowestValue.setText(" ");
            mHighestValue.setText(" ");
            mAverageValue.setText(" ");
        }
        return mDateResponsesList;
    }

    @OnClick(R.id.glucoseView)
    public void setmGlucoseTextViewLine() {
        if (!mGlucoseView.isSelected()) {
            mGlucoseTextViewLine.setSelected(true);
            mPulseTextViewLine.setSelected(false);
            mBloodFlowTextViewLine.setSelected(false);
            mHemoglobinTextViewLine.setSelected(false);
            mSp02TextViewLine.setSelected(false);
            if (AppCommon.getInstance(this).getGlucoseUnit().equals(getString(R.string.mMolText))) {
                selectedUnit = getResources().getString(R.string.mMolText);
            } else {
                selectedUnit = getResources().getString(R.string.mMgdlText);
            }
            selectedColumn = mGetSelectedColumn();
            mChartView.setSelectedUnit(selectedUnit);
            mChartView.setSelectedItem(selectedColumn);
            mChartView.intializeViews(this, mDateResponsesList);
            setAveragesValue(selectedColumn, mDateResponsesList);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
        }
    }

    @OnClick(R.id.pulseView)
    public void setmPulseTextViewLine() {
        if (!mPulseView.isSelected()) {
            mGlucoseTextViewLine.setSelected(false);
            mPulseTextViewLine.setSelected(true);
            mBloodFlowTextViewLine.setSelected(false);
            mHemoglobinTextViewLine.setSelected(false);
            mSp02TextViewLine.setSelected(false);
            selectedColumn = mGetSelectedColumn();
            setAveragesValue(selectedColumn, mDateResponsesList);
            mChartView.setSelectedItem(selectedColumn);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
            mChartView.intializeViews(this, mDateResponsesList);
        }
    }

    @OnClick(R.id.bloodFlowView)
    public void setmBloodFlowTextViewLine() {
        if (!mBloodFlowView.isSelected()) {
            mGlucoseTextViewLine.setSelected(false);
            mPulseTextViewLine.setSelected(false);
            mBloodFlowTextViewLine.setSelected(true);
            mHemoglobinTextViewLine.setSelected(false);
            mSp02TextViewLine.setSelected(false);
            selectedColumn = mGetSelectedColumn();
            setAveragesValue(selectedColumn, mDateResponsesList);
            mChartView.setSelectedItem(selectedColumn);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
            mChartView.intializeViews(this, mDateResponsesList);
        }
    }

    @OnClick(R.id.hemoglobiinView)
    public void setmHemoglobinTextViewLine() {
        if (!mHemoglobiinView.isSelected()) {
            mGlucoseTextViewLine.setSelected(false);
            mPulseTextViewLine.setSelected(false);
            mBloodFlowTextViewLine.setSelected(false);
            mHemoglobinTextViewLine.setSelected(true);
            mSp02TextViewLine.setSelected(false);
            if (AppCommon.getInstance(this).getHemoglobinUnit().equals(getString(R.string.gPerLitreText))) {
                selectedUnit = getResources().getString(R.string.gPerLitreText);
            } else {
                selectedUnit = getResources().getString(R.string.gPerdl);
            }
            mChartView.setSelectedUnit(selectedUnit);
            selectedColumn = mGetSelectedColumn();
            setAveragesValue(selectedColumn, mDateResponsesList);
            mChartView.setSelectedItem(selectedColumn);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
            mChartView.intializeViews(this, mDateResponsesList);
        }
    }

    @OnClick(R.id.sp02View)
    public void setmSp02TextViewLine() {
        if (!mSp02View.isSelected()) {
            mGlucoseTextViewLine.setSelected(false);
            mPulseTextViewLine.setSelected(false);
            mBloodFlowTextViewLine.setSelected(false);
            mHemoglobinTextViewLine.setSelected(false);
            mSp02TextViewLine.setSelected(true);
            selectedColumn = mGetSelectedColumn();
            setAveragesValue(selectedColumn, mDateResponsesList);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
            mChartView.setSelectedItem(selectedColumn);
            mChartView.intializeViews(this, mDateResponsesList);
        }
    }

    @OnClick(R.id.dayTextView)
    public void setmDayTextView() {
        if (!mDayTextView.isSelected()) {
            mDayTextView.setSelected(true);
            mWeekTextView.setSelected(false);
            mMonthTextView.setSelected(false);
            mPeriodTextView.setSelected(false);
            mDayTextView.setTextColor(getResources().getColor(android.R.color.white));
            mWeekTextView.setTextColor(getResources().getColor(android.R.color.black));
            mMonthTextView.setTextColor(getResources().getColor(android.R.color.black));
            mPeriodTextView.setTextColor(getResources().getColor(android.R.color.black));
            Cursor c = dbHelper.getTestDetailsForDate(selectedDate);
            mDateTextView.setText(selectedDate);
            getSelectedType = getResources().getString(R.string.dayText);
            mChartView.setSelectedDay(getSelectedType);
            mDateResponsesList = getArrayListFromCursor(c);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
            mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);
            setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
        }
    }


    @OnClick(R.id.weekTextView)
    public void setmWeekTextView() {
        if (!mWeekTextView.isSelected()) {
            mWeekTextView.setSelected(true);
            mDayTextView.setSelected(false);
            mMonthTextView.setSelected(false);
            mPeriodTextView.setSelected(false);
            mWeekTextView.setTextColor(getResources().getColor(android.R.color.white));
            mDayTextView.setTextColor(getResources().getColor(android.R.color.black));
            mMonthTextView.setTextColor(getResources().getColor(android.R.color.black));
            mPeriodTextView.setTextColor(getResources().getColor(android.R.color.black));
            getSelectedType = getResources().getString(R.string.weekText);
            mChartView.setSelectedDay(getSelectedType);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                Date d = sdf.parse(selectedDate);
                c.setTime(d);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mDateTextView.setText(selectedDate);
            String selectedDayMinusSevenDays = AppCommon.getInstance(HistoryActivity.this).getDateMinusSevenDaysDate(c);
            Cursor lastSelectedDateRecordCursor = DbHelper.getInstance(this).getTestDetailsForSelectedDates(selectedDate, selectedDayMinusSevenDays);
            mDateResponsesList = getArrayListFromCursor(lastSelectedDateRecordCursor);
            setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
            mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);
        }
    }

    @OnClick(R.id.monthTextView)
    public void setmMonthTextView() {
        if (!mMonthTextView.isSelected()) {
            mMonthTextView.setSelected(true);
            mDayTextView.setSelected(false);
            mWeekTextView.setSelected(false);
            mPeriodTextView.setSelected(false);
            intializeMonthDatesArray();
            getSelectedType = getResources().getString(R.string.monthText);
            mChartView.setSelectedDay(getSelectedType);
            mMonthTextView.setTextColor(getResources().getColor(android.R.color.white));
            mDayTextView.setTextColor(getResources().getColor(android.R.color.black));
            mWeekTextView.setTextColor(getResources().getColor(android.R.color.black));
            mPeriodTextView.setTextColor(getResources().getColor(android.R.color.black));
            if (selectedMonth.equals("")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                try {
                    Date d = sdf.parse(selectedDate);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    c.setTime(d);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                int i = c.get(Calendar.MONTH);
                i += 1;
                mDateTextView.setText(c.get(Calendar.YEAR) + "-" + i);
                Calendar cal = Calendar.getInstance();   // this takes current date
                cal.set(Calendar.DAY_OF_MONTH, 1);
                Date testDate = cal.getTime();
                String mFromDate =  sdf.format(testDate);
                String mToDate = AppCommon.getToMonthDate(mFromDate);
                Cursor selectedMonthValue = dbHelper.getTestDetailsForSelectedDates(mToDate, mFromDate);
                mDateResponsesList = getArrayListFromCursor(selectedMonthValue);
                setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
                mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
                mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);
            } else {
                SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM");
                Calendar c = Calendar.getInstance();
                try {
                    Date d = mSimpleDateFormat.parse(selectedMonth);
                    c.set(Calendar.DAY_OF_MONTH, 1);
                    c.setTime(d);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                int i = c.get(Calendar.MONTH);
                i += 1;
                mDateTextView.setText(c.get(Calendar.YEAR) + "-" + i);
                Calendar cal = Calendar.getInstance();   // this takes current date
                cal.set(Calendar.DAY_OF_MONTH, 1);
                Date testDate = cal.getTime();
                String mFromDate =  mSimpleDateFormat.format(testDate);
                String mToDate = AppCommon.getToMonthDate(mFromDate);
                Cursor selectedMonthValue = dbHelper.getTestDetailsForSelectedDates(mToDate, mFromDate);
                mDateResponsesList = getArrayListFromCursor(selectedMonthValue);
                setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
                mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
                mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);
            }

        }
    }

    private void intializeMonthDatesArray() {
        Cursor cursor = DbHelper.getInstance(HistoryActivity.this).getUniqueMonthsFromTestDetails(AppCommon.getInstance(HistoryActivity.this).getUserId());
        int position = 0;
        mMonthArray = new String[cursor.getCount()];
        if (!(cursor.getCount() == 0)) {
            try {
                while (cursor.moveToNext()) {
                    position = cursor.getPosition();
                    mMonthArray[position] = cursor.getString(cursor.getColumnIndex("Year")) + "-" + cursor.getString(cursor.getColumnIndex("Month"));
                }
            } finally {
                cursor.close();
            }
        }
    }


    @OnClick(R.id.periodTextView)
    public void setmPeriodTextView() {
        if (!mPeriodTextView.isSelected()) {
            mPeriodTextView.setSelected(true);
            mMonthTextView.setSelected(false);
            mDayTextView.setSelected(false);
            mWeekTextView.setSelected(false);
            mPeriodTextView.setTextColor(getResources().getColor(android.R.color.white));
            mDayTextView.setTextColor(getResources().getColor(android.R.color.black));
            mWeekTextView.setTextColor(getResources().getColor(android.R.color.black));
            mMonthTextView.setTextColor(getResources().getColor(android.R.color.black));
            showPeriodView();

        }
    }

    public void showPeriodView() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.period_dialog_view, null);
        AlertDialog.Builder mABuilder = new AlertDialog.Builder(this);
        mABuilder.setView(alertLayout);
        final TextViewGothamBook mFromCalendarView = (TextViewGothamBook) alertLayout.findViewById(R.id.fromDateTextView);
        final TextViewGothamBook mTocalendarView = (TextViewGothamBook) alertLayout.findViewById(R.id.toDateTextView);

        mFromCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mABuilder = new AlertDialog.Builder(mContext);
                mABuilder.setItems(mDatesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFromCalendarView.setText(mDatesArray[i]);
                    }
                });
                mABuilder.show();

            }
        });

        mTocalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mABuilder = new AlertDialog.Builder(mContext);
                mABuilder.setItems(mDatesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTocalendarView.setText(mDatesArray[i]);
                    }
                });
                mABuilder.show();

            }
        });
        mABuilder.setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mFromDateText = mFromCalendarView.getText().toString();
                        String mToDateText = mTocalendarView.getText().toString();
                        if (AppCommon.checkDateValidationHistoryCustomView(HistoryActivity.this, mFromDateText, mToDateText)) {
                            String mFromDate = mFromCalendarView.getText().toString();
                            String mToDate = mTocalendarView.getText().toString();
                            Cursor selectedPeriodValue = dbHelper.getTestDetailsForSelectedDates(mToDate, mFromDate);
                            mDateResponsesList = getArrayListFromCursor(selectedPeriodValue);
                            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
                            mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);
                            setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancelText), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = mABuilder.create();
        dialog.show();
    }

    @OnClick(R.id.mLargeUnitButton)
    public void setmMolButton() {
        if (!mMolButton.isSelected()) {
            mMolButton.setSelected(true);
            mMgdlButton.setSelected(false);
            mMolButton.setTextColor(getResources().getColor(android.R.color.white));
            mMgdlButton.setTextColor(getResources().getColor(android.R.color.black));

            if (selectedColumn == DbHelper.COLUMN_TEST_GLUCOSEVALUE) {
                selectedUnit = getResources().getString(R.string.mMolText);
            } else if (selectedColumn == DbHelper.COLUMN_TEST_HEAMOGLOBIN) {
                selectedUnit = getResources().getString(R.string.gPerLitreText);
            }
            mChartView.setSelectedUnit(selectedUnit);
            mChartView.setSelectedItem(selectedColumn);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
            mChartView.intializeViews(this, mDateResponsesList);
            setAveragesValue(selectedColumn, mDateResponsesList);
        }
    }

    @OnClick(R.id.mSmallUnitButton)
    public void setmMgdlButton() {
        if (!mMgdlButton.isSelected()) {
            mMgdlButton.setSelected(true);
            mMolButton.setSelected(false);
            mMgdlButton.setTextColor(getResources().getColor(android.R.color.white));
            mMolButton.setTextColor(getResources().getColor(android.R.color.black));

            if (selectedColumn == DbHelper.COLUMN_TEST_GLUCOSEVALUE) {
                selectedUnit = getResources().getString(R.string.mMgdlText);
            } else if (selectedColumn == DbHelper.COLUMN_TEST_HEAMOGLOBIN) {
                selectedUnit = getResources().getString(R.string.gPerdl);
            }

            mChartView.setSelectedUnit(selectedUnit);
            setAveragesValue(selectedColumn, mDateResponsesList);
            mChartView.setSelectedItem(selectedColumn);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
            mChartView.intializeViews(this, mDateResponsesList);
        }
    }

    @OnClick(R.id.chartButton)
    public void setmChartButton() {
        if (!mChartButton.isSelected()) {
            mChartButton.setSelected(true);
            mListButton.setSelected(false);
            mChartButton.setTextColor(getResources().getColor(android.R.color.white));
            mListButton.setTextColor(getResources().getColor(android.R.color.black));
            mViewsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
            mThreeColumnsView.setVisibility(View.VISIBLE);
            mChartView.setVisibility(View.VISIBLE);
            mChartView.intializeViews(this, mDateResponsesList);
            setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
            mHistoryListView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.listButton)
    public void setmListButton() {
        if (!mListButton.isSelected()) {
            mListButton.setSelected(true);
            mChartButton.setSelected(false);
            mListButton.setTextColor(getResources().getColor(android.R.color.white));
            mChartButton.setTextColor(getResources().getColor(android.R.color.black));
            mThreeColumnsView.setVisibility(View.GONE);
            mChartView.setVisibility(View.GONE);
            mViewsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.4f));
            mHistoryListView.setVisibility(View.VISIBLE);
            setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
            mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
        }
    }

    @OnClick(R.id.dateTextView)
    public void setMDateCalendarView() {
        AlertDialog.Builder mABuilder = new AlertDialog.Builder(this);
        if (getSelectedType.equals(getString(R.string.dayText)) || getSelectedType.equals(getString(R.string.weekText))) {
            mABuilder.setItems(mDatesArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDateTextView.setText(mDatesArray[i]);
                    selectedDate = mDateTextView.getText().toString();
                    if (getSelectedType.equals(getResources().getString(R.string.dayText))) {
                        Cursor selectedDayValue = dbHelper.getTestDetailsForDate(selectedDate);
                        mDateResponsesList = getArrayListFromCursor(selectedDayValue);
                        setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
                        mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
                        mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);
                    } else if (getSelectedType.equals(getResources().getString(R.string.weekText))) {
                        String selectedDayMinusSevenDays = AppCommon.getInstance(HistoryActivity.this).getDateMinusSevenDate(selectedDate);
                        Cursor selectedDatesValue = DbHelper.getInstance(HistoryActivity.this).getTestDetailsForSelectedDates(selectedDate, selectedDayMinusSevenDays);
                        mDateResponsesList = getArrayListFromCursor(selectedDatesValue);
                        setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
                        mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
                        mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);
                    }
                }
            });
            mABuilder.show();
        } else if (getSelectedType.equals(getResources().getString(R.string.monthText))) {
            mABuilder.setItems(mMonthArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDateTextView.setText(mMonthArray[i]);
                    selectedMonth = mDateTextView.getText().toString();
                    String fromDate = AppCommon.getInstance(HistoryActivity.this).getFromMonthDateFromMonthSelected(selectedMonth);
                    String toDate = AppCommon.getInstance(HistoryActivity.this).getToMonthDate(fromDate);
                    Cursor selectedDatesValue = DbHelper.getInstance(HistoryActivity.this).getTestDetailsForSelectedDates(toDate, fromDate);
                    mDateResponsesList = getArrayListFromCursor(selectedDatesValue);
                    setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
                    mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
                    mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);

                }
// String mFromDate = AppCommon.getFromMonthDate(c.get(Calendar.MONTH));
//                    String mToDate = AppCommon.getToMonthDate(mFromDate);
//                    Cursor selectedMonthValue = dbHelper.getTestDetailsForSelectedDates(mToDate, mFromDate);
//                    mDateResponsesList = getArrayListFromCursor(selectedMonthValue);
//                    mHistoryListView.notifyAdapterWithUnitAndData(selectedUnit, selectedColumn, mDateResponsesList);
//                    mChartView.intializeViews(HistoryActivity.this, mDateResponsesList);
//                    setAveragesValue(mGetSelectedColumn(), mDateResponsesList);
            });
            mABuilder.show();
        }
    }

    @OnClick(R.id.backIcon)
    public void backClick() {
        this.finish();
    }

    private String mGetSelectedColumn() {
        String mSelectedColumnName = "";
        if (mGlucoseTextViewLine.isSelected()) {
            mSelectedColumnName = DbHelper.COLUMN_TEST_GLUCOSEVALUE;
            mMolButton.setText(getResources().getString(R.string.mMolText));
            mMgdlButton.setVisibility(View.VISIBLE);
            mMgdlButton.setText(getResources().getString(R.string.mMgdlText));
            if (selectedUnit.equals(getString(R.string.mMolText))) {
                mMolButton.setSelected(true);
                mMolButton.setTextColor(getResources().getColor(R.color.white));
                mMgdlButton.setSelected(false);
                mMgdlButton.setTextColor(getResources().getColor(R.color.black));
            } else {
                mMolButton.setSelected(false);
                mMolButton.setTextColor(getResources().getColor(R.color.black));
                mMgdlButton.setSelected(true);
                mMgdlButton.setTextColor(getResources().getColor(R.color.white));
            }
        } else if (mPulseTextViewLine.isSelected()) {
            mSelectedColumnName = DbHelper.COLUMN_TEST_PULSE;
            selectedUnit = "";
            mMolButton.setText(getResources().getString(R.string.timePerMinText));
            mMolButton.setTextColor(getResources().getColor(R.color.white));
            mMolButton.setSelected(true);
            mMgdlButton.setVisibility(View.GONE);
        } else if (mBloodFlowTextViewLine.isSelected()) {
            mSelectedColumnName = DbHelper.COLUMN_TEST_BLOODFLOWSPEED;
            selectedUnit = "";
            mMolButton.setText(getResources().getString(R.string.AUText));
            mMolButton.setTextColor(getResources().getColor(R.color.white));
            mMolButton.setSelected(true);
            mMgdlButton.setVisibility(View.GONE);
        } else if (mHemoglobinTextViewLine.isSelected()) {
            mSelectedColumnName = DbHelper.COLUMN_TEST_HEAMOGLOBIN;
            mMgdlButton.setVisibility(View.VISIBLE);
            mMolButton.setText(getResources().getString(R.string.gPerLitreText));
            mMgdlButton.setText(getResources().getString(R.string.gPerdl));
            if (selectedUnit.equals(getResources().getString(R.string.gPerLitreText))) {
                mMolButton.setTextColor(getResources().getColor(R.color.white));
                mMolButton.setSelected(true);
                mMgdlButton.setSelected(false);
                mMgdlButton.setTextColor(getResources().getColor(R.color.black));
            } else {
                mMolButton.setTextColor(getResources().getColor(R.color.black));
                mMolButton.setSelected(false);
                mMgdlButton.setSelected(true);
                mMgdlButton.setText(getResources().getString(R.string.gPerdl));
                mMgdlButton.setTextColor(getResources().getColor(R.color.white));
            }
        } else if (mSp02TextViewLine.isSelected()) {
            mSelectedColumnName = DbHelper.COLUMN_TEST_OXYGENSATURATION;
            selectedUnit = "";
            mMolButton.setText(getResources().getString(R.string.percentageText));
            mMolButton.setTextColor(getResources().getColor(R.color.white));
            mMolButton.setSelected(true);
            mMgdlButton.setVisibility(View.GONE);
        }
        return mSelectedColumnName;
    }

    private void setAveragesValue(String mSelectedColumnName, ArrayList<HistorySelectedDateResponse> mDateResponsesList) {
        mIntegerValuesArrayList.clear();
        mFloatValuesArraylist.clear();
        if (mDateResponsesList.size() != 0) {
            for (int i = 0; i < mDateResponsesList.size(); i++) {
                switch (mSelectedColumnName) {
                    case DbHelper.COLUMN_TEST_GLUCOSEVALUE:
                        if (selectedUnit.equals(getResources().getString(R.string.mMolText))) {
                            String values = mDateResponsesList.get(i).getmGlucoseValue();
                            double doubleValues = Double.parseDouble(values);
                            doubleValues = (doubleValues / 10);
                            String mMolStringValues = String.valueOf(doubleValues);
                            mFloatValuesArraylist.add(i, Float.parseFloat(mMolStringValues));
                            getMaxMInAndAverageValuesFromFloatValuesArraylist(mFloatValuesArraylist);
                        } else {
                            String values = mDateResponsesList.get(i).getmGlucoseValue();
                            float doubleValues = Float.parseFloat(values);
                            doubleValues = (doubleValues / 10.0f);
                            float mMolValues = AppCommon.getInstance(this).getMgDlFrommMol(doubleValues);
                            String mMolStringValues = String.valueOf(mMolValues);
                            mFloatValuesArraylist.add(i, Float.parseFloat(mMolStringValues));
                            getMaxMInAndAverageValuesFromFloatValuesArraylist(mFloatValuesArraylist);
                        }
                        break;
                    case DbHelper.COLUMN_TEST_HEAMOGLOBIN:
                        if (selectedUnit.equals(getResources().getString(R.string.gPerdl))) {
                            String values = mDateResponsesList.get(i).getmHeamoGlobinValue();
                            double doubleValues = Double.parseDouble(values);
                            doubleValues = (doubleValues / 100);
                            String mMolStringValues = String.valueOf(doubleValues);
                            mFloatValuesArraylist.add(i, Float.parseFloat(mMolStringValues));
                            getMaxMInAndAverageValuesFromFloatValuesArraylist(mFloatValuesArraylist);
                        } else {
                            String values = mDateResponsesList.get(i).getmHeamoGlobinValue();
                            double doubleValues = Double.parseDouble(values);
                            doubleValues = (doubleValues / 10);
                            String mdoubleStringValues = String.valueOf(doubleValues);
                            mFloatValuesArraylist.add(i, Float.parseFloat(mdoubleStringValues));
                            getMaxMInAndAverageValuesFromFloatValuesArraylist(mFloatValuesArraylist);
                        }
                        break;

                    case DbHelper.COLUMN_TEST_BLOODFLOWSPEED:

                        mIntegerValuesArrayList.add(i, Integer.parseInt(mDateResponsesList.get(i).getmBloodFlowSpeed()));
                        getMaxMInAndAverageValuesFromIntegerValuesArraylist(mIntegerValuesArrayList);
                        break;

                    case DbHelper.COLUMN_TEST_PULSE:

                        mIntegerValuesArrayList.add(i, Integer.parseInt(mDateResponsesList.get(i).getmPulseValue()));
                        getMaxMInAndAverageValuesFromIntegerValuesArraylist(mIntegerValuesArrayList);
                        break;

                    case DbHelper.COLUMN_TEST_OXYGENSATURATION:
                        mIntegerValuesArrayList.add(i, Integer.parseInt(mDateResponsesList.get(i).getmOxygenSaturationValue()));
                        getMaxMInAndAverageValuesFromIntegerValuesArraylist(mIntegerValuesArrayList);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void getMaxMInAndAverageValuesFromIntegerValuesArraylist(ArrayList<Integer> mIntegerValuesArrayList) {
        Integer sum = 0;
        double average = 0;
        int min = mIntegerValuesArrayList.get(0);
        int max = mIntegerValuesArrayList.get(0);

        for (Integer i : mIntegerValuesArrayList) {
            if (i < min) min = i;
            if (i > max) max = i;
            sum += i;
            average = sum.doubleValue() / mIntegerValuesArrayList.size();
        }
        mLowestValue.setText(String.valueOf(min));
        mHighestValue.setText(String.valueOf(max));
        mAverageValue.setText(String.format("%.1f", average));
    }

    private void getMaxMInAndAverageValuesFromFloatValuesArraylist(ArrayList<Float> mFloatValuesArraylist) {
        float sum = 0;
        double average = 0;
        float min = mFloatValuesArraylist.get(0);
        float max = mFloatValuesArraylist.get(0);

        for (Float i : mFloatValuesArraylist) {
            if (i < min) min = i;
            if (i > max) max = i;
            sum += i;
            average = sum / mFloatValuesArraylist.size();
        }
        mLowestValue.setText(String.format("%.1f", min));
        mHighestValue.setText(String.format("%.1f", max));
        mAverageValue.setText(String.format("%.1f", average));
    }

    public void createExcel() {
        String Fnamexls = "excel.xls";
        File file = new File(Environment.getExternalStorageDirectory() + "/", Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet(getResources().getString(R.string.firstSheet), 0);
            WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
            cellFont.setBoldStyle(WritableFont.BOLD);
            WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setWrap(true);
            cellFormat.setAlignment(Alignment.CENTRE);
            Label date = new Label(0, 0, getResources().getString(R.string.dateExcel), cellFormat);
            Label time = new Label(1, 0, getResources().getString(R.string.timeExcel), cellFormat);
            Label glucose = new Label(2, 0, getResources().getString(R.string.glucoseMgExcel), cellFormat);
            Label glucoseMg= new Label(3,0, getResources().getString(R.string.glucosemg),cellFormat);
            Label hemoglobin = new Label(4, 0, getResources().getString(R.string.hemoglobinGExcel), cellFormat);
            Label pulse = new Label(5, 0, getResources().getString(R.string.pulseExcel), cellFormat);
            Label spo2 = new Label(6, 0, getResources().getString(R.string.sp02Text), cellFormat);
            Label bf = new Label(7, 0, getResources().getString(R.string.bloodFlowExcel), cellFormat);
            try {
                sheet.setColumnView(0, 15);
                sheet.setColumnView(1, 15);
                sheet.setColumnView(2, 15);
                sheet.setColumnView(3, 15);
                sheet.setColumnView(4, 15);
                sheet.setColumnView(5, 15);
                sheet.setColumnView(6, 15);
                sheet.setColumnView(7, 15);
                sheet.addCell(date);
                sheet.addCell(time);
                sheet.addCell(glucose);
                sheet.addCell(glucoseMg);
                sheet.addCell(hemoglobin);
                sheet.addCell(pulse);
                sheet.addCell(spo2);
                sheet.addCell(bf);
                for (int i = 0; i < mDateResponsesList.size(); i++) {
                    HistorySelectedDateResponse response = mDateResponsesList.get(i);
                    Label dateData = new Label(0, (i + 1), response.getmDate());
                    Label timeData = new Label(1, (i + 1), response.getmTime());

                    float glucoseValue = Float.valueOf(response.getmGlucoseValue());
                    glucoseValue=glucoseValue/10.0f;
                    Label glucoseData = new Label(2, (i + 1), String.valueOf(String.format("%.1f", glucoseValue)));

                    float glucoseMgdata= Float.valueOf(response.getmGlucoseValue());
                    glucoseMgdata = glucoseMgdata / 10.0f;
                    glucoseMgdata= AppCommon.getInstance(this).getMgDlFrommMol(glucoseMgdata);
                    Label glucoseMgValue= new Label(3, (i + 1),String.valueOf(String.format("%.1f", glucoseMgdata)));

                    float globinValue = Float.valueOf(response.getmHeamoGlobinValue());
                    globinValue = globinValue / 10.0f;
                    Label hemoglobinData = new Label(4, (i + 1), String.valueOf(globinValue));

                    Label pulseData = new Label(5, (i + 1), response.getmPulseValue());
                    Label spo2Data = new Label(6, (i + 1), response.getmOxygenSaturationValue());
                    Label bfData = new Label(7, (i + 1), response.getmBloodFlowSpeed());

                    sheet.addCell(dateData);
                    sheet.addCell(timeData);
                    sheet.addCell(glucoseData);
                    sheet.addCell(glucoseMgValue);
                    sheet.addCell(hemoglobinData);
                    sheet.addCell(pulseData);
                    sheet.addCell(spo2Data);
                    sheet.addCell(bfData);
                }
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            File externalStorage = Environment.getExternalStorageDirectory();
            Uri uri = Uri.fromFile(new File(externalStorage, "excel.xls"));
            Uri photoURI = FileProvider.getUriForFile(HistoryActivity.this, "com.wellnessy.glucotracker.provider", file);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType(getResources().getString(R.string.applicationExcel));
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.healthAlert));
            sendIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.esserHealthCareTechnology));
            startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.excelFileText)));
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
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
            createExcel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createExcel();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick(R.id.shareImageView)
    public void shareImageClick() {
        if (mDateResponsesList.size() == 0) {
            AppCommon.showDialog(this, getResources().getString(R.string.couldnotSendMail));
        } else {
            requestPermission();
        }
    }
}
