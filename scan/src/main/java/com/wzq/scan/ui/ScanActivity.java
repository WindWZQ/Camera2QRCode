package com.wzq.scan.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wzq.scan.R;

public class ScanActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        init();
    }

    private void init() {

    }

}
