package com.wellnessy.glucotracker;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class Test2HoursActivity extends Activity {

    @BindView(R.id.smallSwitch)
    ImageView smallSwitchButton;

    @BindView(R.id.mediumSwitch)
    ImageView mediumSwitchButton;

    @BindView(R.id.largeSwitch)
    ImageView largeSwitchButton;

    @BindView(R.id.personProfileImage)
    CircleImageView mPersonProfileImage;

    @BindView(R.id.personNameText)
    TextViewGothamBook personNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postprandial_layout);
        ButterKnife.bind(this);
        smallSwitchButton.setSelected(true);
        AppCommon.getInstance(this).setMealSizes(1);
        Cursor rs = DbHelper.getInstance(this).getUserData(AppCommon.getInstance(this).getUserId());
        rs.moveToFirst();

        String name = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_NAME));
        String imagePath = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_IMAGE));
        mPersonProfileImage.setBorderColor(getResources().getColor(R.color.blue));
        if (imagePath != null && !imagePath.equals("")) {
            mPersonProfileImage.setImageDrawable(Drawable.createFromPath(imagePath));
        }
        mPersonProfileImage.setBorderWidth(2);
        personNameTextView.setText(AppCommon.getInstance(this).getUpdateUsername());

    }

    @OnClick(R.id.backIcon)
    public void backClkick(){
        this.finish();
    }

    @OnClick(R.id.smallSwitch)
    public void smallButtonClick(){
        smallSwitchButton.setSelected(true);
        mediumSwitchButton.setSelected(false);
        largeSwitchButton.setSelected(false);
        AppCommon.getInstance(this).setMealSizes(1);
    }

    @OnClick(R.id.mediumSwitch)
    public void mediumButtonClick(){
        smallSwitchButton.setSelected(false);
        mediumSwitchButton.setSelected(true);
        largeSwitchButton.setSelected(false);
        AppCommon.getInstance(this).setMealSizes(2);
    }

    @OnClick(R.id.largeSwitch)
    public void largeButtonClick(){
        smallSwitchButton.setSelected(false);
        mediumSwitchButton.setSelected(false);
        largeSwitchButton.setSelected(true);
        AppCommon.getInstance(this).setMealSizes(3);
    }

    @OnClick(R.id.nextButton)
    public void nextButtonClick(){
        this.finish();
    }
}
