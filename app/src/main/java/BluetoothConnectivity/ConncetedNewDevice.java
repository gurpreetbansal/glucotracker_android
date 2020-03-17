package BluetoothConnectivity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.wellnessy.glucotracker.ProcessingActivity;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.BluetoothDeviceEntity;

import static android.content.ContentValues.TAG;

public class ConncetedNewDevice extends Activity {

    Activity mContext = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    String currentDateandTime = sdf.format(new Date());

    String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
    Date currentLocalTime = cal.getTime();
    DateFormat time = new SimpleDateFormat("HHmm");
    String localTime = time.format(currentLocalTime);

    public ArrayList<String> inputArrayList = new ArrayList<String>();
    int inputValueIndex = -1;
    boolean isAnyCaseShow = false;

    private ACSUtility util;
    private boolean isPortOpen = false;
    private ACSUtility.blePort mCurrentPort, mSelectedPort;

    Handler deviceConnectionTimeHandler = null;
    String firstPacketString;
    String secondPacketString;
    String thirdPacketString;
    String fourthPacketString;

    public String glucose = "";
    public String oxygenSaturation = "";
    public String hemoglobin = "";
    public String speed = "";
    public String pulse = "";
    public String envTemp = "";
    public String envHumidity = "";
    public String surfaceTemp = "";
    public String surfaceHumidity = "";
    public String batteryLevel = "";
    int fourthPacketCount = 0;
    boolean isMoveToNextScreen = false;
    StringBuilder dataRecievedSB = new StringBuilder();

    Handler sendDataTimeHandler = null;

