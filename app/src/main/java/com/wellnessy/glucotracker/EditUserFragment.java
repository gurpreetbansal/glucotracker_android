package com.wellnessy.glucotracker;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
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

public class EditUserFragment extends Fragment {
    @BindView(R.id.editUserImageView)
    CircleImageView editImageView;

    @BindView(R.id.normalButton)
    Button normalButton;

    @BindView(R.id.diagnosisLayout)
    LinearLayout diagnosisLayout;

    @BindView(R.id.typeButton)
    Button typeButton;

    @BindView(R.id.changeImageButton)
    ImageView mChangeButton;

    @BindView(R.id.userNameEditText)
    EditTextGothamBook usernameEditText;

    @BindView(R.id.genderTextView)
    TextViewGothamBook genderTextView;

    @BindView(R.id.enterAgeEditText)
    EditTextGothamBook enterAgeEditText;

    @BindView(R.id.enterHeightEditText)
    EditTextGothamBook enterHeightEditText;

    @BindView(R.id.enterWeightEditText)
    EditTextGothamBook enterWeightEditText;

    @BindView(R.id.glucoseEditView)
    EditTextGothamBook mGlucoseEditText;

    @BindView(R.id.glucoseLayout)
    LinearLayout glucoseRelativeLayout;

    @BindView(R.id.line)
    TextView line;

    @BindView(R.id.line1)
    TextView line1;

    @BindView(R.id.enterHeightEditText1)
    EditTextGothamBook heightEditTextInch;

    @BindView(R.id.enterWeightEditText1)
    EditTextGothamBook weightEditTextLb;

    @BindView(R.id.glucoseRange)
    TextViewGothamBook glucoseRangeTextView;

    @BindView(R.id.enterGlucoseEditText1)
    EditTextGothamBook glucoseEditViewMg;

    @BindView(R.id.saveButton)
    Button saveButton;

    @BindView(R.id.havingNoFoodText)
    TextView havingNoFoodText;

    @BindView(R.id.setAsNormalText)
    TextView updateLastWeekText;

    Bitmap mImageBitmap = null;

    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_CAPTURE_IMG = 2;
    AlertDialog mAlertDialog;
    public String mAlertDialogItems[];
    public String genderDialogItems[];

    File photoFile = null;

