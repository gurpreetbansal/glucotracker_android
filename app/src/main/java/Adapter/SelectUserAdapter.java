package Adapter;

import android.app.Activity;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.LoginActivity;
import com.wellnessy.glucotracker.R;
import com.wellnessy.glucotracker.UserName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import CustomControl.TextViewGothamBook;
import Infrastructure.AppCommon;
import Response.SelectUserResponse;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectUserAdapter extends RecyclerView.Adapter<SelectUserAdapter.DataHolder> {
    ArrayList<SelectUserResponse> selectUserResponseArrayList;
    Activity context;

    public SelectUserAdapter(ArrayList<SelectUserResponse> data, Activity context) {
        this.selectUserResponseArrayList = data;
        this.context = context;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_user_row, parent, false);
        DataHolder dataHolder = new DataHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        SelectUserResponse response = selectUserResponseArrayList.get(position);
        holder.usernameTextView.setText(getUserNameForKey(response.getUserName()));
        holder.selectUserLayout.setTag(Integer.toString(position));
    }
    public String getUserNameForKey(String key) {
        String userName = key;
        String fetchList = AppCommon.getInstance(context).getUsernameArrayList();
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
        return selectUserResponseArrayList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.selectUserName)
        TextViewGothamBook usernameTextView;

        @BindView(R.id.selectUserLayout)
        LinearLayout selectUserLayout;

        public DataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = Integer.parseInt(view.getTag().toString());
            SelectUserResponse userResponse = selectUserResponseArrayList.get(position);
           // String username = userResponse.getUserName();
            String password = userResponse.getPassword();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("username", getUserNameForKey(userResponse.getUserName()));
            intent.putExtra("password", password);
            context.startActivity(intent);
            context.finish();
        }
    }
}
