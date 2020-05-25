package com.xixiaohui.scanner.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.activity.GenerateActivity
import com.xixiaohui.scanner.databinding.FragmentGenerateEventBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateEventFragment : Fragment() {
    private val TAG = "tzbc"
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var calendar: Calendar? = null

    lateinit var binding:FragmentGenerateEventBinding
    lateinit var datePickerDialog:DatePickerDialog
    lateinit var timePickerDialog:TimePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        calendar = Calendar.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGenerateEventBinding.inflate(layoutInflater)

        binding.eventGenerate.setOnClickListener{
            showCalenderDialog()
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
         * @return A new instance of fragment GenerateEventFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateEventFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showCalenderDialog(){
        datePickerDialog = DatePickerDialog(context!!,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val calender =
                    year.toString() + "年" + (month + 1) + "月" + dayOfMonth + "日"
                Log.e(TAG, "calender : $calender")
                binding.inputStart.text = Editable.Factory.getInstance().newEditable(calender)

                showTimeDialog()
            },calendar!!.get(Calendar.YEAR),calendar!!.get(Calendar.MONTH),calendar!!.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }

    fun showTimeDialog() {
        timePickerDialog = TimePickerDialog(context!!,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var time = hourOfDay.toString() + "时" + minute + "分";
                Log.e(TAG, "time : " + time);
                binding.inputEnd.text = Editable.Factory.getInstance().newEditable(time)

            }, calendar!!.get(Calendar.HOUR_OF_DAY), calendar!!.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

}