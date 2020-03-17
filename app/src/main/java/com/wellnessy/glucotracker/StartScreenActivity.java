package com.wellnessy.glucotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import Infrastructure.AppCommon;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StartScreenActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.acceptButton)
    public void onAcceptButtonClick(View view) {
        if (AppCommon.getInstance(StartScreenActivity.this).isUserLogIn()) {
            Intent i = new Intent(StartScreenActivity.this, NavigationActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(StartScreenActivity.this, LoginActivity.class);
            startActivity(i);
        }
        this.finish();
    }

}
