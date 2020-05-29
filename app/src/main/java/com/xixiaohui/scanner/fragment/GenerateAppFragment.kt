package com.xixiaohui.scanner.fragment

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.activity.GenerateString
import com.xixiaohui.scanner.databinding.FragmentGenerateAppBinding
import kotlinx.android.synthetic.main.fragment_generate_app_item.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateAppFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateAppFragment : Fragment(), GenerateString {
    // TODO: Rename and change types of parameters
    private var columnCount = 1

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGenerateAppBinding

    private var data: MutableList<PackageInfo> = mutableListOf()
    private lateinit var pm: PackageManager

    private lateinit var myAdapter: MyAppScrollingAdapt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        getAppList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentGenerateAppBinding.inflate(layoutInflater)
        val view = binding.appList

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {

                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }


            }
        }
        myAdapter =
            MyAppScrollingAdapt(
                data, pm
            )
        binding.appList.adapter = myAdapter
        myAdapter.setOnItemClickListener(object :HistoryFragment.OnItemClickListener{
            override fun onItemClicked(position: Int, view: View, holder: RecyclerView.ViewHolder) {

                val holder = holder as AppScrollingViewHolder

                val packageName = holder.packageName.text.toString()

                val result = "market://details?id=$packageName"
                val intent = createIntent(activity!!.baseContext,result)
                startActivity(intent)
            }

        })
        binding.appList.setItemViewCacheSize(5000)


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
         * @return A new instance of fragment GenerateAppFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateAppFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getAppList(): Unit {
        pm = activity!!.packageManager

        val temp = pm.getInstalledPackages(0)

//        var temp:MutableList<PackageInfo> = mutableListOf()

        for (item in temp) {

            //系统自带的
//            if ((ApplicationInfo.FLAG_SYSTEM and item.applicationInfo.flags) !== 0) {
//                data.add(item)
//            }
            //用户安装的
            if ((ApplicationInfo.FLAG_SYSTEM and item.applicationInfo.flags) == 0) {
                data.add(item)
            }
        }
    }


    class MyAppScrollingAdapt(private var data: List<PackageInfo>, val pm: PackageManager) :
        RecyclerView.Adapter<AppScrollingViewHolder>() {


        private lateinit var mOnItemClickListener: HistoryFragment.OnItemClickListener


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppScrollingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_generate_app_item, parent, false)

            return AppScrollingViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: AppScrollingViewHolder, position: Int) {
            val app = data[position]

            holder.packageName.text = app.packageName
            holder.iconImage.setImageDrawable(app.applicationInfo.loadIcon(pm))

            val version = app.versionName
            holder.versionName.text = "Version:  $version"

            holder.countId.text = (position + 1).toString()

            holder.iconImage.setOnClickListener {
                mOnItemClickListener!!.onItemClicked(position, it, holder = holder)
            }
        }

        fun setOnItemClickListener(listener: HistoryFragment.OnItemClickListener) {
            mOnItemClickListener = listener
        }

    }

    class AppScrollingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var iconImage: ImageView = view.app_list_icon
        var packageName: TextView = view.app_list_name
        var versionName: TextView = view.app_list_version
        var countId: TextView = view.app_list_count_id
    }

    override fun assembleResult(): String {
        return ""
    }

    override fun getFormat(): String {
        return "QR_CODE"
    }

}