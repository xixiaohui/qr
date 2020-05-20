package com.xixiaohui.scanner.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.util.Range
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.zxing.Result
import com.xixiaohui.scanner.*
import com.xixiaohui.scanner.activity.ScanActivity
import com.xixiaohui.scanner.utils.MyResult
import com.xixiaohui.scanner.utils.SpUtils

import kotlinx.android.synthetic.main.fragment_history.view.*
import java.text.SimpleDateFormat

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */

class MyItemRecyclerViewAdapter(
    private var values: MutableList<MyResult>, private val fragment: Fragment
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>(), Filterable {

    var mFilteredArrayList: MutableList<MyResult> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_history, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myResult = values[position]
        val item = values[position].result ?: return

        val type = HistoryFragment.getType(item)
        holder.itemType.text = type.toString()
        holder.itemType.typeface = Typeface.defaultFromStyle(Typeface.BOLD)

        var date = SpUtils.getDate(item.timestamp)

//        var content = date+" "+ item.text + " " + item.barcodeFormat.toString()
        var content = "$date $item.text $item.barcodeFormat.toString()"
        if (content.length > 41) {
            content = content.substring(0..40) + "..."
        }

        holder.contentView.text = content

        var resourceId = HistoryFragment.getResourceImageByType(type)
        holder.itemImage.setImageResource(resourceId)

        holder.itemStar.setOnClickListener {
            if (holder.itemStar.tag == "unselect") {
                holder.itemStar.setImageResource(R.drawable.baseline_star_black_48dp)
                holder.itemStar.tag = "select"
                myResult.favorite = true

                val frag = fragment as HistoryFragment
                frag.refreshResult(myResult, position)
            } else {
                holder.itemStar.setImageResource(R.drawable.baseline_star_border_black_48dp)
                holder.itemStar.tag = "unselect"
                myResult.favorite = false

                val frag = fragment as HistoryFragment
                frag.refreshResult(myResult, position)
            }
        }

        if (values[position].favorite) {
            holder.itemStar.setImageResource(R.drawable.baseline_star_black_48dp)
            holder.itemStar.tag = "select"
        }

        //响应事件
        holder.contentView.setOnClickListener {
            val frag = fragment as HistoryFragment
            frag.gotoActivity(ScanActivity::class.java as Class<Activity>, item)
        }

        holder.itemImage.setOnClickListener {
            val frag = fragment as HistoryFragment
            frag.gotoActivity(ScanActivity::class.java as Class<Activity>, item)
        }

        holder.itemMore.setOnClickListener {
//            SpUtils.remove(fragment.context, keyList[position])
//            resultList.removeAt(position)
//            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val contentView: TextView = view.content
        val itemImage: ImageView = view.item_image
        val itemType: TextView = view.item_type
        val itemStar = view.item_star
        val itemMore = view.item_more

    }

    override fun getFilter(): Filter {
        return object : Filter() {

            // 执行过滤操作
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = FilterResults()
                for (item in values) {
                    if (item.favorite) {
                        mFilteredArrayList.add(item)
                    }
                }
                result.values = mFilteredArrayList
                return result
            }

            //把过滤后的值返回出来
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                values = results!!.values as MutableList<MyResult>
                notifyDataSetChanged()
            }
        }
    }

}
