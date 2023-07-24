package com.docapp.frnt.ui.appo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.docapp.frnt.data.model.AppointmentDtl
import com.docapp.frnt.databinding.AppointmentViewBinding

class AppointmentAdapter(
    private var presList: List<AppointmentDtl>,
    private var layoutInflater: LayoutInflater,
    private val viewGroup: ViewGroup?
) : Adapter<AppointmentItem>() {
    private lateinit var appointmentViewBinding: AppointmentViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentItem {
        appointmentViewBinding = AppointmentViewBinding.inflate(layoutInflater, viewGroup, false)
        return AppointmentItem(appointmentViewBinding)
    }

    override fun getItemCount(): Int {
        return presList.size
    }

    override fun onBindViewHolder(holder: AppointmentItem, position: Int) {
        holder.setDetails(presList[position])
    }
}