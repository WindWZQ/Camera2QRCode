package com.wzq.scanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.wzq.scan.ScanView;

public class MainActivity extends AppCompatActivity {

    private ScanView scanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        scanView = findViewById(R.id.sv_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        scanView.openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        scanView.closeCamera();
    }
}
