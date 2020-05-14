package com.xixiaohui.scanner.activity

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xixiaohui.scanner.MainActivity
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.databinding.ActivityScanBinding

/**
 * 扫描详情页
 */

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_scan)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = this.getString(R.string.scan)
        this.supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            this.title = title
        }

//        val bitmap = intent.getParcelableExtra<Bitmap>(MainActivity.DATA.BITMAP.toString())
        val text = intent.getStringExtra(MainActivity.DATA.TEXT.toString())
        val format = intent.getStringExtra(MainActivity.DATA.FORMAT.toString())

        binding.barcodeText.text = text
//        binding.barcodePreview.setImageBitmap(bitmap)
        generateCodeByScannerInfo(text, format = BarcodeFormat.valueOf(format))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateCodeByScannerInfo(contents: String, format: BarcodeFormat): Unit {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(
                contents,
                format, 900, 900
            )
            val imageViewQrCode = binding.barcodePreview
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {

        }
    }

}