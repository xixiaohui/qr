package com.xixiaohui.scanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.google.zxing.client.android.BeepManager
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.xixiaohui.scanner.databinding.ActivityMainBinding
import com.xixiaohui.scanner.fragment.HistoryFragment
import com.xixiaohui.scanner.fragment.MAIN
import com.xixiaohui.scanner.fragment.MainFragment
import com.xixiaohui.scanner.utils.MyResult
import com.xixiaohui.scanner.utils.SpUtils

//所有的扫描结果
var resultList: MutableList<MyResult> = mutableListOf()
//所有的key
var keyList: MutableList<String> = mutableListOf()


class MainActivity : AppCompatActivity() {
//    private lateinit var codeScanner: CodeScanner

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1

    lateinit var binding: ActivityMainBinding

    enum class DATA {
        OBJECT,
        FROM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA

                ), MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
        }

        supportActionBar!!.title = this.getString(R.string.main_title)

        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.main_fragment, MainFragment.newInstance("", ""), MAIN)
        trans.commit()

        initResultList()
    }

    fun generateCodeByCustomeInfo(
        contents: String = "hello~",
        format: BarcodeFormat = BarcodeFormat.QR_CODE
    ) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(
                contents,
                format, 900, 900
            )

//            val imageViewQrCode = binding.imageView
//            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {

        }
    }

    private fun initResultList(): Unit {
        val allRecord = SpUtils.getAllRecode(this)
        val gson = Gson()
//        return gson.fromJson(objString, clazz)
        if(allRecord == null){
            return
        }
        for ((k, v) in allRecord) {

            val result = gson.fromJson(v as String, MyResult::class.java)
            resultList.add(result)
            keyList.add(k)
        }
//        println(resultList)
    }




}