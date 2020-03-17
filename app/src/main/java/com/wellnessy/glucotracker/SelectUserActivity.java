package com.wellnessy.glucotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.SelectUserAdapter;
import CustomControl.DividerItemDecoration;
import Database.DbHelper;
import Response.SelectUserResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectUserActivity extends Activity {

    @BindView(R.id.selectUserRecyclerView)
    RecyclerView selectUserRecyclerView;
    Activity context=this;
    DbHelper mydb;
    ArrayList<SelectUserResponse> selectUserResponseArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user_recyclerview);
        ButterKnife.bind(this);
        mydb = new DbHelper(this);
        selectUserResponseArrayList=mydb.getAllContacts();
        LinearLayoutManager layout= new LinearLayoutManager(this);
        selectUserRecyclerView.setLayoutManager(layout);
        SelectUserAdapter adapter= new SelectUserAdapter(selectUserResponseArrayList,context);
        selectUserRecyclerView.setAdapter(adapter);
        selectUserRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
    }
    @OnClick(R.id.backIcon)
    public void backIconClick(){
        Intent intent= new Intent(SelectUserActivity.this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
