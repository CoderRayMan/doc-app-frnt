package com.docapp.frnt.ui.appo

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.docapp.frnt.data.companions.Constants
import com.docapp.frnt.data.model.AppointmentDtl
import com.docapp.frnt.databinding.AppointmentViewBinding
import java.text.SimpleDateFormat
import java.util.*

class AppointmentItem(private var appointmentViewBinding: AppointmentViewBinding) :
    ViewHolder(appointmentViewBinding.root) {

    @SuppressLint("SetTextI18n")
    fun setDetails(appointmentDtl: AppointmentDtl) {
        appointmentViewBinding.appoId.text = "Appointment-Id : ${appointmentDtl.appoId}"
        appointmentViewBinding.appoDate.text = "Appointment-Date : ${
            SimpleDateFormat(
                Constants.DATE_DAY_DATE_DD_MM_YYYY,
                Locale.US
            ).format(Date.from(appointmentDtl.appointmentDate))
        }"
        appointmentViewBinding.appoDoctor.text = "Doctor : ${appointmentDtl.dctId}"
        appointmentViewBinding.appoTime.text = "Appointment-Time : ${
            SimpleDateFormat(
                "hh:mm aa",
                Locale.US
            ).format(Date.from(appointmentDtl.appointmentTime))
        }"
    }
}