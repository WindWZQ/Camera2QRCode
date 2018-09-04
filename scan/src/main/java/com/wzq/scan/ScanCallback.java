package com.wzq.scan;

public interface ScanCallback {

    void onSuccess(String content);

    void onFail(ScanErrorEnum scanErrorEnum);

}
