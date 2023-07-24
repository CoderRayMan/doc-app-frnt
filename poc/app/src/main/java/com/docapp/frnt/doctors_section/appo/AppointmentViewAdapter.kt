package com.docapp.frnt.doctors_section.appo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.docapp.frnt.R
import com.docapp.frnt.data.companions.Constants
import com.docapp.frnt.data.model.AppointmentDtl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentsAdapter(private val appointments: List<AppointmentDtl>) :
    Adapter<AppointmentsAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewPatientName: TextView =
            itemView.findViewById(R.id.textViewPatientName)
        private val textViewAppointmentTime: TextView =
            itemView.findViewById(R.id.textViewAppointmentTime)
        private val textViewAppointmentDate: TextView =
            itemView.findViewById(R.id.textViewAppointmentDate)
        private val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)

        fun bind(appointment: AppointmentDtl) {
            textViewPatientName.text = appointment.patientName
            textViewAppointmentDate.text = SimpleDateFormat(
                Constants.DATE_DAY_DATE_DD_MM_YYYY,
                Locale.US
            ).format(Date.from(appointment.appointmentDate))
            textViewAppointmentTime.text = SimpleDateFormat(
                "hh:mm aa",
                Locale.US
            ).format(Date.from(appointment.appointmentTime))
            textViewStatus.text = appointment.status
        }
    }
}