package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.ManageAccountsFragment;
import com.wellnessy.glucotracker.R;

import java.util.ArrayList;

import CustomControl.TextViewGothamBook;
import Response.SelectUserResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageAccountsAdapter extends RecyclerView.Adapter<ManageAccountsAdapter.DataHolder> {
    Activity mActivity;
    Fragment fragment;
    ArrayList<SelectUserResponse> selectUserResponseArrayList;

    public ManageAccountsAdapter(Activity mActivity, ArrayList<SelectUserResponse> selectUserResponseArrayList, Fragment fragment) {
        this.mActivity = mActivity;
        this.selectUserResponseArrayList = selectUserResponseArrayList;
        this.fragment = fragment;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_accounts_row, parent, false);
        ManageAccountsAdapter.DataHolder dataHolder = new ManageAccountsAdapter.DataHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        SelectUserResponse response = selectUserResponseArrayList.get(position);
        holder.usernameTextView.setText(response.getUserName());
        holder.parentViewClick.setTag(Integer.toString(position));
        holder.selectUserCheckBox.setSelected(response.isSlected());
    }

    @Override
    public int getItemCount() {
        return selectUserResponseArrayList.size();
    }


    public class DataHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userNameTextView)
        TextViewGothamBook usernameTextView;

        @BindView(R.id.selectUserCheckBox)
        ImageView selectUserCheckBox;

        @BindView(R.id.parentViewClick)
        RelativeLayout parentViewClick;

        public DataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.parentViewClick)
        public void onSelectUserChckBoxClick(View v) {
            int position = Integer.parseInt(v.getTag().toString());
            SelectUserResponse response = selectUserResponseArrayList.get(position);
            if (response.isSlected()) {
                response.setSlected(false);
                ((ManageAccountsFragment) fragment).removeData(position);
            } else {
                response.setSlected(true);
                ((ManageAccountsFragment) fragment).addData(position);
            }
            notifyDataSetChanged();
        }
    }
}
