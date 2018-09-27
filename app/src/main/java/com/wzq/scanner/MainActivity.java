package com.wzq.scanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wzq.scan.ScanCallback;
import com.wzq.scan.ScanErrorEnum;
import com.wzq.scan.ScanView;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private ScanView scanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        scanView = findViewById(R.id.sv_view);
        scanView.setCallback(new ScanCallback() {
            @Override
            public void onSuccess(String content) {
                logger(content);
            }

            @Override
            public void onFail(ScanErrorEnum scanErrorEnum) {
                logger(scanErrorEnum.toString());
            }
        });
        scanView.init();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        scanView.openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        scanView.closeCamera();
    }

    private void logger(String content) {
        Log.e(TAG, content);
    }

}
