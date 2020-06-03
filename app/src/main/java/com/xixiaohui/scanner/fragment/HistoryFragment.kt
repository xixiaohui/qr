package com.xixiaohui.scanner.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.xixiaohui.scanner.MainActivity
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.activity.FavoriteActivityTag
import com.xixiaohui.scanner.databinding.FragmentHistoryBinding
import com.xixiaohui.scanner.databinding.FragmentHistoryListBinding
import com.xixiaohui.scanner.keyList
import com.xixiaohui.scanner.resultList
import com.xixiaohui.scanner.utils.MyResult
import com.xixiaohui.scanner.utils.SpUtils
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


/**
 * A fragment representing a list of Items.
 */
class HistoryFragment : Fragment() {

    private var columnCount = 1

    lateinit var binding: FragmentHistoryListBinding

    private lateinit var data: MutableList<MyResult>

    private lateinit var myTag: String

    private fun selector(p: MyResult): Long = p.result.timestamp

    private var mycontext:Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            myTag = it.getString(TAG).toString()
        }

        data = resultList

//        data.sortByDescending {
//            selector(it)
//        }

        mycontext = this.context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryListBinding.inflate(layoutInflater)
        val view = binding.list

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter =
                    MyItemRecyclerViewAdapter(
                        data, this@HistoryFragment
                    )

                if (myTag == FavoriteActivityTag) {
                    val myad = adapter as MyItemRecyclerViewAdapter
                    myad.filter.filter(data.toString())
                }

            }
        }
        binding.list.setItemViewCacheSize(5000)

        ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(binding.list)

//        binding.list.addOnItemClickListener(object : OnItemClickListener {
//            override fun onItemClicked(position: Int, view: View, holder: RecyclerView.ViewHolder) {
//                Toast.makeText(context, "position" + position, Toast.LENGTH_LONG).show()
//                var holder = holder as MyItemRecyclerViewAdapter.ViewHolder
//            }
//        })


        return binding.root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val DATA = "DATA"
        const val TAG = "TAG"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, data: MutableList<MyResult>, tag: String): Fragment {

            val fragment = HistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(TAG, tag)
                }
            }
            return fragment
        }

        fun getType(result: Result): CodeType {

            if (result.barcodeFormat.toString() == BarcodeFormat.EAN_13.toString()) {
                return CodeType.PRODUCT
            }

            var text = result.text.toLowerCase()
            if (text.startsWith("mecard:") || text.startsWith("vcard:")) {
                return CodeType.CONTACT
            } else if (text.startsWith("mailto:")||text.startsWith("matmsg:to:")) {
                return CodeType.EMAIL
            } else if (text.startsWith("rulto:")) {
                return CodeType.URL
            } else if (text.startsWith("smsto:")) {
                return CodeType.SMS
            } else if (text.startsWith("geo:")) {
                return CodeType.GEO
            } else if (text.startsWith("tel:")) {
                return CodeType.PHONE
            } else if (text.startsWith("wifi:")) {
                return CodeType.WIFI
            } else if (text.startsWith("market:")) {
                return CodeType.APP
            } else if (text.startsWith("begin:vevent")) {
                return CodeType.CALENDAR
            } else if (text.contains("www") || text.contains("http")) {
                return CodeType.URL
            } else {
                return CodeType.TEXT
            }
        }

        fun getResourceImageByType(type: CodeType): Int {
            return when (type) {
                CodeType.URL -> R.drawable.baseline_link_black_48dp
                CodeType.EMAIL -> R.drawable.baseline_email_black_48dp
                CodeType.PHONE -> R.drawable.baseline_phone_black_48dp
                CodeType.CONTACT -> R.drawable.baseline_perm_contact_calendar_black_48dp
                CodeType.SMS -> R.drawable.baseline_sms_black_48dp
                CodeType.GEO -> R.drawable.baseline_location_on_black_48dp
                CodeType.WIFI -> R.drawable.baseline_wifi_black_48dp
                CodeType.APP -> R.drawable.baseline_apps_black_48dp
                CodeType.CALENDAR -> R.drawable.baseline_today_black_48dp
                CodeType.PRODUCT -> R.drawable.baseline_local_grocery_store_black_48dp
                else -> R.drawable.baseline_text_fields_black_48dp
            }
        }

        fun getResourceImage(result: Result): Int {
            return getResourceImageByType(getType(result))
        }
    }

    enum class CodeType {
        URL,
        TEXT,
        EMAIL,
        PHONE,
        CONTACT,
        SMS,
        GEO,
        WIFI,
        APP,
        CALENDAR,
        PRODUCT
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View, holder: RecyclerView.ViewHolder)
    }

    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {


        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view?.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view, holder)
                }
            }
        })
    }


    fun gotoActivity(cls: Class<Activity>, result: Result): Unit {
        val intent = Intent(this.context, cls)

        val mygson = Gson()
        val objString = mygson.toJson(result)

        intent.putExtra(MainActivity.DATA.OBJECT.toString(), objString)
        intent.putExtra(MainActivity.DATA.FROM.toString(), "NotFreshCode")
        startActivity(intent)
    }

    fun refreshResult(myResult: MyResult, position: Int) {
        var key = keyList[position]
        SpUtils.saveBean(this.context, key, myResult)
    }


    private val itemTouchHelperCallBack: SimpleCallback = object : SimpleCallback(0, ItemTouchHelper.LEFT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//            data.removeAt(viewHolder.adapterPosition)
//            val beforeallRecord = SpUtils.getAllRecode(mycontext)

            var result:MyResult = data[viewHolder.adapterPosition]
            data.remove(result)

            SpUtils.remove(mycontext, keyList[viewHolder.adapterPosition])

//            val afterallRecord = SpUtils.getAllRecode(mycontext)


            val view = binding.list
            with(view) {
                adapter!!.notifyDataSetChanged()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}