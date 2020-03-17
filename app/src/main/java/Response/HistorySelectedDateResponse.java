package Response;

import java.io.Serializable;

public class HistorySelectedDateResponse implements Serializable {
    private int id;
    private String mUsername;
    private String mDate;
    private String mBloodFlowSpeed;
    private String mGlucoseValue;
    private String mHeamoGlobinValue;
    private String mPulseValue;
    private String mOxygenSaturationValue;
    private String mEnvironmentTemp;
    private String mEnvironmentHumidity;
    private String mSurfaceTemp;
    private String mSurfaceHumidity;
    private String mBatteryLevel;
    private String mDietValue;
    private String mMedicineValue;
    private String mTime;
    private String mDrawableSelected;

    public HistorySelectedDateResponse(int id, String mUsername, String mDate, String mBloodFlowSpeed, String mGlucoseValue, String mHeamoGlobinValue, String mPulseValue, String mOxygenSaturationValue, String mEnvironmentTemp, String mEnvironmentHumidity, String mSurfaceTemp, String mSurfaceHumidity, String mBatteryLevel, String mDietValue, String mMedicineValue, String mTime) {
        this.id = id;
        this.mUsername = mUsername;
        this.mDate = mDate;
        this.mBloodFlowSpeed = mBloodFlowSpeed;
        this.mGlucoseValue = mGlucoseValue;
        this.mHeamoGlobinValue = mHeamoGlobinValue;
        this.mPulseValue = mPulseValue;
        this.mOxygenSaturationValue = mOxygenSaturationValue;
        this.mEnvironmentTemp = mEnvironmentTemp;
        this.mEnvironmentHumidity = mEnvironmentHumidity;
        this.mSurfaceTemp = mSurfaceTemp;
        this.mSurfaceHumidity = mSurfaceHumidity;
        this.mBatteryLevel = mBatteryLevel;
        this.mDietValue = mDietValue;
        this.mMedicineValue = mMedicineValue;
        this.mTime = mTime;


    }

    public HistorySelectedDateResponse(int id, String mUsername, String mDate, String mBloodFlowSpeed, String mGlucoseValue, String mHeamoGlobinValue, String mPulseValue, String mOxygenSaturationValue, String mEnvironmentTemp, String mEnvironmentHumidity, String mSurfaceTemp, String mSurfaceHumidity, String mBatteryLevel, String mDietValue, String mMedicineValue, String mTime, String mDrawableSelected) {
        this.id = id;
        this.mUsername = mUsername;
        this.mDate = mDate;
        this.mBloodFlowSpeed = mBloodFlowSpeed;
        this.mGlucoseValue = mGlucoseValue;
        this.mHeamoGlobinValue = mHeamoGlobinValue;
        this.mPulseValue = mPulseValue;
        this.mOxygenSaturationValue = mOxygenSaturationValue;
        this.mEnvironmentTemp = mEnvironmentTemp;
        this.mEnvironmentHumidity = mEnvironmentHumidity;
        this.mSurfaceTemp = mSurfaceTemp;
        this.mSurfaceHumidity = mSurfaceHumidity;
        this.mBatteryLevel = mBatteryLevel;
        this.mDietValue = mDietValue;
        this.mMedicineValue = mMedicineValue;
        this.mTime = mTime;
        this.mDrawableSelected = mDrawableSelected;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmBloodFlowSpeed() {
        return mBloodFlowSpeed;
    }

    public void setmBloodFlowSpeed(String mBloodFlowSpeed) {
        this.mBloodFlowSpeed = mBloodFlowSpeed;
    }

    public String getmGlucoseValue() {
        return mGlucoseValue;
    }

    public void setmGlucoseValue(String mGlucoseValue) {
        this.mGlucoseValue = mGlucoseValue;
    }

    public String getmHeamoGlobinValue() {
        return mHeamoGlobinValue;
    }

    public void setmHeamoGlobinValue(String mHeamoGlobinValue) {
        this.mHeamoGlobinValue = mHeamoGlobinValue;
    }

    public String getmPulseValue() {
        return mPulseValue;
    }

    public void setmPulseValue(String mPulseValue) {
        this.mPulseValue = mPulseValue;
    }

    public String getmOxygenSaturationValue() {
        return mOxygenSaturationValue;
    }

    public void setmOxygenSaturationValue(String mOxygenSaturationValue) {
        this.mOxygenSaturationValue = mOxygenSaturationValue;
    }

    public String getmEnvironmentTemp() {
        return mEnvironmentTemp;
    }

    public void setmEnvironmentTemp(String mEnvironmentTemp) {
        this.mEnvironmentTemp = mEnvironmentTemp;
    }

    public String getmEnvironmentHumidity() {
        return mEnvironmentHumidity;
    }

    public void setmEnvironmentHumidity(String mEnvironmentHumidity) {
        this.mEnvironmentHumidity = mEnvironmentHumidity;
    }

    public String getmSurfaceTemp() {
        return mSurfaceTemp;
    }

    public void setmSurfaceTemp(String mSurfaceTemp) {
        this.mSurfaceTemp = mSurfaceTemp;
    }

    public String getmSurfaceHumidity() {
        return mSurfaceHumidity;
    }

    public void setmSurfaceHumidity(String mSurfaceHumidity) {
        this.mSurfaceHumidity = mSurfaceHumidity;
    }

    public String getmBatteryLevel() {
        return mBatteryLevel;
    }

    public void setmBatteryLevel(String mBatteryLevel) {
        this.mBatteryLevel = mBatteryLevel;
    }

    public String getmDietValue() {
        return mDietValue;
    }

    public void setmDietValue(String mDietValue) {
        this.mDietValue = mDietValue;
    }

    public String getmMedicineValue() {
        return mMedicineValue;
    }

    public void setmMedicineValue(String mMedicineValue) {
        this.mMedicineValue = mMedicineValue;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmDrawableSelected() {
        return mDrawableSelected;
    }

    public void setmDrawableSelected(String mDrawableSelected) {
        this.mDrawableSelected = mDrawableSelected;
    }
}
