package com.xixiaohui.scanner.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.activity.GenerateResultActivity
import com.xixiaohui.scanner.activity.GenerateString
import com.xixiaohui.scanner.activity.ScanActivity
import com.xixiaohui.scanner.databinding.FragmentGenerateMyqrBinding
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateMyqrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateMyqrFragment : Fragment(), GenerateString {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentGenerateMyqrBinding


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
        // Inflate the layout for this fragment

        binding = FragmentGenerateMyqrBinding.inflate(layoutInflater)

        binding.myqrGenerate.setOnClickListener {

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
         * @return A new instance of fragment GenerateMyqrFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateMyqrFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun assembleResult(): String {
        var result = "BEGIN:VCARD" + "\n"
        result += "N:" + binding.myqrFirstName.text.toString() + ";" + binding.myqrLastName.text.toString() + ";" + "\n"
        result += "TEL;TYPE=work,VOICE:" + binding.myqrMobile.text.toString() + "\n"
        result += "TEL;TYPE=home,VOICE:" + binding.myqrPhone.text.toString() + "\n"
        result += "TEL;TYPE=fax:" + binding.myqrFax.text.toString() + "\n"
        result += "EMAIL:" + binding.myqrEmail.text.toString() + "\n"
        result += "ORG:" + binding.myqrCompany.text.toString() + "\n"
        result += "TITLE:" + binding.myqrYourJob.text.toString() + "\n"
        result += "ADR;TYPE=WORK,PREF:;;" + binding.myqrStreet.text.toString() + "\n"
        result += "URL:" + binding.myqrWebsite.text.toString() + "\n"
        result += "VERSION:3.0" + "\n"
        result += "END:VCARD"
        return result
    }

    override fun getFormat(): String {
        return "QR_CODE"
    }


}