package com.xixiaohui.scanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xixiaohui.scanner.activity.FavoriteActivity
import com.xixiaohui.scanner.activity.GenerateActivity
import com.xixiaohui.scanner.activity.HistoryActivity
import com.xixiaohui.scanner.activity.SettingsActivity
import com.xixiaohui.scanner.databinding.ActivityMainBinding
import java.lang.Exception


class MainActivity : AppCompatActivity() {
//    private lateinit var codeScanner: CodeScanner

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0

    lateinit var binding: ActivityMainBinding

    lateinit var integrator: IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            MY_PERMISSIONS_REQUEST_READ_CONTACTS
        )

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_DENIED
        ) {
            Log.i("MainActivity", "Permission is not granted")
        }
//        scanner()
        binding.scan.setOnClickListener {
            startScan()
        }

        binding.generator.setOnClickListener {
            generateCodeByCustomeInfo()
        }
        listenBottomNavigationBar()

        supportActionBar!!.title = this.getString(R.string.main_title)

    }

    fun startScan() {
        integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan()
    }

    fun generateCodeByScannerInfo(contents:String,format: BarcodeFormat): Unit {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(
                contents,
                format, 900, 900
            )

            val imageViewQrCode = binding.imageView
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e:Exception) {

        }
    }


    fun addInfo(format:String,contents: String): Unit {
        binding.info.text = contents
        binding.format.text = format
    }

    fun generateCodeByCustomeInfo(contents:String = "hello~",format: BarcodeFormat=BarcodeFormat.QR_CODE){
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(
                contents,
                format, 900, 900
            )

            val imageViewQrCode = binding.imageView
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e:Exception) {

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                generateCodeByScannerInfo(result.getContents(),BarcodeFormat.valueOf(result.formatName))
                addInfo(result.contents,result.formatName)
                Log.i("Tag",result.formatName)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    fun scanner(): Unit {
//        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
//        codeScanner = CodeScanner(this, scannerView)
//        // Parameters (default values)
//        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
//        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
//        // ex. listOf(BarcodeFormat.QR_CODE)
//        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
//        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
//        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
//        codeScanner.isFlashEnabled = false // Whether to enable flash or not
//
//        // Callbacks
//        codeScanner.decodeCallback = DecodeCallback {
//            runOnUiThread {
////                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
//            }
//        }
//        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
//            runOnUiThread {
//                Toast.makeText(
//                    this, "Camera initialization error: ${it.message}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        scannerView.setOnClickListener {
//            codeScanner.startPreview()
//        }
    }

    override fun onResume() {
        super.onResume()
//        codeScanner.startPreview()
    }

    override fun onPause() {
//        codeScanner.releaseResources()
        super.onPause()
    }

    fun gotoActivity(activity:Activity): Unit {
        val intent = Intent(this,activity::class.java)
        startActivity(intent)
    }

     //底部导航切换
    private fun listenBottomNavigationBar(): Unit {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {it ->
            when(it.itemId){
                R.id.page_1 ->{
                    gotoActivity(GenerateActivity())
                    true
                }
                R.id.page_2 ->{
                    gotoActivity(HistoryActivity())
                    true
                }
                R.id.page_3 ->{
                    gotoActivity(FavoriteActivity())
                    true
                }
                R.id.page_4 ->{
                    gotoActivity(SettingsActivity())
                    true
                }
                else -> false

            }
        }
    }
}