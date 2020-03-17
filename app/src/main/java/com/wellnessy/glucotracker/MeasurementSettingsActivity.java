package com.wellnessy.glucotracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import CustomControl.EditTextGothamBook;
import Database.DbHelper;
import Infrastructure.AppCommon;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeasurementSettingsActivity extends Activity {

    @BindView(R.id.email1EditText)
    EditTextGothamBook email1EditText;

    @BindView(R.id.email2EditText)
    EditTextGothamBook email2EditText;

    @BindView(R.id.email3EditText)
    EditTextGothamBook email3EditText;

    @BindView(R.id.spo2More)
    EditTextGothamBook spo2MoreEditText;
    @BindView(R.id.spo2Less)
    EditTextGothamBook spo2LessEditText;

    @BindView(R.id.hgbMore)
    EditTextGothamBook hgbMoreEditText;

    @BindView(R.id.hgbLess)
    EditTextGothamBook hgbLessEditText;

    @BindView(R.id.bfvMore)
    EditTextGothamBook bfvMoreEditText;

    @BindView(R.id.bfvLess)
    EditTextGothamBook bfvLessEditText;

    @BindView(R.id.pulseMore)
    EditTextGothamBook pulseMoreEditText;

    @BindView(R.id.pulseLess)
    EditTextGothamBook pulseLessEditText;

    @BindView(R.id.gluMorningMore)
    EditTextGothamBook gluMorningMoreEditText;

    @BindView(R.id.gluMorningLess)
    EditTextGothamBook gluMorningLessEditText;

    @BindView(R.id.gluAfterMealMore)
    EditTextGothamBook gluAfterMealMoreEditText;

    @BindView(R.id.gluAfterMealLess)
    EditTextGothamBook gluAfterMealLessEditText;

    String glucoseValue = "";
    String pulseValue = "";
    String bloodFlowVelocity = "";
    String hemoglobinValue = "";
    String oxygenSaturation = "";
    String date="";
    String time="";
    Cursor cursor;
    float glucoseFinalValue;
    String glucoseFinal="";
    String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measurement_settings);
        ButterKnife.bind(this);
         name =  AppCommon.getInstance(this).getUpdateUsername();
        Cursor rs = DbHelper.getInstance(this).getMeasurementSettingRecord(AppCommon.getInstance(this).getUserId());
        rs.moveToFirst();

        String spo2Less = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_SPO2_LESS));
        String spo2More = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_SPO2_MORE));
        String hgbLess = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_HGB_LESS));
        String hgbMore = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_HGB_MORE));
        String bfvLess = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_BFV_LESS));
        String bfvMore = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_BFV_MORE));
        String pulseLess = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_PULSE_LESS));
        String pulseMore = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_PULSE_MORE));
        String gluMorningLess = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLU_MORNING_FASTING_LESS));
        String gluMorningMore = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLU_MORNING_FASTING_MORE));
        String gluAfterMealLess = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLU_AFTER_MEAL_LESS));
        String gluAfterMealMore = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_GLU_AFTER_MEAL_MORE));
        String email1 = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_EMAIL1));
        String email2 = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_EMAIL2));
        String email3 = rs.getString(rs.getColumnIndex(DbHelper.COLUMN_EMAIL3));
        if (!rs.isClosed()) {
            rs.close();
        }
        spo2LessEditText.setText(spo2Less);
        spo2MoreEditText.setText(spo2More);
        hgbMoreEditText.setText(hgbMore);
        hgbLessEditText.setText(hgbLess);
        bfvLessEditText.setText(bfvLess);
        bfvMoreEditText.setText(bfvMore);
        pulseLessEditText.setText(pulseLess);
        pulseMoreEditText.setText(pulseMore);
        gluMorningLessEditText.setText(gluMorningLess);
        gluMorningMoreEditText.setText(gluMorningMore);
        gluAfterMealLessEditText.setText(gluAfterMealLess);
        gluAfterMealMoreEditText.setText(gluAfterMealMore);
        email1EditText.setText(email1);
        email2EditText.setText(email2);
        email3EditText.setText(email3);
        checkResultCondition();
    }

    public void checkResultCondition() {
        cursor = DbHelper.getInstance(this).getTestRecord();
        if (cursor.moveToFirst()) {
            do {
                glucoseValue = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_GLUCOSEVALUE));
                pulseValue = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_PULSE));
                bloodFlowVelocity = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_BLOODFLOWSPEED));
                hemoglobinValue = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_HEAMOGLOBIN));
                oxygenSaturation = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_OXYGENSATURATION));
                date=cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_DATE));
                time=cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TEST_TIME));


                if (Double.parseDouble(oxygenSaturation) > Double.parseDouble(spo2MoreEditText.getText().toString())) {
                    spo2MoreEditText.setTextColor(getResources().getColor(R.color.black));
                } else if (Double.parseDouble(oxygenSaturation) < Double.parseDouble(spo2LessEditText.getText().toString())) {
                    spo2LessEditText.setTextColor(getResources().getColor(R.color.black));
                }
                if (Double.parseDouble(pulseValue) > Double.parseDouble(pulseMoreEditText.getText().toString())) {
                    pulseMoreEditText.setTextColor(getResources().getColor(R.color.black));
                } else if (Double.parseDouble(pulseValue) < Double.parseDouble(pulseLessEditText.getText().toString())) {
                    pulseLessEditText.setTextColor(getResources().getColor(R.color.black));
                }
                if (Double.parseDouble(bloodFlowVelocity) > Double.parseDouble(bfvMoreEditText.getText().toString())) {
                    bfvMoreEditText.setTextColor(getResources().getColor(R.color.black));
                } else if (Double.parseDouble(bloodFlowVelocity) < Double.parseDouble(bfvLessEditText.getText().toString())) {
                    bfvLessEditText.setTextColor(getResources().getColor(R.color.black));
                }

                Double hemoglobinConvertValue = Double.parseDouble(hemoglobinValue);
                hemoglobinConvertValue = hemoglobinConvertValue / 10;
                if (hemoglobinConvertValue > Double.parseDouble(hgbMoreEditText.getText().toString())) {
                    hgbMoreEditText.setTextColor(getResources().getColor(R.color.black));
                } else if (hemoglobinConvertValue < Double.parseDouble(hgbLessEditText.getText().toString())) {
                    hgbLessEditText.setTextColor(getResources().getColor(R.color.black));
                }
                float glucoseCovertValue = Float.parseFloat(glucoseValue);
                glucoseCovertValue = glucoseCovertValue / 10.0f;
                glucoseFinalValue = AppCommon.getMgDlFrommMol(glucoseCovertValue);
                glucoseFinal=String.format("%.1f",glucoseFinalValue);
                if (AppCommon.getInstance(this).getMeal() == 0) {
                    if (glucoseFinalValue > Double.parseDouble(gluMorningMoreEditText.getText().toString())) {
                        gluMorningMoreEditText.setTextColor(getResources().getColor(R.color.black));
                    } else if (glucoseFinalValue < Double.parseDouble(gluMorningLessEditText.getText().toString())) {
                        gluMorningLessEditText.setTextColor(getResources().getColor(R.color.black));
                    }
                } else {
                    if (glucoseFinalValue > Double.parseDouble(gluAfterMealMoreEditText.getText().toString())) {
                        gluAfterMealMoreEditText.setTextColor(getResources().getColor(R.color.black));
                    } else if (glucoseFinalValue < Double.parseDouble(gluAfterMealLessEditText.getText().toString())) {
                        gluAfterMealLessEditText.setTextColor(getResources().getColor(R.color.black));
                    }
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    201);

        } else {
            createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createPdf();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void createPdf() {
        Document document = new Document();
        String path = Environment.getExternalStorageDirectory() + "/Report.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font = FontFactory.getFont("assets/Font/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 11);
            document.add(new Paragraph(getResources().getString(R.string.pdfHello) + " " + name, font));
            document.add(new Paragraph(getResources().getString(R.string.emptySpace)));
            String mTimeSubString = "";
            int testTime = 0;
            if (time.length() == 4) {
               mTimeSubString = time.substring(0, 1);
               testTime = Integer.parseInt(mTimeSubString);
            } else {
                mTimeSubString = time.substring(0, 2);
                testTime = Integer.parseInt(mTimeSubString);
            }
            if(testTime<12) {
                document.add(new Paragraph(getResources().getString(R.string.pdfYourRecentMeasurement) + " " + date + " " + time+ "" + getResources().getString(R.string.amText), font));
            }else {
                document.add(new Paragraph(getResources().getString(R.string.pdfYourRecentMeasurement) + " " + date + " " + time+ "" + getResources().getString(R.string.pmText), font));
            }

            document.add(new Paragraph(getResources().getString(R.string.emptySpace)));
            document.add(new Paragraph(getResources().getString(R.string.pdfBloodGlucose) + glucoseFinal + " " + getResources().getString(R.string.mg), font));
            document.add(new Paragraph(getResources().getString(R.string.pdfOxygenSaturation) + " " + oxygenSaturation + "%", font));
            float globinValue = Float.valueOf(hemoglobinValue);
            globinValue = globinValue / 10;
            document.add(new Paragraph(getResources().getString(R.string.pdfHemoglobin) + " " + String.valueOf(globinValue) + "" + getResources().getString(R.string.g), font));
            document.add(new Paragraph(getResources().getString(R.string.pdfBloodFlow) + bloodFlowVelocity + " " + getResources().getString(R.string.au), font));
            document.add(new Paragraph(getResources().getString(R.string.pdfPulse) + "" + pulseValue + " " + getResources().getString(R.string.bpm), font));
            document.add(new Paragraph(getResources().getString(R.string.emptySpace)));
            document.add(new Paragraph(getResources().getString(R.string.pdfFixedText), font));
            document.close();
            File externalStorage = Environment.getExternalStorageDirectory();
           // Uri uri = Uri.fromFile(new File(externalStorage, "Report.pdf"));
            File imagePath = new File(path);
            Uri uri = FileProvider.getUriForFile(MeasurementSettingsActivity.this, "com.wellnessy.glucotracker.provider", imagePath);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType(getResources().getString(R.string.applicationPdf));
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email1EditText.getText().toString(), email2EditText.getText().toString(), email3EditText.getText().toString()});
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.healthAlert));
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.esserHealthCareTechnology));
            startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.pdfFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.saveUpdateButton)
    public void saveButtonClick() {
        if (checkError()) {
            DbHelper.getInstance(this).updateMeasurementSettingsDetails(AppCommon.getInstance(this).getUserId(), spo2MoreEditText.getText().toString(), spo2LessEditText.getText().toString(), hgbMoreEditText.getText().toString(), hgbLessEditText.getText().toString(),
                    bfvMoreEditText.getText().toString(), bfvLessEditText.getText().toString(), pulseMoreEditText.getText().toString(), pulseLessEditText.getText().toString(), gluMorningMoreEditText.getText().toString(),
                    gluMorningLessEditText.getText().toString(), gluAfterMealMoreEditText.getText().toString(), gluAfterMealLessEditText.getText().toString(), email1EditText.getText().toString(),
                    email2EditText.getText().toString(), email3EditText.getText().toString());
            Toast.makeText(this, getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();
        } else {
            AppCommon.showDialog(this, getResources().getString(R.string.enterCorrectDataInSettings));
        }
    }

    public Boolean checkError() {
        Boolean data = true;
        String spo2Less = spo2LessEditText.getText().toString().trim();
        String spo2More = spo2MoreEditText.getText().toString().trim();
        String hemoLess = hgbLessEditText.getText().toString().trim();
        String hemoMore = hgbMoreEditText.getText().toString().trim();
        String bfvMore = bfvMoreEditText.getText().toString().trim();
        String bfvLess = bfvLessEditText.getText().toString().trim();
        String pulseMore = pulseMoreEditText.getText().toString().trim();
        String pulseLess = pulseLessEditText.getText().toString().trim();
        String gluMoringMore = gluMorningMoreEditText.getText().toString().trim();
        String gluMorninLess = gluMorningLessEditText.getText().toString().trim();
        String gluAfterMealLess = gluAfterMealLessEditText.getText().toString().trim();
        String gluAfterMealMore = gluAfterMealMoreEditText.getText().toString().trim();

        if (!spo2Less.isEmpty() && !spo2More.isEmpty()) {
            if (Integer.parseInt(spo2Less) > Integer.parseInt(spo2More)) {
                data = false;
            }
        } else if (!spo2More.isEmpty() && !spo2Less.isEmpty()) {
            if (Integer.parseInt(spo2More) < Integer.parseInt(spo2Less)) {
                data = false;
            }
        } else if (!hemoLess.isEmpty() && !hemoMore.isEmpty()) {
            if (Integer.parseInt(hemoLess) > Integer.parseInt(hemoMore)) {

                data = false;
            }
        } else if (!hemoMore.isEmpty() && !hemoLess.isEmpty()) {
            if (Integer.parseInt(hemoMore) < Integer.parseInt(hemoLess)) {

                data = false;
            }
        } else if (!bfvLess.isEmpty() && !bfvMore.isEmpty()) {
            if (Integer.parseInt(bfvLess) > Integer.parseInt(bfvMore)) {

                data = false;
            }
        } else if (!bfvMore.isEmpty() && !bfvLess.isEmpty()) {
            if (Integer.parseInt(bfvMore) < Integer.parseInt(bfvLess)) {

                data = false;
            }
        } else if (!pulseLess.isEmpty() && !pulseMore.isEmpty()) {
            if (Integer.parseInt(pulseLess) > Integer.parseInt(pulseMore)) {

                data = false;
            }
        } else if (!pulseMore.isEmpty() && !pulseLess.isEmpty()) {
            if (Integer.parseInt(pulseMore) < Integer.parseInt(pulseLess)) {

                data = false;
            }
        } else if (!gluMorninLess.isEmpty() && !gluMoringMore.isEmpty()) {
            if (Integer.parseInt(gluMorninLess) > Integer.parseInt(gluMoringMore)) {

                data = false;
            }
        } else if (!gluMoringMore.isEmpty() && !gluMorninLess.isEmpty()) {
            if (Integer.parseInt(gluMoringMore) < Integer.parseInt(gluMorninLess)) {

                data = false;
            }
        } else if (!gluAfterMealLess.isEmpty() && !gluAfterMealMore.isEmpty()) {
            if (Integer.parseInt(gluAfterMealLess) > Integer.parseInt(gluAfterMealMore)) {

                data = false;
            }
        } else if (!gluAfterMealMore.isEmpty() && !gluAfterMealLess.isEmpty()) {
            if (Integer.parseInt(gluAfterMealMore) < Integer.parseInt(gluAfterMealLess)) {

                data = false;
            }
        }

        return data;
    }

    @OnClick(R.id.backIcon)
    public void backIconClick() {
        this.finish();
    }

    @OnClick(R.id.sendEmailButton)
    public void confirmUpdateButtonClick() {
        Cursor c = DbHelper.getInstance(this).getTestRecord();
        if (c.getCount() == 0) {
            AppCommon.showDialog(this, getResources().getString(R.string.couldnotSendMail));
        } else {
            requestPermission();
        }

    }
}
