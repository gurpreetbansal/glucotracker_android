package com.wellnessy.glucotracker;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


public class ExtendFragment extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);

        FragmentManager fragmentManager=getSupportFragmentManager();
        ReminderSettingsFragment reminderFragment= new ReminderSettingsFragment();
        fragmentManager.beginTransaction().add(R.id.frameLayout,reminderFragment).commit();

    }
}
