package com.wzq.scan.ui

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Rational
import android.util.Size
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import com.wzq.scan.R
import com.wzq.scan.code.CodeAnalyzer
import com.wzq.scan.core.ScanCallback
import com.wzq.scan.core.ScanErrorEnum
import com.wzq.scan.util.LogUtil
import com.wzq.scan.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        init()
    }

    private fun init() {
        val width = ScreenUtil.getWidth(this)
        val height = ScreenUtil.getHeight(this)

        // 预览设置
        val previewConfig = PreviewConfig.Builder()
//                .setTargetAspectRatio(Rational(height, width))
                .setTargetAspectRatio(Rational(20, 1))
                .setTargetResolution(Size(width, height))
                .setLensFacing(CameraX.LensFacing.BACK)
                .setTargetRotation(Surface.ROTATION_0)
                .build()

        val analyzerThread = HandlerThread("CodeAnalyzer").apply { start() }
        // 解析设置
        val analysisConfig = ImageAnalysisConfig.Builder()
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .setCallbackHandler(Handler(analyzerThread.looper))
                .build()

        val preview = Preview(previewConfig)
        val analysis = ImageAnalysis(analysisConfig)
        analysis.analyzer = CodeAnalyzer(callback)

        preview.onPreviewOutputUpdateListener = Preview.OnPreviewOutputUpdateListener { output -> tvPreview!!.surfaceTexture = output.surfaceTexture }
        CameraX.bindToLifecycle(this@ScanActivity, preview, analysis)
    }

    private val callback: ScanCallback = object : ScanCallback {
        override fun onSuccess(content: String?) {
            LogUtil.i(TAG, "onSuccess:$content")
        }

        override fun onFail(scanErrorEnum: ScanErrorEnum?) {
            LogUtil.i(TAG, "onFail:${scanErrorEnum?.reason}")
        }
    }

}
