package CustomControl;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.R;

import java.util.ArrayList;

import Adapter.HistoryListAdapter;
import Database.DbHelper;
import Response.HistorySelectedDateResponse;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryListCustomView extends LinearLayout {
    @Nullable
    @BindView(R.id.historyListRecyclerView)
    RecyclerView mHistoryListRecyclerView;

    ArrayList<HistorySelectedDateResponse> mArraylist = new ArrayList<>();
    HistoryListAdapter mHistoryListAdapter;


    public HistoryListCustomView(Context context) {
        super(context);
    }

    public HistoryListCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HistoryListCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void intializeViews(Context context, ArrayList<HistorySelectedDateResponse> mDateResponsesList, String selectedUnit, String mGetSelectedColumn) {

        this.mArraylist = mDateResponsesList;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.history_list_custom_layout, this);
        ButterKnife.bind(this, v);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        assert mHistoryListRecyclerView != null;
        mHistoryListRecyclerView.setLayoutManager(linearLayoutManager);
        if (mGetSelectedColumn != null && (mGetSelectedColumn.equals(DbHelper.COLUMN_TEST_GLUCOSEVALUE) || mGetSelectedColumn.equals(DbHelper.COLUMN_TEST_HEAMOGLOBIN))) {
            mHistoryListAdapter = new HistoryListAdapter(getContext(), mArraylist, mGetSelectedColumn, selectedUnit);
        } else {
            mHistoryListAdapter = new HistoryListAdapter(getContext(), mArraylist, mGetSelectedColumn, "");
        }
        mHistoryListRecyclerView.setAdapter(mHistoryListAdapter);
        mHistoryListAdapter.notifyDataSetChanged();
    }


    public void notifyAdapterWithUnitAndData(String unit, String item, ArrayList<HistorySelectedDateResponse> mDateResponsesList) {
        this.mArraylist = mDateResponsesList;
        mHistoryListAdapter.setSelectedItem(item);
        mHistoryListAdapter.setSelectedUnit(unit);
        mHistoryListAdapter.notifyDataSetChanged();
    }

}
