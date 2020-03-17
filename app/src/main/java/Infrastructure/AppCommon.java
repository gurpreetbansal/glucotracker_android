package Infrastructure;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Patterns;

import com.wellnessy.glucotracker.HistoryActivity;
import com.wellnessy.glucotracker.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class AppCommon {
    public static AppCommon mInstance = null;
    static Context mContext;
    String pathOfFile = null;

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    public static final int GALLARY_PERMISSION_REQUEST_CODE = 2000;

    public static AppCommon getInstance(Context _Context) {
        if (mInstance == null) {
            mInstance = new AppCommon();
        }
        mContext = _Context;
        return mInstance;
    }

    public boolean isUserLogIn() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(MYPerference.IS_USER_LOGIN, false);
    }

    public void setIsUserLogIn(boolean isUserLogIn) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(MYPerference.IS_USER_LOGIN, isUserLogIn);
        mEditor.commit();
    }

    public String userName() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.USER_NAME, null);
    }

    public void setUserName(String userName) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.USER_NAME, userName);
        mEditor.commit();

    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static void showDialog(Activity mactivity, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
        builder.setTitle(title);
        builder.setNegativeButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public String saveUnitTypesIntoSharedPreferences(String mHeightUnitType, String mWeightUnitType, String mGlucoseUnitType, String mHemoGlobinUnitType) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.HEIGHT_UNIT, mHeightUnitType);
        mEditor.putString(MYPerference.WEIGHT_UNIT, mWeightUnitType);
        mEditor.putString(MYPerference.GLUCOSE_UNIT, mGlucoseUnitType);
        mEditor.putString(MYPerference.HEMOGLOBIN_UNIT, mHemoGlobinUnitType);
        mEditor.apply();
        return "SUCCESS";
    }

    public String getHeightUnit() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.HEIGHT_UNIT, null);
    }

    public String getWeightUnit() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.WEIGHT_UNIT, null);
    }

    public String getGlucoseUnit() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.GLUCOSE_UNIT, null);
    }

    public String getHemoglobinUnit() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.HEMOGLOBIN_UNIT, null);
    }

    public String getRemindText() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.REMIND, "");
    }

    public void setRemindText(String remind) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.REMIND, remind);
        mEditor.commit();

    }

    public String getUserId() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.USER_ID, null);
    }

    public void setUserId(String user_id) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.USER_ID, user_id);
        mEditor.apply();
    }

    public String getHeightInch() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.HEIGHT_INCH, null);
    }

    public void setHeightInch(String height_inch) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.HEIGHT_INCH, height_inch);
        mEditor.apply();
    }

    public String getWeightLb() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.WEIGHT_LB, null);
    }

    public void setWeightLb(String weight_lb) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.WEIGHT_LB, weight_lb);
        mEditor.apply();
    }

    public String getGlucoseMg() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.GLU_MG, null);
    }

    public void setGlucoseMg(String glu_mg) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.GLU_MG, glu_mg);
        mEditor.apply();
    }

    public int getMeal() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.MEAL, 0);
    }

    public void setMeal(int meal_value) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.MEAL, meal_value);
        mEditor.apply();
    }

    public int getMedicineState() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.MEDICINE, 1);
    }

    public void setMedicineState(int medicine) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.MEDICINE, medicine);
        mEditor.apply();
    }

    public int getSulphonylureasState() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.SULPHONYLUREAS, 0);
    }

    public void setSulphonylureasState(int sulphony) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.SULPHONYLUREAS, sulphony);
        mEditor.apply();
    }

    public int getBiguanidesState() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.BIGUANIDES, 0);
    }

    public void setBiguanidesState(int biguanides) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.BIGUANIDES, biguanides);
        mEditor.apply();
    }

    public int getGlucosedesesSate() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.GLUCOSEDASES, 0);
    }

    public void setGlucosedasesState(int glucosedases) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.GLUCOSEDASES, glucosedases);
        mEditor.apply();
    }

    public int getMealSizes() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.SIZE, 1);
    }

    public void setMealSizes(int size) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.SIZE, size);
        mEditor.apply();
    }

    public long getLastTestDateTime() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getLong(MYPerference.LAST_TEST_DATE_TIME, 0);
    }

    public void setLastTestDateTime(long lastTestDateTime) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong(MYPerference.LAST_TEST_DATE_TIME, lastTestDateTime);
        mEditor.apply();
    }


    public static String getCurrentDate() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar c = Calendar.getInstance();
        String formatted = format1.format(c.getTime());
        return formatted;
    }

    public static String getCurrentTime() {
        String time;
        int hrs, min;
        String minuteString = "";
        final Calendar c = Calendar.getInstance();
        hrs = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        if (min < 10) {
            minuteString = "0" + min;
        } else {
            minuteString = String.valueOf(min);
        }
        time = hrs + ":" + minuteString;
        return time;
    }


    public static String getDateMinusSevenDate(String date) {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            c.setTime(d);
            c.add(Calendar.DATE, -7);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String formatted = sdf.format(c.getTime());
        return formatted;
    }

    public void setDeviceAddress(String mDeviceAddress) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.DEVICE_MAC_ADDRESS, mDeviceAddress);
        mEditor.apply();
    }

    public String getDeviceAddress() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.DEVICE_MAC_ADDRESS, "");
    }

    public void setDeviceObject(String mDevice) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.DEVICE_OBJECT, mDevice);
        mEditor.apply();
    }

    public String getDeviceObject() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.DEVICE_OBJECT, "");
    }

    public static String getNormalDate(final String inputDateAsString, final String inputStringFormat) throws ParseException {
        DateFormat inputDateFormatter = new SimpleDateFormat(inputStringFormat, Locale.getDefault());
        final Date inputDate = inputDateFormatter.parse(inputDateAsString);
        final DateFormat getUnixDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String s = getUnixDateFormat.format(inputDate);
        s = s.substring(0, 5);
        return s;
    }

    public static String getNormalTime(final String inputTime, final String inputStringFormat) throws ParseException {
        DateFormat inputDateFormatter = new SimpleDateFormat(inputStringFormat, Locale.getDefault());
        final Date inputDate = inputDateFormatter.parse(inputTime);
        final DateFormat getUnixDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String s = getUnixDateFormat.format(inputDate);
        return s;
    }

    public static boolean checkDateValidationHistoryCustomView(HistoryActivity historyActivity, String mFromDateText, String mToDateText) {
        boolean valid = true;
        if (mFromDateText.equals("- -") || mToDateText.equals("- -")) {
            showDialog(historyActivity, "Set Some Date");
            valid = false;
        } else if (!(mFromDateText.equals("- -") && (mToDateText.equals("- -")))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(mFromDateText);
                Date date1 = sdf.parse(mToDateText);
                if (date.compareTo(date1) > 0) {
                    showDialog(historyActivity, mContext.getResources().getString(R.string.fromDate));
                    valid = false;
                } else if (date.compareTo(date1) == 0) {
                    showDialog(historyActivity, mContext.getResources().getString(R.string.dateNotSame));
                    valid = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return valid;
    }


    public static String getFromMonthDateFromMonthSelected(String date) {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            c.setTime(d);
            c.set(Calendar.DAY_OF_MONTH, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String formatted = sdf1.format(c.getTime());
        return formatted;
    }

    public static String getFromMonthDate(int i) {
        int monthSelected = 0;
        Calendar c = Calendar.getInstance();
        String mDay = "01";

        String mDate;
        switch (i) {
            case 0:
                monthSelected = Calendar.JANUARY;

                break;
            case 1:
                monthSelected = Calendar.FEBRUARY;
                break;
            case 2:
                monthSelected = Calendar.MARCH;
                break;
            case 3:
                monthSelected = Calendar.APRIL;
                break;
            case 4:
                monthSelected = Calendar.MAY;
                break;
            case 5:
                monthSelected = Calendar.JUNE;
                break;
            case 6:
                monthSelected = Calendar.JULY;
                break;
            case 7:
                monthSelected = Calendar.AUGUST;
                break;
            case 8:
                monthSelected = Calendar.SEPTEMBER;
                break;
            case 9:
                monthSelected = Calendar.OCTOBER;
                break;
            case 10:
                monthSelected = Calendar.NOVEMBER;
                break;
            case 11:
                monthSelected = Calendar.DECEMBER;
                break;
        }
        mDate = c.get(Calendar.YEAR) + "-" + (monthSelected + 1) + "-" + mDay;
        return mDate;
    }

    public static String getToMonthDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            Date d = sdf.parse(date);
            c.setTime(d);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String formatted = sdf.format(c.getTime());
        return formatted;
    }

    public String getDateMinusSevenDaysDate(Calendar c) {
        c.add(Calendar.DATE, -7);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(c.getTime());
        return formatted;
    }

    public static double getInchToCM(double inch) {

        return (inch * 2.54);
    }

    public static double getCMToInch(double cm) {
        return (cm * 0.393701);
    }

    public static double getKiloToLB(double weight) {
        return (weight * 2.20462);
    }

    public static double getLbToKilo(double lb) {
        return (lb * 0.453592);
    }

    public static float getMgDlFrommMol(float mMol) {
        return (mMol * 18.01801801801802f);
    }

    public static double getmMolFromMgDl(double mMgDl) {
        return (mMgDl * 0.0555);
    }

    public int getTestCount() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.TEST_COUNT, 1);
    }

    public void setTestCount(int count) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.TEST_COUNT, count);
        mEditor.apply();
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    public String getRealPathFromUri(Uri selectedImage) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(selectedImage, null, null, null, null);
        if (cursor == null) {
            result = selectedImage.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void setFilePath(String filePath) {
        pathOfFile = filePath;
    }

    public String getFilePath() {
        return pathOfFile;
    }

    public void setUserNameArrayList(String usernameArrayList) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.UPDATE_NAME, usernameArrayList);
        mEditor.commit();
    }

    public String getUsernameArrayList() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.UPDATE_NAME, "");
    }

    public void setUpdateUsername(String username) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.Update_Username, username);
        mEditor.commit();
    }

    public String getUpdateUsername() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.Update_Username, "");
    }
}