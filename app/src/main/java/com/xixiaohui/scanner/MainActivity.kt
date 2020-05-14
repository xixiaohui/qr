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

//        initContinuous()

//        listenBottomNavigationBar()
    }


    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }
            lastText = result.text
            barcodeView!!.setStatusText(result.text)
//            beepManager!!.playBeepSoundAndVibrate()

            Toast.makeText(this@MainActivity, result.text, Toast.LENGTH_LONG).show()

            //Added preview of scanned barcode
//            val imageView =
//                findViewById<ImageView>(R.id.barcodePreview)
//            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))

//            gotoActivity(ScanActivity::class.java as Class<Activity>,result)
            addScanFragment()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
            Log.i("Tag", resultPoints.toString())
        }
    }


    fun addScanFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.scan_fragment, ScanFragment.newInstance(), MAIN)
        transaction.commit()
    }

    /**
     * 响应点击事件
     */
    fun clickFromImage(view: View?): Unit {
        Log.i("Tag", "clickFromImage")
    }

    private fun initContinuous(): Unit {
//        barcodeView = binding.barcodeScanner
        barcodeView = findViewById(R.id.barcode_scanner)
        if (barcodeView == null) {
            return
        }
        val formats: Collection<BarcodeFormat> =
            Arrays.asList(
                BarcodeFormat.QR_CODE,
                BarcodeFormat.CODE_39,
                BarcodeFormat.EAN_13
            )
        barcodeView!!.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView!!.initializeFromIntent(intent)
        barcodeView!!.decodeContinuous(callback)
        beepManager = BeepManager(this)

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

    fun scanBarcodeCustomLayout(view: View?) {
        integrator = IntentIntegrator(this)
        integrator.captureActivity = MainActivity::class.java
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Scan something")
        integrator.setOrientationLocked(false)
        integrator.setBeepEnabled(false)
        integrator.initiateScan()
    }


    fun addInfo(format: String, contents: String): Unit {
//        binding.info.text = contents
//        binding.format.text = format
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


    override fun onResume() {
        super.onResume()

//        barcodeView!!.resume()
    }

    override fun onPause() {
        super.onPause()

//        barcodeView!!.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
//        capture!!.onDestroy()
    }

    private fun gotoActivity(activity: Activity): Unit {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    private fun gotoActivity(cls: Class<Activity>, result: BarcodeResult): Unit {
        val intent = Intent(this, cls)
        intent.putExtra(DATA.TEXT.toString(), result.text)
        intent.putExtra(DATA.FORMAT.toString(), result.barcodeFormat.name)
        intent.putExtra(DATA.BITMAP.toString(), result.bitmap)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {

            }

        }
    }

}