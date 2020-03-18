package com.wellnessy.glucotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import CustomControl.EditTextGothamBook;
import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    public boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.userNameEditText)
    EditTextGothamBook usernameEditText;

    @BindView(R.id.passwordEditText)
    EditTextGothamBook passwordEditText;

    boolean isShowPasword = false;
    @BindView(R.id.show)
    TextViewGothamBook mshowPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        ButterKnife.bind(this);

        usernameEditText.setText(getIntent().getStringExtra("username"));
        passwordEditText.setText(getIntent().getStringExtra("password"));
    }

    @OnClick(R.id.signUpButton)
    public void setSignUpButton() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);

    }

    public Boolean validations() {
        Boolean validate = true;
        if (!(usernameEditText.getText().toString().isEmpty())) {
            usernameEditText.setError(null);
        } else {
            usernameEditText.setError(getResources().getString(R.string.enterUsername));
            validate = false;
        }
        if (!(passwordEditText.getText().toString().isEmpty())) {
            passwordEditText.setError(null);
        } else {
            passwordEditText.setError(getResources().getString(R.string.enterPassword));
            validate = false;
        }
        return validate;
    }

    @OnClick(R.id.dropDownIcon)
    public void setDropDownIconStyle() {
        Intent intent = new Intent(LoginActivity.this, SelectUserActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.loginButton)
    public void loginButtonClick() {
        if (validations()) {
            String name = "";
            String usernameEditTextString = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String usernameArrayList = AppCommon.getInstance(this).getUsernameArrayList();
            if (!usernameArrayList.equals("")) {
                Gson gson = new Gson();
                ArrayList<UserName> userNameArrayList = gson.fromJson(usernameArrayList, new TypeToken<ArrayList<UserName>>() {
                }.getType());
                for (UserName userName : userNameArrayList) {
                    if (userName.getValue().equals(usernameEditTextString)) {
                        name = userName.getKey();
                        break;
                    }
                }
            }

            String storedPassword = DbHelper.getInstance(this).getSinlgeEntry(name);
            if (password.equals(storedPassword)) {
                AppCommon.getInstance(this).setUserId(name);
                AppCommon.getInstance(this).setUpdateUsername(getUserNameForKey(name));
                AppCommon.getInstance(this).setIsUserLogIn(true);
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                startActivity(intent);
                finish();
            } else {
                AppCommon.showDialog(this, getResources().getString(R.string.usernameAndPassNotMatch));
            }
        }


    }

    public String getUserNameForKey(String key) {
        String userName = key;
        String fetchList = AppCommon.getInstance(this).getUsernameArrayList();
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

    @OnClick(R.id.show)
    public void showClick() {
        if (!isShowPasword) {
            isShowPasword = true;
            mshowPasswordText.setText(getResources().getString(R.string.hideText));
            passwordEditText.setTransformationMethod(null);
        } else {
            isShowPasword = false;
            passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            mshowPasswordText.setText(getResources().getString(R.string.show));
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        doubleBackToExitPressedOnce = true;

        Toast.makeText(this, R.string.back_msg, Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}




