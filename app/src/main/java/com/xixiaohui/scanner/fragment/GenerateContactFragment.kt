package com.xixiaohui.scanner.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.activity.GenerateString
import com.xixiaohui.scanner.databinding.FragmentGenerateContactBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateContactFragment : Fragment(), GenerateString {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGenerateContactBinding

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
        binding = FragmentGenerateContactBinding.inflate(layoutInflater)

        binding.contactGenerate.setOnClickListener{
            val intent = createIntent(activity!!.baseContext)
            startActivity(intent)
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
         * @return A new instance of fragment GenerateContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun assembleResult(): String {
        var result = "MECARD:"
        result += "N:" + binding.inputLastName.text.toString()+","+binding.inputFirstName.text.toString()+";"
        result += "TEL:"+binding.inputPhone.text.toString()+";"
        result += "EMAIL:"+binding.inputEmail.text.toString()+";"
        result += "URL:"+binding.inputUrl.text.toString()+";;"

        return result
    }

    override fun getFormat(): String {
        return "QR_CODE"
    }
}