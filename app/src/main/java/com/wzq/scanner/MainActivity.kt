package com.wzq.scanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wzq.scan.ui.ScanActivity
import com.wzq.scan.util.LogUtil
import com.wzq.scan.util.ScreenUtil

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        LogUtil.i(TAG, "width:${ScreenUtil.getWidth(this@MainActivity)}")
        LogUtil.i(TAG, "height:${ScreenUtil.getHeight(this@MainActivity)}")
        LogUtil.i(TAG, "realWidth:${ScreenUtil.getRealWidth(this@MainActivity)}")
        LogUtil.i(TAG, "realHeight:${ScreenUtil.getRealHeight(this@MainActivity)}")

        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
    }

}
