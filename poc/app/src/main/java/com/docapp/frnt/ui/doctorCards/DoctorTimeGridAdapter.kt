package com.docapp.frnt.ui.doctorCards

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.activity.result.ActivityResultLauncher
import com.docapp.frnt.R
import com.docapp.frnt.data.companions.CommonFunctions.parseTimeString
import com.docapp.frnt.databinding.TimeButtonBinding
import java.time.LocalTime

class DoctorTimeGridAdapter(
    private var layoutInflater: LayoutInflater,
    private val timeList: List<String>,
    private val launcher: ActivityResultLauncher<Intent>,
    private val bookedTimes: Set<String>?,
    private val clickCallBack: ButtonClickCallBack,
    private val isToday: Boolean = false
) : BaseAdapter() {
    private lateinit var timeButtonBinding: TimeButtonBinding

    override fun getCount(): Int {
        return timeList.size
    }

    override fun getItem(position: Int): Any {
        return timeList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        timeButtonBinding = if (convertView == null) {

            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            TimeButtonBinding.inflate(layoutInflater, parent, false)
        } else {
            // Reuse the existing view
            TimeButtonBinding.bind(convertView)
        }
        timeButtonBinding.timeButton.text = timeList[position]
        val time = parseTimeString(timeList[position].uppercase())!!
        Log.i("inf", time.toString())
        if (bookedTimes?.contains(timeList[position]) == true || (isToday && time.isBefore(LocalTime.now())))
            timeButtonBinding.timeButton.isEnabled = false

        timeButtonBinding.timeButton.setOnClickListener {
            clickCallBack.setTime(timeList[position])
            val filePicker = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*" // Set the type of file to be selected, in this case all files
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            AlertDialog.Builder(layoutInflater.context)
                .setTitle("Do you want to add any reports/prescriptions for the booking?")
                .setPositiveButton(layoutInflater.context.getString(R.string.btn_upload)) { _: DialogInterface?, _: Int ->
                    launcher.launch(filePicker)
                }
                .setNegativeButton(layoutInflater.context.getString(R.string.btn_proceed)) { _: DialogInterface?, _: Int ->
                    clickCallBack.initiateBooking()
                }
                .show()
        }
        return timeButtonBinding.root
    }


}