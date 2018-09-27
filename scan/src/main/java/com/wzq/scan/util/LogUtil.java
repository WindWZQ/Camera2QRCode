package com.wzq.scan.util;

import android.util.Log;

import com.wzq.scan.core.ScanConstant;

public class LogUtil {

    public static void e(String TAG, String content) {
        if (ScanConstant.debug) {
            Log.e(TAG, content);
        }
    }

    public static void i(String TAG, String content) {
        if (ScanConstant.debug) {
            Log.i(TAG, content);
        }
    }

}
