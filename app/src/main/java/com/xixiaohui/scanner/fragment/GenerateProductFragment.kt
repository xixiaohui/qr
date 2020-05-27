package com.xixiaohui.scanner.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.activity.GenerateString
import com.xixiaohui.scanner.databinding.FragmentGenerateProductBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateProductFragment : Fragment(), GenerateString {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGenerateProductBinding

    private var tips = mutableListOf<String>(
        "Ex:12345670",
        "Ex:5701234567899",
        "Ex:12345670",
        "Ex:012345678912",
        "Ex:123456ABC",
        "Ex:WIKIPEDIA",
        "Ex:code128",
        "Ex:00012345678905",
        "Ex:This is a PDF417",
        "Ex:12345678",
        "Ex:Deo free app",
        "Ex:www.amazon.com"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGenerateProductBinding.inflate(layoutInflater)

        binding.productGenerate.setOnClickListener {
            val intent = createIntent(activity!!.baseContext)
            startActivity(intent)
        }

        binding.formatTips.text = tips[0]

        binding.productType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.formatTips.text = tips[position]
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GenerateProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun assembleResult(): String {
        return binding.inputContentProduct.text.toString()
    }

    override fun getFormat(): String {
        return binding.productType.selectedItem.toString()
    }
}