package com.wellnessy.glucotracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.GuideAdapter;
import CustomControl.DividerItemDecoration;
import Response.GuideResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GuideActivity extends Activity {
    @BindView(R.id.guideRecyclerView)
    RecyclerView guideRecyclerView;

    ArrayList<GuideResponse> guideResponseArrayList;
    public String guideItems[];
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);
        ButterKnife.bind(this);
        guideItems=getResources().getStringArray(R.array.guideItems);
        guideResponseArrayList= new ArrayList<>();
        for (int i=0;i<guideItems.length;i++)
        {
            GuideResponse response= new GuideResponse(guideItems[i]);
            guideResponseArrayList.add(response);
        }

        LinearLayoutManager layout= new LinearLayoutManager(this);
        guideRecyclerView.setLayoutManager(layout);
        GuideAdapter guideAdapter= new GuideAdapter(guideResponseArrayList,this,context);
        guideRecyclerView.setAdapter(guideAdapter);
       guideRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
    }
    @OnClick(R.id.backIcon)
    public void backIconClick(){
        this.finish();
    }
}
