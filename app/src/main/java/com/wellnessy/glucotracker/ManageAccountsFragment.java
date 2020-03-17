package com.wellnessy.glucotracker;

import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.ManageAccountsAdapter;
import CustomControl.DividerItemDecoration;
import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import Response.SelectUserResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageAccountsFragment extends Fragment {

    @BindView(R.id.manageAccountsRecyclerView)
    RecyclerView manageAccountsRecyclerView;

    @BindView(R.id.deleteButton)
    Button deleteButton;

    DbHelper mydb;

    ArrayList<SelectUserResponse> selectUserResponseArrayList;
    ManageAccountsAdapter adapter;
    ArrayList<SelectUserResponse> selectedUserList;

    @BindView(R.id.noUsersTextView)
    TextViewGothamBook noUsersTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.manage_accounts, container, false);
        ButterKnife.bind(this, rootView);
        mydb = new DbHelper(getActivity());
        selectedUserList = new ArrayList<SelectUserResponse>();
        selectUserResponseArrayList = mydb.getAllUserList();
        if (selectUserResponseArrayList.size() == 0) {
            noUsersTextView.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        } else {
            noUsersTextView.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        manageAccountsRecyclerView.setLayoutManager(layout);
        adapter = new ManageAccountsAdapter(getActivity(), selectUserResponseArrayList, ManageAccountsFragment.this);
        manageAccountsRecyclerView.setAdapter(adapter);
        manageAccountsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        return rootView;
    }

    public void addData(int position) {
        SelectUserResponse response = selectUserResponseArrayList.get(position);
        selectedUserList.add(response);
    }

    public void removeData(int position) {
        SelectUserResponse response = selectUserResponseArrayList.get(position);
        if (selectedUserList.size() > 0) {
            selectedUserList.remove(response);
        }
    }

    @OnClick(R.id.deleteButton)
    public void onDeleteButtonClick() {
        if (selectedUserList.size() == 0) {
            AppCommon.getInstance(getActivity()).showDialog(getActivity(), getResources().getString(R.string.selectAccountToDelete));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.deleteSelectedAccounts));
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AppCommon.getInstance(getActivity()).showDialog(getActivity(), getResources().getString(R.string.accountDeleteSuccessfully));
                    for (int j = 0; j < selectedUserList.size(); j++) {
                        SelectUserResponse response = selectedUserList.get(j);
                        mydb.deleteuser(response.getUserName());
                        selectUserResponseArrayList.remove(response);

                        if (selectUserResponseArrayList.size() == 0) {
                            deleteButton.setVisibility(View.GONE);
                            noUsersTextView.setVisibility(View.VISIBLE);
                        } else {
                            deleteButton.setVisibility(View.VISIBLE);
                            noUsersTextView.setVisibility(View.GONE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

        }
    }
}
