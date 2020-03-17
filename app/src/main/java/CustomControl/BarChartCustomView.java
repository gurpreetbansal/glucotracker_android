package CustomControl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wellnessy.glucotracker.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.HistorySelectedDateResponse;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BarChartCustomView extends LinearLayout {
    @Nullable
    @BindView(R.id.chart)
    BarChart mBarChart;
    ArrayList<HistorySelectedDateResponse> mHistoryResponsesArrayList = new ArrayList<>();
    public static String[] mDatesArray;
    public static String[] mTimesArray;
    String mGetSelectedColumn;
    String selecteGlucoseValueColumn = "";
    DecimalFormat mFormat;
    String selectedDay;


    public BarChartCustomView(Context context) {
        super(context);
    }

    public BarChartCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChartCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void intializeViews(Context context, ArrayList<HistorySelectedDateResponse> mDateResponsesList) {
        this.mHistoryResponsesArrayList = mDateResponsesList;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.bar_chart_custom_view, this);
        ButterKnife.bind(this, v);
        if (!mHistoryResponsesArrayList.isEmpty()) {
            BarData mBarData;
            mDatesArray = new String[mHistoryResponsesArrayList.size()];
            mTimesArray = new String[mHistoryResponsesArrayList.size()];

            if (selectedDay.equals(getResources().getString(R.string.dayText))) {
                mBarData = new BarData(getmTimeArray(), getDataSetAccordingSelectedItem(mGetSelectedColumn));
            } else {
                mBarData = new BarData(getmDatesArray(), getDataSetAccordingSelectedItem(mGetSelectedColumn));
            }
            assert mBarChart != null;
            mBarChart.setData(mBarData);
            mBarData.notifyDataChanged();
            mBarChart.setDescription(" ");
            mBarChart.setTouchEnabled(true);
            mBarChart.getAxisLeft().setStartAtZero(true);
            mBarChart.getAxisRight().setEnabled(false);
            mBarChart.getXAxis().setLabelsToSkip(0);
            mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            mBarChart.setAutoScaleMinMaxEnabled(true);
            mBarChart.setDragOffsetX(20.0f);
            mBarChart.animateY(2000);
            mBarChart.getLegend().setEnabled(false);
            mBarChart.setVisibleXRange(8, 8);
            mBarChart.notifyDataSetChanged();
            mBarChart.invalidate();
        } else {
            BarData mBarData = new BarData();
            assert mBarChart != null;
            mBarChart.setData(mBarData);
            mBarData.notifyDataChanged();
            mBarChart.setTouchEnabled(false);
            mBarChart.setAutoScaleMinMaxEnabled(true);
            mBarChart.getAxisRight().setEnabled(false);
            mBarChart.setDescription(" ");
            mBarChart.setNoDataText(getResources().getString(R.string.noRecordFound));
            mBarChart.getPaint(Chart.PAINT_INFO).setColor(getResources().getColor(R.color.black));
            mBarChart.notifyDataSetChanged();
            mBarChart.invalidate();
        }
    }


    public String[] getmDatesArray() {
        String dateFormat = "yyyy-MM-dd";
        for (int i = 0; i < mHistoryResponsesArrayList.size(); i++) {
            String inputDate = mHistoryResponsesArrayList.get(i).getmDate();
            try {
                String normalDate = AppCommon.getNormalDate(inputDate, dateFormat);
                mDatesArray[i] = normalDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mDatesArray;
    }

    public String[] getmTimeArray() {
        String timeFormat = "HH:mm";
        for (int i = 0; i < mHistoryResponsesArrayList.size(); i++) {
            String inputDate = mHistoryResponsesArrayList.get(i).getmTime();
            try {
                String normaTime = AppCommon.getNormalTime(inputDate, timeFormat);
                mTimesArray[i] = normaTime;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return mTimesArray;
    }


    private ArrayList<IBarDataSet> getDataSetAccordingSelectedItem(String mGetSelectedColumn) {
        ArrayList<IBarDataSet> mBarDataSets = null;
        ArrayList<BarEntry> valueBarEntryArrayList = new ArrayList<>();
        for (int i = 0; i < mHistoryResponsesArrayList.size(); i++) {
            if (mGetSelectedColumn.equals(DbHelper.COLUMN_TEST_GLUCOSEVALUE)) {
                if (selecteGlucoseValueColumn.equals(getResources().getString(R.string.mMolText))) {
                    String values = mHistoryResponsesArrayList.get(i).getmGlucoseValue();
                    Double doubleValues = Double.parseDouble(values);
                    doubleValues = (doubleValues / 10);
                    String mMolStringValues = String.valueOf(doubleValues);
                    mMolStringValues = mMolStringValues.replace(',', '.');
                    float f = Float.parseFloat(mMolStringValues);
                    BarEntry mBarEntry = new BarEntry(f, i);
                    valueBarEntryArrayList.add(mBarEntry);
                } else {
                    float doubleValues = Float.parseFloat(mHistoryResponsesArrayList.get(i).getmGlucoseValue());
                    doubleValues = (doubleValues / 10.0f);
                    float mMolValues = AppCommon.getInstance(getContext()).getMgDlFrommMol(doubleValues);
                    String mMolStringValues = String.valueOf(mMolValues);
                    mMolStringValues = mMolStringValues.replace(',', '.');
                    BarEntry mBarEntry = new BarEntry(Float.valueOf(mMolStringValues), i);
                    valueBarEntryArrayList.add(mBarEntry);
                }
            } else if (mGetSelectedColumn.equals(DbHelper.COLUMN_TEST_PULSE)) {
                Double doubleValues = Double.parseDouble(mHistoryResponsesArrayList.get(i).getmPulseValue());
                String mStringValues = String.valueOf(doubleValues);
                BarEntry mBarEntry = new BarEntry(Float.valueOf(mStringValues), i);
                valueBarEntryArrayList.add(mBarEntry);

            } else if (mGetSelectedColumn.equals(DbHelper.COLUMN_TEST_BLOODFLOWSPEED)) {
                Double doubleValues = Double.parseDouble(mHistoryResponsesArrayList.get(i).getmBloodFlowSpeed());
                String mStringValues = String.valueOf(doubleValues);
                BarEntry mBarEntry = new BarEntry(Float.valueOf(mStringValues), i);
                valueBarEntryArrayList.add(mBarEntry);

            } else if (mGetSelectedColumn.equals(DbHelper.COLUMN_TEST_HEAMOGLOBIN)) {
                if (selecteGlucoseValueColumn.equals(getResources().getString(R.string.gPerdl))) {
                    String values = mHistoryResponsesArrayList.get(i).getmHeamoGlobinValue();
                    Double doubleValues = Double.parseDouble(values);
                    doubleValues = (doubleValues / 100);
                    String mMolStringValues = String.valueOf(doubleValues);
                    mMolStringValues = mMolStringValues.replace(',', '.');
                    BarEntry mBarEntry = new BarEntry(Float.valueOf(mMolStringValues), i);
                    valueBarEntryArrayList.add(mBarEntry);
                } else {
                    String values = mHistoryResponsesArrayList.get(i).getmHeamoGlobinValue();
                    Double doubleValues = Double.parseDouble(values);
                    doubleValues = (doubleValues / 10);
                    String mdoubleStringValues = String.valueOf(doubleValues);
                    mdoubleStringValues = mdoubleStringValues.replace(',', '.');
                    BarEntry mBarEntry = new BarEntry(Float.valueOf(mdoubleStringValues), i);
                    valueBarEntryArrayList.add(mBarEntry);
                }
            } else if (mGetSelectedColumn.equals(DbHelper.COLUMN_TEST_OXYGENSATURATION)) {
                Double doubleValues = Double.parseDouble(mHistoryResponsesArrayList.get(i).getmOxygenSaturationValue());
                String mStringValues = String.valueOf(doubleValues);
                BarEntry mBarEntry = new BarEntry(Float.valueOf(mStringValues), i);
                valueBarEntryArrayList.add(mBarEntry);
            }
        }
        BarDataSet mBarDataSet1 = new BarDataSet(valueBarEntryArrayList, "");
        mBarDataSet1.setColors(new int[]{R.color.lightBlue, R.color.orangeBrown, R.color.green, R.color.lightYellow}, getContext());
        mBarDataSet1.setBarSpacePercent(10f);
        mBarDataSet1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                mFormat = new DecimalFormat("###,###,##0.0");
                return mFormat.format(value);
            }
        });
        mBarDataSets = new ArrayList<>();
        mBarDataSets.add(mBarDataSet1);

        return mBarDataSets;

    }

    public void setSelectedItem(String getSelectedItem) {
        this.mGetSelectedColumn = getSelectedItem;
    }

    public void setSelectedUnit(String selecteGlucoseValueColumn) {
        this.selecteGlucoseValueColumn = selecteGlucoseValueColumn;
    }

    public void setSelectedDay(String day) {
        this.selectedDay = day;

    }
}
