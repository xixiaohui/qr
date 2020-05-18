package com.xixiaohui.scanner.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xixiaohui.scanner.MainActivity
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.databinding.ActivityScanBinding
import com.xixiaohui.scanner.resultList
import com.xixiaohui.scanner.utils.SpUtils

/**
 * 扫描详情页
 */
data class MyResult(val text: String, val format: String)

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

//      val bitmap = intent.getParcelableExtra<Bitmap>(MainActivity.DATA.BITMAP.toString())
//        val text = intent.getStringExtra(MainActivity.DATA.TEXT.toString())
//        val format = intent.getStringExtra(MainActivity.DATA.FORMAT.toString())
        val objString = intent.getStringExtra(MainActivity.DATA.OBJECT.toString())
        val gson = Gson()
        val result = gson.fromJson(objString, Result::class.java);

//        val myResult = MyResult(text, format)
        saveResult(result)

//        val myhisResult = SpUtils.getBean<MyResult>(baseContext, "TEST", MyResult::class.java)

        binding.barcodeText.text = result.text
//      binding.barcodePreview.setImageBitmap(bitmap)
        generateCodeByScannerInfo(result.text, format = result.barcodeFormat)
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

    private fun saveResult(myResult:Result){
        SpUtils.saveBean(baseContext, SpUtils.randomKey(), myResult)
        resultList.add(myResult)
    }

}