package com.xixiaohui.scanner.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.camera.CameraInstance
import com.xixiaohui.scanner.MainActivity
import com.xixiaohui.scanner.QRCodeDecoder
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.activity.*
import com.xixiaohui.scanner.databinding.FragmentMainBinding
import java.lang.ref.WeakReference
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
const val MAIN = "MAIN"

class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMainBinding

    private lateinit var caIns: CameraInstance

    private var torchOn = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        binding = FragmentMainBinding.inflate(layoutInflater)
        initContinuous()
        listenBottomNavigationBar()

        caIns = CameraInstance(context)

        binding.fromImage.setOnClickListener {
            gotoPhotoAlbum()
        }
        binding.fromImage2.setOnClickListener {
            startLight(null)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null
    private var lastText: String? = null

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
//            if (result.text == null || result.text == lastText) {
//                // Prevent duplicate scans
//                return
//            }
//            lastText = result.text
//            barcodeView!!.setStatusText(result.text)
//            beepManager!!.playBeepSoundAndVibrate()

//            Toast.makeText(context, result.text, Toast.LENGTH_LONG).show()

            //Added preview of scanned barcode
//            val imageView =
//                findViewById<ImageView>(R.id.barcodePreview)
//            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))

            gotoActivity(ScanActivity::class.java as Class<Activity>, result.result)

        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
//            Log.i("Tag", resultPoints.toString())
        }
    }


    private fun initContinuous(): Unit {
        barcodeView = binding.barcodeScanner

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
        barcodeView!!.initializeFromIntent(this.activity!!.intent)
        barcodeView!!.decodeContinuous(callback)
        beepManager = BeepManager(this.activity)
    }

    private fun gotoActivity(cls: Class<Activity>): Unit {
        val intent = Intent(this.activity, cls)
        startActivity(intent)
    }

    private fun gotoActivity(cls: Class<Activity>, result: Result): Unit {
        val intent = Intent(this.activity, cls)

        val mygson = Gson()
        val objString = mygson.toJson(result)

        intent.putExtra(MainActivity.DATA.OBJECT.toString(),objString)
//        intent.putExtra(MainActivity.DATA.TEXT.toString(), result.text)
//        intent.putExtra(MainActivity.DATA.FORMAT.toString(), result.barcodeFormat.name)

        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()

        barcodeView!!.resume()
    }

    override fun onPause() {
        super.onPause()

        barcodeView!!.pause()
    }

    //底部导航切换
    private fun listenBottomNavigationBar(): Unit {
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.page_1 -> {
                    gotoActivity(GenerateActivity::class.java as Class<Activity>)
                    true
                }
                R.id.page_2 -> {
                    gotoActivity(HistoryActivity::class.java as Class<Activity>)
                    true
                }
                R.id.page_3 -> {
                    gotoActivity(FavoriteActivity::class.java as Class<Activity>)
                    true
                }
                R.id.page_4 -> {
                    gotoActivity(SettingsActivity::class.java as Class<Activity>)
                    true
                }
                else -> false
            }
        }
    }


    // 开启闪光灯
    private fun startLight(view: View?): Unit {

        torchOn = !torchOn
        if (torchOn) {
            barcodeView!!.setTorchOn()
        } else {
            barcodeView!!.setTorchOff()
        }
    }

    //跳转相册
    fun gotoPhotoAlbum(): Unit {
        val intent = Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        val wrapperIntent = Intent.createChooser(intent, "please choose qr code")
        startActivityForResult(wrapperIntent, REQUEST_CODE)
    }

    var uri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            uri = data!!.data

            //获取选取的图片的绝对地址
            val photoPath: String? = getRealFilePath(context!!, uri!!)
            if (photoPath == null) {
                Toast.makeText(context, "get path fail!", Toast.LENGTH_SHORT).show();
            } else {
                //解析图片
                parsePhoto(photoPath);
            }
        }
    }

    /**
     * 获取选取的图片的绝对地址
     * @param c
     * @param uri
     * @return
     */
    private fun getRealFilePath(c: Context, uri: Uri): String? {
        var filePath: String? = null
        var wholeID: String? = null

        wholeID = DocumentsContract.getDocumentId(uri)

        // 使用':'分割
        val id = wholeID.split(":".toRegex()).toTypedArray()[1]

        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media._ID + "=?"
        val selectionArgs = arrayOf(id)

        val cursor = context!!.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, null
        )
        val columnIndex = cursor!!.getColumnIndex(projection[0])

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }

    /**
     * 解析图片
     */
    private fun parsePhoto(path: String?): Unit {
        QrCodeAsyncTask(this, path!!).execute(path)
    }

    /**
     * AsyncTask 静态内部类，防止内存泄漏
     */
    internal class QrCodeAsyncTask(fragment: Fragment?, path: String) :
        AsyncTask<String?, Int?, Result?>() {
        private val mWeakReference: WeakReference<Fragment> = WeakReference(fragment!!)
        private val path: String = path

        protected override fun doInBackground(vararg strings: String?): Result {
            // 解析二维码/条码
            return QRCodeDecoder.syncDecodeQRCode(path);
        }

        protected override fun onPostExecute(s: Result?) {
            super.onPostExecute(s)
            //识别出图片二维码/条码，内容为s
            (mWeakReference.get() as MainFragment)?.handleQrCode(s)
        }

    }

    /**
     * 处理图片二维码解析的数据
     * @param s
     */
    fun handleQrCode(s: Result?) {
        if (null == s) {
            Toast.makeText(context, "图片格式有误", Toast.LENGTH_SHORT).show()
        } else {
            // 识别出图片二维码/条码，内容为s
            handleResult(s)
        }
    }

    private fun handleResult(result: Result) {
        gotoActivity(ScanActivity::class.java as Class<Activity>, result)
    }
}