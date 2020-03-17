package com.wellnessy.glucotracker;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import java.util.List;

import Infrastructure.AppCommon;

public class MyReceiver extends BroadcastReceiver {
    NotificationManager mManager;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Intent service1 = new Intent(context, MyAlarmService.class);
        //context.startService(service1);
        // Log.i("receiver","reciver");
        this.context = context;
        if (isApplicationSentToBackground(context)) {
            mManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(context, ExtendFragment.class);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(remindText())
                    .setSmallIcon(R.drawable.app_icon_transparent)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon))
                    .setContentIntent(pendingNotificationIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            mManager.notify(0, notification);
        }else{
//            mManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//            Intent intent1 = new Intent(context, ExtendFragment.class);
//            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Notification notification = new Notification.Builder(context)
//                    .setContentTitle(context.getString(R.string.app_name))
//                    //  .setContentText(remindText())
//                       .setSmallIcon(R.drawable.app_icon_transparent)
//                     .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.app_icon))
//
//                    .setContentIntent(pendingNotificationIntent)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .build();
//            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//            mManager.notify(0, notification);

            Intent push = new Intent();
            push.putExtra("default", remindText());
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            push.setClass(context, NotificationPopupActivity.class);
            context.startActivity(push);
        }
    }

    public String remindText() {
        String remind = "";
        if (AppCommon.getInstance( this.context).getRemindText().equals("")) {
            remind =  this.context.getResources().getString(R.string.healthReminder);
        } else {
            remind =  this.context.getResources().getString(R.string.yourReminderIsSet) + " " + AppCommon.getInstance( this.context).getRemindText();
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


}
