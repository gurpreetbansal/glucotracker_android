package com.wellnessy.glucotracker;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductBasisActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_basis);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.backIcon)
    public void backClick(){
        this.finish();
    }
}
