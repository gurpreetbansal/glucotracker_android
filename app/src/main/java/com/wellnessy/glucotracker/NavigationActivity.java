package com.wellnessy.glucotracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.NavigationAdapter;
import CustomControl.DividerItemDecoration;
import CustomControl.TextViewGothamMedium;
import CustomControl.TextViewIconStyle;
import Infrastructure.AppCommon;
import Response.NavigationResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NavigationActivity extends AppCompatActivity {

    public boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.fragmentName)
    TextViewGothamMedium fragmentNameTextView;

    @BindView(R.id.recyclerView)
    RecyclerView navigationRecyclerView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @BindView(R.id.backIcon)
    TextViewIconStyle backButton;

    @BindView(R.id.menuIcon)
    TextViewIconStyle menuIcon;

    @BindView(R.id.shareImageView)
    ImageView shareImageView;


    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    String itemList[];

    public ArrayList<NavigationResponse> navigationResponseArrayList;
    public NavigationAdapter navigationAdapter;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        ButterKnife.bind(this);
        itemList = getResources().getStringArray(R.array.navigationItems);

        mTitle = mDrawerTitle = getTitle();
        navigationResponseArrayList = new ArrayList<NavigationResponse>();
        for (int i = 0; i < itemList.length; i++) {
            NavigationResponse data = new NavigationResponse(itemList[i]);
            navigationResponseArrayList.add(data);
        }
        LinearLayoutManager myLayout = new LinearLayoutManager(this);
        navigationRecyclerView.setLayoutManager(myLayout);
        navigationAdapter = new NavigationAdapter(navigationResponseArrayList, this);
        navigationRecyclerView.setAdapter(navigationAdapter);
        navigationRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
        seFragment(0);
    }

    @OnClick(R.id.menuIcon)
    public void setMenuIcon() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    public void setmTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
        getActionBar().setTitle(mTitle);
    }

    public void seFragment(int pos) {

        switch (pos) {
            case 0:
                fragmentNameTextView.setText(itemList[0]);
                fragment = new ProfileFragment();
                backButton.setVisibility(View.GONE);
                menuIcon.setVisibility(View.VISIBLE);
                shareImageView.setVisibility(View.GONE);

                break;
            case 1:
                fragmentNameTextView.setText(getResources().getString(R.string.bluetoothSearchText));
                fragment = new BluetoothFragment();
                backButton.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                shareImageView.setVisibility(View.GONE);
                break;
            case 2:
                fragmentNameTextView.setText(itemList[2]);
                fragment = new EditUserFragment();
                backButton.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                shareImageView.setVisibility(View.GONE);
                break;
            case 3:
                fragmentNameTextView.setText(itemList[3]);
                fragment = new UnitSettingsFragment();
                backButton.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                shareImageView.setVisibility(View.GONE);
                break;
            case 4:
                fragmentNameTextView.setText(getResources().getString(R.string.healthReminder));
                fragment = new ReminderSettingsFragment();
                backButton.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                shareImageView.setVisibility(View.GONE);
                break;
            case 5:
                fragmentNameTextView.setText(itemList[5]);
                fragment = new MeasurementRecordFragment();
                backButton.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                shareImageView.setVisibility(View.VISIBLE);
                break;
            case 6:
                fragmentNameTextView.setText(getResources().getString(R.string.healthReference1));
                fragment = new HealthReferenceFragment();
                backButton.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                shareImageView.setVisibility(View.GONE);
                break;

            case 7:
                fragmentNameTextView.setText(itemList[7]);
                fragment = new ManageAccountsFragment();
                backButton.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                shareImageView.setVisibility(View.GONE);
                break;


            case 8:
                fragmentNameTextView.setText(itemList[8]);
                fragment = new SwitchUserFragment();
                backButton.setVisibility(View.VISIBLE);
                menuIcon.setVisibility(View.GONE);
                shareImageView.setVisibility(View.GONE);
                break;

            case 9:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.sureWantToExit))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yesText), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AppCommon.getInstance(NavigationActivity.this).setIsUserLogIn(false);
                                AppCommon.getInstance(NavigationActivity.this).setFilePath("");
                                Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.noText), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;

            default:
                fragment = new ReminderSettingsFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            Log.e("error", "error in fragments");
        }


    }

    public void changeTitle(String title) {
        fragmentNameTextView.setText(title);
        backButton.setVisibility(View.GONE);
        menuIcon.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragment instanceof ReminderSettingsFragment) {
            ((ReminderSettingsFragment) fragment).onActivityResult(requestCode, resultCode, data);
        } else if (fragment instanceof BluetoothFragment) {
            ((BluetoothFragment) fragment).onActivityResult(requestCode, resultCode, data);
        }
    }

    public void updateBluetoothFragment() {
        if (fragment instanceof BluetoothFragment) {
            ((BluetoothFragment) fragment).updateBluetoothFragment();
        }
    }

    @OnClick(R.id.shareImageView)
    public void shareImageClick() {
        if (fragment instanceof MeasurementRecordFragment) {
            ((MeasurementRecordFragment) fragment).shareButton();
        }
    }

    @OnClick(R.id.backIcon)
    public void unitSettingBAckButton() {
        if (fragment instanceof EditUserFragment) {
            ((EditUserFragment) fragment).editUserBackClick();
            fragmentNameTextView.setText(itemList[0]);
            backButton.setVisibility(View.GONE);
            shareImageView.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);

        }
        if (fragment instanceof UnitSettingsFragment) {
            ((UnitSettingsFragment) fragment).unitSettingBackButton();
            fragmentNameTextView.setText(itemList[0]);
            backButton.setVisibility(View.GONE);
            shareImageView.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);
        }

        if (fragment instanceof BluetoothFragment) {
            ((BluetoothFragment) fragment).backFragment();
            fragmentNameTextView.setText(itemList[0]);
            backButton.setVisibility(View.GONE);
            shareImageView.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);
        }
        if (fragment instanceof MeasurementRecordFragment) {
            ((MeasurementRecordFragment) fragment).measurementBackClick();
            fragmentNameTextView.setText(itemList[0]);
            backButton.setVisibility(View.GONE);
            shareImageView.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);
        }
        if (fragment instanceof ReminderSettingsFragment) {
            ((ReminderSettingsFragment) fragment).reminderBackClick();
            fragmentNameTextView.setText(itemList[0]);
            backButton.setVisibility(View.GONE);
            shareImageView.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);
        }
        if (fragment instanceof SwitchUserFragment) {
            ((SwitchUserFragment) fragment).switchBAckClick();
            fragmentNameTextView.setText(itemList[0]);
            shareImageView.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);
        }
        if (fragment instanceof HealthReferenceFragment) {
            seFragment(0);
            fragmentNameTextView.setText(itemList[0]);
            shareImageView.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);
        }
        if (fragment instanceof ManageAccountsFragment) {
            seFragment(0);
            fragmentNameTextView.setText(itemList[0]);
            shareImageView.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            menuIcon.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    ((BluetoothFragment) fragment).blueToothSearchDevice();
                }
                break;
            case AppCommon.CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    ((EditUserFragment) fragment).startCameraIntent();
                }
                break;
            case AppCommon.GALLARY_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    ((EditUserFragment) fragment).startGalleryIntent();
                }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        doubleBackToExitPressedOnce = true;

        Toast.makeText(this, R.string.back_msg, Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}


