package com.wellnessy.glucotracker;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PowerManagementActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_management);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.backIcon)
    public void backClick(){
        this.finish();
    }
}
