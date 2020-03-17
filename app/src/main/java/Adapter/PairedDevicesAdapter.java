package Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.R;

import java.util.ArrayList;

import CustomControl.TextViewGothamBook;
import Response.PairedDevicesResponse;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PairedDevicesAdapter extends RecyclerView.Adapter<PairedDevicesAdapter.ViewHolder> {
    private Context mContext;
    private FragmentManager mFragmentManager;
    ArrayList<PairedDevicesResponse> mListDataResponses;

    public PairedDevicesAdapter(Context mContext, FragmentManager mFragmentManager, ArrayList<PairedDevicesResponse> allPairDevicesRespnse) {
        this.mContext = mContext;
        this.mFragmentManager = mFragmentManager;
        this.mListDataResponses = allPairDevicesRespnse;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paired_devices_row_item, parent, false);
        ViewHolder dataHolder = new ViewHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PairedDevicesResponse mPairedDevicesResponse = mListDataResponses.get(position);
        holder.mPairedDeviceTextView.setText(mPairedDevicesResponse.getDeviceName());
    }

    @Override
    public int getItemCount() {
        return mListDataResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pairedDeviceName)
        TextViewGothamBook mPairedDeviceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
