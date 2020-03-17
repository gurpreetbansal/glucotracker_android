package com.wellnessy.glucotracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TestForNormalActivity extends Activity {
    @BindView(R.id.personProfileImage)
    CircleImageView mPersonProfileImage;

    @BindView(R.id.morningFastingLayout)
    RelativeLayout morningFastingRelativeLayout;

    @BindView(R.id.postprandialLayout)
    RelativeLayout postprandialRelativeLayout;

    @BindView(R.id.morningFastingTick)
    ImageView morningFastingTickImageView;

    @BindView(R.id.postprandialTick)
    ImageView postprandialTickImageView;

    @BindView(R.id.personNameText)
    TextViewGothamBook personNameTextView;

    @BindView(R.id.weightTextView)
    TextViewGothamBook weightTextView;

    @BindView(R.id.ageTextView)
    TextViewGothamBook ageTextView;

    @BindView(R.id.heightTextView)
    TextViewGothamBook heightTextView;

    @BindView(R.id.type2Layout)
    LinearLayout type2Layout;

    @BindView(R.id.noMedicationSwitch)
    ImageView noMedicationSwitchButton;

    @BindView(R.id.sulphonylureasSwitch)
    ImageView sulphonyureasSwitchButton;

    @BindView(R.id.biguanidesSwitch)
    ImageView biguanidesSwitchButton;

    @BindView(R.id.havingNoFoodText)
    TextView havingNoFoodTextView;

    @BindView(R.id.setAsNormalText)
    TextView setAsNormalTextView;

    @BindView(R.id.glucosedasesSwitch)
    ImageView glucosedasesSwitchButton;

    @BindView(R.id.testButton)
    Button testButton;
    private static final int RESULT_OK = 2;

    int BLUETOOTH_CONNECTIVITY = 1000;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_for_normal);
        ButterKnife.bind(this);
        postprandialTickImageView.setVisibility(View.INVISIBLE);
        Cursor rs = DbHelper.getInstance(this).getUserData(AppCommon.getInstance(this).getUserId());
        rs.moveToFirst();

        String name = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_NAME));
        String weight = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_WEIGHT));
        String height = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_hEIGHT));
        String age = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_AGE));
        String imagePath = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_IMAGE));
        String noMedication = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_NO_MEDICATION));
        String sulphonylureas = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_SULPHONY));
        String biguanide = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_BIGUANIDE));
        String glucosedases = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLUCOSEDASES));
        userType = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_DIABETES_TYPE));

        if (!rs.isClosed()) {
            rs.close();
        }
        if (!(userType.equals(getResources().getString(R.string.notExist)))) {
            if (userType.equals("Normal")) {
                AppCommon.getInstance(this).setMedicineState(1);
                mPersonProfileImage.setVisibility(View.VISIBLE);
                type2Layout.setVisibility(View.GONE);
            } else {
                mPersonProfileImage.setVisibility(View.GONE);
                type2Layout.setVisibility(View.VISIBLE);
                AppCommon.getInstance(this).setMedicineState(Integer.parseInt(noMedication));
                AppCommon.getInstance(this).setSulphonylureasState(Integer.parseInt(sulphonylureas));
                AppCommon.getInstance(this).setBiguanidesState(Integer.parseInt(biguanide));
                AppCommon.getInstance(this).setGlucosedasesState(Integer.parseInt(glucosedases));
            }
        } else {
            AppCommon.showDialog(this, getResources().getString(R.string.noTypeSelected));
        }

        mPersonProfileImage.setBorderColor(getResources().getColor(R.color.blue));
        if (imagePath != null && !imagePath.equals("")) {
            mPersonProfileImage.setImageDrawable(Drawable.createFromPath(imagePath));
        }
        mPersonProfileImage.setBorderWidth(2);
        personNameTextView.setText(AppCommon.getInstance(this).getUpdateUsername());
        if (AppCommon.getInstance(this).getHeightUnit().equals(getResources().getString(R.string.cmText))) {
            heightTextView.setText(height + "" + getResources().getString(R.string.centimeter));
        } else if (AppCommon.getInstance(this).getHeightUnit().equals(getResources().getString(R.string.inText))) {
            double heightInch = AppCommon.getCMToInch(Integer.parseInt(height));
            heightTextView.setText(String.format("%.1f", heightInch) + "" + getResources().getString(R.string.inText));
        }
        if (AppCommon.getInstance(this).getWeightUnit().equals(getResources().getString(R.string.kgText))) {
            weightTextView.setText(weight + "" + getResources().getString(R.string.kgText));
        } else if (AppCommon.getInstance(this).getWeightUnit().equals(getResources().getString(R.string.lbText))) {
            double weightLb = AppCommon.getKiloToLB(Double.parseDouble(weight));
            weightTextView.setText(String.format("%.1f", weightLb) + "" + getResources().getString(R.string.lbText));
        }
        ageTextView.setText(age + "" + getResources().getString(R.string.yearsText));
        setMedicationStates();
    }

    public void setMedicationStates() {
        if (AppCommon.getInstance(this).getMedicineState() == 0) {
            noMedicationSwitchButton.setSelected(false);
            sulphonyureasSwitchButton.setSelected(true);
            biguanidesSwitchButton.setSelected(true);
            glucosedasesSwitchButton.setSelected(true);
        } else {
            noMedicationSwitchButton.setSelected(true);
            sulphonyureasSwitchButton.setSelected(false);
            biguanidesSwitchButton.setSelected(false);
            glucosedasesSwitchButton.setSelected(false);
        }

        if (AppCommon.getInstance(this).getSulphonylureasState() == 0) {
            sulphonyureasSwitchButton.setSelected(false);
        } else {
            sulphonyureasSwitchButton.setSelected(true);
        }

        if (AppCommon.getInstance(this).getBiguanidesState() == 0) {
            biguanidesSwitchButton.setSelected(false);
        } else {
            biguanidesSwitchButton.setSelected(true);
        }

        if (AppCommon.getInstance(this).getGlucosedesesSate() == 0) {
            glucosedasesSwitchButton.setSelected(false);
        } else {
            glucosedasesSwitchButton.setSelected(true);
        }
    }

    @OnClick(R.id.morningFastingLayout)
    public void setMorningFastingRelativeLayout() {
        morningFastingRelativeLayout.setSelected(true);
        postprandialRelativeLayout.setSelected(false);
        morningFastingTickImageView.setVisibility(View.VISIBLE);
        postprandialTickImageView.setVisibility(View.INVISIBLE);
        if (userType.equals("Type 2")) {
            havingNoFoodTextView.setVisibility(View.VISIBLE);
            setAsNormalTextView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.postprandialLayout)
    public void setPostprandialRelativeLayout() {
        postprandialRelativeLayout.setSelected(true);
        morningFastingRelativeLayout.setSelected(false);
        postprandialTickImageView.setVisibility(View.VISIBLE);
        morningFastingTickImageView.setVisibility(View.INVISIBLE);
        havingNoFoodTextView.setVisibility(View.GONE);
        setAsNormalTextView.setVisibility(View.GONE);
        Intent intent = new Intent(TestForNormalActivity.this, Test2HoursActivity.class);
        startActivity(intent);

    }

    public int getSelectedType() {
        int selectedType = 0;
        if (morningFastingRelativeLayout.isSelected()) {
            AppCommon.getInstance(this).setMeal(selectedType);

        } else if (postprandialRelativeLayout.isSelected()) {
            selectedType = 2;
            AppCommon.getInstance(this).setMeal(selectedType);
        }
        return selectedType;
    }

    @OnClick(R.id.backIcon)
    public void backClick() {
        this.finish();
    }

    @OnClick(R.id.testButton)
    public void testButtonClick() {
        getSelectedType();
        if (morningFastingRelativeLayout.isSelected() || postprandialRelativeLayout.isSelected()) {
            if (isTestProcess()) {
                if (!AppCommon.getInstance(this).getDeviceAddress().equals("")) {
                    Intent intent = new Intent(TestForNormalActivity.this, ProcessingActivity.class);
                    startActivity(intent);
                    this.finish();
                } else {
                    Intent bluetoothIntent = new Intent(this, BluetoothActivity.class);
                    startActivityForResult(bluetoothIntent, BLUETOOTH_CONNECTIVITY);
                }
            } else {
                AppCommon.showDialog(this, getResources().getString(R.string.pleaseWaitFor3Minutes));
            }
        } else {
            AppCommon.showDialog(this, getResources().getString(R.string.pleaseChooseMealFirst));
        }
    }

    public boolean isTestProcess() {
        boolean isTestProcess = false;
        long previousTime = AppCommon.getInstance(this).getLastTestDateTime();
        long currentTime = Calendar.getInstance().getTimeInMillis();

        long diff = currentTime - previousTime;

        long diffInSecond = diff / 1000;
        long diffInMinute = diff / (60 * 1000);
        long diffInHour = diff / (60 * 60 * 1000);
        long diffInDays = diff / (24 * 60 * 60 * 1000);

        if (previousTime == 0) {
            isTestProcess = true;
        } else if (diffInDays > 0 || diffInHour > 0) {
            isTestProcess = true;
        } else if (diffInMinute >= 5) {
            isTestProcess = true;
        } else {
            isTestProcess = false;
        }
        return isTestProcess;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_CONNECTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(TestForNormalActivity.this, ProcessingActivity.class);
                startActivity(intent);
                this.finish();
            }

//            if (requestCode == 1) {
//                if (resultCode == Activity.RESULT_OK) {
//                    //String whereString = data.getStringExtra("CompleteAddress");
//
//                }
//            }
        }
    }

    @OnClick(R.id.noMedicationSwitch)
    public void buttonClick() {
        if (AppCommon.getInstance(this).getMedicineState() == 0) {
            noMedicationSwitchButton.setSelected(true);
            sulphonyureasSwitchButton.setSelected(false);
            biguanidesSwitchButton.setSelected(false);
            glucosedasesSwitchButton.setSelected(false);
            AppCommon.getInstance(this).setSulphonylureasState(0);
            AppCommon.getInstance(this).setBiguanidesState(0);
            AppCommon.getInstance(this).setGlucosedasesState(0);
            AppCommon.getInstance(this).setMedicineState(1);
        } else if (AppCommon.getInstance(this).getMedicineState() == 1) {
            noMedicationSwitchButton.setSelected(false);
            sulphonyureasSwitchButton.setSelected(true);
            biguanidesSwitchButton.setSelected(true);
            glucosedasesSwitchButton.setSelected(true);
            AppCommon.getInstance(this).setSulphonylureasState(1);
            AppCommon.getInstance(this).setBiguanidesState(1);
            AppCommon.getInstance(this).setGlucosedasesState(1);
            AppCommon.getInstance(this).setMedicineState(0);
        }
        updateUserData();
    }

    public void updateUserData() {
        DbHelper.getInstance(this).updateType2Data(AppCommon.getInstance(this).getUserId(),
                Integer.toString(AppCommon.getInstance(this).getMedicineState()),
                Integer.toString(AppCommon.getInstance(this).getSulphonylureasState()),
                Integer.toString(AppCommon.getInstance(this).getBiguanidesState()),
                Integer.toString(AppCommon.getInstance(this).getGlucosedesesSate()), "0");
    }

    @OnClick(R.id.sulphonylureasSwitch)
    public void sulphonyButtonClick() {
        if (AppCommon.getInstance(this).getSulphonylureasState() == 0) {
            sulphonyureasSwitchButton.setSelected(true);
            noMedicationSwitchButton.setSelected(false);
            AppCommon.getInstance(this).setMedicineState(0);
            AppCommon.getInstance(this).setSulphonylureasState(1);
        } else {
            sulphonyureasSwitchButton.setSelected(false);
            AppCommon.getInstance(this).setSulphonylureasState(0);
        }
        updateUserData();
    }

    @OnClick(R.id.biguanidesSwitch)
    public void biguanidesSwitchClick() {
        if (AppCommon.getInstance(this).getBiguanidesState() == 0) {
            biguanidesSwitchButton.setSelected(true);
            noMedicationSwitchButton.setSelected(false);
            AppCommon.getInstance(this).setMedicineState(0);
            AppCommon.getInstance(this).setBiguanidesState(1);
        } else {
            biguanidesSwitchButton.setSelected(false);
            AppCommon.getInstance(this).setBiguanidesState(0);
        }
        updateUserData();
    }

    @OnClick(R.id.glucosedasesSwitch)
    public void glucosedasesButtonClick() {
        if (AppCommon.getInstance(this).getGlucosedesesSate() == 0) {
            glucosedasesSwitchButton.setSelected(true);
            noMedicationSwitchButton.setSelected(false);
            AppCommon.getInstance(this).setMedicineState(0);
            AppCommon.getInstance(this).setGlucosedasesState(1);
        } else {
            glucosedasesSwitchButton.setSelected(false);
            AppCommon.getInstance(this).setGlucosedasesState(0);
        }
        updateUserData();
    }
}
