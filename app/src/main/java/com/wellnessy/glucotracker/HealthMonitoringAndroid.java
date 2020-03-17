package com.wellnessy.glucotracker;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class HealthMonitoringAndroid extends Application {

    public void onCreate() {
        super.onCreate();
        Fresco.initialize(HealthMonitoringAndroid.this);
    }

}
