package com.wzq.scan.core;

public interface ScanCallback {

    void onSuccess(String content);

    void onFail(ScanErrorEnum scanErrorEnum);

}
