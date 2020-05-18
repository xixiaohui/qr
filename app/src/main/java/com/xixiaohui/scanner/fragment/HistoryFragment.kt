package com.xixiaohui.scanner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.resultList


/**
 * A fragment representing a list of Items.
 */
class HistoryFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter =
                    MyItemRecyclerViewAdapter(
                        resultList
                    )
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }


        fun getType(result: Result): CodeType {

            if (result.barcodeFormat.toString() == BarcodeFormat.EAN_13.toString()){
                return CodeType.PRODUCT
            }

            var text = result.text
            if(text.startsWith("MECARD:")){
                return CodeType.MECARD
            }else if(text.startsWith("mailto:")){
                return CodeType.EMAIL
            }else if(text.startsWith("URLTO:")){
                return CodeType.URL
            }else if(text.startsWith("smsto:")){
                return CodeType.SMS
            }else if(text.startsWith("geo:")){
                return CodeType.GEO
            }else if(text.startsWith("tel:")){
                return CodeType.PHONE
            }else if(text.startsWith("WIFI:")){
                return CodeType.WIFI
            }else if(text.startsWith("market:")){
                return CodeType.APP
            }else if(text.startsWith("BEGIN:VEVENT")){
                return CodeType.CALENDAR
            }else{
                return CodeType.TEXT
            }
        }

        fun getResourceImageByType(type:CodeType):Int{
            return when(type){
                CodeType.URL-> R.drawable.baseline_link_black_48dp
                CodeType.EMAIL-> R.drawable.baseline_email_black_48dp
                CodeType.PHONE-> R.drawable.baseline_phone_black_48dp
                CodeType.MECARD-> R.drawable.baseline_perm_contact_calendar_black_48dp
                CodeType.SMS-> R.drawable.baseline_sms_black_48dp
                CodeType.GEO-> R.drawable.baseline_location_on_black_48dp
                CodeType.WIFI-> R.drawable.baseline_wifi_black_48dp
                CodeType.APP-> R.drawable.baseline_apps_black_48dp
                CodeType.CALENDAR-> R.drawable.baseline_today_black_48dp
                CodeType.PRODUCT-> R.drawable.baseline_local_grocery_store_black_48dp
                else -> R.drawable.baseline_text_fields_black_48dp
            }
        }
        fun getResourceImage(result: Result):Int{
            return getResourceImageByType(getType(result))
        }
    }
    enum class CodeType{
        URL,
        TEXT,
        EMAIL,
        PHONE,
        MECARD,
        SMS,
        GEO,
        WIFI,
        APP,
        CALENDAR,
        PRODUCT
    }


}