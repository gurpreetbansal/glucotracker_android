package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import Infrastructure.AppCommon;
import Response.SelectUserResponse;
import Response.SwitchUserResponse;


public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RegisterDatabase.db";
    public static final String TABLE_NAME = "Registration";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_hEIGHT = "height";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DIABETES_TYPE = "diabetes_type";
    public static final String COLUMN_GLUCOSE = "glucose";
    public static final String COLUMN_NO_MEDICATION = "noMedicine";
    public static final String COLUMN_SULPHONY = "SULPHONIC";
    public static final String COLUMN_BIGUANIDE = "biguanide_vigory";
    public static final String COLUMN_GLUCOSEDASES = "glucosedases_vigory";
    public static final String COLUMN_INSULIN = "INSULIN";


    public static final String TABLE_TEST = "Test";
    public static final String COLUMN_TEST_USERNAME = "Username";
    public static final String COLUMN_TEST_DATE = "Date";
    public static final String COLUMN_TEST_BLOODFLOWSPEED = "Speed";
    public static final String COLUMN_TEST_GLUCOSEVALUE = "GlucoseValue";
    public static final String COLUMN_TEST_HEAMOGLOBIN = "Heamoglobin";
    public static final String COLUMN_TEST_PULSE = "Pulse";
    public static final String COLUMN_TEST_OXYGENSATURATION = "Spo2";
    public static final String COLUMN_TEST_ENV_TEMP = "EnvTemp";
    public static final String COLUMN_TEST_ENV_HUMIDITY = "EnvHumidity";
    public static final String COLUMN_TEST_SURFACE_TEMP = "SurfaceTemp";
    public static final String COLUMN_TEST_SURFACE_HUMIDIT = "SurfaceHumidity";
    public static final String COLUMN_TEST_BATTERY_LEVEL = "BatteryLevel";
    public static final String COLUMN_TEST_DIET = "Diet";
    public static final String COLUMN_TEST_MEDICINE = "Medicine";
    public static final String COLUMN_TEST_TIME = "Time";

    public static final String TABLE_MEASUREMENT_SETTINGS = "measurement_settings";
    public static final String COLUMN_USER_NAME = "Username";
    public static final String COLUMN_SPO2_LESS = "spo2_less";
    public static final String COLUMN_SPO2_MORE = "spo2_more";
    public static final String COLUMN_HGB_LESS = "hgb_less";
    public static final String COLUMN_HGB_MORE = "hgb_more";
    public static final String COLUMN_BFV_LESS = "bfv_less";
    public static final String COLUMN_BFV_MORE = "bfv_more";
    public static final String COLUMN_PULSE_LESS = "pulse_less";
    public static final String COLUMN_PULSE_MORE = "pulse_more";
    public static final String COLUMN_GLU_MORNING_FASTING_LESS = "glu_morning_fasting_less";
    public static final String COLUMN_GLU_MORNING_FASTING_MORE = "glu_morning_fasting_more";
    public static final String COLUMN_GLU_AFTER_MEAL_LESS = "glu_after_meal_less";
    public static final String COLUMN_GLU_AFTER_MEAL_MORE = "glu_after_meal_more";
    public static final String COLUMN_EMAIL1 = "email1";
    public static final String COLUMN_EMAIL2 = "email2";
    public static final String COLUMN_EMAIL3 = "email3";

    public static final String TABLE_EDIT_REMINDER = "edit_reminder";
    public static final String COLUMN_REMINDER = "reminder";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_REMINDER_TYPE = "reminder_type";
    public static final String COLUMN_SWITCH_BUTTON_STATE = "button_state";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "min";


    public static DbHelper mInstance = null;
    static Context mContext;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static DbHelper getInstance(Context _Context) {
        if (mInstance == null) {
            mInstance = new DbHelper(_Context);
        }
        mContext = _Context;
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        " (id integer primary key," +
                        COLUMN_NAME + " text," +
                        COLUMN_PASSWORD + " text," +
                        COLUMN_GENDER + " text," +
                        COLUMN_AGE + " text," +
                        COLUMN_hEIGHT + " text," +
                        COLUMN_WEIGHT + " text," +
                        COLUMN_IMAGE + " text," +
                        COLUMN_DIABETES_TYPE + " text," +
                        COLUMN_NO_MEDICATION + " text," +
                        COLUMN_BIGUANIDE + " text," +
                        COLUMN_GLUCOSEDASES + " text," +
                        COLUMN_SULPHONY + " text," +
                        COLUMN_INSULIN + " text," +
                        COLUMN_GLUCOSE + " text);");
        db.execSQL(
                "create table " + TABLE_TEST +
                        " (id integer primary key," +
                        COLUMN_TEST_USERNAME + " text," +
                        COLUMN_TEST_DATE + " text," +
                        COLUMN_TEST_BLOODFLOWSPEED + " text," +
                        COLUMN_TEST_GLUCOSEVALUE + " text," +
                        COLUMN_TEST_HEAMOGLOBIN + " text," +
                        COLUMN_TEST_PULSE + " text," +
                        COLUMN_TEST_OXYGENSATURATION + " text," +
                        COLUMN_TEST_ENV_TEMP + " text," +
                        COLUMN_TEST_ENV_HUMIDITY + " text," +
                        COLUMN_TEST_SURFACE_TEMP + " text," +
                        COLUMN_TEST_SURFACE_HUMIDIT + " text," +
                        COLUMN_TEST_BATTERY_LEVEL + " text," +
                        COLUMN_TEST_DIET + " text," +
                        COLUMN_TEST_MEDICINE + " text," +
                        COLUMN_TEST_TIME + " text);");
        db.execSQL(
                "create table " + TABLE_MEASUREMENT_SETTINGS +
                        " (id integer primary key," +
                        COLUMN_USER_NAME + " text," +
                        COLUMN_SPO2_MORE + " text," +
                        COLUMN_SPO2_LESS + " text," +
                        COLUMN_HGB_MORE + " text," +
                        COLUMN_HGB_LESS + " text," +
                        COLUMN_BFV_MORE + " text," +
                        COLUMN_BFV_LESS + " text," +
                        COLUMN_PULSE_MORE + " text," +
                        COLUMN_PULSE_LESS + " text," +
                        COLUMN_GLU_MORNING_FASTING_MORE + " text," +
                        COLUMN_GLU_MORNING_FASTING_LESS + " text," +
                        COLUMN_GLU_AFTER_MEAL_MORE + " text," +
                        COLUMN_GLU_AFTER_MEAL_LESS + " text," +
                        COLUMN_EMAIL1 + " text," +
                        COLUMN_EMAIL2 + " text," +
                        COLUMN_EMAIL3 + " text);");
        db.execSQL(
                "create table " + TABLE_EDIT_REMINDER +
                        " (id integer primary key," +
                        COLUMN_USER_NAME + " text," +
                        COLUMN_REMINDER + " text," +
                        COLUMN_TIME + " text," +
                        COLUMN_SWITCH_BUTTON_STATE + " text," +
                        COLUMN_HOUR + " text," +
                        COLUMN_MINUTE + " text," +
                        COLUMN_REMINDER_TYPE + " text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Registration");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASUREMENT_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDIT_REMINDER);
        onCreate(db);
    }

    public boolean insertContact(String name, String password,
                                 String gender, String age, String height,
                                 String weight, String glucose,
                                 String imagePath, String typeSelected,
                                 String noMedication, String sulphonylureas, String biguanide, String glucosedases, String insulin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_AGE, age);
        contentValues.put(COLUMN_hEIGHT, height);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_IMAGE, imagePath);
        contentValues.put(COLUMN_DIABETES_TYPE, typeSelected);
        contentValues.put(COLUMN_NO_MEDICATION, noMedication);
        contentValues.put(COLUMN_BIGUANIDE, biguanide);
        contentValues.put(COLUMN_GLUCOSEDASES, glucosedases);
        contentValues.put(COLUMN_SULPHONY, sulphonylureas);
        contentValues.put(COLUMN_INSULIN, insulin);
        contentValues.put(COLUMN_GLUCOSE, glucose);

        db.insert("Registration", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Registration where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateContact(String name, String gender, String age, String height, String weight, String glucose, String imagePath, String diabetes_type,
                                 String noMedication, String sulphonylureas, String biguanide, String glucosedases, String insulin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_AGE, age);
        contentValues.put(COLUMN_hEIGHT, height);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_DIABETES_TYPE, diabetes_type);
        contentValues.put(COLUMN_GLUCOSE, glucose);
        contentValues.put(COLUMN_IMAGE, imagePath);
        contentValues.put(COLUMN_NO_MEDICATION, noMedication);
        contentValues.put(COLUMN_SULPHONY, sulphonylureas);
        contentValues.put(COLUMN_BIGUANIDE, biguanide);
        contentValues.put(COLUMN_GLUCOSEDASES, glucosedases);
        contentValues.put(COLUMN_INSULIN, insulin);
        db.update(TABLE_NAME, contentValues, COLUMN_NAME + " =  ? ", new String[]{name});
        return true;
    }

    public boolean updateType2Data(String name, String noMedication, String sulphonylureas,
                                   String biguanide, String glucosedases, String insulin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_NO_MEDICATION, noMedication);
        contentValues.put(COLUMN_SULPHONY, sulphonylureas);
        contentValues.put(COLUMN_BIGUANIDE, biguanide);
        contentValues.put(COLUMN_GLUCOSEDASES, glucosedases);
        contentValues.put(COLUMN_INSULIN, insulin);
        db.update(TABLE_NAME, contentValues, COLUMN_NAME + " =  ? ", new String[]{name});
        return true;
    }

    public Integer deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public void deleteuser(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE " + COLUMN_NAME + "=?", new String[]{String.valueOf(name)});
        db.close();
    }

    public void deleteMultiUsers(String[] ids) { //ids is an array
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME+" IN (" + new String(new char[ids.length-1]).replace("\0", "?,") + "?)", ids);
        db.close();
    }

    public ArrayList<SelectUserResponse> getAllContacts() {
        ArrayList<SelectUserResponse> array_list = new ArrayList<SelectUserResponse>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Registration", null);
        if (res.moveToFirst()) {
            do {
                SelectUserResponse contact = new SelectUserResponse(res.getString(1), res.getString(2));
                contact.setUserName(res.getString(1));
                contact.setPassword(res.getString(2));
                array_list.add(contact);
            } while (res.moveToNext());
        }
        return array_list;
    }

    public ArrayList<SelectUserResponse> getAllUserList() {
        ArrayList<SelectUserResponse> array_list = new ArrayList<SelectUserResponse>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Registration", null);
        if (res.moveToFirst()) {
            do {
                SelectUserResponse contact = new SelectUserResponse(res.getString(1));
                contact.setUserName(res.getString(1));
                if(!contact.getUserName().equals(AppCommon.getInstance(mContext).getUserId())){
                   array_list.add(contact);
               }
            } while (res.moveToNext());
        }
        return array_list;
    }



    public ArrayList<SwitchUserResponse> getSwitchUserData() {
        ArrayList<SwitchUserResponse> switchUserResponses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Registration", null);
        if (res.moveToFirst()) {
            do {
                SwitchUserResponse contact = new SwitchUserResponse(res.getString(1), res.getString(2), res.getString(3), res.getString(4));
                switchUserResponses.add(contact);
            } while (res.moveToNext());
        }
        return switchUserResponses;
    }

    public String Exist(String user) {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query(TABLE_NAME, null, COLUMN_NAME + "=?", new String[]{String.valueOf(user)}, null, null, null);

            if (c == null) {
                return name;
            } else {
                c.moveToFirst();
                name = c.getString(c.getColumnIndex(COLUMN_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }


    public boolean validateUser(String username, String password) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE "
                        + COLUMN_NAME + "='" + username + "'AND " + COLUMN_PASSWORD + "='" + password + "'", null);
        if (c.getCount() > 0)
            return true;
        return false;
    }

    public String getSinlgeEntry(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("Registration", null, " name=?", new String[]{name}, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        cursor.close();
        return password;
    }

    public Cursor getUserData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_NAME + "=?", new String[]{String.valueOf(name)}, null, null, null);
        return c;
    }

    public boolean insertTestDetails(String name,
                                     String date,
                                     String bloodFlowSpeed,
                                     String glucoseValue,
                                     String heamoglobinValue,
                                     String pulseValue,
                                     String oxygenSaturationValue,
                                     String envTemp,
                                     String envHumidity,
                                     String surfaceTemp,
                                     String surfaceHumidity,
                                     String balleryLevel,
                                     String diet,
                                     String medicine,
                                     String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TEST_USERNAME, name);
        contentValues.put(COLUMN_TEST_DATE, date);
        contentValues.put(COLUMN_TEST_BLOODFLOWSPEED, bloodFlowSpeed);
        contentValues.put(COLUMN_TEST_GLUCOSEVALUE, glucoseValue);
        contentValues.put(COLUMN_TEST_HEAMOGLOBIN, heamoglobinValue);
        contentValues.put(COLUMN_TEST_PULSE, pulseValue);
        contentValues.put(COLUMN_TEST_OXYGENSATURATION, oxygenSaturationValue);

        contentValues.put(COLUMN_TEST_ENV_TEMP, envTemp);
        contentValues.put(COLUMN_TEST_ENV_HUMIDITY, envHumidity);
        contentValues.put(COLUMN_TEST_SURFACE_TEMP, surfaceTemp);
        contentValues.put(COLUMN_TEST_SURFACE_HUMIDIT, surfaceHumidity);
        contentValues.put(COLUMN_TEST_BATTERY_LEVEL, balleryLevel);


        contentValues.put(COLUMN_TEST_DIET, diet);
        contentValues.put(COLUMN_TEST_MEDICINE, medicine);
        contentValues.put(COLUMN_TEST_TIME, time);

        db.insert(TABLE_TEST, null, contentValues);
        return true;
    }

    public boolean insertMeasurementSettingsDetails(String name,
                                                    String spo2_more,
                                                    String spo2_less,
                                                    String hgb_more,
                                                    String hgb_less,
                                                    String bfv_more,
                                                    String bfv_less,
                                                    String pulse_more,
                                                    String pulse_less,
                                                    String glu_morning_more,
                                                    String glu_morning_less,
                                                    String glu_after_meal_more,
                                                    String glu_after_meal_less,
                                                    String email1,
                                                    String email2,
                                                    String email3
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_SPO2_MORE, spo2_more);
        contentValues.put(COLUMN_SPO2_LESS, spo2_less);
        contentValues.put(COLUMN_HGB_MORE, hgb_more);
        contentValues.put(COLUMN_HGB_LESS, hgb_less);
        contentValues.put(COLUMN_BFV_MORE, bfv_more);
        contentValues.put(COLUMN_BFV_LESS, bfv_less);
        contentValues.put(COLUMN_PULSE_MORE, pulse_more);
        contentValues.put(COLUMN_PULSE_LESS, pulse_less);
        contentValues.put(COLUMN_GLU_MORNING_FASTING_MORE, glu_morning_more);
        contentValues.put(COLUMN_GLU_MORNING_FASTING_LESS, glu_morning_less);
        contentValues.put(COLUMN_GLU_AFTER_MEAL_MORE, glu_after_meal_more);
        contentValues.put(COLUMN_GLU_AFTER_MEAL_LESS, glu_after_meal_less);
        contentValues.put(COLUMN_EMAIL1, email1);
        contentValues.put(COLUMN_EMAIL2, email2);
        contentValues.put(COLUMN_EMAIL3, email3);
        db.insert(TABLE_MEASUREMENT_SETTINGS, null, contentValues);
        return true;
    }

    public boolean insertEditRemindertDetails(String name,
                                              String reminder,
                                              String time,
                                              String switch_state,
                                              String reminder_type,
                                              String hour,
                                              String min
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_REMINDER, reminder);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_SWITCH_BUTTON_STATE, switch_state);
        contentValues.put(COLUMN_REMINDER_TYPE, reminder_type);
        contentValues.put(COLUMN_HOUR, hour);
        contentValues.put(COLUMN_MINUTE, min);
        db.insert(TABLE_EDIT_REMINDER, null, contentValues);
        return true;
    }

    public boolean updateEditRemindertDetails(String name,
                                              String reminder,
                                              String time,
                                              String reminder_type,
                                              String hour,
                                              String min,
                                              int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_REMINDER, reminder);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_REMINDER_TYPE, reminder_type);
        contentValues.put(COLUMN_HOUR, hour);
        contentValues.put(COLUMN_MINUTE, min);
        db.update(TABLE_EDIT_REMINDER, contentValues, "id =" + (position + 1), null);
        return true;
    }

    public boolean updateMeasurementSettingsDetails(String name,
                                                    String spo2_more,
                                                    String spo2_less,
                                                    String hgb_more,
                                                    String hgb_less,
                                                    String bfv_more,
                                                    String bfv_less,
                                                    String pulse_more,
                                                    String pulse_less,
                                                    String glu_morning_more,
                                                    String glu_morning_less,
                                                    String glu_after_meal_more,
                                                    String glu_after_meal_less,
                                                    String email1,
                                                    String email2,
                                                    String email3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_SPO2_MORE, spo2_more);
        contentValues.put(COLUMN_SPO2_LESS, spo2_less);
        contentValues.put(COLUMN_HGB_MORE, hgb_more);
        contentValues.put(COLUMN_HGB_LESS, hgb_less);
        contentValues.put(COLUMN_BFV_MORE, bfv_more);
        contentValues.put(COLUMN_BFV_LESS, bfv_less);
        contentValues.put(COLUMN_PULSE_MORE, pulse_more);
        contentValues.put(COLUMN_PULSE_LESS, pulse_less);
        contentValues.put(COLUMN_GLU_MORNING_FASTING_MORE, glu_morning_more);
        contentValues.put(COLUMN_GLU_MORNING_FASTING_LESS, glu_morning_less);
        contentValues.put(COLUMN_GLU_AFTER_MEAL_MORE, glu_after_meal_more);
        contentValues.put(COLUMN_GLU_AFTER_MEAL_LESS, glu_after_meal_less);
        contentValues.put(COLUMN_EMAIL1, email1);
        contentValues.put(COLUMN_EMAIL2, email2);
        contentValues.put(COLUMN_EMAIL3, email3);

        db.update(TABLE_MEASUREMENT_SETTINGS, contentValues, COLUMN_USER_NAME + " =  ? ", new String[]{name});
        return true;
    }



    public Cursor getAllTestDetails(String mUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_TEST + " where Username ='" + mUsername + "'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor getTestDetailsForDate(String dateSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_TEST + " where Username ='" + AppCommon.getInstance(mContext).getUserId() + "' AND Date ='" + dateSelected + "'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getTestDetailsForSelectedDates(String currentDate, String currentDayMinusSevenDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_TEST + " where Username ='" + AppCommon.getInstance(mContext).getUserId() + "' AND Date BETWEEN '" + currentDayMinusSevenDate + "'AND '" + currentDate + "'";
        Cursor c = db.rawQuery(sql, null);

        return c;
    }

    public Cursor getLastRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TEST + " WHERE id = " + "(SELECT MAX(id) FROM " + TABLE_TEST + ")";
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor getTestRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TEST + " where Username ='" + AppCommon.getInstance(mContext).getUserId() + "' AND id = " + "(SELECT MAX(id) FROM " + TABLE_TEST + ")";
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor getMeasurementSettingRecord(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_MEASUREMENT_SETTINGS, null, COLUMN_USER_NAME + "=?", new String[]{String.valueOf(username)}, null, null, null);
        return c;
    }

    public Cursor getEditReminderRecord(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_EDIT_REMINDER, null, COLUMN_USER_NAME + "=?", new String[]{String.valueOf(username)}, null, null, null);
        return c;
    }

    public String getTestTypeForUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{DbHelper.COLUMN_DIABETES_TYPE}, COLUMN_NAME + "=?", new String[]{String.valueOf(username)}, null, null, null);
        if (c.getCount() < 1) {
            c.close();
            return "NOT EXIST";
        }
        c.moveToFirst();
        String diabetes_type = c.getString(c.getColumnIndex(COLUMN_DIABETES_TYPE));
        c.close();
        return diabetes_type;
    }

    public void deleteTestDetails(int rowId, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEST, " id = " + rowId, null);

    }

    public boolean updateSwitchButtonEditRemindertState(
            int position, String SwitchState) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SWITCH_BUTTON_STATE, SwitchState);
        db.update(TABLE_EDIT_REMINDER, contentValues, "id =" + (position + 1), null);
        return true;
    }

    public Cursor getDistinctTestDetails(String mUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT  DISTINCT " + DbHelper.COLUMN_TEST_DATE + " FROM " + TABLE_TEST + " where Username ='" + mUsername + "' ORDER BY " + COLUMN_TEST_DATE + " DESC";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUniqueMonthsFromTestDetails(String mUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT strftime('%m', Date) as Month, strftime('%Y', Date) as Year from Test where Username ='" + mUsername + "' ORDER BY " + COLUMN_TEST_DATE + " DESC";
        Cursor c = db.rawQuery(query, null);
        return c;
    }
}