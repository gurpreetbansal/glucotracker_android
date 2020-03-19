package com.wellnessy.glucotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.splunk.mint.Mint;

import Infrastructure.AppCommon;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        AppCommon appCommon = AppCommon.getInstance(this);
        Mint.initAndStartSession(this.getApplication(), "5afa5f4e");

        startTimer();
    }

    private void startTimer() {

        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(() -> startApp(), SPLASH_TIME_OUT);
    }

    private void startApp() {
        Intent i = new Intent(SplashActivity.this, StartScreenActivity.class);
        startActivity(i);
        finishAffinity();
    }
}

