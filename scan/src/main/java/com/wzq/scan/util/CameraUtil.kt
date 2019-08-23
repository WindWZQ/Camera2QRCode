package com.wzq.scan.util

import android.hardware.Camera

object CameraUtil {

    /**
     * preview width:1920,height:1080
     * preview width:1440,height:1080
     * preview width:1280,height:720
     * preview width:1056,height:864
     * preview width:960,height:720
     * preview width:800,height:480
     * preview width:720,height:480
     * preview width:640,height:480
     * preview width:352,height:288
     * preview width:320,height:240
     * preview width:176,height:144
     *
     * picture width:4128,height:3096
     * picture width:4128,height:2322
     * picture width:3264,height:2448
     * picture width:3264,height:1836
     * picture width:2560,height:1920
     * picture width:2048,height:1536
     * picture width:2048,height:1152
     * picture width:1920,height:1080
     * picture width:1280,height:960
     * picture width:1280,height:720
     * picture width:640,height:480
     */

    fun supportSize() {
        val camera = Camera.open()
        val parameters = camera.parameters
        val supportedPreviewSizes = parameters.supportedPreviewSizes
        val supportedPictureSizes = parameters.supportedPictureSizes

        for (size in supportedPreviewSizes) {
            LogUtil.i("CameraUtil", "preview width:${size.width},height:${size.height}")
        }

        for (size in supportedPictureSizes) {
            LogUtil.i("CameraUtil", "picture width:${size.width},height:${size.height}")

        }
    }

}
