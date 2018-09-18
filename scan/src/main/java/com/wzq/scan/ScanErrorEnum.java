package com.wzq.scan;

public enum ScanErrorEnum {
    CAMERA_NOT_SUPPORT(0, "不支持camera2"),
    CAMERA_OPEN_FAIL(1, "摄像头打开失败"),
    CAMERA_BACK_CAMERA_NOT_FOUND(2, "没找到后置摄像头"),
    CAMERA_CLOSE(3, "摄像头已关闭"),
    CAMERA_PREVIEW_FAIL(4, "摄像头预览失败"),
    CAMERA_UNKNOWN_FAIL(9, "未知错误");

    private int id;
    private String reason;

    ScanErrorEnum(int id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public int getId() {
        return this.id;
    }

    public String getReason() {
        return this.reason;
    }

}