    String noMedicationValue = "";
    String biguanideValue = "";
    String sulphonylureasValue = "";
    String glucosedasesValue = "";
    String insulinValue = "";
    String glu = "";
    String selectTpe = "";
    String name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_user_layout, container, false);
        ButterKnife.bind(this, rootView);

        genderDialogItems = getResources().getStringArray(R.array.genderItems);
        mAlertDialogItems = getResources().getStringArray(R.array.captureImageItems);

        String typeTest = DbHelper.getInstance(getActivity()).getTestTypeForUsername(AppCommon.getInstance(getActivity()).getUserId());
        if (!(typeTest.equals(getResources().getString(R.string.notExist)))) {
            if (typeTest.equals("Normal")) {
                normalButton.setSelected(true);
                typeButton.setSelected(false);
                diagnosisLayout.setBackgroundResource(R.drawable.normal);
                normalButton.setTextColor(getResources().getColor(R.color.white));
                typeButton.setTextColor(getResources().getColor(R.color.black));
                glucoseRelativeLayout.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                glucoseRangeTextView.setVisibility(View.GONE);
                havingNoFoodText.setVisibility(View.GONE);
                updateLastWeekText.setVisibility(View.GONE);

            } else {
                typeButton.setSelected(true);
                normalButton.setSelected(false);
                diagnosisLayout.setBackgroundResource(R.drawable.type);
                normalButton.setTextColor(getResources().getColor(R.color.black));
                typeButton.setTextColor(getResources().getColor(R.color.white));
                glucoseRelativeLayout.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                glucoseRangeTextView.setVisibility(View.VISIBLE);
                havingNoFoodText.setVisibility(View.VISIBLE);
                updateLastWeekText.setVisibility(View.VISIBLE);
            }

        } else {
            AppCommon.showDialog(getActivity(), getResources().getString(R.string.noTypeSelected));
        }
        Cursor rs = DbHelper.getInstance(getActivity()).getUserData(AppCommon.getInstance(getActivity()).getUserId());
        rs.moveToFirst();

        name = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_NAME));
        String gender = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GENDER));
        String age = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_AGE));
        String height = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_hEIGHT));
        String weight = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_WEIGHT));
        String diabetes = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_DIABETES_TYPE));
        String glucose = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLUCOSE));
        String imagePath = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_IMAGE));

        if (!rs.isClosed()) {
            rs.close();
        }
        editImageView.setBorderColor(getResources().getColor(R.color.blue));
        if (imagePath != null && !imagePath.equals("")) {
            editImageView.setImageDrawable(Drawable.createFromPath(imagePath));
        }
        editImageView.setBorderColor(getResources().getColor(R.color.blue));
        editImageView.setBorderWidth(2);
        usernameEditText.setText(AppCommon.getInstance(getActivity()).getUpdateUsername());
        genderTextView.setText(gender);
        enterAgeEditText.setText(age);
        enterWeightEditText.setText(weight);
        enterHeightEditText.setText(height);
        mGlucoseEditText.setText(glucose);
        converCmToInch();
        kgToLb();
        if (!diabetes.equals("Normal")) {
            molToMgDl();
        }
        enterHeightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!enterHeightEditText.getText().toString().trim().isEmpty() && !b) {
                    converCmToInch();
                }
            }
        });
        enterHeightEditText.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!enterHeightEditText.getText().toString().trim().isEmpty()) {
                        converCmToInch();
                    }
                }
                return false;
            }
        });

        heightEditTextInch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!heightEditTextInch.getText().toString().trim().isEmpty() && !b) {
                    convertInchToCM();
                }
            }
        });
        heightEditTextInch.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!heightEditTextInch.getText().toString().trim().isEmpty()) {
                        convertInchToCM();
                    }
                }
                return false;
            }
        });

        enterWeightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!enterWeightEditText.getText().toString().trim().isEmpty() && !b) {
                    kgToLb();
                }
            }
        });
        enterWeightEditText.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!enterWeightEditText.getText().toString().trim().isEmpty()) {
                        kgToLb();
                    }
                }
                return false;
            }
        });
        weightEditTextLb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!weightEditTextLb.getText().toString().trim().isEmpty() && !b) {
                    lbToKilo();
                }
            }
        });
        weightEditTextLb.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!weightEditTextLb.getText().toString().trim().isEmpty()) {
                        lbToKilo();
                    }
                }
                return false;
            }
        });

        mGlucoseEditText.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!mGlucoseEditText.getText().toString().trim().isEmpty()) {
                        molToMgDl();
                    }
                }
                return false;
            }
        });
        glucoseEditViewMg.setOnEditorActionListener(new EditTextGothamBook.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!glucoseEditViewMg.getText().toString().trim().isEmpty()) {
                        double d = AppCommon.getmMolFromMgDl(Double.parseDouble(glucoseEditViewMg.getText().toString()));
                        mGlucoseEditText.setText(String.valueOf(d));
                        AppCommon.getInstance(getActivity()).setGlucoseMg(glucoseEditViewMg.getText().toString());
                    }
                }
                return false;
            }
        });

        mGlucoseEditText.addTextChangedListener(new TextWatcher() {
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
                if (!mGlucoseEditText.getText().toString().isEmpty()) {
                    molToMgDl();
                }
            }
        });

        return rootView;
    }


    public void converCmToInch() {
        double d = AppCommon.getCMToInch(Double.parseDouble(enterHeightEditText.getText().toString()));
        heightEditTextInch.setText(String.valueOf(d));
        AppCommon.getInstance(getActivity()).setHeightInch(heightEditTextInch.getText().toString());
    }

    public void convertInchToCM() {
        double d = AppCommon.getInchToCM(Double.parseDouble(heightEditTextInch.getText().toString()));
        double originalValue = Math.ceil(d);
        int value = (int) originalValue;
        enterHeightEditText.setText(String.valueOf(value));
        AppCommon.getInstance(getActivity()).setHeightInch(heightEditTextInch.getText().toString());
    }

    public void kgToLb() {
        double d = AppCommon.getKiloToLB(Double.parseDouble(enterWeightEditText.getText().toString()));
        weightEditTextLb.setText(String.valueOf(d));
        AppCommon.getInstance(getActivity()).setWeightLb(weightEditTextLb.getText().toString());
    }

    public void lbToKilo() {
        double d = AppCommon.getLbToKilo(Double.parseDouble(weightEditTextLb.getText().toString()));
        double originalValue = Math.ceil(d);
        int value = (int) originalValue;
        enterWeightEditText.setText(String.valueOf(value));
        AppCommon.getInstance(getActivity()).setWeightLb(weightEditTextLb.getText().toString());
    }

    public void molToMgDl() {
        float d = AppCommon.getMgDlFrommMol(Float.parseFloat(mGlucoseEditText.getText().toString()));
        glucoseEditViewMg.setText(String.valueOf(d));
        AppCommon.getInstance(getActivity()).setGlucoseMg(glucoseEditViewMg.getText().toString());
    }

    @OnClick(R.id.normalButton)
    public void setNormalButton() {
        if (!normalButton.isSelected()) {
            normalButton.setSelected(true);
            typeButton.setSelected(false);
            normalButton.setTextColor(getResources().getColor(R.color.white));
            diagnosisLayout.setBackgroundResource(R.drawable.normal);
            typeButton.setTextColor(getResources().getColor(R.color.black));
            glucoseRelativeLayout.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
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
            typeButton.setTextColor(getResources().getColor(R.color.white));
            diagnosisLayout.setBackgroundResource(R.drawable.type);
            normalButton.setTextColor(getResources().getColor(R.color.black));
            glucoseRelativeLayout.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            glucoseRangeTextView.setVisibility(View.VISIBLE);
            havingNoFoodText.setVisibility(View.VISIBLE);
            updateLastWeekText.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.genderTextView)
    public void genderDialogClick() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.gender));
        builder.setItems(genderDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (genderDialogItems[i].equals(getResources().getString(R.string.male))) {
                    genderTextView.setText(genderDialogItems[0]);
                } else if (genderDialogItems[i].equals(getResources().getString(R.string.female))) {
                    genderTextView.setText(genderDialogItems[1]);
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

    public void updateDAta() {
        if (validations()) {
            String mPhotoPath = AppCommon.getInstance(getContext()).getFilePath();
            DbHelper.getInstance(getActivity()).updateContact(name, genderTextView.getText().toString(), enterAgeEditText.getText().toString(), enterHeightEditText.getText().toString(), enterWeightEditText.getText().toString(), glu, mPhotoPath, selectTpe, noMedicationValue, sulphonylureasValue, biguanideValue, glucosedasesValue, insulinValue);
            Toast.makeText(getActivity(), getResources().getString(R.string.saved),
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.saveButton)
    public void setSaveButton() {
        if (normalButton.isSelected()) {
            selectTpe = "Normal";
            glu = getResources().getString(R.string.zero);
            noMedicationValue = "1";
            sulphonylureasValue = "0";
            biguanideValue = "0";
            glucosedasesValue = "0";
            insulinValue = "0";
            updateDAta();
        } else {
            selectTpe = "Type 2";
            glu = mGlucoseEditText.getText().toString();
            noMedicationValue = "1";
            sulphonylureasValue = "0";
            biguanideValue = "0";
            glucosedasesValue = "0";
            insulinValue = "0";
            updateDAta();
        }
        String usernameArrayList = AppCommon.getInstance(getActivity()).getUsernameArrayList();
        String usernameEditTextString = usernameEditText.getText().toString().trim();
        if (usernameArrayList.equals("")) {
            UserName username = new UserName(usernameEditTextString, usernameEditTextString);
            ArrayList<UserName> userNameArrayList = new ArrayList<>();
            userNameArrayList.add(username);
            Gson gson = new Gson();
            String gsonObj = gson.toJson(userNameArrayList);
            AppCommon.getInstance(getActivity()).setUserNameArrayList(gsonObj);
            AppCommon.getInstance(getActivity()).setUpdateUsername(usernameEditTextString);
        } else {
            Gson gson = new Gson();
            ArrayList<UserName> userNameArrayList = gson.fromJson(usernameArrayList, new TypeToken<ArrayList<UserName>>() {
            }.getType());
            for (UserName userName : userNameArrayList) {
                if (userName.getKey().equals(name)) {
                    userName.setValue(usernameEditTextString);
                    break;
                }
            }
            String gsonObj = gson.toJson(userNameArrayList);
            AppCommon.getInstance(getActivity()).setUserNameArrayList(gsonObj);
            AppCommon.getInstance(getActivity()).setUpdateUsername(usernameEditTextString);
        }
    }

    @OnClick(R.id.changeImageButton)
    public void setmChangeButton(View v) {
        changePicture();
    }

    @OnClick(R.id.editUserImageView)
    public void mEditUserImageClick(View v) {
        changePicture();
    }

    private void changePicture() {
        AlertDialog.Builder mABuilder = new AlertDialog.Builder(getContext());
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
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    AppCommon.CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCameraIntent();
        }
    }

    private void gallaryPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    AppCommon.GALLARY_PERMISSION_REQUEST_CODE);

        } else {
            startGalleryIntent();

        }
    }

    public void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void startCameraIntent() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, RESULT_CAPTURE_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAPTURE_IMG && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String newImageSelectedPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    bitmap, "Image Description", null);
            if (newImageSelectedPath == null) {
                try {
                    FileOutputStream ostream = new FileOutputStream(AppCommon.getInstance(getActivity()).createImageFile());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ostream);
                    ostream.flush();
                    ostream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String url = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
                Uri bmpUri = Uri.parse(url);
                editImageView.setImageURI(bmpUri);
                AppCommon.getInstance(getActivity()).setFilePath(url);
            } else {
                Uri bmpUri = Uri.parse(newImageSelectedPath);
                String selectedFilePath = AppCommon.getInstance(getActivity()).getRealPathFromUri(bmpUri);
                AppCommon.getInstance(getActivity()).setFilePath(selectedFilePath);
                editImageView.setImageURI(bmpUri);
            }
        } else if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String selectedFilePath = AppCommon.getInstance(getContext()).getRealPathFromUri(selectedImage);
            AppCommon.getInstance(getContext()).setFilePath(selectedFilePath);
            editImageView.setBorderWidth(2);
            editImageView.setImageURI(selectedImage);
        }
        editImageView.setBorderColor(getResources().getColor(R.color.blue));
    }

    public Boolean validations() {
        Boolean validate = true;
        String name = usernameEditText.getText().toString().trim();
        String age = enterAgeEditText.getText().toString().trim();
        String height = enterHeightEditText.getText().toString().trim();
        String weight = enterWeightEditText.getText().toString().trim();
        String glucose = mGlucoseEditText.getText().toString().trim();

        if (!(name.isEmpty())) {
            usernameEditText.setError(null);
        } else {
            usernameEditText.setError(getResources().getString(R.string.enterUsername));
            validate = false;
        }
        if (!age.isEmpty()) {
            if (Integer.parseInt(age) < 12 || Integer.parseInt(age) > 80) {
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
                    mGlucoseEditText.setError(getResources().getString(R.string.enterCorrectGlucose));
                    validate = false;
                } else {
                    mGlucoseEditText.setError(null);
                }
            } else {
                mGlucoseEditText.setError(getResources().getString(R.string.enterGlucose));
                validate = false;
            }
        }

        return validate;
    }

    public void editUserBackClick() {
        Fragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.editUserLayout, profileFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack("EditUserFragment");
        transaction.commit();
    }
}
