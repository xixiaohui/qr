package com.xixiaohui.scanner.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xixiaohui.scanner.activity.GenerateString
import com.xixiaohui.scanner.databinding.FragmentGenerateEmailBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateEmailFragment : Fragment(), GenerateString {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentGenerateEmailBinding

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
        binding = FragmentGenerateEmailBinding.inflate(layoutInflater)

        binding.emailGenerate.setOnClickListener{

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
         * @return A new instance of fragment GenerateEmailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateEmailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun assembleResult(): String {
        var result = "MATMSG:TO:"+binding.inputEmail.text.toString()+";\n"
        result+= "SUB:" + binding.inputTitle.text.toString()+";\n"
        result+= "BODY:"+binding.inputContent.text.toString()+";;\n"

        if(binding.inputTitle.text.toString() == "" || binding.inputTitle.text.toString() == null){
            return "mailto:" + binding.inputEmail.text.toString()
        }

        return result
    }

    override fun getFormat(): String {
        return "QR_CODE"
    }

//    fun sendEmail(activity: Activity, `object`: EmailSendObject) {
//        val uri: Uri = Uri.parse("mailto:" + `object`.recipient)
//        val intent = Intent(Intent.ACTION_SENDTO, uri)
//        intent.putExtra(Intent.EXTRA_CC, `object`.cc)
//        intent.putExtra(Intent.EXTRA_SUBJECT, `object`.subject)
//        intent.putExtra(Intent.EXTRA_TEXT, `object`.content)
//        activity.startActivity(Intent.createChooser(intent, "Choose a email app!"))
//    }
}