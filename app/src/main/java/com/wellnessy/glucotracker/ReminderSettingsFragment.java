package com.wellnessy.glucotracker;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.HealthReminderAdapter;
import CustomControl.DividerItemDecoration;
import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.HealthReminderResponse;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ReminderSettingsFragment extends Fragment {

    @BindView(R.id.reminderRecyclerview)
    RecyclerView healthReminderRecyclerView;
    ArrayList<HealthReminderResponse> healthReminderResponseArrayList;
    HealthReminderAdapter healthReminderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.health_reminder_recycleview, container, false);
        ButterKnife.bind(this, rootView);
        healthReminderResponseArrayList = new ArrayList<>();
        Cursor c = DbHelper.getInstance(getActivity()).getEditReminderRecord(AppCommon.getInstance(getActivity()).getUserId());
        if (!(c.getCount() == 0)) {
            try {
                while (c.moveToNext()) {
                    String reminderName = c.getString(c.getColumnIndex(DbHelper.COLUMN_REMINDER));
                    String time = c.getString(c.getColumnIndex(DbHelper.COLUMN_TIME));
                    String reminderType = c.getString(c.getColumnIndex(DbHelper.COLUMN_REMINDER_TYPE));
                    String switchState = c.getString(c.getColumnIndex(DbHelper.COLUMN_SWITCH_BUTTON_STATE));
                    String hour = c.getString(c.getColumnIndex(DbHelper.COLUMN_HOUR));
                    String min = c.getString(c.getColumnIndex(DbHelper.COLUMN_MINUTE));
                    healthReminderResponseArrayList.add(c.getPosition(), new HealthReminderResponse(Integer.parseInt(min), time, reminderType, switchState, Integer.parseInt(hour), reminderName));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                c.close();
            }
        } else {
            AppCommon.showDialog(getActivity(), getResources().getString(R.string.noRecordFound));
        }
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        healthReminderRecyclerView.setLayoutManager(layout);
        healthReminderAdapter = new HealthReminderAdapter(healthReminderResponseArrayList, getActivity());
        healthReminderRecyclerView.setAdapter(healthReminderAdapter);
        healthReminderAdapter.notifyDataSetChanged();
        healthReminderRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                int index = Integer.parseInt(bundle.getString(getResources().getString(R.string.index)));
                String time = bundle.getString(getResources().getString(R.string.timeText));
                String reminderName = bundle.getString(getResources().getString(R.string.reminderName));
                String reminderType = bundle.getString(getResources().getString(R.string.reminderType));
                int hours = bundle.getInt(getResources().getString(R.string.hours));
                int min = bundle.getInt(getResources().getString(R.string.minutes));
                HealthReminderResponse response = healthReminderResponseArrayList.get(index);
                response.setTimeSet(time);
                response.setReminderText(reminderName);
                response.setReminderType(reminderType);
                response.setHours(hours);
                response.setMin(min);
                healthReminderAdapter.setAlarm(index);
                healthReminderAdapter.notifyItemChanged(index);
            }
        }
    }

    public void reminderBackClick() {
        Fragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.reminderLayout, profileFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("ReminderSettingsFragment");
        transaction.commit();
    }
}
