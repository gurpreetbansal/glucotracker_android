package com.wellnessy.glucotracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import CustomControl.TextViewGothamBook;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DatePickerView extends LinearLayout {
    @BindView(R.id.fromDateTextView)
    TextViewGothamBook mFromDateTextView;

    @BindView(R.id.toDateTextView)
    TextViewGothamBook mToDateTextView;

    public DatePickerView(Context context) {
        super(context);
        intializeAllViews(context);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intializeAllViews(context);
    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intializeAllViews(context);
    }

    public void intializeAllViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.period_dialog_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fromDateTextView)
    public void setmFromDateTextView() {

    }

    @OnClick(R.id.toDateTextView)
    public void setmToDateTextView() {

    }
}
