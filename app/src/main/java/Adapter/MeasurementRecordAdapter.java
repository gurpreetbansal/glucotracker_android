package Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.R;
import com.wellnessy.glucotracker.ResultActivity;

import java.util.ArrayList;

import CustomControl.TextViewGothamBook;
import CustomControl.TextViewGothamMedium;
import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.HistorySelectedDateResponse;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MeasurementRecordAdapter extends RecyclerView.Adapter<MeasurementRecordAdapter.DataHolder> {
    ArrayList<HistorySelectedDateResponse> measurementRecordResponseArrayList;
    Activity mContext;
    DbHelper mDbHelper;
    AppCommon mAppCommon;

    public MeasurementRecordAdapter(Activity context, ArrayList<HistorySelectedDateResponse> arrayListFromCursor, DbHelper mDbHelper, AppCommon mAppCommon) {
        this.mContext = context;
        this.measurementRecordResponseArrayList = arrayListFromCursor;
        this.mDbHelper = mDbHelper;
        this.mAppCommon = mAppCommon;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.measurement_record_row, parent, false);
        DataHolder dataHolder = new DataHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {

        HistorySelectedDateResponse mHistorySelectedDateResponse = measurementRecordResponseArrayList.get(position);
        if (mHistorySelectedDateResponse.getmDrawableSelected().equals(mContext.getResources().getString(R.string.blueColor))) {
            holder.circleImageView.setImageResource(R.drawable.circle_blue);
        } else if (mHistorySelectedDateResponse.getmDrawableSelected().equals(mContext.getResources().getString(R.string.yellowColor))) {
            holder.circleImageView.setImageResource(R.drawable.circle_yellow);
        }
        if (mHistorySelectedDateResponse.getmDrawableSelected().equals(mContext.getResources().getString(R.string.greenColor))) {
            holder.circleImageView.setImageResource(R.drawable.circle_green);
        } else if (mHistorySelectedDateResponse.getmDrawableSelected().equals(mContext.getResources().getString(R.string.redColor))) {
            holder.circleImageView.setImageResource(R.drawable.circle_red);
        }
        if (mHistorySelectedDateResponse.getmDietValue().equals("0")) {
            holder.mDietValueTextView.setText(mContext.getResources().getString(R.string.morningFastingText));
        } else {
            holder.mDietValueTextView.setText(mContext.getResources().getString(R.string.recordData));
        }
        if (AppCommon.getInstance(mContext).getGlucoseUnit().equals(mContext.getResources().getString(R.string.mMolText))) {
            String values = mHistorySelectedDateResponse.getmGlucoseValue();
            float doubleValues = Float.parseFloat(values);
            doubleValues = (doubleValues / 10.0f);
            String mmOlVaues = String.format("%.1f", doubleValues);
            holder.mGlucoseValueText.setText(mmOlVaues);
            holder.mUnitText.setText(mContext.getResources().getString(R.string.mMolText));

        } else {
            String values = mHistorySelectedDateResponse.getmGlucoseValue();
            float doubleValues = Float.parseFloat(values);
            doubleValues = (doubleValues / 10.0f);
            float mMolValues = AppCommon.getInstance(mContext).getMgDlFrommMol(doubleValues);
            String mMolStringValues = String.format("%.1f", mMolValues);
            holder.mGlucoseValueText.setText(String.valueOf(mMolStringValues));

        }
        if (mHistorySelectedDateResponse.getmMedicineValue().equals("1")) {
            holder.mMedicationRecord.setText(mContext.getResources().getString(R.string.noMedicationText));
        } else {
            holder.mMedicationRecord.setText(mContext.getResources().getString(R.string.medicationText));
        }

        holder.mDateTextView.setText(mHistorySelectedDateResponse.getmDate() + " " + mHistorySelectedDateResponse.getmTime());
    }

    @Override
    public int getItemCount() {
        return measurementRecordResponseArrayList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.circleImage)
        ImageView circleImageView;

        @BindView(R.id.imageText)
        TextViewGothamMedium mGlucoseValueText;

        @BindView(R.id.date)
        TextViewGothamBook mDateTextView;

        @BindView(R.id.recordData)
        TextViewGothamBook mDietValueTextView;

        @BindView(R.id.deleteRecord)
        ImageView mDeleteRecord;

        @BindView(R.id.measurementRecordRow)
        RelativeLayout mMeasurementRecordRow;

        @BindView(R.id.unitText)
        TextViewGothamBook mUnitText;

        @BindView(R.id.medicationRecord)
        TextViewGothamBook mMedicationRecord;

        public DataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mMeasurementRecordRow.setOnClickListener(this);
            mDeleteRecord.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.measurementRecordRow:
                    HistorySelectedDateResponse mHistorySelectedDateResponse = measurementRecordResponseArrayList.get(getPosition());
                    Intent intent = new Intent(mContext, ResultActivity.class);
                    intent.putExtra(mContext.getResources().getString(R.string.responseText), mHistorySelectedDateResponse);
                    mContext.startActivity(intent);
                    break;

                case R.id.deleteRecord:
                    final int id = measurementRecordResponseArrayList.get(getPosition()).getId();
                    AlertDialog.Builder mABuilder = new AlertDialog.Builder(mContext);
                    mABuilder.setTitle(mContext.getResources().getString(R.string.sureDelete));
                    mABuilder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    mABuilder.setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mDbHelper.deleteTestDetails(id, AppCommon.getInstance(mContext).getUserId());
                            measurementRecordResponseArrayList.remove(getPosition());
                            notifyDataSetChanged();
                        }
                    });
                    mABuilder.show();
                    break;
            }
        }
    }
}
