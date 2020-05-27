package com.xixiaohui.scanner.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.datamatrix.encoder.SymbolShapeHint
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.databinding.ActivityGenerateResultBinding
import java.util.*


class GenerateResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateResultBinding

    enum class GenerateResultActivityData {
        RESULT,
        FORMAT
    }

    private lateinit var result: String
    private lateinit var format: String

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

        val contents = String(result.toByteArray(Charsets.UTF_8))
        binding.barcodeText.text = contents
        generateCodeByScannerInfo(contents, format = BarcodeFormat.valueOf(format))
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
            val hints: Map<EncodeHintType, Any> =
                mutableMapOf(
                    EncodeHintType.CHARACTER_SET to "utf-8",
                    EncodeHintType.DATA_MATRIX_SHAPE to SymbolShapeHint.FORCE_RECTANGLE
                )

            val bitmap = barcodeEncoder.encodeBitmap(
                contents,
                format, 900, 900, hints
            )
            val imageViewQrCode = binding.barcodePreview
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {

        }
    }
}