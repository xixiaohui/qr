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
import androidx.fragment.app.Fragment
import com.google.zxing.Result
import com.xixiaohui.scanner.*
import com.xixiaohui.scanner.activity.ScanActivity
import com.xixiaohui.scanner.utils.SpUtils

import kotlinx.android.synthetic.main.fragment_history.view.*
import java.text.SimpleDateFormat

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */

class MyItemRecyclerViewAdapter(
    private val values: List<Result>, val fragment: Fragment
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_history, parent, false)

        var holder = ViewHolder(view)



        return holder
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

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
                favoritesList.add(item)

            } else {
                holder.itemStar.setImageResource(R.drawable.baseline_star_border_black_48dp)
                holder.itemStar.tag = "unselect"
                favoritesList.remove(item)

            }
        }
        if (favoritesList.contains(item)){
            holder.itemStar.setImageResource(R.drawable.baseline_star_black_48dp)
            holder.itemStar.tag = "select"
        }

        //响应事件
        holder.contentView.setOnClickListener{
            val frag = fragment as HistoryFragment
            frag.gotoActivity(ScanActivity::class.java as Class<Activity>,item)
        }
        holder.itemImage.setOnClickListener{
            val frag = fragment as HistoryFragment
            frag.gotoActivity(ScanActivity::class.java as Class<Activity>,item)
        }

        holder.itemMore.setOnClickListener{
            SpUtils.remove(fragment.context,keyList[position])
            resultList.removeAt(position)
            notifyDataSetChanged()

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
