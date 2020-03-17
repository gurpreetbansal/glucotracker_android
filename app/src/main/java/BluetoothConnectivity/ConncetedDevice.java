package BluetoothConnectivity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.wellnessy.glucotracker.ProcessingActivity;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import Database.DbHelper;
import Infrastructure.AppCommon;

public class ConncetedDevice {
    final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    IDevice mDevice = null;

    InputStream mInStream = null;
    OutputStream mOutStream = null;
    boolean mDisconnecting = false;

    Thread mInThread = null;
    Thread mOutThread = null;

    Handler mOutHander = null;

    Handler timerHandler = null;

    String address = "";
    Activity mContext = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    String currentDateandTime = sdf.format(new Date());

    String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
    Date currentLocalTime = cal.getTime();
    DateFormat time = new SimpleDateFormat("HHmm");
    String localTime = time.format(currentLocalTime);

    public ArrayList<String> inputArrayList = new ArrayList<String>();
    int inputValueIndex = 0;
    boolean isAnyCaseShow = false;

    public ConncetedDevice(Activity c, String address) {
        timerHandler = new Handler();
        if (address.startsWith("/dev")) {
            this.mDevice = new SerialDevice(address);
        } else {
            this.mDevice = new BTDevice(address);
        }
        this.address = address;
        this.mContext = c;
        makeInputArrayList();
    }

