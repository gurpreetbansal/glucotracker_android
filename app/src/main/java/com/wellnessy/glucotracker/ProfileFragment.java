package com.wellnessy.glucotracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    @BindView(R.id.personProfileImage)
    CircleImageView mPersonProfileImage;

    @BindView(R.id.weightTextView)
    TextViewGothamBook weightTextView;

    @BindView(R.id.heightTextView)
    TextViewGothamBook heightTextView;

    @BindView(R.id.ageTextView)
    TextViewGothamBook ageTextView;

    @BindView(R.id.personNameText)
    TextViewGothamBook personNametextView;

    @BindView(R.id.profileFragmentLayout)
    RelativeLayout mProfileRelativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        ButterKnife.bind(this, rootView);
        mProfileRelativeLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        Cursor rs = DbHelper.getInstance(getActivity()).getUserData(AppCommon.getInstance(getActivity()).getUserId());
        rs.moveToFirst();

        String name = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_NAME));
        String weight = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_WEIGHT));
        String height = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_hEIGHT));
        String age = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_AGE));
        String imagePath = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_IMAGE));
        if (!rs.isClosed()) {
            rs.close();
        }

        mPersonProfileImage.setBorderColor(getResources().getColor(R.color.blue));
        if (imagePath != null && !imagePath.equals("")) {
            mPersonProfileImage.setImageDrawable(Drawable.createFromPath(imagePath));
        } else {
            mPersonProfileImage.setBorderColor(getResources().getColor(R.color.blue));
            mPersonProfileImage.setImageResource(R.drawable.person_profile_image);

        }
        mPersonProfileImage.setBorderWidth(2);

       personNametextView.setText(AppCommon.getInstance(getActivity()).getUpdateUsername());

        if (AppCommon.getInstance(getActivity()).getHeightUnit().equals(getResources().getString(R.string.cmText))) {
            heightTextView.setText(height + "" + getResources().getString(R.string.centimeter));
        } else if (AppCommon.getInstance(getActivity()).getHeightUnit().equals(getResources().getString(R.string.inText))) {
            double heightInch = AppCommon.getCMToInch(Double.parseDouble(height));
            heightTextView.setText(String.format("%.1f", heightInch) + "" + getResources().getString(R.string.inText));
        }
        if (AppCommon.getInstance(getActivity()).getWeightUnit().equals(getResources().getString(R.string.kgText))) {
            weightTextView.setText(weight + "" + getResources().getString(R.string.kgText));
        } else if (AppCommon.getInstance(getActivity()).getWeightUnit().equals(getResources().getString(R.string.lbText))) {
            double weightLb = AppCommon.getKiloToLB(Double.parseDouble(weight));
            weightTextView.setText(String.format("%.1f", weightLb) + "" + getResources().getString(R.string.lbText));
        }
        ageTextView.setText(age + "" + getResources().getString(R.string.yearsText));
        return rootView;
    }

    @OnClick(R.id.guideLayout)
    public void guideClick() {
        Intent intent = new Intent(getActivity(), GuideActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.settingsLayout)
    public void settingClick() {
        Intent intent = new Intent(getActivity(), MeasurementSettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.testLayout)
    public void testClick() {
        Intent intent = new Intent(getActivity(), TestForNormalActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.historyLayout)
    public void historyClick() {
        Intent intent = new Intent(getActivity(), HistoryActivity.class);
        startActivity(intent);
    }

}
