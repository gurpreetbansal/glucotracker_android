package Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.R;

import java.text.ParseException;
import java.util.ArrayList;

import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.HistorySelectedDateResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<HistorySelectedDateResponse> mListDataResponses;
    private DbHelper mDbHelper;
    String mGetSelectedItem;
    String selectedUnit;

    public HistoryListAdapter(Context activity, ArrayList<HistorySelectedDateResponse> allHistoryListData, String mGetSelectedColumn, String selectedUnit) {
        this.mContext = activity;
        this.mListDataResponses = allHistoryListData;
        this.mGetSelectedItem = mGetSelectedColumn;
        this.selectedUnit = selectedUnit;
        mDbHelper = new DbHelper(activity);
    }

    public void setSelectedUnit(String selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public void setSelectedItem(String selectedItem) {
        this.mGetSelectedItem = selectedItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_row_item, parent, false);
        ViewHolder dataHolder = new ViewHolder(view);
        return dataHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mListDataResponses.size() != 0) {
            if ((position % 2 == 0)) {
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightWhite));
            } else {
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
            HistorySelectedDateResponse mHistoryListResponse = mListDataResponses.get(position);
            String dateFormat = "yyyy-MM-dd";
            String inputDate = mHistoryListResponse.getmDate();
            try {
                String normalDate = AppCommon.getNormalDate(inputDate, dateFormat);
                normalDate = normalDate.replace('-', '/');
                holder.mDateTextView.setText(normalDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.mTimeValueTextView.setText(mHistoryListResponse.getmTime());
            holder.mResultValueTextView.setTypeface(null, Typeface.BOLD);
            if (mGetSelectedItem.equals(DbHelper.COLUMN_TEST_GLUCOSEVALUE)) {
                if (selectedUnit.equals(mContext.getResources().getString(R.string.mMolText))) {
                    String values = mHistoryListResponse.getmGlucoseValue();
                    float doubleValues = Float.parseFloat(values);
                    doubleValues = (doubleValues / 10.0f);
                    String mmOlVaues = String.format("%.1f", doubleValues);
                    holder.mResultValueTextView.setText(mmOlVaues);
                } else {
                    String values = mHistoryListResponse.getmGlucoseValue();
                    float doubleValues = Float.parseFloat(values);
                    doubleValues = (doubleValues / 10.0f);
                    float mMolValues = AppCommon.getInstance(mContext).getMgDlFrommMol(doubleValues);
                    String mMolStringValues = String.format("%.1f", mMolValues);
                    holder.mResultValueTextView.setText(String.valueOf(mMolStringValues));
                }
            } else if (mGetSelectedItem.equals(DbHelper.COLUMN_TEST_PULSE)) {
                holder.mResultValueTextView.setText(String.valueOf(mHistoryListResponse.getmPulseValue()));
            } else if (mGetSelectedItem.equals(DbHelper.COLUMN_TEST_BLOODFLOWSPEED)) {
                holder.mResultValueTextView.setText(String.valueOf(mHistoryListResponse.getmBloodFlowSpeed()));
            } else if (mGetSelectedItem.equals(DbHelper.COLUMN_TEST_HEAMOGLOBIN)) {
                if (selectedUnit.equals(mContext.getResources().getString(R.string.gPerdl))) {
                    String values = mHistoryListResponse.getmHeamoGlobinValue();
                    double doubleValues = Double.parseDouble(values);
                    doubleValues = (doubleValues / 100);
                    String mMolStringValues = String.format("%.2f",doubleValues);
                    holder.mResultValueTextView.setText(mMolStringValues);
                } else {
                    String values = mHistoryListResponse.getmHeamoGlobinValue();
                    double doubleValues = Double.parseDouble(values);
                    doubleValues = (doubleValues / 10);
                    String mdoubleStringValues = String.format("%.1f",doubleValues);
                    holder.mResultValueTextView.setText(mdoubleStringValues);
                }
            } else if (mGetSelectedItem.equals(DbHelper.COLUMN_TEST_OXYGENSATURATION)) {
                holder.mResultValueTextView.setText(String.valueOf(mHistoryListResponse.getmOxygenSaturationValue()));
            }
            holder.mResultValueTextView.setTextSize(16f);
             if (mHistoryListResponse.getmDietValue().equals("0")) {
                holder.mDietValueImage.setImageResource(R.drawable.no_food_history);
            } else {
                holder.mDietValueImage.setImageResource(R.drawable.two_hours_history);
            }
            if (mHistoryListResponse.getmMedicineValue().equals("1")) {
                holder.mMedicineValueImage.setImageResource(R.drawable.cross_history);
            } else {

                holder.mMedicineValueImage.setImageResource(R.drawable.tick_history);
            }

            holder.mDeleteValueImage.setImageResource(R.drawable.delete_history);
        }
    }


    @Override
    public int getItemCount() {
        return mListDataResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dateValueTextView)
        TextViewGothamBook mDateTextView;

        @BindView(R.id.timeValueTextView)
        TextViewGothamBook mTimeValueTextView;

        @BindView(R.id.resultValueTextView)
        TextViewGothamBook mResultValueTextView;

        @BindView(R.id.dietView)
        RelativeLayout mDietView;

        @BindView(R.id.dietValueImage)
        ImageView mDietValueImage;

        @BindView(R.id.medicineValueImage)
        ImageView mMedicineValueImage;

        @BindView(R.id.dietTextView)
        TextViewGothamBook mDietTextView;

        @BindView(R.id.deleteTextView)
        TextViewGothamBook mDeleteTextView;

        @BindView(R.id.deleteValueImage)
        ImageView mDeleteValueImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        @OnClick(R.id.deleteValueImage)
        public void mDeleteValueImage() {
           final int positionDeleteIcon = getPosition();
           final int id = mListDataResponses.get(positionDeleteIcon).getId();
            AlertDialog.Builder mABuilder = new AlertDialog.Builder(mContext);
            mABuilder.setTitle(mContext.getResources().getString(R.string.sureDelete));
            mABuilder.setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDbHelper.deleteTestDetails(id, AppCommon.getInstance(mContext).getUserId());
                    mListDataResponses.remove(positionDeleteIcon);
                    notifyDataSetChanged();
                }
            });
            mABuilder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            mABuilder.show();
        }
    }

}
