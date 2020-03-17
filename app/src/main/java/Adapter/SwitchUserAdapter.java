package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.NavigationActivity;
import com.wellnessy.glucotracker.ProfileFragment;
import com.wellnessy.glucotracker.R;
import com.wellnessy.glucotracker.UserName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.SwitchUserResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwitchUserAdapter extends RecyclerView.Adapter<SwitchUserAdapter.DataHolder> {
    ArrayList<SwitchUserResponse> switchUserResponseArrayList;
    Activity mActivity;
    FragmentManager mFragmentManager;
    DbHelper mDbHelper;


    public SwitchUserAdapter(ArrayList<SwitchUserResponse> data, FragmentManager mFragmentManager, Activity mActivity, DbHelper mDbHelper) {
        this.switchUserResponseArrayList = data;
        this.mFragmentManager = mFragmentManager;
        this.mActivity = mActivity;
        this.mDbHelper = mDbHelper;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_user_row, parent, false);
        DataHolder dataHolder = new DataHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        SwitchUserResponse switchUserResponse = switchUserResponseArrayList.get(position);
        holder.usernameTextView.setText(getUserNameForKey(switchUserResponse.getName()));
        holder.selectUserLayout.setTag(Integer.toString(position));
    }

    public String getUserNameForKey(String key) {
        String userName = key;
        String fetchList = AppCommon.getInstance(mActivity).getUsernameArrayList();
        Gson gson = new Gson();
        ArrayList<UserName> userNameArrayList = gson.fromJson(fetchList, new TypeToken<ArrayList<UserName>>() {
        }.getType());
        for (UserName userNameObj : userNameArrayList) {
            if (userNameObj.getKey().equals(key)) {
                userName = userNameObj.getValue();
                break;
            }
        }
        return userName;
    }

    @Override
    public int getItemCount() {
        return switchUserResponseArrayList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.selectUserName)
        TextViewGothamBook usernameTextView;

        @BindView(R.id.selectUserLayout)
        LinearLayout selectUserLayout;

        public DataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //itemView.setOnClickListener(this);
        }

        @OnClick(R.id.selectUserLayout)
        public void itemClick(View v) {
            int position = Integer.parseInt(v.getTag().toString());
            SwitchUserResponse response = switchUserResponseArrayList.get(position);
            AppCommon.getInstance(mActivity).setUserId(response.getName());
            AppCommon.getInstance(mActivity).setUpdateUsername(getUserNameForKey(response.getName()));
            ProfileFragment profileFragment = new ProfileFragment();
            ((NavigationActivity) mActivity).changeTitle(mActivity.getResources().getString(R.string.profileText));
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.switchUserLayout, profileFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
