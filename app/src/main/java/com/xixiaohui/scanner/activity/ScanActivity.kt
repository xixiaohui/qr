package com.xixiaohui.scanner.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.Result
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xixiaohui.scanner.*
import com.xixiaohui.scanner.QRCodeDecoder.HINTS
import com.xixiaohui.scanner.databinding.ActivityScanBinding
import com.xixiaohui.scanner.utils.MyResult
import com.xixiaohui.scanner.utils.SpUtils
import java.util.*


/**
 * 扫描详情页
 */
//data class MyResult(val text: String, val format: String)

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = this.getString(R.string.scan)
        this.supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            this.title = title
        }

        var from = intent.getStringExtra(MainActivity.DATA.FROM.toString())
        val objString = intent.getStringExtra(MainActivity.DATA.OBJECT.toString())
        val gson = Gson()
        val result = gson.fromJson(objString, Result::class.java);

        if (from == null) {
            saveResult(result)
        }

        val contents = String(result.text.toByteArray(Charsets.UTF_8))
        binding.barcodeText.text = contents
        generateCodeByScannerInfo(contents, format = result.barcodeFormat)
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
            var hints:Map<EncodeHintType, String> = mutableMapOf(EncodeHintType.CHARACTER_SET to "utf-8")

            val bitmap = barcodeEncoder.encodeBitmap(
                contents,
                format, 900, 900, hints
            )
            val imageViewQrCode = binding.barcodePreview
            imageViewQrCode.setImageBitmap(bitmap)

        } catch (e: Exception) {

        }
    }

    private fun saveResult(myResult: Result) {
        var key = SpUtils.randomKey()
        val saveResult = MyResult(myResult)
        SpUtils.saveBean(baseContext, key, saveResult)
        resultList.add(saveResult)
        keyList.add(key)
    }

}