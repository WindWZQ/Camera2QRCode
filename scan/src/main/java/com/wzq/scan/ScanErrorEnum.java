package com.wzq.scan;

public enum ScanErrorEnum {
    CAMERA_OPEN_FAIL(0, "");

    private int id;
    private String reason;

    ScanErrorEnum(int id, String reason) {
        this.reason = reason;
    }

    public int getId() {
        return this.id;
    }

    public String getReason() {
        return this.reason;
    }

}
