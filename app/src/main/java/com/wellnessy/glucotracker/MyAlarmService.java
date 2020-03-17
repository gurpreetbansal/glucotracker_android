package com.wellnessy.glucotracker;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;


import androidx.annotation.Nullable;

import java.util.List;

import Infrastructure.AppCommon;

public class MyAlarmService extends Service {
    NotificationManager mManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if (isApplicationSentToBackground(getApplicationContext())) {
            mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(this.getApplicationContext(), ExtendFragment.class);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            Resources res = this.getResources();
            Notification notification = new Notification.Builder(this)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(remindText())
                    .setSmallIcon(R.drawable.app_icon_transparent)
                    .setLargeIcon(BitmapFactory.decodeResource(res,R.drawable.app_icon))
                    .setContentIntent(pendingNotificationIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            mManager.notify(0, notification);
        } else {
            mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(this.getApplicationContext(), ExtendFragment.class);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            Resources res1 = this.getResources();
            Notification notification = new Notification.Builder(this)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(remindText())
                    .setSmallIcon(R.drawable.app_icon_transparent)
                    .setLargeIcon(BitmapFactory.decodeResource(res1,R.drawable.app_icon))
                    .setContentIntent(pendingNotificationIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            mManager.notify(0, notification);

            Intent push = new Intent();
            push.putExtra("default", remindText());
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            push.setClass(this, NotificationPopupActivity.class);
            this.startActivity(push);
        }
    }

    public String remindText() {
        String remind = "";
        if (AppCommon.getInstance(this).getRemindText().equals("")) {
            remind = getResources().getString(R.string.healthReminder);
        } else {
            remind = getResources().getString(R.string.yourReminderIsSet) + " " + AppCommon.getInstance(this).getRemindText();
        }
        return remind;
    }

    private boolean isApplicationSentToBackground(Context mcontext) {
        // TODO Auto-generated method stub
        ActivityManager am = (ActivityManager) mcontext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mcontext.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
