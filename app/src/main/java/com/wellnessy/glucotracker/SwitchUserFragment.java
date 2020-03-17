package com.wellnessy.glucotracker;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.SwitchUserAdapter;
import CustomControl.DividerItemDecoration;
import Database.DbHelper;
import Response.SwitchUserResponse;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SwitchUserFragment extends Fragment {
    @BindView(R.id.selectUserRecyclerView)
    RecyclerView selectUserRecyclerView;
    ArrayList<SwitchUserResponse> switchUserResponseArrayList;
    DbHelper mDbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.switch_user, container, false);
        ButterKnife.bind(this, rootView);
        mDbHelper = new DbHelper(getContext());
        switchUserResponseArrayList = DbHelper.getInstance(getActivity()).getSwitchUserData();
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        selectUserRecyclerView.setLayoutManager(layout);
        SwitchUserAdapter adapter = new SwitchUserAdapter(switchUserResponseArrayList, getChildFragmentManager(), getActivity(), mDbHelper);
        selectUserRecyclerView.setAdapter(adapter);
        selectUserRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return rootView;
    }

    public void switchBAckClick() {
        Fragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.switchUserLayout, profileFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("SwitchUserFragment");
        transaction.commit();
    }


}
