package com.wzq.scan.code

import android.text.TextUtils
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.wzq.scan.core.ScanCallback
import com.wzq.scan.util.LogUtil
import com.yanzhenjie.zbar.Image
import com.yanzhenjie.zbar.ImageScanner


abstract class CodeAnalyzer(callback: ScanCallback) : ImageAnalysis.Analyzer {
    private val TAG = this.javaClass.simpleName
    private val mImageScanner = ImageScanner()

    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val data = ByteArray(buffer.remaining())
        val height = image.height
        val width = image.width
        buffer.get(data)

        val barcode = Image(height, width, "Y800")
        barcode.setData(data)
        // 设置截取区域，也就是你的扫描框在图片上的区域.
        // barcode.setCrop(startX, startY, width, height);

        var qrCodeString: String? = null

        val result = mImageScanner.scanImage(barcode)
        if (result != 0) {
            val symSet = mImageScanner.getResults()
            for (sym in symSet)
                qrCodeString = sym.getData()
        }

        LogUtil.i(TAG, "codeString:$qrCodeString")
        if (!TextUtils.isEmpty(qrCodeString)) {
            // 非空表示识别出结果了。
        }
    }

}
