package Adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.EditReminderActivity;
import com.wellnessy.glucotracker.MyReceiver;
import com.wellnessy.glucotracker.R;

import java.util.ArrayList;
import java.util.Calendar;

import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Response.HealthReminderResponse;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


public class HealthReminderAdapter extends RecyclerView.Adapter<HealthReminderAdapter.DataHolder> {
    ArrayList<HealthReminderResponse> healthReminderResponseArrayList;
    Activity context;
    private PendingIntent pendingIntent;
    AlarmManager alarmManager;
    String remind = "";

    public HealthReminderAdapter(ArrayList<HealthReminderResponse> data, Activity context) {
        this.healthReminderResponseArrayList = data;
        this.context = context;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_reminder_row, parent, false);
        DataHolder dataHolder = new DataHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        HealthReminderResponse healthReminderResponse = healthReminderResponseArrayList.get(position);
        holder.healthReminderTextView.setText(healthReminderResponse.getReminderText());
        holder.timeTextView.setText(healthReminderResponse.getTimeSet());

        if (healthReminderResponse.getSwitchButtonState().equals(context.getResources().getString(R.string.trueText))) {
            holder.switchButton.setSelected(true);
        } else {
            holder.switchButton.setSelected(false);
        }

        if (healthReminderResponse.getReminderType().equals(context.getResources().getString(R.string.measure))) {
            holder.measureTextView.setTextColor(context.getResources().getColor(R.color.red));

        } else {
            holder.measureTextView.setTextColor(context.getResources().getColor(R.color.black));
        }

        if (healthReminderResponse.getReminderType().equals(context.getResources().getString(R.string.medicationText))) {
            holder.medicationTextView.setTextColor(context.getResources().getColor(R.color.red));

        } else {
            holder.medicationTextView.setTextColor(context.getResources().getColor(R.color.black));
        }

        String remindText = context.getResources().getString(R.string.measure) + " and " + context.getResources().getString(R.string.medicationText);
        if (healthReminderResponse.getReminderType().equals(remindText)) {
            holder.measureTextView.setTextColor(context.getResources().getColor(R.color.red));
            holder.medicationTextView.setTextColor(context.getResources().getColor(R.color.red));
        }


        holder.parentLayout.setTag(Integer.toString(position));
        holder.switchButton.setTag(Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        return healthReminderResponseArrayList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.healthReminderText)
        TextViewGothamBook healthReminderTextView;

        @BindView(R.id.timeTextView)
        TextViewGothamBook timeTextView;

        @BindView(R.id.parentLayout)
        RelativeLayout parentLayout;

        @BindView(R.id.measureText)
        TextViewGothamBook measureTextView;

        @BindView(R.id.switchButton)
        ImageView switchButton;

        @BindView(R.id.medicationText)
        TextViewGothamBook medicationTextView;

        public DataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentLayout.setOnClickListener(this);
            switchButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = Integer.parseInt(view.getTag().toString());
            switch (view.getId()) {
                case R.id.parentLayout:
                    HealthReminderResponse reminderResponse = healthReminderResponseArrayList.get(position);
                    Intent intent = new Intent(context, EditReminderActivity.class);
                    intent.putExtra(context.getResources().getString(R.string.index), view.getTag().toString());
                    intent.putExtra(context.getResources().getString(R.string.reminderName), healthReminderTextView.getText().toString());
                    intent.putExtra(context.getResources().getString(R.string.reminderTime), timeTextView.getText().toString());
                    context.startActivityForResult(intent, 1);
                    break;
                case R.id.switchButton:
                    HealthReminderResponse mHealthReminderResponse = healthReminderResponseArrayList.get(position);
                    if (mHealthReminderResponse.getSwitchButtonState().equals(context.getResources().getString(R.string.falseText))) {
                        mHealthReminderResponse.setSwitchButtonState(context.getResources().getString(R.string.trueText));
                        registerAlarm(mHealthReminderResponse, position);
                    } else {
                        mHealthReminderResponse.setSwitchButtonState(context.getResources().getString(R.string.falseText));
                        unRegisterAlarm(mHealthReminderResponse, position);
                    }
                    break;
            }
        }
    }

    public void registerAlarm(HealthReminderResponse mHealthReminderResponse, int position) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, mHealthReminderResponse.getHours());
        c.set(Calendar.MINUTE, mHealthReminderResponse.getMin());
        c.set(Calendar.SECOND, 0);
        long timeInMills = c.getTimeInMillis();
        Intent myIntent = new Intent(context, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, position, myIntent, FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent);
        }
        notifyItemChanged(position);
        DbHelper.getInstance(context).updateSwitchButtonEditRemindertState(position, mHealthReminderResponse.getSwitchButtonState());
    }


    public void unRegisterAlarm(HealthReminderResponse mHealthReminderResponse, int position) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, position, myIntent, FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        notifyItemChanged(position);
        DbHelper.getInstance(context).updateSwitchButtonEditRemindertState(position, mHealthReminderResponse.getSwitchButtonState());
    }

    public void setAlarm(int position) {
        HealthReminderResponse reminderResponse = healthReminderResponseArrayList.get(position);
        if (reminderResponse.getSwitchButtonState().equals(context.getResources().getString(R.string.falseText))) {
            unRegisterAlarm(reminderResponse, position);
        } else {
            unRegisterAlarm(reminderResponse, position);
            registerAlarm(reminderResponse, position);
        }
    }

}
