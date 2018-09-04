package com.wzq.scan;

import android.util.Log;

class LogUtil {

    static void e(String TAG, String content) {
        if (ScanConstant.debug) {
            Log.e(TAG, content);
        }
    }

    static void i(String TAG, String content) {
        if (ScanConstant.debug) {
            Log.i(TAG, content);
        }
    }

}