    private ACSUtility.IACSUtilityCallback userCallback = new ACSUtility.IACSUtilityCallback() {

        @Override
        public void didFoundPort(ACSUtility.blePort newPort) {
            // TODO Auto-generated method stub

        }

        @Override
        public void didFinishedEnumPorts() {
            // TODO Auto-generated method stub

        }

        @Override
        public void didOpenPort(ACSUtility.blePort port, Boolean bSuccess) {
            if (bSuccess) {
                isPortOpen = true;
                mCurrentPort = port;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendDataToDevice();
                    }
                });
            } else {
                Log.v("port not connected", "port not connected");
            }
        }

        @Override
        public void didClosePort(ACSUtility.blePort port) {
            // TODO Auto-generated method stub
            isPortOpen = false;
            mCurrentPort = null;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isMoveToNextScreen) {
                        sendMessageToMainThread("Not_Connected");
                    }
                }
            });
        }

        @Override
        public void didPackageReceived(ACSUtility.blePort port, byte[] packageToSend) {
            sendDataTimeHandler.removeCallbacks(sendDataRunnable);
            if (dataRecievedSB != null && dataRecievedSB.toString().contains("$")) {
                dataRecievedSB = new StringBuilder();
            }
            for (byte b : packageToSend) {
                if ((b & 0xff) <= 0x0f) {
                    dataRecievedSB.append("0");
                }
                dataRecievedSB.append((char) (b & 0xff));
            }
            final String mReceivedData = dataRecievedSB.toString();
            Log.v("RecievedData", mReceivedData);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mReceivedData.contains("$")) {
                        if (mReceivedData.equals("DDJSOK$")) {
                            if (inputValueIndex != 0 && inputValueIndex != 2) {
                                sendDataToDevice();
                            }
                        } else {
                            readData(mReceivedData);
                        }
                    }
                }
            });
        }

        @Override
        public void heartbeatDebug() {
            // TODO Auto-generated method stub

        }

        @Override
        public void utilReadyForUse() {
            // TODO Auto-generated method stub
            deviceConnectionTimeHandler.removeCallbacks(connectionDeviceRunnable);
            Gson gson = new Gson();
            BluetoothDeviceEntity deviceEntity = gson.fromJson(AppCommon.getInstance(mContext).getDeviceObject(), BluetoothDeviceEntity.class);
            mSelectedPort = util.new blePort(deviceEntity.getDevice());
            if (isPortOpen) {
                util.closePort();
            } else if (mSelectedPort != null) {
                util.openPort(mSelectedPort);
            } else {
                Log.i(TAG, "User didn't select a port...So the port won't be opened...");
            }
        }

        @Override
        public void didPackageSended(boolean succeed) {
            // TODO Auto-generated method stub
            if (succeed) {
                // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }

    };

    public void connectDevice(Activity c) {
        mContext = c;
        deviceConnectionTimeHandler = new Handler();
        sendDataTimeHandler = new Handler();
        makeInputData();
        util = new ACSUtility(this, userCallback);
        deviceConnectionTimeHandler.postDelayed(connectionDeviceRunnable, 8000);
    }

    public void sendDataToDevice() {
        if (inputValueIndex < inputArrayList.size() - 1) {
            inputValueIndex = inputValueIndex + 1;
            String str = inputArrayList.get(inputValueIndex);
            writeData(str);
            sendDataTimeHandler.postDelayed(sendDataRunnable, 8000);
        }
    }

    public void writeData(String str) {
        if (util != null) {
            try {
                util.writePort(str.getBytes());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Data Send
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void makeInputData() {
        Cursor rs = DbHelper.getInstance(mContext).getUserData(AppCommon.getInstance(mContext).getUserId());
        rs.moveToFirst();
        String id = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_ID));
        String weight = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_WEIGHT));
        String height = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_hEIGHT));
        String age = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_AGE));
        String diabetesType = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_DIABETES_TYPE));
        String glucose = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLUCOSE));

        inputArrayList.add("CCDLJC$");

        String userDataStr = "DDYHSJ";
        userDataStr = userDataStr + AppCommon.getInstance(mContext).getMeal();
        if (diabetesType.equals("Normal")) {
            userDataStr = userDataStr + "0";
            userDataStr = userDataStr + "000";
        } else {
            userDataStr = userDataStr + "2";

            int testValue = (int) Float.parseFloat(glucose);
            Float gulcoseValue = Float.parseFloat(glucose) * 10;
            if (testValue < 10) {
                userDataStr = userDataStr + "0" + String.format("%.0f", gulcoseValue);
            } else {
                userDataStr = userDataStr + String.format("%.0f", gulcoseValue);
            }
        }

        userDataStr = userDataStr + getBMIValue(weight, height);
        String medication = "" + AppCommon.getInstance(mContext).getSulphonylureasState() + "" +
                AppCommon.getInstance(mContext).getBiguanidesState() + "" +
                AppCommon.getInstance(mContext).getGlucosedesesSate() + "0";
        userDataStr = userDataStr + medication + "$";

        inputArrayList.add(userDataStr);
        inputArrayList.add("CCHJJC$");
        inputArrayList.add("CCSTAT$");

    }

    public String getBMIValue(String weight, String height) {
        String bmiValue = "";
        Float weightValue = Float.parseFloat(weight);
        Float heightValue = Float.parseFloat(height);

        Float lowerValue = heightValue / 100;
        lowerValue = lowerValue * lowerValue;

        Float bmi = (weightValue / lowerValue) * 10; // 10 for remove the decimal

        bmiValue = String.format("%.0f", bmi);
        return bmiValue;
    }


    public void makeInputArrayList() {
        Cursor rs = DbHelper.getInstance(mContext).getUserData(AppCommon.getInstance(mContext).getUserId());
        rs.moveToFirst();
        String id = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_ID));
        String weight = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_WEIGHT));
        String height = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_hEIGHT));
        String age = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_AGE));
        String diabetesType = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_DIABETES_TYPE));
        String glucose = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLUCOSE));
        int type = 0;
        if (diabetesType.equals("Normal")) {
            type = 0;
        } else {
            type = 2;
        }

        String mealSizeCondition = "";
        if (AppCommon.getInstance(mContext).getMeal() == 0) {
            mealSizeCondition = "DDYHYS0$";
        } else {
            mealSizeCondition = "DDYHYS" + AppCommon.getInstance(mContext).getMealSizes() + "$";
        }


        int glucoseType = 0;
        if (diabetesType.equals("Normal")) {
            glucoseType = 4;
        } else {
            // if (AppCommon.getInstance(mContext).getMeal() == 0) {
            glucoseType = 0;
            // } else {
            // glucoseType = 2;
            // }
        }
        String glucoseInputValue;

        if (diabetesType.equals("Normal")) {
            glucoseInputValue = "DDTJXT00.00$";
        } else {
            int testValue = (int) Float.parseFloat(glucose);
            if (testValue < 10) {
                glucoseInputValue = "DDTJXT0" + String.format("%.2f", Float.parseFloat(glucose)) + "$";
            } else {
                glucoseInputValue = "DDTJXT" + String.format("%.2f", Float.parseFloat(glucose)) + "$";
            }
        }
        if (!rs.isClosed()) {
            rs.close();
        }
        String inputHeight = "DDYHSG" + height + "$";
        String inputWeight;
        if (Integer.parseInt(weight) <= 99) {
            inputWeight = "DDYHTZ0" + weight + "$";
        } else {
            inputWeight = "DDYHTZ" + weight + "$";
        }
        String inputAge = "DDYHNL" + age + "$";
        String patientType = "DDYHLX" + type + "$";
        String inputGlucose = "DDTJZT" + glucoseType + "$";
        String userId = "DDYHID" + id + "$";
        //String testCount = "DDCLCS" + AppCommon.getInstance(mContext).getTestCount() + "$";
        String testCount = "DDCLCS" + "1" + "$";
        String inputMeal = "DDMEAL" + AppCommon.getInstance(mContext).getMeal() + "$";
        String testDate = "DDJCRQ" + date + "$";
        String testTime = "DDJCSJ" + localTime + "$";
        String dateTime = "DDYHBH" + currentDateandTime + "$";
        String medication = "DDYHCY" + AppCommon.getInstance(mContext).getSulphonylureasState() + "" + AppCommon.getInstance(mContext).getBiguanidesState() + "" + AppCommon.getInstance(mContext).getGlucosedesesSate() + "0" + AppCommon.getInstance(mContext).getMedicineState() + "$";

        inputArrayList.add("CCDLJC$");
        inputArrayList.add(inputMeal);   // Meal
        inputArrayList.add(inputHeight);  //height
        inputArrayList.add(inputWeight);  //Weight
        inputArrayList.add(inputAge);  // age
        inputArrayList.add(userId);   //user-iD
        inputArrayList.add(testCount);   // test time
        inputArrayList.add(dateTime);  //Patient No.
        inputArrayList.add(testDate);  // test Date
        inputArrayList.add(testTime);      // test time
        inputArrayList.add(mealSizeCondition);        // meal condition
        inputArrayList.add(patientType);      // Patient Type
        // inputArrayList.add(inputGlucose);      // blood glucose tested
        inputArrayList.add(medication);    //medicine
        inputArrayList.add(glucoseInputValue);    //glucose Value
        inputArrayList.add("CCHJJC$");       // check humidity
        Log.i("inputArrayList", "" + inputArrayList);
    }

    public void readData(String data) {
        Log.v("Device Data", data);
        if (data.startsWith("DDDLZT")) {
            isAnyCaseShow = true;
            String strBlock = data.substring(6, data.length() - 1);
            int batteryLevel = Integer.valueOf(strBlock);
            if (batteryLevel <= 20) {
                sendMessageToMainThread("Battery_Level_Low");
            }
            sendDataToDevice();
        } else if (data.startsWith("DDHJER")) {
            String strBlock = data.substring(6, data.length() - 1);
            int envTemp = Integer.valueOf(strBlock);
            if (envTemp == 0) {
                sendMessageToMainThread("Env_Temp_Normal");
            } else if (envTemp == 1) {
                sendMessageToMainThread("Env_Temp_Low");
            } else if (envTemp == 2) {
                sendMessageToMainThread("Env_Temp_High");
            } else if (envTemp == 3) {
                sendMessageToMainThread("Env_Temp_Dry");
            } else if (envTemp == 4) {
                sendMessageToMainThread("Env_Temp_Humidity");
            }
            sendDataToDevice();
        } else if (data.contains("CWHM")) {
            String strBlock = data.substring(6, data.length() - 1);
            int envTemp = Integer.valueOf(strBlock);
            if (envTemp == 01) {
                sendMessageToMainThread("Finger_Temp_Low");
            } else if (envTemp == 06) {
                sendMessageToMainThread("Finger_Temp_High");
            } else {
                sendMessageToMainThread("ERBW");
            }
        } else if (data.contains("FRSZ")) {
            sendDataTimeHandler.removeCallbacks(sendDataRunnable);
            sendMessageToMainThread("Env_Start");
            sendMessageToMainThread("Finger_Temp_Normal");
            writeData("CCJSOK$");
        } else if (data.contains("ERBW")) {
            sendMessageToMainThread("ERBW");
        } else if (data.contains("ERAD")) {
            sendMessageToMainThread("ERAD");
        } else if (data.contains("ERTT")) {
            sendMessageToMainThread("ERTT");
        } else if (data.contains("ERRO")) {
            sendMessageToMainThread("ERRO");
        } else if (data.contains("CCFRCS")) {
            sendMessageToMainThread("CCFRCS");
        } else if (data.contains("CSZY")) {
            firstPacketString = data;
            writeData("CCJSOK$");
            readFirstPacket();
        } else if (data.contains("CSZE")) {
            secondPacketString = data;
            writeData("CCJSOK$");
            readSecondPacket();
        } else if (data.contains("CSZS")) {
            thirdPacketString = data;
            writeData("CCJSOK$");
            readThirdPacket();
        } else if (data.contains("FXCS")) {
            fourthPacketString = data;
            fourthPacketCount = fourthPacketCount + 1;
            writeData("CCJSOK$");
            if (fourthPacketCount == 4) {
                writeData("CCJSOK$");
                isMoveToNextScreen = true;
                ((ProcessingActivity) mContext).saveDataAndMoveToNextScreen();
            }
        }
    }

    public void readFirstPacket() {
        firstPacketString = firstPacketString.replace("DDCSZY", "");
        firstPacketString = firstPacketString.replace("$", "");
        glucose = firstPacketString.substring(0, 3);
        oxygenSaturation = firstPacketString.substring(3, 5);
        hemoglobin = firstPacketString.substring(5, 9);
        speed = firstPacketString.substring(9);
    }

    public void readSecondPacket() {
        secondPacketString = secondPacketString.replace("DDCSZE", "");
        secondPacketString = secondPacketString.replace("$", "");
        envTemp = secondPacketString.substring(0, 3);
        envHumidity = secondPacketString.substring(3, 6);
        surfaceTemp = secondPacketString.substring(6, 9);
        surfaceHumidity = secondPacketString.substring(9);
    }

    public void readThirdPacket() {
        thirdPacketString = thirdPacketString.replace("DDCSZS", "");
        thirdPacketString = thirdPacketString.replace("$", "");
        pulse = thirdPacketString.substring(0, 3);
        batteryLevel = thirdPacketString.substring(3, 6);

        pulse = Integer.valueOf(pulse).toString();
        batteryLevel = Integer.valueOf(batteryLevel).toString();

    }

    public void sendMessageToMainThread(String str) {
        ((ProcessingActivity) mContext).changeScreensAccordingToCase(str);
    }

    public Runnable connectionDeviceRunnable = new Runnable() {
        @Override
        public void run() {
            sendMessageToMainThread("Not_Connected");
        }
    };

    public Runnable sendDataRunnable = new Runnable() {
        @Override
        public void run() {
            sendMessageToMainThread("Not_Connected");
        }
    };

    public void makeUtilNull() {
        if (util != null) {
            util.closePort();
        }
        userCallback = null;
        util = null;
    }
}