package com.xixiaohui.scanner.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.VisibleForTesting
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.databinding.ActivityGenerateResultBinding


class GenerateResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateResultBinding
    enum class GenerateResultActivityData {
        RESULT,
        FORMAT
    }
    private lateinit var result:String
    private lateinit var format:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_generate_result)
        binding = ActivityGenerateResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = this.getString(R.string.generate)
        this.supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            this.title = title
        }

        result = intent.getStringExtra(GenerateResultActivityData.RESULT.toString())
        format = intent.getStringExtra(GenerateResultActivityData.FORMAT.toString())

        binding.barcodeText.text = result
        generateCodeByScannerInfo(result, format = BarcodeFormat.valueOf(format))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                true
            }
            else->false
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