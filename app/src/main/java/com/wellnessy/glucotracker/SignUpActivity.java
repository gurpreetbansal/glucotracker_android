package com.wellnessy.glucotracker;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import CustomControl.EditTextGothamBook;
import CustomControl.TextViewGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends Activity {
    @BindView(R.id.normalButton)
    Button normalButton;

    @BindView(R.id.diagnosisLayout)
    LinearLayout diagnosisLayout;

    @BindView(R.id.typeButton)
    Button typeButton;

    @BindView(R.id.userNameEditText)
    EditTextGothamBook usernameEditText;

    @BindView(R.id.passwordEditText)
    EditTextGothamBook passwordEditText;

    @BindView(R.id.genderTextView)
    TextViewGothamBook genderTextView;

    @BindView(R.id.enterAgeEditText)
    EditTextGothamBook enterAgeEditText;

    @BindView(R.id.enterHeightEditText)
    EditTextGothamBook enterHeightEditText;

    @BindView(R.id.enterWeightEditText)
    EditTextGothamBook enterWeightEditText;

    @BindView(R.id.enterWeightEditText1)
    EditTextGothamBook weightEditTextLb;

    @BindView(R.id.glucoseEditView)
    EditTextGothamBook glucoseEditView;

    @BindView(R.id.glucoseLayout)
    LinearLayout glucoseRelativeLayout;

    @BindView(R.id.line)
    TextView line;

    @BindView(R.id.glucoseRange)
    TextViewGothamBook glucoseRangeTextView;

    @BindView(R.id.signUpImageView)
    CircleImageView signUpImageView;

    @BindView(R.id.enterGlucoseEditText1)
    EditTextGothamBook glucoseEditViewMg;

    @BindView(R.id.enterHeightEditText1)
    EditTextGothamBook heightEditTextInch;

    @BindView(R.id.havingNoFoodText)
    TextView havingNoFoodText;

    @BindView(R.id.setAsNormalText)
    TextView updateLastWeekText;

    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_CAPTURE_IMG = 2;
    AlertDialog mAlertDialog;

    public String mAlertDialogItems[];
    public String genderDialogItems[];

    String noMedicationValue = "";
    String biguanideValue = "";
    String sulphonylureasValue = "";
    String glucosedasesValue = "";
    String insulinValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        ButterKnife.bind(this);
        genderDialogItems = getResources().getStringArray(R.array.genderItems);
        mAlertDialogItems = getResources().getStringArray(R.array.captureImageItems);
        normalButton.setTextColor(getResources().getColor(R.color.white));
        normalButton.setSelected(true);

        glucoseRelativeLayout.setVisibility(View.GONE);
        usernameEditText.setTextColor(getResources().getColor(R.color.blue));
        passwordEditText.setTextColor(getResources().getColor(R.color.blue));
        enterHeightEditText.setTextColor(getResources().getColor(R.color.blue));
        enterWeightEditText.setTextColor(getResources().getColor(R.color.blue));
        enterAgeEditText.setTextColor(getResources().getColor(R.color.blue));
        glucoseEditView.setTextColor(getResources().getColor(R.color.blue));
        heightEditTextInch.setTextColor(getResources().getColor(R.color.blue));
        glucoseEditViewMg.setTextColor(getResources().getColor(R.color.blue));
        weightEditTextLb.setTextColor(getResources().getColor(R.color.blue));

        enterHeightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!enterHeightEditText.getText().toString().trim().isEmpty() && !b) {
                    double d = AppCommon.getCMToInch(Double.parseDouble(enterHeightEditText.getText().toString()));
                    heightEditTextInch.setText(String.valueOf(d));
                    AppCommon.getInstance(SignUpActivity.this).setHeightInch(heightEditTextInch.getText().toString());
                }
            }
        });
        enterHeightEditText.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!enterHeightEditText.getText().toString().trim().isEmpty()) {
                        double d = AppCommon.getCMToInch(Double.parseDouble(enterHeightEditText.getText().toString()));
                        heightEditTextInch.setText(String.valueOf(d));
                        AppCommon.getInstance(SignUpActivity.this).setHeightInch(heightEditTextInch.getText().toString());
                    }
                }
                return false;
            }
        });
        heightEditTextInch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!heightEditTextInch.getText().toString().trim().isEmpty() && !b) {
                    double d = AppCommon.getInchToCM(Double.parseDouble(heightEditTextInch.getText().toString()));
                    double originalValue = Math.ceil(d);
                    int value = (int) originalValue;
                    enterHeightEditText.setText(String.valueOf(value));
                }
            }
        });
        heightEditTextInch.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!heightEditTextInch.getText().toString().trim().isEmpty()) {
                        double d = AppCommon.getInchToCM(Double.parseDouble(heightEditTextInch.getText().toString()));
                        double originalValue = Math.ceil(d);
                        int value = (int) originalValue;
                        enterHeightEditText.setText(String.valueOf(value));
                    }
                }
                return false;
            }
        });
        enterWeightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!enterWeightEditText.getText().toString().trim().isEmpty() && !b) {
                    double d = AppCommon.getKiloToLB(Double.parseDouble(enterWeightEditText.getText().toString()));
                    weightEditTextLb.setText(String.valueOf(d));
                    AppCommon.getInstance(SignUpActivity.this).setWeightLb(weightEditTextLb.getText().toString());
                }
            }
        });

        enterWeightEditText.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!enterWeightEditText.getText().toString().trim().isEmpty()) {
                        double d = AppCommon.getKiloToLB(Double.parseDouble(enterWeightEditText.getText().toString()));
                        weightEditTextLb.setText(String.valueOf(d));
                        AppCommon.getInstance(SignUpActivity.this).setWeightLb(weightEditTextLb.getText().toString());
                    }
                }
                return false;
            }
        });

        weightEditTextLb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!weightEditTextLb.getText().toString().trim().isEmpty() && !b) {
                    double d = AppCommon.getLbToKilo(Double.parseDouble(weightEditTextLb.getText().toString()));
                    double originalValue = Math.ceil(d);
                    int value = (int) originalValue;
                    enterWeightEditText.setText(String.valueOf(value));
                }
            }
        });
        weightEditTextLb.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!weightEditTextLb.getText().toString().trim().isEmpty()) {
                        double d = AppCommon.getLbToKilo(Double.parseDouble(weightEditTextLb.getText().toString()));
                        double originalValue = Math.ceil(d);
                        int value = (int) originalValue;
                        enterWeightEditText.setText(String.valueOf(value));
                    }
                }
                return false;
            }
        });

        glucoseEditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!glucoseEditView.getText().toString().trim().isEmpty() && !b) {
                   float d = AppCommon.getMgDlFrommMol(Float.parseFloat(glucoseEditView.getText().toString()));
                    glucoseEditViewMg.setText(String.valueOf(d));
                    AppCommon.getInstance(SignUpActivity.this).setGlucoseMg(glucoseEditViewMg.getText().toString());
                }
            }
        });

        glucoseEditView.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!glucoseEditView.getText().toString().trim().isEmpty()) {
                        float d = AppCommon.getMgDlFrommMol(Float.parseFloat(glucoseEditView.getText().toString()));
                        glucoseEditViewMg.setText(String.valueOf(d));
                        AppCommon.getInstance(SignUpActivity.this).setGlucoseMg(glucoseEditViewMg.getText().toString());
                    }
                }
                return false;
            }
        });
        glucoseEditViewMg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!glucoseEditViewMg.getText().toString().trim().isEmpty() && !b) {
                    double d = AppCommon.getmMolFromMgDl(Double.parseDouble(glucoseEditViewMg.getText().toString()));
                    glucoseEditView.setText(String.valueOf(d));
                }
            }
        });

        glucoseEditViewMg.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    double d = AppCommon.getmMolFromMgDl(Double.parseDouble(glucoseEditViewMg.getText().toString()));
                    glucoseEditView.setText(String.valueOf(d));
                }
                return false;
            }
        });

        glucoseEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (!glucoseEditView.getText().toString().trim().isEmpty()) {
                    float d = AppCommon.getMgDlFrommMol(Float.parseFloat(glucoseEditView.getText().toString()));
                    glucoseEditViewMg.setText(String.valueOf(d));
                    AppCommon.getInstance(SignUpActivity.this).setGlucoseMg(glucoseEditViewMg.getText().toString());
                }
            }
        });
    }


    @OnClick(R.id.signInButton)
    public void signInClick() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @OnClick(R.id.normalButton)
    public void setNormalButton() {
        if (!normalButton.isSelected()) {
            normalButton.setSelected(true);
            typeButton.setSelected(false);
            diagnosisLayout.setBackgroundResource(R.drawable.normal);
            normalButton.setTextColor(getResources().getColor(R.color.white));
            typeButton.setTextColor(getResources().getColor(R.color.black));
            glucoseRelativeLayout.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            glucoseRangeTextView.setVisibility(View.GONE);
            havingNoFoodText.setVisibility(View.GONE);
            updateLastWeekText.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.typeButton)
    public void setTypeButton() {
        if (!typeButton.isSelected()) {
            typeButton.setSelected(true);
            normalButton.setSelected(false);
            diagnosisLayout.setBackgroundResource(R.drawable.type);
            typeButton.setTextColor(getResources().getColor(R.color.white));
            normalButton.setTextColor(getResources().getColor(R.color.black));
            glucoseRelativeLayout.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            glucoseRangeTextView.setVisibility(View.VISIBLE);
            havingNoFoodText.setVisibility(View.VISIBLE);
            updateLastWeekText.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.genderTextView)
    public void genderDialogClick() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.gender));
        builder.setItems(genderDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (genderDialogItems[i].equals(genderDialogItems[0])) {
                    genderTextView.setText(genderDialogItems[0]);
                    genderTextView.setError(null);
                } else if (genderDialogItems[i].equals(genderDialogItems[1])) {
                    genderTextView.setText(genderDialogItems[1]);
                    genderTextView.setError(null);
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancelText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    @OnClick(R.id.changeImageButton)
    public void setmChangePictureDialogView(View v) {
        changePicture();
    }

    @OnClick(R.id.signUpImageView)
    public void signUpPicture(View v) {
        changePicture();
    }

    public void changePicture() {
        AlertDialog.Builder mABuilder = new AlertDialog.Builder(this);
        mABuilder.setItems(mAlertDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mAlertDialogItems[i].equals(mAlertDialogItems[0])) {
                    requestPermission();
                } else if (mAlertDialogItems[i].equals(mAlertDialogItems[1])) {
                    gallaryPermission();
                } else if (mAlertDialogItems[i].equals(mAlertDialogItems[2])) {
                    dialogInterface.dismiss();
                }
            }
        });

        mAlertDialog = mABuilder.show();
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);

        } else {
            startCameraIntent();
        }
    }

    private void gallaryPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    201);

        } else {
            startGalleryIntent();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startCameraIntent();
                }
                break;
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                   startGalleryIntent();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    private void startCameraIntent() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, SignUpActivity.RESULT_CAPTURE_IMG);
    }

    public String getSelectedType() {
        String mSelectedColumnName = "";
        if (normalButton.isSelected()) {
            mSelectedColumnName = "Normal";
        } else if (typeButton.isSelected()) {
            mSelectedColumnName = "Type 2";
        }
        return mSelectedColumnName;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAPTURE_IMG && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String newImageSelectedPath = MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    bitmap, "Image Description", null);
            if (newImageSelectedPath == null) {
                try {
                    FileOutputStream ostream = new FileOutputStream(AppCommon.getInstance(this).createImageFile());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ostream);
                    ostream.flush();
                    ostream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                Uri bmpUri = Uri.parse(url);
                signUpImageView.setImageURI(bmpUri);
                AppCommon.getInstance(this).setFilePath(url);
            } else {
                Uri bmpUri = Uri.parse(newImageSelectedPath);
                String selectedFilePath = AppCommon.getInstance(this).getRealPathFromUri(bmpUri);
                AppCommon.getInstance(this).setFilePath(selectedFilePath);
                signUpImageView.setImageURI(bmpUri);
            }
        } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String selectedFilePath = AppCommon.getInstance(this).getRealPathFromUri(selectedImage);
            AppCommon.getInstance(this).setFilePath(selectedFilePath);
            signUpImageView.setImageURI(selectedImage);
        }
        signUpImageView.setBorderColor(getResources().getColor(R.color.blue));
        signUpImageView.setBorderWidth(2);
    }

    public void insertData() {
        String mPhotoPath = AppCommon.getInstance(this).getFilePath();
        if (DbHelper.getInstance(this).insertContact(usernameEditText.getText().toString(), passwordEditText.getText().toString(), genderTextView.getText().toString(), enterAgeEditText.getText().toString(),
                enterHeightEditText.getText().toString(), enterWeightEditText.getText().toString(), glucoseEditView.getText().toString(), mPhotoPath, getSelectedType(), noMedicationValue, sulphonylureasValue, biguanideValue, glucosedasesValue, insulinValue)) {
            setPreData();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.registerSuccessfully)).setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(SignUpActivity.this, NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.registerAgain))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @OnClick(R.id.createButton)
    public void run(View view) {

        if (validations()) {
            if (getSelectedType().equals("Normal")) {
                noMedicationValue = "1";
                sulphonylureasValue = "0";
                biguanideValue = "0";
                glucosedasesValue = "0";
                insulinValue = "0";
                insertData();
            } else if (getSelectedType().equals("Type 2")) {
                noMedicationValue = "1";
                sulphonylureasValue = "0";
                biguanideValue = "0";
                glucosedasesValue = "0";
                insulinValue = "0";
                insertData();
            }
        }
        String usernameArrayList= AppCommon.getInstance(this).getUsernameArrayList();
        String usernameEditTextString= usernameEditText.getText().toString().trim();
        if(usernameArrayList.equals("")){
            UserName username= new UserName(usernameEditTextString,usernameEditTextString);
            ArrayList<UserName> userNameArrayList= new ArrayList<>();
            userNameArrayList.add(username);
            Gson gson= new Gson();
            String gsonObj= gson.toJson(userNameArrayList);
            AppCommon.getInstance(this).setUserNameArrayList(gsonObj);
            AppCommon.getInstance(this).setUpdateUsername(usernameEditTextString);
        }else{
            boolean isEntryFound = false;
            Gson gson = new Gson();
            ArrayList<UserName> userNameArrayList = gson.fromJson(usernameArrayList, new TypeToken<ArrayList<UserName>>() {
            }.getType());
            for (UserName userName : userNameArrayList) {
                if (userName.getKey().equals(usernameEditTextString)) {
                    userName.setValue(usernameEditTextString);
                    isEntryFound=true;
                    break;
                }
            }
            if(!isEntryFound){
                UserName username= new UserName(usernameEditTextString,usernameEditTextString);
                userNameArrayList.add(username);
            }
            String gsonObj = gson.toJson(userNameArrayList);
            AppCommon.getInstance(this).setUserNameArrayList(gsonObj);
            AppCommon.getInstance(this).setUpdateUsername(usernameEditTextString);
        }
    }

    public void setPreData() {
        AppCommon.getInstance(SignUpActivity.this).setUserId(usernameEditText.getText().toString());
        AppCommon.getInstance(SignUpActivity.this).saveUnitTypesIntoSharedPreferences(getResources().getString(R.string.cmText), getResources().getString(R.string.kgText), getResources().getString(R.string.mMolText), getResources().getString(R.string.g));

        // inserted Measurement Settings Default values on Successful Sign up
        DbHelper.getInstance(this).insertMeasurementSettingsDetails(AppCommon.getInstance(SignUpActivity.this).getUserId(), getResources().getString(R.string.pulseMore), getResources().getString(R.string.spo2less), getResources().getString(R.string.hgbMore), getResources().getString(R.string.hgbLess), getResources().getString(R.string.bfvMore), getResources().getString(R.string.bfvLess), getResources().getString(R.string.pulseMore), getResources().getString(R.string.pulseLess), getResources().getString(R.string.gluMorninMore), getResources().getString(R.string.gluMorinLess), getResources().getString(R.string.gluafterMealLess), getResources().getString(R.string.gluMorinLess), "", "", "");

        // inserted Default Reminders details in db on Successful Sign up
        DbHelper.getInstance(this).insertEditRemindertDetails(AppCommon.getInstance(SignUpActivity.this).getUserId(), getResources().getString(R.string.fastingBlood), getResources().getString(R.string.time), getResources().getString(R.string.falseText), "", getResources().getString(R.string.seven), getResources().getString(R.string.doubleZero));
        DbHelper.getInstance(this).insertEditRemindertDetails(AppCommon.getInstance(SignUpActivity.this).getUserId(), getResources().getString(R.string.breakfastText), getResources().getString(R.string.timeEight), getResources().getString(R.string.falseText), "", getResources().getString(R.string.eight), getResources().getString(R.string.doubleZero));
        DbHelper.getInstance(this).insertEditRemindertDetails(AppCommon.getInstance(SignUpActivity.this).getUserId(), getResources().getString(R.string.lunchText), getResources().getString(R.string.timeOne), getResources().getString(R.string.falseText), "", getResources().getString(R.string.oneText), getResources().getString(R.string.doubleZero));
        DbHelper.getInstance(this).insertEditRemindertDetails(AppCommon.getInstance(SignUpActivity.this).getUserId(), getResources().getString(R.string.dinnerText), getResources().getString(R.string.time), getResources().getString(R.string.falseText), "", getResources().getString(R.string.seven), getResources().getString(R.string.doubleZero));
        DbHelper.getInstance(this).insertEditRemindertDetails(AppCommon.getInstance(SignUpActivity.this).getUserId(), getResources().getString(R.string.sleepingText), getResources().getString(R.string.timeTen), getResources().getString(R.string.falseText), "", getResources().getString(R.string.ten), getResources().getString(R.string.doubleZero));
        DbHelper.getInstance(this).insertEditRemindertDetails(AppCommon.getInstance(SignUpActivity.this).getUserId(), getResources().getString(R.string.othersText), getResources().getString(R.string.timeFive), getResources().getString(R.string.falseText), "", getResources().getString(R.string.five), getResources().getString(R.string.doubleZero));
        AppCommon.getInstance(SignUpActivity.this).setIsUserLogIn(true);
    }

    public Boolean validations() {
        Boolean validate = true;
        String age = enterAgeEditText.getText().toString().trim();
        String height = enterHeightEditText.getText().toString().trim();
        String weight = enterWeightEditText.getText().toString().trim();
        String glucose = glucoseEditView.getText().toString().trim();
        String gender=genderTextView.getText().toString().trim();


        if (!(usernameEditText.getText().toString().trim().isEmpty())) {
            usernameEditText.setError(null);
        } else {
            usernameEditText.setError(getResources().getString(R.string.enterUsername));
            validate = false;
        }
        if ((usernameEditText.getText().toString().equals( DbHelper.getInstance(this).Exist(usernameEditText.getText().toString())))) {
            usernameEditText.setError(getResources().getString(R.string.enterUsername));
            validate = false;
        } else {
            usernameEditText.setError(null);
        }
        if (!(passwordEditText.getText().toString().isEmpty())) {
            passwordEditText.setError(null);
        } else {
            passwordEditText.setError(getResources().getString(R.string.enterPassword));
            validate = false;
        }
        if (gender.length()!=0) {
            genderTextView.setError(null);
        } else {
            genderTextView.setError(getResources().getString(R.string.selectGender));
            validate = false;
        }

        if (!age.isEmpty()) {
            if (Double.parseDouble(age) < 12 || Double.parseDouble(age) > 80) {
                enterAgeEditText.setError(getResources().getString(R.string.selectAge));
                validate = false;
            } else {
                enterAgeEditText.setError(null);
            }
        } else {
            enterAgeEditText.setError(getResources().getString(R.string.enterYourAge));
            validate = false;
        }

        if (!height.isEmpty()) {
            if (Double.parseDouble(height) < 50 || Double.parseDouble(height) > 250) {
                enterHeightEditText.setError(getResources().getString(R.string.enterCorrectHeight));
                validate = false;
            } else {
                enterHeightEditText.setError(null);
            }
        } else {
            enterHeightEditText.setError(getResources().getString(R.string.enterYourHeight));
            validate = false;
        }
        if (!weight.isEmpty()) {
            if (Double.parseDouble(weight) < 30 || Double.parseDouble(weight) > 200) {
                enterWeightEditText.setError(getResources().getString(R.string.enterCorrectWeight));
                validate = false;
            } else {
                enterWeightEditText.setError(null);
            }
        } else {
            enterWeightEditText.setError(getResources().getString(R.string.enterYourWeight));
            validate = false;
        }
        if (typeButton.isSelected()) {
            if (!glucose.isEmpty()) {
                if (Double.parseDouble(glucose) < 3.1 || Double.parseDouble(glucose) > 17.5) {
                    glucoseEditView.setError(getResources().getString(R.string.enterCorrectGlucose));
                    validate = false;
                } else {
                    glucoseEditView.setError(null);
                }
            } else {
                glucoseEditView.setError(getResources().getString(R.string.enterGlucose));
                validate = false;
            }
        }
        return validate;
    }

    @OnClick(R.id.backIcon)
    public void backbuttonClick() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}