    public boolean Connect() throws IOException {
        if (this.mInThread != null) {
            return true;
        }
        mDisconnecting = false;

        this.mInThread = new Thread() {

            @Override
            public void run() {
                String data = "";

                try {
                    super.run();

                    ConncetedDevice.this.mDevice.Connect();
                    ConncetedDevice.this.mInStream = ConncetedDevice.this.mDevice.GetInputStream();
                    ConncetedDevice.this.mOutStream = ConncetedDevice.this.mDevice.GetOutputStream();
                    ConncetedDevice.this.sendCmd(inputArrayList.get(inputValueIndex));

                    while (!this.isInterrupted()) {
                        data = ConncetedDevice.this.readData();
                        if (data.length() <= 0) {
                            break;
                        }

                        if (data.contains("GLUS") ||
                                data.contains("XYBH") ||
                                data.contains("XHDB") ||
                                data.contains("XLSD") ||
                                data.contains("XTMB") ||
                                data.contains("HJWD") ||
                                data.contains("HJSD") ||
                                data.contains("TBWD") ||
                                data.contains("TBSD")) {
                            timerHandler.removeCallbacks(timerRunnable);
                            isAnyCaseShow = true;
                            if (outPutHandler != null) {
                                Message msgObj = outPutHandler.obtainMessage();
                                Bundle b = new Bundle();
                                b.putString("message", data);
                                msgObj.setData(b);
                                outPutHandler.sendMessage(msgObj);
                            }
                        } else if (data.startsWith("DDDLZT")) {
                            timerHandler.removeCallbacks(timerRunnable);
                            isAnyCaseShow = true;
                            String strBlock = data.substring(6, data.length() - 1);
                            int batteryLevel = Integer.valueOf(strBlock);
                            if (batteryLevel < 21) {
                                sendMessageToMainThread("Battery_Level_Low");
                                Message msgObj = outPutHandler.obtainMessage();
                                Bundle b = new Bundle();
                                b.putString("message", data);
                                msgObj.setData(b);
                                outPutHandler.sendMessage(msgObj);
                            } else {
                                Message msgObj = outPutHandler.obtainMessage();
                                Bundle b = new Bundle();
                                b.putString("message", data);
                                msgObj.setData(b);
                                outPutHandler.sendMessage(msgObj);
                            }
                        } else {
                            timerHandler.removeCallbacks(timerRunnable);
                            if (data.startsWith("DDJSOK")) {
                                if (inputValueIndex < inputArrayList.size() - 1) {
                                    inputValueIndex++;
                                    if (inputValueIndex == 13) {
                                        sendMessageToMainThread("Env_Start");
                                    }
                                    ConncetedDevice.this.sendCmd(inputArrayList.get(inputValueIndex));
                                }
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
                                ConncetedDevice.this.sendCmd("CCSTAT$");
                            } else if (data.contains("SZER")) {
                                String strBlock = data.substring(6, data.length() - 1);
                                int envTemp = Integer.valueOf(strBlock);
                                if (envTemp == 0) {
                                    sendMessageToMainThread("Finger_Temp_Normal");
                                } else if (envTemp == 1) {
                                    sendMessageToMainThread("Finger_Temp_Low");
                                } else if (envTemp == 2) {
                                    sendMessageToMainThread("Finger_Temp_High");
                                }
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
                            }
                        }
                    }
                } catch (IOException e) {
                    sendMessageToMainThread("Not_Connected");
                    sendPowerOffCommand();
                    if (mDisconnecting) {
                        return;
                    }

                } catch (JSONException e) {
                    if (mDisconnecting) {
                        return;
                    }
                    e.printStackTrace();
                } catch (Exception e) {
                } finally {
                    if (!isAnyCaseShow) {
                        sendMessageToMainThread("SomeThing_Wrong");
                        sendPowerOffCommand();
                    }
                    ConncetedDevice.DelDevice(ConncetedDevice.this.address);
                }
            }

        };

        this.mOutThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ConncetedDevice.this.mOutHander = new Handler();
                Looper.loop();
            }
        };
        this.mOutThread.setDaemon(true);
        this.mOutThread.start();
        this.mInThread.setDaemon(true);
        this.mInThread.start();

        return true;
    }

    public void Disconnect() throws Exception {
        if (mInThread == null) {
            return;
        }
        this.mDisconnecting = true;
        mInThread.interrupt();

        this.mDevice.Disconnect();

        this.mInThread.join();
        this.mOutHander.postAtFrontOfQueue(new Runnable() {

            @Override
            public void run() {
                Looper.myLooper().quit();
            }

        });
        this.mOutThread.join();
        this.mInStream = null;
        this.mOutStream = null;
        timerHandler = null;
        outPutHandler = null;
        handler = null;

    }

    public void sendCmd(String cmd) throws IOException {
        this.mOutHander.post(new OutRunnable(cmd, this.mOutStream));
        timerHandler.postDelayed(timerRunnable, 3000);
    }

    void SendCmd(String cmd, int timeout) throws IOException {

        this.mOutHander.postDelayed(new OutRunnable(cmd, this.mOutStream), timeout);
    }

    void DspPowerOn() throws Exception {
        this.mDevice.DspPowerOn();
    }

    void DspPowerOff() throws Exception {
        this.mDevice.DspPowerOff();
    }

    private String readData() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (this.mInStream.available() <= 0) {
                if (Thread.currentThread().isInterrupted()) {
                    return "";
                }
                try {
                    Thread.sleep(1);
                    continue;
                } catch (InterruptedException e) {
                    return "";
                }
            }
            int b = this.mInStream.read();
            if (b < 0) {
                return "";
            }
            char c = (char) (b & 0xff);
            sb.append(c);
            if (c == '$') {
                break;
            }
        }
        return sb.toString();
    }

    void toFile() {
        try {
            File f = File.createTempFile("jcsj", "");
            inputstreamtofile(this.mInStream, f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    void inputstreamtofile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static final Map<String, ConncetedDevice> sConncetedDeviceMap = new HashMap<String, ConncetedDevice>();

    public static ConncetedDevice PutDevice(String address, ConncetedDevice dev) {
        synchronized (sConncetedDeviceMap) {
            sConncetedDeviceMap.put(address, dev);
        }
        return dev;
    }

    public static ConncetedDevice GetDevice(String address) {
        ConncetedDevice device = null;
        synchronized (sConncetedDeviceMap) {
            device = sConncetedDeviceMap.get(address);
        }
        return device;
    }

    public static ConncetedDevice DelDevice(String address) {
        ConncetedDevice device = null;
        synchronized (sConncetedDeviceMap) {
            device = sConncetedDeviceMap.remove(address);
        }

        return device;
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

    public void sendMessageToMainThread(String msg) {
        isAnyCaseShow = true;
        if (handler != null) {
            Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("message", msg);
            msgObj.setData(b);
            handler.sendMessage(msgObj);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String aResponse = msg.getData().getString("message");
            if ((null != aResponse)) {
                ((ProcessingActivity) mContext).changeScreensAccordingToCase(aResponse);
            } else {

            }
        }
    };

    private Handler outPutHandler = new Handler() {
        public void handleMessage(Message msg) {
            String aResponse = msg.getData().getString("message");
            if ((null != aResponse)) {
                //((ProcessingActivity) mContext).handleOutPutFromDevice(aResponse);
            }
        }
    };

    public Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            sendMessageToMainThread("Not_Connected");
        }
    };

    public void sendPowerOffCommand() {
        try {
            ConncetedDevice.this.sendCmd("CCCSSJ$");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}