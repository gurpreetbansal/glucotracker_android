package com.wellnessy.glucotracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationPopupActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_popup);
        ButterKnife.bind(this);
        String mTextmessage = getIntent().getStringExtra("default");
        showDialog(this, mTextmessage);
    }
    public void showDialog(Activity mactivity,String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
        builder.setTitle(getResources().getString(R.string.reminder));
        builder.setMessage(title);
        builder.setCancelable(false);
        builder.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                NotificationPopupActivity.this.finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @OnClick(R.id.Popup_notification)
    protected void click(){
        NotificationPopupActivity.this.finish();
    }
}

