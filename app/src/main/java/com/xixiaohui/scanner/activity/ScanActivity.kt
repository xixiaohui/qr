package com.xixiaohui.scanner.activity

import android.Manifest
import android.app.Application
import android.app.SearchManager
import android.content.*
import android.content.pm.PackageManager
import android.media.Image
import android.net.MailTo
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.provider.Settings
import android.text.Layout
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.Result
import com.google.zxing.ResultMetadataType
import com.google.zxing.client.result.CalendarParsedResult
import com.google.zxing.client.result.URLTOResultParser
import com.google.zxing.client.result.VEventResultParser
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xixiaohui.scanner.*
import com.xixiaohui.scanner.QRCodeDecoder.HINTS
import com.xixiaohui.scanner.databinding.ActivityScanBinding
import com.xixiaohui.scanner.fragment.HistoryFragment
import com.xixiaohui.scanner.utils.MyResult
import com.xixiaohui.scanner.utils.SpUtils
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


/**
 * 扫描详情页
 */
//data class MyResult(val text: String, val format: String)

class ScanActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1

    private lateinit var binding: ActivityScanBinding

    private lateinit var type: HistoryFragment.CodeType

    private lateinit var extraOperation: ExtraOperation

    private lateinit var linearLayout: LinearLayout

    private lateinit var result: Result


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CONTACTS

                ), MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )
        }


        val title = this.getString(R.string.scan)
        this.supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            this.title = title
        }

        var from = intent.getStringExtra(MainActivity.DATA.FROM.toString())
        val objString = intent.getStringExtra(MainActivity.DATA.OBJECT.toString())
        val gson = Gson()
        result = gson.fromJson(objString, Result::class.java);

        if (from == null) {
            saveResult(result)
        }

        val contents = String(result.text.toByteArray(Charsets.UTF_8))
        binding.barcodeText.text = contents
        generateCodeByScannerInfo(contents, format = result.barcodeFormat)

        type = HistoryFragment.getType(result)
        binding.scanTypeImage.setImageDrawable(
            getDrawable(
                HistoryFragment.getResourceImageByType(
                    type
                )
            )
        )

        val time = SpUtils.getDate(result.timestamp)
        binding.scanTime.text = time

        if (result.resultMetadata != null) {
            val country = result.resultMetadata[ResultMetadataType.POSSIBLE_COUNTRY]
            if (country == null) {
                binding.scanProductCountry.text = " "
            } else {
                binding.scanProductCountry.text = country.toString()
            }
        }


        binding.scanCodeFormat.text = result.barcodeFormat.toString()

        linearLayout = binding.frameExtraOperations

        extraOperation =
            ExtraOperation(context = baseContext, codeType = type, linearLayout = linearLayout)
        extraOperation.doAction(result)
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
            var hints: Map<EncodeHintType, String> =
                mutableMapOf(EncodeHintType.CHARACTER_SET to "utf-8")

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

    class ExtraOperation(
        val context: Context,
        var codeType: HistoryFragment.CodeType,
        val linearLayout: LinearLayout
    ) {

        private fun getParams(): LinearLayout.LayoutParams {
            val metrics = context.resources.displayMetrics
            val dp = 50f
            val fpixels = metrics.density * dp
            val value = fpixels.roundToInt()

            val parmas = LinearLayout.LayoutParams(
                value,
                value
            )
            parmas.setMargins(value / 2, value, value / 2, value)
            return parmas
        }

        private fun addImageView(drawableLists: List<Int>): List<ImageView> {
            val params = getParams()
            val imageViewList = mutableListOf<ImageView>()
            for (ids in drawableLists) {
                val imageView = ImageView(linearLayout.context)
                imageView.setImageResource(ids)
                linearLayout.addView(imageView, params)
                imageViewList.add(imageView)
            }
            return imageViewList
        }

        private fun openUrl(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_open_in_browser_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            val urlString = result.text.toLowerCase().removePrefix("urlto:")

            var url = if (urlString.contains("http://") || urlString.contains("https://")) {
                urlString
            } else {
                "http://$urlString"
            }

            imageViewList[0].setOnClickListener {
                val uri: Uri = Uri.parse(url)
                val myIntent = Intent(Intent.ACTION_VIEW, uri)
                myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                context.startActivity(myIntent)
            }

            imageViewList[1].setOnClickListener {
                shareText(urlString)
            }

        }

        /**
         * 分享文本
         */
        private fun shareText(text: String) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share))
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(
                intent
            )
        }

        private fun copyToClipboardNoToast(text: String) {
            val clipData = ClipData.newPlainText("", text)
            val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            manager.setPrimaryClip(clipData)
        }

        private fun copyToClipboard(text: String) {
            val clipData = ClipData.newPlainText("", text)
            val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            manager.setPrimaryClip(clipData)

            Toast.makeText(context, "Copy:$text", Toast.LENGTH_SHORT).show()
        }

        private fun extraOperationText(result: Result) {


            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_file_copy_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            imageViewList[0].setOnClickListener {
                copyToClipboard(result.text)
                Toast.makeText(context, "Copy to clipboard.", Toast.LENGTH_SHORT).show()
            }
            imageViewList[1].setOnClickListener {
                shareText(result.text)
            }

        }


        private fun sendEmail(result: Result): Unit {
            val text = result.text.toLowerCase()

            if (text.startsWith("mailto:")) {
                val uri = Uri.parse(text)
                var intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
//                Toast.makeText(context, "sendEmail mailto ", Toast.LENGTH_SHORT).show()
            } else if (text.startsWith("matmsg:to:")) {

//                Log.i("matmsg:to:",result.text)//MATMSG:TO:Jn@163.com;SUB:Hj;BODY:Hopjbh;;

                val p: Pattern = Pattern.compile("\\s*|\t|\r|\n")
                val m: Matcher = p.matcher(text)
                val dest = m.replaceAll("")
                val strList = dest.removePrefix("matmsg:to:").split(";")
                val mailTo = "mailto:" + strList[0]
                val obj = strList[1]
                val mainText = strList[2]

                val uri = Uri.parse(mailTo)
                var intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                if (obj != null) {
                    Log.i("matmsg:to:", obj.removePrefix("sub:"))
                    intent.putExtra(Intent.EXTRA_SUBJECT, obj.removePrefix("sub:"))//主题
                }
                if (mainText != null) {
                    Log.i("matmsg:to:", mainText.removePrefix("body:"))
                    intent.putExtra(Intent.EXTRA_TEXT, mainText.removePrefix("body:")) //正文
                }

                context.startActivity(intent)
//                Toast.makeText(context, "sendEmail matmsg to ", Toast.LENGTH_SHORT).show()
            }
        }

        private fun extraOperationEmail(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_contact_mail_black_48dp,
                    R.drawable.baseline_file_copy_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )
            imageViewList[0].setOnClickListener {
                sendEmail(result)
            }

            imageViewList[1].setOnClickListener {
                copyToClipboard(result.text)
            }

            imageViewList[2].setOnClickListener {
                shareText(result.text)
            }
        }

        //跳转到拨号面板
        private fun callPhone(result: Result): Unit {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(result.text))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        private fun extraOperationPhone(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_contact_phone_black_48dp,
                    R.drawable.baseline_file_copy_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            imageViewList[0].setOnClickListener {
                callPhone(result)
            }

            imageViewList[1].setOnClickListener {
                val text = result.text.toLowerCase().removePrefix("tel:")
                copyToClipboard(text)
            }

            imageViewList[2].setOnClickListener {
                shareText(result.text)
            }
        }

        //跳转到拨号面板
        private fun callSMSPhone(num: String) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$num"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


        private fun addToContacts(phoneNumber: String) {

//            val values = ContentValues()
//            val rawContactUri = context.contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI,values)
//            var rawContactId = ContentUris.parseId(rawContactUri)
//            values.clear()
//
//            values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId)
//            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phoneNumber)
//            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
//            context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
//
//            values.clear();


        }

        //调用联系人界面
        private fun callContactInterface(phoneNumber: String) {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.setData(ContactsContract.Contacts.CONTENT_URI)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        private fun gotoContactsPage() {
            val intent = Intent()
            intent.action = "android.intent.action.PICK"
            intent.addCategory("android.intent.category.DEFAULT")
            intent.type = "vnd.android.cursor.dir/phone_v2"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        private fun extraOperationSMS(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_contact_phone_black_48dp,
                    R.drawable.baseline_file_copy_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            imageViewList[0].setOnClickListener {
                val text = result.text.toLowerCase().removePrefix("smsto:")
                val strList = text.split(":")
                if (strList[0] != null) {
                    callSMSPhone(strList[0])
                }
            }

            imageViewList[1].setOnClickListener {
                val text = result.text.toLowerCase().removePrefix("smsto:")
                copyToClipboard(text)
            }

            imageViewList[2].setOnClickListener {
                shareText(result.text)
            }
        }

        private fun showMap(geoLocation: Uri) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = geoLocation
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }

        private fun extraOperationGeo(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_directions_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            imageViewList[0].setOnClickListener {
                val uri = Uri.parse(result.text)
                Log.i("extraOperationGeo", result.text)

                showMap(uri)
            }

            imageViewList[1].setOnClickListener {
                shareText(result.text)
            }

        }

        private fun openWifiSettings() {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }


        private fun extraOperationWifi(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_wifi_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            imageViewList[0].setOnClickListener {
                openWifiSettings()
                copyToClipboardNoToast(result.text)
            }

            imageViewList[1].setOnClickListener {
                shareText(result.text)
            }
        }

        private fun searchWeb(query: String) {
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, query)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }

        private fun extraOperationApp(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_apps_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            imageViewList[0].setOnClickListener {
                val appPackageName = result.text.removePrefix("market://")
                val url = "https://play.google.com/store/apps/$appPackageName"
                searchWeb(url)
            }

            imageViewList[1].setOnClickListener {
                shareText(result.text)
            }
        }

        private fun addEvent(title: String, location: String, begin: Long, end: Long) {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }

        private fun extraOperationCalendar(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_today_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            imageViewList[0].setOnClickListener {

                val vevent: CalendarParsedResult = VEventResultParser().parse(result)
                addEvent(vevent.summary, vevent.location, vevent.start.time, vevent.end.time)
            }

            imageViewList[1].setOnClickListener {
                shareText(result.text)
            }
        }



        private fun extraOperationProduct(result: Result) {
            val imageViewList = addImageView(
                mutableListOf(
                    R.drawable.baseline_search_black_48dp,
                    R.drawable.baseline_share_black_48dp
                )
            )

            imageViewList[0].setOnClickListener {
                val num = result.text.toString()
                val url = "https://www.google.com/m/products?q=$num"
                searchWeb(url)
            }

            imageViewList[1].setOnClickListener {
                shareText(result.text)
            }
        }

        fun doAction(result: Result) {

            when (codeType) {
                HistoryFragment.CodeType.URL -> {
                    openUrl(result)
                }
                HistoryFragment.CodeType.TEXT -> {
                    extraOperationText(result)
                }
                HistoryFragment.CodeType.EMAIL -> {
                    extraOperationEmail(result)
                }
                HistoryFragment.CodeType.PHONE -> {
                    extraOperationPhone(result)
                }
//                HistoryFragment.CodeType.CONTACT -> {
//
//                }
                HistoryFragment.CodeType.SMS -> {
                    extraOperationSMS(result)
                }
                HistoryFragment.CodeType.GEO -> {
                    extraOperationGeo(result)
                }
                HistoryFragment.CodeType.WIFI -> {
                    extraOperationWifi(result)
                }
                HistoryFragment.CodeType.APP -> {
                    extraOperationApp(result)
                }
                HistoryFragment.CodeType.CALENDAR -> {
                    extraOperationCalendar(result)
                }
                HistoryFragment.CodeType.PRODUCT -> {
                    extraOperationProduct(result)
                }

            }

        }




    }


}