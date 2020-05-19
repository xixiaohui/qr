package com.xixiaohui.scanner.fragment

import android.app.Activity
import android.graphics.Typeface
import android.util.Range
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.Result
import com.xixiaohui.scanner.MainActivity
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.activity.ScanActivity
import com.xixiaohui.scanner.keyList
import com.xixiaohui.scanner.resultList
import com.xixiaohui.scanner.utils.SpUtils

import kotlinx.android.synthetic.main.fragment_history.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */

class MyItemRecyclerViewAdapter(
    private val values: List<Result>, val activity: Activity
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_history, parent, false)

        var holder = ViewHolder(view)

        holder.itemStar.setOnClickListener {
            if (holder.itemStar.tag == "select") {
                holder.itemStar.setImageResource(R.drawable.baseline_star_black_48dp)
                holder.itemStar.tag = "unselect"
            } else {
                holder.itemStar.setImageResource(R.drawable.baseline_star_border_black_48dp)
                holder.itemStar.tag = "select"
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        val type = HistoryFragment.getType(item)
        holder.itemType.text = type.toString()
        holder.itemType.typeface = Typeface.defaultFromStyle(Typeface.BOLD)

        var content = item.text + " " + item.barcodeFormat.toString()
        if (content.length > 41) {
            content = content.substring(0..40) + "..."
        }

        holder.contentView.text = content

        var resourceId = HistoryFragment.getResourceImageByType(type)
        holder.itemImage.setImageResource(resourceId)

        holder.contentView.setOnClickListener{
            MainActivity.gotoActivity(activity,ScanActivity().javaClass,values[position])
        }
        holder.itemType.setOnClickListener{
            MainActivity.gotoActivity(activity,ScanActivity().javaClass,values[position])
        }

        holder.itemMore.setOnClickListener{
            resultList.removeAt(position)
            notifyDataSetChanged()
            SpUtils.remove(activity,keyList[position])
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
}
