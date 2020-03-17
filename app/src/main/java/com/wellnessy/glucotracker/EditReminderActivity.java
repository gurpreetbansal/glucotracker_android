package com.wellnessy.glucotracker;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import CustomControl.EditTextGothamBook;
import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditReminderActivity extends Activity {

    @BindView(R.id.enterReminderEditView)
    EditTextGothamBook reminderEditView;

    @BindView(R.id.enterTimeTextView)
    TextViewGothamBook timeTextView;

    @BindView(R.id.measureCheckBox)
    ImageView measureCheckboxImage;

    @BindView(R.id.medicationCheckBox)
    ImageView medicationCheckboxImage;

    @BindView(R.id.measureTextView)
    TextViewGothamBook measureTextView;

    @BindView(R.id.medicationTextView)
    TextViewGothamBook medicationTextView;

    long timeInMills;

    String position;
    int selectedHour;
    int selectedMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_reminder_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        position = intent.getStringExtra(getResources().getString(R.string.index));
        reminderEditView.setText(intent.getStringExtra(getResources().getString(R.string.reminderName)));
        timeTextView.setText(intent.getStringExtra(getResources().getString(R.string.reminderTime)));
    }

    @OnClick(R.id.measureCheckBox)
    public void setMeasureCheckboxImage() {
        if(!measureCheckboxImage.isSelected()) {
            measureCheckboxImage.setSelected(true);
        }else
            measureCheckboxImage.setSelected(false);
    }

    @OnClick(R.id.medicationCheckBox)
    public void setMedicationCheckboxImage() {
        if(!medicationCheckboxImage.isSelected()) {
            medicationCheckboxImage.setSelected(true);
        }else {
            medicationCheckboxImage.setSelected(false);
        }
    }

    @OnClick(R.id.backIcon)
    public void backClick() {
        this.finish();
    }

    @OnClick(R.id.timeLayout)
    public void setTimeRelativeLayout() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(EditReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeTextView.setText(setTime(selectedHour, selectedMinute));
            }
        }, hour, minute, true);
        mTimePicker.setTitle(getResources().getString(R.string.selectTime));
        mTimePicker.show();
    }

    public String setTime(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        selectedHour = hour;
        selectedMin = minute;
        timeInMills = c.getTimeInMillis();

        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        String formatted = format1.format(c.getTime());
        return formatted;
    }

    public String getSelectedType() {
        String mSelectedColumnName = "";
        if (measureCheckboxImage.isSelected()) {
            mSelectedColumnName = measureTextView.getText().toString();
            if(medicationCheckboxImage.isSelected())
            {
                mSelectedColumnName = mSelectedColumnName + " and " + medicationTextView.getText().toString();
            }
            AppCommon.getInstance(this).setRemindText(mSelectedColumnName);
        } else if (medicationCheckboxImage.isSelected()) {
            mSelectedColumnName = medicationTextView.getText().toString();
            if(measureCheckboxImage.isSelected()){
                mSelectedColumnName=mSelectedColumnName + " and "+ measureTextView.getText().toString();
            }
            AppCommon.getInstance(this).setRemindText(mSelectedColumnName);
        }
        else{
            AppCommon.getInstance(this).setRemindText("");
        }
        return mSelectedColumnName;
    }

    @OnClick(R.id.saveButton)
    public void saveButtonClick() {
        String time = timeTextView.getText().toString();
        String reminderName = reminderEditView.getText().toString();
       DbHelper.getInstance(this).updateEditRemindertDetails(AppCommon.getInstance(this).getUserId(), reminderName, time, getSelectedType(), String.valueOf(selectedHour), String.valueOf(selectedMin), Integer.parseInt(position));
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.index), getIntent().getExtras().getString(getResources().getString(R.string.index)));
        intent.putExtra(getResources().getString(R.string.timeText), time);
        intent.putExtra(getResources().getString(R.string.hours), selectedHour);
        intent.putExtra(getResources().getString(R.string.minutes), selectedMin);
        intent.putExtra(getResources().getString(R.string.reminderName), reminderName);
        intent.putExtra(getResources().getString(R.string.reminderType), getSelectedType());
        setResult(Activity.RESULT_OK, intent);
        Toast.makeText(this,getResources().getString(R.string.saved),
                Toast.LENGTH_LONG).show();
        this.finish();
    }

}
