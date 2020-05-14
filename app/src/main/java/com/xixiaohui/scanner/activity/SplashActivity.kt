package com.xixiaohui.scanner.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.xixiaohui.scanner.MainActivity
import com.xixiaohui.scanner.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        scanToolbar(null)

        supportActionBar!!.title = this.resources.getString(R.string.splash)
    }

    fun scanCustomScanner(view: View?) {
        IntentIntegrator(this).setOrientationLocked(true)
            .setCaptureActivity(MainActivity::class.java).initiateScan()
    }

    fun scanToolbar(view: View?) {
        IntentIntegrator(this).setCaptureActivity(MainActivity::class.java).initiateScan()
    }
}