package com.xixiaohui.scanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.google.zxing.client.android.Intents.Scan.RESULT
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.*
import com.xixiaohui.scanner.activity.*
import com.xixiaohui.scanner.databinding.ActivityMainBinding
import com.xixiaohui.scanner.fragment.MAIN
import com.xixiaohui.scanner.fragment.MainFragment
import com.xixiaohui.scanner.fragment.ScanFragment
import java.util.*


class MainActivity : AppCompatActivity() {
//    private lateinit var codeScanner: CodeScanner

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1

    lateinit var binding: ActivityMainBinding

    lateinit var integrator: IntentIntegrator

    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null
    private var lastText: String? = null

    var fm: FragmentManager = supportFragmentManager
    var scanFragment = ScanFragment.newInstance()


    enum class DATA {
        TEXT,
        FORMAT,
        BITMAP
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
            ActivityCompat.requestPermissions(this,
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

}