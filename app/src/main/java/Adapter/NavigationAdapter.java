package Adapter;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import androidx.recyclerview.widget.RecyclerView;

import com.wellnessy.glucotracker.NavigationActivity;
import com.wellnessy.glucotracker.R;

import java.util.ArrayList;

import CustomControl.TextViewGothamBook;
import Response.NavigationResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.DataHolder> {
    ArrayList<NavigationResponse> navigationResponseArrayList;
    Activity activity;


    public NavigationAdapter(ArrayList<NavigationResponse> dataList, Activity activity){
        this.navigationResponseArrayList=dataList;
        this.activity=activity;
    }
    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_layout, parent, false);
        DataHolder dataHolder = new DataHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        NavigationResponse navigationResponse= navigationResponseArrayList.get(position);
        holder.navigationItemsTextView.setText(navigationResponse.getItemList());
        holder.relativeLayoutParent.setTag(Integer.toString(position));

    }

    @Override
    public int getItemCount() {
        return navigationResponseArrayList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.navigationItems)
        TextViewGothamBook navigationItemsTextView;


        @BindView(R.id.listParent)
        RelativeLayout relativeLayoutParent;

        public DataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick(R.id.listParent)
        public void itemClick(View v) {
            int pos = Integer.parseInt(v.getTag().toString());
            ((NavigationActivity) activity).seFragment(pos);

        }
    }
}
