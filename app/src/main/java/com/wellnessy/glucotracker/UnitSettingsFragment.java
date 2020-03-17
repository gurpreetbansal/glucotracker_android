package com.wellnessy.glucotracker;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnitSettingsFragment extends Fragment {
    @BindView(R.id.cmTextView)
    TextView mCmTextView;

    @BindView(R.id.inTextView)
    TextView mInTextView;

    @BindView(R.id.kgTextView)
    TextView mKgTextView;

    @BindView(R.id.lbTextView)
    TextView mLbTextView;

    @BindView(R.id.mMolTextView)
    TextView mMolTextView;

    @BindView(R.id.mgDlTextView)
    TextView mMgDlTextVIew;

    @BindView(R.id.gLTextView)
    TextView mGlTextView;

    @BindView(R.id.gDlTextView)
    TextView mGdlTextView;

    AppCommon mAppCommon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.unit_settings, container, false);
        ButterKnife.bind(this, rootView);
        setButtonState();
        mAppCommon = AppCommon.getInstance(getActivity());
        return rootView;
    }

    public void setButtonState() {

        String heightState = AppCommon.getInstance(getActivity()).getHeightUnit();
        String weightState = AppCommon.getInstance(getActivity()).getWeightUnit();
        String glucoseState = AppCommon.getInstance(getActivity()).getGlucoseUnit();
        String hemoglobinState = AppCommon.getInstance(getActivity()).getHemoglobinUnit();

        if (heightState.equals(getResources().getString(R.string.cmText))) {
            mCmTextView.setSelected(true);
            mCmTextView.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            mInTextView.setSelected(true);
            mInTextView.setTextColor(getResources().getColor(android.R.color.white));
        }
        if (weightState.equals(getResources().getString(R.string.kgText))) {
            mKgTextView.setSelected(true);
            mKgTextView.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            mLbTextView.setSelected(true);
            mLbTextView.setTextColor(getResources().getColor(android.R.color.white));
        }
        if (glucoseState.equals(getResources().getString(R.string.mMolText))) {
            mMolTextView.setSelected(true);
            mMolTextView.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            mMgDlTextVIew.setSelected(true);
            mMgDlTextVIew.setTextColor(getResources().getColor(android.R.color.white));
        }
        if (hemoglobinState.equals(getResources().getString(R.string.g))) {
            mGlTextView.setSelected(true);
            mGlTextView.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            mGdlTextView.setSelected(true);
            mGdlTextView.setTextColor(getResources().getColor(android.R.color.white));
        }
    }


    @OnClick(R.id.cmTextView)
    public void setmcmTextView() {
        if (!mCmTextView.isSelected()) {
            mCmTextView.setSelected(true);
            mInTextView.setSelected(false);
            mCmTextView.setTextColor(getResources().getColor(android.R.color.white));
            mInTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }


    @OnClick(R.id.inTextView)
    public void setminTextView() {
        if (!mInTextView.isSelected()) {
            mInTextView.setSelected(true);
            mCmTextView.setSelected(false);
            mInTextView.setTextColor(getResources().getColor(android.R.color.white));
            mCmTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    @OnClick(R.id.kgTextView)
    public void setmkgTextView() {
        if (!mKgTextView.isSelected()) {
            mKgTextView.setSelected(true);
            mLbTextView.setSelected(false);
            mKgTextView.setTextColor(getResources().getColor(android.R.color.white));
            mLbTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    @OnClick(R.id.lbTextView)
    public void setmlbTextView() {
        if (!mLbTextView.isSelected()) {
            mLbTextView.setSelected(true);
            mKgTextView.setSelected(false);
            mLbTextView.setTextColor(getResources().getColor(android.R.color.white));
            mKgTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    @OnClick(R.id.mMolTextView)
    public void mMolTextView() {
        if (!mMolTextView.isSelected()) {
            mMolTextView.setSelected(true);
            mMgDlTextVIew.setSelected(false);
            mMolTextView.setTextColor(getResources().getColor(android.R.color.white));
            mMgDlTextVIew.setTextColor(getResources().getColor(android.R.color.black));
        }
    }


    @OnClick(R.id.mgDlTextView)
    public void setmgDlTextView() {
        if (!mMgDlTextVIew.isSelected()) {
            mMgDlTextVIew.setSelected(true);
            mMolTextView.setSelected(false);
            mMgDlTextVIew.setTextColor(getResources().getColor(android.R.color.white));
            mMolTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    @OnClick(R.id.gLTextView)
    public void setmgLTextView() {
        if (!mGlTextView.isSelected()) {
            mGlTextView.setSelected(true);
            mGdlTextView.setSelected(false);
            mGlTextView.setTextColor(getResources().getColor(android.R.color.white));
            mGdlTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    @OnClick(R.id.gDlTextView)
    public void setmmgDlTextView() {
        if (!mGdlTextView.isSelected()) {
            mGdlTextView.setSelected(true);
            mGlTextView.setSelected(false);
            mGdlTextView.setTextColor(getResources().getColor(android.R.color.white));
            mGlTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    @OnClick(R.id.saveButton)
    public void setmSaveButton() {

        String mHeightUnitType;
        String mWeightUnitType;
        String mGlucoseUnitType;
        String mHemoGlobinUnitType;

        if (mCmTextView.isSelected()) {
            mHeightUnitType = mCmTextView.getText().toString();
        } else {
            mHeightUnitType = mInTextView.getText().toString();
        }
        if (mKgTextView.isSelected()) {
            mWeightUnitType = mKgTextView.getText().toString();
        } else {
            mWeightUnitType = mLbTextView.getText().toString();
        }
        if (mMolTextView.isSelected()) {
            mGlucoseUnitType = mMolTextView.getText().toString();
        } else {
            mGlucoseUnitType = mMgDlTextVIew.getText().toString();
        }
        if (mGlTextView.isSelected()) {
            mHemoGlobinUnitType = mGlTextView.getText().toString();
        } else {
            mHemoGlobinUnitType = mGdlTextView.getText().toString();
        }
        if (AppCommon.getInstance(getActivity()).saveUnitTypesIntoSharedPreferences(mHeightUnitType, mWeightUnitType, mGlucoseUnitType, mHemoGlobinUnitType).equals("SUCCESS")) {
        }
        Toast.makeText(getActivity(), getResources().getString(R.string.saved),
                Toast.LENGTH_LONG).show();
    }

    public void unitSettingBackButton() {
        Fragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.unitSettingLayout, profileFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("UnitSettingsFragment");
        transaction.commit();
    }
}
